package omar.modules923.multidownload.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import omar.modules923.multidownload.R;

 
public class CustomAlertDialog {
    Activity activity;
    String alerttitle, alertbutton;
    String alertnegbutton = "";
      private AlertDialogInterface alertDialogInterface;

    public CustomAlertDialog(Activity activity) {
        this.activity = activity;
         alerttitle = activity.getResources().getString(R.string.alert);
        alertbutton = activity.getResources().getString(R.string.ok);
     }

    public CustomAlertDialog(AlertDialogInterface alertDialogInterface, Activity activity) {
        this.alertDialogInterface = alertDialogInterface;
        alerttitle = activity.getResources().getString(R.string.alert);
        alertbutton = activity.getResources().getString(R.string.ok);
        this.activity = activity;
    }


    public void alertDialogWithIntent(String alertmessage, final Class cls) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(alertmessage);

        alertDialog.setTitle(alerttitle);
        alertDialog.setButton(alertbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent myIntent = new Intent(activity, cls);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(myIntent);
                activity.finish();
            }
        });

        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
    }

    public void alertDialogWithIntentFinishAffinity(String alertmessage, final Class cls) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(alertmessage);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(alerttitle);
        alertDialog.setButton(alertbutton, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent myIntent = new Intent(activity, cls);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(myIntent);
                activity.finishAffinity();
            }
        });

        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
    }

    public void alertDialogWithIntentWithoutFinish(String alertmessage, final Class cls) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(alertmessage);

        alertDialog.setTitle(alerttitle);
        alertDialog.setButton(alertbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent myIntent = new Intent(activity, cls);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(myIntent);
             }
        });

        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
    }

    public void alertDialog(String alertmessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(alertmessage);
        alertDialog.setTitle(alerttitle);
        alertDialog.setButton(alertbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
        //  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.darkPurple));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
    }

    public void alertDialogFinish(String alertmessage)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setMessage(alertmessage);
        alertDialog.setTitle(alerttitle);
        alertDialog.setCancelable(false);
        alertDialog.setButton(alertbutton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        });

        alertDialog.show();
        //  alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.darkPurple));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorAccent));
    }



    public void alertDialogConfirm(String alertmessage)

    {


        alerttitle = activity.getResources().getString(R.string.alert);
        alertbutton = activity.getResources().getString(R.string.yes);
        alertnegbutton = activity.getResources().getString(R.string.no);


        AlertDialog.Builder alert_builder = new AlertDialog.Builder(
                activity)
                .setCancelable(false)
                .setTitle(alerttitle)
                .setMessage(
                        alertmessage)
                .setPositiveButton(alertbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                alertDialogInterface.onPositiveButtonClicked();

                            }
                        })
                .setNegativeButton(alertnegbutton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.cancel();

                            }
                        });

        AlertDialog alert = alert_builder.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(18);

    }

}
