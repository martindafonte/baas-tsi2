package com.google.android.gcm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;



public class AndroidAPI {
	
    static final String SERVER_URL = "http://192.168.0.107:8080/WebUserManager";
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    

    static final String SENDER_ID = "920697992611";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "Android SDK";


		 public static String registrarseCanal(int appId, int canalId, Context context) {
			 String regid = "";
			 GoogleCloudMessaging gcm;
             try {
                 gcm = GoogleCloudMessaging.getInstance(context);
                 regid = gcm.register(SENDER_ID);

                 // Send the regId to the Baas
                 sendRegistrationIdToBackend(appId, canalId, regid);
                 
             } catch (IOException ex) {
                 
             }
             return regid;
			 	
		    }
		 
		 public static Boolean desRegistrarseCanal(int appId, int canalId, Context context, String regid) {
			 GoogleCloudMessaging gcm;
             try {
                 gcm = GoogleCloudMessaging.getInstance(context);
                 gcm.unregister();

                 // Deletes the regId in the Baas
                 deleteRegistrationIdToBackend(appId, canalId, regid);

             } catch (IOException ex) {
            	 return false;
             }
             return true;
		    }
	 
	    
	    /**
	     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
	     * messages to your app. Not needed for this demo since the device sends upstream messages
	     * to a server that echoes back the message using the 'from' address in the message.
	     */
	    private static Boolean sendRegistrationIdToBackend(int appId, int canalId, String regId) {
	    	Log.i(TAG, "registering device (regId = " + regId + ")");
	        String serverUrl = SERVER_URL+ "/register";
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("appId", "1");
	        params.put("canId", "1");
	        params.put("regId", regId);
	        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
	        // Once GCM returns a registration id, we need to register it in the
	        // demo server. As the server might be down, we will retry it a couple
	        // times.
	        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
	            Log.d(TAG, "Attempt #" + i + " to register");
	            try {
	                post(serverUrl, params);
	                return true;
	            } catch (IOException e) {
	                // Here we are simplifying and retrying on any error; in a real
	                // application, it should retry only on unrecoverable errors
	                // (like HTTP error code 503).
	                Log.e(TAG, "Failed to register on attempt " + i, e);
	                if (i == MAX_ATTEMPTS) {
	                    break;
	                }
	                try {
	                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
	                    Thread.sleep(backoff);
	                } catch (InterruptedException e1) {
	                    // Activity finished before we complete - exit.
	                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
	                    Thread.currentThread().interrupt();
	                    return false;
	                }
	                // increase backoff exponentially
	                backoff *= 2;
	            }
	        }
	        return false;
	    }
	    
	    private static void deleteRegistrationIdToBackend(int appId, int canalId, String regId) {
	    	Log.i(TAG, "unregistering device (regId = " + regId + ")");
	        String serverUrl = SERVER_URL + "/unregister";
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("appId", "1");
	        params.put("canId", "1");
	        params.put("regId", regId);
	        try {
	            post(serverUrl, params);
	        } catch (IOException e) {}
	      }
	    
	    /**
	     * Issue a POST request to the server.
	     *
	     * @param endpoint POST address.
	     * @param params request parameters.
	     *
	     * @throws IOException propagated from POST.
	     */
	    private static void post(String endpoint, Map<String, String> params)
	            throws IOException {
	        URL url;
	        try {
	            url = new URL(endpoint);
	        } catch (MalformedURLException e) {
	            throw new IllegalArgumentException("invalid url: " + endpoint);
	        }
	        StringBuilder bodyBuilder = new StringBuilder();
	        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	        // constructs the POST body using the parameters
	        while (iterator.hasNext()) {
	            Entry<String, String> param = iterator.next();
	            bodyBuilder.append(param.getKey()).append('=')
	                    .append(param.getValue());
	            if (iterator.hasNext()) {
	                bodyBuilder.append('&');
	            }
	        }
	        String body = bodyBuilder.toString();
	        Log.v(TAG, "Posting '" + body + "' to " + url);
	        byte[] bytes = body.getBytes();
	        HttpURLConnection conn = null;
	        try {
	            conn = (HttpURLConnection) url.openConnection();
	            conn.setDoOutput(true);
	            conn.setUseCaches(false);
	            conn.setFixedLengthStreamingMode(bytes.length);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Content-Type",
	                    "application/x-www-form-urlencoded;charset=UTF-8");
	            // post the request
	            OutputStream out = conn.getOutputStream();
	            out.write(bytes);
	            out.close();
	            // handle the response
	            int status = conn.getResponseCode();
	            if (status != 200) {
	              throw new IOException("Post failed with error code " + status);
	            }
	        } finally {
	            if (conn != null) {
	                conn.disconnect();
	            }
	        }
	      }
}
