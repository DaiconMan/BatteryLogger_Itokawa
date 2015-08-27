package android.daiconman.jp.itokawa;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by KenNishimura on 15/07/17.
 */
public class CPUMonitor {
    private double CPU_U;

    private long getCpuTime() throws Exception {
        // カーネル全体の統計情報を表示する
        String [] cmdArgs = {"/system/bin/cat","/proc/stat"};

        String cpuLine    = "";
        StringBuffer cpuBuffer    = new StringBuffer();

        ProcessBuilder cmd = new ProcessBuilder(cmdArgs);
        Process process = cmd.start();

        InputStream in  = process.getInputStream();

        // 統計情報より1024バイト分を読み込む
        // cpu user/nice/system/idle/iowait/irq/softirq/steal/の情報を取得する

        byte[] lineBytes = new byte[1024];

        while(in.read(lineBytes) != -1 ) {
            cpuBuffer.append(new String(lineBytes));
        }
        in.close();
        cpuLine = cpuBuffer.toString();

        // 1024バイトより「cpu～cpu0」までの文字列を抽出
        int start = cpuLine.indexOf("cpu");
        int end = cpuLine.indexOf("cpu0");

        cpuLine = cpuLine.substring(start, end);
        long cputime = Long.parseLong(cpuLine.split(" ")[2]);
        return cputime;
    }

    private int setCPU_U() {
        long cpu_tmpA;
        long cpu_tmpB;
        try {
            cpu_tmpA = getCpuTime();
            Thread.sleep(1000);
            cpu_tmpB = getCpuTime();
        } catch (Exception e) {
            Log.w("MyApp", "原因不明のエラー\n" + e);
            return -1;
        }

        if (cpu_tmpA >= cpu_tmpB) {
            long tmp = (cpu_tmpA - cpu_tmpB);
            CPU_U = (double) tmp / 10.0;
        } else if (cpu_tmpA < cpu_tmpB) {
            long tmp = (cpu_tmpB - cpu_tmpA);
            CPU_U = (double) tmp / 10.0;
        } else {
            return -1;
        }

        return 0;
    }

    public double getCPU_U() {
        setCPU_U();
        return CPU_U;
    }
}
