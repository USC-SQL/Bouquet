package usc.edu.HTTPChecker.HTTPChecker;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import SootEvironment.AndroidApp;
import soot.*;
import soot.baf.internal.BVirtualInvokeInst;
import soot.jimple.*;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

class DominationPoint {
    SootInstruction ins;
    Set<SootInstruction> Dominators = new HashSet<SootInstruction>();

    public String toString() {
        String r = "";
        r += ins.toString() + ":\n";
        for (SootInstruction dom : Dominators) {
            r += "	" + dom.toString() + "\n";
        }
        return r;
    }
}

public class MethodSummary {
    SootMethod sm;
    Set<DominationPoint> internalPostDomination = new HashSet<DominationPoint>();
    Set<HTTPSession> Sessions = new HashSet<HTTPSession>();
    Set<SootInstruction> entryPostDominators = new HashSet<SootInstruction>();
    Set<SootInstruction> httpinstructions = new HashSet<SootInstruction>();
    Set<SootInstruction> inloopinstructions=new HashSet<SootInstruction>();
    HashMap<SootInstruction,String> pointtoTable=new HashMap<SootInstruction,String>();
    HashMap<SootInstruction, Set<NameValuepair>> postparametertable=new HashMap<SootInstruction, Set<NameValuepair>>();

    int HTTPcnt;
    public  Set<DominationPoint> getDominatePoint()
    {
        return this.internalPostDomination;
    }

    public String DominatorStrings() {
        String r = "";
        r += sm.getSignature() + "\n";
        for (DominationPoint dp : internalPostDomination) {
            r += dp.toString() + "\n";
        }
        return r;
    }
    public String LoopInsStrings(){
        String r = "";
        r += sm.getSignature() + "\n           ";
        for (SootInstruction dp : inloopinstructions) {
            r += dp.toString() + "\n";
        }
        return r;
    }
    public boolean ReturnHasHTTP() {
        if (entryPostDominators.size() > 0)
            return true;
        return false;
    }

    public boolean HasHTTP() {
        if (httpinstructions.size() > 0)
            return true;
        return false;
    }
    public void AnalyzeLoop(){
        Body b = this.sm.retrieveActiveBody();
        UnitGraph g = new BriefUnitGraph(b);
        LoopAnalyzer httpan = new LoopAnalyzer(g);
        Chain units = b.getUnits();

        Iterator stmtIt = units.snapshotIterator();
        int cnt=0;
        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            FlowSet set=( FlowSet)httpan.getFlowBefore(stmt);
            if(set.contains(stmt))
            {
                SootInstruction ins=new SootInstruction(this.sm,stmt,cnt);
                inloopinstructions.add(ins);
            }
            cnt++;
        }


    }
    //A very simplyfied Andersen's Algorithm
    //Only for intra-procudural analysis
    //Only for local variables, no fields
    //the alloc nodes are new URL(), new HttpGet(), and new HttpPost()
    //the assignment nodes is URL.openconnection, URLconnection.getInputStream and HttpClient.execute()
    //private boolean nee
    public void AnalyzePointTo(){
        if(!this.isInteresting())
            return;
        Body b = this.sm.retrieveActiveBody();
        UnitGraph g = new BriefUnitGraph(b);
        PointToAnalyzer Pointto = new PointToAnalyzer(g);
        Chain units = b.getUnits();
        boolean flag=false;
        Iterator stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            int offset = Pointto.getOffset(stmt);
            SootInstruction sins = new SootInstruction(this.sm, stmt, offset);
            FlowSet set = (FlowSet) Pointto.getFlowBefore(stmt);
            //System.out.println(ToolKit.getInvokeSignature(stmt));
            if("<java.net.URLConnection: java.io.InputStream getInputStream()>".equals(ToolKit.getInvokeSignature(stmt)))
            {

                Stmt s=(Stmt) stmt;
                InstanceInvokeExpr inv=(InstanceInvokeExpr)s.getInvokeExpr();
                Value base=inv.getBase();
                for(Object obj:set)
                {
                    AliasPair p=(AliasPair)obj;
                    String vv=p.key.split("@")[0];
                    if(base.toString().equals(vv))
                    {
                        System.out.println(this.sm);
                        pointtoTable.put(sins,p.value);
                        System.out.println(p);
                    }
                }
                flag=true;
            }
            if("<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>".equals(ToolKit.getInvokeSignature(stmt)))
            {
                System.out.println(stmt);

                Stmt s=(Stmt) stmt;
                InstanceInvokeExpr inv=(InstanceInvokeExpr)s.getInvokeExpr();
                Value arg=inv.getArg(0);

                for(Object obj:set)
                {

                    AliasPair p=(AliasPair)obj;
                    String vv=p.key.split("@")[0];

                    if(arg.toString().equals(vv))
                    {
                        System.out.println(p);
                        pointtoTable.put(sins,p.value);

                    }
                }
                flag=true;
            }

        }


    }
    private void generateSessions()
    {
        Sessions.add(new HTTPSession());
        for(DominationPoint dp:internalPostDomination)
        {
            for(HTTPSession s:Sessions)
            {
                if(s.insert(dp,pointtoTable))
                {
                    break;
                }
                HTTPSession hs=new HTTPSession();
                hs.insert(dp,pointtoTable);
                Sessions.add(hs);

            }
        }
        for(HTTPSession s:Sessions)
        {
            for(HTTPUnit u:s.list)
            {
                if(postparametertable.containsKey(u.ins))
                {
                    u.arugaddress.addAll(postparametertable.get(u.ins));
                }
            }
        }
    }
    private void AnalyseHTTPPost()
    {
        Body b = this.sm.retrieveActiveBody();
        UnitGraph g = new BriefUnitGraph(b);
        HTTPPostAnalysis Pointto = new HTTPPostAnalysis(g);
        SootMethod method = g.getBody().getMethod();
        Chain units = b.getUnits();

        Iterator stmtIt = units.snapshotIterator();


        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            int offset = Pointto.getOffset(stmt);
            FlowSet set = (FlowSet) Pointto.getFlowBefore(stmt);
            //System.out.println(ToolKit.getInvokeSignature(stmt));
            /*if("<org.apache.http.client.methods.HttpEntityEnclosingRequestBase: void setEntity(org.apache.http.HttpEntity)>".equals(ToolKit.getInvokeSignature(stmt)))
            {
                //System.out.println(stmt);
                Stmt s=(Stmt) stmt;
                InstanceInvokeExpr inv=(InstanceInvokeExpr)s.getInvokeExpr();
                Value base=inv.getBase();
                Value parameter=inv.getArg(0);
                String basekey=base.toString()+"@"+method.getSignature()+offset;
                HashSet<NameValuepair> setto=new HashSet<NameValuepair>();
                String listkey="";
                System.out.println(stmt);
                for(Object obj:set)
                {

                    AliasPair p=(AliasPair)obj;
                    String vv=p.key.split("@")[0];
                    System.out.println(p.toString());

                    if(parameter.toString().equals(vv))
                    {
                        listkey=p.value;
                    }

                }

            }*/
            if("<org.apache.http.client.methods.HttpEntityEnclosingRequestBase: void setEntity(org.apache.http.HttpEntity)>".equals(ToolKit.getInvokeSignature(stmt)))
            {
                //System.out.println(stmt);
                Stmt s=(Stmt) stmt;
                InstanceInvokeExpr inv=(InstanceInvokeExpr)s.getInvokeExpr();
                Value base=inv.getBase();
                Value parameter=inv.getArg(0);
                String basekey=base.toString()+"@"+method.getSignature()+offset;
                HashSet<NameValuepair> setto=new HashSet<NameValuepair>();
                String listkey="";
                System.out.println(stmt);
                for(Object obj:set)
                {

                    AliasPair p=(AliasPair)obj;
                    String vv=p.key.split("@")[0];
                    System.out.println(p.toString());

                    if(parameter.toString().equals(vv))
                    {
                        listkey=p.value;
                    }

                }

            }

        }
    }
    private void ListAnalysis()
    {
        Body b = this.sm.retrieveActiveBody();
        UnitGraph g = new BriefUnitGraph(b);
        ListAnalysis Pointto = new ListAnalysis(g);
        Chain units = b.getUnits();
        SootMethod method = g.getBody().getMethod();
        Iterator stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            int offset = Pointto.getOffset(stmt);
            //System.out.println(ToolKit.getInvokeSignature(stmt));
            if("<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>".equals(ToolKit.getInvokeSignature(stmt)))
            {
                Stmt s=(Stmt)stmt;
                InstanceInvokeExpr insinvoke=(InstanceInvokeExpr)s.getInvokeExpr();
                Value parameter=insinvoke.getArg(0);
                FlowSet set = (FlowSet) Pointto.getFlowBefore(stmt);
                for(Object obj:set)
                {

                    AliasPair p=(AliasPair)obj;
                    String vv=p.key.split("@")[0];
                    System.out.println(p.toString());
                    SootInstruction sins = new SootInstruction(this.sm, stmt, offset);
                    if(!postparametertable.containsKey(sins))
                    {
                        postparametertable.put(sins,new HashSet<NameValuepair>());
                    }
                    Set<NameValuepair> paraset=postparametertable.get(sins);
                    if(parameter.toString().equals(vv))
                    {
                        paraset.addAll(Pointto.parametertable.get(p.value));
                    }

                }

            }
        }

    }
    public void Analyze(HashMap<String, MethodSummary> summarytable) {
        Body b = this.sm.retrieveActiveBody();
        UnitGraph g = new BriefUnitGraph(b);
        HTTPAnalyzer httpan = new HTTPAnalyzer(g, summarytable);
        Chain units = b.getUnits();
        Iterator stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            int offset = httpan.getOffset(stmt);
            SootInstruction sins = new SootInstruction(this.sm, stmt, offset);
            entryPostDominators.add(sins);

        }
        // typical while loop for iterating over each statement
        int fullins = entryPostDominators.size();
        stmtIt = units.snapshotIterator();

        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            if (ToolKit.isHttpOpen(stmt)) {
                this.HTTPcnt++;
                FlowSet set = (FlowSet) httpan.getFlowAfter(stmt);
                int offset = httpan.getOffset(stmt);
                SootInstruction sins = new SootInstruction(this.sm, stmt, offset);
                httpinstructions.add(sins);
                if (set.size() > 0) {
                    DominationPoint dp = new DominationPoint();
                    dp.ins = sins;
                    Iterator ir = set.iterator();
                    while (ir.hasNext()) {
                        SootInstruction next = (SootInstruction) ir.next();
                        dp.Dominators.add(next);
                    }
                    this.internalPostDomination.add(dp);
                }


            } else if (ToolKit.isInvocation(stmt)) {
                String signature = ToolKit.getInvokeSignature(stmt);
                if (summarytable.containsKey(signature)) {
                    MethodSummary ms = summarytable.get(signature);
                    if (ms.HasHTTP()) {
                        int offset = httpan.getOffset(stmt);
                        SootInstruction sins = new SootInstruction(this.sm, stmt, offset);
                        httpinstructions.add(sins);
                    }
                }
            } else if (g.getHeads().contains(stmt)) {
                FlowSet set = (FlowSet) httpan.getFlowBefore(stmt);
                if (set.size() < fullins) {
                    Set<SootInstruction> tempset = new HashSet<SootInstruction>();
                    Iterator ir = set.iterator();
                    while (ir.hasNext()) {
                        SootInstruction next = (SootInstruction) ir.next();
                        tempset.add(next);
                    }
                    entryPostDominators.retainAll(tempset);
                }
            }

        }
        if (entryPostDominators.size() == fullins) {
            entryPostDominators = new HashSet<SootInstruction>();
        }
        ListAnalysis();
        AnalyzePointTo();
        generateSessions();

    }
    public void displaySessions()
    {
        for(HTTPSession s:Sessions)
        {
            s.display();
        }

    }

    public MethodSummary(SootMethod m) {
        this.sm = m;

    }

    public boolean isInteresting() {
        return internalPostDomination.size() > 0;
    }
    private String getExtraDiscription(HTTPSession s,String StringAnalysis, int index) throws IOException {
        String r="\t\t\t/*\n\t\t\tExtra Dexription of reuqests\n";
        for(int i=index;i<s.list.size();i++)
        {
            for(int j=i+1;j<s.list.size();j++)
            {
                HTTPUnit first=s.list.get(i);
                HTTPUnit second=s.list.get(j);
                if(first.linkaddress.equals(second.linkaddress))
                {
                    String p="\t\t\tRequest "+i+" has the same address variable as "+j;
                    r+=(p+"\n");
                }

            }
        }
        for(int i=index;i<s.list.size();i++)
        {
            for(int j=i+1;j<s.list.size();j++)
            {

                HTTPUnit first=s.list.get(i);
                HTTPUnit second=s.list.get(j);
                StringSet set1=new StringSet(StringAnalysis+"/"+first.linkaddress);
                StringSet set2=new StringSet(StringAnalysis+"/"+second.linkaddress);

                if(set1.set.equals(set2.set))
                {
                    String p="\t\t\tRequest "+(i-index)+" has the same address value as "+(j-index);
                    r+=(p+"\n");
                }

            }
        }
        r+="\t\t\t*/\n";
        return r;
    }
    private boolean isDoable(HTTPSession s,int sessionid,int index,String StringAnalysis) throws IOException {
        HTTPUnit first=s.list.get(index);
        StringSet set=new StringSet(StringAnalysis+"/"+first.linkaddress);
        Set<String> patternset=new HashSet<String>();
        boolean firstconstant=false;
        for(String url:set.set)
        {
            patternset.add(url.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?"));
        }
        System.out.println(patternset);
        System.out.println(set.set);

        if(set.set.equals(patternset)&&patternset.size()==1)
        {
            firstconstant=true;
            System.out.println(firstconstant);
        }
        int rexidcnt=0;
        for(String dis:patternset)
        {
            String variablename="Session_"+sessionid+"_"+index+"_rex0_"+rexidcnt;
            if(first.arugaddress.size()>0)
            {
                dis=dis.replaceAll("/","\\\\/").replaceAll("\\.","\\\\.")+"\\?(.+)";
            }
            rexidcnt++;
        }

        rexidcnt=0;
        boolean r0=true;
        boolean r1=true;
        boolean r2=true;
        for(String dis:patternset)
        {
            String variablename="Session_"+sessionid+"_"+index+"_rex0_"+rexidcnt;
            String matchername="Macther_"+sessionid+"_"+index+"_rex0_"+rexidcnt;


                for(NameValuepair np:first.arugaddress)
                {
                    StringSet nameset=new StringSet(StringAnalysis+"/"+np.nameaddress);
                    StringSet valueset=new StringSet(StringAnalysis+"/"+np.valueaddress);
                    for(String n:nameset.set)
                        for(String v:valueset.set)
                        {
                            String printn=n.replaceAll("###@@@.*@@@###", "([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?").replaceAll("/","\\\\/");
                            String printv=v.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?").replaceAll("/","\\\\/");

                        }


                }


            for(int hi=index+1;hi<s.list.size();hi++)
                {
                    HTTPUnit next=s.list.get(hi);
                    Set<String> nextpatternset=new HashSet<String>();
                    StringSet nextset=new StringSet(StringAnalysis+"/"+next.linkaddress);
                    boolean nextlinkconst=false;

                    for(String url:nextset.set)
                    {
                        nextpatternset.add(url.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?"));
                    }
                    if(nextset.set.equals(nextpatternset)&&nextpatternset.size()==1)
                    {
                        nextlinkconst=true;
                    }

                    if(!(next.arugaddress.equals(first.arugaddress)&&nextlinkconst&&patternset.equals(set.set)))
                    {
                        r0=false;
                    }
                    if(!(first.linkaddress.equals(next.linkaddress)))
                    {
                        r1=false;
                    }
                    if(!(first.linkaddress.equals(next.linkaddress)))
                    {
                        r1=false;
                    }
                    if(!(set.set.equals(nextset.set)))
                    {
                        r2=false;
                    }


                }



            }

            rexidcnt++;
        return r0&&r1&&r2;


    }
    /*public boolean Doable()
    {
        if(!this.isInteresting())
            return false;
        int sessionid=0;
        for(HTTPSession s:Sessions)
        {
            for(int i=0;i<s.list.size()-1;i++)
            {
                //genRulefunction(s,sessionid,i,ps,StringAnalysis);

            }
            sessionid++;
        }
    }*/

    private void genRulefunction(HTTPSession s,int sessionid,int index, PrintStream ps,String StringAnalysis) throws IOException {
        HTTPUnit first=s.list.get(index);
        StringSet set=new StringSet(StringAnalysis+"/"+first.linkaddress);
        Set<String> patternset=new HashSet<String>();
        boolean firstconstant=false;
        for(String url:set.set)
        {
            patternset.add(url.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?"));
        }
        System.out.println(patternset);
        System.out.println(set.set);

        if(set.set.equals(patternset)&&patternset.size()==1)
        {
            firstconstant=true;
            System.out.println(firstconstant);
        }
        int rexidcnt=0;
        for(String dis:patternset)
        {
            String variablename="Session_"+sessionid+"_"+index+"_rex0_"+rexidcnt;
            if(first.arugaddress.size()>0)
            {
                dis=dis.replaceAll("/","\\\\/").replaceAll("\\.","\\\\.")+"\\?(.+)";
            }
            ps.println("var " + variablename + "=new RegExp(/" + dis + "/);");
            rexidcnt++;
        }
        ps.println("var Session_"+sessionid+"_"+index+"_rule = function (link){");
        ps.println("    var pack={main:link,bundled:[]};");

        rexidcnt=0;
        for(String dis:patternset)
        {
            String variablename="Session_"+sessionid+"_"+index+"_rex0_"+rexidcnt;
            String matchername="Macther_"+sessionid+"_"+index+"_rex0_"+rexidcnt;
            ps.println("    "+matchername+"="+variablename+".exec(link)");

            ps.println("    if("+matchername+"!=null)");
            {
                ps.println("    {");
                ps.println("\t\t\t//The incomming link has pattern: "+dis);
                ps.println("\t\t\t/*The incomming link has parameter list");

                for(NameValuepair np:first.arugaddress)
                {
                    StringSet nameset=new StringSet(StringAnalysis+"/"+np.nameaddress);
                    StringSet valueset=new StringSet(StringAnalysis+"/"+np.valueaddress);
                    for(String n:nameset.set)
                        for(String v:valueset.set)
                        {
                            String printn=n.replaceAll("###@@@.*@@@###", "([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?").replaceAll("/","\\\\/");
                            String printv=v.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?").replaceAll("/","\\\\/");
                            ps.println("\t\t\t"+printn+":"+printv);

                        }


                }
                ps.println("\t\t\t*/");

                for(int hi=index+1;hi<s.list.size();hi++)
                {
                    HTTPUnit next=s.list.get(hi);
                    Set<String> nextpatternset=new HashSet<String>();
                    StringSet nextset=new StringSet(StringAnalysis+"/"+next.linkaddress);
                    boolean nextlinkconst=false;

                    for(String url:nextset.set)
                    {
                        nextpatternset.add(url.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?"));
                    }
                    if(nextset.set.equals(nextpatternset)&&nextpatternset.size()==1)
                    {
                        nextlinkconst=true;
                    }
                    ps.println("\t\t\t/*");
                    ps.println("\t\t\tThe Next "+(hi-index)+" link has patterns: ");

                    for(String nextdis:nextpatternset)
                    {
                        ps.println("\t\t\t"+nextdis);
                    }
                    ps.println("\t\t\tThe Next "+(hi-index)+" link has parameters as: ");

                    for(NameValuepair np:next.arugaddress)
                    {
                        StringSet nameset=new StringSet(StringAnalysis+"/"+np.nameaddress);
                        StringSet valueset=new StringSet(StringAnalysis+"/"+np.valueaddress);
                        for(String n:nameset.set)
                            for(String v:valueset.set)
                            {
                                String printn=n.replaceAll("###@@@.*@@@###", "([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?").replaceAll("/","\\\\/");
                                String printv=v.replaceAll("###@@@.*@@@###","([A-Za-z0-9\\+]+)").replaceAll("\\?","\\\\?").replaceAll("/","\\\\/");
                                ps.println("\t\t\t"+printn+":"+printv);

                            }


                    }
                    ps.println("\t\t\tPlease add these patterns and parameters to pack.bundled");
                    ps.println("\t\t\t*/");
                    if(next.arugaddress.equals(first.arugaddress)&&nextlinkconst&&patternset.equals(set.set))
                    {
                        for(String nexturl:nextpatternset)
                        {
                            ps.println("\t\t\tvar link"+(hi-index)+"=\""+nexturl+"\"+match[1];");
                            ps.println("\t\t\tpack.bundled.push(link"+(hi-index)+");");
                        }
                        ps.println("\t\t\tvar link1="+"+match[1];");
                    }

                }
                ps.println(getExtraDiscription(s,StringAnalysis,index));
                ps.println("\t\t\treturn pack;");

                ps.println("    }");


            }
            rexidcnt++;
        }
        ps.println("\treturn pack;");

        ps.println("}");


        s.display();
        System.out.println(first.linkaddress);
    }

    public void DumpRules(PrintStream ps, String StringAnalysis) throws IOException {
        if(!this.isInteresting())
            return;
        int sessionid=0;
        for(HTTPSession s:Sessions)
        {
            for(int i=0;i<s.list.size()-1;i++)
            {
                genRulefunction(s,sessionid,i,ps,StringAnalysis);

            }
            sessionid++;
        }
    }

    public void display() {
        System.out.println("==========start==============");
        System.out.println(sm);
        System.out.println("++++++++++return+++++++++++++");

        System.out.println(entryPostDominators.size());

        System.out.println(entryPostDominators);
        System.out.println("++++++++++Dominators+++++++++++++");

        System.out.println(internalPostDomination.size());

        for (DominationPoint dp : internalPostDomination) {
            System.out.println(dp.ins);
            System.out.println(dp.Dominators.size());

            System.out.println(dp.Dominators);

        }


        System.out.println("==========end==============");

    }
}
