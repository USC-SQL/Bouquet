package usc.edu.HTTPChecker.HTTPChecker;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import soot.*;
import CallGraph.Node;
import SootEvironment.AndroidApp;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocalBox;
import soot.jimple.spark.sets.HybridPointsToSet;

import javax.tools.Tool;

public class GlobalAnalyzer {
    AndroidApp App;
    HashMap<String, MethodSummary> summarytable = new HashMap<String, MethodSummary>();
    int HTTPcnt = 0;
    int domicnt = 0;
    int loopcnt=0;
    public GlobalAnalyzer(AndroidApp app) {
        this.App = app;
        List<Node> rtolist = App.getCallgraph().getRTOdering();
        for (Node n : rtolist) {
            SootMethod sm = n.getMethod();

            MethodSummary summary = new MethodSummary(sm);
            //summary.Analyze(summarytable);
            //summary.AnalyzeLoop();
            summary.AnalyzePointTo(App.pointto);

            HTTPcnt += summary.HTTPcnt;
            domicnt += summary.internalPostDomination.size();
            loopcnt+=summary.inloopinstructions.size();
            summarytable.put(sm.getSignature(), summary);
        }

    }
    public void PrintPointTo()
    {
        for (String key : summarytable.keySet()) {
            MethodSummary sum = summarytable.get(key);

            if (sum.isInteresting()) {
                //sum.display();
                System.out.println("INt");
                Set<DominationPoint> ds=sum.getDominatePoint();
                for(DominationPoint dp:ds)
                {
                    Stmt stmt=(Stmt)dp.ins.u;
                    if(ToolKit.isHttpOpen(stmt))
                    {
                        String sig=ToolKit.getInvokeSignature(stmt);
                        if(sig.equals("<java.net.URLConnection: java.io.InputStream getInputStream()>"))
                        {
                            InvokeExpr exp = stmt.getInvokeExpr();
                            Value l=exp.getUseBoxes().get(0).getValue();
                            PointsToSet pset=App.pointto.reachingObjects((Local)l);
                            //pset.
                            //System.out.println(pset.getClass().getName());
                            System.out.println(pset.isEmpty());


                        }
                        System.out.println(sig);
                    }
                    //System.out.println(stmt.getUseBoxes());
                }

            }
        }
    }

    public void DumeResult(PrintWriter out) {
        double r = ((double) domicnt) / ((double) HTTPcnt);
        out.println(r + " " + domicnt + " " + HTTPcnt);
        for (String key : summarytable.keySet()) {
            MethodSummary sum = summarytable.get(key);
            if (sum.isInteresting()) {
                //sum.display();
                out.println("summary_start");
                //String printstring=sum.sm.getSignature()+"#"+sum.internalDomination.toString();
                out.println(sum.DominatorStrings());
                out.println("summary_end");

            }
        }
    }
    public void DumeLoopResult(PrintWriter out) {
        out.println("There are: " + loopcnt + " loop HTTP" );
        for (String key : summarytable.keySet()) {
            MethodSummary sum = summarytable.get(key);
            if (sum.inloopinstructions.size()>0) {
                //sum.display();
                out.println("summary_start");
                //String printstring=sum.sm.getSignature()+"#"+sum.internalDomination.toString();
                out.println(sum.LoopInsStrings());
                out.println("summary_end");

            }
        }
    }
}
