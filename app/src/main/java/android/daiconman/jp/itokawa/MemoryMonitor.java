package android.daiconman.jp.itokawa;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.io.FileInputStream;

/**
 * Created by KenNishimura on 15/07/19.
 */

public class MemoryMonitor {
    private ActivityManager am;
    private ActivityManager.MemoryInfo mi;
    private android.os.Debug.MemoryInfo[] dmi;

    MemoryMonitor(Context context) {
        // システムで利用可能な空きメモリー
        am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 自プロセスが使用中のメモリー
        int[] pids = new int[1];
        pids[0] = android.os.Process.myPid();
        dmi = am.getProcessMemoryInfo(pids);
    }

    // 物理空きメモリーの取得[byte]
    public long getTotalMem() {
        return readTotalMem();
    }

    // 物理合計メモリーの取得[byte]
    public long getAvailMem() {
        return readAvailMem();
    }

    // 利用可能なメモリーサイズ
    public long getAvailMem_AM() {
        return mi.availMem;
    }

    // 利用可能なメモリーサイズが不足の時、解放するか判断する、しきい値(B)
    public long getThreshold_AM() {
        return mi.threshold;
    }

    // システムがメモリ不足と判断しているか
    public boolean getlowMemory_AM() {
        return mi.lowMemory;
    }

    // 使用中のメモリーサイズ(KB)
    public long getTotalPrivateDirty_AM() {
        return dmi[0].getTotalPrivateDirty();
    }

    // プロセスの使用メモリ合計サイズ(KB)
    public long getTotalPss_AM() {
        return dmi[0].getTotalPss() * 1024;
    }

    // 共有メモリーの使用合計サイズ(KB)
    public long getTotalSharedDirty_AM() {
        return dmi[0].getTotalSharedDirty();
    }

    /**
     * 物理空きメモリーの取得
     * @return long
     */
    private static long readAvailMem() {
        byte[] buffer = new byte[1024];
        try {
            long memFree = 0;
            long memCached = 0;
            FileInputStream is = new FileInputStream("/proc/meminfo");
            int len = is.read(buffer);
            is.close();
            final int BUFLEN = buffer.length;
            for (int i=0; i<len && (memFree == 0 || memCached == 0); i++) {
                if (matchText(buffer, i, "MemFree")) {
                    i += 7;
                    memFree = extractMemValue(buffer, i);
                } else if (matchText(buffer, i, "Cached")) {
                    i += 6;
                    memCached = extractMemValue(buffer, i);
                }
                while (i < BUFLEN && buffer[i] != '\n') {
                    i++;
                }
            }
            return memFree + memCached;
        } catch (java.io.FileNotFoundException e) {
            // ファイルが存在しない
        } catch (java.io.IOException e) {
            // ファイルへのアクセスに失敗かな？
        }
        return 0;
    }

    /**
     * 物理合計メモリーの取得
     * @return long
     */
    private static long readTotalMem() {
        byte[] buffer = new byte[1024];
        try {
            long memTotal = 0;
            FileInputStream is = new FileInputStream("/proc/meminfo");
            int len = is.read(buffer);
            is.close();
            final int BUFLEN = buffer.length;
            for (int i=0; i<len && (memTotal == 0); i++) {
                if (matchText(buffer, i, "MemTotal")) {
                    i += 8;
                    memTotal = extractMemValue(buffer, i);
                }
                while (i < BUFLEN && buffer[i] != '\n') {
                    i++;
                }
            }
            return memTotal;
        } catch (java.io.FileNotFoundException e) {
            // ファイルが存在しない
        } catch (java.io.IOException e) {
            // ファイルへのアクセスに失敗
        }
        return 0;
    }

    private static boolean matchText(byte[] buffer, int index, String text) {
        int N = text.length();
        if ((index+N) >= buffer.length) {
            return false;
        }
        for (int i=0; i<N; i++) {
            if (buffer[index+i] != text.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static long extractMemValue(byte[] buffer, int index) {
        while (index < buffer.length && buffer[index] != '\n') {
            if (buffer[index] >= '0' && buffer[index] <= '9') {
                int start = index;
                index++;
                while (index < buffer.length && buffer[index] >= '0'
                        && buffer[index] <= '9') {
                    index++;
                }
                @SuppressWarnings("deprecation")
                String str = new String(buffer, 0, start, index-start);
                return ((long)Integer.parseInt(str)) * 1024;
            }
            index++;
        }
        return 0;
    }
}
