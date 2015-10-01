package com.example.lian.flickrimages;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import java.io.IOException;

public class GridActivity extends AppCompatActivity {
    //public static String[] photoSearchResults;
    private String query;

    private class Connection extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg) {
            connect();
            return null;
        }
    }

    private void connect() {
        //photoSearchResults = new String[1];
        Flickr f = new Flickr(MainActivity.API_KEY);
        PhotosInterface photosInterface = f.getPhotosInterface();
        SearchParameters myParameters = new SearchParameters();
        myParameters.setText(MainActivity.query);
        myParameters.setSort(SearchParameters.INTERESTINGNESS_DESC);
        GridView grid = (GridView) findViewById(R.id.gridView);
        try {
            PhotoList myList = photosInterface.search(myParameters, MainActivity.PHOTOS_PER_PAGE, 1);
            for (int i=0; i<MainActivity.PHOTOS_PER_PAGE;i++) {
                String photoURL = myList.get(i).getSmallUrl();
                View gridChild = grid.getChildAt(i);
                new DownloadImageTask((ImageView) gridChild).execute(photoURL);
            }
        }
        catch(IOException e) {
            Log.i("--- IOEXCEPTION !!!---", "");
            e.printStackTrace();
        }
        catch(FlickrException e) {
            Log.i("--- FlickrException -->" ,e.getErrorMessage());
            e.printStackTrace();
        }
        catch(Exception e) {
            PhotoList myList = new PhotoList();
            Log.i("---- ERROR ---","");
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        //Connection connection = new Connection();
        //connection.execute();
        new Connection().execute();

        GridView gridview = (GridView) findViewById(R.id.gridView);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
