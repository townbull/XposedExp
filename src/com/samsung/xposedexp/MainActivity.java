
package com.samsung.xposedexp;

import com.samsung.xposedexp.R;
import com.samsung.xposedexp.LoadedPackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Switch swHttpBlock;
    private Switch swLocationChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        
//        swHttpBlock = (Switch) findViewById(R.id.swHttpBlock);
//        swLocationChange = (Switch) findViewById(R.id.swLocationChange);
//        		
//        OnCheckedChangeListener	listner = new OnCheckedChangeListener() {       
//			 @Override
//			 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			      if(isChecked){
//			    	  Toast.makeText(getBaseContext(), buttonView.getText() + " is currently ON",
//			    			   Toast.LENGTH_LONG).show();
//			      }
//			      else{
//			    	  Toast.makeText(getBaseContext(), buttonView.getText() + " is currently OFF",
//			   			   Toast.LENGTH_LONG).show();
//			      }       
//			 }
//        };
//        
//        //set the switch to OFF 
//        swHttpBlock.setChecked(false);
//        swLocationChange.setChecked(false);
//        //attach a listener to check for changes in state
//        swHttpBlock.setOnCheckedChangeListener(listner);
//        swLocationChange.setOnCheckedChangeListener(listner);
         
        Button changePrefs = (Button) findViewById(R.id.btn_prefs);
        changePrefs.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
                startActivity(settingsActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
