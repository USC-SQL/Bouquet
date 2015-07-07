package usc.edu.HTTPChecker.HTTPChecker;

import CallGraph.Node;
import SootEvironment.AndroidApp;
import soot.SootMethod;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dingli on 6/16/15.
 */
public class GlobalLoopAnalyzer {
    AndroidApp App;
    HashMap<String, MethodSummary> summarytable = new HashMap<String, MethodSummary>();
    int HTTPcnt = 0;
    int domicnt = 0;

    public GlobalLoopAnalyzer(AndroidApp app) {
        this.App = app;
        List<Node> rtolist = App.getCallgraph().getRTOdering();
        for (Node n : rtolist) {
            SootMethod sm = n.getMethod();

            MethodSummary summary = new MethodSummary(sm);
            summary.Analyze(summarytable);
            HTTPcnt += summary.HTTPcnt;
            domicnt += summary.internalPostDomination.size();
            summarytable.put(sm.getSignature(), summary);
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
}
