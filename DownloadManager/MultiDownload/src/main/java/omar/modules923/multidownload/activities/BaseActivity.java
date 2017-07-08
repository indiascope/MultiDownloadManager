package omar.modules923.multidownload.activities;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import omar.modules923.multidownload.GUI.AlertDialogInterface;
import omar.modules923.multidownload.GUI.CustomAlertDialog;


public class BaseActivity extends AppCompatActivity implements AlertDialogInterface
{

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

         customAlertDialog=new CustomAlertDialog(this,this);

    }


    @Override
    public void onPositiveButtonClicked() {

    }
}
