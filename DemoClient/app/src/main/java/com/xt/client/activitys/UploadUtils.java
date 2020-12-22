package com.xt.client.activitys;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

public class UploadUtils {
    private static final String UPLOAD_WORK_TAG = "upload_work_tag";
    public static final String UPLOAD_RELEATIVE_DIR = "/uploading"; // relative path

    public static int uploadFileSync(File file) {
        // implementation of uploading, no need write code here
        // synchronous upload file, return 0(success)/1(failed)
        return 0;
    }

    public static void startPeriodicUpload(AppCompatActivity context, Long periodicTime) {
        // write code here
        //定期任务，
        Timer timer = new Timer();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        updateFiles(context);
                    }
                });
            }
        }, 1000 * 1000);
    }

    public static void updateFiles(Activity context) {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + UPLOAD_RELEATIVE_DIR);
        List<File> list = new ArrayList<>();
        searchFile(list, folder);

        Handler handler = new Handler(context.getMainLooper());
        for (int i = 0; i < list.size(); i++) {
            int i1 = uploadFileSync(list.get(i));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TestActivity activity = (TestActivity) context;
                    String result = i1 + "/" + list.size();
//                    activity.show(result);
                }
            });
        }
    }

    public static void searchFile(List<File> list, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (File f : files) {
                searchFile(list, f);
            }
        } else {
            list.add(file);
        }
    }

}