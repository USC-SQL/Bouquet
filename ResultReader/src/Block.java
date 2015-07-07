/**
 * Created by dingli on 6/18/15.
 */
public class Block {
    long start;
    long end;
    public Block(long s, long e){
        this.start=s;
        this.end=e;
    }
    public boolean Merge(MethodData m)
    {
        if(m.end<this.start||m.start>this.end)
        {
            return false;
        }
        if(m.end>=this.end)
            this.end=m.end;
        if(m.start<this.start)
            this.start=m.start;
        return true;
    }

}
