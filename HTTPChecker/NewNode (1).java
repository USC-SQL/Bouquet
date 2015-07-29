package CallGraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;

public class NewNode {
    private SootMethod method;
    private Set<NewNode> children;
    private Set<NewNode> parent;
    private int order = -1;
    private int lowLink = -1;
    private boolean onStack = false;
    private LinkedList<NewEdge> inEdges;
    private LinkedList<NewEdge> outEdges;

    public NewNode(SootMethod m) {
        this.method = m;
        this.children = new HashSet<NewNode>();
        this.parent = new HashSet<NewNode>();
        this.inEdges = new LinkedList<NewEdge>();
        this.outEdges = new LinkedList<NewEdge>();

    }

    public boolean OrderAssigned() {
        return order != -1;
    }

    public void SetOrder(int o) {
        this.order = o;
    }

    public int GetOrder() {
        return this.order;
    }
    
    public boolean lowLinkAssigned() {
        return lowLink != -1;
    }

    public void SetLowLink(int o) {
        this.lowLink = o;
    }

    public int GetLowLink() {
        return this.lowLink;
    }
    
    public void SetOnStack(boolean b) {
        this.onStack = b;
    }

    public boolean isOnStack() {
        return this.onStack;
    }

    public SootMethod getMethod() {
        return this.method;
    }

    public Set<NewNode> getChildren() {
        return this.children;
    }

    public Set<NewNode> getParent() {
        return this.parent;
    }

    public void addChild(NewNode c) {
        this.children.add(c);
    }

    public void addParent(NewNode p) {
        this.parent.add(p);
    }
    
    public void removeChild(NewNode c){
    	this.children.remove(c);
    }
    
    public void removeParent(NewNode p){
    	this.parent.remove(p);
    }

    public int getIndgree() {
        return this.parent.size();
    }

    public int getOutdgree() {
        return this.children.size();
    }

    public String toString() {
        return method.getSignature();
    }
    
    public void addInEdge(Edge e){
    	NewEdge ne = new NewEdge(e);
    	this.inEdges.add(ne);
    }
    
    public void addOutEdge(Edge e){
    	NewEdge ne = new NewEdge(e);
    	this.outEdges.add(ne);
    }
    
    public LinkedList<NewEdge> getInEdge(){
    	return this.inEdges;
    }
    
    public LinkedList<NewEdge> getOutEdge(){
    	return this.outEdges;
    }
}
