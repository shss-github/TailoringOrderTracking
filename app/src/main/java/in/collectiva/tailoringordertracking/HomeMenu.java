package in.collectiva.tailoringordertracking;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import in.collectiva.tailoringordertracking.CommonFunction.SessionManagement;

public class HomeMenu extends AppCompatActivity {
    // Session Manager Class
    SessionManagement session;

    //DrawerLayout variables
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        TextView ltxtWelcomeMsg = (TextView) findViewById(R.id.txtWelcomeMsg);

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String lName = user.get(SessionManagement.KEY_NAME);

        ltxtWelcomeMsg.setText("Welcome " + lName + "!");
        DrawerLayoutInitialization();
    }

    private void DrawerLayoutInitialization() {
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();
        DataModel[] drawerItem = new DataModel[7];

        drawerItem[0] = new DataModel(R.drawable.fixtures, "Items");
        drawerItem[1] = new DataModel(R.drawable.fixtures, "My Orders");
        drawerItem[2] = new DataModel(R.drawable.fixtures, "Order Tracking");
        drawerItem[3] = new DataModel(R.drawable.fixtures, "Make List");
        drawerItem[4] = new DataModel(R.drawable.fixtures, "Ready List");
        drawerItem[5] = new DataModel(R.drawable.fixtures, "Edit Profile");
        drawerItem[6] = new DataModel(R.drawable.fixtures, "Logout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    // Drawer Layout functions
    void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    private void selectItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                //Toast.makeText(getApplicationContext(), "Pay", Toast.LENGTH_SHORT).show();
                //fragment = new PayFragment();
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, payFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();*/
                //fragment = new Item();
                startActivity(new Intent(HomeMenu.this, Item.class));
                break;
            case 1:
                //fragment = new FixturesFragment();
                Toast.makeText(getApplicationContext(), "Add Money", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //fragment = new TableFragment();
                Toast.makeText(getApplicationContext(), "Pass Book", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                //fragment = new TableFragment();
                Toast.makeText(getApplicationContext(), "KYC", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
