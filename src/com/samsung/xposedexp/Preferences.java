package com.samsung.xposedexp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;
 
public class Preferences extends PreferenceActivity {
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  getFragmentManager().beginTransaction().replace(android.R.id.content,
    new PrefsFragment()).commit();
  PreferenceManager.setDefaultValues(Preferences.this, R.xml.prefs, false);
   
 }
  
 public class PrefsFragment extends PreferenceFragment {
   
  @Override
  public void onCreate(Bundle savedInstanceState) {
    
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.prefs);
   
   // Get the block_http
   Preference block_http = (Preference) findPreference("exp_block_http");
   block_http.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	   public boolean onPreferenceClick(Preference preference) {
		 Toast.makeText(getBaseContext(), "The Block Http has been switched.",
		   Toast.LENGTH_LONG).show();
		 SharedPreferences customSharedPreference = getSharedPreferences(
		   "myCustomSharedPrefs", Activity.MODE_PRIVATE);
		 SharedPreferences.Editor editor = customSharedPreference.edit();
		 editor.putString("myCustomPref", "The preference has been clicked");
		 editor.commit();
		 return true;
	   }
   	});
    
   // Get the custom preference
   Preference customPref = (Preference) findPreference("customPref");
   customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	   public boolean onPreferenceClick(Preference preference) {
		 Toast.makeText(getBaseContext(), "The custom preference has been clicked",
		   Toast.LENGTH_LONG).show();
//		 SharedPreferences customSharedPreference = getSharedPreferences(
//		   "myCustomSharedPrefs", Activity.MODE_PRIVATE);
//		 SharedPreferences.Editor editor = customSharedPreference.edit();
//		 editor.putString("myCustomPref", "The preference has been clicked");
//		 editor.commit();
		 return true;
	   }
   	});
    
  }
   
 }
}
