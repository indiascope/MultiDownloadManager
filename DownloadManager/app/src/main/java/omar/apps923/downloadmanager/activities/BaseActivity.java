package omar.apps923.downloadmanager.activities;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import omar.apps923.downloadmanager.GUI.AlertDialogInterface;
import omar.apps923.downloadmanager.GUI.CustomAlertDialog;
import omar.apps923.downloadmanager.helpers.IonWrapper;
import omar.apps923.downloadmanager.helpers.WebServices;


public class BaseActivity extends AppCompatActivity implements IonWrapper.WebServiceInterFace, AlertDialogInterface
{

    public WebServices webServices ;
    public CustomAlertDialog customAlertDialog ;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Log.e("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public void initVars()
    {

         webServices = new WebServices( new IonWrapper(this, this));
        customAlertDialog=new CustomAlertDialog(this,this);

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
