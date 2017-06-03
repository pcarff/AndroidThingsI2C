package com.anzym.testi2c.driver;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.UserSensor;
import com.google.android.things.userdriver.UserSensorDriver;
import com.google.android.things.userdriver.UserSensorReading;

import java.io.IOException;

/**
 * Created by pcarff on 6/2/17.
 */

public class EzoCircuitDriver implements AutoCloseable {

    private static final String TAG = EzoCircuitDriver.class.getSimpleName();
    private static final String DRIVER_NAME = "EZO pH Circuit";
    private static final String PH_SENSOR_TYPE = "pH Sensor" ;

    private PhProbe mPhProbe;
    private UserSensor mUserSensor;


    /**
     * Create a new framework for pH probe circuit connected to the given I2C bus.
     * The driver emits {@link android.hardware.Sensor} with pH data when registered.
     * @param bus
     * @throws IOException
     * @see #register()
     */
    public EzoCircuitDriver(String bus) throws IOException {
        mPhProbe = new PhProbe(bus);
    }

    /**
     * Close the driver and the underlying device.
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        unregister();
        if (mPhProbe != null) {
            try {
                mPhProbe.close();
            } finally {
                mPhProbe = null;
            }
        }
    }

    /**
     * Register the new draver in the framework
     * @see #unregister()
     */
    public void register() {
        if (mPhProbe == null) {
            throw new IllegalStateException("cannot register closed driver");
        }
        if (mUserSensor == null) {
            mUserSensor = build(mPhProbe);
            UserDriverManager.getManager().registerSensor(mUserSensor);
        }
    }

    /**
     * Unregister the driver from the framework
     *
     */
    public void unregister() {
        if (mUserSensor !=null) {
            UserDriverManager.getManager().unregisterSensor(mUserSensor);
            mUserSensor = null;
        }
    }

    /**
     *
     */
    static UserSensor build(final PhProbe phprobe) {
        return new UserSensor.Builder()
                .setName(DRIVER_NAME)
                .setCustomType(Sensor.TYPE_DEVICE_PRIVATE_BASE,
                        PH_SENSOR_TYPE,
                        Sensor.REPORTING_MODE_CONTINUOUS)
                .setDriver(new UserSensorDriver() {
                    @Override
                    public UserSensorReading read() throws IOException {
                        float[] sample = {phprobe.readSample()};
                        return new UserSensorReading(
                            sample,
                            SensorManager.SENSOR_STATUS_ACCURACY_HIGH); // 120Hz
                    }
                })
                .build();

        //TODO - example as a setEnabled method.

    }
}
