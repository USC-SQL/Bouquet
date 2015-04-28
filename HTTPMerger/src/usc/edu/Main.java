package usc.edu;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
	// write your code here
        //paramter -cp /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar:/home/dingli/android-sdk-linux/platforms/android-17/android.jar:/home/dingli/StringChecker/recordlib.jar:/home/dingli/StringChecker/Simpletest/target/ Simpletest
        Scene.v().addBasicClass(AgentClassConstants.AgentClass, SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.io.BufferedWriter",SootClass.SIGNATURES);

        Scene.v().addBasicClass("java.io.OutputStreamWriter",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        Scene.v().addBasicClass("java.util.Formatter",SootClass.SIGNATURES);

        PrintWriter pw=new PrintWriter("./InstrumentLog.txt");
        Options.v().set_src_prec(Options.src_prec_J);
        Options.v().set_output_format(Options.output_format_J);
        PackManager.v().getPack("jtp").add(new Transform("jtp.myTransform", new Instrumenter(pw)));
        soot.Main.main(args);
        pw.close();
    }
}
