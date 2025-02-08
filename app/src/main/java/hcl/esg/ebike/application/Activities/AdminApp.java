package hcl.esg.ebike.application.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import hcl.esg.ebike.application.Fragments.CyclesListFragment;
import hcl.esg.ebike.application.Fragments.HistoryFragment;
import hcl.esg.ebike.application.Fragments.HomeFragment;
import hcl.esg.ebike.application.Fragments.UsersFragment;
import hcl.esg.ebike.application.R;

public class AdminApp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.bottom_home){
                    openFragment(new HomeFragment());
                    return true;
                }else if(itemId == R.id.bottom_cycle) {
                    openFragment(new CyclesListFragment());

                    return true;
                }
                else if(itemId == R.id.bottom_users) {
                    openFragment(new UsersFragment());
                    return true;
                }
                else if(itemId == R.id.bottom_history) {
                    openFragment(new HistoryFragment());
                    return true;
                }
                return false;
            }
        });
        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());


    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.add_cycles){
            Intent intent = new Intent(AdminApp.this, AddCycles.class);
            startActivity(intent);

        }else if (itemId == R.id.damaged_cycles){
            Intent intent = new Intent(AdminApp.this, DamagedCycles.class);
            startActivity(intent);
        }
        else if (itemId == R.id.qr_code_scanner){
            Intent intent = new Intent(AdminApp.this, QrCodeScanner.class);
            startActivity(intent);
        }
        else if (itemId == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();

            // Remove email from SharedPreferences
            SharedPreferences preferences = getSharedPreferences("YOUR_PREFS_NAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("email");
            editor.apply();
            Intent intent = new Intent(AdminApp.this, Login.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {

            super.onBackPressed();
        }
    }
    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
