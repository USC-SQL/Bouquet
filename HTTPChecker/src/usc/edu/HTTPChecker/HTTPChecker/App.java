package usc.edu.HTTPChecker.HTTPChecker;

import java.io.*;
import java.util.*;

import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;
import soot.toolkits.scalar.FlowSet;
import CallGraph.StringCallGraph;
import SootEvironment.AndroidApp;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 3) {
            System.out.println("Usage: android_jar_path apk classlist.txt");
        }
        AndroidApp App = new AndroidApp(args[0], args[1], args[2]);
        GlobalAnalyzer ga = new GlobalAnalyzer(App);
        ga.PrintPointTo();


    }
}
