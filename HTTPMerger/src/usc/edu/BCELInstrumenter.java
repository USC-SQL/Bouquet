package usc.edu;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dingli on 5/13/15.
 */
class ReplacePair {
    InstructionHandle oldins;
    Instruction newins;
}

public class BCELInstrumenter {
    private static Method Instrumentmethod(MethodGen input, boolean Timeprof, boolean opt) {

        InstructionList ilist = input.getInstructionList();

        //System.out.println(ilist);
        InstructionHandle[] ihs = ilist.getInstructionHandles();
        ConstantPoolGen cpg = input.getConstantPool();
        LinkedList<ReplacePair> ivs = new LinkedList<ReplacePair>();
        boolean flag = false;
        InstructionFactory inf = new InstructionFactory(input.getConstantPool());
        LocalVariableTable lvt=input.getLocalVariableTable(input.getConstantPool());
        LocalVariableGen lvtime=input.addLocalVariable("dingstartmillitime", Type.LONG, null, null);
        int start_time_milli_idx=lvtime.getIndex();
        Instruction MstartLogger = inf.createInvoke("usc.edu.AgentURLConnection", "LogCallStart", Type.LONG, new Type[0], Constants.INVOKESTATIC);//probes for the entry of methods
        Instruction MendLogger = inf.createInvoke("usc.edu.AgentURLConnection", "LogCallReturn", Type.VOID, new Type[]{Type.LONG}, Constants.INVOKESTATIC);//probes for the exit of methods
        LinkedList<InstructionHandle> returnpoints=new LinkedList<InstructionHandle>();
        for (int i = 0; i < ihs.length; i++) {
            BCELUtils.counter++;
            if (BCELUtils.isTarget(ihs[i], cpg)) {
                ReplacePair p = new ReplacePair();
                p.oldins = ihs[i];
                p.newins = BCELUtils.getNewInstruction(ihs[i], cpg, inf,opt);
                System.out.println(input.getName());
                ivs.push(p);
                //flag=true;


                //InstructionFactory inf=new InstructionFactory(input.getConstantPool());
            } else if (ihs[i].getInstruction() instanceof ReturnInstruction) {
                returnpoints.add(ihs[i]);
                //flag = true;

            } else if (ihs[i].getInstruction() instanceof ATHROW) {
                returnpoints.add(ihs[i]);
            }
        }
        if (flag) {
            System.out.println(input.getMethod().getCode());
        }
        if(Timeprof)
        {
            ilist.insert(InstructionFactory.createStore(Type.LONG,start_time_milli_idx));
            ilist.insert(MstartLogger);
            for(InstructionHandle ih:returnpoints)
            {
                InstructionHandle nih=ilist.insert(ih, InstructionFactory.createLoad(Type.LONG, start_time_milli_idx));
                ilist.insert(ih, MendLogger);
                ilist.redirectBranches(ih,nih);
            }
        }
        for (ReplacePair ih : ivs) {
            InstructionHandle nih=ilist.append(ih.oldins, ih.newins);
            ilist.redirectBranches(ih.oldins,nih);

            try {
                ilist.delete(ih.oldins);
            } catch (TargetLostException e) {
                e.printStackTrace();
            }
            //System.out.println(inf.createInvoke(classname, methodname, Type.LONG, new Type[0], Constants.INVOKESTATIC));
            // ilist.insert(ih.getInstruction(),)
            // ilist.

        }
        // instrument probes for time
        ilist.setPositions();
        input.setInstructionList(ilist);
        input.removeLineNumbers();
        input.setMaxStack();
        input.setMaxLocals();
        if (Timeprof) {
            System.out.println(input.getMethod().getCode());
        }
        return input.getMethod();
    }

    public static void main(String args[]) throws IOException {

        if (!( args.length == 3)) {
            System.err.println("Usage: java -jar BCELInstrumenter.jar input_dir1 timeprof opt");
            System.exit(-1);
        }
        List<String> all_cls = get_all_classes(args[0]);
        boolean timeprof = Boolean.parseBoolean(args[1]);
        boolean opt = Boolean.parseBoolean(args[2]);
        for (String myclasses : all_cls) {
            JavaClass jcls = new ClassParser(myclasses).parse();
            ClassGen cgen = new ClassGen(jcls);
            for (Method mthd : jcls.getMethods()) {
                MethodGen method = new MethodGen(mthd, cgen.getClassName(), cgen.getConstantPool());
                if (!method.isAbstract() && !method.isInterface()) {
                    Method m = Instrumentmethod(method, timeprof,opt);
                    cgen.replaceMethod(mthd, m);

                }

                File out_file = new File(myclasses.replaceFirst("classes", "outPut"));
                File ofp = out_file.getParentFile();
                if (!ofp.exists()) {
                    ofp.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(out_file);
                cgen.getJavaClass().dump(fos);
                fos.close();
            }
            //System.out.println(jcls.toString());
        }
        System.out.println(BCELUtils.counter);

    }

    private static List<String> get_all_classes(String cls_dir) {
        List<String> all_cls = new ArrayList<String>();

        try {
            Process proc = Runtime.getRuntime().exec("find " + cls_dir + " -name *.class");
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()), 16);
            String line;
            while ((line = br.readLine()) != null) {
                all_cls.add(line.trim());
            }
            proc.waitFor();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return all_cls;
    }
}
