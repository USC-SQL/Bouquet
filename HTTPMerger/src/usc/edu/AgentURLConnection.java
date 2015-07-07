package usc.edu;

import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.net.*;
import java.security.Permission;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by dingli on 4/22/15.
 */
public class AgentURLConnection {
    private static int counter = 0;
    //private static final String proxylink="http://ec2-52-11-252-130.us-west-2.compute.amazonaws.com:1989";
    //private static final String lookuplink="http://ec2-52-11-252-130.us-west-2.compute.amazonaws.com:1988";
    private static final String lookuplink="http://dingli.usc.edu:1988";
    private static final String proxylink="http://dingli.usc.edu:1989";

    private static HashMap<String,BundledResponse> reserved=new HashMap<String,BundledResponse>();
    private static HashMap<String,HttpResponse> HttpClientreserved=new HashMap<String,HttpResponse>();
    private static Object lock=new Object();

    private static DataOutputStream methodout=null;
    private static DataOutputStream HTTPout=null;
    private static PrintWriter HTTPpw=null;


    private static void LogMethodLong(long start,long end ){

            try {
                    OutputStream file = new FileOutputStream("/sdcard/CALL_log", true);
                    OutputStream buffer = new BufferedOutputStream(file);
                    methodout = new DataOutputStream(buffer);

                methodout.writeLong(start);
                methodout.writeLong(end);
                methodout.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private static void LogHTTPLong(long start,long end,String link ){
        try {
            OutputStream file = new FileOutputStream("/sdcard/HTTP_log", true);
            HTTPpw=new PrintWriter(file,true);
            HTTPpw.println(link);
            HTTPpw.println(start);
            HTTPpw.println(end);


            try{
                throw new Exception();
            }
            catch (Exception e)
            {

                StackTraceElement[] trace = e.getStackTrace();

                for(int ourCause = 0; ourCause < trace.length; ++ourCause) {
                    HTTPpw.println("\tat " + trace[ourCause]);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static long LogCallStart() {
        /*if (counter == 0) {
            Long currentmilli = System.currentTimeMillis();
            System.out.println("Block Starts: " + currentmilli+" ");
        }
        counter++;*/

        Long currentmilli = System.currentTimeMillis();
        return currentmilli;

    }

    public static void LogCallReturn(long starttime) {
        //counter--;
        Long currentmilli = System.currentTimeMillis();
        LogMethodLong(starttime,currentmilli);

        //System.out.println("Method Call: " + starttime+" "+currentmilli);
        /*if (counter == 0) {
            System.out.println("Block Ends: " + currentmilli);
        }*/

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
    private static List<BundledResponse> getBundledResponsesFromInputStream(byte[] content) throws IOException {
        long start=System.currentTimeMillis();
        //byte[] content=getbytesFromInputStream(is);
        long end1=System.currentTimeMillis();
        //System.out.println("In get 1 "+(end1-start));
        //System.out.println("System "+content.length);
        LinkedList<BundledResponse> responses=new  LinkedList<BundledResponse>();
        LinkedList<Integer> anchorpoints=new  LinkedList<Integer>();
        for(int i=0;i<content.length-3;i++)
        {
            if(content[i]=='@'&&content[i+1]=='@'&&content[i+2]=='@')
            {
                anchorpoints.add(i + 3);
            }
        }
        anchorpoints.add(content.length);
        long end2=System.currentTimeMillis();
        //System.out.println("In get 2 "+(end2-end1));
        int last=0;
        for(int s:anchorpoints)
        {
            //System.out.println("Anchor "+s);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            buffer.write(content,last,s-last-3);
            last=s;
            byte[] allbytes=buffer.toByteArray();
            //String temps= getStringFromInputStream(new ByteArrayInputStream(allbytes));
            //System.out.println(temps);
            BundledResponse br=new BundledResponse(allbytes);
            //System.out.println(br.link+" link");
            responses.add(br);
            //String value=getStringFromInputStream(new ByteArrayInputStream(buffer.toByteArray()));
           // System.out.println(value);
        }
        long end3=System.currentTimeMillis();
        //System.out.println("In get 3 "+(end3-end2));
        return responses;
    }

    private static byte[] getbytesFromInputStream(InputStream is) throws IOException {
        int nRead;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[1024];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();

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


    public static InputStream getInputStreamOPT(URLConnection urlconn) throws IOException {
        //System.out.println("URLgetInputStreamOPT It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());
        return getInputStreamOPT((HttpURLConnection)urlconn);
    }
    public static InputStream getInputStreamOPT(HttpURLConnection urlconn) throws IOException {
        // System.out.println("HTTPRULgetInputStreamOPT It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());

        /*try {
            throw new Exception();
        } catch (Exception e) {
            //StackTraceElement[] selem=e.getStackTrace();

            //System.out.println("AT " + selem[1]);
            e.printStackTrace(System.out);
        }*/
        if(!urlconn.getDoOutput())
        {

            long start=System.currentTimeMillis();
            InputStream r=null;
            if(reserved.containsKey(urlconn.getURL().toString()))
            {
                //System.out.println("Hit");
                BundledResponse br=reserved.get(urlconn.getURL().toString());
                r=new ByteArrayInputStream(br.buffer.toByteArray());

            }
            else{
                // System.out.println("Miss");

                URL url = new URL(proxylink);//to proxy
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("targetlink", urlconn.getURL().toString());
                InputStream in = urlConnection.getInputStream();
                long end1=System.currentTimeMillis();
                //System.out.println("end1 "+(end1-start)+" "+urlConnection.getContentLength());
                byte[] content=getbytesFromInputStream(in);
                long end2=System.currentTimeMillis();
                //System.out.println("end2 "+(end2-start));

                List<BundledResponse> res=getBundledResponsesFromInputStream(content);
                //System.out.println(res.size()+" size");
                long end21=System.currentTimeMillis();
                //System.out.println("end21 "+(end21-start));
                for(BundledResponse br:res)
                {
                    reserved.put(br.link,br);
                    //System.out.println(br.link);
                    if(br.link.equals(urlconn.getURL().toString()))
                    {
                        r=new ByteArrayInputStream(br.buffer.toByteArray());
                    }

                }
                long end3=System.currentTimeMillis();
                //System.out.println("end3 "+(end3-start));
                //System.out.println(reserved.keySet().size()+" Keys");

            }
            long end=System.currentTimeMillis();
            LogHTTPLong(start,end,urlconn.getURL().toString());
            // System.out.println("NetworkCost: "+(end-start));
            return r;




        }
        else{
            //POST not handled yes
            System.out.println("Big Warning: this is an HTTP Post");

            System.out.println("HTTP POST call start: " + System.currentTimeMillis());

            InputStream in = urlconn.getInputStream();
            System.out.println("HTTPcall end: " + System.currentTimeMillis());


            return in;
        }

    }
    /*public static InputStream getInputStream(URLConnection urlconn) throws IOException {
        // System.out.println("URLgetInputStream It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());
        long start=System.currentTimeMillis();
        InputStream r=urlconn.getInputStream();
        long end =System.currentTimeMillis();
        LogHTTPLong(start,end,urlconn.getURL().toString());

        return r;


    }
    public static InputStream getInputStream(HttpURLConnection urlconn) throws IOException {
        // System.out.println("URLgetInputStream It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());
        long start=System.currentTimeMillis();
        InputStream r=urlconn.getInputStream();
        long end =System.currentTimeMillis();
        LogHTTPLong(start,end,urlconn.getURL().toString());

        return r;


    }*/
    public static InputStream getInputStream(URLConnection urlconn) throws IOException {
       // System.out.println("URLgetInputStream It works! " + urlconn.getURL().toString() + " " + urlconn.getDoOutput() + " " + android.os.Process.myTid() + " " + Thread.currentThread().getId());
        return getInputStream((HttpURLConnection) urlconn);


    }

    public static InputStream getInputStream(HttpURLConnection urlconn) throws IOException {


        if(!urlconn.getDoOutput())
        {
            long start=System.currentTimeMillis();

            URL url = new URL(lookuplink);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty("targetlink", urlconn.getURL().toString());

            InputStream in = urlConnection.getInputStream();
            long end1=System.currentTimeMillis();
            //System.out.println("end1 "+(end1-start)+" "+in.available());
            byte[] content=getbytesFromInputStream(in);
            InputStream r=new ByteArrayInputStream(content);
            long end2=System.currentTimeMillis();
            //System.out.println("end2 "+(end2-end1));

            long end=System.currentTimeMillis();
            LogHTTPLong(start,end,urlconn.getURL().toString());

            //System.out.println("NetworkCost: "+(end-start));
            return r;


        }
        else{
            //POST not handled yes
            System.out.println("Big Warning: this is an HTTP Post");

            System.out.println("HTTP POST call start: " + System.currentTimeMillis());

            InputStream in = urlconn.getInputStream();
            System.out.println("HTTPcall end: " + System.currentTimeMillis());


            return in;
        }

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
    public static HttpResponse HttpResponseexecuteOPT(HttpClient client,HttpUriRequest request) throws IOException, URISyntaxException {

        //System.out.println("it works OPT " + request.getURI().toString() + " " + request.getMethod());

        if(request instanceof HttpPost)
        {
            long start=System.currentTimeMillis();

            HttpPost post=(HttpPost)request;
            HttpEntity en=post.getEntity();
            String query=request.getURI().toString();
            query+="?";
            if(en!=null)
            {
                String v=getStringFromInputStream(en.getContent());
                query+=v;
            }
            //System.out.println(query);
            if(reserved.containsKey(query))
            {
                //System.out.println("hit");
                HttpResponse r=HttpClientreserved.get(query);
                BundledResponse br =reserved.get(query);
                HttpEntity newentity=new ByteArrayEntity(br.buffer.toByteArray());
                r.setEntity(newentity);
                long end=System.currentTimeMillis();
                LogHTTPLong(start,end,query);
                return r;

            }
            URI oldurl=request.getURI();
           // System.out.println(oldurl.toString());
            //((HttpPost)request).setURI(new URI("http://ec2-52-24-51-136.us-west-2.compute.amazonaws.com:1988"));
            ((HttpPost)request).setURI(new URI(proxylink));
            request.addHeader("targetlink", oldurl.toString());
            request.setHeader("targetlink", oldurl.toString());

            HttpResponse r=client.execute(request);
            InputStream instream = r.getEntity().getContent();
            Header contentEncoding = r.getFirstHeader("content-encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                instream = new GZIPInputStream(instream);
            }
            byte[] content=getbytesFromInputStream(instream);
            List<BundledResponse> res=getBundledResponsesFromInputStream(content);
            for(BundledResponse br:res)
            {

                //System.out.println(br.link+" link");
                //System.out.println(query+" query");
                //System.out.println(br.link.equals(query));

                reserved.put(br.link,br);
                HttpClientreserved.put(br.link,r);
                if(br.link.equals(query))
                {
                    HttpEntity newentity=new ByteArrayEntity(br.buffer.toByteArray());
                    r.setEntity(newentity);

                }

            }
            //int mylength=getStringFromInputStream(r.getEntity().getContent()).length();
            long end=System.currentTimeMillis();
            LogHTTPLong(start,end,query);

            //System.out.println("NetworkCost: "+(end-start));

            return r;
        }
        else if(request instanceof HttpGet)
        {
            long start=System.currentTimeMillis();

            URI oldurl=request.getURI();
            //System.out.println(oldurl.toString()+" GET");
            //((HttpPost)request).setURI(new URI("http://ec2-52-24-51-136.us-west-2.compute.amazonaws.com:1988"));

            ((HttpGet)request).setURI(new URI(proxylink));
            request.addHeader("targetlink", oldurl.toString());
            request.setHeader("targetlink", oldurl.toString());
            //System.out.println("I am here 111");
            if(reserved.containsKey(oldurl.toString()))
            {
                //System.out.println("hit");
                HttpResponse r=HttpClientreserved.get(oldurl.toString());
                BundledResponse br =reserved.get(oldurl.toString());
                HttpEntity newentity=new ByteArrayEntity(br.buffer.toByteArray());
                r.setEntity(newentity);
                long end=System.currentTimeMillis();
                LogHTTPLong(start,end,oldurl.toString());
                return r;

            }
            HttpResponse r=client.execute(request);
            //System.out.println("I am here le    ");
            InputStream instream = r.getEntity().getContent();
            Header contentEncoding = r.getFirstHeader("content-encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                instream = new GZIPInputStream(instream);
            }
            byte[] content=getbytesFromInputStream(instream);
            String s=new String(content);
            //System.out.println(s);
            List<BundledResponse> res=getBundledResponsesFromInputStream(content);
            for(BundledResponse br:res)
            {
                //System.out.println("I am here "+ br.link);

                reserved.put(br.link,br);
                HttpClientreserved.put(br.link,r);
                if(br.link.equals(request.getURI().toString()))
                {
                    HttpEntity newentity=new ByteArrayEntity(br.buffer.toByteArray());
                    r.setEntity(newentity);

                }

            }
            //int mylength=getStringFromInputStream(r.getEntity().getContent()).length();
            long end=System.currentTimeMillis();
            LogHTTPLong(start,end,oldurl.toString());

            // System.out.println("NetworkCost: "+(end-start));

            return r;
        }

        HttpResponse r=client.execute(request);
        return r;
    }
    public static HttpResponse HttpResponseexecute(HttpClient client,HttpUriRequest request) throws IOException, URISyntaxException {

        System.out.println("it works " + request.getURI().toString() + " " + request.getMethod());

        if(request instanceof HttpPost)
        {
            long start=System.currentTimeMillis();

            HttpPost post=(HttpPost)request;
            HttpEntity en=post.getEntity();
            String query=request.getURI().toString();
            query+="?";
            if(en!=null)
            {
                String v=getStringFromInputStream(en.getContent());
                query+=v;
            }
            URI oldurl=request.getURI();
            //System.out.println(oldurl.toString());
            //((HttpPost)request).setURI(new URI("http://ec2-52-24-51-136.us-west-2.compute.amazonaws.com:1988"));
            HttpPost newpost=new HttpPost(lookuplink);
            newpost.addHeader("targetlink", oldurl.toString());
            Header[] hs=request.getAllHeaders();
            for(Header h:hs)
            {
                newpost.addHeader(h.getName(),h.getValue());

            }
            newpost.setEntity(((HttpPost) request).getEntity());
            /*for old version
            ((HttpPost)request).setURI(new URI(lookuplink));
            request.addHeader("targetlink", oldurl.toString());
            request.setHeader("targetlink", oldurl.toString());*/

            HttpResponse r=client.execute(newpost);
            String output=getStringFromInputStream(r.getEntity().getContent());
            int mylength=output.length();
            //int mylength=getStringFromInputStream(r.getEntity().getContent()).length();
            HttpEntity newentity=	new StringEntity(output);
            long end=System.currentTimeMillis();
            r.setEntity(newentity);
            LogHTTPLong(start,end,query);
            ((HttpPost) request).setURI(oldurl);
            //System.out.println("NetworkCost: "+(end-start));

            return r;
        }
        else if(request instanceof HttpGet)
        {
            long start=System.currentTimeMillis();

            URI oldurl=request.getURI();
            //System.out.println(oldurl.toString()+" GET");
            //((HttpPost)request).setURI(new URI("http://ec2-52-24-51-136.us-west-2.compute.amazonaws.com:1988"));

            ((HttpGet)request).setURI(new URI(lookuplink));
            request.addHeader("targetlink", oldurl.toString());
            request.setHeader("targetlink", oldurl.toString());
            //System.out.println("I am here ");

            HttpResponse r=client.execute(request);
            String output=getStringFromInputStream(r.getEntity().getContent());
            int mylength=output.length();
            //int mylength=getStringFromInputStream(r.getEntity().getContent()).length();
            HttpEntity newentity=	new StringEntity(output);
            long end=System.currentTimeMillis();
            r.setEntity(newentity);
            LogHTTPLong(start,end,oldurl.toString());

            //System.out.println("NetworkCost: "+(end-start));

            return r;
        }

        HttpResponse r=client.execute(request);
        return r;
    }
    /*public static HttpResponse HttpResponseexecute(HttpClient client, HttpUriRequest request) throws IOException, URISyntaxException {
        System.out.println("HTTP execute it works " + request.getURI().toString() + " " + request.getMethod());

        long start=System.currentTimeMillis();

        HttpResponse r = client.execute(request);
        long end=System.currentTimeMillis();
        LogHTTPLong(start, end, request.getURI().toString());

        return r;

    }*/

}
