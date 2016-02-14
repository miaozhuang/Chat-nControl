package com.example.zhuangmi.chatncontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Created by Y510P on 12/25/2015.
 */
public class ChatDBAdapter {

    private static final String KEY_ID       =   "id_number";
    private static final String KEY_NAME     =   "id_name";
    private static final String KEY_IMAGE    =   "id_image";
    private static final String KEY_MSG      =   "id_message";
    private static final String KEY_CREATED = "created";

    public static final int NAMELIST_AREA   =   5;


    private static final String KEY_TIME    = "time";
    private static final String KEY_PERSON  = "person";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_ROWID   = "_id";
    private static final String KEY_IMAGE_PATH = "image_path";

    public static final int CONTENTLIST_AREA    =   5;

    private static final String TAG = "ChatDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table name_list (_id integer primary key autoincrement, "
            + "id_number text not null, id_name text not null, id_image text not null, id_message text not null, created text not null);";

    private static final String DATABASE_CHAT_CREATE_1 = "create table if not exists \'";

    private static final String DATABASE_CHAT_CREATE_2 = "" +
            "' (_id integer primary key autoincrement, "
            + "time text not null, person text not null, content text not null, image_path text not null);";

    private static final String DATABASE_NAME = "database";
    private static final String DATABASE_TABLE = "name_list";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {


        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS name_list");
            onCreate(db);
        }
    }
    public ChatDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public ChatDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        mDbHelper.close();
    }

    // add one person
    public long addNameList(String strID, String strName, String strImage) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, strID);
        initialValues.put(KEY_NAME, strName);
        initialValues.put(KEY_IMAGE, strImage);
        initialValues.put(KEY_MSG, "");
        Calendar calendar = Calendar.getInstance();
        String created = calendar.toString();//calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "小时" + calendar.get(Calendar.MINUTE) + "分种";
        initialValues.put(KEY_CREATED, created);
        long lRtn = mDb.insert(DATABASE_TABLE, null, initialValues);
        createChat(strID);
        return lRtn;
    }

    private void createChat(String strID)
    {
        // if the person does not exist, create a table with the name of the stringID of the person
        mDb.execSQL(DATABASE_CHAT_CREATE_1 + strID + DATABASE_CHAT_CREATE_2);
        //db.execSQL(DATABASE_CREATE);
    }

    public Cursor getAllName()
    {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_MSG }, null, null, null, null, null);
    }

    public long addChatContent(String strTable, String strTime, String strPerson, String strContent) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIME, strTime);
        initialValues.put(KEY_PERSON, strPerson);
        initialValues.put(KEY_CONTENT, strContent);
        Calendar calendar = Calendar.getInstance();
        String created = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        initialValues.put(KEY_IMAGE_PATH, created);
        return mDb.insert("\'"+strTable+"\'", null, initialValues);
    }

    public boolean deleteDiary(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllChats(String strID) {
        return mDb.query("\'"+strID+"\'", new String[] { KEY_ROWID, KEY_TIME, KEY_PERSON, KEY_CONTENT, KEY_IMAGE_PATH }, null, null, null, null, null);
        //return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID }, null, null, null, null, null);
    }

    public Cursor getChat(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TIME,KEY_PERSON,
                                KEY_CONTENT, KEY_CREATED }, KEY_ROWID + "=" + rowId, null, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // may no use
    public boolean updateDiary(long rowId, String strTime, String strPerson, String strContent) {
        ContentValues args = new ContentValues();
        args.put(KEY_TIME, strTime);
        args.put(KEY_PERSON, strPerson);
        args.put(KEY_CONTENT, strContent);
        Calendar calendar = Calendar.getInstance();
        String created = calendar.toString();//.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "小时" + calendar.get(Calendar.MINUTE) + "分种";
        args.put(KEY_CREATED, created);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
