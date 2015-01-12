
package com.samsung.xposedexp;

import static de.robv.android.xposed.XposedHelpers.findClass;

import java.lang.reflect.*;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.util.SparseArray;

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

//            XposedBridge.hookAllConstructors(sensorEQ, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    Exception ex = new Exception();
//                    String s = Log.getStackTraceString(ex);
//
//                    XposedBridge.log("   Hooked method: " + param.method);
//                    XposedBridge.log("   Listener:"
//                            + ((SensorEventListener) param.args[0]).getClass());
//
//                    XposedBridge.log("   calling trace: " + s);
//                }
//            });

            XposedBridge.hookAllMethods(sensorEQ, "dispatchSensorEvent", new
                    XC_MethodHook() {
                        @SuppressWarnings("unchecked")
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws
                                Throwable {
                            Exception ex = new Exception();
                            String stk = Log.getStackTraceString(ex);

                            XposedBridge.log("   Hooked method: " + param.method);       
                            XposedBridge.log("   SensorEvent: handle=" + param.args[0]);
                            XposedBridge.log("   SensorEvent: x=" + ((float[]) param.args[1])[0]);
                            XposedBridge.log("   SensorEvent: y=" + ((float[]) param.args[1])[1]);
                            XposedBridge.log("   SensorEvent: z=" + ((float[]) param.args[1])[2]);
                            XposedBridge.log("   SensorEvent: accuracy=" + param.args[2]);
                            XposedBridge.log("   SensorEvent: timestamp=" + param.args[3]);
                            
                            
                            XposedBridge.log("   Class: " + param.thisObject.getClass());
                            XposedBridge.log("   Enclosing Class: " + param.thisObject.getClass().getEnclosingClass());


                            Field field = param.thisObject.getClass().getEnclosingClass().getDeclaredField("sHandleToSensor");
                            field.setAccessible(true);
                            XposedBridge.log("   Field: " + field.toString());
                            int handle = (Integer) param.args[0];
                            Sensor ss = ((SparseArray<Sensor>) field.get(0)).get(handle);                            
                            XposedBridge.log("   SensorEvent: sensor=" + ss);
                            XposedBridge.log("   SensorEvent: sensorType=" + (ss.getType() == Sensor.TYPE_ACCELEROMETER));                            
                        }
                    });

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

                            // XposedBridge.log("   Hooked method: " +
                            // param.method);
                            // XposedBridge.log("   SensorEvent: x=" +
                            // ((SensorEvent)param.args[0]).values[0]);
                            // XposedBridge.log("   SensorEvent: y=" +
                            // ((SensorEvent)param.args[0]).values[1]);
                            // XposedBridge.log("   SensorEvent: z=" +
                            // ((SensorEvent)param.args[0]).values[2]);
                            // XposedBridge.log("   SensorEvent: accuracy=" +
                            // ((SensorEvent)param.args[0]).accuracy);
                            // XposedBridge.log("   SensorEvent: timestamp=" +
                            // ((SensorEvent)param.args[0]).timestamp);

                            // XposedBridge.log("   calling trace: " + s);
                        }
                    });
        } catch (Throwable t) {
            throw t;
        }

    }

}
