package omar.modules923.multidownload;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.activeandroid.query.Select;
import omar.modules923.multidownload.Download.DownloadCallBack;
import omar.modules923.multidownload.architecture.DownloadResponse;
import omar.modules923.multidownload.architecture.DownloadStatusDelivery;
import omar.modules923.multidownload.architecture.Downloader;
import omar.modules923.multidownload.core.DownloadResponseImpl;
import omar.modules923.multidownload.core.DownloadStatusDeliveryImpl;
import omar.modules923.multidownload.core.DownloaderImpl;
import omar.modules923.multidownload.db.DataBaseManager;
import omar.modules923.multidownload.db.ThreadInfo;
import omar.modules923.multidownload.models.Download;
import omar.modules923.multidownload.util.L;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


 
public class DownloadManager implements Downloader.OnDownloaderDestroyedListener {

    public static final String TAG = DownloadManager.class.getName();

 
 
    private static DownloadManager sDownloadManager;

    private DataBaseManager mDBManager;

    private Map<String, Downloader> mDownloaderMap;

    private DownloadConfiguration mConfig;

    private ExecutorService mExecutorService;

    private DownloadStatusDelivery mDelivery;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    private Context mContext ;

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                if (sDownloadManager == null) {
                    sDownloadManager = new DownloadManager();
                }
            }
        }
        return sDownloadManager;
    }


    /**
     * private construction
     */
    private DownloadManager() {
        mDownloaderMap = new LinkedHashMap<String, Downloader>();


    }

    public void init(Context context) {
        init(context, new DownloadConfiguration());
    }

    public void init(Context context, @NonNull DownloadConfiguration config) {
        if (config.getThreadNum() > config.getMaxThreadNum()) {
            throw new IllegalArgumentException("thread num must < max thread num");
        }

        mConfig = config;
        mDBManager = DataBaseManager.getInstance(context);
        mExecutorService = Executors.newFixedThreadPool(mConfig.getMaxThreadNum());
        mDelivery = new DownloadStatusDeliveryImpl(mHandler);

    }

    @Override
    public void onDestroyed(final String key, Downloader downloader)
    {


    }

    public void download(DownloadRequest request, String tag, CallBack callBack) {
        final String key = tag;
        if (check(key)) {
            DownloadResponse response = new DownloadResponseImpl(tag , mDelivery, callBack);
            Downloader downloader = new DownloaderImpl(request, response, mExecutorService, mDBManager, key, mConfig, this);
            mDownloaderMap.put(key, downloader);
            downloader.start();
        }
    }

    public void download(String tag ) {
        Download downloadInfo =  new Select()
                .from(Download.class)
                .where("url = ?", tag)
                .executeSingle();

        final DownloadRequest request = new DownloadRequest.Builder()
                .setName(downloadInfo.getFileName() )
                .setUri(downloadInfo.getUrl())
                .setFolder(new File(downloadInfo.getDirectoryPath()))
                .build();

        download(request, tag, new DownloadCallBack(
                mContext.getApplicationContext()));
    }

    public void pause_resume(String tag) {

        Log.e(  "pause_resume: ",tag );
        String key = tag;
         if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                 }
                else
                {
                     downloader.start();
                }
            }
            else
            {
                download(tag);

            }
        }
        else
        {

            download(tag);


        }
    }

    public void pause(String tag) {
        String key = tag;
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
         }
    }

    public void resume(String tag) {
        String key = tag;
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (!downloader.isRunning())
                {
                     downloader.start();
                }
            }
            else
            {
                download(tag);

            }
         }
        else
        {

            download(tag);

        }
    }

    public void cancel(String tag) {
        String key = tag;
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                downloader.cancel();
            }
            mDownloaderMap.remove(key);
        }

        else
        {

           // delete(tag);
        }
    }



    public void pauseAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Downloader downloader : mDownloaderMap.values()) {
                    if (downloader != null) {
                        if (downloader.isRunning()) {
                            downloader.pause();
                        }
                    }
                }
            }
        });
    }



    public void resumeAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                for (Map.Entry<String, Downloader> entry  : mDownloaderMap.entrySet()) {
                    if (entry.getValue() != null) {
                         if (!entry.getValue().isRunning()) {
                             entry.getValue().start();
                        }
                    }
                    else
                    {
                         download((entry.getKey()));

                    }
                }
            }
        });
    }

    public void cancelAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Downloader downloader : mDownloaderMap.values()) {
                    if (downloader != null) {
                        if (downloader.isRunning()) {
                            downloader.cancel();
                        }
                    }
                }
                mDownloaderMap.clear();
            }
        });
    }

    public void delete(String tag) {
        String key = tag;
        mDBManager.delete(key);
    }

    public boolean isRunning(String tag) {
        String key = tag;
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                return downloader.isRunning();
            }
        }
        return false;
    }

    public DownloadInfo getDownloadInfo(String tag) {
        String key = tag;
        List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(key);
        DownloadInfo downloadInfo = null;
        if (!threadInfos.isEmpty()) {
            int finished = 0;
            int progress = 0;
            int total = 0;
            for (ThreadInfo info : threadInfos) {
                finished += info.getFinished();
                total += (info.getEnd() - info.getStart());
            }
            progress = (int) ((long) finished * 100 / total);
            downloadInfo = new DownloadInfo();
            downloadInfo.setFinished(finished);
            downloadInfo.setLength(total);
            downloadInfo.setProgress(progress);
        }
        return downloadInfo;
    }

    private boolean check(String key) {
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            mDownloaderMap.remove(key);
            if (downloader != null) {
                if ((downloader.isRunning() )) {
                    downloader.cancel();
                     return true;
                }
//                else {
//                    throw new IllegalStateException("Downloader instance with same tag has not been destroyed!");
//                }
            }
        }
        return true;
    }


    public void initVars(Context context) {

        mContext=context ;


    }
}
