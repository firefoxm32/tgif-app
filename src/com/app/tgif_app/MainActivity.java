package com.app.tgif_app;


import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.tgif.http.EndPoints;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import model.DrawerItem;
import model.Session;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	public static Toolbar mToolbar;
	public int pos = -1;
	public int globalPos;
	public int _globalPos;
	public static boolean loaded = true;
	private ProgressDialog pDialog;
	protected Session session;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**x
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static Context appContext;
    //public static TGIFRequest tgifRequest;
    
    
    public static String[] menus = new String[]{
			"Home","Food Menu","My Order","Check Out","Logout"
    };
    
    public static List<DrawerItem> list;
    
   public static Context getContext() {
	   return appContext;
   }
   public static void setContext(Context context) {
	   appContext = context;
   }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContext(this);
        
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = MainActivity.menus[0];//getString(MainActivity.menus[0]);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(mToolbar,
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    	onSectionAttached(position);
    }

    public void onSectionAttached(int number) {
    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (number) {            
        	case 0:
        		mTitle = MainActivity.menus[0];//getString(R.string.Home);
        		ft.replace(R.id.container, Fragment.instantiate(MainActivity.this, "com.app.tgif_app.Home"));
                ft.addToBackStack(null);
                ft.commit();
        		break;
        	case 1:
                mTitle = MainActivity.menus[1] + "s";//getString(R.string.Food_Menu);
                ft.replace(R.id.container, Fragment.instantiate(MainActivity.this, "com.app.tgif_app.FoodMenuFragment"));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 2:
                mTitle = MainActivity.menus[2] + "s";//getString(R.string.My_Order);
                
                loaded = false;
               
                Fragment myOrderFragment = new MyOrderFragment();
                
                ft.replace(R.id.container, myOrderFragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 3:
                mTitle = MainActivity.menus[3];//getString(R.string.Check_Out);
                break;
            case 4:
            	mTitle = MainActivity.menus[4];
            	session = new Session(getContext());
            	pDialog = new ProgressDialog(getContext());
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Loading.... Please wait...");
				pDialog.setIndeterminate(true);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.show();
            	if (session.getUsername() != "") {
            		System.out.println("uname: "+session.getUsername());
                	System.out.println("pass: "+session.getPassword());
                	System.out.println("pass: "+session.getTableNumber());
                	Ion.with(getContext())
                	.load(EndPoints.LOGOUT)
                	.progress(new ProgressCallback() {
						@Override
						public void onProgress(long arg0, long arg1) {
							// TODO Auto-generated method stub
							
						}
					})
                	.setBodyParameter("username", session.getUsername())
                	.asString()
                	.setCallback(new FutureCallback<String>() {
						@Override
						public void onCompleted(Exception e, String response) {
							// TODO Auto-generated method stub
							JsonParser jsonParser = new JsonParser();
							JsonObject object = jsonParser.parse(response).getAsJsonObject();
							
							if (object == null) {
								Toast.makeText(getContext(), "json null", Toast.LENGTH_SHORT).show();
								return;
							}
							if (object.get("status").getAsString().equalsIgnoreCase("error")) {
								Toast.makeText(getContext(), object.get("message").getAsString(), Toast.LENGTH_SHORT).show();
								return;
							}
							pDialog.dismiss();
							session.clearPrefs();
							finish();
						}
					});
				} else {
					Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
				}
            	break;
			default:
				mTitle = MainActivity.menus[0];//getString(R.string.Home);
        		ft.replace(R.id.container, Fragment.instantiate(MainActivity.this, "com.app.tgif_app.Home"));
                ft.addToBackStack(null);
                ft.commit();
				break;
        }
    }

    public void restoreActionBar() {
		//Instead of ActionBar, work with Toolbar
		mToolbar.setTitle(mTitle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}
}
