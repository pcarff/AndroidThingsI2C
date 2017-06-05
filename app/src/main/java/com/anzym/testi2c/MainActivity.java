package com.anzym.testi2c;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.anzym.testi2c.driver.EzoPhCircuitDriver;
import com.anzym.testi2c.driver.PhProbe;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    PhProbe mPhProbe;
    private static final String I2C_DEVICE_NAME = "I2C1";   // BUS NAME

    SensorEventListener mListener ;
    SensorManager mSensorManager;
    EzoPhCircuitDriver mSensorDiver;
    private float pH;

    private ButtonInputDriver mButtonInputDriverA;
    private ButtonInputDriver mButtonInputDriverB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startPhProbeSensorDriver();

        try {
            mButtonInputDriverA = new ButtonInputDriver("BCM21",
                    Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_A);
            mButtonInputDriverA.register();
            mButtonInputDriverB = new ButtonInputDriver("BCM20",
                    Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_B);
            mButtonInputDriverB.register();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing GPIO button", e);
        }

        /**
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
         **/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPhProbeSensorDriver();
        try {
            mSensorDiver.close();
        } catch (IOException e) {

        }
        /*
        if (mPhProbe != null) {
            try {
                mPhProbe.close();
                mPhProbe = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close I2C device " + mPhProbe.toString(), e);
            }
        }
        */
    }

    private void startPhProbeSensorDriver() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        mSensorManager.registerDynamicSensorCallback(new SensorManager.DynamicSensorCallback() {
            @Override
            public void onDynamicSensorConnected(Sensor sensor) {
                mSensorManager.registerListener(mListener, sensor, 2000);
            }
        });

        try {
            mSensorDiver = new EzoPhCircuitDriver(I2C_DEVICE_NAME);
            mSensorDiver.register();
        } catch (IOException e) {

        }
    }

    private void stopPhProbeSensorDriver() {
        mSensorManager.unregisterListener(mListener);
        mSensorDiver.unregister();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_A) {
            Log.d(TAG,"Key A pressed.");
            //<TODO> Do I need to unregister and the register below?
            //mSensorDiver.sleep();
            stopPhProbeSensorDriver();
            return true;

        }
        if (keyCode == KeyEvent.KEYCODE_B) {
            Log.d(TAG,"Key B pressed.");
            startPhProbeSensorDriver();
            //  startPhProbeSensorDriver();  // NOT WORKING.
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
