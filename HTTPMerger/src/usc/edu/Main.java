package usc.edu;

import soot.*;
import soot.jimple.spark.SparkTransformer;
import soot.options.Options;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Main {
    private static SootClass loadClass(String name,
                                       boolean main) {
        SootClass c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
        if (main) Scene.v().setMainClass(c);
        return c;
    }
    public static void main(String[] args) throws FileNotFoundException {
        // write your code here
        //paramter -cp /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar:/home/dingli/android-sdk-linux/platforms/android-17/android.jar:/home/dingli/StringChecker/recordlib.jar:/home/dingli/StringChecker/Simpletest/target/ Simpletest
        /*Scene.v().addBasicClass(AgentClassConstants.AgentClass, SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.io.BufferedWriter", SootClass.SIGNATURES);

        Scene.v().addBasicClass("java.io.OutputStreamWriter", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.Formatter", SootClass.SIGNATURES);*/
        Scene.v().setSootClassPath("/home/dingli/Soottest:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar");

        loadClass("Item",false);
        loadClass("Container",false);
        SootClass c = loadClass("Main",true);
        HashMap opt = new HashMap();
        opt.put("verbose","true");
        opt.put("propagator","worklist");
        opt.put("simple-edges-bidirectional","false");
        opt.put("on-fly-cg","true");
        opt.put("set-impl","double");
        opt.put("double-set-old","hybrid");
        opt.put("double-set-new","hybrid");
        SparkTransformer.v().transform("",opt);
        PointsToAnalysis pa=Scene.v().getPointsToAnalysis();
        System.out.println(pa);
        /*PrintWriter pw = new PrintWriter("./InstrumentLog.txt");
        Options.v().set_src_prec(Options.src_prec_J);
        Options.v().set_output_format(Options.output_format_J);
        PackManager.v().getPack("jtp").add(new Transform("jtp.myTransform", new Instrumenter(pw)));
        soot.Main.main(args);
        pw.close();*/
    }
}
