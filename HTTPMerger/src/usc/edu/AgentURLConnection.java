package usc.edu;

import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.net.*;
import java.security.Permission;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by dingli on 4/22/15.
 */
public class AgentURLConnection {
    private static int counter = 0;

    public static void LogCallStart() {
        if (counter == 0) {
            Long currentmilli = System.currentTimeMillis();
            System.out.println("Block Starts: " + currentmilli);
        }
        counter++;
        System.out.println("Start counter: " + counter);


    }

    public static void LogCallReturn() {
        counter--;
        Long currentmilli = System.currentTimeMillis();

        System.out.println("End counter: " + counter + " " + currentmilli);
        if (counter == 0) {
            System.out.println("Block Ends: " + currentmilli);
        }

    }

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

    public static Hashtable<String, String> contentcache = new Hashtable<String, String>();

    public static boolean getAllowUserInteraction(URLConnection urlconn) {
        return urlconn.getAllowUserInteraction();
    }

    public static Object getContent(URLConnection urlconn) throws IOException {
        return urlconn.getContent();
    }

    public static Object getContent(URLConnection urlconn, Class[] types) throws IOException {
        return urlconn.getContent(types);
    }

    public static String getContentEncoding(URLConnection urlconn) {
        return urlconn.getContentEncoding();
    }


    public static int getContentLength(URLConnection urlconn) {
        return urlconn.getContentLength();
    }

    public static String getContentType(URLConnection urlconn) {
        return urlconn.getContentType();
    }

    public static long getDate(URLConnection urlconn) {
        return urlconn.getDate();
    }

    public static boolean getDefaultUseCaches(URLConnection urlconn) {
        return urlconn.getDefaultUseCaches();
    }


    public static boolean getDoInput(URLConnection urlconn) {
        return urlconn.getDoInput();
    }


    public static boolean getDoOutput(URLConnection urlconn) {
        return urlconn.getDoOutput();
    }


    public static long getExpiration(URLConnection urlconn) {
        return urlconn.getExpiration();
    }


    public static String getHeaderField(URLConnection urlconn, int pos) {
        return urlconn.getHeaderField(pos);
    }


    public static Map<String, List<String>> getHeaderFields(URLConnection urlconn) {
        return urlconn.getHeaderFields();
    }


    public static Map<String, List<String>> getRequestProperties(URLConnection urlconn) {
        return urlconn.getRequestProperties();
    }


    public static void addRequestProperty(URLConnection urlconn, String field, String newValue) {
        urlconn.addRequestProperty(field, newValue);
    }


    public static String getHeaderField(URLConnection urlconn, String key) {
        return urlconn.getHeaderField(key);
    }


    public static long getHeaderFieldDate(URLConnection urlconn, String field, long defaultValue) {
        return urlconn.getHeaderFieldDate(field, defaultValue);
    }


    public static int getHeaderFieldInt(URLConnection urlconn, String field, int defaultValue) {
        return urlconn.getHeaderFieldInt(field, defaultValue);
    }


    public static String getHeaderFieldKey(URLConnection urlconn, int posn) {
        return urlconn.getHeaderFieldKey(posn);
    }


    public static long getIfModifiedSince(URLConnection urlconn) {
        return urlconn.getIfModifiedSince();
    }


    /*public static InputStream getInputStream(URLConnection urlconn) throws IOException {
        System.out.println("It works!");
        if(urlconn instanceof HttpURLConnection)
        {
            long start=System.currentTimeMillis();

            HttpURLConnection httpconn=(HttpURLConnection)urlconn;
            String targeturl=httpconn.getURL().toString();
            if(contentcache.containsKey(targeturl))
            {
                String body=contentcache.get(targeturl);
                InputStream returnstream=new ByteArrayInputStream(body.getBytes());
                long end=System.currentTimeMillis();
                System.out.println((end-start));
                return returnstream;
            }

            URL url = new URL("http://dingli.usc.edu:1988?targetlink="+ URLEncoder.encode(targeturl));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //String s=getStringFromInputStream(in);
                //System.out.println(getStringFromInputStream(in));
                //System.out.println(getStringFromInputStream(urlconn.getInputStream()));
                //InputStream stream = new ByteArrayInputStream(s.getBytes());
                byte[] buf=new byte[4096];
                InputStream in=urlConnection.getInputStream();
                String s=getStringFromInputStream(in);
                //System.out.println(s);
                String content[]=s.split("!!!");
                //System.out.println(content.length);

                for(int i=0;i<content.length;i++)
                {
                    String values[]=content[i].split("@@@");
                    contentcache.put(values[0], values[1]);
                    //System.out.println(values[1]);


                }
                String body=contentcache.get(targeturl);
                InputStream returnstream=new ByteArrayInputStream(body.getBytes());
                long end=System.currentTimeMillis();
                System.out.println((end-start));

                return returnstream;


            }
            finally {
                urlConnection.disconnect();
            }
        }


        return urlconn.getInputStream();
    }*/
    //for ground truth
    /*
    public static InputStream getInputStream(URLConnection urlconn) throws IOException {
        System.out.println("It works! "+urlconn.getURL().toString()+" "+urlconn.getDoOutput());
        long start=System.currentTimeMillis();
        Log.d("myapp", Log.getStackTraceString(new Exception()));

        InputStream in=urlconn.getInputStream();
        String s=getStringFromInputStream(in);
        InputStream returnstream=new ByteArrayInputStream(s.getBytes());
        long end=System.currentTimeMillis();
        String content[]=s.split("<");
        contentcache.put(content[0], content[1]);

        System.out.println((end-start));

        return returnstream;
    }*/
    public static InputStream getInputStream(URLConnection urlconn) throws IOException {
        System.out.println("URLgetInputStream It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());

        /*URL url = new URL("http://dingli.usc.edu:1988");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.addRequestProperty("targetlink", urlconn.getURL().toString());
        urlConnection.getInputStream();*/

        try {
            throw new Exception();
        } catch (Exception e) {
            //StackTraceElement[] selem=e.getStackTrace();
            e.printStackTrace(System.out);
            //System.out.println("AT " + selem[1]);
            //e.printStackTrace();
        }
        System.out.println("HTTPcall start: " + System.currentTimeMillis());

        InputStream in = urlconn.getInputStream();
        System.out.println("HTTPcall end: " + System.currentTimeMillis());


        return in;
    }

    public static InputStream getInputStream(HttpURLConnection urlconn) throws IOException {
        System.out.println("HTTPRULgetInputStream It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());

        try {
            throw new Exception();
        } catch (Exception e) {
            //StackTraceElement[] selem=e.getStackTrace();

            //System.out.println("AT " + selem[1]);
            e.printStackTrace(System.out);
        }
        /*URL url = new URL("http://dingli.usc.edu:1988");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        for(String key:urlconn.getHeaderFields().keySet())
        {
            urlConnection.addRequestProperty(key,urlconn.getHeaderFields().get(key).get(0));
        }
        urlConnection.getInputStream();
        System.out.println(urlconn.getHeaderFields());*/
        System.out.println("HTTPcall start: " + System.currentTimeMillis());
        InputStream in = urlconn.getInputStream();
        System.out.println("HTTPcall end: " + System.currentTimeMillis());


        return in;
    }

    public static long getLastModified(URLConnection urlconn) {
        return urlconn.getLastModified();
    }


    public static OutputStream getOutputStream(URLConnection urlconn) throws IOException {
        return urlconn.getOutputStream();
    }


    public static Permission getPermission(URLConnection urlconn) throws IOException {
        return urlconn.getPermission();
    }


    public static String getRequestProperty(URLConnection urlconn, String field) {
        return urlconn.getRequestProperty(field);
    }


    public static URL getURL(URLConnection urlconn) {
        return urlconn.getURL();
    }


    public static boolean getUseCaches(URLConnection urlconn) {
        return urlconn.getUseCaches();
    }


    public static void setAllowUserInteraction(URLConnection urlconn, boolean newValue) {
        urlconn.setAllowUserInteraction(newValue);
    }


    public static void setDefaultUseCaches(URLConnection urlconn, boolean newValue) {
        urlconn.setDefaultUseCaches(newValue);
    }


    public static void setDoInput(URLConnection urlconn, boolean newValue) {
        urlconn.setDoInput(newValue);
    }


    public static void setDoOutput(URLConnection urlconn, boolean newValue) {
        urlconn.setDoOutput(newValue);
    }


    public static void setIfModifiedSince(URLConnection urlconn, long newValue) {
        urlconn.setIfModifiedSince(newValue);
    }


    public static void setRequestProperty(URLConnection urlconn, String field, String newValue) {
        urlconn.setRequestProperty(field, newValue);
    }


    public static void setUseCaches(URLConnection urlconn, boolean newValue) {
        urlconn.setUseCaches(newValue);
    }


    public static void setConnectTimeout(URLConnection urlconn, int timeoutMillis) {
        urlconn.setConnectTimeout(timeoutMillis);
    }


    public static int getConnectTimeout(URLConnection urlconn) {
        return urlconn.getConnectTimeout();
    }


    public static void setReadTimeout(URLConnection urlconn, int timeoutMillis) {
        urlconn.setReadTimeout(timeoutMillis);
    }


    public static int getReadTimeout(URLConnection urlconn) {
        return urlconn.getReadTimeout();
    }


    public static String toString(URLConnection urlconn) {
        return urlconn.toString();
    }


    public static void connect(URLConnection urlconn) throws IOException {
        urlconn.connect();
    }

    //Below are APIs for apache HTTP client
    /*
    public static HttpResponse HttpResponseexecute(HttpUriRequest request,HttpClient client) throws IOException, URISyntaxException {

        System.out.println("it works " + request.getURI().toString() + " " + request.getMethod());

        if(request instanceof HttpPost)
        {
            URI oldurl=request.getURI();
            System.out.println(oldurl.toString());
            ((HttpPost)request).setURI(new URI("http://ec2-52-24-51-136.us-west-2.compute.amazonaws.com:1988"));
            request.addHeader("targetlink", oldurl.toString());
            request.setHeader("targetlink", oldurl.toString());

            request.addHeader("targetmethod", request.getMethod());
            long start=System.currentTimeMillis();
            HttpResponse r=client.execute(request);
            String output=getStringFromInputStream(r.getEntity().getContent());
            int mylength=output.length();
            //int mylength=getStringFromInputStream(r.getEntity().getContent()).length();
            HttpEntity newentity=	new StringEntity(output);
            long end=System.currentTimeMillis();
            r.setEntity(newentity);
            System.out.println(mylength+" "+(end-start));

            return r;
        }

        HttpResponse r=client.execute(request);
        return r;
    }*/
    public static HttpResponse HttpResponseexecute(HttpClient client, HttpUriRequest request) throws IOException, URISyntaxException {
        System.out.println("HTTP execute it works " + request.getURI().toString() + " " + request.getMethod());

        try {
            throw new Exception();
        } catch (Exception e) {
            //StackTraceElement[] selem=e.getStackTrace();

            // System.out.println("AT "+selem[1]);
            e.printStackTrace(System.out);
        }
        System.out.println("HTTPcall start: " + System.currentTimeMillis());

        HttpResponse r = client.execute(request);
        System.out.println("HTTPcall end: " + System.currentTimeMillis());
        return r;

    }

}
