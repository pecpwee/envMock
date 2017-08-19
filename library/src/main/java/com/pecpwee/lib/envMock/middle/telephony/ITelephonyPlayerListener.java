package com.pecpwee.lib.envMock.middle.telephony;

import android.os.Bundle;

/**
 * Created by pw on 2017/6/14.
 */

public interface ITelephonyPlayerListener {
    void setCellLocation(Bundle bundle);
    void setNetworkType(int NetworkType);

}
