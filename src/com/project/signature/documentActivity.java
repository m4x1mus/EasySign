package com.project.signature;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarDb;
import com.project.signature.utils.Globals;
import com.project.signture.entities.User;

public class documentActivity extends Activity {
    ListView listView ;
    Cursor cursor;
    String[] values;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documents_layout);
        
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        
        //cursor = 
        if(Globals.user_display){
	        List<User> users = User.listAll(User.class);
	        List<String> list =new ArrayList<String>();
			for(User u: users){
				System.out.println(u.getName()+":"+u.getId());
				list.add(u.getName());
			}
	        // Defined Array values to show in ListView
	        values = list.toArray(new String[list.size()]);
	        
        }else{
        	values = new String[]{
        			"Document one",
        			"Document two",
        			"Document three",
        			"Document four",
        			"Document five",
        			"Document six"
        	};
        }
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
                
               // ListView Clicked item index
               int itemPosition     = position;
               
               // ListView Clicked item value
               String  itemValue    = (String) listView.getItemAtPosition(position);
                  
                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "TODO: Open Document" +itemValue , Toast.LENGTH_LONG)
                  .show();
                if(Globals.user_display){
                	Globals.user_display = false;
                	Intent list = new Intent(documentActivity.this, documentActivity.class);
    				startActivity(list);
                }
              }

         }); 
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Globals.user_display = true;
	}

	private Cursor getCursor() {
        SQLiteDatabase db = new SugarDb(this).getReadableDatabase();
        return db.rawQuery("Select * from user", null);
    }

}
