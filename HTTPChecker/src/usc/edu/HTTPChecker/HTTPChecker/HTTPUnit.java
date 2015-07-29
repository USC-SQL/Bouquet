package usc.edu.HTTPChecker.HTTPChecker;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dingli on 7/15/15.
 */
public class HTTPUnit {
    public SootInstruction ins;
    public String linkaddress;
    public Set<NameValuepair> arugaddress=new HashSet<NameValuepair>();
    public boolean hasCookie=false;
}
