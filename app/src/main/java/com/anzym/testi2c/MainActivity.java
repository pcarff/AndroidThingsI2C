package com.anzym.testi2c;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String I2C_DEVICE_NAME = "I2C1";   // BUS NAME
    //private static final int I2C_ADDRESS = 0x64;
    private static final int PH_PROBE_ADDRESS = 99;  //0x63
    private static final int TDS_PROBE_ADDRESS = 100;  //0x64
    private static final int BUFFER_SIZE = 20;

    private int delay_time = 300;

    //ASCII for i = 105
    private static final char[] PH_INFO = {105,13};
    //ASCII for r = 114
    private static final char[] PH_READ = {114,13};


    private I2cDevice pHProbe;
    private I2cDevice tDSProbe;

    private char[] cmd;
    private byte[] cmdBytes;
    private byte[] readBytes;
    private byte[] buffer;
    private int code;
    private char[] phData;
    private float pH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize variables.
        cmd = new char[BUFFER_SIZE];
        cmdBytes = new byte[BUFFER_SIZE];
        readBytes = new byte[BUFFER_SIZE];
        buffer = new byte[BUFFER_SIZE];
        phData = new char[BUFFER_SIZE];

        // Set up I2C bus and devices
        try {  //TODO consider trying each device separately to get error for that device
            PeripheralManagerService service = new PeripheralManagerService();
            pHProbe = service.openI2cDevice(I2C_DEVICE_NAME, PH_PROBE_ADDRESS);
            //tDSProbe = service.openI2cDevice(I2C_DEVICE_NAME, TDS_PROBE_ADDRESS);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access I2C device", e);
        }

        // Send command to get info from device.
        cmd = PH_INFO;

        // <TODO> move this for method when executing commands.
        if (cmd[0] == 'c' || cmd[0] == 'r') {
            delay_time = 1800;
        } else {
            delay_time = 300;
        }

        // convert char[] to byte[]
        cmdBytes =  new String(cmd).getBytes();
        try {
            pHProbe.write(cmdBytes, cmdBytes.length);
        } catch (IOException e) {
            Log.d(TAG, "Error writing to device: " + e);
        }

        // Wait time for circuit to complete intstruction
        try {
            Log.d(TAG, "Waiting : " + delay_time + " secs.");
            Thread.sleep(delay_time);
        } catch (InterruptedException e) {
            Log.d(TAG, "delay time error: " + e);
        }

        //read result code back from circuit
        try {
            // read all bytes
            //pHProbe.read(readBytes, BUFFER_SIZE);

            // read one byte to get code
            pHProbe.read(readBytes, 1);
            code = readBytes[0];
            Log.d(TAG, "Read (code only) results: " + readBytes);
            Log.d(TAG, "code : " + code);
        } catch (IOException e) {
            Log.d(TAG, "Error reading data: " + e);
        }

        switch (code) {
            case 1:
                Log.d (TAG, "Read was success");
                break;
            case 2:
                Log.d (TAG, "Read Failure");
                break;
            case 254:
                Log.d (TAG, "Read Pending");   //Command has not yet finished executing
                break;
            case 255:
                Log.d (TAG, "Read no data");
                break;

        }
        //read rest of results back from circuit
        // (OR)
        //Do we do the whole read above and massage results
        try {
            // read one byte to get code
            pHProbe.read(readBytes, BUFFER_SIZE);
            Log.d(TAG, "Read results: " + readBytes);
        } catch (IOException e) {
            Log.d(TAG, "Error reading data: " + e);
        }

        //<TODO> Do we need to CLOSE the device??

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pHProbe != null) {
            try {
                pHProbe.close();
                pHProbe = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close I2C device " + pHProbe.toString(), e);
            }
        }
        if (tDSProbe != null) {
            try {
                tDSProbe.close();
                tDSProbe = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close I2C device" + tDSProbe.toString(), e);
            }
        }
    }



}
