
package com.samsung.xposedexp;

import com.samsung.xposedexp.R;
import com.samsung.xposedexp.AdsHooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{

    private SharedPreferences prefs;
    private Switch prefHttp;
    private Switch prefLocation;
    private Spinner appSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // swHttpBlock = (Switch) findViewById(R.id.swHttpBlock);
        // swLocationChange = (Switch) findViewById(R.id.swLocationChange);
        //
        // OnCheckedChangeListener listner = new OnCheckedChangeListener() {
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView, boolean
        // isChecked) {
        // if(isChecked){
        // Toast.makeText(getBaseContext(), buttonView.getText() +
        // " is currently ON",
        // Toast.LENGTH_LONG).show();
        // }
        // else{
        // Toast.makeText(getBaseContext(), buttonView.getText() +
        // " is currently OFF",
        // Toast.LENGTH_LONG).show();
        // }
        // }
        // };
        //
        // //set the switch to OFF
        // swHttpBlock.setChecked(false);
        // swLocationChange.setChecked(false);
        // //attach a listener to check for changes in state
        // swHttpBlock.setOnCheckedChangeListener(listner);
        // swLocationChange.setOnCheckedChangeListener(listner);

        prefs = getSharedPreferences("expSettings", Activity.MODE_WORLD_READABLE);

        prefHttp = (Switch) findViewById(R.id.swHttpBlock);
        prefHttp.setChecked(prefs.getBoolean("exp_block_http", false));

        prefLocation = (Switch) findViewById(R.id.swLocationChange);
        prefLocation.setChecked(prefs.getBoolean("exp_change_location", false));

        appSpinner = (Spinner) findViewById(R.id.app_spinner);
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.app_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        appSpinner.setAdapter(adapter);
    }

    // Called when Save button is clicked
    public void savePreferences(View view) {
        prefs = getSharedPreferences("expSettings", Activity.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("exp_block_http", prefHttp.isChecked());
        editor.putBoolean("exp_change_location", prefLocation.isChecked());
        editor.commit();

        // Send the broadcast to apply settings
        Intent applyIntent = new Intent("com.samsung.xposedexp" + ".UPDATE_PERMISSIONS");
        applyIntent.putExtra("action", "update_permissions");
        applyIntent.putExtra("Package", String.valueOf(appSpinner.getSelectedItem()));
        applyIntent.putExtra("Kill", false);
        sendBroadcast(applyIntent);
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
        // if (id == R.id.action_settings) {
        // return true;
        // }
        return super.onOptionsItemSelected(item);
    }
}
