package com.droiduino.companionappcourse;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.content.ContentValues.TAG;

public class DeviceSetup2  extends AppCompatActivity {

    boolean isBluetoothOn = false;
    boolean isLocationOn = false;

    private String deviceAddress = null;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
//    public static ConnectedThread connectedThread;
//    public static CreateConnectThread createConnectThread;

    // The following variables used in bluetooth handler to identify message status
    private final static int CONNECTION_STATUS = 1;
    private final static int MESSAGE_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_setup_2);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
        // getSupportActionBar().hide(); // hides appbar
        getSupportActionBar().setTitle("DEVICE SETUP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays back button on app bar

        getSupportActionBar().setLogo(R.drawable.heart);

        // asking permission for bluetooth and location
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        Switch sw = (Switch) findViewById(R.id.bluetoothSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    mBluetoothAdapter.enable();
                    isBluetoothOn = true;
//                    if(isBluetoothOn==true && isLocationOn==true){
//                        connectToArduino();
//                    }
                } else {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                        isBluetoothOn = false;
                    }
                }
            }
        });

        Switch locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);
                    isLocationOn = true;
//                    if(isBluetoothOn==true && isLocationOn==true){
//                        connectToArduino();
//                    }
                } else {
                    isLocationOn = false;
                }
            }
        });

//        if(isBluetoothOn==true && isLocationOn==true) {
//            Intent intent = new Intent(DeviceSetup2.this, SelectDeviceActivity.class);
//            startActivity(intent);
//        }
//
//        final TextView bluetoothStatus = findViewById(R.id.textBluetoothStatus);
//        final TextView ledStatus = findViewById(R.id.textLedStatus);
//        // Get Device Address from SelectDeviceActivity.java to create connection
//        deviceAddress = getIntent().getStringExtra("deviceAddress");
//        bluetoothStatus.setText(deviceAddress+"ibkjkjnw");
//        if (deviceAddress != null){
//            bluetoothStatus.setText("Connecting...");
//            /*
//            This is the most important piece of code.
//            When "deviceAddress" is found, the code will call the create connection thread
//            to create bluetooth connection to the selected device using the device Address
//             */
//            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            createConnectThread = new DeviceSetup2.CreateConnectThread(bluetoothAdapter,deviceAddress);
//            createConnectThread.start();
//        }
//
//        /*
//        Second most important piece of Code.
//        This handler is used to update the UI whenever a Thread produces a new output
//        and passes through the values to Main Thread
//         */
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg){
//                ledStatus.setText(msg.what+"dfghjnkml");
//                switch (msg.what){
//                    // If the updates come from the Thread to Create Connection
//                    case CONNECTION_STATUS:
//                        switch(msg.arg1){
//                            case 1:
//                                bluetoothStatus.setText("Bluetooth Connected");
//                                break;
//                            case -1:
//                                bluetoothStatus.setText("Connection Failed");
//                                break;
//                        }
//                        break;
//
//                    // If the updates come from the Thread for Data Exchange
//                    case MESSAGE_READ:
//                        String statusText = msg.obj.toString().replace("/n","");
//                        ledStatus.setText(statusText);
//
//                        String androidCmd = "p";
//                        connectedThread.write(androidCmd);
//
//                        break;
//                }
//            }
//        };

        Button thermoPairingButton = findViewById(R.id.thermoPairingButton);
        thermoPairingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This is the code to move to another screen
                Intent intent = new Intent(DeviceSetup2.this, SuccessfulThermometerPairing.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    System.out.println("grranted");
                } else {
                    //not granted
                    System.out.println("not grranted");
                    //keep asking
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.topnav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                Intent intent = new Intent(DeviceSetup2.this, DeviceSetupInfo.class);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /* ============================ Thread to Create Connection ================================= */
//    public static class CreateConnectThread extends Thread {
//
//        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
//            // Opening connection socket with the Arduino board
//            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
//            BluetoothSocket tmp = null;
//            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();
//            try {
//                // Get a BluetoothSocket to connect with the given BluetoothDevice.
//                // MY_UUID is the app's UUID string, also used in the server code.
//                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket's create() method failed", e);
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//            // Cancel discovery because it otherwise slows down the connection.
//            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            bluetoothAdapter.cancelDiscovery();
//            try {
//                // Connect to the Arduino board through the socket. This call blocks
//                // until it succeeds or throws an exception.
//                mmSocket.connect();
//                handler.obtainMessage(CONNECTION_STATUS, 1, -1).sendToTarget();
//            } catch (IOException connectException) {
//                // Unable to connect; close the socket and return.
//                try {
//                    mmSocket.close();
//                    handler.obtainMessage(CONNECTION_STATUS, -1, -1).sendToTarget();
//                } catch (IOException closeException) { }
//                return;
//            }
//
//            // The connection attempt succeeded. Perform work associated with
//            // the connection in a separate thread.
//            // Calling for the Thread for Data Exchange (see below)
//            connectedThread = new DeviceSetup2.ConnectedThread(mmSocket);
//            connectedThread.run();
//        }
//
//        // Closes the client socket and causes the thread to finish.
//        // Disconnect from Arduino board
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
//    }

    /* =============================== Thread for Data Exchange ================================= */
//    public static class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        // Getting Input and Output Stream when connected to Arduino Board
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) { }
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        // Read message from Arduino device and send it to handler in the Main Thread
//        public void run() {
//            byte[] buffer = new byte[1024];  // buffer store for the stream
//            int bytes = 0; // bytes returned from read()
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    buffer[bytes] = (byte) mmInStream.read();
//                    String arduinoMsg = null;
//
//                    // Parsing the incoming data stream
//                    if (buffer[bytes] == '\n'){
//                        arduinoMsg = new String(buffer,0,bytes);
//                        Log.e("Arduino Message",arduinoMsg);
//                        handler.obtainMessage(MESSAGE_READ,arduinoMsg).sendToTarget();
//                        bytes = 0;
//                    } else {
//                        bytes++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//        }
//
//        // Send command to Arduino Board
//        // This method must be called from Main Thread
//        public void write(String input) {
//            byte[] bytes = input.getBytes(); //converts entered String into bytes
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) { }
//        }
//    }
}