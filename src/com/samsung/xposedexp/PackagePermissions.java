
package com.samsung.xposedexp;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.samsung.xposedexp.AdsHooks;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class PackagePermissions extends BroadcastReceiver {
    public static final String TAG = "XposedExp";

    private final Object pmSvc;
    private final Map<String, Object> mPackages;
    private final Object mSettings;

    @SuppressWarnings("unchecked")
    public PackagePermissions(Object pmSvc) {
        this.pmSvc = pmSvc;
        this.mPackages = (Map<String, Object>) getObjectField(pmSvc, "mPackages");
        this.mSettings = getObjectField(pmSvc, "mSettings");
    }

    public static void initHooks() {
        /*
         * Hook to the PackageManager service in order to - Listen for
         * broadcasts to apply new settings and restart the app - Intercept the
         * permission granting function to remove disabled permissions
         */
        try {
            final Class<?> clsPMS = findClass("com.android.server.pm.PackageManagerService",
                    AdsHooks.class.getClassLoader());

            // Listen for broadcasts from the Settings part of the mod, so it's
            // applied immediately
            findAndHookMethod(clsPMS, "systemReady", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    Context mContext = (Context) getObjectField(param.thisObject, "mContext");
                    mContext.registerReceiver(new PackagePermissions(param.thisObject),
                            new IntentFilter("com.samsung.xposedexp.UPDATE_PERMISSIONS"),
                            "com.samsung.xposedexp" + ".BROADCAST_PERMISSION",
                            null);
                }
            });
        } catch (Throwable e) {
            XposedBridge.log("===" + e);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // The app broadcasted a request to update settings for a running
            // app

            // Validate the action being requested
            if (!"update_permissions".equals(intent.getExtras().getString("action")))
                return;

            String pkgName = intent.getExtras().getString("Package");
//            Toast.makeText(context, "Killing "+pkgName, Toast.LENGTH_LONG).show();
            boolean killApp = intent.getExtras().getBoolean("Kill", false);

            AdsHooks.sharedPrefs.reload();
            
            Object pkgInfo;
            synchronized (mPackages) {
                pkgInfo = mPackages.get(pkgName);
                callMethod(pmSvc, "grantPermissionsLPw", pkgInfo, true);
                callMethod(mSettings, "writeLPr");
            }

            // Apply new permissions if needed
            if (killApp) {
                try {
                    ApplicationInfo appInfo = (ApplicationInfo) getObjectField(pkgInfo,
                            "applicationInfo");
                    if (Build.VERSION.SDK_INT <= 18)
                        callMethod(pmSvc, "killApplication", pkgName, appInfo.uid);
                    else
                        callMethod(pmSvc, "killApplication", pkgName, appInfo.uid,
                                "apply App Settings");
                } catch (Throwable t) {
                    XposedBridge.log(t);
                }
            }

            Toast.makeText(context, "Settings saved!", Toast.LENGTH_LONG).show();

//            XposedBridge.log("sharedPrefs size = " +
//                    AdsHooks.sharedPrefs.getAll().size());
//            XposedBridge.log("sharedPrefs: exp_block_http = "
//                    + AdsHooks.sharedPrefs.getBoolean("exp_block_http", true));
//            XposedBridge.log("sharedPrefs: exp_change_location = "
//                    + AdsHooks.sharedPrefs.getBoolean("exp_change_location", true));

            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(30);
        } catch (Throwable t) {
            XposedBridge.log("+++" + t);
        }
    }
}
