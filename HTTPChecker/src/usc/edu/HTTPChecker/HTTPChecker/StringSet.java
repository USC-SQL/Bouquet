package usc.edu.HTTPChecker.HTTPChecker;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dingli on 7/15/15.
 */
public class StringSet {
    Set<String> set=new HashSet<String>();
    public StringSet(String path) throws IOException {
        FileInputStream fis = new FileInputStream(new File(path));

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;
        while ((line = br.readLine()) != null) {
            set.add(line);
        }

        br.close();
    }
}
