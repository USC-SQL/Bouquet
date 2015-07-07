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
            URL url0 = new URL("http://www.baidu.com");

            urlConnection = (HttpURLConnection) url0.openConnection();
            long start=System.currentTimeMillis();
            urlConnection.getInputStream();
            long end1=System.currentTimeMillis();
            System.out.println((end1-start));
            urlConnection.getInputStream().close();
            long end2=System.currentTimeMillis();
            System.out.println((end2-end1));


            /*for(int i=0;i<5;i++)
            {
                URL url = new URL("http://www.google.com");

                urlConnection = (HttpURLConnection) url.openConnection();
                 s=getStringFromInputStream(urlConnection.getInputStream());
                System.out.println(s);
            }*/


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
