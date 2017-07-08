package omar.modules923.multidownload.activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import omar.modules923.multidownload.Download.DownloadService;
import omar.modules923.multidownload.R;
import omar.modules923.multidownload.adapters.DownloadsViewPagerAdapter;
import omar.modules923.multidownload.databinding.ActDownloadsBinding;
import omar.modules923.multidownload.fragments.AllDownloads;
import omar.modules923.multidownload.fragments.BaseFragment;
import omar.modules923.multidownload.fragments.CompletedDownloads;
import omar.modules923.multidownload.fragments.PausedDownloads;
import omar.modules923.multidownload.models.Download;

import static omar.modules923.multidownload.models.Download.STATUS_CANCELLED;

public class DownloadsActivity extends BaseActivity
{
    Fragment currentFragment ;

    CharSequence Titles[]=new CharSequence[3];

    public ActDownloadsBinding binding;
    DownloadsViewPagerAdapter downloadsViewPagerAdapter ;
    public NotificationManagerCompat mNotificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.act_downloads);
        binding.rlToolBar.tvToolBarNormal.setText(getString(R.string.downloads));
        initVars();

        mNotificationManager = NotificationManagerCompat.from(
                getApplicationContext());

        Titles[0]=getString(R.string.allDownloads);
        Titles[1]=getString(R.string.pausedDownloads);
        Titles[2]=getString(R.string.completedDownloads);

          downloadsViewPagerAdapter =  new DownloadsViewPagerAdapter(getSupportFragmentManager(),Titles,3);

        binding.viewpager.setOffscreenPageLimit(1);
        binding.viewpager.setAdapter(downloadsViewPagerAdapter);

        binding.viewpagertab.setViewPager(binding.viewpager);

        binding.txtvStartAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                resumeAll();

            }
        });


        binding.txtvCancelAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                cancelAll();
            }
        });



        binding.txtvRemoveAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                removeAll();

            }
        });


        binding.viewpagertab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {


                getCurrentFragment();
                ((BaseFragment)currentFragment).loadData();
             }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }



    private void resumeAll() {

        List<Download> downloadList =new ArrayList<>();

        downloadList =  new Select()
                .from(Download.class)
                .where("status   !=  ?   " , STATUS_CANCELLED )
                .orderBy("id ASC")
                .execute();


        for (int ui = 0; ui < downloadList.size(); ui++) {


            Download download = ((downloadList.get(ui))) ;

            switch (download.getStatus()) {

                case Download.STATUS_PAUSED:
                    resumeDownload(download);

                    break;
                case Download.STATUS_NOT_DOWNLOAD:
                    resumeDownload(download);

                    break;
                case Download.STATUS_DOWNLOAD_ERROR:
                    resumeDownload(download);
                    break;



            }

        }

        getCurrentFragment();
        ((BaseFragment)currentFragment).loadData();

     }

    public void resumeDownload(Download download)
    {
        DownloadService.intentResume(this, download.getUrl());
    }

    private void cancelAll() {

        List<Download> downloadList =new ArrayList<>();

        downloadList =  new Select()
                .from(Download.class)
                .where("status   !=  ?   " , STATUS_CANCELLED )
                .orderBy("id ASC")
                .execute();


        for (int ui = 0; ui < downloadList.size(); ui++) {


            Download download = ((downloadList.get(ui))) ;

            switch (download.getStatus()) {


                case Download.STATUS_DOWNLOADING:

                     deleteDownload(download);
                    break;

                case Download.STATUS_PAUSED:
                     deleteDownload(download);
                    break;

            }

        }

         getCurrentFragment();
         ((BaseFragment)currentFragment).loadData();

        DownloadService.intentCancelAll(this);
    }

    private void removeAll()

    {

        List<Download> downloadList =new ArrayList<>();

        downloadList =  new Select()
                .from(Download.class)
                .where("status   !=  ?   " , STATUS_CANCELLED )
                .orderBy("id ASC")
                .execute();


        for (int ui = 0; ui < downloadList.size(); ui++) {
            Download download = ((downloadList.get(ui))) ;
             deleteDownload(download);
        }


         getCurrentFragment();

        ((BaseFragment)currentFragment).loadData();
        DownloadService.intentCancelAll(this);

    }


    private void deleteDownload(Download download) {

        download.delete();

        mNotificationManager.cancel(
                download.getId().intValue() + 1000
        );

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDownloadItem(Download download)
    {

         getCurrentFragment();
        ((BaseFragment)currentFragment).updateData(download);

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

    public void getCurrentFragment() {

        currentFragment =  downloadsViewPagerAdapter.getRegisteredFragment(binding.viewpager.getCurrentItem());


     }
}
