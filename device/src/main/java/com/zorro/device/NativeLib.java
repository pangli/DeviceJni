package com.zorro.device;

import androidx.annotation.Keep;

@Keep
public class NativeLib {

    // Used to load the 'device' library on application startup.
    static {
        System.loadLibrary("device");
    }

    /**
     * A native method that is implemented by the 'device' native library,
     * which is packaged with this application.
     */
    public native String call(String key, String defaultValue);
}