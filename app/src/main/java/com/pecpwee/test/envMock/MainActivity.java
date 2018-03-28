package com.pecpwee.test.envMock;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.pecpwee.test.envMock.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static int PERMISSION_REQUEST_CODE = 1;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.btnGoRecord.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RecorderActivity.class))
        );
        binding.btnGoReplay.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PlayerActivity.class)));
        requestPermission();

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                        , android.Manifest.permission.READ_PHONE_STATE
                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE
        );
    }

}
