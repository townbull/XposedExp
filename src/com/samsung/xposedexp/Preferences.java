
package com.samsung.xposedexp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Preferences extends Activity {
    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // getFragmentManager().beginTransaction().replace(android.R.id.content, new
    // PrefsFragment())
    // .commit();
    // PreferenceManager.setDefaultValues(Preferences.this, getPackageName() +
    // "_preferences", Context.MODE_WORLD_READABLE, R.xml.prefs, false);
    // }
    //
    // public class PrefsFragment extends PreferenceFragment {
    //
    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // addPreferencesFromResource(R.xml.prefs);
    // }
    // }

    private SharedPreferences prefs;
    private Switch prefHttp;
    private Switch prefLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_pref);

        prefs = getSharedPreferences("expSettings", Activity.MODE_WORLD_READABLE);

        prefHttp = (Switch) findViewById(R.id.swHttpBlock);
        prefHttp.setChecked(prefs.getBoolean("exp_block_http", false));

        prefLocation = (Switch) findViewById(R.id.swLocationChange);
        prefLocation.setChecked(prefs.getBoolean("exp_change_location", false));

//        Button mClose = (Button) findViewById(R.id.close);
//        mClose.setOnClickListener(new OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                finish();
//            }
//        });

        Button mSave = (Button) findViewById(R.id.save);
        mSave.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                savePreferences();
                finish();
            }
        });
    }

    private void savePreferences() {
        prefs = getSharedPreferences("expSettings", Activity.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("exp_block_http", prefHttp.isChecked());
        editor.putBoolean("exp_change_location", prefLocation.isChecked());
        editor.commit();
    }
}
