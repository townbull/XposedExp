
package com.samsung.xposedcaptest2;

import com.samsung.xposedcaptest2.LoadedPackage;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        tv = (TextView)findViewById(R.id.loadedapps);
//        LoadedPackage lp =  new LoadedPackage();
//        tv.append("\n" + lp.getTV());
//        tv.append("\n" + "Check the XPosed -> Logs");
        
        tv = (TextView)findViewById(R.id.loadedapps);
        tv.setText("Check out the result at Xposed -> Logs");
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
