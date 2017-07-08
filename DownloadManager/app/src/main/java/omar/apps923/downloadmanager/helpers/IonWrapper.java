package omar.apps923.downloadmanager.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.Charsets;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.io.File;
import java.util.Date;
import java.util.Map;

import omar.apps923.downloadmanager.R;
import omar.apps923.downloadmanager.cache.controller.DataController;
import omar.apps923.downloadmanager.cache.listener.DBResultListener;


public class IonWrapper {


    public Context context;
    public WebServiceInterFace webServiceInterFace;

    Builders.Any.B ionBuilder;
    Future<String> response;

    ProgressDialog progressDialog;

    public IonWrapper(Context context, WebServiceInterFace webServiceInterFace) {
        this.context = context;
         this.webServiceInterFace = webServiceInterFace;

    }



    public IonWrapper(Context context) {
        this.context = context;
        this.webServiceInterFace = (WebServiceInterFace)context;
    }

    public void getResponse(String methodType, String url,
                             Map<String, String> params  ,boolean withProgress,
                            boolean isCaching,boolean multiPart,String multiPartKey , File multiPartFile) {


        KeyBoardUtil.hideSoftKeyboard((Activity)this.context);

        for (Map.Entry<String, String> entry : params.entrySet())
        {
            Log.e("params web service","   "+entry.getKey() + "/" + entry.getValue());
        }


        boolean internetFound=checkInternet();

        if(isCaching) {
            loadCacheData(url, params,  withProgress,internetFound);
        }

        if (internetFound)
        {

            loadFromUrl(methodType, url,
                    params,withProgress,isCaching , multiPart , multiPartKey , multiPartFile);
        }
        else
        {

            if(withProgress)
            {
                Toast.makeText(context, context.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
                dismissProgressDialog();
            }
        }
    }

    private void showProgressDialog() {



        try
        {
            if(progressDialog!=null)
            {
                if(!progressDialog.isShowing())
                {
                    progressDialog = ProgressDialog.show(context,"",context.getString(R.string.loading), true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }

            }
            else
            {
                progressDialog = ProgressDialog.show(context,"",context.getString(R.string.loading), true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        catch (Exception e)
        {

        }

    }
    private void dismissProgressDialog()

    {
        try
        {
           if(progressDialog!=null)
           {
               if(progressDialog.isShowing())
               {
                   progressDialog.dismiss();
               }

           }
        }
        catch (Exception e)
        {

        }

    }

    public void loadFromUrl(String methodType, final String url,final Map<String, String> params
                           , final boolean withProgress
                            , final boolean isCaching,boolean multiPart
            ,String multiPartKey , File multiPartFile
    ) {
        if(withProgress)
        {
            showProgressDialog();
        }
        ionBuilder = Ion.with(context)
                .load(methodType, url);


        if(!multiPart)
        {
            for (Map.Entry<String, String> entry : params.entrySet()) {

                ionBuilder.setBodyParameter(entry.getKey(), entry.getValue());

            }
        }
        else
        {
            for (Map.Entry<String, String> entry : params.entrySet()) {

                ionBuilder.setMultipartParameter(entry.getKey(), entry.getValue());

            }

            ionBuilder.setMultipartFile(multiPartKey,multiPartFile);

        }


        response = ionBuilder.asString(Charsets.UTF_8).setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (e == null) {
                            if(withProgress) {

                                dismissProgressDialog();
                            }
                            if(isCaching) {
                                // cache here

                                DataController dataController = new DataController(context);
                                dataController.saveData(url, params.toString(), result, new Date().getTime());
                            }

           Log.e("webservice resut succes","    "+result);

                            try
                            {
                                webServiceInterFace.parseString(result);

                            }
                            catch (Exception ex)
                            {
                                Log.e("Exception parseString",ex.toString());

                            }
                        } else {
                            if(withProgress) {

                                dismissProgressDialog();
                            }
                             webServiceInterFace.failResponse();
                        }
                    }

                });
    }


     // get from cache

    private void loadCacheData(String url,
                               Map<String, String> params,final boolean withProgress
                               , final boolean internetFound ) {
        if(withProgress) {
            showProgressDialog();

        }

        DataController dataController = new DataController(context);
         dataController.getData(url, params.toString(), new DBResultListener() {
            @Override
            public void onDBResult(String result) {
                if(withProgress && !internetFound) {

                    dismissProgressDialog();
                }

                 if(result!=null) {
                 Log.e("webservice result cache","    "+result);

                     try
                     {
                         webServiceInterFace.parseString(result);

                     }
                     catch (Exception ex)
                     {
                         Log.e("ExceptionCacheParseStr",ex.toString());

                     }

                 }
            }
        });

    }



    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {

            return false;

        }
    }


    public interface WebServiceInterFace {

        public void parseString(String response);

        public void failResponse();

    }
}