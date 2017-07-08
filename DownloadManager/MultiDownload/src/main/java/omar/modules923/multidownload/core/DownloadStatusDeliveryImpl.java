package omar.modules923.multidownload.core;

import android.os.Handler;

import omar.modules923.multidownload.CallBack;
import omar.modules923.multidownload.DownloadException;
import omar.modules923.multidownload.architecture.DownloadStatus;
import omar.modules923.multidownload.architecture.DownloadStatusDelivery;

import java.util.concurrent.Executor;
 
public class DownloadStatusDeliveryImpl implements DownloadStatusDelivery {
    private Executor mDownloadStatusPoster;

    public DownloadStatusDeliveryImpl(final Handler handler) {
        mDownloadStatusPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void post(DownloadStatus status) {
        mDownloadStatusPoster.execute(new DownloadStatusDeliveryRunnable(status));
    }

    private static class DownloadStatusDeliveryRunnable implements Runnable {
        private final DownloadStatus mDownloadStatus;
        private final CallBack mCallBack;

        public DownloadStatusDeliveryRunnable(DownloadStatus downloadStatus) {
            this.mDownloadStatus = downloadStatus;
            this.mCallBack = mDownloadStatus.getCallBack();
        }

        @Override
        public void run() {
            switch (mDownloadStatus.getStatus()) {
                case DownloadStatus.STATUS_CONNECTING:
                    mCallBack.onConnecting(mDownloadStatus.getTag());
                    break;
                case DownloadStatus.STATUS_CONNECTED:
                    mCallBack.onConnected(mDownloadStatus.getLength(), mDownloadStatus.isAcceptRanges()
                            ,mDownloadStatus.getTag());
                    break;
                case DownloadStatus.STATUS_PROGRESS:
                    mCallBack.onProgress(mDownloadStatus.getFinished(), mDownloadStatus.getLength(),
                            mDownloadStatus.getPercent(),mDownloadStatus.getTag());
                    break;
                case DownloadStatus.STATUS_COMPLETED:
                    mCallBack.onCompleted(mDownloadStatus.getTag());
                    break;
                case DownloadStatus.STATUS_PAUSED:
                    mCallBack.onDownloadPaused(mDownloadStatus.getTag());
                    break;
                case DownloadStatus.STATUS_CANCELED:
                    mCallBack.onDownloadCanceled(mDownloadStatus.getTag());
                    break;
                case DownloadStatus.STATUS_FAILED:
                    mCallBack.onFailed((DownloadException) mDownloadStatus.getException()
                    , mDownloadStatus.getTag());
                    break;
            }
        }
    }
}
