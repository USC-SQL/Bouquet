package usc.edu;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.*;

import java.util.Hashtable;
import java.util.Set;

/**
 * Created by dingli on 5/13/15.
 */
class Parameter {
    String methodname = "";
    String classname = "";

    public Parameter(String m, String c) {
        this.methodname = m;
        this.classname = c;
    }

}

public class BCELUtils {
    String methodname = "java.io.InputStream getInputStream(java.net.URLConnection)";
    String classname = "usc.edu.AgentURLConnection";
    public static int counter=0;
    final static private Hashtable<String, Parameter> AgentMethodTable = new Hashtable<String, Parameter>();

    static {
        AgentMethodTable.put("<java.net.URLConnection: java.io.InputStream getInputStream()>", new Parameter("getInputStream", "usc.edu.AgentURLConnection"));
        AgentMethodTable.put("<java.net.HttpURLConnection: java.io.InputStream getInputStream()>", new Parameter("getInputStream", "usc.edu.AgentURLConnection"));
        AgentMethodTable.put("<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>", new Parameter("HttpResponseexecute", "usc.edu.AgentURLConnection"));


    }

    public static boolean isInvoke(InstructionHandle ih) {
        if (ih.getInstruction().getOpcode() <= 0xba && ih.getInstruction().getOpcode() >= 0xb6)
            return true;
        return false;
    }

    public static Instruction getNewInstruction(InstructionHandle ih, ConstantPoolGen cpg, InstructionFactory inf, boolean opt) {
        Instruction ins = ih.getInstruction();

        if (ins instanceof InvokeInstruction) {
            InvokeInstruction invoke = (InvokeInstruction) ins;
            String methodname = invoke.getMethodName(cpg);
            String returnv = invoke.getReturnType(cpg).toString();
            String classname = invoke.getLoadClassType(cpg).toString();
            String arglist = " ";
            for (Type t : invoke.getArgumentTypes(cpg)) {
                arglist += t.toString() + " ";
            }
            arglist = arglist.trim();
            String querystring = "<" + classname + ": " + returnv + " " + methodname + "(" + arglist + ")>";
            Parameter pas = AgentMethodTable.get(querystring);
            if (pas != null) {
                Type[] oldpara = invoke.getArgumentTypes(cpg);
                Type[] newpara = new Type[oldpara.length + 1];
                for (int i = 0; i < oldpara.length; i++)
                    newpara[i + 1] = oldpara[i];
                newpara[0] = invoke.getReferenceType(cpg);

                InvokeInstruction iv;
                if(opt)
                {
                    iv= inf.createInvoke(pas.classname, pas.methodname+"OPT", invoke.getReturnType(cpg), newpara, Constants.INVOKESTATIC);
                }
                else{
                    iv= inf.createInvoke(pas.classname, pas.methodname, invoke.getReturnType(cpg), newpara, Constants.INVOKESTATIC);
                }
                return iv;
            }
            return null;

        }
        return null;
    }

    public static boolean isTarget(InstructionHandle ih, ConstantPoolGen cpg) {
        Instruction ins = ih.getInstruction();

        if (ins instanceof InvokeInstruction) {
            InvokeInstruction invoke = (InvokeInstruction) ins;
            String methodname = invoke.getMethodName(cpg);
            //<java.net.URLConnection: java.io.InputStream getInputStream()>
            String returnv = invoke.getReturnType(cpg).toString();
            String classname = invoke.getLoadClassType(cpg).toString();
            String arglist = " ";
            for (Type t : invoke.getArgumentTypes(cpg)) {
                arglist += t.toString() + " ";
            }
            arglist = arglist.trim();
            String querystring = "<" + classname + ": " + returnv + " " + methodname + "(" + arglist + ")>";
            System.out.println(querystring);
            if (AgentMethodTable.containsKey(querystring)) {
                return true;
            }
                /*
                System.out.println(querystring+AgentClassConstants.QueryAgentMethodForBCEL(querystring));
                System.out.println(invoke.getLoadClassType(cpg));
                System.out.println(methodname);
                */

            return false;

        }
        return false;
    }

}
