package com.pecpwee.lib.envmock.player.location.LM;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * //from AIDL GENERATE
 */

public abstract class IGpsStatusListener extends Binder implements IInterface {
    static final int TRANSACTION_onGpsStarted = 1;
    static final int TRANSACTION_onGpsStopped = 2;
    static final int TRANSACTION_onFirstFix = 3;
    static final int TRANSACTION_onSvStatusChanged = 4;
    static final int TRANSACTION_onNmeaReceived = 5;
    private static final String DESCRIPTOR = "android.location.IGpsStatusListener";


    public abstract void onSvStatusChanged(int _arg01, int[] _arg1, float[] _arg12
            , float[] arg3, float[] _arg4, int _arg5, int _arg6, int _arg7);
    public abstract void onGpsStarted();
    public abstract void onGpsStopped();
    public abstract void onFirstFix(int time);
    public IBinder asBinder() {
        return this;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        int _arg01;
        switch (code) {
            case 1:
                data.enforceInterface(DESCRIPTOR);
                onGpsStarted();
                reply.writeNoException();
                return true;
            case 2:
                data.enforceInterface(DESCRIPTOR);
                this.onGpsStopped();
                reply.writeNoException();
                return true;
            case 3:
                data.enforceInterface(DESCRIPTOR);
                _arg01 = data.readInt();
                this.onFirstFix(_arg01);
                reply.writeNoException();
                return true;
            case 4:
                data.enforceInterface(DESCRIPTOR);
                _arg01 = data.readInt();
                int[] _arg1 = data.createIntArray();
                float[] _arg12 = data.createFloatArray();
                float[] _arg3 = data.createFloatArray();
                float[] _arg4 = data.createFloatArray();
                int _arg5 = data.readInt();
                int _arg6 = data.readInt();
                int _arg7 = data.readInt();
                this.onSvStatusChanged(_arg01, _arg1, _arg12, _arg3, _arg4, _arg5, _arg6, _arg7);
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
