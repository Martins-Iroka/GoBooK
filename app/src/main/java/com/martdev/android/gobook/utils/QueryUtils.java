package com.martdev.android.gobook.utils;

import android.text.TextUtils;
import android.util.Log;

import com.martdev.android.gobook.model.GoBooK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<GoBooK> getGoBooKData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractDataFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving google book json results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<GoBooK> extractDataFromJson(String goBookJSON) {
        if (TextUtils.isEmpty(goBookJSON)) {
            return null;
        }

        List<GoBooK> goBooKS = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(goBookJSON);
            JSONArray itemArray = object.optJSONArray("items");

            for (int i = 0; i < itemArray.length(); i++) {

                JSONObject object1 = itemArray.getJSONObject(i);

                JSONObject volumeObj = object1.getJSONObject("volumeInfo");

                String title = volumeObj.getString("title");

                JSONArray authorArray = volumeObj.optJSONArray("authors");

                String author = "";

                if (!(authorArray == null)) {
                    for (int j = 0; j < authorArray.length(); j++)
                        author = authorArray.getString(j);
                }

                JSONObject imageObj = volumeObj.getJSONObject("imageLinks");
                String imageUrl = imageObj.getString("smallThumbnail");

                String infoLink = volumeObj.getString("infoLink");

                String publishedDate = volumeObj.optString("publishedDate");

                GoBooK goBooK = new GoBooK(title, author, publishedDate, infoLink, imageUrl);

                goBooKS.add(goBooK);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return goBooKS;
    }
}
