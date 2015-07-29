package usc.edu.HTTPChecker.HTTPChecker;

/**
 * Created by dingli on 7/21/15.
 */
public class NameValuepair {
    String nameaddress;
    String valueaddress;
    public int hashCode() {
        return nameaddress.hashCode() * 10 + valueaddress.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof NameValuepair))
            return false;
        NameValuepair come = (NameValuepair) o;
        return this.nameaddress.equals(come.nameaddress) && this.valueaddress.equals(come.valueaddress);

    }

}
