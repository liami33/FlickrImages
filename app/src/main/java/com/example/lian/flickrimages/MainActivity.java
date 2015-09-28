package com.example.lian.flickrimages;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "2e9e404bc26d63ad8b97c1abd125355c";
    public static final String PHOTOS = "com.example.lian.flickrimages.PHOTOS";
    public static String[] photoSearchResults;
    public static final int PHOTOS_PER_PAGE = 9;



    private class Connection extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg) {
            connect();
            return null;
        }
    }

    private void connect() {
        MainActivity.photoSearchResults = new String[1];
        Flickr f = new Flickr(API_KEY);
        PhotosInterface photosInterface = f.getPhotosInterface();
        SearchParameters myParameters = new SearchParameters();
        myParameters.setText("bumblebee");
        myParameters.setSort(SearchParameters.INTERESTINGNESS_DESC);
        GridView grid = (GridView) findViewById(R.id.gridView2);
        try {
            PhotoList myList = photosInterface.search(myParameters, MainActivity.PHOTOS_PER_PAGE, 1);
            for (int i=0; i<MainActivity.PHOTOS_PER_PAGE;i++) {
                String photoURL = myList.get(i).getSmallUrl();
                View gridChild = grid.getChildAt(i);
                new DownloadImageTask((ImageView) gridChild).execute(photoURL);
            }
        }
        catch(IOException e) {
            Log.i("--- IOEXCEPTION !!!---","");
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
        setContentView(R.layout.activity_main);

        //Connection connection = new Connection();
        //connection.execute();
        new Connection().execute();

        GridView gridview = (GridView) findViewById(R.id.gridView2);
        ImageAdapter adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
