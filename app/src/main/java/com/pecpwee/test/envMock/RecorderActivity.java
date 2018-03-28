package com.pecpwee.test.envMock;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pecpwee.lib.envmock.RecordController;
import com.pecpwee.lib.envmock.model.AbsTimeModel;
import com.pecpwee.lib.envmock.recorder.RecordListener;
import com.pecpwee.test.envMock.databinding.ActivityRecorderBinding;

public class RecorderActivity extends AppCompatActivity {
    private static final String TAG = "RecorderActivity ";
    ActivityRecorderBinding binding;
    private StringBuilder stringBuilder = new StringBuilder();
    private int recordCount = 0;
    RecordController controller = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recorder);
        binding.setViewModel(new ViewModel());
        getSupportActionBar().setTitle("Record");
        controller = new RecordController(this);

    }

    public void startRecord() {
        controller.addRecordListener(recordListener);
        controller.start();

    }

    private RecordListener recordListener = new RecordListener() {
        @Override
        public void onRecordNew(AbsTimeModel model) {
//            LogUtils.d(TAG + "recordListener " + model.getClass().getName());
            stringBuilder.append(model.getClass().getSimpleName()).append("\n");
            if (recordCount > 15) {
                stringBuilder.setLength(0);
                recordCount = 0;
            }
            recordCount++;
            binding.getViewModel().tvInfos.set(stringBuilder.toString());
        }
    };

    public void stopRecord() {
        controller.stop();
    }

    public class ViewModel {
        public ObservableField<String> tvInfos = new ObservableField<>();
        public ObservableBoolean isStarted = new ObservableBoolean(false);

        public void onStart() {
            isStarted.set(true);
            binding.getViewModel().tvInfos.set("recording...");
            startRecord();
        }

        public void onStop() {
            isStarted.set(false);
            binding.getViewModel().tvInfos.set("record finished");
            stopRecord();
        }
    }


}
