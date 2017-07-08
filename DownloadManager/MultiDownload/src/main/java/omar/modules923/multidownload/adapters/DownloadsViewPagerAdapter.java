package omar.modules923.multidownload.adapters;


          import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;
          import android.util.SparseArray;
          import android.view.ViewGroup;

          import com.activeandroid.util.Log;

          import java.util.ArrayList;

          import omar.modules923.multidownload.fragments.AllDownloads;
          import omar.modules923.multidownload.fragments.CompletedDownloads;
          import omar.modules923.multidownload.fragments.PausedDownloads;


public class DownloadsViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when DownloadsViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the DownloadsViewPagerAdapter is created

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public DownloadsViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }


    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new AllDownloads();

            case 1:
                return new PausedDownloads();

            case 2:
                return new CompletedDownloads();

        }


        return null;

    }
//    @Override
//    public Object instantiateItem(ViewGroup container, int position)
//    {
//        Fragment fragment = (Fragment) adaper.instantiateItem(mViewPager, position);
//
//        return fragment ;
//    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }




    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        Log.e("currentFragment",registeredFragments.get(position).getClass().getName());

        return registeredFragments.get(position);
    }

}
