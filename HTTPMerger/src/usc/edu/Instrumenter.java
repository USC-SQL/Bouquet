package usc.edu;

/**
 * Created by dingli on 4/21/15.
 */
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JimpleLocal;
import soot.util.*;

import java.util.*;
public class Instrumenter extends BodyTransformer{
    /* some internal fields */
    static SootClass Agentclass;
    static SootMethod increaseCounter, reportCounter,StringReporter;
    PrintWriter out;
    /*
     * internalTransform goes through a method body and inserts counter
     * instructions before an INVOKESTATIC instruction
     */
    static int instrumentid=0;
    public Instrumenter(PrintWriter pw)
    {
        out=pw;
    }
    protected void internalTransform(final Body body, String phase, Map options) {
        Chain units = body.getUnits();
        SootMethod method = body.getMethod();
        if(method.getSignature().startsWith("<"+AgentClassConstants.AgentClass))
        {

            return;
        }
        //if(method)
        boolean flag=false;
        Iterator stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {

            // cast back to a statement.
            Stmt stmt = (Stmt) stmtIt.next();
            if(stmt.containsInvokeExpr())
            {
                InvokeExpr invoke=stmt.getInvokeExpr();
                SootMethod agent=AgentClassConstants.QueryAgentMethod(invoke.getMethod().getSignature());
                if(agent!=null)
                {
                    List<Value> arglist=new LinkedList<Value>();
                    for(ValueBox vb:invoke.getUseBoxes())
                    {
                        arglist.add(vb.getValue());
                    }
                    //Jimple.v().newStaticInvokeExpr(agent.makeRef())
                    if(stmt instanceof AssignStmt)
                    {
                        Value assivalue=((AssignStmt) stmt).getLeftOp();
                        Stmt newassign=Jimple.v().newAssignStmt(assivalue, Jimple.v().newStaticInvokeExpr(agent.makeRef(), arglist));
                        units.insertBefore(newassign, stmt);
                        units.remove(stmt);
                        flag=true;


                    }
                    else if(stmt instanceof InvokeStmt)
                    {
                        Stmt newinvoke=Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(agent.makeRef(), arglist));
                        units.insertBefore(newinvoke, stmt);
                        units.remove(stmt);
                        flag=true;

                    }

                }
            }

        }
        body.validate();
        /*
        try {
            if(flag)
            {
                PrintWriter pw=new PrintWriter("/home/dingli/HTTPChecker/TestingApps/myinstrument.txt"+method.getSignature());

                System.out.println("======================After Instrumentation===========================");
                stmtIt = units.snapshotIterator();
                while (stmtIt.hasNext()) {
                    //System.out.println(stmtIt.next());
                    pw.println(stmtIt.next());

                }
                pw.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/







    }

}

