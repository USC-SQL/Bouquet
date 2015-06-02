package usc.edu;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import java.util.Hashtable;

/**
 * Created by dingli on 4/21/15.
 */
public class AgentClassConstants {
    public final static  String AgentClass="usc.edu.AgentURLConnection";
    public final static String getAllowUserInteraction="boolean getAllowUserInteraction(java.net.URLConnection)";
    public final static String getContent1="java.lang.Object getContent(java.net.URLConnection)";
    public final static String getContent2="java.lang.Object getContent(java.net.URLConnection, java.lang.Class[])";
    public final static String getContentEncoding="java.lang.String getContentEncoding(java.net.URLConnection)";
    public final static String getContentLength="int getContentLength(java.net.URLConnection)";
    public final static String getContentType="java.lang.String getContentType(java.net.URLConnection)";
    public final static String getDate="long getDate(java.net.URLConnection)";
    public final static String getDefaultUseCaches="boolean getDefaultUseCaches(java.net.URLConnection)";
    public final static String getDoInput="boolean getDoInput(java.net.URLConnection)";
    public final static String getDoOutput="boolean getDoOutput(java.net.URLConnection)";
    public final static String getExpiration="long getExpiration(java.net.URLConnection)";
    public final static String getHeaderField1="java.lang.String getHeaderField(java.net.URLConnection, int)";
    public final static String getHeaderFields="java.util.Map getHeaderFields(java.net.URLConnection)";
    public final static String getRequestProperties="java.util.Map getRequestProperties(java.net.URLConnection)";
    public final static String addRequestProperty="void addRequestProperty(java.net.URLConnection, java.lang.String, java.lang.String)";
    public final static String getHeaderField2="java.lang.String getHeaderField(java.net.URLConnection, java.lang.String)";
    public final static String getHeaderFieldDate="long getHeaderFieldDate(java.net.URLConnection, java.lang.String, long)";
    public final static String getHeaderFieldInt="int getHeaderFieldInt(java.net.URLConnection, java.lang.String, int)";
    public final static String getHeaderFieldKey="java.lang.String getHeaderFieldKey(java.net.URLConnection, int)";
    public final static String getIfModifiedSince="long getIfModifiedSince(java.net.URLConnection)";
    public final static String getInputStream="java.io.InputStream getInputStream(java.net.URLConnection)";
    public final static String getLastModified="long getLastModified(java.net.URLConnection)";
    public final static String getOutputStream="java.io.OutputStream getOutputStream(java.net.URLConnection)";
    public final static String getPermission="java.security.Permission getPermission(java.net.URLConnection)";
    public final static String getRequestProperty="java.lang.String getRequestProperty(java.net.URLConnection, java.lang.String)";
    public final static String getURL="java.net.URL getURL(java.net.URLConnection)";
    public final static String getUseCaches="boolean getUseCaches(java.net.URLConnection)";
    public final static String setAllowUserInteraction="void setAllowUserInteraction(java.net.URLConnection, boolean)";
    public final static String setDefaultUseCaches="void setDefaultUseCaches(java.net.URLConnection, boolean)";
    public final static String setDoInput="void setDoInput(java.net.URLConnection, boolean)";
    public final static String setDoOutput="void setDoOutput(java.net.URLConnection, boolean)";
    public final static String setIfModifiedSince="void setIfModifiedSince(java.net.URLConnection, long)";
    public final static String setRequestProperty="void setRequestProperty(java.net.URLConnection, java.lang.String, java.lang.String)";
    public final static String setUseCaches="void setUseCaches(java.net.URLConnection, boolean)";
    public final static String setConnectTimeout="void setConnectTimeout(java.net.URLConnection, int)";
    public final static String getConnectTimeout="int getConnectTimeout(java.net.URLConnection)";
    public final static String setReadTimeout="void setReadTimeout(java.net.URLConnection, int)";
    public final static String getReadTimeout="int getReadTimeout(java.net.URLConnection)";
    public final static String toString="java.lang.String toString(java.net.URLConnection)";
    public final static String connect="void connect(java.net.URLConnection)";
    //list of original signatures
    public final static String HTTPClientExecution="org.apache.http.HttpResponse HttpResponseexecute(org.apache.http.client.methods.HttpUriRequest,org.apache.http.client.HttpClient)";
    //list of signatures
    /*
    <java.net.URLConnection: boolean getAllowUserInteraction()>
    <java.net.URLConnection: java.lang.Object getContent()>
    <java.net.URLConnection: java.lang.Object getContent(java.lang.Class[])>
    <java.net.URLConnection: java.lang.String getContentEncoding()>
    <java.net.URLConnection: int getContentLength()>
    <java.net.URLConnection: java.lang.String getContentType()>
    <java.net.URLConnection: long getDate()>
    <java.net.URLConnection: boolean getDefaultUseCaches()>
    <java.net.URLConnection: boolean getDoInput()>
    <java.net.URLConnection: boolean getDoOutput()>
    <java.net.URLConnection: long getExpiration()>
    <java.net.URLConnection: java.lang.String getHeaderField(int)>
    <java.net.URLConnection: java.util.Map getHeaderFields()>
    <java.net.URLConnection: java.util.Map getRequestProperties()>
    <java.net.URLConnection: void addRequestProperty(java.lang.String,java.lang.String)>
    <java.net.URLConnection: java.lang.String getHeaderField(java.lang.String)>
    <java.net.URLConnection: long getHeaderFieldDate(java.lang.String,long)>
    <java.net.URLConnection: int getHeaderFieldInt(java.lang.String,int)>
    <java.net.URLConnection: java.lang.String getHeaderFieldKey(int)>
    <java.net.URLConnection: long getIfModifiedSince()>
    <java.net.URLConnection: java.io.InputStream getInputStream()>
    <java.net.URLConnection: long getLastModified()>
    <java.net.URLConnection: java.io.OutputStream getOutputStream()>
    <java.net.URLConnection: java.security.Permission getPermission()>
    <java.net.URLConnection: java.lang.String getRequestProperty(java.lang.String)>
    <java.net.URLConnection: java.net.URL getURL()>
    <java.net.URLConnection: boolean getUseCaches()>
    <java.net.URLConnection: void setAllowUserInteraction(boolean)>
    <java.net.URLConnection: void setDefaultUseCaches(boolean)>
    <java.net.URLConnection: void setDoInput(boolean)>
    <java.net.URLConnection: void setDoOutput(boolean)>
    <java.net.URLConnection: void setIfModifiedSince(long)>
    <java.net.URLConnection: void setRequestProperty(java.lang.String,java.lang.String)>
    <java.net.URLConnection: void setUseCaches(boolean)>
    <java.net.URLConnection: void setConnectTimeout(int)>
    <java.net.URLConnection: int getConnectTimeout()>
    <java.net.URLConnection: void setReadTimeout(int)>
    <java.net.URLConnection: int getReadTimeout()>
    <java.net.URLConnection: java.lang.String toString()>
    <java.net.URLConnection: void connect()>
    */
    final static private Hashtable<String,String > AgentMethodTable=new Hashtable<String,String >();
    static {
        String[][] pairs = {
                {"<java.net.URLConnection: java.io.InputStream getInputStream()>", getInputStream},
                {"<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>", HTTPClientExecution}
        };
        for (String[] pair : pairs) {
            AgentMethodTable.put(pair[0], pair[1]);
        }
    }
    public static String QueryAgentMethodForBCEL(String Sig)
    {
        if(!AgentMethodTable.containsKey(Sig))
        {
            return null;
        }
        String sub=AgentMethodTable.get(Sig);
        return sub;
    }
    public static SootMethod QueryAgentMethod(String Sig)
    {
        if(!AgentMethodTable.containsKey(Sig))
        {
            return null;
        }
        String sub=AgentMethodTable.get(Sig);
        SootClass Agentclass = Scene.v().loadClassAndSupport(AgentClassConstants.AgentClass);
        SootMethod sm=Agentclass.getMethod(sub);
        return sm;


    }


}