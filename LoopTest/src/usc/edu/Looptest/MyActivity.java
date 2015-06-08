package usc.edu.Looptest;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection = null;
        try {
            for(int i=0;i<5;i++)
            {
                URL url = new URL("http://dingli.usc.edu:1988?id="+i);

                urlConnection = (HttpURLConnection) url.openConnection();
                String s=getStringFromInputStream(urlConnection.getInputStream());
                System.out.println(s);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
