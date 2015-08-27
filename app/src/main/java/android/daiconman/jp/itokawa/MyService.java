package android.daiconman.jp.itokawa;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KenNishimura on 15/07/23.
 */

public class MyService extends Service{
    private final String TAG = "Itokawa";

    private Timer mTimer;
    private Handler mHandler;
    private CPUMonitor cpu;
    private MemoryMonitor mem;
    private ScreenMonitor sm;
    private BatteryMonitor bm;
    private NetworkMonitor nm;
    private ExportCSV eCSV;

    private static long TotalMem;
    private static long AvailMem;
    private static long availMem_AM;
    private static long threshold_AM;
    private static boolean lowMemory_AM;
    private static long totalPrivateDirty_AM;
    private static long totalPss_AM;
    private static long totalSharedDirty_AM;
    private static double cpu_u;
    private static int brightness;

    private static int health;
    private static int icon_small;
    private static int level;
    private static int plugged;
    private static boolean present;
    private static int scale;
    private static int status;
    private static String technology;
    private static float temperature;
    private static int voltage;

    private static long totalTxBytes;
    private static long totalRxBytes;

    private static boolean serviceFlag = false;

    private static String expStartDate;
    private static String nowDate;

    public static long getTotalMem() {
        return TotalMem;
    }

    public static long getAvailMem() {
        return AvailMem;
    }

    public static long getAvailMem_AM() {
        return availMem_AM;
    }

    public static long getThreshold_AM() {
        return threshold_AM;
    }

    public static boolean isLowMemory_AM() {
        return lowMemory_AM;
    }

    public static long getTotalPrivateDirty_AM() {
        return totalPrivateDirty_AM;
    }

    public static long getTotalPss_AM() {
        return totalPss_AM;
    }

    public static long getTotalSharedDirty_AM() {
        return totalSharedDirty_AM;
    }

    public static double getCpu_u() {
        return cpu_u;
    }

    public static int getBrightness() {
        return brightness;
    }

    public static int getHealth() {
        return health;
    }

    public static int getIcon_small() {
        return icon_small;
    }

    public static int getLevel() {
        return level;
    }

    public static int getPlugged() {
        return plugged;
    }

    public static boolean isPresent() {
        return present;
    }

    public static int getScale() {
        return scale;
    }

    public static int getStatus() {
        return status;
    }

    public static String getTechnology() {
        return technology;
    }

    public static float getTemperature() {
        return temperature;
    }

    public static int getVoltage() {
        return voltage;
    }

    public static long getTotalTxBytes() {
        return totalTxBytes;
    }

    public static long getTotalRxBytes() {
        return totalRxBytes;
    }

    public static boolean isServiceFlag() {
        return serviceFlag;
    }

    public static String getExpStartDate() {
        return expStartDate;
    }

    public static String getNowDate() {
        return nowDate;
    }

    //サービスに接続するためのBinder
    public class MyServiceLocalBinder extends Binder {
        //サービスの取得
        MyService getService() {
            return MyService.this;
        }
    }
    //Binderの生成
    private final IBinder mBinder = new MyServiceLocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "MyService#onBind" + ": " + intent, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onBind" + ": " + intent);

        // 実験(記録開始)
        startExp();
        serviceFlag = true;

        // 日付を記録
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd','kk':'mm':'ss");
        expStartDate = sdf.format(date);

        return mBinder;
    }

    @Override
    public void onRebind(Intent intent){
        Toast.makeText(this, "MyService#onRebind"+ ": " + intent, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onRebind" + ": " + intent);
    }

    @Override
    public boolean onUnbind(Intent intent){
        Toast.makeText(this, "MyService#onUnbind"+ ": " + intent, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onUnbind" + ": " + intent);

        stopExp();
        expStartDate = null;

        //onUnbindをreturn trueでoverrideすると次回バインド時にonRebildが呼ばれる
        return true;
    }

    private void startExp() {
        mHandler = new Handler();
        mTimer = new Timer(true);

        cpu = new CPUMonitor();
        mem = new MemoryMonitor(this);
        sm = new ScreenMonitor(this);
        bm = new BatteryMonitor(this);
        nm = new NetworkMonitor(this);
        eCSV = new ExportCSV(this);

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 別スレッドで動くらしい... 動くのでよしとします

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd','kk':'mm':'ss");
                nowDate = sdf.format(date);

                cpu_u = cpu.getCPU_U();

                TotalMem = mem.getTotalMem();
                AvailMem = mem.getAvailMem();

                availMem_AM = mem.getAvailMem_AM();
                threshold_AM = mem.getThreshold_AM();
                lowMemory_AM = mem.getlowMemory_AM();
                totalPrivateDirty_AM = mem.getTotalPrivateDirty_AM();
                totalPss_AM = mem.getTotalPss_AM();
                totalSharedDirty_AM = mem.getTotalSharedDirty_AM();

                brightness = sm.getBrightness();

                health = bm.getHealth();
                icon_small = bm.getIcon_small();
                level = bm.getLevel();
                plugged = bm.getPlugged();
                present = bm.getPresent();
                scale = bm.getScale();
                status = bm.getStatus();
                technology = bm.getTechnology();
                temperature = bm.getTemperature();
                voltage = bm.getVoltage();

                totalTxBytes = nm.getTotalTxBytes();
                totalRxBytes = nm.getTotalRxBytes();

                eCSV.writeCSV();

                // mHandlerを通じてUI Threadへ処理をキューイング
                // ↑ということは、mHandleを通じなければ別スレッドなのか？
                // どうやらそういうことらしいのでAsyncTasからこっちに変更
                mHandler.post(new Runnable() {
                    public void run() {
                        if (serviceFlag) {
                            MainActivity.cpuText.setText("CPU:" + cpu_u + "%");

                            MainActivity.totalMemText.setText("TotalMem:" + TotalMem + "Byte");
                            MainActivity.availMemText.setText("AvailMem:" + AvailMem + "Byte");

                            MainActivity.availMem_AMText.setText("AvailMem_AM:" + availMem_AM + "Byte");
                            MainActivity.threshold_AMText.setText("Threshold_AM:" + threshold_AM + "Byte");
                            MainActivity.lowMemory_AMText.setText("LowMemory_AM:" + ((lowMemory_AM) ? "True" : "False"));
                            MainActivity.totalPrivateDirty_AMText.setText("TotalPrivateDirty_AM:" + totalPrivateDirty_AM + "Byte");
                            MainActivity.totalPss_AMText.setText("TotalPss_AM:" + totalPss_AM + "Byte");
                            MainActivity.totalSharedDirty_AMText.setText("TotalSharedDirty_AM:" + totalSharedDirty_AM + "Byte");
                            MainActivity.brightnessText.setText("brightness:" + brightness + "/255");

                            MainActivity.healthText.setText("health:" + health);
                            MainActivity.icon_smallText.setText("icon_small:" + icon_small);
                            MainActivity.levelText.setText("level:" + level);
                            MainActivity.pluggedText.setText("plugged:" + plugged);
                            MainActivity.presentText.setText("present:" + present);
                            MainActivity.scaleText.setText("scale:" + scale);
                            MainActivity.statusText.setText("status:" + status);
                            MainActivity.technologyText.setText("technology:" + technology);
                            MainActivity.temperatureText.setText("temperature:" + temperature);
                            MainActivity.voltageText.setText("voltage:" + voltage);

                            MainActivity.totalTxBytesText.setText("TotalTxBytes:" + totalTxBytes);
                            MainActivity.totalRxBytesText.setText("TotalRxBytes:" + totalRxBytes);

                            MainActivity.expStartDateText.setText("" + expStartDate);
                            MainActivity.nowDateText.setText("" + nowDate);

                            // Log.v(TAG, "CPU:" + cpu_u + "%");
                        } else {
                            MainActivity.cpuText.setText("");

                            MainActivity.totalMemText.setText("");
                            MainActivity.availMemText.setText("");

                            MainActivity.availMem_AMText.setText("");
                            MainActivity.threshold_AMText.setText("");
                            MainActivity.lowMemory_AMText.setText("");
                            MainActivity.totalPrivateDirty_AMText.setText("");
                            MainActivity.totalPss_AMText.setText("");
                            MainActivity.totalSharedDirty_AMText.setText("");
                            MainActivity.brightnessText.setText("");

                            MainActivity.healthText.setText("");
                            MainActivity.icon_smallText.setText("");
                            MainActivity.levelText.setText("");
                            MainActivity.pluggedText.setText("");
                            MainActivity.presentText.setText("");
                            MainActivity.scaleText.setText("");
                            MainActivity.statusText.setText("");
                            MainActivity.technologyText.setText("");
                            MainActivity.temperatureText.setText("");
                            MainActivity.voltageText.setText("");

                            MainActivity.totalTxBytesText.setText("");
                            MainActivity.totalRxBytesText.setText("");

                            MainActivity.expStartDateText.setText("");
                            MainActivity.nowDateText.setText("");
                        }
                    }
                });
            }
        }, 100, 1000);
    }

    private void stopExp() {
        serviceFlag = false;

        try {
            mTimer.cancel();
            bm.stopMonitor();

            Thread.sleep(2000);

            // mHandler = null;
            mTimer = null;
            cpu = null;
            mem = null;
            sm = null;
            bm = null;
            nm = null;

        } catch (Exception e) {
            Log.v(TAG, "なんかエラーだって");
        }
    }
}
