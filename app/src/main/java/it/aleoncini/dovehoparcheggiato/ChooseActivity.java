package it.aleoncini.dovehoparcheggiato;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseActivity extends AppCompatActivity {

    Cursor _cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        /*http://www.vogella.com/tutorials/AndroidListView/article.html*/

        // DB for content
        ContentValues cv;
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        _cursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " ORDER BY " + DbHelper.PlacesContract.Entry._ID, null);
        // Total number of contents
        int placesNum = _cursor.getCount();

        final ListView listview = (ListView) findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < placesNum; ++i) {
            _cursor.moveToPosition(i);
            String name = _cursor.getString(
                    _cursor.getColumnIndexOrThrow(
                            DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE
                    )
            );

            list.add(name);
        }

        TextView noHistory = (TextView) findViewById(R.id.textViewNoHistory);
        if(placesNum == 0)
        {
            listview.setVisibility(View.GONE);
        }
        else
        {
            noHistory.setVisibility(View.GONE);
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                list.remove(item);
//                                adapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//                            }
//                        });
                CreateNewPark(position);
            }

        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void CreatePlaceAndHistory(View view) {

        EditText et = (EditText)findViewById(R.id.editText);
        String name = et.getText().toString();

        if(name == "")
        {
/*            Context context = getApplicationContext();
            CharSequence text = "Inserisci un nome!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/

            finish();
            startActivity(getIntent());
        }

        ContentValues cv = new ContentValues();
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Insert in places only if not already present
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " WHERE " + DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE + "='" + name +"'", null);
        if(cursor.getCount() == 0) {
            cv.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE, name);
            cv.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_LAT, 0);
            cv.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_LONG, 0);
            db.insert(DbHelper.PlacesContract.Entry.TABLE_NAME,
                    DbHelper.PlacesContract.Entry._ID,
                    cv);
            // Now select the place id
            cursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " WHERE " + DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE + "='" + name +"'", null);
        }

        cursor.moveToFirst();
        int placeid = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        DbHelper.PlacesContract.Entry._ID
                )
        );
        cv = new ContentValues();
        cv.put(DbHelper.HistoryContract.Entry.COLUMN_NAME_PLACE, placeid);
        cv.put(DbHelper.HistoryContract.Entry.COLUMN_NAME_DATE, DbHelper.getDateTime());
        db.insert(DbHelper.HistoryContract.Entry.TABLE_NAME,
                DbHelper.HistoryContract.Entry._ID,
                cv);

        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void CreatePlace(View view) {

        EditText et = (EditText)findViewById(R.id.editText);
        String name = et.getText().toString();

        if(name == "")
        {
/*            Context context = getApplicationContext();
            CharSequence text = "Inserisci un nome!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        }

        ContentValues cv = new ContentValues();
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        cv.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_TITLE, name);
        cv.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_LAT, 0);
        cv.put(DbHelper.PlacesContract.Entry.COLUMN_NAME_LONG, 0);
        db.insert(DbHelper.PlacesContract.Entry.TABLE_NAME,
                DbHelper.PlacesContract.Entry._ID,
                cv);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private void CreateNewPark(int userPosition){
        // Reset current user
        ContentValues cv;
        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        _cursor = db.rawQuery("SELECT * FROM " + DbHelper.PlacesContract.Entry.TABLE_NAME + " ORDER BY " + DbHelper.PlacesContract.Entry._ID, null);

        // Set selected as current and go to profile
        _cursor.moveToPosition(userPosition);
        int placeid = _cursor.getInt(
                _cursor.getColumnIndexOrThrow(
                        DbHelper.PlacesContract.Entry._ID
                )
        );

        cv = new ContentValues();
        cv.put(DbHelper.HistoryContract.Entry.COLUMN_NAME_PLACE, placeid);
        cv.put(DbHelper.HistoryContract.Entry.COLUMN_NAME_DATE, DbHelper.getDateTime());
        db.insert(DbHelper.HistoryContract.Entry.TABLE_NAME,
                DbHelper.HistoryContract.Entry._ID,
                cv);

        // go to
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
