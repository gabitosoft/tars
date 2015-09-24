package com.gabitosoft.net;

/**
 * Created by Gabriel Delgado on 23-09-15.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpManager {

    private static final int TIMEOUT = 5000;
    private String url;

    public HttpManager() {

        url = "";
    }

    public HttpManager(String url) {

        this.url = url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public void sendPost(JSONObject json) throws Exception {

        try {

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost request = new HttpPost(this.url);
            request.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
            request.setHeader("json", json.toString());
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                InputStream inputStream = entity.getContent();
                String result = inputStreamToString(inputStream);
                Log.d("Read from server", result);
            }
        } catch (Exception ex) {

            Log.d("Fail on sendPost method", ex.getMessage());
        }
    }

    public void sendPost(String url, String json) throws Exception {

        try {

            new HttpAsyncTask().execute(url, json);
        } catch (Exception ex) {

            Log.d("Fail on sendPost method", ex.getMessage());
        }
    }

    public String inputStreamToString(InputStream inputStream) {

        String result = "";
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                result += line;
            }

            inputStream.close();
        } catch (Exception ex) {

            Log.d("inputStreamToString", ex.getMessage());
        }

        return result;
    }

    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager conectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... parameters) {

            String result = "";
            try {

                String url = parameters[0];
                String jsonString = parameters[1];

                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
                HttpClient client = new DefaultHttpClient(httpParams);

                HttpPost request = new HttpPost(url);
                request.setEntity(new ByteArrayEntity(jsonString.getBytes("UTF8")));
                Log.d("PARAMETERS", jsonString);
                request.setHeader("json", jsonString);
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    InputStream inputStream = entity.getContent();
                    result = inputStreamToString(inputStream);
                    Log.d("Read from server", result);
                }
            } catch (Exception ex) {

                Log.d("doInBackground", ex.getMessage());
            } finally {
                return result;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.d("onPostExecute", "Data was sent!");
        }
    }
}
