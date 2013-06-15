package com.sumioturk.satomi.service;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * General purpose Asynchronous HTTP task.
 */
public class HttpAsyncTask extends AsyncTask<Void, Integer, String> {

    protected URL url;

    protected String method;

    protected HttpAsyncTaskCallback callback;

    protected List<Pair<String, String>> params;

    /**
     * Callback
     */
    public interface HttpAsyncTaskCallback {

        void onSuccess(String result);

        void onFailure(Exception e);

    }


    public HttpAsyncTask(URL url, String method, List<Pair<String, String>> params, HttpAsyncTaskCallback callback) {
        this.url = url;
        this.method = method;
        this.callback = callback;
        this.params = params;
    }


    @Override
    protected String doInBackground(Void... voids) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            for(int i = 0; i < params.size(); i++){
                Pair<String, String> param = params.get(i);
                conn.setRequestProperty(param.first, param.second);
            }
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while((line = reader.readLine()) != null){
                result += line;
            }
            return result;
        } catch (Exception e){
            callback.onFailure(e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onSuccess(s);
    }
}
