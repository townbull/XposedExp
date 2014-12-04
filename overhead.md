Xposed Performance Evaluation Report
==============

### How does Xposed work?

Xposed is a framework for modules that can change the behavior of the system and apps without touching any APKs. That's great because it means that modules can work for different versions and even ROMs without any changes (as long as the original code was not changed too much). It's also easy to undo. As all changes are done in the memory, you just need to deactivate the module and reboot to get your original system back. There are many other advantages, but here is just one more: Multiple modules can do changes to the same part of the system or app. With modified APKs, you to decide for one. No way to combine them, unless the author builds multiple APKs with different combinations.

The Xposed framework requires Root access for installation because it replaces a file in **/system/bin**. Once the framework is installed, it should work without root access.

The **/system/bin/app_process** executable is extended to load a JAR file on startup. The classes of this file will sit in every process (including the one for system services) and can act with their powers. And even more: some functions are implemented for developers to replace any method in any class (may it be in the framework, systemui or a custom app). This makes Xposed very powerful. You can change parameters for the method call, modify the return value or skip the call to the method completely - it's all up to you! Also replacing or adding resources is easy, thanks to many helpers in Xposed's API that developers can use.

Source: [http://forum.xda-developers.com/xposed/xposed-installer-versions-changelog-t2714053](http://forum.xda-developers.com/xposed/xposed-installer-versions-changelog-t2714053)

### Evaluation Results

According to the description above, the Xposed mechanism is summarized to be "Hook and Intercept". Thus, in order to benchmark the performance overhead introduced by Xposed framework, we conducted tests against both "Hook" and "Interception" processes. The test results are shown below.

All the tests are conducted with ***XposedBridge-v54*** and ***Android 4.4.2 (KitKat)*** on **Galaxy S5 SM-G900H**.

#### Xposed Hook Overhead


|  | Mean (ms) | STD (ms) | Max (ms) | Min (ms) |
|: ---------- :| ------ | ----- | -------- | ----- |
| Hook | 0.007528 | 0.00092201 | 0.00895935 | 0.00690205 |



Test method: [http://forum.xda-developers.com/showpost.php?p=47771876
](http://forum.xda-developers.com/showpost.php?p=47771876
)


#### Location and Http Interception Overhead

|  | Mean (ms) | STD (ms) | Max (ms) | Min (ms) |
|: ---------- :| ------ | ----- | -------- | ----- |
| Location | 0.030983 | 0.058025 | 0.192959 | 0.007375 |
| Http | 1.80500 | 0.95311| 4.055916 | 0.997250 |

#### Combined Overhead per Call

The combined overhead is the sum of Hook and interceptions, which is considered as the estimated overhead for intercepting Location and Http connections with Xposed. On average, the combined overhead is about 1.84 ms per call.

|  | Mean (ms) | STD (ms) | Max (ms) | Min (ms) |
|: ---------- :| ------ | ----- | -------- | ----- |
| Hook | 0.007528 | 0.00092201 | 0.00895935 | 0.00690205 |
| Location | 0.030983 | 0.058025 | 0.192959 | 0.007375 |
| Http | 1.805 | 0.95311| 4.055916 | 0.997250 |
| **Overall** | 1.843511

