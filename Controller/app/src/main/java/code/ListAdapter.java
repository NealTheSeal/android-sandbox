package code;

import grand.master.R;

import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<BluetoothDevice> {
	private List<BluetoothDevice> devices;
	
	public ListAdapter(Context context, int textViewResourceId, List<BluetoothDevice> stuff) {
	    super(context, R.layout.list_item, stuff);
	    devices = stuff;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// setup the view
	    View v = convertView;
	    if (v == null) {
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.list_item, null);
	    }
	    
	    // get the textView id's
	    TextView nameTextView = (TextView)v.findViewById(R.id.name);
	    TextView addressTextView = (TextView)v.findViewById(R.id.address);
	    // edit the values
	    nameTextView.setText(devices.get(position).getName());
	    addressTextView.setText(devices.get(position).getAddress());

	    return v;
	}
	
	public BluetoothDevice getDevice(int position) {
		return devices.get(position);
	}
}
