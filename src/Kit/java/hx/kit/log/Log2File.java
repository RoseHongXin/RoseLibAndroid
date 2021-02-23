package hx.kit.log;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by RoseHongXin on 2017/9/5 0005.
 *
 * Meizu 5.0 dir 不能写Logs
 *
 */

public class Log2File {

    private static final String FILE_NAME_DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //    private static final int CACHE_QUEUE_SIZE = Integer.MAX_VALUE / 0xffff;
    private static final int CACHE_QUEUE_SIZE = 80960;
    public static final String DEFAULT_DIR = "_Logs";
    private static final SimpleDateFormat mFileNameDateFormat = new SimpleDateFormat(FILE_NAME_DATE_FORMAT, Locale.CHINA);
    private static final SimpleDateFormat mLogDateFormat = new SimpleDateFormat(LOG_DATE_FORMAT, Locale.CHINA);

    private static File mFile;
    private static ExecutorService sLogExecutor = Executors.newSingleThreadExecutor();
    private static Queue<String> sMsgQueue = new ArrayBlockingQueue<>(CACHE_QUEUE_SIZE);
    private static String mDir;

    public static String getPath(String dir){
        File f = new File(Environment.getExternalStorageDirectory().toString() + File.separator + dir + File.separator);
        if (!f.exists()) {
            if(f.mkdirs()) return f.getAbsolutePath();
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }

    public static void init(){
        init(DEFAULT_DIR);
    }
    public static void init(String dir){
//        if(dir != null && dir.charAt(dir.length() - 1) != '/') dir = dir + "/";
        mDir = dir;
        String path = getPath(TextUtils.isEmpty(mDir) ? DEFAULT_DIR : DEFAULT_DIR + File.separator + mDir);
        File dirFile = new File(path);
        if(!dirFile.exists()) dirFile.mkdirs();
        mFile = new File(path, mFileNameDateFormat.format(new Date()) + ".log");
        Log4Android.v("Log2File", "Init log file: " + mFile.getAbsolutePath());
    }

    public static void e(Object obj, String msg){ write2File("--e--" + obj, msg);}
    public static void w(Object obj, String msg){ write2File("--w--" + obj, msg);}
    public static void i(Object obj, String msg){ write2File("--i--" + obj, msg);}
    public static void d(Object obj, String msg){ write2File("--d--" + obj, msg);}
    public static void v(Object obj, String msg){ write2File("--v--" + obj, msg);}
    public static void sysout(Object obj, String msg){ write2File("--s--" + obj, msg);}


    private static void write2File(final Object tag, final String msg) {
        sLogExecutor.execute(() -> {
//            String logMsg = String.format(Locale.CHINA, "%s pid=%d %s: %s\n", mLogDateFormat.format(new Date()), android.os.Process.myPid(), tag, msg);
            String logMsg = String.format(Locale.CHINA, "%s|%s: %s\n", mLogDateFormat.format(new Date()), (tag instanceof String ? (String)tag : tag.getClass().getName()), msg);
            sMsgQueue.add(logMsg);
            if (sMsgQueue.size() >= CACHE_QUEUE_SIZE) {
                flush2File();
            }
        });
    }

    public static void write2FileDirectly(final String tag, final String msg) {
        String logMsg = String.format(Locale.CHINA, "%s|%s: %s\n", mLogDateFormat.format(new Date()), tag, msg);
        sMsgQueue.add(logMsg);
        flush2File();
    }

    public static void flush2File() {
        if(mFile == null) init(mDir);
        if(mFile == null) {
            Log.e("Log2File", "error, no specific file created.");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String message : sMsgQueue) {
            stringBuilder.append(message);
        }
        sMsgQueue.clear();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(mFile, true);
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
//            Log.v("Log2File", "Content:\n" + stringBuilder.toString());
            Log.v("Log2File", "to file:" + mFile.getAbsolutePath());
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {fileWriter.close();} catch (Exception e) {e.printStackTrace();}
            }
            init(mDir);
        }
    }

}
