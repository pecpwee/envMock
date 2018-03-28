package com.example.uiwrapper;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.pecpwee.lib.envmock.PlayController;
import com.pecpwee.lib.envmock.utils.TimerJob;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by pw on 2018/3/24.
 */

public class MainPannelFragment extends DialogFragment {

    private MapView mapView;
    private Button btnStart;
    private Button btnStop;
    private Button btnHide;
    private SeekBar progressBar;
    private MainPannelRepository repository;
    private AMap mMapController;
    private Polyline mPathOnMap;
    private Marker mCurrentPlayPositionMarker;
    private TimerJob mUIRefreshTimeJob = new TimerJob()
            .setInterval(400)
            .setRunnable(new Runnable() {
                @Override
                public void run() {
                    refreshPlayProgress();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_main_panel, container, false);
        btnStart = view.findViewById(R.id.btn_start);
        btnStop = view.findViewById(R.id.btn_stop);
        btnHide = view.findViewById(R.id.btn_hide);
        progressBar = view.findViewById(R.id.progressbar);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goStartState();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goStopState();
            }
        });

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPannelFragment.this.hide();
            }
        });

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float offsetPercentage = progress / (float) 100;
                    PlayController.getInstance().setBeginOffsetPercent(offsetPercentage);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mMapController = mapView.getMap();
        mMapController.getUiSettings().setMyLocationButtonEnabled(false);
        mMapController.getUiSettings().setZoomControlsEnabled(false);
        mMapController.getUiSettings().setTiltGesturesEnabled(false);

        return view;
    }

    private void goStartState() {
        if (!PlayController.getInstance().isPlaying()) {
            try {
                PlayController.getInstance().startPlay();
            } catch (FileNotFoundException e) {
                Toast.makeText(this.getActivity(), "no envMock data file founded, please record some data firstly", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        setStartUI();
    }

    private void setStartUI() {
        drawPath();
        progressBar.setEnabled(false);
        btnStop.setEnabled(true);
        btnStart.setEnabled(false);

        mUIRefreshTimeJob.start();

    }

    private void goStopState() {
        if (PlayController.getInstance().isPlaying()) {
            PlayController.getInstance().stopPlay();
        }
        PlayController.getInstance().setBeginOffsetPercent(0);
        setStopUI();
    }


    private void setStopUI() {
        if (mCurrentPlayPositionMarker != null) {
            mCurrentPlayPositionMarker.remove();
            mCurrentPlayPositionMarker = null;
        }
        mUIRefreshTimeJob.stop();
        progressBar.setEnabled(true);
        progressBar.setProgress(0);
        btnStop.setEnabled(false);
        btnStart.setEnabled(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        synchronizeUIState();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void synchronizeUIState() {
        PlayController controller = PlayController.getInstance();
        drawPath();
        if (controller.isPlaying()) {
            setStartUI();
            refreshPlayProgress();
        } else {
            setStopUI();
        }
        mMapController.moveCamera(CameraUpdateFactory.zoomTo(mMapController.getMaxZoomLevel()));
        mMapController.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                        MainPannelStateKeeper.sUIState.cameraPosition));

    }

    private void refreshPlayProgress() {
        float currentProgress = PlayController.getInstance().getPlayingProgress();
        if (currentProgress == 1.0F) {
            goStopState();
        } else {
            progressBar.setProgress((int) (PlayController.getInstance().getPlayingProgress() * 100));
            this.setCurrentLocationOnMap(PlayController.getInstance().getLastPlayedLocation());
        }
    }

    private void setCurrentLocationOnMap(Location location) {
        if (location == null) {
            return;
        }
        LatLng latLng = repository.convert2AmapLoc(location);
        moveCamera(latLng);
        if (mCurrentPlayPositionMarker != null) {
            mCurrentPlayPositionMarker.remove();
        }
        mCurrentPlayPositionMarker = mMapController
                .addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("played location"));

    }


    private void moveCamera(LatLng latLng) {
        mMapController.moveCamera(CameraUpdateFactory.zoomTo(mMapController.getMaxZoomLevel()));
        mMapController.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

    }

    private void drawPath() {
        repository = new MainPannelRepository(this.getActivity().getApplicationContext());
        List<LatLng> latLngs = repository.getPathList();
        if (latLngs == null) {
            return;
        }
        if (mPathOnMap != null) {
            mPathOnMap.remove();
        }

        mPathOnMap = mMapController.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 255, 1, 1)));
        if (!PlayController.getInstance().isPlaying()) {
            if (latLngs.size() > 0) {
                moveCamera(latLngs.get(0));
            }
        }


    }

    private static final String FRAGMENT_TAG = "envMockMainPannel";

    public void showFragment(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (existedFragment == null) {
            fragmentManager.beginTransaction().add(this, FRAGMENT_TAG).commit();
        } else {
            fragmentManager.beginTransaction().show(this).commit();
            getDialog().show();
        }
    }

    public void hide() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        Fragment existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (existedFragment != null) {
            fragmentManager.beginTransaction().hide(this).commit();
            getDialog().hide();
        }
        MainPannelStateKeeper.sUIState.cameraPosition = mMapController.getCameraPosition();

    }

    public void destroy(Activity activity) {
        setStopUI();
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (existedFragment != null) {
            fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
        }


    }

}
