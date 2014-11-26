package com.samsung.xposedcaptest2;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.google.android.gms.location.*;
import com.google.android.gms.common.GooglePlayServicesClient;

import android.location.Location;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;


//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;



import java.util.HashMap;

import android.util.Log;


public class LoadedPackage implements IXposedHookLoadPackage {
    
    public static Context ctx = null;
    public static final String[] adsList = {
//        "com.accuweather.android",
        "com.google.android.gms.ads",
        "com.google.ads",
        "com.google.analytics.tracking.android",
        "com.amazon.device.associates",
        "com.tremorvideo.sdk.android.videoad",
        "com.IQzone.android.configuration",
        "com.amobee.adsdk",
        "com.amobee.richmedia",
        "com.IQzone.postitial",
        /* AroundMe */
        "com.tweakersoft.aroundme",
        "com.google.ads",
        "com.google.analytics.tracking.android",
        "com.mopub.mobileads"
    };
    
    public static HashMap<String, String> adsHM;
    
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        
//        if (!lpparam.packageName.equals("com.samsung.btang.locationservicetest"))
//            return;
        
//        if (!lpparam.packageName.equals("com.accuweather.android")) return;
        
        
        boolean flag = false;
        for(String s : adsList) if (lpparam.packageName.equals(s)) flag = true;        
        if(!flag) return;
//        
        XposedBridge.log("Loaded app: " + lpparam.packageName);
/*        
        findAndHookMethod("android.location.LocationManager", lpparam.classLoader, "getLastKnownLocation",
                String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("   calling package: " + lpparam.appInfo);
                XposedBridge.log("   Hooked method: " + param.method);
//              XposedBridge.log("   Method Callbacks: " + param);
                XposedBridge.log("   Method Args: " + param.args[0].toString());
            }
            
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Location loc = (Location) param.getResult();
                loc.setLatitude(100);
                loc.setLongitude(100);
                            
                XposedBridge.log("==> Result location is changed to: " + param.getResult().toString());
            }
        });
        
        findAndHookMethod("android.location.LocationManager.ListenerTransport", lpparam.classLoader, "onLocationChanged", 
                Location.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
                XposedBridge.log("   calling package: " + lpparam.appInfo);  
                

                XposedBridge.log("   Hooked method: " + param.method);
                XposedBridge.log("   Method Args: " + param.args[0].toString());
//                XposedBridge.log("   Arg0 Class: " + param.args[0].getClass());
                
                ((Location) param.args[0]).setLatitude(100);
                ((Location) param.args[0]).setLongitude(100);               
                
                XposedBridge.log("   Changed location to: " + param.args[0].toString());
                
//                Exception ex = new Exception();
//                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
        
        findAndHookMethod("com.google.android.gms.location.LocationClient", lpparam.classLoader, "getLastLocation", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
                XposedBridge.log("   calling package: " + lpparam.appInfo);
                XposedBridge.log("   Hooked method: " + param.method);
//                XposedBridge.log("   Method Args: " + param.args[0].toString());

//                Exception ex = new Exception();
//                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex)); 
                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("   Result location: " + param.getResult().toString());
                
                Location loc = (Location) param.getResult();
                loc.setLatitude(200);
                loc.setLongitude(200);
                            
                XposedBridge.log("   Result location is changed to: " + param.getResult().toString());

            }
        });
 */       
//        findAndHookMethod("com.google.android.gms.location.LocationListener", lpparam.classLoader, "onLocationChanged", 
//                Location.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                // this will be called before the clock was updated by the original method
//                XposedBridge.log("   calling package: " + lpparam.appInfo);
//                XposedBridge.log("   Hooked method: " + param.method);
//                XposedBridge.log("   Method Args: " + param.args);
//
////                Exception ex = new Exception();
////                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));
//                
//                ((Location) param.args[0]).setLatitude(200);
//                ((Location) param.args[0]).setLongitude(200);               
//                
//                XposedBridge.log("   Changed location to: " + param.args[0].toString());
//                
//            }
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("   Result location: " + param.getResult().toString());
//            }
//        });
        
//      
//        findAndHookMethod("com.samsung.btang.locationservicetest.MainActivity", lpparam.classLoader, "onLocationChanged", 
//                Location.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                // this will be called before the clock was updated by the original method
//                XposedBridge.log("   calling package: " + lpparam.appInfo);
//                XposedBridge.log("   Hooked method: " + param.method);
//                XposedBridge.log("   Method Args: " + param.args);
//
////                Exception ex = new Exception();
////                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));
//                
//                ((Location) param.args[0]).setLatitude(200);
//                ((Location) param.args[0]).setLongitude(200);               
//                
//                XposedBridge.log("   Changed location to: " + param.args[0].toString());
//                
//            }
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("   Result location: " + param.getResult().toString());
//            }
//        });

        
        
//        findAndHookMethod("android.location.LocationManager.ListenerTransport", lpparam.classLoader, "onLocationChanged", 
//                android.location.Location.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                // this will be called before the clock was updated by the original method
//                XposedBridge.log("   calling package: " + lpparam.appInfo);  
//                
//
//                XposedBridge.log("   Hooked method: " + param.method);
//                XposedBridge.log("   Method Args: " + param.args[0].toString());
//                
//                android.location.Location loc = new android.location.Location(param.args[0].toString());
//                
//                loc.setLatitude(100);
//                loc.setLongitude(155.2333);                
//                param.args[0] = loc;
//                
//                XposedBridge.log("   Changed location to: " + param.args[0].getClass());
//                
////                Exception ex = new Exception();
////                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));                
//            }
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//            }
//        });
        
       
//        findAndHookMethod("com.c.d.bo.tang.test2", lpparam.classLoader, "returnTick", String.class, new XC_MethodReplacement() {
//
//            @Override
//            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                
//                return "1000" + param.args[0].toString();
//            }
//            
//        });
        
        /******************** Begin Internet Interception ***************************/
        
        findAndHookMethod("java.net.URL", lpparam.classLoader, "openConnection", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Exception ex = new Exception();
                String s = Log.getStackTraceString(ex);
                if(s.contains("com.tweakersoft.aroundme") && !s.contains("com.google.maps.api")){
                    XposedBridge.log("   calling package: " + lpparam.appInfo.packageName);
                    XposedBridge.log("   Hooked method: " + param.method);
    //                XposedBridge.log("   URL: " + param.thisObject.toString());
                   
    //                Throwable t = new IOException("This URL is blocked for privacy protection.");
    //                param.setThrowable(t);
                    
                    XposedBridge.log("   calling trace: " + s);
                }
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                try{
//                    XposedBridge.log("   Result: " + param.getResultOrThrowable().toString() );
//                }
//                catch(Exception e){
//                    XposedBridge.log("   Result: null" );
//                }
            }
        });
        
        
        
        final Class<?> httpClient = findClass("org.apache.http.impl.client.DefaultHttpClient", lpparam.classLoader);

        XposedBridge.hookAllMethods(httpClient, "execute", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
                XposedBridge.log("   calling package: " + lpparam.appInfo.packageName);
                XposedBridge.log("   HttpClient Class: " + httpClient.getName());
                XposedBridge.log("   Param Class: " + param.getClass());
                XposedBridge.log("   Hooked method: " + param.method);
                XposedBridge.log("   Args: " + param.args[0].toString());
                
               
//                Throwable t = new IOException("This URL is blocked for privacy protection.");
//                param.setThrowable(t);
                
                Exception ex = new Exception();
                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex)); 
                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                try{
//                    XposedBridge.log("   Result: " + param.getResultOrThrowable().toString() );
//                }
//                catch(Exception e){
//                    XposedBridge.log("   Result: null" );
//                }
            }
        });
        
        
        findAndHookMethod("com.mopub.mobileads.MoPubView", lpparam.classLoader, "loadAd", XC_MethodReplacement.DO_NOTHING);
        
        Class<?> adview = findClass("com.mopub.mobileads.MoPubView", lpparam.classLoader);        
        
        
        XposedBridge.hookAllMethods(adview, "loadAd", new XC_MethodHook() {
            
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                
                XposedBridge.log("Detected MoPub loadAd in " + lpparam.appInfo.packageName);
                
//              Exception ex = new Exception();
//              XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex)); 
            }
            @Override
              protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                  try{
//                      XposedBridge.log("   Result: " + param.getResultOrThrowable().toString() );
//                  }
//                  catch(Exception e){
//                      XposedBridge.log("   Result: null" );
//                  }
              }
        });
        
    }
    
  }
