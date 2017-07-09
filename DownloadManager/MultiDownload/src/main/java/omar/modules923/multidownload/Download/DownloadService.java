package omar.modules923.multidownload.Download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.greenrobot.eventbus.EventBus;

 import omar.modules923.multidownload.DownloadManager;
import omar.modules923.multidownload.DownloadRequest;
import omar.modules923.multidownload.R;
import omar.modules923.multidownload.models.Download;

import java.io.File;


public class DownloadService extends Service {





    public static final String ACTION_DOWNLOAD = "omar.modules923.multidownload.demo.action_download";

    public static final String ACTION_PAUSE = "omar.modules923.multidownload.demo.action_pause";

    public static final String ACTION_RESUME = "omar.modules923.multidownload.demo.action_resume";

    public static final String ACTION_NOTIFICATION_PAUSE_RESUME = "omar.modules923.multidownload.action_notification_pause_resume";

    public static final String ACTION_NOTIFICATION_CANCEL = "omar.modules923.multidownload.action_notification_cancel";




    public static final String ACTION_CANCEL = "omar.modules923.multidownload.demo.action_cancel";

    public static final String ACTION_PAUSE_ALL = "omar.modules923.multidownload.demo.action_pause_all";

    public static final String ACTION_RESUME_ALL = "omar.modules923.multidownload.demo.action_resume_all";

    public static final String ACTION_CANCEL_ALL = "omar.modules923.multidownload.demo.action_cancel_all";

    public static final String ACTION_NOTIFICATION_OPEN_APP = "omar.modules923.multidownload.action_notification_open_app";


    public static final String EXTRA_TAG = "extra_tag";

    public static final String EXTRA_DOWNLOAD_INFO = "EXTRA_DOWNLOAD_INFO";



    private DownloadManager mDownloadManager;


    public static void intentDownload(Context context , String tag, Download info     )
    {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
         intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_DOWNLOAD_INFO, info);
          context.startService(intent);
    }

    public static void intentPause(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_PAUSE);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentResume(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_RESUME);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }


    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_PAUSE_ALL);
        context.startService(intent);
    }

    public static void intentResumeAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_RESUME_ALL);
        context.startService(intent);
    }

    public static void intentCancelAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_CANCEL_ALL);
        context.startService(intent);
    }

    public static void intentNotificationCancel(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_NOTIFICATION_CANCEL);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }


    public static void intentCancel(Context context, String tag) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_CANCEL);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }



    public static void destory(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
             Download downloadInfo = (Download) intent.getParcelableExtra(EXTRA_DOWNLOAD_INFO);
             String tag = intent.getStringExtra(EXTRA_TAG);

            if(action.contains(ACTION_NOTIFICATION_PAUSE_RESUME))
            {
                pause_resume(tag);

            }
            else
            if(action.contains(ACTION_NOTIFICATION_CANCEL))
            {
                cancel(tag);

            }

            else
            {
                switch (action) {
                    case ACTION_DOWNLOAD:
                        download( downloadInfo, tag );
                        break;
                    case ACTION_PAUSE:
                        pause(tag);
                        break;
                    case ACTION_NOTIFICATION_PAUSE_RESUME:
                        pause_resume(tag);
                        break;
                    case ACTION_NOTIFICATION_CANCEL:
                        cancel(tag);
                        break;
                    case ACTION_CANCEL:
                        cancel(tag);
                        break;
                    case ACTION_RESUME:
                        resume(tag);
                        break;

                    case ACTION_PAUSE_ALL:
                        pauseAll();
                        break;
                    case ACTION_CANCEL_ALL:
                        cancelAll();
                        break;
                    case ACTION_RESUME_ALL:
                        resumeAll();
                        break;
                    case ACTION_NOTIFICATION_OPEN_APP:
                        openApp(tag);
                        break;


                }
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(final Download downloadInfo, String tag ) {
        final DownloadRequest request = new DownloadRequest.Builder()
                .setName(downloadInfo.getFileName() )
                .setUri(downloadInfo.getUrl())
                .setFolder(new File(downloadInfo.getDirectoryPath()))
                .build();

        downloadInfo.save();

        mDownloadManager.download(request, tag, new DownloadCallBack(
              getApplicationContext()));

    }



    private void pause_resume(String tag) {

         mDownloadManager.pause_resume(tag);
     }
    private void pause(String tag) {
        mDownloadManager.pause(tag);
    }

    private void cancel(String tag) {
        mDownloadManager.cancel(tag);
    }



    private void resume(String tag) {
        mDownloadManager.resume(tag);
    }



    private void resumeAll() {
        mDownloadManager.resumeAll();
    }
    private void openApp(String url) {

    Download downloadItem = getDownloadItem(url);

     // EventBus.getDefault().post(downloadItem.getId().intValue());

    }

    public Download getDownloadItem(String tag)
    {

        Download downloadItem =  new Select()
                .from(Download.class)
                .where("url = ?", tag)
                .executeSingle();

        return downloadItem ;
    }

    private void pauseAll() {
        mDownloadManager.pauseAll();
    }

    private void cancelAll() {
        mDownloadManager.cancelAll();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);

        mDownloadManager = DownloadManager.getInstance();

        mDownloadManager.initVars(getApplicationContext());


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadManager.pauseAll();
    }



}
