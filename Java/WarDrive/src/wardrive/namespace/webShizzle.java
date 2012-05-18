package wardrive.namespace;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.util.Log;

public class webShizzle extends Activity {
	
    //String server = "http://199.15.250.136/adam/";
		
	public void transmit(String url)
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	
		
		try {
		     HttpClient httpclient = new DefaultHttpClient();
		     url = url.replaceAll(" ","");	
		     url = url.replaceAll("SSID:","");
		     
		     url = url.replaceAll("BSSID:","");
		     url = url.replaceAll("capabilities:","");
		     url = url.replaceAll("level:","");
		     url = url.replaceAll("frequency:","");
		     
		     HttpPost httppost = new HttpPost(url);
		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		     HttpResponse response = httpclient.execute(httppost);
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}		    
}
