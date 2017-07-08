package omar.modules923.multidownload.Download;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import omar.modules923.multidownload.CallBack;
import omar.modules923.multidownload.DownloadException;
import omar.modules923.multidownload.R;
import omar.modules923.multidownload.helpers.Utils;
import omar.modules923.multidownload.models.Download;
import omar.modules923.multidownload.util.L;

import org.greenrobot.eventbus.EventBus;

import static omar.modules923.multidownload.models.Download.STATUS_COMPLETE;


 
public     class DownloadCallBack implements CallBack

{



    private DownloadNotification mDownloadNotification ;
    private Context mContext ;
    private long mLastTime;


    public DownloadCallBack(

                             Context context
    )

    {

        this.mContext = context;
        mDownloadNotification=new DownloadNotification(this.mContext);


    }

    public Download getDownloadItem(String tag)
    {

        Download downloadItem =  new Select()
                .from(Download.class)
                .where("url = ?", tag)
                .executeSingle();

            return downloadItem ;
    }

    @Override
    public void onStarted(String tag) {



    }


    @Override
    public void onConnecting(String tag) {

        try {
        Download downloadItem = getDownloadItem(tag);

        downloadItem.setStatus(Download.STATUS_CONNECTING);

        downloadItem.save();

        mDownloadNotification.onConnecting(downloadItem);


        mDownloadNotification.startNotification(downloadItem);


        sendBroadCast(downloadItem);

    }
        catch (Exception e)
    {
        Log.e("Exceptionon onConnecting ",e.toString());
    }
    }

    @Override
    public void onConnected(long total, boolean isRangeSupport , String tag) {

    }

    @Override
    public void onProgress(long finished, long total, int progress , String tag) {

        try {
            if (mLastTime == 0) {
                mLastTime = System.currentTimeMillis();
            }

            long currentTime = System.currentTimeMillis();


            if (currentTime - mLastTime > 1800) {

                Download downloadItem = getDownloadItem(tag);

                downloadItem.setStatus(Download.STATUS_DOWNLOADING);
                downloadItem.setProgress(progress);
                downloadItem.setDownloadPerSize(Utils.getDownloadPerSize(finished, total));
                downloadItem.save();


                mDownloadNotification.onProgress(  finished,   total,   progress
                        , downloadItem);

                sendBroadCast(downloadItem);

                mLastTime = currentTime;
            }
        }
        catch (Exception e)
        {
            Log.e("Exceptionon onProgress ",e.toString());
        }


    }

    @Override
    public void onCompleted(String tag) {

        try {
            Download downloadItem = getDownloadItem(tag);

            downloadItem.setStatus(STATUS_COMPLETE);
            downloadItem.setProgress(100);

            downloadItem.save();

            Toast.makeText(mContext,mContext.getString(R.string.downloadOf )
                    +" "+downloadItem.getFileName()+" "+
                    mContext.getString(R.string.completed),Toast.LENGTH_LONG).show();


            mDownloadNotification.onCompleted(downloadItem);


            mDownloadNotification.updateNotification(downloadItem);


            sendBroadCast(downloadItem);
        }
        catch (Exception e)
        {
            Log.e("onCompleted exception",e.toString());

        }

    }

    @Override
    public void onDownloadPaused(String tag) {

        try {


            Download downloadItem = getDownloadItem(tag);

            downloadItem.setStatus(Download.STATUS_PAUSED);
            downloadItem.save();

            mDownloadNotification.onDownloadPaused(downloadItem);

            mDownloadNotification.updateNotification(downloadItem);

            sendBroadCast(downloadItem);
        }
         catch (Exception e)
        {
            Log.e("onDownloadPaused exception",e.toString());

        }
    }

    @Override
    public void onDownloadCanceled(String tag) {


        try {
        Download downloadItem = getDownloadItem(tag);

        if (downloadItem != null) {
            // cancel is from notificaion bar
            if (downloadItem.getStatus() == STATUS_COMPLETE) {
                mDownloadNotification.onDownloadCanceled(downloadItem, false);

            } else {
                downloadItem.setStatus(Download.STATUS_CANCELLED);
                downloadItem.setProgress(0);
                downloadItem.setDownloadPerSize("");
                downloadItem.save();

                mDownloadNotification.onDownloadCanceled(downloadItem, true);

                sendBroadCast(downloadItem);
            }
        }
    }
      catch (Exception e)
        {
            Log.e("onDownloadCanceled exception",e.toString());

        }

    }

    @Override
    public void onFailed(DownloadException e , String tag) {

        try {
         e.printStackTrace();

        Download downloadItem = getDownloadItem(tag);

        downloadItem.setStatus(Download.STATUS_DOWNLOAD_ERROR);
        downloadItem.setProgress(0);
        downloadItem.setDownloadPerSize("");
        downloadItem.save();


        mDownloadNotification.onFailed(e,downloadItem);

        mDownloadNotification.updateNotification(downloadItem);

        sendBroadCast(downloadItem);

    }
      catch (Exception e2)
    {
        Log.e("onFailed exception",e2.toString());

    }
    }


    private void sendBroadCast(Download downloadInfo) {


        EventBus.getDefault().post(downloadInfo);

    }
}
