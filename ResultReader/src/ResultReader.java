import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dingli on 6/18/15.
 */
public class ResultReader {
    public static boolean isHot(HTTPRequest r)
    {
        if(r.link.startsWith("http://api.worldweatheronline.com"))
            return true;
        if(r.link.startsWith("http://api.openweathermap.org"))
            return true;
        if(r.link.startsWith("http://lirr42.mta.info/index.php?FromStation"))
            return true;
        if(r.link.startsWith("http://lirr42.mta.info/schedules.php?FromStation=105"))
            return true;
        if(r.link.startsWith("https://s3.amazonaws.com/tapjoy/videos/assets/watermark.png"))
            return true;
        if(r.link.startsWith("http://mis-aljazeera.gos2m.nl/MisLogM2Auth"))
            return true;
        return false;

    }

    public static void printData(String timedata, String energydata, String HTTPdata, PrintWriter output, int cnt) throws IOException {
        BlockList bl=new BlockList();
        FileInputStream timefile;
        timefile = new FileInputStream(timedata);
        double total_energy=0.0;
        DataInputStream timeinput=new DataInputStream(timefile);
        int count=0;
        while(true)
        {
            try{
                long start=timeinput.readLong();
                long end=timeinput.readLong();
                if(end>=start)
                    bl.addMethod(new MethodData(start,end));
                count++;
            }
            catch(EOFException e)
            {
                //System.out.println(count);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EnergyDataBase edb=new EnergyDataBase(energydata);
        for(Block b:bl.blist)
        {
            total_energy+=edb.QueryLongEnergy(b.start,b.end);
        }
        FileInputStream fis = new FileInputStream(HTTPdata);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        List<String> HTTPlogs=new LinkedList<String>();
            while ((line = br.readLine()) != null) {
                HTTPlogs.add(line);

            }
        LinkedList<HTTPRequest> httplogs=new LinkedList<HTTPRequest>();
        HTTPRequest cur=new HTTPRequest();
        for(int i=0;i<HTTPlogs.size()-1;i++)
        {
            String current=HTTPlogs.get(i);

            if(current.startsWith("http"))
            {
                cur=new HTTPRequest();
                cur.link=current;

            }
            String next=HTTPlogs.get(i+1);
            String nextnext="http";
            if(i<HTTPlogs.size()-2)
            {
                nextnext=HTTPlogs.get(i+2);
            }
            if(current.startsWith("1")&&next.startsWith("1")&&!nextnext.startsWith("1"))
            {
                cur.start=Long.parseLong(current);
                cur.end=Long.parseLong(next);
                httplogs.add(cur);
            }
        }
        double http_energy=0;
        double shrs_energy=0;
        for(HTTPRequest http:httplogs)
        {
            http_energy+=edb.QueryLongEnergy(http.start,http.end);
            if(isHot(http))
            {
                System.out.println(http.link);
                shrs_energy+=edb.QueryLongEnergy(http.start,http.end);
            }

        }
        System.out.printf("%d %.0f %.0f %.0f\n", cnt,total_energy,http_energy,shrs_energy);
        output.printf("%d %.0f %.0f %.0f\n", cnt, total_energy, http_energy, shrs_energy);


    }
    public static void Refine(String path) throws IOException {
        PrintWriter pw=new PrintWriter("./energy.log");

        for(int i=1;i<16;i++)
        {
            String timedata=path+"/"+i+"/CALL_log";
            String energydata=path+"/"+i+"/Energy.txt";
            String HTTPdata=path+"/"+i+"/HTTP_log";

            try{
                printData(timedata,energydata,HTTPdata,pw,i);

            }
            catch (Exception e)
            {
                File dir=new File(path+"/"+i);
                //FileUtils.deleteDirectory(dir);

                e.printStackTrace();
            }
        }

        pw.close();
    }
    public static double ReadTotalEnergy(String timedata,String energydata) throws FileNotFoundException {
        BlockList bl=new BlockList();
        FileInputStream timefile;
        timefile = new FileInputStream(timedata);
        double total_energy=0.0;
        DataInputStream timeinput=new DataInputStream(timefile);
        int count=0;
        while(true)
        {
            try{
                long start=timeinput.readLong();
                long end=timeinput.readLong();
                if(end>=start)
                    bl.addMethod(new MethodData(start,end));
                count++;
            }
            catch(EOFException e)
            {
                //System.out.println(count);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EnergyDataBase edb=new EnergyDataBase(energydata);
        for(Block b:bl.blist)
        {
            total_energy+=edb.QueryLongEnergy(b.start,b.end);
        }
        return total_energy;
    }
    public static double ReadSHRSEnergy(String HTTPdata,String energydata) throws IOException {
        FileInputStream fis = new FileInputStream(HTTPdata);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        List<String> HTTPlogs=new LinkedList<String>();
        while ((line = br.readLine()) != null) {
            HTTPlogs.add(line);

        }
        LinkedList<HTTPRequest> httplogs=new LinkedList<HTTPRequest>();
        HTTPRequest cur=new HTTPRequest();
        for(int i=0;i<HTTPlogs.size()-1;i++)
        {
            String current=HTTPlogs.get(i);

            if(current.startsWith("http"))
            {
                cur=new HTTPRequest();
                cur.link=current;

            }
            String next=HTTPlogs.get(i+1);
            String nextnext="http";
            if(i<HTTPlogs.size()-2)
            {
                nextnext=HTTPlogs.get(i+2);
            }
            if(current.startsWith("1")&&next.startsWith("1")&&!nextnext.startsWith("1"))
            {
                cur.start=Long.parseLong(current);
                cur.end=Long.parseLong(next);
                httplogs.add(cur);
            }
        }
        double http_energy=0;
        double shrs_energy=0;
        EnergyDataBase edb=new EnergyDataBase(energydata);

        for(HTTPRequest http:httplogs)
        {
            http_energy+=edb.QueryLongEnergy(http.start,http.end);
            if(isHot(http))
            {
                shrs_energy+=edb.QueryLongEnergy(http.start,http.end);
            }

        }
        return shrs_energy;
    }
    public static double getMean(double[] data)
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/data.length;
    }
    public static double getSTD(double[] data)
    {
        double mean = getMean(data);
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        double v= temp/data.length;
        return Math.sqrt(v)/mean;

    }
    public static void SavingCalculator(String Resultpath,String network,String appname) throws IOException {
        double[] totalunopt=new double[5];
        double[] totalopt=new double[5];
        double[] SHRSunopt=new double[5];
        double[] SHRSopt=new double[5];
        for(int i=1;i<6;i++)
        {
            String unoptpath=Resultpath+"/unopt/"+network+"/"+appname+"/"+i;
            String optpath=Resultpath+"/opt/"+network+"/"+appname+"/"+i;
            totalunopt[i-1]=ReadTotalEnergy(unoptpath+"/CALL_log",unoptpath+"/Energy.txt");
            totalopt[i-1]=ReadTotalEnergy(optpath+"/CALL_log",optpath+"/Energy.txt");
            SHRSunopt[i-1]=ReadSHRSEnergy(unoptpath+"/HTTP_log",unoptpath+"/Energy.txt");
            SHRSopt[i-1]=ReadSHRSEnergy(optpath+"/HTTP_log",optpath+"/Energy.txt");

        }

        double totalsaving=(getMean(totalunopt)-getMean(totalopt))/getMean(totalunopt);
        double SHRSsaving=(getMean(SHRSunopt)-getMean(SHRSopt))/getMean(SHRSunopt);

        System.out.println(appname+" "+totalsaving+" "+SHRSsaving+" "+getSTD(totalunopt)+" "+getSTD(totalopt)+" "+getSTD(SHRSunopt)+" "+getSTD(SHRSopt));
    }

    public static void main(String argv[]) throws IOException {
        String app1="com.bobcares.BobsWeather.instrumented.apk";
        String app2="com.salman.lirrschedule.instrumented.apk";
        String app3="com.tapjoy.mytapjoy.instrumented.apk";
        String app4="net.aljazeera.tablet.english.instrumented.apk";
        String app5="pch.apps.pchsweeps.instrumented.apk";
        String wholepath="/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result/unopt/WIFI/";
        //Refine(wholepath+app3);
        System.out.println("Appname total_saving shrs_saving std_total_unopt std_total_opt std_shrs_unopt std_shrs_opt");

        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","LTE",app1);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","LTE",app2);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","LTE",app3);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","LTE",app4);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","LTE",app5);
        System.out.println();

        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result", "WIFI", app1);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","WIFI",app2);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","WIFI",app3);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","WIFI",app4);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","WIFI",app5);

        System.out.println();

        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","3G",app1);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","3G",app2);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","3G",app3);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","3G",app4);
        SavingCalculator("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/Reran/Result","3G",app5);



    }

}
