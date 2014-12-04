Performance Evaluation Report
-------------------

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

|  | Mean (ms) | STD (ms) | Max (ms) | Min (ms) |
|: ---------- :| ------ | ----- | -------- | ----- |
| Hook | 0.007528 | 0.00092201 | 0.00895935 | 0.00690205 |
| Location | 0.030983 | 0.058025 | 0.192959 | 0.007375 |
| Http | 1.805 | 0.95311| 4.055916 | 0.997250 |
| **Subtotal** | 1.843511

