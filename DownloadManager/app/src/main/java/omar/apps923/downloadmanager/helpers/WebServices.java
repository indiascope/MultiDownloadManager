package omar.apps923.downloadmanager.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class WebServices {



    private HashMap<String, String> map = new HashMap<>();

    Map<String, String> mHeaders = new HashMap<>(); ;

    public static String getHttpMethod="GET";
    public static String postHttpMethod="POST";

    public static String base_url_server="http://192.168.1.3:8081/RecyclerAdapters";

    public String base_url=base_url_server+"/rest_api/";


    public String mServiceName;
    public String extensionFile = ".php";
    private IonWrapper ionWrapper;
    private Context context;
    private Activity activity;
     public WebServices(Activity activity )
    {
        this.activity = activity;
    }

    public WebServices(IonWrapper ionWrapper)
    {
        this.ionWrapper = ionWrapper;
    }
    public WebServices(Activity activity , IonWrapper ionWrapper)
    {
        this.activity = activity;
        this.ionWrapper = ionWrapper;
    }
    public WebServices(Context context , IonWrapper ionWrapper)
    {
        this.context = context;
        this.ionWrapper = ionWrapper;
    }

//     public void getBooks()
//    {
//        map = new HashMap<>();
//        map.put("security90","any value");
//        mServiceName = "getAllBooks";
//        ionWrapper.getResponse(postHttpMethod,base_url +mServiceName+extensionFile  , map,true,false ,false,"",null);
//
//    }




}