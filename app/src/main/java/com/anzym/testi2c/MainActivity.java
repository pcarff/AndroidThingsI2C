package com.anzym.testi2c;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.anzym.testi2c.driver.EzoCircuitDriver;
import com.anzym.testi2c.driver.PhProbe;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    PhProbe mPhProbe;
    private static final String I2C_DEVICE_NAME = "I2C1";   // BUS NAME

    /** <TODO> Remove this after suscessful conversion to using driver.

    private static final int PH_PROBE_ADDRESS = 99;  //0x63
    private static final int TDS_PROBE_ADDRESS = 100;  //0x64
    private static final int BUFFER_SIZE = 20;

    private int delay_time = 300;

    // <TODO> Update these to strings and the use getBytes
    //ASCII for i = 105
    private static final String PH_INFO = "i";
    //ASCII for r = 114
    private static final String PH_READ = "r";


    private I2cDevice pHProbe;
    private I2cDevice tDSProbe;

    private char[] cmd;
    private byte[] cmdBytes;
    private byte[] readBytes;
    private byte[] buffer;
    private int code;
    private char[] phData;
     </TODO>
     */

    private float pH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Accessing phProbe directly.
        try {
            mPhProbe = new PhProbe(I2C_DEVICE_NAME);
        } catch (IOException e) {
            //couldn't configure the device
        }

        // Read ph value

        try {
            sample = PhProbe.r
        }



        /*
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



        // convert char[] to byte[]
        //<TODO> remove the new String after I make these Strings above.
        //cmdBytes =  PH_READ.getBytes();
        cmdBytes = PH_INFO.getBytes();

        // <TODO> move this for method when executing commands.
        if (cmdBytes[0] == 'c' || cmdBytes[0] == 'r') {
            delay_time = 1800;
        } else {
            delay_time = 300;
        }

        try {
            pHProbe.write(cmdBytes, cmdBytes.length);
            Log.d(TAG,"Wrote: " + new String(cmdBytes));
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
            pHProbe.read(readBytes, BUFFER_SIZE);
            code = readBytes[0];
            Log.d(TAG, "code : " + code);
            Log.d(TAG, "Read results: " + new String(readBytes, "UTF-8"));
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

        //<TODO> Do we need to CLOSE the device??

        */

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
