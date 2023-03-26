package org.JE.JE2.Utility;

import org.JE.JE2.Annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class InternetRequests {
    public static void makeRequestAsync(String url, String requestMethod, @Nullable HashMap<String, String> queries, String contentType, RunnableArg callback) {
        Thread requestThread = new Thread(() -> {
            try {
                String res = makeRequest(url, requestMethod, queries, contentType);
                callback.run(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        requestThread.start();
    }
    public static String makeRequest(String url, String requestMethod, @Nullable HashMap<String,String> queries, String contentType) throws Exception{
        url += getQueriesString(queries);
        URL url1 = new URL(url);
        System.out.println(url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestMethod(requestMethod);

        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", contentType);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.connect();

        int responseCode = connection.getResponseCode();

        if(responseCode == 200){
            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print response
            return response.toString();
        }
        else{
            System.out.println("Error: " + responseCode);
        }
        // Close connection
        connection.disconnect();
        return "url error for " + url;
    }

    private static String getQueriesString(HashMap<String, String> queries) throws Exception {
        if(queries == null)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        int i = 0;
        for (HashMap.Entry<String, String> kv : queries.entrySet()) {
            sb.append(URLEncoder.encode(kv.getKey(), "UTF-8"));
            sb.append("=");

            if (i == queries.size() - 1) {
                sb.append(URLEncoder.encode(kv.getValue(), "UTF-8"));
            } else {
                sb.append(URLEncoder.encode(kv.getValue(), "UTF-8"));
                sb.append("&");
            }
            i++;
        }

       return sb.toString();
    }
}
