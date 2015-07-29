package usc.edu.HTTPChecker.HTTPChecker;

import polyglot.ast.Local;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JimpleLocal;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.util.*;

public class HTTPPostAnalysis extends ForwardFlowAnalysis {
    private SootMethod method;
    UnitGraph g;
    FlowSet emptySet = new ArraySparseSet();
    FlowSet FullSet = new ArraySparseSet();
    HashMap<String, MethodSummary> summarytable = null;
    private Map<Unit, FlowSet> unitToGenerateSet;
    private Hashtable<Unit, Integer> offsettable = new Hashtable<Unit, Integer>();
    private HashMap<String,String> AliasTable=new  HashMap<String,String>();
    private HashMap<String,Set<NameValuepair>> parametertable=new HashMap<String,Set<NameValuepair>>();
    //private Set<Value>
    public HTTPPostAnalysis(UnitGraph graph) {
        super(graph);
        this.g = graph;
        method = graph.getBody().getMethod();
        Body body=graph.getBody();
        unitToGenerateSet = new HashMap<Unit, FlowSet>(graph.size() * 2 + 1, 0.7f);
        int offset = 0;
        //initilize the gen set, includes all new URL(String)
        for (Iterator unitIt = graph.iterator(); unitIt.hasNext(); ) {
            Unit stmt = (Unit) unitIt.next();
            FlowSet genSet = emptySet.clone();
            //System.out.println(stmt);

            if("<java.util.ArrayList: void <init>()>".equals(ToolKit.getInvokeSignature(stmt))
                    ||"<java.util.ArrayList: void <init>()>".equals(ToolKit.getInvokeSignature(stmt)))
            {
                Stmt st=(Stmt)stmt;
                InstanceInvokeExpr invokeins=(InstanceInvokeExpr)st.getInvokeExpr();
                AliasPair p=new AliasPair();
                String key=invokeins.getBase()+"@"+method.getSignature()+offset;
                p.key=key;
                p.value=key;
                genSet.add(p);


            }

            unitToGenerateSet.put(stmt, genSet);
            offsettable.put(stmt, offset);
            offset++;

        }
        doAnalysis();
        // TODO Auto-generated constructor stub
    }

    public int getOffset(Unit u) {
        return offsettable.get(u);
    }
    private Value getRight(AssignStmt u)
    {
        Value right=u.getRightOp();

        if(right instanceof  Local)
            return right;

        if(right instanceof JCastExpr)
        {
            JCastExpr cast=(JCastExpr)right;
            Value castright=cast.getOp();
            //System.out.println(castright+" cast "+(castright.getClass()));

            if(castright instanceof JimpleLocal)
                return castright;
        }
        return null;
    }
    @Override


    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        // TODO Auto-generated method stub
        FlowSet
                in = (FlowSet) inValue,
                out = (FlowSet) outValue;
        Unit stmt = (Unit) unit;
        FlowSet kill=emptySet.clone();
        int offset=offsettable.get(stmt);
        FlowSet gen = emptySet.clone();
        if("<java.util.ArrayList: void <init>()>".equals(ToolKit.getInvokeSignature(stmt)))
        {
            Stmt s=(Stmt)stmt;
            InstanceInvokeExpr insinvoke=(InstanceInvokeExpr)s.getInvokeExpr();
            Value currentvalue=insinvoke.getBase();
            //gen
            String valuekey=currentvalue+"@"+method.getSignature()+offset;
            AliasPair genp=new AliasPair();
            genp.key=valuekey;
            genp.value=valuekey;
            gen.add(genp);
            //kill
            for(Object obj:in)
            {
                AliasPair killp=(AliasPair)obj;
                String killstringv=killp.key.split("@")[0];
                if(currentvalue.toString().equals(killstringv))
                {
                    kill.add(killp);
                }


            }
        }
        if("<org.apache.http.client.entity.UrlEncodedFormEntity: void <init>(java.util.List)>".equals(ToolKit.getInvokeSignature(stmt)))
        {
            Stmt s=(Stmt)stmt;
            InstanceInvokeExpr insinvoke=(InstanceInvokeExpr)s.getInvokeExpr();
            Value base=insinvoke.getBase();

            Value parameter=insinvoke.getArg(0);
            for(Object obj:in)
            {
                AliasPair tp=(AliasPair)obj;
                String tstringv=tp.key.split("@")[0];
                if(parameter.toString().equals(tstringv))
                {
                    AliasPair genp=new AliasPair();
                    genp.key=base.toString()+"@"+method.getSignature()+offset;
                    genp.value=tp.value;
                    gen.add(genp);
                }


            }
            // kill set
            for(Object obj:in)
            {
                AliasPair killp=(AliasPair)obj;
                String killstringv=killp.key.split("@")[0];
                if(base.toString().equals(killstringv))
                {
                    kill.add(killp);
                }


            }

        }

        else if(stmt instanceof JAssignStmt)
        {
            Value right=getRight((JAssignStmt)stmt);
            if(right!=null)
            {
                //System.out.println(right+" right");
                AssignStmt as=(AssignStmt)stmt;
                Value left=as.getLeftOp();
                //gen set
                for(Object obj:in)
                {
                    AliasPair tp=(AliasPair)obj;
                    String killstringv=tp.key.split("@")[0];
                    if(right.toString().equals(killstringv))
                    {
                        AliasPair genp=new AliasPair();
                        genp.key=left.toString()+"@"+method.getSignature()+offset;
                        genp.value=tp.value;
                        gen.add(genp);
                    }


                }
                // kill set
                for(Object obj:in)
                {
                    AliasPair killp=(AliasPair)obj;
                    String killstringv=killp.key.split("@")[0];
                    if(left.toString().equals(killstringv))
                    {
                        kill.add(killp);
                    }


                }
            }




        }
        out.union(in, out);

        out.difference(kill, out);

        out.union(gen, out);

    }

    @Override
    protected void copy(Object source, Object dest) {
        // TODO Auto-generated method stub
        FlowSet
                sourceSet = (FlowSet) source,
                destSet = (FlowSet) dest;

        sourceSet.copy(destSet);

    }

    @Override
    protected Object entryInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.clone();
    }

    @Override
    protected void merge(Object in1, Object in2, Object out) {
        // TODO Auto-generated method stub
        FlowSet
                inSet1 = (FlowSet) in1,
                inSet2 = (FlowSet) in2,
                outSet = (FlowSet) out;
        inSet1.union(inSet2, outSet);

    }

    @Override
    protected Object newInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.clone();
    }


}
