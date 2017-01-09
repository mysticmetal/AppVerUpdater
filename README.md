AppVerUpdater
---------------------

<p align="left">
 <a target="_blank" href="https://android-arsenal.com/api?level=14"><img src="https://img.shields.io/badge/API-14+-orange.svg"></a>
 <a href="https://github.com/atzcx/AppVerUpdater"><img src="https://jitpack.io/v/atzcx/AppVerUpdater.svg"></a>
 <a target="_blank" href="https://android-arsenal.com/details/1/5050"><img src="https://img.shields.io/badge/Android%20Arsenal-AppVerUpdater-blue.svg"></a>
 </p>


<p align="center">
<img src="https://github.com/atzcx/AppVerUpdater/blob/master/screenshots/screenshot.png" >
</p>

How to include
--------------

Add the repository to your project build.gradle:
```JavaScript
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```
Maven:
```JavaScript
<dependency>
	<groupId>com.github.atzcx</groupId>
	<artifactId>AppVerUpdater</artifactId>
	<version>1.0.0</version>
</dependency>
```


or Gradle:
```JavaScript
compile 'com.github.atzcx:AppVerUpdater:1.0.0'
```

Usage
--------

Add permissions to your app's Manifest:
```JavaScript
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

Basic Usage
-------------------
Activity and Fragment

```Java
new AppVerUpdater(this)
	.setUpdateJSONUrl("http://example.com/update.json")
	.setViewNotes(true)
	.build();
```

Example JSON
------------------

```Json
{
  "newVersion": "4.4.0",
  "apkUrl": "https://example.net/example.apk",
  "versionNotes": [
    "- Bug fixes"
  ]
}
```

String Resourses
---------------------

```Txt
<string name="appverupdate_dialog_title">Available a new update!</string>
<string name="appverupdater_dialog_content">Update %1$s mobile app to version %2$s</string>
<string name="appverupdater_dialog_content_notes">Update %1$s mobile app to version %2$s \nFeatures: \n%3$s</string>
<string name="appverupdater_dialog_positivetext">Update</string>
<string name="appverupdater_dialog_negativetext">Later</string>
<string name="appver_updater_progressdialog_message">Download</string>
```

License
----------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
