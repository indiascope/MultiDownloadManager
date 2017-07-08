package omar.apps923.downloadmanager.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

//import com.activeandroid.query.Select;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;

import com.activeandroid.query.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import omar.apps923.downloadmanager.R;
import omar.apps923.downloadmanager.databinding.ActMainBinding;
import omar.apps923.downloadmanager.helpers.Prefs;
import omar.modules923.multidownload.Download.DownloadService;
import omar.modules923.multidownload.activities.DownloadsActivity;
import omar.modules923.multidownload.models.Download;

public class MainActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener

        {

    public ActMainBinding binding;

    String urlDownload ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.act_main);
        initVars();

        binding.rlToolBar.imgvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });

        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                urlDownload = binding.edUrl.getText().toString();

                if(!urlDownload.isEmpty())
                {
                    if(URLUtil.isValidUrl(urlDownload))
                    {
                        final int version = Build.VERSION.SDK_INT;

                        if (version >= 23) {

                            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                //  return;


                                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        58);

                            }                        // TODO: Consider calling
                            else {

                                addDownload();
                                // binding.edUrl.setText("");

                            }

                        }

                        else {

                            addDownload();
                            // binding.edUrl.setText("");

                        }


                    }
                    else
                    {
                        customAlertDialog.alertDialog(getString(R.string.invalidLINK));
                     }
                }
                else
                {
                    customAlertDialog.alertDialog(getString(R.string.enterLinkWarn));

                }



            }
        });




        binding.btnDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(MainActivity.this,DownloadsActivity.class);
                startActivity(intent);
            }
        });


    }

            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemSettings:
                        Intent intentSettings=new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intentSettings);
                         return true;
                    case R.id.itemLicense:
                        Intent intentLicense=new Intent(MainActivity.this,LicenseActivity.class);
                        startActivity(intentLicense);
                        return true;
                    default:
                        return true;
                }
            }


    public void addDownload( )
    {

        Download downloadItem = new Download( urlDownload
        ,Prefs.getString(getString(R.string.prefsDirectoryLocation)
        ,Environment.getExternalStorageDirectory().getAbsolutePath()));

        Download downloadItemTemp =  new Select()
                .from(Download.class)
                .where("url = ?", urlDownload)
                .executeSingle();

        if(downloadItemTemp==null)
        {
            DownloadService.intentDownload(this, downloadItem.getUrl(), downloadItem);

        }
        else
        {
            customAlertDialog.alertDialog(getString(R.string.foundBefore));

        }


    }




    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 58) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try
                {
                    addDownload();
                }
                catch (Exception e)
                {
                    Log.e("ExceptionDownload",e.toString());
                }

            }

        }
    }

            @Subscribe(threadMode = ThreadMode.MAIN)
            public void updateDownloadItem(Download download)
            {

                if(download.getProgress()>=100)

                {
                    Toast.makeText(getApplicationContext(),getString(R.string.downloadOf )
                            +" "+download.getFileName()+" "+getString(R.string.completed),Toast.LENGTH_LONG).show();

                }

             }




    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }







}
