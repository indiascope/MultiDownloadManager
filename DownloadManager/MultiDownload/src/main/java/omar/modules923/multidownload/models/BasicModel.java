package omar.modules923.multidownload.models;

import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class BasicModel extends BaseObservable implements Parcelable
{

    public BasicModel basicModel  ;
    public ArrayList<BasicModel> basicModels = new ArrayList<>();


    public BasicModel(   )

    {


    }

    public BasicModel(Context context , JSONArray jsonArray)

    {
        basicModels = new ArrayList<>();
    }


    public BasicModel(Context context , JSONObject jsonObject)

    {
        basicModel   = new BasicModel();
    }



    protected BasicModel(Parcel in) {
    }

    public static final Creator<BasicModel> CREATOR = new Creator<BasicModel>() {
        @Override
        public BasicModel createFromParcel(Parcel in) {
            return new BasicModel(in);
        }

        @Override
        public BasicModel[] newArray(int size) {
            return new BasicModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
