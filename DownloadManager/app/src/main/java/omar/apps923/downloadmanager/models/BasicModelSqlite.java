package omar.apps923.downloadmanager.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

 
public class BasicModelSqlite extends Model implements Parcelable
{

    public BasicModelSqlite basicModel  ;

    public ArrayList<BasicModelSqlite> basicModels = new ArrayList<>();


    public BasicModelSqlite(   )

    {

        super();
    }

    public BasicModelSqlite(Context context , JSONArray jsonArray)

    {
        basicModels = new ArrayList<>();
    }


    public BasicModelSqlite(Context context , JSONObject jsonObject)

    {
        basicModel   = new BasicModelSqlite();
    }



    protected BasicModelSqlite(Parcel in) {
    }

    public static final Creator<BasicModelSqlite> CREATOR = new Creator<BasicModelSqlite>() {
        @Override
        public BasicModelSqlite createFromParcel(Parcel in) {
            return new BasicModelSqlite(in);
        }

        @Override
        public BasicModelSqlite[] newArray(int size) {
            return new BasicModelSqlite[size];
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
