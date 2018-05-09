package code;

import grand.master.R;

import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;                            

public class MainActivity extends Activity {

	final String LogTag = "MainActivity";
	//contains the Bluetooth radio for the device(phone)
	private BluetoothAdapter BlueToothRadio;
	//Socket used for connecting
	private BluetoothSocket BlueToothSocket;
	//Creates a connection to the Bluetooth devices
	private ConnectionManager manager;
	 // Name of the connected device
	private String mConnectedDeviceName = null;
	
	private final int REQUEST_ENABLE_BT = 1;
	private final int REQUEST_CONNECT_DEVICE = 2;
	private TextView Status;
	private int ConnectionState = 0;
	JoyStickClass js;
	LinearLayout layout_joystick;
	
	//Flag for connection feedback
	private boolean flag = true;
	
	// Message types sent from the BluetoothReadService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Status = (TextView)findViewById(R.id.status);
		manager = new ConnectionManager(this, mHandlerBT);
		
		// Initial Bluetooth setup (turn it on)
		setupBlueTooth();
		
		layout_joystick = (LinearLayout)findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
        		, layout_joystick, R.drawable.image_button);
	    js.setStickSize(150, 150);
	    js.setLayoutSize(375, 375);
	    js.setLayoutAlpha(150);
	    js.setStickAlpha(100);
	    js.setOffset(70);
	    js.setMinimumDistance(0);
	    
	    layout_joystick.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN
						|| arg1.getAction() == MotionEvent.ACTION_MOVE) {
					Log.i(LogTag,"X : " + String.valueOf(js.getX()));
					Log.i(LogTag,"Y : " + String.valueOf(js.getY()));
					Log.i(LogTag,"Angle : " + String.valueOf(js.getAngle()));
					Log.i(LogTag,"Distance : " + String.valueOf(js.getDistance()));
					
					int direction = js.get8Direction();
					if(direction == JoyStickClass.STICK_UP) {
						Log.i(LogTag,"Direction : Up");
					} else if(direction == JoyStickClass.STICK_UPRIGHT) {
						Log.i(LogTag,"Direction : Up Right");
					} else if(direction == JoyStickClass.STICK_RIGHT) {
						Log.i(LogTag,"Direction : Right");
					} else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
						Log.i(LogTag,"Direction : Down Right");
					} else if(direction == JoyStickClass.STICK_DOWN) {
						Log.i(LogTag,"Direction : Down");
					} else if(direction == JoyStickClass.STICK_DOWNLEFT) {
						Log.i(LogTag,"Direction : Down Left");
					} else if(direction == JoyStickClass.STICK_LEFT) {
						Log.i(LogTag,"Direction : Left");
					} else if(direction == JoyStickClass.STICK_UPLEFT) {
						Log.i(LogTag,"Direction : Up Left");
					} else if(direction == JoyStickClass.STICK_NONE) {
						Log.i(LogTag,"Direction : Center");
					}
				} else if(arg1.getAction() == MotionEvent.ACTION_UP) {
					Log.i(LogTag,"X :");
					Log.i(LogTag,"Y :");
					Log.i(LogTag,"Angle :");
					Log.i(LogTag,"Distance :");
					Log.i(LogTag,"Direction :");
				}
				return true;
			}
        });
		Log.d(LogTag, "onCreate Finished");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.menu, menu);
	    //ImageButton ib = (ImageButton) menu.findItem(R.id.connect);
	    //registerForContextMenu(ib);
	    return true;
	}
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem ConnectMenuItem = menu.findItem(R.id.connect);
        if (ConnectionState == 1) {
            ConnectMenuItem.setTitle("Disconnect");
        } else {
        	ConnectMenuItem.setTitle(R.string.connect);
        }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.connect:
	        	 if (ConnectionState == 0) {
	        		// Launch the DeviceListActivity to see devices and do scan
	        		Intent serverIntent = new Intent(this, DeviceListActivity.class);
	        		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	        		}
	        		else
	        		if (ConnectionState == 1) {
	        			manager.cancelConnections();
	        		}
	            return true;
	        case R.id.cbj:
	        	Toast.makeText(this, "cbj :(", Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(BlueToothSocket != null) {
			try {
				BlueToothSocket.close();
			} catch (IOException e) {
				Log.w(LogTag,"Caught Exception:\n" + e.toString());
			}
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	/*
	 * Right arrow button functionality
	 */
	public void send(View v) {
		String msg = "right";
		if(ConnectionState == 1) {
			manager.write(msg.getBytes());
		}
	}
	
	public void setupBlueTooth() {
		BlueToothRadio = BluetoothAdapter.getDefaultAdapter();
		if (BlueToothRadio == null) {
			Log.d(LogTag, "VERY BAD");
		} else {
			if (!BlueToothRadio.isEnabled()) {
				Log.d(LogTag, "Enabling Bluetooth...");
			    Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    startActivityForResult(enableBlueTooth, REQUEST_ENABLE_BT);
			} else {
				Log.d(LogTag, "Bluetooth already enabled!");
			}
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LogTag, "at least this worked");
        if (resultCode == RESULT_CANCELED) {
        	if (resultCode == Activity.RESULT_OK) {
	        	Toast.makeText(this, "Connection failure.", Toast.LENGTH_LONG).show();
	        	Log.d(LogTag,"Connection failure.");
        	}
        }
	    if (requestCode == REQUEST_ENABLE_BT) {
	        if(resultCode == RESULT_OK){
	        	Toast.makeText(this, "BlueTooth connected!", Toast.LENGTH_LONG).show();
	        	Log.d(LogTag, "BlueTooth connected!");
	        }
	    }
        if (requestCode == REQUEST_CONNECT_DEVICE) {
        	Log.d(LogTag,"in the request connection");
        	// When DeviceListActivity returns with a device to connect
        	if (resultCode == Activity.RESULT_OK) {
        		Log.d(LogTag,"in the connection, with a thumbs up");
	        	// Get the device MAC address
	        	String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	        	Log.d(LogTag,"Address of device returned to Main: " + address);
	        	// Get the BLuetoothDevice object
	        	BluetoothDevice device = BlueToothRadio.getRemoteDevice(address);
	        	// Attempt to connect to the device
	        	manager.connect(device);
        	}
        }
	}
	
	public void ListViewClickFeedBack() {
        if(!flag) {
    		Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
        } else {
        	Toast.makeText(this, "Connection failed.", Toast.LENGTH_SHORT).show();	      
        }
	}
	
    // The Handler that gets information back from the BluetoothService
    private final Handler mHandlerBT = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		    switch (msg.what) {
			    case MESSAGE_STATE_CHANGE:
				    Log.i(LogTag, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				    switch (msg.arg1) {
				    	case ConnectionManager.STATE_CONNECTED:
				    		Status.setText("Connected!");
				    		ConnectionState = 1;
						    break;				
					    case ConnectionManager.STATE_CONNECTING:
					    	Status.setText("Connecting...");
						    break;
					    case ConnectionManager.STATE_LISTEN:
					    	Status.setText("shhhh");
					    	break;
					    case ConnectionManager.STATE_NONE:
					    	Status.setText("Snack break");
					    	ConnectionState = 0;
					    	break;
				    }
				    break;
			    case MESSAGE_WRITE:				
				    break;
			    /*
			    case MESSAGE_READ:
			    byte[] readBuf = (byte[]) msg.obj;
			    mEmulatorView.write(readBuf, msg.arg1);
			
			    break;
			    */
			    case MESSAGE_DEVICE_NAME:
				    // save the connected device's name
				    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				    break;
			    case MESSAGE_TOAST:
				    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
				    break;
		    }
	    }
    };
}

