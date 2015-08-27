package android.daiconman.jp.itokawa;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by KenNishimura on 15/07/21.
 */

public class ScreenMonitor {
    private String brightness;
    private Context myContext;

    ScreenMonitor(Context context) {
        myContext = context;
        setBrightness();
    }

    private void setBrightness() {
        try {
            brightness = Settings.System.getString(myContext.getContentResolver(), "screen_brightness");
        } catch (Exception e) {
            Log.w("MyApp", "エラー:" + e);
        }
    }

    public int getBrightness() {
        setBrightness();

        return Integer.parseInt(brightness);
    }
}
