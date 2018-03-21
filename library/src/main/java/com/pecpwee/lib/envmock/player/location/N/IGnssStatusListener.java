package com.pecpwee.lib.envmock.player.location.N;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by hasee on 2017/6/17.
 */

public abstract class IGnssStatusListener extends Binder implements IInterface {

    static final int TRANSACTION_onGpsStarted = 1;
    static final int TRANSACTION_onGpsStopped = 2;
    static final int TRANSACTION_onFirstFix = 3;
    static final int TRANSACTION_onSvStatusChanged = 4;
    static final int TRANSACTION_onNmeaReceived = 5;
    private static final String DESCRIPTOR = "android.location.IGnssStatusListener";

    public IBinder asBinder() {
        return this;
    }

    public abstract void onSvStatusChanged(int _arg01, int[] _arg1, float[] _arg12
            , float[] arg3, float[] _arg4);

    public abstract void onGnssStarted();

    public abstract void onGnssStopped();

    public abstract void onFirstFix(int ttff);

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {


        int _arg01;
        switch (code) {

            case 1:
                data.enforceInterface(DESCRIPTOR);
                onGnssStarted();
                reply.writeNoException();
                return true;
            case 2:
                data.enforceInterface(DESCRIPTOR);
                this.onGnssStopped();
                reply.writeNoException();
                return true;
            case 3:
                data.enforceInterface(DESCRIPTOR);
                _arg01 = data.readInt();
                this.onFirstFix(_arg01);
                reply.writeNoException();
                return true;
            case 4:// TODO: 2017/6/18 some problem here.the arg3 and arg4 is null object.
                data.enforceInterface(DESCRIPTOR);
                _arg01 = data.readInt();
                int[] _arg1 = data.createIntArray();
                float[] _arg12 = data.createFloatArray();
                float[] _arg3 = data.createFloatArray();
                float[] _arg4 = data.createFloatArray();
                this.onSvStatusChanged(_arg01, _arg1, _arg12, _arg3, _arg4);
                reply.writeNoException();
                return true;

            case 5:
                data.enforceInterface(DESCRIPTOR);
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }

}
