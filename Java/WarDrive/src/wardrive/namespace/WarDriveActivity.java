package wardrive.namespace;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class WarDriveActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final ImageButton button = (ImageButton) findViewById(R.id.btnStart);
        button.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
		    	handleClick(v);				
			}
		}); 
        
        //Set roatation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);        		
    }
    
    public void handleClick(View v){    	
    	Intent intent = new Intent();
    	intent.setClass(this,Scan.class);
    	startActivity(intent);
    }
    /**
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }*/	
}