package com.anzym.testi2c.driver;


import android.util.Log;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by pcarff on 6/2/17.
 *
 * Driver for the EZO PhProbe Circuit
 */

public class PhProbe  implements AutoCloseable {

    private static final String TAG = PhProbe.class.getSimpleName();

    private int delay_time = 300;
    /**
     * I2C slave address for ph circuit
     */
    private static final int I2C_ADDRESS = 0x63;
    private static final int BUFFER_SIZE = 20;
    private static final String READ_PH = "r";
    private static final String GET_INFO = "i";



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

        float pH = 0;
        int resultCode;
        byte[] readResult = new byte[BUFFER_SIZE];
        readResult = getResult(READ_PH);
        Log.d(TAG,"readResult: " + new String(readResult, "UTF-8"));
        resultCode = (int)readResult[0];
        switch (resultCode) {
            case 1:
                Log.d(TAG, "Read was success");
                int counter = 0;
                for (int i =1; i < readResult.length; i++) {
                    if (readResult[i] != 0) {
                        Log.d(TAG, "readResult[" + i + "]: " + readResult[i]);
                        counter = i;
                    }
                }
                byte[] pHbByte = Arrays.copyOfRange(readResult, 1, counter+1);
                Log.d(TAG,"phByte: " + new String(pHbByte));

                pH = Float.valueOf(new String(pHbByte));
                break;
            case 2:
                Log.d (TAG, "Read Falure");
                break;
            case 254:
                Log.d (TAG, "Read Pending");   //Command has not yet finished executing
                break;
            case 255:
                Log.d (TAG, "Read no data");
                break;
        }

        return pH;

    }

    private byte[] getResult(String cmdStr) {
        byte[] readBytes = new byte[BUFFER_SIZE];
        byte code;
        if (mDevice == null ) {
            throw new IllegalStateException("device not connected");
        }
        byte[] cmdBytes = cmdStr.getBytes();
        if (cmdBytes[0] == 'c' || cmdBytes[0] == 'r') {
            delay_time = 1800;
        } else {
            delay_time = 500;
        }
        try {
            mDevice.write(cmdBytes, cmdBytes.length);
        } catch (IOException e) {
            Log.d(TAG, "Error writing to device: " + e);
        }
        // Wait time for circuit to complete intstruction
        try {
            Thread.sleep(delay_time);
        } catch (InterruptedException e) {
            Log.d(TAG, "delay time error: " + e);
        }

        //read result code back from circuit
        try {
            // read all bytes
            mDevice.read(readBytes, BUFFER_SIZE);
            code = readBytes[0];


        } catch (IOException e) {
            Log.d(TAG, "Error reading data: " + e);
        }
        return readBytes;
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
