
package com.samsung.xposedexp;

import static de.robv.android.xposed.XposedHelpers.findClass;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ContactsHooks implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.android.contacts"))
            return;

        XposedBridge.log("Loaded app: " + lpparam.packageName);

        try {
            final Class<?> cResolver = findClass("android.content.ContentResolver",
                    lpparam.classLoader);

            XposedBridge.hookAllMethods(cResolver, "query", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {

                    XposedBridge.log("   Hooked method: " + param.method);
                    XposedBridge.log("   uri=" + param.args[0]);
                }
            });
            
            /*---------------------------------------------------------------------*/

            final Class<?> cLoader = findClass("android.content.CursorLoader",
                    lpparam.classLoader);

            XposedBridge.hookAllConstructors(cLoader, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {
                    XposedBridge.log("   Hooked method: " + param.method);
                    if(param.args[1] != null){
                        XposedBridge.log("   uri=" + param.args[1]);
                    }                    
                }
            });
            
            XposedBridge.hookAllMethods(cLoader, "setUri", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {

                    XposedBridge.log("   Hooked method: " + param.method);
                    XposedBridge.log("   uri=" + param.args[0]);
                }
            });
            
            /*---------------------------------------------------------------------*/
            
            final Class<?> scLoader = findClass("android.support.v4.content.CursorLoader",
                    lpparam.classLoader);

            XposedBridge.hookAllConstructors(scLoader, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {
                    XposedBridge.log("   Hooked method: " + param.method);
                    if(param.args[1] != null){
                        XposedBridge.log("   uri=" + param.args[1]);
                    }                    
                }
            });
            
            XposedBridge.hookAllMethods(scLoader, "setUri", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {

                    XposedBridge.log("   Hooked method: " + param.method);
                    XposedBridge.log("   uri=" + param.args[0]);
                }
            });

        } catch (Throwable t) {
            throw t;
        }
    }
}
