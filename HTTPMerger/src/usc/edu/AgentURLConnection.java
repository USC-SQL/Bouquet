package usc.edu;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
/**
 * Created by dingli on 4/22/15.
 */
public class AgentURLConnection {
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
    public static Hashtable<String,String> contentcache=new Hashtable<String,String>();
    public static boolean getAllowUserInteraction(URLConnection urlconn) {
        return urlconn.getAllowUserInteraction();
    }

    public static Object getContent(URLConnection urlconn) throws IOException {
        return urlconn.getContent();
    }

    public static Object getContent(URLConnection urlconn,Class[] types) throws IOException {
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


    public static String getHeaderField(URLConnection urlconn,int pos) {
        return urlconn.getHeaderField(pos);
    }


    public static Map<String, List<String>> getHeaderFields(URLConnection urlconn) {
        return urlconn.getHeaderFields();
    }


    public static Map<String, List<String>> getRequestProperties(URLConnection urlconn) {
        return urlconn.getRequestProperties();
    }


    public static void addRequestProperty(URLConnection urlconn,String field, String newValue) {
        urlconn.addRequestProperty(field, newValue);
    }


    public static String getHeaderField(URLConnection urlconn,String key) {
        return urlconn.getHeaderField(key);
    }


    public static long getHeaderFieldDate(URLConnection urlconn,String field, long defaultValue) {
        return urlconn.getHeaderFieldDate(field, defaultValue);
    }


    public static int getHeaderFieldInt(URLConnection urlconn,String field, int defaultValue) {
        return urlconn.getHeaderFieldInt(field, defaultValue);
    }


    public static String getHeaderFieldKey(URLConnection urlconn,int posn) {
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

    public static InputStream getInputStream(URLConnection urlconn) throws IOException {
        System.out.println("It works! "+urlconn.getURL().toString()+" "+urlconn.getDoOutput());
        long start=System.currentTimeMillis();

        InputStream in=urlconn.getInputStream();
        String s=getStringFromInputStream(in);
        InputStream returnstream=new ByteArrayInputStream(s.getBytes());
        long end=System.currentTimeMillis();
        String content[]=s.split("<");
        contentcache.put(content[0], content[1]);

        System.out.println((end-start));

        return returnstream;
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


    public static String getRequestProperty(URLConnection urlconn,String field) {
        return urlconn.getRequestProperty(field);
    }


    public static URL getURL(URLConnection urlconn) {
        return urlconn.getURL();
    }


    public static boolean getUseCaches(URLConnection urlconn) {
        return urlconn.getUseCaches();
    }


    public static void setAllowUserInteraction(URLConnection urlconn,boolean newValue) {
        urlconn.setAllowUserInteraction(newValue);
    }


    public static void setDefaultUseCaches(URLConnection urlconn,boolean newValue) {
        urlconn.setDefaultUseCaches(newValue);
    }


    public static void setDoInput(URLConnection urlconn,boolean newValue) {
        urlconn.setDoInput(newValue);
    }


    public static void setDoOutput(URLConnection urlconn,boolean newValue) {
        urlconn.setDoOutput(newValue);
    }


    public static void setIfModifiedSince(URLConnection urlconn,long newValue) {
        urlconn.setIfModifiedSince(newValue);
    }


    public static void setRequestProperty(URLConnection urlconn,String field, String newValue) {
        urlconn.setRequestProperty(field, newValue);
    }


    public static void setUseCaches(URLConnection urlconn,boolean newValue) {
        urlconn.setUseCaches(newValue);
    }


    public static void setConnectTimeout(URLConnection urlconn,int timeoutMillis) {
        urlconn.setConnectTimeout(timeoutMillis);
    }


    public static int getConnectTimeout(URLConnection urlconn) {
        return urlconn.getConnectTimeout();
    }


    public static void setReadTimeout(URLConnection urlconn,int timeoutMillis) {
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
}
