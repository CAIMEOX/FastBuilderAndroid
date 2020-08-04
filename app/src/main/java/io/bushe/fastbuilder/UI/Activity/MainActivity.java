package io.bushe.fastbuilder.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.navigation.NavigationView;

import cn.bmob.v3.BmobUser;
import io.bushe.fastbuilder.R;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "FastBuilder/Main";
    private static int onClick;
    private AppBarConfiguration mAppBarConfiguration;
    private Menu menu_busheID;
    private MenuItem menuItem_busheID_username;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_local, R.id.nav_cloud,
                R.id.nav_tools, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        menu_busheID = navigationView.getMenu();
        menuItem_busheID_username = menu_busheID.findItem(R.id.nav_username).setOnMenuItemClickListener(null);

        toolbar.setOnClickListener(v -> {
            ++onClick;
            if (onClick >= 5) {
//                    Toast.makeText(MainActivity.this, "Developer mode enabled.", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("DevTools")
                        .setMessage("This is a tool for developers, don't feel free to do it.")
                        .setPositiveButton("LoginActivity", (dialog, which) -> {
                            Log.d(TAG, "onClick: Developer Tools: BuSheIDActivity ");
                            long Time = System.currentTimeMillis();
                            startActivity(new Intent(MainActivity.this, BuSheIDActivity.class));
                            Log.d(TAG, "onClick: Developer Tools: Started BuSheIDActivity in " + (System.currentTimeMillis() - Time));
                        }).show();
            }
        });
        Intent intent = getIntent();
        String UserData = intent.getStringExtra("bushe-id-username");
        menuItem_busheID_username.setTitle(UserData);
        menu_busheID.findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
            SharedPreferences sharedPreferences = getSharedPreferences("BuSheID",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            BmobUser.logOut();
            android.os.Process.killProcess(android.os.Process.myPid());
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_licenses) {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.license_title));
            startActivity(new Intent(this, OssLicensesMenuActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
