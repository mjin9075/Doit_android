package com.example.doit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DB_Chat_DBHelper extends SQLiteOpenHelper {

    // DB의 버전으로 1부터 시작하고 스키마가 변경될 때 숫자를 올린다
    private static final int DB_VERSION = 1;

    // DB 파일명
    private static final String DB_NAME = "doitChat";

    // 테이블 생성 SQL문
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DB_ChatContract.ChatEntry.TABLE_NAME + " (" +
                    DB_ChatContract.ChatEntry.C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DB_ChatContract.ChatEntry.COLUMN_RoomN + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_SenderID + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_MSG + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_TIME_C + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_TIME_D + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_MyName + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_MyIMG + " TEXT," +
                    DB_ChatContract.ChatEntry.COLUMN_Iam + " TEXT)";


    public DB_Chat_DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    void insertChat (String RoomN, String SenderID, String MSG, String Time_c, String Time_d, String myName, String myImg, String Iam){
//        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_ChatContract.ChatEntry.COLUMN_RoomN, RoomN);
        values.put(DB_ChatContract.ChatEntry.COLUMN_SenderID, SenderID);
        values.put(DB_ChatContract.ChatEntry.COLUMN_MSG, MSG);
        values.put(DB_ChatContract.ChatEntry.COLUMN_TIME_C, Time_c);
        values.put(DB_ChatContract.ChatEntry.COLUMN_TIME_D, Time_d);
        values.put(DB_ChatContract.ChatEntry.COLUMN_MyName, myName);
        values.put(DB_ChatContract.ChatEntry.COLUMN_MyIMG, myImg);
        values.put(DB_ChatContract.ChatEntry.COLUMN_Iam, Iam);

        db.insert(DB_ChatContract.ChatEntry.TABLE_NAME,null,values);

    }

    public Cursor readChatOrderByTimeDWhereRoomN(String roomN) {
        SQLiteDatabase db = getReadableDatabase();

        //가져오려는데이터
        String[] projection ={
                DB_ChatContract.ChatEntry.COLUMN_RoomN,
                DB_ChatContract.ChatEntry.COLUMN_SenderID,
                DB_ChatContract.ChatEntry.COLUMN_MSG,
                DB_ChatContract.ChatEntry.COLUMN_TIME_C,
                DB_ChatContract.ChatEntry.COLUMN_TIME_D,
                DB_ChatContract.ChatEntry.COLUMN_MyName,
                DB_ChatContract.ChatEntry.COLUMN_MyIMG,
                DB_ChatContract.ChatEntry.COLUMN_Iam
        };

        //WHERE ROOM = roomN
        String selection = DB_ChatContract.ChatEntry.COLUMN_RoomN + "=?";
        String[] selectRoom = {String.valueOf(roomN)};

        //정렬 _최신게 아래에
        String sortOrder = DB_ChatContract.ChatEntry.COLUMN_TIME_D + " ASC";

        Cursor cursor = db.query(
                DB_ChatContract.ChatEntry.TABLE_NAME,
                projection, //반환 할 열 배열
                selection,
                selectRoom, //where문에 필요한 value
                null,
                null,
                sortOrder
        );
        return cursor;
    }

}
