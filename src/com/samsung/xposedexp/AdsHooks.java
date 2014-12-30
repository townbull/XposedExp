
package com.samsung.xposedexp;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.google.android.gms.ads.search.SearchAdView;

public class AdsHooks implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public static final String TAG = "XposedExp";
    public static final String[] adsList = {
            // "com.accuweather.android",
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
            // "com.tweakersoft.aroundme",
            "com.google.ads",
            "com.google.analytics.tracking.android",
            "com.mopub.mobileads"
    };

    private static int OVERHEAD_TEST_SIZE = 1000;
    public static final MovingAverage avgHttpOverHeadNanos = new MovingAverage(OVERHEAD_TEST_SIZE);
    public static final MovingAverage avgLocOverHeadNanos = new MovingAverage(OVERHEAD_TEST_SIZE);
    public static XSharedPreferences sharedPrefs;

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam)
            throws Throwable {
        loadPrefs();
        PackagePermissions.initHooks();
    }

    public static void loadPrefs() {
        sharedPrefs = new XSharedPreferences("com.samsung.xposedexp", "expSettings");
        sharedPrefs.makeWorldReadable();
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable
    {

        if (
        // !lpparam.packageName.equals("com.accuweather.android") &&
        !lpparam.packageName.equals("com.tweakersoft.aroundme") &&
                !lpparam.packageName.equals("com.samsung.xposedexp"))
            return;

        XposedBridge.log("Loaded app: " + lpparam.packageName);

        // if(sharedPrefs.hasFileChanged()) loadPrefs();

        sharedPrefs.reload();

        // XposedBridge.log("sharedPrefs size = " +
        // sharedPrefs.getAll().size());
        // XposedBridge.log("sharedPrefs: exp_block_http = "
        // + sharedPrefs.getBoolean("exp_block_http", true));
        // XposedBridge.log("sharedPrefs: exp_change_location = "
        // + sharedPrefs.getBoolean("exp_change_location", true));

        try {

//            final Class<?> lt = findClass("android.location.LocationManager", lpparam.classLoader);
//            Method[] mds = XposedHelpers.findMethodsByExactParameters(lt, null, Location.class);
//            for (Method m : mds) {
//                XposedBridge.log("%%%% Found method: " + m.getDeclaringClass() + "#" +
//                        m.getName());
//            }

            findAndHookMethod("android.location.LocationManager", lpparam.classLoader,
                    "getLastKnownLocation",
                    String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            XposedBridge.log("   calling package: " + lpparam.appInfo);
//                            XposedBridge.log("   Hooked method: " + param.method);
//                            // XposedBridge.log("   Method Callbacks: " +
//                            // param);
//                            XposedBridge.log("   Method Args: " + param.args[0].toString());
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            sharedPrefs.reload();
                            if (sharedPrefs.getBoolean("exp_change_location", true))
                            {
                                if (param.getResult() != null) {

                                    long start = System.nanoTime();
                                    // Exception ex = new Exception();
                                    // String s = Log.getStackTraceString(ex);
                                    // for (String ad : adsList) {
                                    // if (s.contains(ad)) {
                                    // XposedBridge
                                    // .log("================== Ads requesting location ==============");
                                    // XposedBridge.log(Log.getStackTraceString(ex));

                                    XposedBridge.log("   Result location: "
                                            + param.getResult().toString());
                                    Location loc = (Location) param.getResult();
                                    // set location to San Antonio
                                    loc.setLatitude(29.4241219);
                                    loc.setLongitude(-98.4936282);

                                    XposedBridge
                                            .log("==> [android] Result location is changed to: "
                                                    + param.getResult().toString());
                                    // }
                                    // }
                                    long end = System.nanoTime();
                                    XposedBridge
                                            .log("### Overhead for Get Last Location (nano sec.): "
                                                    + avgLocOverHeadNanos.next(end - start));
                                }
                            }
                        }
                    });

            findAndHookMethod("android.location.LocationManager.ListenerTransport",
                    lpparam.classLoader, "onLocationChanged",
                    Location.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            sharedPrefs.reload();
                            if (sharedPrefs.getBoolean("exp_change_location", true))
                            {
                                long start = System.nanoTime();
                                // Exception ex = new Exception();
                                // String s = Log.getStackTraceString(ex);
                                // for (String ad : adsList) {
                                // if (s.contains(ad)) {
                                // XposedBridge
                                // .log("================== Ads requesting location ==============");
                                // XposedBridge.log(Log.getStackTraceString(ex));

                                // XposedBridge.log("   Hooked method: "
                                // +
                                // param.method);
                                // XposedBridge.log("   Method Args: " +
                                // param.args[0].toString());
                                // // XposedBridge.log("   Arg0 Class: "
                                // +
                                // param.args[0].getClass());

                                Location loc = (Location) param.args[0];
                                // set location to San Antonio
                                loc.setLatitude(29.4241219);
                                loc.setLongitude(-98.4936282);

                                // XposedBridge.log("   Changed location to: "
                                // +
                                // param.args[0].toString());

                                // Exception ex = new Exception();
                                // XposedBridge.log("   calling trace: "
                                // +
                                // Log.getStackTraceString(ex));
                                // }
                                // }
                                long end = System.nanoTime();

                                XposedBridge.log("### Overhead for Location Change (nano sec.): "
                                        + (end - start));
                            }
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        }
                    });

            /****************************** BEGIN GMS (NOT USED) ********************/

            // findAndHookMethod("com.google.android.gms.location.LocationClient"
            // , lpparam.classLoader, "getLastLocation", new XC_MethodHook() {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam
            // param) throws Throwable {
            // XposedBridge.log("   calling package: " + lpparam.appInfo);
            // XposedBridge.log("   Hooked method: " + param.method); //
            // XposedBridge.log("   Method Args: " + param.args[0].toString());
            //
            // }
            //
            // @Override
            // protected void afterHookedMethod(MethodHookParam param)
            // throws Throwable {
            // if (param.getResult() != null) {
            // XposedBridge.log("   Result location: " +
            // param.getResult().toString());
            // Location loc = (Location)
            // param.getResult(); // set location to
            // // San Antonio
            // loc.setLatitude(29.4241219);
            // loc.setLongitude(-98.4936282);
            // XposedBridge.log("==> [gms] Result location is changed to: " +
            // param.getResult().toString());
            // }
            // }
            // });
            //
            // findAndHookMethod("com.google.android.gms.internal.ly$a",
            // lpparam.classLoader, "handleMessage", android.os.Message.class,
            // new XC_MethodHook() {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam
            // param) throws Throwable {
            // XposedBridge.log("   calling package: " + lpparam.appInfo);
            // XposedBridge.log("   Hooked method: " + param.method);
            // XposedBridge.log("   Method Args: " + param.args[0].toString());
            // Location loc = (Location)
            // ((android.os.Message) param.args[0]).obj;
            // // set location to San Antonio
            // loc.setLatitude(29.4241219);
            // loc.setLongitude(-98.4936282);
            // XposedBridge.log("===> [gms] Changed location to: " +
            // param.args[0].toString());
            // }
            //
            // @Override
            // protected void afterHookedMethod(MethodHookParam param)
            // throws Throwable {
            // }
            // });

            /********************** END OF GMS ***********************/

            // try {
            // findAndHookMethod(Activity.class, "onPause", new XC_MethodHook()
            // {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam param) throws
            // Throwable {
            //
            // XposedBridge.log("   Calling Package: " +
            // lpparam.appInfo.packageName);
            // XposedBridge.log("===> Hooked Method: " +
            // param.method);
            // }
            //
            // @Override
            // protected void afterHookedMethod(MethodHookParam param) throws
            // Throwable {
            //
            // }
            // });
            // } catch (Throwable e) {
            // XposedBridge.log(e);
            // }
            //
            // try {
            // findAndHookMethod(Activity.class, "onResume", new XC_MethodHook()
            // {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam param) throws
            // Throwable {
            // Exception ex = new Exception();
            // String s = Log.getStackTraceString(ex);
            //
            // XposedBridge.log("   Calling Package: " +
            // lpparam.appInfo.packageName);
            // XposedBridge.log("===> Hooked Method: " +
            // param.method);
            // // XposedBridge.log("   calling trace: " + s);
            // }
            //
            // @Override
            // protected void afterHookedMethod(MethodHookParam param) throws
            // Throwable {
            //
            // }
            // });
            // } catch (Throwable e) {
            // XposedBridge.log(e);
            // }

            final Class<?> url = findClass("java.net.URL", lpparam.classLoader);
            XposedBridge.hookAllConstructors(url, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    sharedPrefs.reload();
                    // XposedBridge.log("sharedPrefs size = " +
                    // sharedPrefs.getAll().size());
                    // XposedBridge.log("sharedPrefs: exp_block_http = "
                    // + sharedPrefs.getBoolean("exp_block_http", true));
                    // XposedBridge.log("sharedPrefs: exp_change_location = "
                    // + sharedPrefs.getBoolean("exp_change_location", true));

                    if (sharedPrefs.getBoolean("exp_block_http", true))
                    {
                        long start = System.nanoTime();

                        Exception ex = new Exception();
                        String s = Log.getStackTraceString(ex);

//                        StackTraceElement[] ste = new
//                                Exception().getStackTrace();
//                        for (int i = 0; i < ste.length; i++) {
//                            XposedBridge.log("   Caller [" + i + "]: " +
//                                    ste[i].getClassName()
//                                    + "#" + ste[i].getMethodName());
//                        }

                        for (String ad : adsList) {
                            if (s.contains(ad)) {
                                XposedBridge.log("==> Ads Provider: " + ad);
                                XposedBridge.log("   Calling Package: " +
                                        lpparam.appInfo.packageName);
                                XposedBridge.log("   Hooked Method: " +
                                        param.method);

                                XposedBridge.log("   calling trace: " + s);

//                                if (param.args.length != 1)
//                                    return;
//                                String org_url = (String) param.args[0];
//
//                                XposedBridge.log("=== org_url: " + org_url);
//
//                                // modify the value of 'q' in query (location
//                                // param)
//
//                                String[] queries = org_url.split("&");
//
//                                if (queries.length < 2)
//                                    break;
//
//                                // FIXME: more sophisticated URL decoding and
//                                // encoding
//                                HashSet<String> hs_cat = new HashSet<String>();
//                                hs_cat.add("Banks%2FATM");
//                                hs_cat.add("Bars");
//                                hs_cat.add("Coffee%20Shops");
//                                hs_cat.add("Deal");
//                                hs_cat.add("Gas%20Stations");
//                                hs_cat.add("Hospitals");
//                                hs_cat.add("Hotels");
//                                hs_cat.add("Movie%20Theaters");
//                                hs_cat.add("Movies");
//                                hs_cat.add("Parking");
//                                hs_cat.add("Pharmacies");
//                                hs_cat.add("Pubs");
//                                hs_cat.add("Restaurants");
//                                hs_cat.add("Supermarkets");
//                                hs_cat.add("Taxis");
//                                hs_cat.add("Theaters");
//
//                                for (int i = 0; i < queries.length; i++) {
//                                    if (queries[i].startsWith("q=")) {
//                                        String p = queries[i];
//                                        int k = p.indexOf("%20", 2);
//                                        if (!hs_cat.contains(p.substring(2, k)))
//                                            k = p.indexOf("%20", k + 3);
//
//                                        queries[i] = queries[i].substring(0, k)
//                                                + "%20San%20Antonio%20United%20States";
//                                        break;
//                                    }
//                                }
//
//                                String new_url = queries[0];
//                                for (int i = 1; i < queries.length; i++) {
//                                    new_url = new_url + "&" + queries[i];
//                                }
//
//                                XposedBridge.log(" +++ url is changed to: " + new_url);
//                                param.args[0] = new_url;

                            }
                        }
//                        long end = System.nanoTime();
//                        XposedBridge.log("*** Overhead for HTTP block (nano sec.): "
//                                + (end - start));
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                }
            });

            // final Class<?> url = findClass("java.net.URL",
            // lpparam.classLoader);
            // XposedBridge.hookAllConstructors(url, new XC_MethodHook() {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam param) throws
            // Throwable {
            // sharedPrefs.reload();
            // XposedBridge.log("sharedPrefs size = " +
            // sharedPrefs.getAll().size());
            // XposedBridge.log("sharedPrefs: exp_block_http = "
            // + sharedPrefs.getBoolean("exp_block_http", true));
            // XposedBridge.log("sharedPrefs: exp_change_location = "
            // + sharedPrefs.getBoolean("exp_change_location", true));
            //
            // if (sharedPrefs.getBoolean("exp_block_http", true))
            // {
            // long start = System.nanoTime();
            //
            // Exception ex = new Exception();
            // String s = Log.getStackTraceString(ex);
            // for (String ad : adsList) {
            // if (s.contains(ad)) {
            // try {
            // param.setResult(null);
            // } catch (Throwable t) {
            // param.setThrowable(t);
            // }
            // }
            // }
            // long end = System.nanoTime();
            // XposedBridge.log("*** Overhead for HTTP block (nano sec.): "
            // + (end - start));
            // }
            // }
            //
            // @Override
            // protected void afterHookedMethod(MethodHookParam param) throws
            // Throwable {
            // }
            // });

            findAndHookMethod("android.webkit.WebView", lpparam.classLoader, "loadUrl",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            sharedPrefs.reload();
                            if (sharedPrefs.getBoolean("exp_block_http", true))
                            {
                                long start = System.nanoTime();

                                Exception ex = new Exception();
                                String s = Log.getStackTraceString(ex);

                                StackTraceElement[] ste = new Exception().getStackTrace();
                                for (int i = 0; i < ste.length; i++) {
                                    XposedBridge.log("   Caller [" + i + "]: "
                                            + ste[i].getClassName()
                                            + "#" + ste[i].getMethodName());
                                }

                                for (String ad : adsList) {
                                    if (s.contains(ad)) {
                                        XposedBridge.log("===> Ads Provider: " + ad);
                                        XposedBridge.log("   Calling Package: "
                                                + lpparam.appInfo.packageName);
                                        XposedBridge.log("   Hooked method: " + param.method);
                                        // XposedBridge.log("   URL: " +
                                        // param.thisObject.toString());

                                        // Throwable t = new
                                        // IOException("This URL is blocked for privacy protection.");
                                        // param.setThrowable(t);

                                        XposedBridge.log("   calling trace: " + s);

                                        try {
                                            param.setResult(null);
                                        } catch (Throwable t) {
                                            param.setThrowable(t);
                                        }
                                    }
                                }
                                long end = System.nanoTime();

                                // XposedBridge.log("*** Average Overhead for HTTP block (nano sec.): "
                                // + avgHttpOverHeadNanos.next(end-start));
                            }
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        }
                    });

            final Class<?> httpClient = findClass("org.apache.http.impl.client.AbstractHttpClient",
                    lpparam.classLoader);

            XposedBridge.hookAllMethods(httpClient, "execute", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    sharedPrefs.reload();
                    if (sharedPrefs.getBoolean("exp_block_http", true))
                    {
                        long start = System.nanoTime();
                        Exception ex = new Exception();
                        String s = Log.getStackTraceString(ex);

                        for (String ad : adsList) {
                            if (s.contains(ad)) {
                                XposedBridge.log("===> Ads Provider: " + ad);
                                XposedBridge.log("   Calling Package: "
                                        + lpparam.appInfo.packageName);
                                XposedBridge.log("   Hooked method: " + param.method);

                                XposedBridge.log("   calling trace: " + s);

                                try {
                                    param.setResult(null);
                                } catch (Throwable t) {
                                    param.setThrowable(t);
                                }
                            }
                        }
                        long end = System.nanoTime();

                        // XposedBridge.log("*** Average Overhead for HTTP block (nano sec.): "
                        // + avgHttpOverHeadNanos.next(end-start));
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                }
            });

            // Method m = findMethodBestMatch(httpClient, "execute");
            // XposedBridge.hookMethod(m, new XC_MethodHook() {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam param) throws
            // Throwable {
            // // this will be called before the clock was updated by the
            // original
            // method
            // XposedBridge.log("   calling package: " +
            // lpparam.appInfo.packageName);
            // XposedBridge.log("   HttpClient Class: " + httpClient.getName());
            // XposedBridge.log("   Param Class: " + param.getClass());
            // XposedBridge.log("   Hooked method: " + param.method);
            // XposedBridge.log("   Args: " + param.args[0].toString());
            //
            //
            // Exception ex = new Exception();
            // XposedBridge.log("   calling trace: " +
            // Log.getStackTraceString(ex));
            //
            // }
            // @Override
            // protected void afterHookedMethod(MethodHookParam param) throws
            // Throwable {
            // }
            // });

            final Class<?> aHttpClient = findClass("android.net.http.AndroidHttpClient",
                    lpparam.classLoader);

            XposedBridge.hookAllMethods(aHttpClient, "execute", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    sharedPrefs.reload();
                    if (sharedPrefs.getBoolean("exp_block_http", true))
                    {
                        Exception ex = new Exception();
                        String s = Log.getStackTraceString(ex);
                        for (String ad : adsList) {
                            if (s.contains(ad)) {
                                XposedBridge.log("===> ad = " + ad);
                                XposedBridge.log("   Calling package: "
                                        + lpparam.appInfo.packageName);
                                XposedBridge.log("   Hooked method: " + param.method);
                                // XposedBridge.log("   URL: " +
                                // param.thisObject.toString());

                                // Throwable t = new
                                // IOException("This URL is blocked for privacy protection.");
                                // param.setThrowable(t);

                                // XposedBridge.log("   calling trace: " + s);

                                try {
                                    param.setResult(null);
                                } catch (Throwable t) {
                                    param.setThrowable(t);
                                }
                            }
                        }

                        // Exception ex = new Exception();
                        // XposedBridge.log("   calling trace: " +
                        // Log.getStackTraceString(ex));
                    }

                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                }
            });

            Class<?> adview = findClass("com.google.ads.AdView",
                    lpparam.classLoader);
            XposedBridge.hookAllMethods(adview, "loadAd",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam
                                param) throws Throwable {
                            XposedBridge
                                    .log("Detected com.google.ads.AdView loadAd in "
                                            +
                                            lpparam.appInfo.packageName);
                            XposedBridge.log("   Hooked method: " + param.method);

                            Exception ex = new Exception();
                            XposedBridge.log("   calling trace: " +
                                    Log.getStackTraceString(ex));
                        }
                    });

            // Class<?> adview = findClass("com.mopub.mobileads.MoPubView",
            // lpparam.classLoader);
            // XposedBridge.hookAllMethods(adview,
            // "loadAd", new XC_MethodHook() {
            // @Override
            // protected void beforeHookedMethod(MethodHookParam
            // param) throws Throwable {
            // XposedBridge.log("Detected MoPub loadAd in " +
            // lpparam.appInfo.packageName);
            //
            // // Exception ex = new Exception();
            // // XposedBridge.log("   calling trace: " +
            // // Log.getStackTraceString(ex));
            // }
            //
            // @Override
            // protected void afterHookedMethod(MethodHookParam param)
            // throws Throwable {
            // }
            // });

            // findAndHookMethod("com.mopub.mobileads.MoPubView",
            // lpparam.classLoader, "loadAd", new XC_MethodReplacement() {
            // @Override
            // protected Object replaceHookedMethod(MethodHookParam
            // param) throws Throwable {
            // XposedBridge.log("Blocked MoPub loadAd in " +
            // lpparam.appInfo.packageName);
            // return null;
            // }
            // });

        } catch (Throwable t) {
            throw t;
        }
    }
}
