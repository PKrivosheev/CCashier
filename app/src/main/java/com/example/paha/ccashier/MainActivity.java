package com.example.paha.ccashier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

import java.io.File;

import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import android_serialport_api.*;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10 ;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tvC_call);
//        tv.setVisibility(View.INVISIBLE);
        tvTimerOut = (TextView) findViewById(R.id.tvTimerOut);

        Log.d(TAG, "onCreate");

        tv.setText(stringFromJNI());

        // подключим слушателя к кнопке btStart
        btStart = findViewById(R.id.btStart);  // получим ссылку на кнопку. это метод наследуется от Activity
        btStart.setVisibility(View.VISIBLE);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of a call to a native method
                tv.setText(stringFromJNI());
                tv.setVisibility(View.VISIBLE);
                btTimer.setVisibility(View.VISIBLE);

                // установим разрешения
                pSetPermission();

            }
        });

        btDevice = findViewById(R.id.btDrivers);
        btDevice.setVisibility(View.VISIBLE);
        btDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pSeeDrivers();
            }
        });

        btSerial = findViewById(R.id.btSerial);
        btSerial.setVisibility(View.VISIBLE);
        btSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pOpenPort();
            }
        });

        oTimer = new Timer();
        hRunTimer = new CTimerTask();
        tvTimerOut.setVisibility(View.VISIBLE);
        tvTimerOut.setText("Hello"+hRunTimer.getCounter());
        oTimer.schedule(hRunTimer, 0, 1000);

        // подключим слушателя на кнопку Timer
        btTimer = findViewById(R.id.btTimer);
        btTimer.setOnClickListener( new View.OnClickListener() {

            public void onClick( View view ) {
                tvTimerOut.setText("Hello"+hRunTimer.getCounter());
            }
        });
    }

    protected void pOpenPort() {
        //
        openComJNI("/dev/ttyACM");
        openComJNI("/dev/ttyGS");
        openComJNI("/dev/ttyMT");
    }

    protected void pSeeDrivers() {
        //!!!!!
        File[] Roots = File.listRoots();

        Roots = Roots[0].listFiles();
        for( File lcItem : Roots ) {
            if (lcItem.isDirectory() == true)
                Log.d(TAG, "Dirs : " + lcItem.getName() + "[len : " + lcItem.length() + "]");
        }

        Log.d(TAG, "DEVICE FOLDER\\n------------");
        File dev = new File("/dev");
        if ( dev != null )
            Log.d( TAG, "Readable = "+dev.setReadable( true ));

        Log.d(TAG,dev.exists()+"");
        if ( dev.exists() == true ) {
            Log.d(TAG, "File: "+dev.getName()+" Len:" + dev.length());
            Log.d(TAG, "Can Read? - "+ ((Boolean)dev.canRead()).toString());
        }

        /*
        Log.d(TAG,dev.isDirectory()+"");
        File[] lvDevList = dev.listFiles();
        for ( File item : lvDevList ) {
            if ( item.isDirectory() == false )
//                Log.d(TAG, "Folder" + item.getName() + "["+item.length()+"]");
//            else
                Log.d(TAG, item.getName() + "["+item.length()+"]");
        }
        */


        String[] Devices = mSerialPortFinder.getAllDevices();
        try {
            SerialPort Rs = new SerialPort(new File("dev/ttyACM"), 9600, 0);
        }
        catch (Exception e) {}


    }
    // проверяем наличия разрешения на чтения dev папки (файла)
    protected void pSetPermission() {
//        int vPermission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE);


        // Here, thisActivity is the current activity
//        if (    vPermission != PackageManager.PERMISSION_GRANTED)
//
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "READ_EXTERNAL_STORAGE - Permission GRANTED");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Log.d(TAG, "READ_EXTERNAL_STORAGE - Permission DENIED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native int openComJNI(String s);

    public TextView    tv;
    public TextView    tvTimerOut;
    private Button      btStart;
    private Button      btTimer;
    private Button      btDevice;
    private Button      btSerial;
    private Integer     Count= 0;
    private Timer       oTimer;
    private CTimerTask   hRunTimer;
    final private String TAG = "MainActivity";
}
