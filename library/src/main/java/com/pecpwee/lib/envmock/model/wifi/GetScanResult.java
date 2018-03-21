package com.pecpwee.lib.envmock.model.wifi;


import android.net.wifi.ScanResult;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2017/6/12.
 */

public class GetScanResult extends AbsTimeModel {
    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public void setScanResults(ArrayList<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    List<ScanResult> scanResults;

    public GetScanResult(List<ScanResult> list) {
        super(CONST.GET_SCAN_RESULT);
        this.scanResults = list;
    }



}
