package it.aleoncini.dovehoparcheggiato;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor hcursor = db.rawQuery("SELECT * FROM " + DbHelper.HistoryContract.Entry.TABLE_NAME + " ORDER BY " + DbHelper.HistoryContract.Entry._ID + " DESC", null);

        TextView p1 = (TextView)findViewById(R.id.textViewP1);
        TextView p2 = (TextView)findViewById(R.id.textViewP2);
        TextView p3 = (TextView)findViewById(R.id.textViewP3);

        p1.setText("");
        p2.setText("");
        p3.setText("");

        if(hcursor.getCount() > 0)
        {
            hcursor.moveToFirst();
            int placeid = hcursor.getInt(
                    hcursor.getColumnIndexOrThrow(
                            DbHelper.HistoryContract.Entry.COLUMN_NAME_PLACE
                    )
            );
            String date = hcursor.getString(
                    hcursor.getColumnIndexOrThrow(
                            DbHelper.HistoryContract.Entry.COLUMN_NAME_DATE
                    )
            );
            Cursor pcursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " WHERE " + DbHelper.PlacesContract.Entry._ID + "=" + placeid, null);
            pcursor.moveToFirst();
            String name = pcursor.getString(
                    pcursor.getColumnIndexOrThrow(
                            DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE
                    )
            );
            p1.setText(name + ", " + date);
        }

        if(hcursor.getCount() > 1) {
            //////////////////////////////
            hcursor.moveToNext();
            int placeid = hcursor.getInt(
                    hcursor.getColumnIndexOrThrow(
                            DbHelper.HistoryContract.Entry.COLUMN_NAME_PLACE
                    )
            );
            String date = hcursor.getString(
                    hcursor.getColumnIndexOrThrow(
                            DbHelper.HistoryContract.Entry.COLUMN_NAME_DATE
                    )
            );
            Cursor pcursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " WHERE " + DbHelper.PlacesContract.Entry._ID + "=" + placeid, null);
            pcursor.moveToFirst();
            String name = pcursor.getString(
                    pcursor.getColumnIndexOrThrow(
                            DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE
                    )
            );
            p2.setText(name + ", " + date);
        }

        //////////////////////////////
        if(hcursor.getCount() > 2) {
            hcursor.moveToNext();
            int placeid = hcursor.getInt(
                    hcursor.getColumnIndexOrThrow(
                            DbHelper.HistoryContract.Entry.COLUMN_NAME_PLACE
                    )
            );
            String date = hcursor.getString(
                    hcursor.getColumnIndexOrThrow(
                            DbHelper.HistoryContract.Entry.COLUMN_NAME_DATE
                    )
            );
            Cursor pcursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " WHERE " + DbHelper.PlacesContract.Entry._ID + "=" + placeid, null);
            pcursor.moveToFirst();
            String name = pcursor.getString(
                    pcursor.getColumnIndexOrThrow(
                            DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE
                    )
            );
            p3.setText(name + ", " + date);
        }

    }

    public void CreateNewHistory(View view) {
        finish();
        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
    }
}
