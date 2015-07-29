package usc.edu.HTTPChecker.HTTPChecker;

import java.io.*;
import java.util.*;

import org.xmlpull.v1.XmlPullParserException;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
import soot.options.Options;
import soot.toolkits.scalar.FlowSet;
import CallGraph.StringCallGraph;
import SootEvironment.AndroidApp;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, XmlPullParserException {
        if (args.length != 5) {
            System.out.println("Usage: android_jar_path apk classlist.txt stringresult output");
        }
        AndroidApp App = new AndroidApp(args[0], args[1], args[2]);
        GlobalAnalyzer ga = new GlobalAnalyzer(App);
        PrintWriter pw=new PrintWriter(args[4]);
        ga.DumeResult(pw);
        pw.close();

        //ga.DumpRules(System.out, args[3]);
        //ga.PrintPointTo();
        /*SetupApplication app = new SetupApplication("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/libs", "/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/com.bobcares.BobsWeather.apk");

        app.calculateSourcesSinksEntrypoints("./SourcesAndSinks.txt");
        soot.G.reset();



        Options.v().set_src_prec(Options.src_prec_apk);

        Options.v().set_process_dir(Collections.singletonList("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/com.bobcares.BobsWeather.apk"));

        Options.v().set_android_jars("/home/dingli/gitprojects/Merger/AppsAndScripts/TestingApps/libs");

        Options.v().set_whole_program(true);

        Options.v().set_allow_phantom_refs(true);

        Options.v().set_output_format(Options.output_format_J);


        Options.v().setPhaseOption("cg.spark", "on");



        Scene.v().loadNecessaryClasses();



        SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();

        Options.v().set_main_class(entryPoint.getSignature());

        Scene.v().setEntryPoints(Collections.singletonList(entryPoint));

        System.out.println(entryPoint.getActiveBody());



        PackManager.v().runPacks();

        JimpleBasedInterproceduralCFG icfg = new JimpleBasedInterproceduralCFG();

        System.out.println(Scene.v().getCallGraph().size());*/




    }
}
