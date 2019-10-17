//package com.xt.client.fragment;
//
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//
//import com.liulishuo.filedownloader.BaseDownloadTask;
//import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
//import com.liulishuo.filedownloader.FileDownloader;
//import com.xt.client.R;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public class DownLoadFragment extends BaseFragment {
//
//    final String downloadUrl = "http://static1.51gonggui.com/mft100/decoder/file/Decode_349020.zip";
//    final String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1";
//
//    BaseDownloadTask baseDownloadTask;
//
//    @Override
//    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getViewHolder().getButton1().setText("开始下载");
//        getViewHolder().getButton2().setText("停止下载");
//        getViewHolder().getLine2().setVisibility(View.VISIBLE);
//        getViewHolder().getButton3().setText("继续下载");
//        getViewHolder().getButton4().setText("清空");
//
//
//        baseDownloadTask = FileDownloader.getImpl().create(downloadUrl);
//        baseDownloadTask.setPath(downloadPath, true);
//        baseDownloadTask.setCallbackProgressTimes(500);
//        baseDownloadTask.setMinIntervalUpdateSpeed(400);
//        baseDownloadTask.setTag(1, "lxldown");
//        baseDownloadTask.setListener(new FileDownloadLargeFileListener() {
//            @Override
//            protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
//                Log.i("lxltest", "pending:" + soFarBytes + ",totalBytes:" + totalBytes + "," + soFarBytes * 100 / totalBytes + "%");
//            }
//
//            @Override
//            protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
//                Log.i("lxltest", "progress:" + soFarBytes + ",totalBytes:" + totalBytes + "," + soFarBytes * 100 / totalBytes + "%");
//            }
//
//            @Override
//            protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
//                Log.i("lxltest", "paused:" + soFarBytes + ",totalBytes:" + totalBytes + "," + soFarBytes * 100 / totalBytes + "%");
//            }
//
//            @Override
//            protected void completed(BaseDownloadTask task) {
//                Log.i("lxltest", "completed");
//            }
//
//            @Override
//            protected void error(BaseDownloadTask task, Throwable e) {
//                Log.i("lxltest", "error");
//            }
//
//            @Override
//            protected void warn(BaseDownloadTask task) {
//                Log.i("lxltest", "warn");
//            }
//        });
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.button1) {
//            int start = baseDownloadTask.start();
//            Log.i("lxltest", "start:" + start);
//        } else if (id == R.id.button2) {
//            boolean pause = baseDownloadTask.pause();
//            Log.i("lxltest", "pause:" + pause);
//        } else if (id == R.id.button3) {
//            boolean reuse = baseDownloadTask.reuse();
//            Log.i("lxltest", "reuse:" + reuse);
//        } else if (id == R.id.button4) {
//            boolean cancel = baseDownloadTask.cancel();
//            Log.i("lxltest", "cancel:" + cancel);
//        }
//
//    }
//}
