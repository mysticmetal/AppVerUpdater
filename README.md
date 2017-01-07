AppVerUpdater
---------------------

<p align="left">
 <a target="_blank" href="https://android-arsenal.com/api?level=14"><img src="https://img.shields.io/badge/API-14+-orange.svg"></a>
 <a target="_blank" href="https://github.com/atzcx/AppVerUpdater"><img src="https://jitpack.io/v/atzcx/AppVerUpdater.svg"></a>
 </p>

A library that checks for your apps' updates on your own server. API 14+ required.

<br>
<p align="left">
	<img src="https://github.com/atzcx/AppVerUpdater/blob/master/Screenshots/screenshots1.png" width="200px">
	<img src="https://github.com/atzcx/AppVerUpdater/blob/master/Screenshots/screenshots2.png" width="200px">
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
	<version>1.0</version>
</dependency>
```


or Gradle:
```JavaScript
compile 'com.github.atzcx:AppVerUpdater:1.0'
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
new AppVerUpdater(MainActivity.this)
	.setJSONUrl("http://example.com/update.json")
	.setDialogTitle("Availabe a new update!")
	.setDialogContent("Update %s mobile app to version %s \nFeatures: \n%s")
	.setDialogPositiveText("Update")
	.setDialogNegativeText("Later")
	.setProgressDialogMessage("Download updates...")
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
