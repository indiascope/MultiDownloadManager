package omar.modules923.multidownload.fragments;

import android.support.v4.app.Fragment;

import omar.modules923.multidownload.GUI.AlertDialogInterface;
import omar.modules923.multidownload.GUI.CustomAlertDialog;
import omar.modules923.multidownload.models.Download;

 
public class BaseFragment extends Fragment implements AlertDialogInterface
{

     public CustomAlertDialog customAlertDialog ;

    public void initVars()
    {

         customAlertDialog=new CustomAlertDialog(this,getActivity());

    }

 

    public void loadData() {

    }

    public void updateData(Download download) {

    }

    @Override
    public void onPositiveButtonClicked() {

    }
}
