
package com.samsung.xposedexp;

import static de.robv.android.xposed.XposedHelpers.findClass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class SensorHooks implements IXposedHookLoadPackage {

    @SuppressWarnings("null")
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // com.nike.plusgps
        if (!lpparam.packageName.equals("com.samsung.sensorexp"))
            return;

        XposedBridge.log("Loaded app: " + lpparam.packageName);

        try {
            // com.samsung.sensorexp.SensorActivity
            final Class<?> sensorEQ = findClass(
                    "android.hardware.SystemSensorManager$SensorEventQueue",
                    lpparam.classLoader);

            final Class<?> seListener;

            XposedBridge.hookAllConstructors(sensorEQ, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Exception ex = new Exception();
                    String s = Log.getStackTraceString(ex);

                    XposedBridge.log("   Hooked method: " + param.method);
                    XposedBridge.log("   Listener:"
                            + ((SensorEventListener) param.args[0]).getClass());
                    // seListener = ((SensorEventListener)
                    // param.args[0]).getClass();
                    
                    XposedBridge.log("   calling trace: " + s);
                }
            });

            // XposedBridge.log("   seListener:" + seListener.toString());

            // XposedBridge.hookAllMethods(sensorEQ, "dispatchSensorEvent", new
            // XC_MethodHook() {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam param) throws
            // Throwable {
            // Exception ex = new Exception();
            // String s = Log.getStackTraceString(ex);
            //
            // XposedBridge.log("   Hooked method: " + param.method);
            // XposedBridge.log("   Sensor:" + ((Sensor)
            // param.args[0]).toString());
            // }
            // });

            final Class<?> sensorEL = findClass("com.samsung.sensorexp.SensorActivity",
                    lpparam.classLoader);

            // onSensorChanged
            XposedBridge.hookAllMethods(sensorEL, "onSensorChanged", new
                    XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws
                                Throwable {
                            Exception ex = new Exception();
                            String s = Log.getStackTraceString(ex);

//                            XposedBridge.log("   Hooked method: " + param.method);
//                            XposedBridge.log("   SensorEvent: x=" + ((SensorEvent)param.args[0]).values[0]);
//                            XposedBridge.log("   SensorEvent: y=" + ((SensorEvent)param.args[0]).values[1]);
//                            XposedBridge.log("   SensorEvent: z=" + ((SensorEvent)param.args[0]).values[2]);
//                            XposedBridge.log("   SensorEvent: accuracy=" + ((SensorEvent)param.args[0]).accuracy);
//                            XposedBridge.log("   SensorEvent: timestamp=" + ((SensorEvent)param.args[0]).timestamp);
                            
//                            XposedBridge.log("   calling trace: " + s);
                        }
                    });
        } catch (Throwable t) {
            throw t;
        }

    }

}
