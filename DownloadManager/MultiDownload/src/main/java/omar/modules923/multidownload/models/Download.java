package omar.modules923.multidownload.models;

import android.os.Parcel;
import android.webkit.URLUtil;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Downloads")
public class Download extends BasicModelSqlite
{
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_CANCELLED = 7;


    @Column(name = "url", unique = true)
    public String url;
    @Column(name = "fileName")
    public String fileName;
      @Column(name = "progress")
    public float progress;
    @Column(name = "status")
    private int status;
    @Column(name = "source")
    private String source;
    @Column(name = "downloadPerSize")
    private String downloadPerSize;
    @Column(name = "directoryPath")
    private String directoryPath;



    public Download() {

        super();

    }

    public Download( String downlodURL , String directoryPath)
    {
        fileName = URLUtil.guessFileName(downlodURL, null, null);

        set( downlodURL, fileName,   0, directoryPath);
    }



    protected Download(Parcel in) {
         url = in.readString();
        fileName = in.readString();
         progress = in.readFloat();
        status = in.readInt();
        source = in.readString();
        downloadPerSize = in.readString();
        directoryPath = in.readString();

     }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };

    public void set( String url, String fileName , float progress , String directoryPath)
    {
         this.url = url;
        this.fileName = fileName;
         this.progress = progress;
        status = 0;
        source = "";
        this.directoryPath = directoryPath;

    }




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDownloadPerSize() {
        return downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getStatusText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Not Downloaded";
            case STATUS_CONNECTING:
                return "Connecting";
            case STATUS_CONNECT_ERROR:
                return "Connect Error";
            case STATUS_DOWNLOADING:
                return "Downloading";
            case STATUS_PAUSED:
                return "Paused";
            case STATUS_DOWNLOAD_ERROR:
                return "Download Error";
            case STATUS_COMPLETE:
                return "Completed";
            default:
                return "Not Downloaded";
        }
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
         parcel.writeString(url);
        parcel.writeString(fileName);
         parcel.writeFloat(progress);
        parcel.writeInt(status);
        parcel.writeString(source);
        parcel.writeString(downloadPerSize);
        parcel.writeString(directoryPath);

    }
}
