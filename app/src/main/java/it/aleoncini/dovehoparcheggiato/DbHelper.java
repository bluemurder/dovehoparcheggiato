package it.aleoncini.dovehoparcheggiato;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "AppDB.db";

    public final class PlacesContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public PlacesContract() {}

        /* Inner class that defines the table contents */
        public abstract class Entry implements BaseColumns {
            public static final String TABLE_NAME = "PlacesTable";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_LAT = "lat";
            public static final String COLUMN_NAME_LONG = "long";
        }

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " +Entry.TABLE_NAME + " (" +
                        Entry._ID + " INTEGER PRIMARY KEY," +
                        Entry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_LAT + REAL_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_LONG + REAL_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;
    }

    public final class HistoryContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public HistoryContract() {}

        /* Inner class that defines the table contents */
        public abstract class Entry implements BaseColumns {
            public static final String TABLE_NAME = "HistoryTable";
            public static final String COLUMN_NAME_PLACE = "place";
            public static final String COLUMN_NAME_DATE = "date";
        }

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " +Entry.TABLE_NAME + " (" +
                        Entry._ID + " INTEGER PRIMARY KEY," +
                        Entry.COLUMN_NAME_PLACE + INTEGER_TYPE + COMMA_SEP +
                        Entry.COLUMN_NAME_DATE + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                //"yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(PlacesContract.SQL_DELETE_ENTRIES);
        db.execSQL(HistoryContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void onCreate(SQLiteDatabase db) {

        // Create table
        db.execSQL(PlacesContract.SQL_CREATE_ENTRIES);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Insert the new row
        values.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE, "Pzza Aprosio");
        values.put(PlacesContract.Entry.COLUMN_NAME_LAT, 44.4243191);
        values.put(PlacesContract.Entry.COLUMN_NAME_LONG, 8.8528946);
        db.insert(PlacesContract.Entry.TABLE_NAME,
                PlacesContract.Entry._ID,
                values);

        // Create table
        db.execSQL(HistoryContract.SQL_CREATE_ENTRIES);
    }

}
