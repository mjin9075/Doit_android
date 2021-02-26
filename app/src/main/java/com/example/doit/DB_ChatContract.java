package com.example.doit;

import android.provider.BaseColumns;

public final class DB_ChatContract {

    private DB_ChatContract() {
    }

    public static class ChatEntry {
        public static final String TABLE_NAME = "chat";

        public static final String C_ID = "_id";

        public static final String COLUMN_RoomN = "roomN";
        public static final String COLUMN_SenderID = "senderID";
        public static final String COLUMN_MSG = "msg";
        public static final String COLUMN_TIME_C = "time_c";
        public static final String COLUMN_TIME_D = "time_d";
        public static final String COLUMN_MyName = "myName";
        public static final String COLUMN_MyIMG = "myImg";
        public static final String COLUMN_Iam = "Iam";

    }
}
