package com.example.juan.theapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.juan.theapp.Domain.User;

import java.util.ArrayList;
import java.util.List;

public class AppDB extends SQLiteOpenHelper {
    private static int BD_VERSION = 1;
    private static String BD_NAME = "bd_project";
    private static String TABLE_NAME = "ranking";
    private SQLiteDatabase database;
    private static final class Column {
        static final String ID = "id";
        static final String NICK = "nick";
        static final String POINTS = "points";
        static final String IMAGE = "image";
    }

    public AppDB(Context context) {
        super(context, BD_NAME, null, BD_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" ("+Column.ID+" INTEGER PRIMARY KEY,"+Column.NICK+" TEXT UNIQUE,"+Column.POINTS+" INTEGER, "+Column.IMAGE+" TEXT)");
        sqLiteDatabase.execSQL("INSERT INTO "+ TABLE_NAME +" ("+Column.ID+", "+Column.NICK+", "+Column.POINTS+") " +
                "VALUES('0','Guest','-1')");
    }

    public User getUserWithId(long id) {
        Cursor cursor = database.query(AppDB.TABLE_NAME, null, Column.ID + "=" + id, null, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    public void registerUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.ID, user.getId());
        contentValues.put(Column.POINTS, user.getPoints());
        if (user.hasProfilePic()) contentValues.put(Column.IMAGE, user.getProfileImage().toString());
        contentValues.put(Column.NICK, user.getNickName());
        database.insert(TABLE_NAME, null, contentValues);
    }

    public void resetPoints() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.POINTS, -1);
        database.update(TABLE_NAME, contentValues, null, null);
    }

    public void updateUser(User user) {
        ContentValues contentValues = new ContentValues();
        if (user.hasProfilePic()) contentValues.put(Column.IMAGE, user.getProfileImage().toString());
        contentValues.put(Column.POINTS, user.getPoints());

        database.update(TABLE_NAME, contentValues, Column.ID+"="+user.getId(), null);
    }

    public List<User> getAllUsersWithPoints() {
        Cursor cursor = database.query(AppDB.TABLE_NAME, null, Column.POINTS+">=0", null, null, null, AppDB.Column.POINTS);

        ArrayList<User> list = new ArrayList<>(cursor.getCount());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToUser(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    private static User cursorToUser(Cursor cursor) {
        return new User(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3) == null ? null:Uri.parse(cursor.getString(3)));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void close() {
        super.close();
        database.close();
    }
}
