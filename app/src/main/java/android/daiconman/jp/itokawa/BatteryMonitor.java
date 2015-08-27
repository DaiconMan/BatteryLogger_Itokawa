package android.daiconman.jp.itokawa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by KenNishimura on 15/07/19.
 */

public class BatteryMonitor {
    private BroadcastReceiver mReceiver = null;
    private Context myContext;

    private int health;
    private int icon_small;
    private int level;
    private int plugged;
    private boolean present;
    private int scale;
    private int status;
    private String technology;
    private float temperature;
    private int voltage;
    private String statusString;
    private String healthString;
    private String acString;

    public BatteryMonitor(Context context) {
        // init
        myContext = context;
        startMonitor();
    }

    public void startMonitor() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        myContext.registerReceiver(mBroadcastReceiver, filter);
    }

    public void stopMonitor() {
        // ブロードキャストレシーバ登録解除
        myContext.unregisterReceiver(mBroadcastReceiver);
    }

    public int getHealth() {
        return health;
    }

    public int getIcon_small() {
        return icon_small;
    }

    public int getLevel() {
        return level;
    }

    public int getPlugged() {
        return plugged;
    }

    public boolean getPresent() {
        return present;
    }

    public int getScale() {
        return scale;
    }

    public String getTechnology() {
        return technology;
    }

    public int getStatus() {
        return status;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getVoltage() {
        return voltage;
    }

    public String getStatusString() {
        return statusString;
    }

    public String getHealthString() {
        return healthString;
    }

    public String getAcString() {
        return acString;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                health = intent.getIntExtra("health", 0);
                icon_small = intent.getIntExtra("icon-small", 0);
                level = intent.getIntExtra("level", 0);
                plugged = intent.getIntExtra("plugged", 0);
                present = intent.getBooleanExtra("present", false);
                scale = intent.getIntExtra("scale", 0);
                status = intent.getIntExtra("status", 0);
                technology = intent.getStringExtra("technology");
                temperature = intent.getIntExtra("temperature", 0) / 10;
                voltage = intent.getIntExtra("voltage", 0);

                statusString = "";

                switch (status) {
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        statusString = "unknown";
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusString = "discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusString = "not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "full";
                        break;
                }

                healthString = "";

                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        healthString = "unknown";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        healthString = "good";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        healthString = "overheat";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        healthString = "dead";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        healthString = "voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        healthString = "unspecified failure";
                        break;
                }

                acString = "";

                switch (plugged) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        acString = "plugged ac";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        acString = "plugged usb";
                        break;
                }

//                Log.v("status", statusString);
//                Log.v("health", healthString);
//                Log.v("present", String.valueOf(present));
//                Log.v("level", String.valueOf(level));
//                Log.v("scale", String.valueOf(scale));
//                Log.v("icon_small", String.valueOf(icon_small));
//                Log.v("plugged", acString);
//                Log.v("voltage", String.valueOf(voltage));
//                Log.v("temperature", String.valueOf(temperature));
//                Log.v("technology", technology);
            }
        }
    };

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            destruction();
        }
    }

    private void destruction() {
        // 解放とか、破棄とか
        // stopMonitor();
    }
}
