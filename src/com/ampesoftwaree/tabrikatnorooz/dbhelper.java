package com.ampesoftwaree.tabrikatnorooz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper{

	private static String DB_PATH = "/data/data/com.ampesoftwaree.tabrikatnorooz/databases/";
	 
	private static String DB_NAME = "Tabrikestan";
	 
	private SQLiteDatabase myDataBase;
	 
	private final Context myContext;
	
	public dbhelper(Context context) {
		 
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		}	
	public void createDataBase() throws IOException{
		 
		boolean dbExist = checkDataBase();
		 
		if(dbExist){
		//do nothing - database already exist
		}else{

		this.getReadableDatabase();
		 
		try {
		 
		copyDataBase();
		 
		} catch (IOException e) {
		 
		throw new Error("Error copying database");
		 
		}
		}
		 
		}
	private boolean checkDataBase(){
		 
		SQLiteDatabase checkDB = null;
		 
		try{
		String myPath = DB_PATH + DB_NAME;
		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		 
		}catch(SQLiteException e){
		 
		//database does't exist yet.
		 
		}
		 
		if(checkDB != null){
		 
		checkDB.close();
		 
		}
		 
		return checkDB != null ? true : false;
		}
	private void copyDataBase() throws IOException{
		 
		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		 
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		 
		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		 
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
		myOutput.write(buffer, 0, length);
		}
		 
		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		 
		}
		 
		public void openDataBase() throws SQLException{
		 
		//Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		 
		}
		 
		@Override
		public synchronized void close() {
		 
		if(myDataBase != null)
		myDataBase.close();
		 
		super.close();
		 
		}
		
		public String getdata(int recordinx, int fieldpos){
			
			 Cursor c=myDataBase.rawQuery("select * from Main",null);
			 c.moveToPosition(recordinx);
			 String str=c.getString(fieldpos).toString();
			 return str;
		}
		
		public void editmsg(String ff, int idx){
			ContentValues cv=new ContentValues();
			cv.put("Message",ff);
			myDataBase.update("Main", cv, "_id="+idx , null);
		}

		public int countlistitems(int groupinx){
			Cursor c=myDataBase.rawQuery("select * from Dessert where Gpr="+groupinx,null);
			return c.getCount();
		}
      


		public String getdatabyid(int recordid, int fielindex){
			
			 Cursor c=myDataBase.rawQuery("select * from Dessert where _id="+recordid,null);
			 c.moveToFirst();
			 String str=c.getString(fielindex).toString();
			 return str;
		}

		public String favitems(int fieldindex,int recordindex, int groupinx){
			
			 Cursor c=myDataBase.rawQuery("select * from Dessert where Fav=1 AND Gpr="+groupinx,null);
			 c.moveToPosition(recordindex);
			 String str=c.getString(fieldindex).toString();
			 return str;
		}

		public int countfavitem(int groupinx){
			Cursor c=myDataBase.rawQuery("select * from Dessert where Fav=1 AND Gpr="+groupinx,null);
			return c.getCount();
		}

		public String searchitems(int recordindex,int fieldindex,String condition,int groupinx){
			
			 Cursor c=myDataBase.rawQuery("select * from Dessert where Gpr="+groupinx+" AND Dessertname like '%"+condition+"%'",null);
			 c.moveToPosition(recordindex);
			 String str=c.getString(fieldindex).toString();
			 return str;
		}

		public int countserachitems(String condition,int groupinx){
			Cursor c=myDataBase.rawQuery("select * from Dessert where Gpr="+groupinx+" AND Dessertname like '%"+condition+"%'",null);
			return c.getCount();
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
		 
		}
		 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
		}
		 
	
}
