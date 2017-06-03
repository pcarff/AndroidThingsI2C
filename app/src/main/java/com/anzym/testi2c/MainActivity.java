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
            pH = mPhProbe.readSample();
            Log.d(TAG,"ph: " + pH);
        } catch (IOException e) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhProbe != null) {
            try {
                mPhProbe.close();
                mPhProbe = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close I2C device " + mPhProbe.toString(), e);
            }
        }
    }
}
