package CallGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;
import edu.usc.sql.ad.reachability.ReachabilityAnalysis;
import edu.usc.sql.ad.reachability.Tree;
import edu.usc.sql.ad.reachability.Tree.TNode;
import edu.usc.sql.graphs.Node;
import edu.usc.sql.graphs.NodeInterface;
import edu.usc.sql.graphs.cfg.CFGInterface;
import edu.usc.sql.graphs.cfg.SootCFG;

class Counter {
    private int cnt = 0;

    public void inCreaseCounter() {
        this.cnt++;
    }

    public int getCnt() {
        return cnt;
    }
}

public class StringCallGraph {
    private Set<NewNode> nodes;
    private Set<NewNode> heads;
    private Set<NewNode> nodesinscc;
    private LinkedList<NewNode> RTOlist = new LinkedList<NewNode>();
    private HashMap<NewNode, Integer> indegredmap=new HashMap<NewNode, Integer>();
    public static HashMap<String, Tree<String>> summaryTree = new HashMap<>();
    private String entry = "entry";

    private void Visit(NewNode n, List<NewNode> list, Set<NewNode> visited) {
        if (visited.contains(n))
            return;
        visited.add(n);

        for (NewNode w : n.getChildren()) {
            Visit(w, list, visited);
        }
        list.add(n);
    }
    //reverse topology ordering
    //refer to the first algorithm of https://en.wikipedia.org/wiki/Topological_sorting
    private static Set<String> sigSet = new HashSet<String>();
    private static int count = 0;
    //strongly connected component detection
    private void isLoopNode(Iterator<NewNode> iter, NewNode m){
    	//System.out.print(++count);
    	sigSet.add(m.getMethod().getSignature());
    	if(!iter.hasNext()) {
    		System.out.println("+++ entry parent: " + m.getMethod().getSignature());
    		return;
    	}
    	while(iter.hasNext()){
    		NewNode n = iter.next();
    		System.out.println("\t" + getNodeString(n.getMethod().getSignature()) + " -> " + getNodeString(m.getMethod().getSignature()) + ";");
    		if(sigSet.contains(n.getMethod().getSignature())) {
    			System.out.println("*** 2nd parent: " + n.getMethod().getSignature());
    			return;
    		}
    		//System.out.println("parent: " + n.getMethod().getSignature());
    		sigSet.add(n.getMethod().getSignature());
    		isLoopNode(n.getParent().iterator(), n);
    	}
    }
    public static String getNodeString(String str){
		String[] arr = str.split(":"), arr1 = arr[1].split(" ");
		String c = arr[0].substring(arr[0].lastIndexOf('.') + 1), m = arr[1].substring(0, arr[1].lastIndexOf('('));
		String result = "<" + c + ": " + m + ">";
		return result;
	}
    private void RTOsorting(){
		for(NewNode n:nodes)
		{
			indegredmap.put(n,n.getIndgree());
			/*if(n.getMethod().getSignature().contains("com.wisesharksoftware.core.DiskLruCache: boolean remove(")){
				isLoopNode(n.getParent().iterator(), n);
            }*/
		}
		Set<NewNode> visited=new HashSet<NewNode> ();
		Queue<NewNode> S=new LinkedList<NewNode>();
		S.addAll(heads);
		while(!S.isEmpty())
		{
			NewNode n=S.poll();
			visited.add(n);
			RTOlist.addFirst(n);
			for(NewNode m:n.getChildren())
			{
				/*System.out.println("p: " + n.getMethod().getSignature() + "\tc: " + m.getMethod().getSignature());
				if(!nodes.contains(m)){
					System.out.println("this node is not included in nodes, with indegree: " + m.getIndgree());
				}*/
				int deg=indegredmap.get(m);
				deg--;
				indegredmap.put(m, deg);
				if(deg<=0&& !visited.contains(m))
				{
					S.add(m);
					visited.add(m);
				}
			}
		}
		
	}
    
    public List<CFGInterface> getRTOInterface(){
        List<CFGInterface> r=new LinkedList<CFGInterface>();
        System.out.println("Nodes size: " + nodes.size() + "\tRTOlist size: " + RTOlist.size());
        /*for(NewNode n : nodes){
        	if(!RTOlist.contains(n)){
        		System.out.println("missing node after RTO: " + n.getMethod().getSignature() + "\tIndegree: " + n.getIndgree() + "\tOutdegree: " + n.getOutdgree());
        	}
        }*/
        for(NewNode n:RTOlist)
        {
            if(n.getMethod().isConcrete()){
            	SootCFG cfg = new SootCFG(n.getMethod().getSignature(),n.getMethod());
                r.add(cfg);
                if(n.getMethod().getSignature().contains("com.wisesharksoftware.ad.Banner: void <init>(")){
                	System.out.println("All related loadAd: " + n.getMethod().getSignature());
                }/*else if(cfg.getSignature().contains("onCreate(") && !cfg.getSignature().contains("android.support") && !cfg.getSignature().contains("com.google")){
                	System.out.println("All related loadAd: " + n.getMethod().getSignature());
                }*/
                processSummary(n, cfg);
            }
        }
        return r;
    }

    public LinkedList<NewNode> getRTOdering() {

        return RTOlist;
    }
    
    public HashMap<String, Tree<String>> getSummary(){
    	return summaryTree;
    }
    
    public void processSummary(NewNode n, SootCFG cfg){
    	if(n.getMethod().getName().equals("loadAd")){
    		//System.out.println("Method Node (loadAd): " + n.getMethod().getSignature());
    		if(summaryTree.containsKey(n.getMethod().getSignature())) return;
    		//Tree<String> t = new Tree<String>("entry");
    		Tree<String> t = new Tree<String>(n.getMethod().getSignature());
    		TNode<String> root = t.getRoot();
    		List<TNode<String>> chList = new LinkedList<TNode<String>>();
    		TNode<String> ch = new TNode<String>(n.getMethod().getSignature());
    		chList.add(ch);
    		root.setChildren(chList);
    		//ch.setParent(root);
    		summaryTree.put(n.getMethod().getSignature(), t);
    	}else if(isVirtualAdNode(cfg)){
    		//System.out.println("Method Node: " + n.getMethod().getSignature());
    		ReachabilityAnalysis ra = new ReachabilityAnalysis(cfg.getUnitGraph(), summaryTree);
    		FlowSet outSet = (FlowSet) ra.getFlowAfter(cfg.getUnitGraph().getTails().get(0));
    		/*for(Object u:outSet){
				System.out.println("out: " + u);
			}*/
    		
    		Set<Unit> adUnitSet = new HashSet<Unit>();
    		adUnitSet = getVirtualAdUnit(cfg.getUnitGraph(), summaryTree);
    		//System.out.println("adUnitSet: " + adUnitSet.size());
    		
    		//Tree<String> t = new Tree<String>("entry");
    		Tree<String> t = new Tree<String>(n.getMethod().getSignature());
    		TNode<String> root = t.getRoot();
    		List<TNode<String>> rootChList = new LinkedList<TNode<String>>();
    		for(Unit u:adUnitSet){
    			String unitSig = getAdUnitSignature(u);
    			rootChList.add(new TNode<String>(unitSig));
    			//System.out.println("adUnit signature: " + unitSig);
    		}
    		for(Unit u:adUnitSet){
    			FlowSet inSet = (FlowSet) ra.getFlowBefore(u);
    			//add to summaryTree
    			TNode<String> adChildUnit = getAdUnitNode(rootChList, getAdUnitSignature(u));
    			for(Object o:inSet){
    				TNode<String> adParentUnit = getAdUnitNode(rootChList, getAdUnitSignature((Unit)o));
    				if(adParentUnit.getChildren() == null){
    					List<TNode<String>> adUnitChList = new LinkedList<TNode<String>>();
    					adUnitChList.add(adChildUnit);
    					adParentUnit.setChildren(adUnitChList);
    				}else{
    					List<TNode<String>> adUnitChList = adParentUnit.getChildren();
    					adUnitChList.add(adChildUnit);
    				}
    			}
    		}
    		root.setChildren(rootChList);
    		summaryTree.put(n.getMethod().getSignature(), t);
    		//System.out.println("Key: " + n.getMethod().getSignature() + "\ttree root children size: " + t.getRoot().getChildren().size());
    	}
    }
    
    public boolean isVirtualAdNode(SootCFG cfg){
		for(NodeInterface n:cfg.getAllNodes()){
			if(((Node)n).getActualNode()!=null){
				String signature = ((Node) n).getActualNode().toString();
				if (signature.contains("<") && signature.contains(">")) {
					signature = signature.substring(signature.indexOf('<'),	signature.lastIndexOf('>') + 1);
					if (signature.contains("loadAd(") || summaryTree.containsKey(signature)) {
						//System.out.println("Unit string: " + ((Node) n).getActualNode().toString());
						return true;
					}
				}
			}
		}
    	return false;
    }
    
    public Set<Unit> getVirtualAdUnit(DirectedGraph<Unit> graph, HashMap<String, Tree<String>> summary){
    	Set<Unit> adUnitSet = new HashSet<Unit>();
    	for(Iterator unitIt = graph.iterator(); unitIt.hasNext();){
            Unit s = (Unit) unitIt.next();
            String signature = s.toString();
            if(signature.contains("<") && signature.contains(">")){
	            signature = signature.substring(signature.indexOf('<'), signature.lastIndexOf('>') + 1);
	            if(signature.contains("loadAd(") || summary.containsKey(signature)) {
	            	//System.out.println("unit signature: " + signature);
	            	adUnitSet.add(s);
	            }
            }
        }
    	return adUnitSet;
    }
    
    public TNode<String> getAdUnitNode(List<TNode<String>> list, String str){
    	for(TNode<String> NewNode:list){
    		if(NewNode.getData().equals(str))
    			return NewNode;
    	}
    	System.out.println(str + " doesn't exist");
    	return null;
    }
    
    public String getAdUnitSignature(Unit u){
    	return u.toString().substring(u.toString().indexOf('<'), u.toString().lastIndexOf('>') + 1);
    }

    public StringCallGraph(){
    	
    }

    public StringCallGraph(CallGraph cg, Set<SootMethod> allmethods) {
        nodes = new HashSet<NewNode>();
        heads = new HashSet<NewNode>();
        nodesinscc = new HashSet<NewNode>();
        HashMap<SootMethod, NewNode> methodToNodeMap = new HashMap<SootMethod, NewNode>();
        for (SootMethod sm : allmethods) {
            NewNode n = new NewNode(sm);
            nodes.add(n);
            methodToNodeMap.put(sm, n);
            /*if(sm.getName().equals("requestNewInterstitial")){
            	System.out.println("Double check: " + sm.getSignature());
            }*/
        }
        for (NewNode n : nodes) {
            SootMethod sm = n.getMethod();
            Iterator sources = new Sources(cg.edgesInto(sm));
            while (sources.hasNext()) {
                SootMethod src = (SootMethod) sources.next();
                /*if(sm.getName().equals("requestNewInterstitial")){
                	System.out.println("sources: " + src.getSignature());
                }*/
                if (allmethods.contains(src)) {
                    NewNode p = methodToNodeMap.get(src);
                    if(!p.getMethod().getSignature().equals(n.getMethod().getSignature()))
                    {	
                    	p.addChild(n);
                    	n.addParent(p);

                    }
                    /*if(n.getMethod().getName().equals("requestNewInterstitial")){
                    	System.out.println("Parent: " + p.getMethod().getSignature());
                    	System.out.println("Child: " + n.getMethod().getSignature());
                    }*/
                    
                    
                }
                
            }
            Iterator<Edge> inEdges = cg.edgesInto(sm);
            while(inEdges.hasNext()){
            	Edge e = (Edge) inEdges.next();
            	n.addInEdge(e);
            	/*if(n.getMethod().getName().equals("loadAd")){
            		System.out.println("original: " + e);
            	}*/
            }
            Iterator<Edge> outEdges = cg.edgesOutOf(sm);
            while(outEdges.hasNext()){
            	Edge e = (Edge) outEdges.next();
            	n.addOutEdge(e);
            }
            
            /*Iterator<Edge> edges = cg.edgesInto(sm);
            while(edges.hasNext()){
            	Edge e = (Edge) edges.next();
            	System.out.println("src: " + e.getSrc() + "\tedge: " + e + "\ttgt: " + e.getTgt());
            }*/
        }
        for (NewNode n : nodes) {
            if (n.getIndgree() == 0)
                heads.add(n);
        }
        System.out.println("Before SCC Nodes size: " + nodes.size() + "\theads size: " + heads.size());
        //SCCdetection();
        SCCTarjanDetection();
        processSCC(cg, allmethods);
        System.out.println("After SCC Nodes size: " + nodes.size() + "\theads size: " + heads.size());
        System.out.println("NodesInSCC size: " + nodesinscc.size());
        RTOsorting();
        //System.out.println(RTOlist);
    }   
    
    //Tarjan's strongly connected components algorithm
    public static int index = 0;
    public static Stack<NewNode> stack = new Stack<NewNode>(); 
    public static List<List<NewNode>> allSCC = new ArrayList<List<NewNode>>();
    public void SCCTarjanDetection(){
    	for(NewNode n : nodes){
    		if(!n.OrderAssigned())
    		{
    			strongConnect(n);
    		}
    	}
    }    
    public void strongConnect(NewNode v){
    	v.SetLowLink(index);
    	v.SetOrder(index);
    	index++;
    	stack.push(v);
    	v.SetOnStack(true);
    	int min = v.GetLowLink();
    	for(NewNode w : v.getChildren()){
    		if(!w.OrderAssigned()){
    			strongConnect(w);
    		}
    		if(w.GetLowLink() < min){
    			min = w.GetLowLink();
    		}
    	}
    	if(min < v.GetLowLink()){
    		v.SetLowLink(min);
    		return;
    	}
    	List<NewNode> component = new ArrayList<NewNode>();
    	NewNode w;
    	do{
    		w = stack.pop();
    		component.add(w);
    		w.SetLowLink(nodes.size());
    	}while(w != v);
    	if(component.size() > 1){
    		for(NewNode n : component){
    			nodesinscc.add(n);
    		}
    		allSCC.add(component);
    	}
    	
    	/*v.SetOrder(index);
    	v.SetLowLink(index);
    	index = index + 1;
    	stack.push(v);
    	v.SetOnStack(true);
    	for(NewNode w : v.getChildren()){
    		if(!w.OrderAssigned()){
    			strongConnect(w);
    			v.SetLowLink(min(v.GetLowLink(), w.GetLowLink()));
    		}else if(w.isOnStack()){
    			v.SetLowLink(min(v.GetLowLink(), w.GetOrder()));
    		}
    	}
    	if(v.GetLowLink() == v.GetOrder()){
			int cnt = 0;
			NewNode sTop = stack.peek();			
			while (!stack.peek().equals(v)) {
                cnt++;
                NewNode w = stack.pop();
                w.SetOnStack(false);
                nodesinscc.add(w);
            }
			if (stack.peek().equals(v)) {
				NewNode n = stack.pop();
                if (cnt > 0) {
                    nodesinscc.add(n);
                }
            }
    	}*/
    }    
    /*public int min(int i, int j){
    	return (i <= j) ? i : j;
    }*/
    
    //strongly connect components
    public void SCCdetection() {
        Counter c = new Counter();
        Stack<NewNode> S = new Stack<NewNode>();
        Stack<NewNode> P = new Stack<NewNode>();
        for (NewNode n : heads) {
            if (n.getOutdgree() > 0)
                PathBased(n, c, S, P);
        }
    }
    
    private void PathBased(NewNode n, Counter c, Stack<NewNode> S, Stack<NewNode> P) {
        n.SetOrder(c.getCnt());
        c.inCreaseCounter();
        S.push(n);
        P.push(n);
        for (NewNode w : n.getChildren()) {
            if (!w.OrderAssigned()) {
                PathBased(w, c, S, P);
            } else if (!nodesinscc.contains(w)) {
                //NewNode top=P.pop();
                System.out.println("Strongly connected detected");
                System.out.println("**w**: " + w + " " + w.GetOrder());

                if(P.isEmpty()){
                	System.out.println("P size: 0");
                }else{
                while (!P.isEmpty() && P.peek().GetOrder() > w.GetOrder()) {
                    System.out.println("++P++: " + P.peek() + " " + P.peek().GetOrder());
                    P.pop();
                }
                }


            }
        }
        if(!P.isEmpty()){
        NewNode Ptop = P.peek();
        if (Ptop.equals(n)) {
            int cnt = 0;
            NewNode Stop = S.peek();
            while (!S.peek().equals(n)) {
                cnt++;
                NewNode fn = S.pop();
                if(cnt == 2){
                	nodesinscc.add(Stop);
                	nodesinscc.add(fn);
                }else if(cnt > 2){
                	nodesinscc.add(fn);
                }
            }
            if (S.peek().equals(n)) {
                if (cnt > 1) {
                    nodesinscc.add(S.pop());
                } else {
                    S.pop();
                }
            }
            P.pop();

        }
        }

        // there is one step of path-based SCC detection not implemented, it will not be used at this time, but we may want to implement
        // it some times in the future
    }
    
    public void processSCC(CallGraph cg, Set<SootMethod> allmethods){
    	for(List<NewNode> scc : allSCC){
	    	Set<NewNode> parents = new HashSet<NewNode>();
	    	Set<NewNode> children = new HashSet<NewNode>();
	    	for(NewNode n : scc){
	    		for(NewNode p : n.getParent()){
	    			if(!scc.contains(p)){
		    			parents.add(p);
		    			p.removeChild(n);
	    			}
	    		}
	    		for(NewNode c : n.getChildren()){
	    			if(!scc.contains(c)){
		    			children.add(c);
		    			c.removeParent(n);
	    			}
	    		}
	    		nodes.remove(n);
	    	}
	    	for(NewNode p : parents){
	    		for(NewNode c : children){
	    			p.addChild(c);
	    			c.addParent(p);
	    		}
	    	}
	    	heads = new HashSet<NewNode>();
	    	for (NewNode n : nodes) {
	            if (n.getIndgree() == 0)
	                heads.add(n);
	        }
    	}
    	/*Set<NewNode> tmp = new HashSet<NewNode>();
    	for(NewNode n : nodes){
    		tmp.add(n);
    	}
    	for (NewNode n : tmp) {
    		if(nodesinscc.contains(n)){
    			nodes.remove(n);
    			for(NewNode c : n.getChildren()){
    				c.removeParent(n);
    			}
    			for(NewNode p : n.getParent()){
    				p.removeChild(n);
    			}
    		}
        }
    	heads = new HashSet<NewNode>();
    	for (NewNode n : nodes) {
            if (n.getIndgree() == 0)
                heads.add(n);
        }*/
    }
    
    public void countAdMapping(CallGraph cg, Set<SootMethod> allmethods){
    	HashSet<NewNode> apis = new HashSet<NewNode>();
        //HashSet<NewNode> activities = new HashSet<NewNode>();
        for (SootMethod sm : allmethods) {
            if(sm.getName().equals("loadAd")){
            	System.out.println(sm.getSignature());
            	HashMap<String, Integer> ApiCallCount = new HashMap<String, Integer>();
            	Queue<SootMethod> queue = new LinkedList<SootMethod>();
            	Set<String> visited = new HashSet<String>();
            	Set<String> srcVisited = new HashSet<String>();
            	queue.add(sm);
            	while(!queue.isEmpty()){
            		SootMethod r = queue.poll();
            		visited.add(sm.getSignature());
            		Iterator<MethodOrMethodContext> sources = new Sources(cg.edgesInto(r));
            		if(!sources.hasNext()){
            			System.out.println("Entry method: " + r + "\tCount: " + ApiCallCount.get(r.getSignature()));
            		}
            		
            		//int i = 0;
                	while(sources.hasNext()){
                		SootMethod src = (SootMethod)sources.next();
                		//i++;
                		if(!visited.contains(src.getSignature())){
                			if(!ApiCallCount.containsKey(src.getSignature())){
                				ApiCallCount.put(src.getSignature(), 1);
                			}else{
                				ApiCallCount.put(src.getSignature(), ApiCallCount.get(src.getSignature()).intValue() + 1);
                			}
                		}
                		if(!srcVisited.contains(src.getSignature())){
                			queue.add(src);
                    		srcVisited.add(src.getSignature());
                		}
                	}
                	//System.out.println("inedges count:" + i);
            	}
            }
        }
    }
    
    public void getEntryMethod2Adapi(){
        for (NewNode n : nodes) {
            if (n.getMethod().getName().equals("loadAd")  && n.getIndgree() > 0 /*&& n.getMethod().getParameterTypes().get(0).toString().equals("com.google.android.gms.ads.AdRequest")*/)
            {
				System.out.println(n.toString());
				/*System.out.println("size: " + n.getInEdge().size());
				ListIterator<NewEdge> iter = n.getInEdge().listIterator();
				while(iter.hasNext()){
		    		NewEdge ne = iter.next();
		    		System.out.println(ne.getEdge());
		    	}*/
				
				ListIterator<NewEdge> iter = n.getInEdge().listIterator();
				while(iter.hasNext()){
					NewEdge ne = iter.next();
					VisitReverse(ne);
				}
			}
        }
        System.out.println("###########################");
        for (NewNode n : nodes) {
            if (n.getMethod().getName().equals("loadAd")  && n.getIndgree() > 0){
            	System.out.println(n.toString());
            	Set<NewNode> visited = new HashSet<NewNode>();
            	VisitReverse1(n,visited);            	
            }
        }
    }
    
    private void VisitReverse(NewEdge ne){
    	if(ne.isVisited()) return;
    	ne.setVisited();
    	if(ne.getSrc().getIndgree() == 0){
    		System.out.println("Entry method:\t" + ne.getSrc());
    	}else{
    		ListIterator<NewEdge> iter = ne.getSrc().getInEdge().listIterator();
        	while(iter.hasNext()){
        		NewEdge ne1 = iter.next();
        		VisitReverse(ne1);
    	    	//System.out.println(ne.toString());
    			//System.out.println("Visit Edge: " + ne.getEdge());
        	}
    	}
    	
    }
    
    private NewEdge nextNotVisited(ListIterator<NewEdge> iter){
    	while(iter.hasNext()){
    		NewEdge ne = iter.next();
    		if(!ne.isVisited()){
    			return ne;
    		}
    	}
    	return null;
    }
    
    private void VisitReverse1(NewNode n, Set<NewNode> visited) {
        if (visited.contains(n))
            return;
        visited.add(n);
        //System.out.println("***" + n);
        
        if(n.getIndgree() == 0){
        	System.out.println("Entry method:\t" + n);
        }else{
        	//visited.add(n);	//head nodes is not included in the set
	        for (NewNode w : n.getParent()) {
	        	VisitReverse1(w, visited);
	        }
        }
    }
    

    public void display() {
        //System.out.println(heads);
        System.out.println("=================================");
        for (NewNode n : nodes) {
            String sig = n.getMethod().getSignature();
            for (NewNode c : n.getChildren()) {
                String log = sig + "->" + c.getMethod().getSignature();
                System.out.println(log);
            }
        }
        System.out.println("=================================");
        System.out.println(nodesinscc);


    }
}
