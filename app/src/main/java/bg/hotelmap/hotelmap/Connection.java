package bg.hotelmap.hotelmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Teo on 19-Oct-16.
 */

public class Connection extends AsyncTask<String, Void, JSONObject> {

    private Context context;
    private OnTaskCompleted delegate;
    private ProgressDialog dialog;
    private String host;
    private HttpURLConnection urlConnection;

    public interface OnTaskCompleted {
        void processFinish(JSONObject jSONObject);
    }

    public Connection(Context context) {
        this.urlConnection = null;
        String address = "geomon.cloudapp.net";
        this.host = "http://" + address + "/Service1.svc/";
        this.delegate = null;
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setMessage("Loading, please wait...");
        this.dialog.show();
    }

    protected JSONObject doInBackground(String... params) {
        try {
            this.urlConnection = (HttpURLConnection) new URL(this.host + params[0]).openConnection();
            this.urlConnection.connect();
            String result = convertStreamToString(this.urlConnection.getInputStream());
            if (!isJSONInvalid(result)) {
                return new JSONObject(result);
            }
            try {
                JSONObject object = new JSONObject();
                object.put("value", result);
                return object;
            } catch (JSONException e) {
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    private boolean isJSONInvalid(String test) {
        try {
            JSONObject jSONObject = new JSONObject(test);
            return false;
        } catch (JSONException e) {
            return true;
        }
    }

    protected void onPostExecute(JSONObject jsonObject) {
        this.dialog.dismiss();
        this.delegate.processFinish(jsonObject);
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
