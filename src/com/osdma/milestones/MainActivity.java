package com.osdma.milestones;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.osdma.R;
import com.osdma.milestones.adapters.GridViewAdapter;

public class MainActivity extends Activity implements OnClickListener{
	private static final CharSequence CAPTURE_TAB_TEXT = "Admin";
	private static final CharSequence GALLERY_TAB_TEXT = "Users";
	private static final CharSequence REGISTER_TAB_TEXT = "Register";
	private ViewPager viewPager;
	private GridViewAdapter customGridAdapter;
	private Context context;
	private SharedPreferences settings = null;
	private String PREF_NAME = "OSDMADATA";
	private String USERNAME = "username";
	private String PASSWORD = "password";
	private String SITENO = "sitenumber";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);  
        context = this;
        settings = this.getSharedPreferences(
				PREF_NAME, Activity.MODE_PRIVATE);

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager = (ViewPager)findViewById(R.id.pager);
        //Admin
        Tab captureTab = getActionBar().newTab();
        captureTab.setText((CharSequence) CAPTURE_TAB_TEXT);
        ApplicationTabListener applicationTabListener = new ApplicationTabListener();
        captureTab.setTabListener(applicationTabListener);
        getActionBar().addTab(captureTab);
        //User
        Tab galleryTab = getActionBar().newTab();
        galleryTab.setText((CharSequence) GALLERY_TAB_TEXT);
        galleryTab.setTabListener(applicationTabListener);
        getActionBar().addTab(galleryTab);
        //Register
        Tab registerTab = getActionBar().newTab();
        registerTab.setText((CharSequence) REGISTER_TAB_TEXT);
        registerTab.setTabListener(applicationTabListener);
        getActionBar().addTab(registerTab);
        
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				//System.out.println("Inside onPageSelected....");
				if(arg0 == 0){
					getActionBar().setSelectedNavigationItem(0);
				}else if(arg0==1){
					//getAllImages();
					getActionBar().setSelectedNavigationItem(1);					
				}else if(arg0==2){
					//getAllImages();
					getActionBar().setSelectedNavigationItem(2);					
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        setPagerAdapter();
    }

	public void setPagerAdapter(){
    	viewPager.setAdapter(new ViewPagerAdapter());
    }
    
	public class ViewPagerAdapter extends PagerAdapter{
    	@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view==(LinearLayout)object;
		}
		
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub			
			return super.getItemPosition(object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			LinearLayout linearLayout = null;
			
			if(position==0){
				linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.capture_layout, null);
				
			}else if(position==1){
				linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.gallery_layout, null);
			}else if(position==2){
				linearLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.register_layout, null);
			}
			((ViewPager)container).addView(linearLayout, 0);
			return linearLayout;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView((LinearLayout)object);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			
		}
    }

    private class ApplicationTabListener implements ActionBar.TabListener{

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(tab.getText().equals(CAPTURE_TAB_TEXT)){
				setPagerAdapter();
				viewPager.setCurrentItem(0);
			}else if(tab.getText().equals(GALLERY_TAB_TEXT)){
				viewPager.setCurrentItem(1);
			}else if(tab.getText().equals(REGISTER_TAB_TEXT)){
				viewPager.setCurrentItem(2);
			}
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(tab.getText().equals(CAPTURE_TAB_TEXT)){
				//setPagerAdapter();
				viewPager.setCurrentItem(0);
			}else if(tab.getText().equals(GALLERY_TAB_TEXT)){
				viewPager.setCurrentItem(1);
			}else if(tab.getText().equals(REGISTER_TAB_TEXT)){
				viewPager.setCurrentItem(2);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case R.id.action_settings:
            openSettings();
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	private void openSettings(){
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			LinearLayout set = new LinearLayout(this);
			set.setOrientation(1);
		    final EditText username = new EditText(this);
		    if("".equals(settings.getString(USERNAME, ""))) {
		    	username.setHint("UserName");
		    }
		    else {
		    	username.setText(settings.getString(USERNAME, ""));
		    }
		    
		    final EditText password = new EditText(this);
		    if("".equals(settings.getString(PASSWORD, ""))) {
		    	password.setHint("PASSWORD");
		    }
		    else {
		    	password.setText(settings.getString(PASSWORD, ""));
		    }
		    
		    final EditText siteno = new EditText(this);
		    if("".equals(settings.getString(SITENO, ""))) {
		    	siteno.setHint("SITE NUMBER");
		    }
		    else {
		    	siteno.setText(settings.getString(SITENO, ""));
		    }
		    
		    set.addView(username);
		    set.addView(password);
		    set.addView(siteno);
		    alert.setView(set);
			alert.setNeutralButton("OK", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences.Editor editor = settings.edit();
			        editor.putString(USERNAME, username.getText().toString());
			        editor.putString(PASSWORD, password.getText().toString());
			        editor.putString(SITENO, siteno.getText().toString());
			        editor.commit();
				}
				
			}).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	/*private void getData() {
        // TODO Auto-generated method stub
	    System.out.println("Inside getdata....");
	    if(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+FOLDER_NAME).exists() && Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+FOLDER_NAME).isDirectory()){
	         if(!data.isEmpty()){
	                 data.clear();
	         }
	         File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+FOLDER_NAME).listFiles();
	         ImageLoader imageLoader = new ImageLoader();
	         imageLoader.execute(files);
	    }
	    System.out.println("Data length : " + data.size());
	}
	*/
}