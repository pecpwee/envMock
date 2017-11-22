package com.pecpwee.lib.envMock.utils;

import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2017/6/12.
 */

public class GsonFactory {

    public static Gson gsonInstance;

    public static class NoBundleGsonStrategy implements ExclusionStrategy {
        public boolean shouldSkipClass(Class<?> arg0) {
            return arg0 == Bundle.class;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }
    }

    private static class ScanResultStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {

            return f.getDeclaringClass().equals(ScanResult.class) &&
                    f.getDeclaredClass().equals(CharSequence.class);
            //不序列化除了以下名字field之外的其他字段.一是减小体积，二是防止出错
//            return f.getDeclaringClass().equals(ScanResult.class)
//                    && !f.getName().equals("BSSID")
//                    && !f.getName().equals("SSID")
//                    && !f.getName().equals("capabilities")
//                    && !f.getName().equals("frequency")
//                    && !f.getName().equals("level");


//            //get ride of bundle directly
//            return (f.getDeclaringClass() == Location.class && f.getName().equals("mExtras")) ||
//                    (f.getDeclaringClass() == OnStatusChanged.class && f.getName().equals("extras"));
        }
    }

    private static class ConnStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            boolean shouldSkip = false;
            if (f.getName().equals("mNetworkSpecifier")) {
                return true;
            }
            return shouldSkip;
        }
    }

    public static Gson getGson() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder()
                    .addSerializationExclusionStrategy(new ScanResultStrategy())
                    .addSerializationExclusionStrategy(new ConnStrategy())
                    .addDeserializationExclusionStrategy(new ScanResultStrategy())
                    .addDeserializationExclusionStrategy(new ConnStrategy())
                    .registerTypeAdapterFactory(new BundleTypeAdapterFactory())
//                    .addSerializationExclusionStrategy(new NoBundleGsonStrategy())
//                    .addDeserializationExclusionStrategy(new NoBundleGsonStrategy())
                    //.serializeNulls() <-- uncomment to serialize NULL fields as well
                    .create();
        }
        return gsonInstance;
    }

    /**
     * Type adapter for Android Bundle. It only stores the actual properties set in the bundle
     *
     * @author Inderjeet Singh
     */
    public static class BundleTypeAdapterFactory implements TypeAdapterFactory {

        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> type) {
            if (!Bundle.class.isAssignableFrom(type.getRawType())) {
                return null;
            }

            return (TypeAdapter<T>) new TypeAdapter<Bundle>() {
                @Override
                public void write(JsonWriter out, Bundle bundle) throws IOException {
                    if (bundle == null) {
                        out.nullValue();
                        return;
                    }
                    out.beginObject();
                    for (String key : bundle.keySet()) {
                        out.name(key);
                        Object value = bundle.get(key);
                        if (value == null) {
                            out.nullValue();
                        } else {
//                            LogUtils.d("type bundle write" + value);

                            gson.toJson(value, value.getClass(), out);
                        }
                    }
                    out.endObject();
                }

                @Override
                public Bundle read(JsonReader in) throws IOException {

                    switch (in.peek()) {
                        case NULL:
                            in.nextNull();
                            return null;
                        case BEGIN_OBJECT:
                            return toBundle(readObject(in));
                        default:
                            throw new IOException("expecting object: " + in.getPath());
                    }
                }

                private Bundle toBundle(List<Pair<String, Object>> values) throws IOException {
                    Bundle bundle = new Bundle();
                    for (Pair<String, Object> entry : values) {
                        String key = entry.first;
                        Object value = entry.second;
                        if (value instanceof String) {
                            bundle.putString(key, (String) value);
                        } else if (value instanceof Integer) {
//                            LogUtils.d("type bundle integer" + value);

                            bundle.putInt(key, ((Integer) value).intValue());
                        } else if (value instanceof Long) {
                            bundle.putLong(key, ((Long) value).longValue());
                        } else if (value instanceof Double) {
                            bundle.putDouble(key, ((Double) value).doubleValue());
                        } else if (value instanceof Parcelable) {
                            bundle.putParcelable(key, (Parcelable) value);
                        } else if (value instanceof List) {
                            List<Pair<String, Object>> objectValues = (List<Pair<String, Object>>) value;
                            Bundle subBundle = toBundle(objectValues);
                            bundle.putParcelable(key, subBundle);
                        } else {
                            throw new IOException("Unparcelable key, value: " + key + ", " + value);
                        }
                    }
                    return bundle;
                }

                private List<Pair<String, Object>> readObject(JsonReader in) throws IOException {
                    List<Pair<String, Object>> object = new ArrayList<Pair<String, Object>>();
                    in.beginObject();
                    while (in.peek() != JsonToken.END_OBJECT) {
                        switch (in.peek()) {
                            case NAME:
                                String name = in.nextName();
                                Object value = readValue(in);
                                object.add(new Pair<String, Object>(name, value));
                                break;
                            case END_OBJECT:
                                break;
                            default:
                                throw new IOException("expecting object: " + in.getPath());
                        }
                    }
                    in.endObject();
                    return object;
                }

                private Object readValue(JsonReader in) throws IOException {
                    switch (in.peek()) {
                        case BEGIN_ARRAY:
                            return readArray(in);
                        case BEGIN_OBJECT:
                            return readObject(in);
                        case BOOLEAN:
                            return in.nextBoolean();
                        case NULL:
                            in.nextNull();
                            return null;
                        case NUMBER:
                            return readNumber(in);
                        case STRING:
                            return in.nextString();
                        default:
                            throw new IOException("expecting value: " + in.getPath());
                    }
                }

                private Object readNumber(JsonReader in) throws IOException {
                    double doubleValue = in.nextDouble();
                    if (doubleValue - Math.ceil(doubleValue) == 0) {
                        long longValue = (long) doubleValue;
                        if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                            return (int) longValue;
                        }
                        return longValue;
                    }
                    return doubleValue;
                }

                @SuppressWarnings("rawtypes")
                private List readArray(JsonReader in) throws IOException {
                    List list = new ArrayList();
                    in.beginArray();
                    while (in.peek() != JsonToken.END_ARRAY) {
                        Object element = readValue(in);
                        list.add(element);
                    }
                    in.endArray();
                    return list;
                }
            };
        }
    }
}
