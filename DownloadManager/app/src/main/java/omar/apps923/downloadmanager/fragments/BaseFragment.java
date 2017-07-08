package omar.apps923.downloadmanager.fragments;

import android.support.v4.app.Fragment;

import org.json.JSONException;

import omar.apps923.downloadmanager.GUI.AlertDialogInterface;
import omar.apps923.downloadmanager.GUI.CustomAlertDialog;
import omar.apps923.downloadmanager.helpers.IonWrapper;
import omar.apps923.downloadmanager.helpers.WebServices;
 

public class BaseFragment extends Fragment implements IonWrapper.WebServiceInterFace, AlertDialogInterface
{

    public WebServices webServices ;
    public CustomAlertDialog customAlertDialog ;

    public void initVars()
    {

        webServices = new WebServices( new IonWrapper(getActivity(), this));
        customAlertDialog=new CustomAlertDialog(this,getActivity());

    }
    @Override
    public void parseString(String response) {

    }

    @Override
    public void failResponse() {

    }


    @Override
    public void onPositiveButtonClicked() {

    }




}
