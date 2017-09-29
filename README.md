# JApi

```xml
<sinfo.ufrn.br.japi.JApiWebView
android:id="@+id/japiwebview"
android:layout_width="match_parent"
android:layout_height="match_parent" />
```

```java
JApiWebView japiWebView = (JApiWebView) findViewById(R.id.japiwebview);
japiWebView.loadJapiWebView("URL_BASE", "CLIENT_ID", "CLIENT_SECRET", this, new Intent(this, ResultActivity.class));
```

```gradle
compile 'ca.mimic:oauth2library:2.3.0'
```
