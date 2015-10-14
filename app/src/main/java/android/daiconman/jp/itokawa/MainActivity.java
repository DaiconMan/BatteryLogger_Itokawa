package android.daiconman.jp.itokawa;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    static TextView cpuText;
    static TextView totalMemText;
    static TextView availMemText;
    static TextView availMem_AMText;
    static TextView threshold_AMText;
    static TextView lowMemory_AMText;
    static TextView totalPrivateDirty_AMText;
    static TextView totalPss_AMText;
    static TextView totalSharedDirty_AMText;
    static TextView brightnessText;
    static TextView healthText;
    static TextView icon_smallText;
    static TextView levelText;
    static TextView pluggedText;
    static TextView presentText;
    static TextView scaleText;
    static TextView statusText;
    static TextView technologyText;
    static TextView temperatureText;
    static TextView voltageText;
    static TextView totalTxBytesText;
    static TextView totalRxBytesText;
    static TextView expStartDateText;
    static TextView nowDateText;

    private Button BindButton;
    private Button UnbindButton;

    private Timer mTimer;
    private Handler mHandler;
    private MyService service;

    private static WindowManager.LayoutParams lp;

    //取得したServiceの保存
    private MyService mBoundService;
    private boolean mIsBound;

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.BindButton://doBindService
                    doBindService();
                    break;

                case R.id.UnbindButton://doUnbindService
                    doUnbindService();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text01 = (TextView) findViewById(R.id.textView);
        // text01.setText("ふじしゃわくんがんばれー");
        text01.setText("");
        text01.setTextSize(30.0f);

        cpuText = (TextView) findViewById(R.id.cpuText);

        totalMemText = (TextView) findViewById(R.id.totalMem);
        availMemText = (TextView) findViewById(R.id.availMem);

        availMem_AMText = (TextView) findViewById(R.id.availMem_AM);
        threshold_AMText = (TextView) findViewById(R.id.threshold_AM);
        lowMemory_AMText = (TextView) findViewById(R.id.lowMemory_AM);
        totalPrivateDirty_AMText = (TextView) findViewById(R.id.totalPrivateDirty_AM);
        totalPss_AMText = (TextView) findViewById(R.id.totalPss_AM);
        totalSharedDirty_AMText = (TextView) findViewById(R.id.totalSharedDirty_AM);

        brightnessText = (TextView) findViewById(R.id.brightnessText);

        healthText = (TextView) findViewById(R.id.healthText);
        icon_smallText = (TextView) findViewById(R.id.icon_smallText);
        levelText = (TextView) findViewById(R.id.levelText);
        pluggedText = (TextView) findViewById(R.id.pluggedText);
        presentText = (TextView) findViewById(R.id.presentText);
        scaleText = (TextView) findViewById(R.id.scaleText);
        statusText = (TextView) findViewById(R.id.statusText);
        technologyText = (TextView) findViewById(R.id.technologyText);
        temperatureText = (TextView) findViewById(R.id.temperatureText);
        voltageText = (TextView) findViewById(R.id.voltageText);

        totalTxBytesText = (TextView) findViewById(R.id.totalTxBytesText);
        totalRxBytesText = (TextView) findViewById(R.id.totalRxBytesText);

        expStartDateText  = (TextView) findViewById(R.id.expStartDateText);
        nowDateText = (TextView) findViewById(R.id.nowDateText);

        BindButton = (Button) findViewById(R.id.BindButton);
        UnbindButton = (Button) findViewById(R.id.UnbindButton);

        mHandler = new Handler();
        mTimer = new Timer(true);

        BindButton.setOnClickListener(btnListener);
        UnbindButton.setOnClickListener(btnListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            // サービスとの接続確立時に呼び出される
            Toast.makeText(MainActivity.this, "Activity:onServiceConnected",
                    Toast.LENGTH_SHORT).show();

            // サービスにはIBinder経由で#getService()してダイレクトにアクセス可能
            mBoundService = ((MyService.MyServiceLocalBinder)service).getService();

            //必要であればmBoundServiceを使ってバインドしたサービスへの制御を行う
        }

        public void onServiceDisconnected(ComponentName className) {
            // サービスとの切断(異常系処理)
            // プロセスのクラッシュなど意図しないサービスの切断が発生した場合に呼ばれる。
            mBoundService = null;
            Toast.makeText(MainActivity.this, "Activity:onServiceDisconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        //サービスとの接続を確立する。明示的にServiceを指定
        //(特定のサービスを指定する必要がある。他のアプリケーションから知ることができない = ローカルサービス)
        bindService(new Intent(MainActivity.this,
                MyService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // コネクションの解除
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}
