package distance.com.tms.distancemeasurement.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import distance.com.tms.distancemeasurement.POJO_Class.RecordDto;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Record.db";
    public static final String TABLE_NAME = "records";
    String memberId;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TODO Auto-generated method stub
        db.execSQL(
                "create table records" +
                        "(id Long primary key, fullname String, distance String, steps String, caloriesburn String)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS message");
        onCreate(db);
    }

    public boolean insertMessage (String fullName, String distance, String steps, String caloriesBurn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullName", fullName);
        contentValues.put("distance", distance);
        contentValues.put("steps", steps);
        contentValues.put("caloriesBurn",caloriesBurn);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from messages where memberid="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (String fullName, String distance,String steps, String caloriesBurn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullName", fullName);
        contentValues.put("distance", distance);
        contentValues.put("steps", steps);
        contentValues.put("caloriesBurn",caloriesBurn);
        db.update(TABLE_NAME, contentValues, "fullname = ? ", new String[] { fullName } );
        return true;
    }

    public Integer deleteContact (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("messages",
                "date = ? ",
                new String[] { id });
    }

    public ArrayList<RecordDto> getAllMessages(String name) {
        //ArrayList<String> array_list = new ArrayList<String>();
        ArrayList<RecordDto> array_list = new ArrayList<RecordDto>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from records where fullname='"+name+"'", null );
        cursor.moveToFirst();

       /* while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;*/

        if (cursor.moveToFirst()) {
            do {
                RecordDto records = new RecordDto();

                records.setFullName(cursor.getString(1));
                records.setDistance(cursor.getString(2));
                records.setSteps(cursor.getString(3));
                records.setCaloriesBurn(cursor.getString(4));

                array_list.add(records);
            } while (cursor.moveToNext());
        }

        return array_list;
    }

    public void deleteAllPushMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
