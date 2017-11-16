package com.example.paha.ccashier;

import android.util.Log;
import java.util.TimerTask;

/**
 * Created by krivosheev on 11/13/2017.
 */

public class CTimerTask extends TimerTask {

    public CTimerTask () {
        Cnt = 0;
    }

    @Override
    public void run() {
        // выведем на экран счетчик
        try {
            Cnt++;
            Log.d(TAG, "run cnt = " + Cnt);
        }
        catch (Exception e){
            Str = e.getMessage();
        }
    }

    public String getCounter() {
        return String.valueOf(Cnt);
    }

    public  String Str;
    private int Cnt;
    private String TAG = "TimerTask";
}

