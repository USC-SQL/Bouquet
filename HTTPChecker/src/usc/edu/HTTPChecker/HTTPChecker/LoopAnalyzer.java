package usc.edu.HTTPChecker.HTTPChecker;

import CallGraph.Node;
import SootEvironment.AndroidApp;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by dingli on 6/16/15.
 */
public class LoopAnalyzer extends ForwardFlowAnalysis {
    private SootMethod method;
    UnitGraph g;
    FlowSet emptySet = new ArraySparseSet();
    FlowSet FullSet = new ArraySparseSet();
    private Map<Unit, FlowSet> unitToGenerateSet;
    private Hashtable<Unit, Integer> offsettable = new Hashtable<Unit, Integer>();

    public LoopAnalyzer(UnitGraph graph) {
        super(graph);
        this.g = graph;
        method = graph.getBody().getMethod();
        unitToGenerateSet = new HashMap<Unit, FlowSet>(graph.size() * 2 + 1, 0.7f);
        int offset = 0;
        for (Iterator unitIt = graph.iterator(); unitIt.hasNext(); ) {
            Unit s = (Unit) unitIt.next();
            FlowSet genSet = emptySet.clone();
            if (ToolKit.isHttpOpen( s)) {
                genSet.add(s);
            }
            unitToGenerateSet.put(s, genSet);
            offsettable.put(s, offset);
            offset++;

        }
        doAnalysis();
        // TODO Auto-generated constructor stub
    }



    public int getOffset(Unit u) {
        return offsettable.get(u);
    }

    @Override
    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        // TODO Auto-generated method stub
        FlowSet
                in = (FlowSet) inValue,
                out = (FlowSet) outValue;
        Unit s = (Unit) unit;
        FlowSet gen = unitToGenerateSet.get(s);
        in.union(gen, out);
    }

    @Override
    protected void copy(Object source, Object dest) {
        // TODO Auto-generated method stub
        FlowSet
                sourceSet = (FlowSet) source,
                destSet = (FlowSet) dest;

        sourceSet.copy(destSet);

    }

    @Override
    protected Object entryInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.clone();
    }

    @Override
    protected void merge(Object in1, Object in2, Object out) {
        // TODO Auto-generated method stub
        FlowSet
                inSet1 = (FlowSet) in1,
                inSet2 = (FlowSet) in2,
                outSet = (FlowSet) out;
        inSet1.union(inSet2, outSet);

    }

    @Override
    protected Object newInitialFlow() {
        // TODO Auto-generated method stub
        return FullSet.clone();
    }
}
