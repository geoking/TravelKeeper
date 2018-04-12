package me.geoking.travelkeeper;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import me.geoking.travelkeeper.fragments.HolidayDetailsEditFragment;
import me.geoking.travelkeeper.fragments.HolidayDetailsFragment;
import me.geoking.travelkeeper.fragments.HolidayFragment;
import me.geoking.travelkeeper.fragments.MainFragment;
import me.geoking.travelkeeper.fragments.NearbyPlacesFragment;
import me.geoking.travelkeeper.model.Holiday;
import me.geoking.travelkeeper.model.HolidayDatabase;

import static android.graphics.BitmapFactory.decodeStream;

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
        HolidayDatabase.createInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);


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
            currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof  HolidayDetailsEditFragment) {
                if (currentFragment.getArguments() != null) {
                    buildAlertDialog("Discard changes?", "Are you sure you want to discard any changes made?\n\nNOTE: This will NOT delete the currently selected holiday", false);
                }
                else {
                    buildAlertDialog("Discard changes?", "Are you sure you want to discard any changes made?", false);
                }
                navigationView.setCheckedItem(R.id.nav_holidays);

            }
            else {
                super.onBackPressed();
                currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                tag = currentFragment.getTag();
                if (Objects.equals("main", tag)) {
                    navigationView.setCheckedItem(R.id.nav_home);
                } else if (Objects.equals("holidays", tag)) {
                    navigationView.setCheckedItem(R.id.nav_holidays);
                }
            }
        }
    }

    public void goFragmentBack () {
        super.onBackPressed();
    }

    public void buildAlertDialog(String title, String message, boolean noLocationPermission) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        if (!(noLocationPermission)) {
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            goFragmentBack();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .show();
        }
        else {
            builder.setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkLocationPermission();
                        }
                    })
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .show();
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
        Fragment currentFragment;
        final Holiday holiday;
        final FragmentTransaction transaction;
        transaction =
                getSupportFragmentManager().beginTransaction();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Bundle args = new Bundle();
        Bitmap newHolidayBitmap = null;
        switch (item.getItemId()) {
            case R.id.edit:
                currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                holiday = (Holiday) currentFragment.getArguments().getSerializable("Holiday");
                HolidayDetailsEditFragment newFragment = new HolidayDetailsEditFragment();
                args.putSerializable("Holiday", holiday);
                newFragment.setArguments(args);
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
            case R.id.confirm:
                HolidayDetailsEditFragment detailsEditFragment = (HolidayDetailsEditFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                EditText editTitle = (EditText)findViewById(R.id.holiday_details_title);
                EditText editTags = (EditText)findViewById(R.id.holiday_details_tags);
                Button startButton = (Button)findViewById(R.id.holiday_details_start);
                Button endButton = (Button)findViewById(R.id.holiday_details_end);
                EditText editNotes = (EditText)findViewById(R.id.holiday_details_notes);
                ImageView holidayImg = (ImageView)findViewById(R.id.holiday_details_image);
                Drawable drawable = holidayImg.getDrawable();
                boolean hasImage = (drawable != null);

                if (hasImage && (drawable instanceof BitmapDrawable)) {
                    newHolidayBitmap = ((BitmapDrawable) holidayImg.getDrawable()).getBitmap();

                }
                String newTitle = editTitle.getText().toString();
                String newTags = editTags.getText().toString();
                String newStart = startButton.getText().toString();
                String newEnd = endButton.getText().toString();
                String newNotes = editNotes.getText().toString();
                if (!detailsEditFragment.checkInputErrors()) {
                    return false;
                }
                if (detailsEditFragment.getArguments() != null) {
                    holiday = (Holiday) detailsEditFragment.getArguments().getSerializable("Holiday");
                    holiday.setTitle(newTitle);
                    holiday.setTags(newTags);
                    holiday.setStartDate(newStart);
                    holiday.setEndDate(newEnd);
                    holiday.setNotes(newNotes);
                    if (newHolidayBitmap != null) {
                        UUID uuid = UUID.randomUUID();
                        holiday.setImageLocationUUID(uuid.toString());
                        holiday.setImageLocation(saveToInternalStorage(newHolidayBitmap, uuid.toString()));
                    }
                    HolidayDatabase.getInstance().getHolidayDao().updateHoliday(holiday);

                }
                else {
                    holiday = new Holiday();
                    String holidayLocation=null;
                    UUID uuid=null;
                    if (newHolidayBitmap != null) {
                        uuid = UUID.randomUUID();
                        holidayLocation = saveToInternalStorage(newHolidayBitmap, uuid.toString());
                    }
                    String uuidString = null;
                    if (uuid != null) {
                        uuidString = uuid.toString();
                    }
                    Holiday newHoliday = addHoliday(holiday, newTitle, newTags, newStart, newEnd, newNotes, holidayLocation, uuidString);
                    HolidayDatabase.getInstance().getHolidayDao().insertHoliday(newHoliday);
                }
                super.onBackPressed();
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            case R.id.delete:
                currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                final boolean holidayBool;
                if (currentFragment.getArguments() != null) {
                    holidayBool = true;
                    holiday = (Holiday) currentFragment.getArguments().getSerializable("Holiday");
                }
                else {
                    holidayBool = false;
                    holiday = null;
                }
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Delete holiday")
                        .setMessage("Are you sure you want to delete this holiday?\n\nThis CANNOT be undone!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HolidayFragment newHolidayFragment = new HolidayFragment();
                                if (holidayBool) {
                                    HolidayDatabase.getInstance().getHolidayDao().deleteHoliday(holiday);
                                }
                                transaction.replace(R.id.fragment_container, newHolidayFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        String tag = null;
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_home) {
            fragmentClass  = MainFragment.class;
            tag = "main";
            navigationView.setCheckedItem(R.id.nav_home);
        } else if (id == R.id.nav_holidays) {
            fragmentClass = HolidayFragment.class;
            tag = "holidays";
            navigationView.setCheckedItem(R.id.nav_holidays);
        } else if (id == R.id.nav_visited) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        } else if (id == R.id.nav_gallery) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        } else if (id == R.id.nav_camera) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        } else if (id == R.id.nav_nearby) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                buildAlertDialog("Where are you again?", "This feature only works when we can receive your location. Please click 'OK' followed by 'Allow' and try that again!", true);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
            fragmentClass = NearbyPlacesFragment.class;
            tag = "nearby";
            navigationView.setCheckedItem(R.id.nav_nearby);
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        }


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

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String uuidString){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath =new File(directory,uuidString);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path, String uuid)
    {

        try {
            File f=new File(path, uuid);
            return decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Holiday addHoliday (Holiday holiday, String title, String tags, String startDate, String endDate, String notes, String imageLocation, String uuid) {
        holiday.setTitle(title);
        holiday.setTags(tags);
        holiday.setStartDate(startDate);
        holiday.setEndDate(endDate);
        holiday.setNotes(notes);
        holiday.setImageLocation(imageLocation);
        holiday.setImageLocationUUID(uuid);
        return holiday;
    }

}
