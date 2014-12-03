
XPosed Investigation Notes
===========================

Deploy XPrivacy
-----------------
- Download the XPrivacy project source code from the GitHub repository.
	```
	$ cd ~/workspace
	$ git clone https://github.com/M66B/XPrivacy.git
	```

- Import the project into Eclipse > File > Import... > Android > Existing Android Code Into Workspace and click next. Click Browse... to select ~/workspace/XPrivacy and rename the project name to XPrivacy. Then, click finish.
- Add Java Build Path Libraries: Right click the project > Build Path > Configure Build Path... > go to Libraries tab > click Add External JAR... > choose **android-support-v4.jar** and **android-support-v7-appcompat.jar** then click OK.
- Import the following two projects into the workspace and check the **Is Library** box in Properties > Android > Library for each project.

	<i class="icon-folder-open"></i> ~/adt/sdk/extras/android/support/v7/cardview
	<i class="icon-folder-open"></i> ~/adt/sdk/extras/android/support/v7/appcompat

- Add Android Library for the XPrivacy Project: Right click the project > Properties > Android > Library > Add... > choose the two projects above.
- (Optional:) Switch the Project Build Target to Android 4.4.2
>**Note: ** if switched to Android 4.4.2 some errors may emerge due to incompatible Android 5.0 functions, such as the heart rate sensor, which should be commented out as shown below.

	```
	/* ///////////// XSensorManager.java ////////////////
	} else if (type == Sensor.TYPE_HEART_RATE) {
				if (isRestricted(param, "heartrate"))
					return true;
	*/
	```


----------


Sample Module 1: XPrivacy
-------------------
### Useful Resources:

 * https://github.com/rovo89/XposedBridge/wiki/Development-tutorial
 * [https://github.com/rovo89/XposedBridge/wiki/Helpers](https://github.com/rovo89/XposedBridge/wiki/Helpers)
 *  [https://github.com/rovo89/XposedBridge/wiki/Replacing-resources](https://github.com/rovo89/XposedBridge/wiki/Replacing-resources)


### Steps:
1. Identify entrance(s) in assets > xposed_init file.
2.  


### Call Graph

```sequence
Title: Here is a title
A->B: Normal line
B-->C: Dashed line
C->>D: Open arrow
D-->>A: Dashed open arrow
```
----------


Sample Module 2: XPosedTrace
--------------------

### Settings solution using XSharedPreferences

#### Put Values

#### Get Values
<i class="icon-file"> Source code from **package com.asksven.xposed.trace**


```
import de.robv.android.xposed.XSharedPreferences;
...

public class XposedHooks implements IXposedHookLoadPackage
{
	public static final String TAG = "XposedTrace";
	
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable
	{
		
		XSharedPreferences sharedPrefs = new XSharedPreferences("com.asksven.xposedtrace");
//		XposedBridge.log("Loaded app: " + lpparam.packageName);
		
		if (!lpparam.packageName.equals("android"))
			return;
		
		...
		
		if ((sharedPrefs != null) && sharedPrefs.getBoolean("trace_location_manager_service", true))
		{
			systemLogforClass("com.android.server.LocationManagerService", "D", lpparam);
		}
		
		...
```

Location APIs Analysis
---------------------

### Android Location API (android.location)

| Class Name	|	Method Name	| Args Types		| Notes		|
|---------------|---------------|-------------------|-----------|
| android.location.LocationManager| getLastKnownLocation | String | works |
| android.location.LocationManager.ListenerTransport | onLocationChanged | Location | works |
 

### Google Location API (com.google.android.gms.location)
**Note:** Include *google-play-services_lib* as library

| Class Name	|	Method Name	| Args Types		| Notes		|
|---------------|---------------|-------------------|-----------|
| com.google.android.gms.location.LocationClient | getLastLocation | n/a | works |
| [Interface] com.google.android.gms.location.LocationListener | onLocationChanged | Location | Cannot hook interface |
| com.google.android.gms.internal.ly$a | handleMessage | android.os.Message | The public variable ***obj*** stores the Location for onLocationChanged() function. |


Internet APIs Analysis
---------------------
### two functions to intercept:
>java.net.URL.openConnection()
>org.apache.http.client.HttpClient.execute(org.apache.http.client.methods.HttpUriRequest)

### two apps:
AccuWeather -- com.accuweather.android
com.google.android.gms
com.google.ads
com.google.analytics.tracking.android
com.amazon.device.associates
com.tremorvideo.sdk.android.videoad
com.IQzone.android.configuration
com.amobee.adsdk
com.amobee.richmedia
com.IQzone.postitial

AroundMe -- com.tweakersoft.aroundme
com.google.android.gms.ads
com.google.ads
com.google.analytics.tracking.android
com.mopub.mobileads

### FAQ
- Which class to hook? The URL or the internet connection?
	- android.net.AndroidHttPClient (not found)
	- org.apache.http.impl.client.DefaultHttpClient (not found)
	- java.net.URL (some works)
- How to block the API calls and do nothing?

### Ads calling Http connection methods

| Ads Provider | Calling Methods  |
| -------------| ---------------- |
| com.google.android.gms.ads | | 
| com.google.ads | java.net.URL |
| com.google.analytics.tracking.android
| com.amazon.device.associates
| com.tremorvideo.sdk.android.videoad
| com.IQzone.android.configuration
| com.amobee.adsdk
| com.amobee.richmedia
| com.IQzone.postitial
| com.google.ads
| com.google.analytics.tracking.android
| com.mopub.mobileads | org.apache.http.impl.client.AbstractHttpClient.execute(HttpUriRequest) |
| | android.webkit.WebView.loadUrl(String) |


Performance Overhead
-------------------

| APP | Location Change Overhead (ms) | Ads Http Block Overhead (ms) | 
|: -------------| ---------------- | -------- |
| Accuweather | 4.9194 | 1.2418 |
| AroundMe | 3.1558 | 1.0663 |

><i class="icon-pencil">**Note:** The overhead time is measured on **Galaxy S5 SM-G900H** using the  **MovingAverage** as the following with the window size of **1000**.

```
import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
    private int window_size;
    private double cur_avg;
    private Queue<Double> win;

    public MovingAverage(int ws){
        if(ws <= 0) return;
        window_size = ws;
        cur_avg = 0;
        win = new LinkedList<Double> ();
    }

    public double next(double num){
        double sum = 0;
        sum = cur_avg * win.size() ;

        if(win.size() >= window_size)
            sum -= win.poll();
            
        win.add(num);
        cur_avg = (sum + num) / win.size();
        return cur_avg;
    }
}
```
***Xposed 2.4 beta1/beta2*** Hook Speed: overhead per call used to be ~13 μs (= 0.013 ms) per call to a hooked method (with one empty callback handler) on Galaxy S2.
http://forum.xda-developers.com/showpost.php?p=47771876
