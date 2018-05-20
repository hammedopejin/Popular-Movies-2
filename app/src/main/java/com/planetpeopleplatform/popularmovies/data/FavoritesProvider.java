package com.planetpeopleplatform.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class FavoritesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoritesDBHelper mOpenHelper;

    // Codes for the UriMatcher
    private static final int MOVIE = 100;
    private static final int MOVIES_ID = 101;

    private static final String FAILED_TO_INSERT_ROW_INTO = "Failed to insert row into ";
    private static final String CANNOT_HAVE_NULL = "Cannot have null content values";

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavoritesContract.MOVIE_PATH, MOVIE);
        uriMatcher.addURI(authority, FavoritesContract.MOVIE_PATH + "/#", MOVIES_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoritesDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mOpenHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                cursor = database.query(FavoritesContract.FavoritesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MOVIES_ID:
                String movie_id = uri.getLastPathSegment();
                selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{movie_id};
                cursor = database.query(FavoritesContract.FavoritesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return FavoritesContract.FavoritesEntry.CONTENT_DIR_TYPE;
            case MOVIES_ID:
                return FavoritesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }



    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case MOVIE:
                id = db.insertWithOnConflict(FavoritesContract.FavoritesEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = FavoritesContract.FavoritesEntry.buildFavoriteUri(id);
                } else {
                    throw new android.database.SQLException(FAILED_TO_INSERT_ROW_INTO + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(FavoritesContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoritesContract.FavoritesEntry.TABLE_NAME + "'");
                break;
            case MOVIES_ID:
                String movie_id = uri.getLastPathSegment();
                selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{movie_id};
                rowsDeleted = db.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoritesContract.FavoritesEntry.TABLE_NAME + "'");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        final int match = sUriMatcher.match(uri);
        if (contentValues == null){
            throw new IllegalArgumentException(CANNOT_HAVE_NULL);
        }

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(FavoritesContract.FavoritesEntry.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case MOVIES_ID:
                String movie_id = uri.getLastPathSegment();
                selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{movie_id};
                rowsUpdated = db.update(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : contentValues) {
                        if (value == null){
                            throw new IllegalArgumentException(CANNOT_HAVE_NULL);
                        }
                        long id = -1;
                             id = db.insertWithOnConflict(FavoritesContract.FavoritesEntry.TABLE_NAME,
                                    null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    if(returnCount > 0){
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if(returnCount > 0){
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return returnCount;
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }

}
