package usc.edu.HTTPChecker.HTTPChecker;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by dingli on 7/14/15.
 */
public class HTTPSession {
    public LinkedList<HTTPUnit> list=new  LinkedList<HTTPUnit>();
    public boolean insert(DominationPoint dp,HashMap<SootInstruction,String> pointtoTable)
    {
        if(list.size()==0)
        {
            HTTPUnit u=new HTTPUnit();
            u.ins=dp.ins;
            u.linkaddress=pointtoTable.get(dp.ins);
            list.add(u);
            for(SootInstruction ins:dp.Dominators)
            {
                u=new HTTPUnit();
                u.ins=ins;
                u.linkaddress=pointtoTable.get(ins);
                list.add(u);
            }
            return true;
        }
        if(dp.ins.equals(list.getLast().ins))
        {
            for(SootInstruction ins:dp.Dominators)
            {
                HTTPUnit u=new HTTPUnit();
                u.ins=ins;
                u.linkaddress=pointtoTable.get(ins);
                list.add(u);
            }
            return true;
        }
        for(SootInstruction ins:dp.Dominators)
        {
            if(ins.equals(list.getFirst().ins))
            {
                HTTPUnit u=new HTTPUnit();
                u.ins=dp.ins;
                u.linkaddress=pointtoTable.get(dp.ins);
                list.addFirst(u);
                return true;

            }
        }

        return false;
    }
    public void display()
    {
        System.out.println("session start");
        for(HTTPUnit s:list)
        {
            System.out.println(s.linkaddress);
        }
        System.out.println("session end");

    }

}
