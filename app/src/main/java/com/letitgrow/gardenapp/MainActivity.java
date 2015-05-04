package com.letitgrow.gardenapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import android.app.ListActivity;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;
    boolean ListFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_list);
        this.getListView().setDividerHeight(2);
        ListFavorites = false;
        fillData();
        registerForContextMenu(getListView());

    }
    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createTodo() {
        Intent i = new Intent(this, PlantDetailActivity.class);
        startActivity(i);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, PlantDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE, todoUri);

        startActivity(i);
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { PlantTable.COLUMN_PLANT };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.plant_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }

    public void onPlantNowClicked(View view){
        Intent i = new Intent(this, PlantNowActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/");
        i.putExtra(MyContentProvider.CONTENT_TYPE, todoUri);
        i.putExtra("favPushed", ListFavorites);
        startActivityForResult(i, Activity.RESULT_OK);
    }

    public void onMainToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        ContentValues values = new ContentValues();
        if (on) {
           ListFavorites = true;
        } else {
           ListFavorites = false;
        }

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ListFavorites = data.getBooleanExtra("favNowPushed", false);
            ToggleButton aTglBtn = (ToggleButton) findViewById(R.id.mainToggleButton);
            aTglBtn.setChecked(ListFavorites);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String SELECTION;

        if (ListFavorites){
            SELECTION = PlantTable.COLUMN_FAVORITE+"= 'Y'";
        }
        else SELECTION = null;

        String[] projection = { PlantTable.COLUMN_ID, PlantTable.COLUMN_PLANT };
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, SELECTION, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}
