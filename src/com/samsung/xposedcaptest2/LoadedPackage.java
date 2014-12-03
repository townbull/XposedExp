package com.samsung.xposedcaptest2;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;
import static de.robv.android.xposed.XposedHelpers.findClass;

import java.lang.reflect.Method;


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
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.internal.ly;

import android.location.Location;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

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
//        "com.tweakersoft.aroundme",
        "com.google.ads",
        "com.google.analytics.tracking.android",
        "com.mopub.mobileads"
    };
    
    public static HashMap<String, String> adsHM;
    private static int OVERHEAD_TEST_SIZE = 1000;
    public static final MovingAverage avgHttpOverHeadNanos = new MovingAverage(OVERHEAD_TEST_SIZE);
    public static final MovingAverage avgLocOverHeadNanos = new MovingAverage(OVERHEAD_TEST_SIZE);
    
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        
//        if (!lpparam.packageName.equals("com.samsung.btang.locationservicetest"))
//            return;

        if (!lpparam.packageName.equals("com.accuweather.android") &&
            !lpparam.packageName.equals("com.tweakersoft.aroundme") ) return;
        
        
//        boolean flag = false;
//        for(String s : adsList) if (lpparam.packageName.equals(s)) flag = true;        
//        if(!flag) return;
//        
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        
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
                if(param.getResult() != null){
                    
                    long start = System.nanoTime();

                    XposedBridge.log("   Result location: " + param.getResult().toString());
                    Location loc = (Location) param.getResult();
                    //set location to San Antonio
                    loc.setLatitude(29.4241219);
                    loc.setLongitude(-98.4936282);
                                
                    XposedBridge.log("==> [android] Result location is changed to: " + param.getResult().toString());
                    long end = System.nanoTime();                    
                    XposedBridge.log("### Average Overhead for Location Change (nano sec.): " + avgLocOverHeadNanos.next(end-start));
                }
            }
        });
        
        findAndHookMethod("android.location.LocationManager.ListenerTransport", lpparam.classLoader, "onLocationChanged", 
                Location.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                long start = System.nanoTime();
                

                XposedBridge.log("   Hooked method: " + param.method);
                XposedBridge.log("   Method Args: " + param.args[0].toString());
//                XposedBridge.log("   Arg0 Class: " + param.args[0].getClass());
                
                Location loc = (Location) param.args[0];
                //set location to San Antonio
                loc.setLatitude(29.4241219);
                loc.setLongitude(-98.4936282);              
                
                XposedBridge.log("   Changed location to: " + param.args[0].toString());
                
//                Exception ex = new Exception();
//                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));
                
                long end = System.nanoTime();
                
                XposedBridge.log("### Average Overhead for Location Change (nano sec.): " + avgLocOverHeadNanos.next(end-start));
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
  
 /*       *****************************BEGIN GMS (NOT USED)********************
        
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
                if(param.getResult() != null){
                    XposedBridge.log("   Result location: " + param.getResult().toString());
                    
                    Location loc = (Location) param.getResult();
                    //set location to San Antonio
                    loc.setLatitude(29.4241219);
                    loc.setLongitude(-98.4936282);
                                
                    XposedBridge.log("==> [gms] Result location is changed to: " + param.getResult().toString());
                } 
            }
        });
 
        
//        findAndHookMethod("com.google.android.gms.location.LocationListener", lpparam.classLoader, "onLocationChanged", 
//            Location.class, new XC_MethodHook() {        
        
        findAndHookMethod("com.google.android.gms.internal.ly$a", lpparam.classLoader, "handleMessage", 
                android.os.Message.class, new XC_MethodHook() {        
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
                XposedBridge.log("   calling package: " + lpparam.appInfo);
                XposedBridge.log("   Hooked method: " + param.method);
                XposedBridge.log("   Method Args: " + param.args[0].toString());

//                Exception ex = new Exception();
//                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));
                
                Location loc = (Location) ((android.os.Message) param.args[0]).obj;
                    //set location to San Antonio
                loc.setLatitude(29.4241219);
                loc.setLongitude(-98.4936282);             
                
                XposedBridge.log("===> [gms] Changed location to: " + param.args[0].toString());
                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
        
 
                  **********************END OF GMS**********************                        */
        
//        findAndHookMethod("com.samsung.btang.locationservicetest.MainActivity", lpparam.classLoader, "onLocationChanged", 
//              Location.class, new XC_MethodHook() {
//          @Override
//          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//              // this will be called before the clock was updated by the original method
//              XposedBridge.log("   calling package: " + lpparam.appInfo);
//              XposedBridge.log("   Hooked method: " + param.method);
//              XposedBridge.log("   Method Args: " + param.args);
//
//              Exception ex = new Exception();
//              XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex));
//              
//              ((Location) param.args[0]).setLatitude(200);
//              ((Location) param.args[0]).setLongitude(200);               
//              
//              XposedBridge.log("   Changed location to: " + param.args[0].toString());
//              
//          }
//          @Override
//          protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////              XposedBridge.log("   Result location: " + param.getResult().toString());
//          }
//      });
        
        
        /******************** Begin HTTP Blocks ***************************/
     
        final Class<?> url = findClass("java.net.URL", lpparam.classLoader);
 
//      findAndHookMethod("java.net.URL", lpparam.classLoader, "openConnection", new XC_MethodHook() {

        XposedBridge.hookAllConstructors(url, new XC_MethodHook() {        
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                
                long start = System.nanoTime();

                Exception ex = new Exception();
                String s = Log.getStackTraceString(ex);
                for(String ad : adsList){
                    if(s.contains(ad)){
                        XposedBridge.log("==> Ads Provider: " + ad);
                        XposedBridge.log("   Calling Package: " + lpparam.appInfo.packageName);
                        XposedBridge.log("   Hooked Method: " + param.method);
                        
//                        XposedBridge.log("   calling trace: " + s);

                    
                        try {
                            param.setResult(null);
                        } catch (Throwable t) {
                            param.setThrowable(t);
                        }
                    }
                }
                long end = System.nanoTime();
                
                XposedBridge.log("*** Average Overhead for HTTP block (nano sec.): " + avgHttpOverHeadNanos.next(end-start));
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
        
        

        findAndHookMethod("android.webkit.WebView", lpparam.classLoader, "loadUrl", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                long start = System.nanoTime();

                Exception ex = new Exception();
                String s = Log.getStackTraceString(ex);
                for(String ad : adsList){
                    if(s.contains(ad)){
                        XposedBridge.log("===> Ads Provider: " + ad);
                        XposedBridge.log("   Calling Package: " + lpparam.appInfo.packageName);
                        XposedBridge.log("   Hooked method: " + param.method);
        //                XposedBridge.log("   URL: " + param.thisObject.toString());
                       
        //                Throwable t = new IOException("This URL is blocked for privacy protection.");
        //                param.setThrowable(t);
                        
//                        XposedBridge.log("   calling trace: " + s);
                        
                        try {
                            param.setResult(null);
                        } catch (Throwable t) {
                            param.setThrowable(t);
                        }
                    }
                }
                long end = System.nanoTime();
                
                XposedBridge.log("*** Average Overhead for HTTP block (nano sec.): " + avgHttpOverHeadNanos.next(end-start));
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
      
        final Class<?> httpClient = findClass("org.apache.http.impl.client.AbstractHttpClient", lpparam.classLoader);

        XposedBridge.hookAllMethods(httpClient, "execute", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                
                long start = System.nanoTime();
                Exception ex = new Exception();
                String s = Log.getStackTraceString(ex);
                for(String ad : adsList){
                    if(s.contains(ad)){
                        XposedBridge.log("===> Ads Provider: " + ad);
                        XposedBridge.log("   Calling Package: " + lpparam.appInfo.packageName);
                        XposedBridge.log("   Hooked method: " + param.method);
                   
//                        XposedBridge.log("   calling trace: " + s);
                        
                        try {
                            param.setResult(null);
                        } catch (Throwable t) {
                            param.setThrowable(t);
                        }
                    }
                }
                long end = System.nanoTime();
               
                XposedBridge.log("*** Average Overhead for HTTP block (nano sec.): " + avgHttpOverHeadNanos.next(end-start));
                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
        
//        Method m = findMethodBestMatch(httpClient, "execute");
//        XposedBridge.hookMethod(m, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                // this will be called before the clock was updated by the original method
//                XposedBridge.log("   calling package: " + lpparam.appInfo.packageName);
//                XposedBridge.log("   HttpClient Class: " + httpClient.getName());
//                XposedBridge.log("   Param Class: " + param.getClass());
//                XposedBridge.log("   Hooked method: " + param.method);
//                XposedBridge.log("   Args: " + param.args[0].toString());
//                
//               
//                Exception ex = new Exception();
//                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex)); 
//                
//            }
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//            }
//        });        
        
        
        final Class<?> aHttpClient = findClass("android.net.http.AndroidHttpClient", lpparam.classLoader);

        XposedBridge.hookAllMethods(aHttpClient, "execute", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Exception ex = new Exception();
                String s = Log.getStackTraceString(ex);
                for(String ad : adsList){
                    if(s.contains(ad)){
                        XposedBridge.log("===> ad = " + ad);
                        XposedBridge.log("   Calling package: " + lpparam.appInfo.packageName);
                        XposedBridge.log("   Hooked method: " + param.method);
        //                XposedBridge.log("   URL: " + param.thisObject.toString());
                       
        //                Throwable t = new IOException("This URL is blocked for privacy protection.");
        //                param.setThrowable(t);
                        
                        XposedBridge.log("   calling trace: " + s);
                        
                        try {
                            param.setResult(null);
                        } catch (Throwable t) {
                            param.setThrowable(t);
                        }
                    }
                }
                
//                Exception ex = new Exception();
//                XposedBridge.log("   calling trace: " + Log.getStackTraceString(ex)); 
                
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
        
        /*      
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
              }
        });
        
        findAndHookMethod("com.mopub.mobileads.MoPubView", lpparam.classLoader, "loadAd", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("Blocked MoPub loadAd in " + lpparam.appInfo.packageName);
                return null;
            }
        });*/

        
    }
    
  }
