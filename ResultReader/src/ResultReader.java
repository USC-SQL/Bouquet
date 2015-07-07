import java.io.*;

/**
 * Created by dingli on 6/18/15.
 */
public class ResultReader {
    public static void main(String argv[]) throws IOException {
        if(argv.length!=1 )
        {
            System.out.println("Usage: DataReader.jar time_log energy_database");
            System.exit(0);
        }
       // EnergyDataBase edb=new EnergyDataBase(argv[1]);
        BlockList bl=new BlockList();
        FileInputStream timefile;
        timefile = new FileInputStream(argv[0]);
        double energy=0.0;
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
            }

        }
       /* for(Block b:bl.blist)
        {
            energy+=edb.QueryLongEnergy(b.start,b.end);
        }*/
        System.out.println(bl.nonIdleTime());
        System.out.println(energy);

        System.out.println(count);
    }
        /*
        BufferedReader br = new BufferedReader(new FileReader(logcatfile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp=line.split(": ")[2].split(" ");
                long start=Long.parseLong(tmp[0]);
                long end=Long.parseLong(tmp[1]);
                System.out.println(end-start);
                if(end>=start)
                    bl.addMethod(new MethodData(start,end));
            }
        System.out.println(bl.nonIdleTime());*/

}
