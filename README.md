# EnvMock

[ ![Download](https://api.bintray.com/packages/pecpwee/envMock/envMock/images/download.svg) ](https://bintray.com/pecpwee/envMock/envMock/_latestVersion)

## Introduce
it's an Android Library for mocking the signal of locating. No root permission require.

Support signal list : Wi-Fi,Cell and GPS.

Support Android 5.0 and above.

Although the minSdkVersion is 14, the installation of the library will return false and no mock will happend if the version of android is not Lollipop or above.

### Problem it solve:
With the library,there is no need for you to test the locating relative by going outside the room.（because there is no GPS signal in the room ）
And It also support mocking GPS,Wi-Fi and Cell signals on the Android Virtual Devices.

What you need to do is just record the signal by going outdoors once,and replay it whenever and wherever you like.

The App will receive the GPS callback, or get a recording Wi-Fi and Cell when they call the framework api to retrive Wi-Filist or Cell.




## HOW TO USE

### Add Dependency
It has been published to Jcenter repository
```
compile 'com.pecpwee.lib.envMock:library:1.3.8'
```

### Init
you should init the envMock library in the `attachBaseContext` method in the Application class:

There are two parts of init method,the record one and the replay one . you can init corresponding initialization method depends on what you need.

If you wanna record the signals ,you should config and install the recording module as below.

```
protected void attachBaseContext(Context base){
...
	RecordConfig recordConfig = new RecordConfig.Builder(this)
	.build();
	EnvMockInstaller.installRecordService(recordConfig);
...
}
```

If you wanna replay the signals,you should config and install the replay module as below.
```
protected void attachBaseContext(Context base){
	...
	PlayConfig playConfig = new PlayConfig.Builder(this)
	        .setAutoPlayMode(true)
	        .setAutoStopMode(true)
	        .build();
	EnvMockInstaller.installPlayService(playConfig);
	...
}
```

### Record some signal outside the house

Before you replay,you should have a recording file of the signal.
You can record the signal by using RecordController class.

In the class,you can also choose which module should be disable , where the records need to store, the sample rate,etc.

To start the record:
```
RecordController controller = new RecordController(context);
controller.start();
```
To stop the record
```
controller.stop();
```

The file will store at /sdcard/envMock path by default.it will record GPS,Wi-Fi and Cell Signal by default.
you should stop the recording process by calling `controller.stop() `before you  controller.

### Replay the signal Inside the house

There are two modes for replay the signal:
the Auto Mode and the Manual Mode.

if `PlayConfig.setAutoPlayMode(true)` or`PlayConfig.setAutoStopMode(true)` is set when building a config ,the envMock is now in the Auto Play mode.

#### The Auto replay mode
The replay will begin automatically when there is at lease one callback is registered.
The replay will stop automatically when there is no GPS callbacks in the system.

In the auto play mode,you are not supposed to have any contact with PlayController. A RuntimeException will occur when you are trying to get instance of the PlayController to configurate the library.
Because in the Auto Replay mode ,the envMock library take control of the start and stop automatically,any manual operation may cause unexpected result.
The only chance that you configurate the replay options in the Auto Replay mode is the time when you build a playConfig and called `EnvMockInstaller.installPlayService(playConfig)`

#### The Manual replay mode
you can control when the replay starts and finishes as you need.

First,you can get a replay controller by calling the method
```
PlayController.getInstance();
```
when we got the instance of PlayController,now we can control the start and stop,and something else options.
```
PlayController.getInstance().startPlay();
PlayController.getInstance().stopPlay();
```
you should stop the replaying process by calling `PlayController.getInstance().stop() `before you configure the controller.

It will read the record files in the /sdcard/envMock by default, you can tell the specific data file to the player by calling `setWiFiRecordFilePath(),setCellRecordFilePath() or setGpsRecordFilePath()`.
you can also choose the start position of the replay by calling `setBeginOffsetPercent`.


## HOW IT WORKS
It is based on Binder Hook.