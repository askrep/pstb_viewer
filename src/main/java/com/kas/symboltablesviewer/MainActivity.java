package com.kas.symboltablesviewer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   
   
   private BufferedReader bufferedReader;
   private BufferedWriter bufferedWriter;
   
   private String DB_NAME = "data.db";
   private String DB_PATH;
   
   DataBaseHelper dataBaseHelper;
   SQLiteDatabase db;
   Cursor myCursor;
   ArrayAdapter<String> userAdapter;
   private List<String> arrTblNames = new ArrayList<>();
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      
      
      Log.d("___", "test1");
      dataBaseHelper = new DataBaseHelper(this);
      dataBaseHelper.createDataBase();
      
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      
      ListView plcListView = findViewById(R.id.mainListView);
      
      db = dataBaseHelper.getReadableDatabase();
      arrTblNames = getTablesNames();
      
      
      try {
         userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrTblNames);
      } catch (Exception e) {
         Log.e("___Cursor", "" + e.getMessage());
      }
      plcListView.setAdapter(userAdapter);
      plcListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            String pressedPosition = null;
            
            pressedPosition = userAdapter.getItem(position);
            
            intent.putExtra("SELECTED_PLC", pressedPosition);
            Log.i("___pressed", "" + pressedPosition);
            
            startActivity(intent);
            
         }
      });
      
   }
   
   private List<String> getTablesNames() {
      ArrayList arrTableNames = new ArrayList<String>();
      Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
      
      if (c.moveToFirst()) {
         while (!c.isAfterLast()) {
            String plcName = c.getString(c.getColumnIndex("name"));
            Log.d("___", plcName);
            if (plcName.contains("table")) {
               arrTableNames.add(plcName.replace("table_", "PLC "));
            }
            c.moveToNext();
         }
      }
      return arrTableNames;
   }
   
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
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
      
      return super.onOptionsItemSelected(item);
   }
}
