package org.hfzy.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hfzy.R;

import org.hfzy.globle.SeviceUrl;
import org.hfzy.ui.fragment.HomeFragment;
import org.hfzy.ui.fragment.OtherFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
  //  private SwipeRefreshLayout swipeRefresh;
    public String TAG = "MainActivity";
    private Toolbar toolbar;
    private NavigationView navView;
    private ActionBar actionBar;
    private View headerView;
    private TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }

    private void initData() {
        //得到FragmentManager()
        FragmentManager  manager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rl,new HomeFragment());
        transaction.commit();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_news);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                int Id = item.getItemId();
                int groupId = item.getGroupId();
                Log.e(TAG, "groupId:" + groupId);
                //得到FragmentManager()
                FragmentManager  manager = getSupportFragmentManager();
                //开启事务
                FragmentTransaction transaction = manager.beginTransaction();
                switch (item.getItemId()+1) {
                    case R.id.nav_news:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.HOME));
                        break;
                    case R.id.nav_normal:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.NORMOL));
                        break;
                    case R.id.nav_recom:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.comm));
                        break;
                    case R.id.nav_movies:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.MOVIES));
                        break;
                    case R.id.nav_noBoring:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.NOBORING));
                        break;
                    case R.id.nav_design:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.DESIGN));
                        break;
                    case R.id.nav_bigCompany:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.BIGCOMPANY));
                        break;
                    case R.id.nav_money:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.MONEY));
                        break;
                    case R.id.nav_interet:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.INTENT));
                        break;
                    case R.id.nav_cartoon:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.CARTOON));
                        break;
                    case R.id.nav_sport:
                        transaction.replace(R.id.rl,new OtherFragment(SeviceUrl.SPORT));
                        break;

                }
                transaction.commit();
                return true;
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "you click login!");
            }
        });

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        actionBar = getSupportActionBar();
        //swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        headerView = navView.getHeaderView(0);
        login = (TextView) headerView.findViewById(R.id.login);
    }


    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        adapter.notifyDataSetChanged();
                       // swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

}
