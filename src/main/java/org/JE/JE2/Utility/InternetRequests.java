package org.JE.JE2.Utility;

import org.JE.JE2.Annotations.Nullable;
import org.JE.JE2.IO.Logging.Errors.JE2Error;
import org.JE.JE2.IO.Logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

public class InternetRequests {
    public static void makeRequestAsync(String url, String requestMethod, @Nullable HashMap<String, String> queries, String contentType, RunnableGeneric<HashMap<String, Object>> callback) {
        Thread requestThread = new Thread(() -> {
            try {
                HashMap<String, Object> res = makeRequest(url, requestMethod, queries, contentType);
                callback.invoke(res);
            } catch (Exception e) {
                Logger.log(new JE2Error("INTERNET REQUEST ERROR FOR <" + url + "> VIA: " +requestMethod ,"An exception occurred: " + e.getMessage()));
            }
        });
        requestThread.start();
    }
    public static HashMap<String, Object> makeRequest(String url, String requestMethod, @Nullable HashMap<String,String> queries, String contentType) throws Exception{
        if(url == null)
            throw new Exception("URL cannot be null");
        url += getQueriesString(queries);
        URL url1 = new URL(url);
        Logger.log("Making internet request to <" + url + "> through method: " + requestMethod,0);
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

            connection.disconnect();
            // Print response
            return mapResult(response.toString());
        }
        else{
            Logger.log(new JE2Error("INTERNET REQUEST ERROR FOR <" + url + "> VIA: " +requestMethod ,"Response code not 200: " + responseCode));
        }
        // Close connection
        connection.disconnect();
        HashMap<String,Object> error = new HashMap<>();
        error.put("error", "Error: " + responseCode);
        return error;
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

    private static HashMap<String,Object> mapResult(String result){
        // Will be format {"key":"value", "key":"value", etc...}
        HashMap<String,Object> map = new HashMap<>();
        String[] pairs;
        // some results will have a comma in the string, we need to split by commas, but not the ones in the string
        if(result.contains("\"")){
            pairs = result.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        }
        else{
            pairs = result.split(",");
        }
        pairs[0] = pairs[0].substring(1);
        pairs[pairs.length-1] = pairs[pairs.length-1].substring(0,pairs[pairs.length-1].length()-1);
        for (String pair : pairs) {
            String[] kv;
            if(pair.contains("\"")){
                kv = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            }
            else{
                kv = pair.split(":");
            }

            map.put(kv[0], getBestFit(kv[1]));
        }
        return map;
    }

    private static Object getBestFit(String result){
        try{
            return Integer.parseInt(result);
        }catch (Exception e){
            try{
                return Double.parseDouble(result);
            }catch (Exception e1){
                return result;
            }
        }
    }
}
