package omar.modules923.multidownload.Download;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.activeandroid.util.Log;

import omar.modules923.multidownload.DownloadException;
import omar.modules923.multidownload.R;
import omar.modules923.multidownload.models.Download;


import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static omar.modules923.multidownload.Download.DownloadService.ACTION_NOTIFICATION_CANCEL;
import static omar.modules923.multidownload.Download.DownloadService.ACTION_NOTIFICATION_OPEN_APP;
import static omar.modules923.multidownload.Download.DownloadService.ACTION_NOTIFICATION_PAUSE_RESUME;
import static omar.modules923.multidownload.Download.DownloadService.EXTRA_TAG;

 
public class DownloadNotification {




    public NotificationCompat.Builder mBuilder;

    public NotificationManagerCompat mNotificationManager;
    public RemoteViews mNotificationView;
    public Notification mNotification;
    public Context mContext ;




    public DownloadNotification(

            Context context
    )

    {
          this.mContext = context;

        mNotificationManager = NotificationManagerCompat.from(
                mContext.getApplicationContext());
        mBuilder = new android.support.v7.app.NotificationCompat.Builder(
                mContext.getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);

        mNotificationView = new RemoteViews(
                mContext.getApplicationContext().getPackageName(),
                R.layout.notif_download);

        Log.e("dnotif Context.getApplicationContext()",mContext.getApplicationContext().getPackageName());

        mBuilder.setContent(mNotificationView);
        mNotification = mBuilder.build();


    }



    public void startNotification(Download downloadItem) {


        mNotificationView.setTextViewText(R.id.txtvName,downloadItem.getFileName());
        mNotificationView.setTextViewText(R.id.txtvProgress,"Start download " + ": "
                +downloadItem.progress + "%");

        mNotificationView.setImageViewResource(R.id.imgvPauseResume,
                R.drawable.pause_notif);


        Intent   ACTION_OPEN_APP_INTENT = new Intent(mContext.getApplicationContext()
                ,DownloadService.class);


        Intent   ACTION_PAUSE_RESUME_INTENT = new Intent(mContext.getApplicationContext()
        ,DownloadService.class);


        Intent   ACTION_CANCEL_INTENT = new Intent(mContext.getApplicationContext()
                ,DownloadService.class);

        PendingIntent openApp_pendingIntent , pause_resume_pendingIntent, cancel_pendingIntent ;


         ACTION_OPEN_APP_INTENT.setAction(ACTION_NOTIFICATION_OPEN_APP);
        ACTION_OPEN_APP_INTENT.putExtra(EXTRA_TAG, downloadItem.getUrl());

        ACTION_PAUSE_RESUME_INTENT.putExtra(EXTRA_TAG, downloadItem.getUrl());

        ACTION_CANCEL_INTENT.putExtra(EXTRA_TAG, downloadItem.getUrl());

        ACTION_PAUSE_RESUME_INTENT.setAction(
                ACTION_NOTIFICATION_PAUSE_RESUME+Long.toString(System.currentTimeMillis()));
        ACTION_CANCEL_INTENT.setAction(
                 ACTION_NOTIFICATION_CANCEL+Long.toString(System.currentTimeMillis()));


        openApp_pendingIntent = PendingIntent.getService(
                mContext.getApplicationContext(),  downloadItem.getId().intValue(),
                ACTION_OPEN_APP_INTENT,
                FLAG_UPDATE_CURRENT);


        pause_resume_pendingIntent = PendingIntent.getService(
                mContext.getApplicationContext(),  downloadItem.getId().intValue(),
                ACTION_PAUSE_RESUME_INTENT,
                FLAG_UPDATE_CURRENT);


        cancel_pendingIntent = PendingIntent.getService(
                mContext.getApplicationContext(), downloadItem.getId().intValue(),
                ACTION_CANCEL_INTENT,
                FLAG_UPDATE_CURRENT);


        mNotificationView.setOnClickPendingIntent(R.id.rlInfo,openApp_pendingIntent);

        mNotificationView.setOnClickPendingIntent(R.id.imgvPauseResume,
                pause_resume_pendingIntent);

        mNotificationView.setOnClickPendingIntent(R.id.imgvCancel,
                cancel_pendingIntent);

        mBuilder.setContent(mNotificationView);
        mBuilder.setOngoing(true);
        mNotification = mBuilder.build();

        updateNotification(downloadItem);

    }

    public void onProgress (  long finished, long total, int progress ,  Download downloadItem)
    {

        mNotificationView.setViewVisibility(R.id.imgvPauseResume,VISIBLE);
        mNotificationView.setViewVisibility(R.id.imgvCancel,VISIBLE);

        mNotificationView.setTextViewText(R.id.txtvName,downloadItem.getFileName());


        if(downloadItem.progress >= 100) {
            mNotificationView.setTextViewText(R.id.txtvProgress,
                    mContext.getApplicationContext().getString(R.string.downloadComplete));

            mNotificationView.setProgressBar(R.id.progressBar,100,100,false);

        }
        else
        {

            mNotificationView.setTextViewText(R.id.txtvProgress,
                    mContext.getApplicationContext().getString(R.string.downloading) + ": "
                            + downloadItem.progress + "%");

            mNotificationView.setProgressBar(R.id.progressBar,100,(int) progress,false);


            if(downloadItem.getStatus()==Download.STATUS_PAUSED)
                mNotificationView.setImageViewResource(R.id.imgvPauseResume,
                        R.drawable.resume_notif);
            else
                mNotificationView.setImageViewResource(R.id.imgvPauseResume,
                        R.drawable.pause_notif);
        }
        if(progress >= 100)
        {
            mBuilder.setOngoing(false);
        }
        else
        {
            mBuilder.setOngoing(true);
        }
        mNotification = mBuilder.build();

        updateNotification(downloadItem);


    }
    public void onConnecting (    Download downloadItem)
    {
        mNotificationView.setTextViewText(R.id.txtvProgress,
                mContext.getApplicationContext().getString(R.string.downloading) + ": "
                        + downloadItem.progress + "%");

        mBuilder.setOngoing(true);
        mNotification = mBuilder.build();


    }

    public void onCompleted (    Download downloadItem)
    {


        mNotificationView.setTextViewText(R.id.txtvProgress,
                mContext.getApplicationContext().getString(R.string.downloadComplete) + ": "
                        + downloadItem.progress + "%");
        mNotificationView.setTextViewText(R.id.txtvName,downloadItem.getFileName());
        mNotificationView.setViewVisibility(R.id.imgvPauseResume,GONE);
        mNotificationView.setProgressBar(R.id.progressBar,100,100,false);
        mBuilder.setOngoing(false);
        mNotification = mBuilder.build();


    }

    public void onDownloadPaused (    Download downloadItem)
    {


        mNotificationView.setViewVisibility(R.id.imgvPauseResume,VISIBLE);
        mNotificationView.setViewVisibility(R.id.imgvCancel,VISIBLE);

        mNotificationView.setTextViewText(R.id.txtvName,downloadItem.getFileName());

        mNotificationView.setImageViewResource(R.id.imgvPauseResume,
                R.drawable.resume_notif);

        mNotificationView.setTextViewText(R.id.txtvProgress,
                mContext.getApplicationContext().getString(R.string.downloadPaused)
                        + ": "
                        + downloadItem.progress + "%");

        mBuilder.setOngoing(false);
        mNotification = mBuilder.build();


    }
    public void onDownloadCanceled (  Download downloadItem , boolean removeFromDB)
    {

        if(removeFromDB)
        {
            downloadItem.delete();

        }

        mNotificationManager.cancel(
                downloadItem.getId().intValue() + 1000
        );

    }


    public void onFailed (DownloadException e , Download downloadItem)
    {
        mNotificationView.setTextViewText(R.id.txtvName,downloadItem.getFileName());
        mNotificationView.setTextViewText(R.id.txtvProgress,
                mContext.getApplicationContext().getString(R.string.downloadError));

        mNotificationView.setViewVisibility(R.id.imgvCancel,INVISIBLE);

        mNotificationView.setImageViewResource(R.id.imgvPauseResume,
                R.drawable.resume_notif);

    }
    public void updateNotification(Download downloadItem)
    {

        mNotificationManager.notify(
                downloadItem.getId().intValue() + 1000
                ,
                mBuilder.build());

    }



}
