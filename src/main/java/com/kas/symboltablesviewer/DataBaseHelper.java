package com.kas.symboltablesviewer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
   
   
   public static final String COLUMN_TAG = "tag";
   public static final String COLUMN_ADDRESS = "address";
   public static final String COLUMN_COMMENT = "comments";
   
   private static String TAG = "___DataBaseHelper"; // Tag just for the LogCat window
   //destination path (location) of our database on device
   private static String DB_PATH = "";
   
   private static String DB_NAME = "data.db";
   private SQLiteDatabase mDataBase;
   private final Context mContext;
   
   public DataBaseHelper(Context context) {
      super(context, DB_NAME, null, 1);// 1? its Database Version
      DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
      Log.i(TAG, DB_PATH);
      this.mContext = context;
   }
   
   public void createDataBase() {
      //If database not exists copy it from the assets
      
      boolean mDataBaseExist = checkDataBase();
      if (!mDataBaseExist) {
         this.getReadableDatabase();
         this.close();
         try {
            //Copy the database from assests
            copyDataBase();
            Log.e(TAG, "createDatabase database created");
         } catch (IOException mIOException) {
            Log.e(TAG, "createDataBase " + mIOException + "");
         }
      }
   }
   
   //Check that the database exists here: /data/data/your package/databases/Da Name
   private boolean checkDataBase() {
      File dbFile = new File(DB_PATH + DB_NAME);
      Log.v("___checkDataBase", dbFile + "   " + dbFile.exists());
      return dbFile.exists();
   }
   
   //Copy the database from assets
   private void copyDataBase() throws IOException {
      try {
         InputStream mInput = mContext.getAssets().open(DB_NAME);
         String outFileName = DB_PATH + DB_NAME;
         OutputStream mOutput = new FileOutputStream(outFileName);
         byte[] mBuffer = new byte[1024];
         int mLength;
         while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
         }
         mOutput.flush();
         mOutput.close();
         mInput.close();
      } catch (IOException mIOException) {
         Log.e(TAG, "___copyDataBase " + mIOException + "");
         
      }
   }
   
   //Open the database, so we can query it
   public boolean openDataBase() throws SQLException {
      String mPath = DB_PATH + DB_NAME;
      Log.v("___openDataBase", mPath);
      mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
      //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
      return mDataBase != null;
   }
   
   public Cursor mySelectAllFromTable(SQLiteDatabase db, String nameSelectedPlc) {
      
      Cursor myCursor = null;
      try {
         
         myCursor = db.rawQuery("select * from " + nameSelectedPlc, null);
      } catch (Exception e) {
         Log.e("___ ALM mySelectAll "+nameSelectedPlc, e.getMessage());
      }
      
      return myCursor;
   }
   
   @Override
   public synchronized void close() {
      if (mDataBase != null)
         mDataBase.close();
      super.close();
   }
   
   @Override
   public void onCreate(SQLiteDatabase arg0) {
      // TODO Auto-generated method stub
      
   }
   
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      
   }
   
}
