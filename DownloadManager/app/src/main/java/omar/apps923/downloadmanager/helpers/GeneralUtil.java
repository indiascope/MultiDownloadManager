package omar.apps923.downloadmanager.helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import omar.apps923.downloadmanager.R;


public class GeneralUtil {


    public Context context ;


    public GeneralUtil(Context context)
    {
        this.context=context;


    }

    public  void replaceFragment (Fragment fragment ){
        String fragmentTag =  fragment.getClass().getName();

        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

       // ft.replace(R.id.mainframelayout, fragment, fragmentTag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

    }


}
