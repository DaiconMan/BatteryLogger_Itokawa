package android.daiconman.jp.itokawa;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by KenNishimura on 15/07/21.
 */

public class ScreenMonitor {
    private int systemBrightness;
    private float windowBrightness;
    private Context myContext;

    ScreenMonitor(Context context) {
        myContext = context;
    }

    private void setSystemBrightness() {
        try {
            // brightness = Settings.System.getString(myContext.getContentResolver(), "screen_brightness");
            systemBrightness = Settings.System.getInt(myContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            // Log.w("MyApp", "systemBrightness:" + systemBrightness);
        } catch (Exception e) {
            // Log.w("MyApp", "エラー:" + e);
        }
    }

    public int getSystemBrightness() {
        setSystemBrightness();
        return systemBrightness;
    }
}
