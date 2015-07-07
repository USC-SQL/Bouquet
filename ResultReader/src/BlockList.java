import java.util.LinkedList;

/**
 * Created by dingli on 6/18/15.
 */
public class BlockList {
    LinkedList<Block> blist=new LinkedList<Block>();
    public void addMethod(MethodData m){
        for(Block b:blist)
        {
            if(b.Merge(m))
                return;
        }
        Block nb=new Block(m.start,m.end);
        blist.add(nb);
    }
    public long nonIdleTime(){
        long r=0;
        for(Block b:blist)
        {
            r+=(b.end-b.start);
        }
        return r;
    }
}
