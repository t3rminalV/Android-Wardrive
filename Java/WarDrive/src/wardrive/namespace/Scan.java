package wardrive.namespace;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Scan extends Activity implements OnClickListener, LocationListener {
	WifiManager wifi;	
	double lat = 0.0;
	double lon = 0.0;
	TextView textStatus;
	TextView textGps;
	Button buttonScan;
	Button buttonMap;
	List<ScanResult> results;
	Object[] Sresults;
	int refresh;
	int state = 0;
	String output = "";
	List<String> BStore = new ArrayList<String>();
	

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		textGps = (TextView) findViewById(R.id.textGps);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);
		buttonScan.setEnabled(false);
		buttonScan.setVisibility(View.INVISIBLE);		
		Toast toastie = Toast.makeText(getApplicationContext(), "Doing cool stuffs, please wait...", 30);	
		toastie.show();
		
		/*
		buttonMap = (Button) findViewById(R.id.buttonMap);
		buttonMap.setOnClickListener(new OnClickListener()
		{
			
			public void onClick(View arg0) {
				Bundle weeBundle = new Bundle();
				String newStringArray = null;
				for (int i = 0; i <Sresults.length; i++)
				{
					newStringArray += "|-|" + Sresults[i];
				}
				weeBundle.putString("array", newStringArray);
				Intent intent = new Intent(Scan.this,adamMap.class);
				intent.putExtras(weeBundle);
		        startActivity(intent); 
				
			}
	    });*/
		
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		
		//Set rotation
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Get WiFi status		
		if (!wifi.isWifiEnabled()) {	
			textStatus.append("\n\nWiFi is OFFLINE");
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("WiFi is currently disabled, press OK to enable.")
			.setCancelable(true)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {				
				public void onClick(DialogInterface dialog, int which) {					
					wifi.setWifiEnabled(true);
					Toast toast = Toast.makeText(getApplicationContext(), "Starting WiFiS, Please Wait...", 30);	
					Toast toast2 = Toast.makeText(getApplicationContext(), "WiFi Started!", 4);					
					toast.show();
					
					while (true) {					
						if (wifi.isWifiEnabled() == true && lat != 0.0) {
							toast.cancel();
							toast2.show();
							break;
						}
					}					
				}
			}).show();			
		}		
		
		if (textGps.getText() == "") 
		{
			buttonScan.setEnabled(false);
			buttonScan.setVisibility(View.INVISIBLE);
		}else
		{
			buttonScan.setEnabled(true);
			buttonScan.setVisibility(View.VISIBLE);
			toastie.cancel();
		}
		
		/*
		WifiInfo info = wifi.getConnectionInfo();
		textStatus.append("\n\nWiFi Status: " + info.toString());
		*/		
	}

	public void onClick(View view) {				
		
		switch(view.getId()){
		case R.id.buttonScan:		
			if (state == 0) {
				state = 1;
				buttonScan.setText("Stop Scan");
			} else {				
				state = 0;				
				remoteSend();				
				buttonScan.setText("Start Scan");				
			}		
			MyAsyncTask aTask = new MyAsyncTask();
			aTask.execute();
			//runTask();
		}
	}
	
	public void remoteSend() {
		// Doesnt work:
		//Toast toastie2 = Toast.makeText(getApplicationContext(), "Saving data to server, please wait...", 30);	
		//toastie2.show();
		webShizzle send = new webShizzle();
		send.transmit("http://199.15.250.136/adam/recieve.php?data=" + output + "&make=" + Build.MANUFACTURER + "&model=" + Build.MODEL);
		//toastie2.cancel();
	}
	
	class MyAsyncTask extends AsyncTask<Void, Object, Void> {	
		
		/*
		protected void onPreExecute(){
			SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(Scan.this);
			rfsh = sharePref.getString("refresh", "5");
			Toast toast = Toast.makeText(getApplicationContext(), rfsh, 30);
			toast.show();
		}*/
		
		@Override
		protected Void doInBackground(Void... params) {		
			
    		while(state == 1){
    			wifi.startScan();
    			results = wifi.getScanResults();    			
    			Sresults = results.toArray();  			
    			
    			publishProgress(Sresults);
    			
    			try {
					Thread.sleep(refresh);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  	   			
    		} 		
    		this.cancel(true);
			return null;
    	}
		
		protected void onProgressUpdate(Object... Sresults){				
			textStatus.setText("");
			
			for(int i=0; i<Sresults.length; i++){
				textStatus.append("\n\n" + Sresults[i].toString());
				
				String[] temp1 = Sresults[i].toString().split("BSSID: ");
				String temp2 = temp1[1].toString().substring(0, 17);				
				
				if (lat != 0) {	
					if (!BStore.contains(temp2))
					{
						BStore.add(temp2);						
						output += Sresults[i].toString() + "," + Double.toString(lat) + "," + Double.toString(lon) + "!!!";
					} 
				}
			}
		}
		
    }
	
	public class MyLocationListener implements LocationListener

	{

		public void onLocationChanged(Location location) {
			lat = location.getLatitude();
			lon = location.getLongitude();
			textGps.setText("Lat: " + lat + ", Lon: " + lon );	
			
			if (textGps.getText() != "") 			
			{
				buttonScan.setEnabled(true);
				buttonScan.setVisibility(View.VISIBLE);
			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			
			
		}


	}

	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}











