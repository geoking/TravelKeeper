package me.geoking.travelkeeper;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import me.geoking.travelkeeper.fragments.HolidayDetailsEditFragment;
import me.geoking.travelkeeper.fragments.HolidayDetailsFragment;
import me.geoking.travelkeeper.fragments.HolidayFragment;
import me.geoking.travelkeeper.fragments.MainFragment;
import me.geoking.travelkeeper.model.Holiday;
import me.geoking.travelkeeper.model.HolidayData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener,
        HolidayFragment.OnListFragmentInteractionListener, HolidayDetailsFragment.OnFragmentInteractionListener, HolidayDetailsEditFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.fragment_container, new MainFragment(), "main");
        tx.commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HolidayData.createInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);



  /*      final Button holidaysbutton = findViewById(R.id.button_holidays);
        holidaysbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button visitedbutton = findViewById(R.id.button_visited);
        visitedbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
            }
        }); */
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        String tag = currentFragment.getTag();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (Objects.equals("main", tag)) {
            finish();
        }
        else {
            super.onBackPressed();
            currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            tag = currentFragment.getTag();
            if (Objects.equals("main", tag)) {
                navigationView.setCheckedItem(R.id.nav_home);
            }
            else if (Objects.equals("holidays", tag)) {
                navigationView.setCheckedItem(R.id.nav_holidays);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.edit:
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                Holiday holiday = (Holiday) currentFragment.getArguments().getSerializable("Holiday");
                HolidayDetailsEditFragment newFragment = new HolidayDetailsEditFragment();
                Bundle args = new Bundle();
                args.putSerializable("Holiday", holiday);
                newFragment.setArguments(args);
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_holidays);
                FragmentTransaction editTransaction =
                        getSupportFragmentManager().beginTransaction();
                editTransaction.replace(R.id.fragment_container, newFragment);
                editTransaction.addToBackStack(null);
                editTransaction.commit();
                return true;
            case R.id.add:
                HolidayDetailsEditFragment editFragment = new HolidayDetailsEditFragment();
                FragmentTransaction addTransaction =
                        getSupportFragmentManager().beginTransaction();
                addTransaction.replace(R.id.fragment_container, editFragment);
                addTransaction.addToBackStack(null);
                addTransaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String tag = null;
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_home) {
            fragmentClass  = MainFragment.class;
            tag = "main";
        } else if (id == R.id.nav_holidays) {
            fragmentClass = HolidayFragment.class;
            tag = "holidays";
        } else if (id == R.id.nav_visited) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_places) {

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Holiday holiday) {
        // Create the new fragment,
        HolidayDetailsFragment newFragment = new HolidayDetailsFragment();
        // add an argument specifying the holiday it should show
        // note that the DummyItem class must implement Serializable
        Bundle args = new Bundle();
        args.putSerializable("Holiday", holiday);
        newFragment.setArguments(args);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_holidays);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
