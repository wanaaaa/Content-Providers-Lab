package com.example.ubun17.myapplication;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarkitAPI extends AppCompatActivity {
    private static final String TAG = "log//////";
    private static final String URL_CEREAL =
            "http://dev.markitondemand.com/MODApis/Api/v2/Quote/JSON?symbol=googl";
    public static final Uri CONTENT_URI = ProductsContract.Products.CONTENT_URI;
    private ContentResolver mContentResolver;
    Button buAddData;
    List<String> listArray = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;

    private DownloadTask mCerealTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markit_api);
        mContentResolver = getContentResolver();


        buAddData = (Button) findViewById(R.id.buAdd);
        buAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    mCerealTask = new DownloadTask();
                    mCerealTask.execute(URL_CEREAL);

                    mListView = (ListView) findViewById(R.id.listView);
                    mAdapter = new ArrayAdapter<>(MarkitAPI.this, android.R.layout.simple_list_item_1, listArray);
                    mListView.setAdapter(mAdapter);

                } else {
                    Toast.makeText(MarkitAPI.this, "no net work", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    // Given a URL, establishes an HttpUrlConnection and retrieves the web page content as a
    // InputStream, which it returns as a string.
    private List<String> downloadUrl(String myUrl) throws IOException, JSONException {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return parseJson(contentAsString);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private List<String> parseJson(String contentAsString) throws JSONException {
        //List<String> items = new ArrayList<>();

        //JSONArray root = new JSONArray(contentAsString);

//        for(int i = 0; i < root.length();i++) {
//            JSONObject object = root.getJSONObject(i);
//            String organization = object.getString("organization_name");
//            items.add(organization);
//        }

        JSONObject root = new JSONObject(contentAsString);
        String stName = root.getString("Name");
        String stPercent = root.getString("ChangePercent");
        String stNameChange = stName + " : " + stPercent;
        Log.d(TAG, stName);
        Log.d(TAG, stPercent);

        ContentValues values = new ContentValues();
        values.put(ProductsContract.Products.COLUMN_NAME,stName);
        values.put(ProductsContract.Products.COLUMN_CHANGE,stPercent);

        Uri uri = mContentResolver.insert(CONTENT_URI, values);

        listArray.add(stNameChange);
        //JSONArray array = root.getJSONArray();
//    for (int i = 0; i < array.length(); i++)
//    {
//      JSONObject item = array.getJSONObject(i);
//      String name = item.getString("name");
//      items.add(name);
//    }
        return listArray;
    }

    public String readIt(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String read;

        while((read = br.readLine()) != null) {
            sb.append(read);
        }
        return sb.toString();
    }

    private class DownloadTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> items = new ArrayList<>();

            try {
                items = downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return items;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(List<String> items) {
//            mItems.clear();
//            mItems.addAll(items);
//            mAdapter.notifyDataSetChanged();
        }
    }

}
