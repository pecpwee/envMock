package com.pecpwee.lib.envMock.recorder.telephony;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.pecpwee.lib.envMock.AbsConfig;
import com.pecpwee.lib.envMock.RecordConfig;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;
import com.pecpwee.lib.envMock.model.telephony.GetCellLocation;
import com.pecpwee.lib.envMock.model.telephony.GetNetworkType;
import com.pecpwee.lib.envMock.recorder.AbsRecorder;
import com.pecpwee.lib.envMock.utils.TimerJob;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;


/**
 * Created by pw on 2017/6/15.
 */

public class TelephonyRecorder extends AbsRecorder {
    private TimerJob cellTimedJob = new TimerJob();

    private Context context;

    public TelephonyRecorder(Context context) {
        this.context = context;
        final TelephonyManager telephonyManager = (TelephonyManager) CenterServiceManager.getInstance().getServiceFetcher(Context.TELEPHONY_SERVICE).getOrigManagerObj();

        cellTimedJob.setInterval(RecordConfig.getInstance().getSampleInterval())
                .setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = TelephonyRecorder.this.getCellLocation();
                        doRecord(new GetCellLocation(bundle));
                        doRecord(new GetNetworkType(telephonyManager.getNetworkType()));

                    }
                });
    }

    public Bundle getCellLocation() {
        Object origInterfaceBinderObj = CenterServiceManager
                .getInstance()
                .getServiceFetcher(Context.TELEPHONY_SERVICE)
                .getOrigBinderProxyObj();
        Bundle bundle = null;
        try {
            bundle = (Bundle) MethodUtils.invokeMethod(origInterfaceBinderObj, "getCellLocation", context.getPackageName());//6.0及以上
        } catch (NoSuchMethodException e) {
            try {
                bundle = (Bundle) MethodUtils.invokeMethod(origInterfaceBinderObj, "getCellLocation");//5.0
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @Override
    public void onStart() {
        cellTimedJob.setInterval(RecordConfig.getInstance().getSampleInterval());
        cellTimedJob.start();
    }

    @Override
    public void onStop() {
        cellTimedJob.stop();
    }

    @Override
    public String getDefaultFilePath() {
        return AbsConfig.DEFAULT_FILEPATH_CELL;
    }


}
