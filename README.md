# RadioPlayer
Radio library for background play

## Implement

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.LikhanovD:RadioPlayer:version'
```

## Usage

Extend your service from RadioService. Register service in manifest. Call init and set URL.
Run like others MediaBrowserServiceCompat using mediaController.

## Example

```
class RadioPlayerService : RadioService() {

    override fun onCreate() {
        super.onCreate()
        init(this, RadioPlayerService::class.java)
        updateUrl(url)
        setSessionActivity(MainActivity::class.java)
        setDefaultDrawable(R.drawable.ic_track_placeholder)
        setNotificationDrawable(R.mipmap.ic_notification)
        setActivityForNotificationIntent(MainActivity::class.java)
    }
}
```

