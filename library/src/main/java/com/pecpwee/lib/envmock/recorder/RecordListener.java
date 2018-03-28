package com.pecpwee.lib.envmock.recorder;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2018/3/26.
 */

public interface RecordListener {
    public void onRecordNew(AbsTimeModel model);
}
