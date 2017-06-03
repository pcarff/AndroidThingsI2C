package com.anzym.testi2c.driver;


import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by pcarff on 6/2/17.
 *
 * Driver for the EZO PhProbe Circuit
 */

public class PhProbe  implements AutoCloseable {

    private static final String TAG = PhProbe.class.getSimpleName();

    /**
     * I2C slave address for ph circuit
     */
    public static final int I2C_ADDRESS = 0x63;


    private I2cDevice mDevice;

    /**
     * Create a new EZO pH Circuit driver connected to the given I2C bus.
     *
     * @param bus
     * @throws java.io.IOException
     */
    public PhProbe(String bus) throws IOException {
        PeripheralManagerService service = new PeripheralManagerService();
        I2cDevice device = service.openI2cDevice(bus, I2C_ADDRESS);
        try {
            connect(device);
        } catch (IOException | RuntimeException e ) {
            try {
                close();
            } catch (IOException | RuntimeException ignored) {

            }
            throw e;
        }
    }

    /**
     * Create a new EZo pH Circuit driver connected to the given I2C device
     * @param device
     * @throws IOException
     */
    /*package*/ PhProbe(I2cDevice device) throws IOException {
        connect(device);
    }

    private void connect(I2cDevice device) throws IOException {
        if (mDevice != null) {
            throw new IllegalStateException("device already connected");
        }
        mDevice = device;
        //<TODO> any other settings to do??

    }

    /**
     * Read data from pHProbe
     *
     */
    public float readSample() throws IOException, IllegalStateException {
        if (mDevice == null ) {
            throw new IllegalStateException("device not connected");
        }
        
    }

    /**
     * Close the driver and the underlying device.
     */
    @Override
    public void close() throws IOException {
        if (mDevice != null) {
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }
}
