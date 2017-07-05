package in.collectiva.tailoringordertracking.CommonFunction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import in.collectiva.tailoringordertracking.Fragments.AllFragment;
import in.collectiva.tailoringordertracking.Fragments.MakeListFragment;
import in.collectiva.tailoringordertracking.Fragments.ReadyListFragment;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class PageAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AllFragment tab1 = new AllFragment();
                return tab1;
            case 1:
                MakeListFragment tab2 = new MakeListFragment();
                return tab2;
            case 2:
                ReadyListFragment tab3 = new ReadyListFragment();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
