package com.planetpeopleplatform.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHelper  extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "favorites.db";


        public FavoritesDBHelper (Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            final String SQL_CREATE_FAVORITE_TABLE =

                    "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_NAME + " (" +
                            FavoritesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE , " +
                            FavoritesContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                            FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                            FavoritesContract.FavoritesEntry.COLUMN_RATING + " REAL NOT NULL, " +
                            FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                            FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL" +
                            "); ";
            sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                    FavoritesContract.FavoritesEntry.TABLE_NAME + "'");
            onCreate(sqLiteDatabase);
        }

}
