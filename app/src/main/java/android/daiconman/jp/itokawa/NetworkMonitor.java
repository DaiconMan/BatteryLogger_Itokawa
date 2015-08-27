package android.daiconman.jp.itokawa;

import android.content.Context;
import android.net.TrafficStats;

/**
 * Created by KenNishimura on 15/07/23.
 */

public class NetworkMonitor {
    // private int myUid;

    NetworkMonitor(Context context) {
        // myUid = android.os.Process.myUid();
    }

    private long getTxBytes() {
        // return TrafficStats.getUidTxBytes(myUid);
        return 0;
    }

    private long getRxBytes() {
        // return TrafficStats.getUidRxBytes(myUid);
        return 0;
    }

    public long getTotalTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    public long getTotalRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }
}
