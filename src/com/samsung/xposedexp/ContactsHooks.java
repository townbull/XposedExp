
package com.samsung.xposedexp;

import static de.robv.android.xposed.XposedHelpers.findClass;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.database.Cursor;
import android.net.Uri;

public class ContactsHooks implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        // com.android.contacts
        // com.android.dialer
        if (    !lpparam.packageName.equals("com.android.contacts") &&
                !lpparam.packageName.equals("com.android.dialer") &&
                !lpparam.packageName.equals("com.linkedin.android") &&
                !lpparam.packageName.equals("com.facebook.katana"))
            return;

        XposedBridge.log("Loaded app: " + lpparam.packageName);

        try {
            final Class<?> cResolver = findClass("android.content.ContentResolver",
                    lpparam.classLoader);

//            findAndHookMethod(cResolver, "query", android.net.Uri.class,java.lang.String[].class,
//                    java.lang.String.class,java.lang.String[].class,java.lang.String.class, new XC_MethodHook() {
            
            XposedBridge.hookAllMethods(cResolver, "query", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {
//                    XposedBridge.log("==>Hooked package: " + param.getClass().getPackage());
                    XposedBridge.log("   Hooked method: " + param.method);
                    
                    Uri uri = (Uri) param.args[0];
                    XposedBridge.log("   uri=" + uri + "-->" + uri.getHost()+uri.getPath());
                    
                    String[] pNames = (String[]) param.args[1];
                    boolean flag = false;
                    for(int i=0; i<pNames.length; i++){
                        XposedBridge.log("   Field[" + i + "] = " + pNames[i]);
                        if(pNames[i].equals("name") || pNames[i].equals("display_name")) 
                            flag = true;
                    }
                    
                  String selection = (String) param.args[2];
                  XposedBridge.log("   selection=" + selection);
                                        
////                    if((uri.getHost()+uri.getPath()).equals("com.android.contacts/profile") || 
//                  if(uri.getHost().equals("com.android.contacts") || 
//                       uri.getHost().equals("call_log")){
                  
                  if(flag){
                        String sName = "";
                        if(uri.getHost().equals("com.android.contacts")) sName = "display_name";
                        if(uri.getHost().equals("call_log")) sName = "name";
                        
                        if(selection == null || selection.isEmpty())
                            selection = sName + " NOT LIKE '*%' ";
                        else selection += " AND " + sName + " NOT LIKE '*%'";
                        
                        param.args[2] = selection;
                        XposedBridge.log("   new selection=" + param.args[2]);
                    }                
                        
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws
                        Throwable {

//                    XposedBridge.log("   Hooked method: " + param.method);
//                    String[] cNames = ((Cursor) param.getResult()).getColumnNames();
//                    for(int i=0; i<cNames.length; i++){
//                        XposedBridge.log("   Field[" + i + "] = " + cNames[i]);
//                    }
//                    
//                    Uri uri = (Uri) param.args[0];
//                    
//                    if((uri.getHost()+uri.getPath()).equals("com.android.contacts/contacts")){                        
//                        param.setResult(null);
//                    }          
                }
            });

            /*---------------------------------------------------------------------*/
            
            final Class<?> db = findClass("android.database.sqlite.SQLiteQueryBuilder",
                    lpparam.classLoader);
            XposedBridge.hookAllMethods(db, "query", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws
                        Throwable {
                    XposedBridge.log("   Hooked method: " + param.method);
//                    XposedBridge.log("   SQL: " + (String) param.args[0]);
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
                    if (param.args.length > 1) {
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

//            final Class<?> scLoader = findClass("android.support.v4.content.CursorLoader",
//                    lpparam.classLoader);
//
//            XposedBridge.hookAllConstructors(scLoader, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws
//                        Throwable {
//                    XposedBridge.log("   Hooked method: " + param.method);
//                    if (param.args[1] != null) {
//                        XposedBridge.log("   uri=" + param.args[1]);
//                    }
//                }
//            });
//
//            XposedBridge.hookAllMethods(scLoader, "setUri", new XC_MethodHook() {
//                protected void beforeHookedMethod(MethodHookParam param) throws
//                        Throwable {
//
//                    XposedBridge.log("   Hooked method: " + param.method);
//                    XposedBridge.log("   uri=" + param.args[0]);
//                }
//            });

        } catch (Throwable t) {
            throw t;
        }
    }
}
