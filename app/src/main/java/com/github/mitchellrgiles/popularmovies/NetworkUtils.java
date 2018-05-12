package com.github.mitchellrgiles.popularmovies;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static String getResponseFromHttpsUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                Log.d("!!!!TEST!!!", "getResponseFromHttpsUrl: response is null");
                return null;
            }
        } catch (IOException e) {
            Log.d("!!!!TEST!!!", "getResponseFromHttpsUrl: " + e.toString());
            return null;
        }finally {
            urlConnection.disconnect();
        }

    }
}
