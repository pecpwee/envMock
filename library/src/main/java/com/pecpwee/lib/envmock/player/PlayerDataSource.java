package com.pecpwee.lib.envmock.player;

import com.pecpwee.lib.envmock.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by pw on 2018/3/24.
 */

public class PlayerDataSource {


    private boolean isDataLoadedOk = false;

    private ArrayList<AbsPlayer.TimedRunnable> mTimedActionsList;


    public PlayerDataSource() {
        this.mTimedActionsList = new ArrayList<>();
    }

    void setNeedReloadData() {
        isDataLoadedOk = false;
    }

    public boolean isDataLoadedOk() {
        return isDataLoadedOk;
    }


    void addTimedAction(AbsPlayer.TimedRunnable timeRunnable) {
        mTimedActionsList.add(timeRunnable);
    }


    public int getSize() {
        return mTimedActionsList.size();
    }

    public AbsPlayer.TimedRunnable getAction(int index) {
        return mTimedActionsList.get(index);
    }

    public synchronized void loadDataIfNeed(File file, ILineDataParser parser) throws FileNotFoundException {
        if (isDataLoadedOk) {
            return;
        }
        mTimedActionsList.clear();
        doLoadData(file, parser);
        isDataLoadedOk = true;
    }

    private void doLoadData(File file, ILineDataParser parser) throws FileNotFoundException {
        FileInputStream fis = null;
        if (!file.exists()) {
            throw new FileNotFoundException("cannot find mock data file");
        }
        mTimedActionsList.clear();
        LogUtils.d(getClass().getSimpleName() + " loading data from" + file.getAbsolutePath());
        try {
            int linecount = 0;

            fis = new FileInputStream(file);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            while ((line = br.readLine()) != null) {
                parser.onNewLineGot(line);
                linecount++;
            }

            br.close();
            LogUtils.d(getClass().getSimpleName() + "parsed the count of the line is:" + linecount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
