package com.pecpwee.test.envMock;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pecpwee.lib.envMock.RecordController;
import com.pecpwee.lib.envMock.recorder.AbsRecorder;

import java.util.ArrayList;

public class RecorderActivity extends AppCompatActivity {
    private Button btnStart;
    private Button btnStop;
    private static int PERMISSION_REQUEST_CODE = 1;
    private ArrayList<AbsRecorder> mRecordList = null;
    RecordController controller = null;
    private TextView tvInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        tvInfoView = (TextView) findViewById(R.id.tv_info);
        getSupportActionBar().setTitle("Record");

        controller = new RecordController(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInfoView.setText("recording...");
                controller.start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvInfoView.setText("record has stopped");
                controller.stop();
            }
        });

        requestPermission();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(RecorderActivity.this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                        , android.Manifest.permission.READ_PHONE_STATE
                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE
        );
    }


}
