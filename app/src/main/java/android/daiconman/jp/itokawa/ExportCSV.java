package android.daiconman.jp.itokawa;import android.content.Context;import android.os.Environment;import android.util.Log;import java.io.BufferedWriter;import java.io.File;import java.io.FileNotFoundException;import java.io.FileOutputStream;import java.io.IOException;import java.io.OutputStreamWriter;import java.io.PrintWriter;import java.io.UnsupportedEncodingException;import java.util.ArrayList;/** * Created by KenNishimura on 15/07/24. */public class ExportCSV {    private Context context;    private boolean mkdirFlag;    private boolean mkfileFlag;    private String target_dir;    private static String target_file;    private static BufferedWriter bw;    ExportCSV(Context context) {        this.context = context;        target_dir = Environment.getExternalStorageDirectory().getPath() + "/Itokawa/";        File file_e = new File(target_dir);        if (!file_e.exists()) {            // ディレクトリが存在しない場合生成する            mkdirFlag = mkdir(target_dir);            if (mkdirFlag) {                Log.v("Itokawa", "ディレクトリ生成に成功");                target_file = target_dir + "ExpNo1.csv";                mkfileFlag = mkfile(target_file);                csvTemplate();            } else {                Log.v("Itokawa", "ディレクトリ生成に失敗");            }        } else {            // ディレクトリが存在する場合            //target_dirにあるcsvファイルを取得する。            ArrayList<String> searchFilesArrayList = searchFiles(target_dir, ".*..csv", false);            target_file = target_dir + "ExpNo" + (searchFilesArrayList.size() + 1) + ".csv";            mkfileFlag = mkfile(target_file);            csvTemplate();        }    }    private boolean mkdir(String path){        File file = new File(path);        return file.mkdir();    }    private boolean mkfile(String path){        File file = new File(path);        try{            return file.createNewFile();        }catch(IOException e){            System.out.println(e);            return false;        }    }    private boolean csvTemplate() {        // CSV書き込み準備        FileOutputStream outputStream_filePath;        try {            outputStream_filePath = new FileOutputStream(target_file, true);            OutputStreamWriter output_filePath_sw = new OutputStreamWriter(outputStream_filePath, "UTF-8");            PrintWriter output_filePath_pw = new PrintWriter(output_filePath_sw);            String write =                    "NowDate," +                    "NowTime" +                    "ExpStartDate," +                    "ExpStartTime," +                    "CPU[%]," +                    "TotalMem[Byte]," +                    "AvailMem_AM[Byte]," +                    "Threshold_AM[Byte]," +                    "LowMemory_AM[T/F]," +                    "TotalPrivateDirty_AM[KByte]," +                    "TotalPss_AM[KByte]," +                    "TotalSharedDirty_AM[KByte]," +                    "Brightness[0-255]," +                    "Health_Battery," +                    "Icon_small_Battery," +                    "Level_Battery," +                    "Plugged_Battery," +                    "Present_Battery," +                    "Scale_Battery," +                    "Status_Battery," +                    "Technology_Battery," +                    "Temperature_Battery," +                    "Voltage_Battery," +                    "TotalTxBytes(on Boot)[Byte]," +                    "TotalRxBytes(on Boot)[Byte]," +                    "\n";            output_filePath_pw.append(write);            output_filePath_pw.close();            output_filePath_sw.close();            outputStream_filePath.close();            return true;        } catch (UnsupportedEncodingException e) {            e.printStackTrace();            return false;        } catch (FileNotFoundException e) {            e.printStackTrace();            return false;        } catch (IOException e) {            e.printStackTrace();            return false;        }    }    private ArrayList<String> searchFiles(String dir_path, String expr, boolean search_subdir) {        final File dir = new File(dir_path);        ArrayList<String> find_files = new ArrayList<String>();        final File[] files = dir.listFiles();        if(null != files){            for(int i = 0; i < files.length; ++i) {                if(!files[i].isFile()){                    if(search_subdir){                        ArrayList<String> sub_files = searchFiles(files[i].getPath(), expr, search_subdir);                        find_files.addAll(sub_files);                    }                    continue;                }                final String filename = files[i].getName();                if((null == expr) || filename.matches(expr)){                    find_files.add(dir.getPath() + "/" + filename);                }            }        }        return find_files;    }    public void writeCSV() {        // CSV書き込み準備        FileOutputStream outputStream_filePath;        try {            outputStream_filePath = new FileOutputStream(target_file, true);            OutputStreamWriter output_filePath_sw = new OutputStreamWriter(outputStream_filePath, "UTF-8");            PrintWriter output_filePath_pw = new PrintWriter(output_filePath_sw);            String write =                    MyService.getNowDate() + "," +                    MyService.getExpStartDate() + "," +                    MyService.getCpu_u() + "," +                    MyService.getTotalMem() + "," +                    MyService.getAvailMem() + "," +                    MyService.getThreshold_AM() + "," +                    MyService.isLowMemory_AM() + "," +                    MyService.getTotalPrivateDirty_AM() + "," +                    MyService.getTotalPss_AM() + "," +                    MyService.getTotalSharedDirty_AM() + "," +                    MyService.getBrightness() + "," +                    MyService.getHealth() + "," +                    MyService.getIcon_small() + "," +                    MyService.getLevel() + "," +                    MyService.getPlugged() + "," +                    MyService.isPresent() + "," +                    MyService.getScale() + "," +                    MyService.getStatus() + "," +                    MyService.getTechnology() + "," +                    MyService.getTemperature() + "," +                    MyService.getVoltage() + "," +                    MyService.getTotalTxBytes() + "," +                    MyService.getTotalRxBytes() + "," +                    "\n";            output_filePath_pw.append(write);            output_filePath_pw.close();            output_filePath_sw.close();            outputStream_filePath.close();        } catch (UnsupportedEncodingException e) {            e.printStackTrace();        } catch (FileNotFoundException e) {            e.printStackTrace();        } catch (IOException e) {            e.printStackTrace();        }    }}