package eda045f.exercises.flowgraph;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;
import soot.util.Switchable;

public abstract class AbstractFlowAnalysis<V extends Switchable, N extends Switchable, T, A> {
	/**
	 * NOTE:
	 * 		Interface inspired by Soot FlowAnalysis
	 */
	protected DomainSet<T> abstractDomain;
    private DirectedGraph<N> graph;

    private HashMap<N, A> flowMap = new HashMap<N, A>();
    private Stack<N> workList = new Stack<N>();
    

	public AbstractFlowAnalysis(DirectedGraph<N> graph, DomainSet<T> abstractDomain, boolean forward) {
		this.abstractDomain = abstractDomain;
        this.graph = forward ? graph : new InverseGraph<>(graph);
	}

    protected abstract void copy(A src, A dst);
    protected abstract A newInitialFlow();

    public A getFlowBefore(N inSet) {
    	return getIn(inSet);
    }

    public A getFlowAfter(N inSet) {
    	return flowMap.get(inSet);
    }
    
    protected abstract void flowThrough(A in, N node, A out);
    protected abstract void merge(A in1, A in2, A out);

    private A doMerge(List<A> preds) {
    	if(preds.size() == 0) return newInitialFlow();
    	if(preds.size() == 1) return preds.remove(0);
    	A out = newInitialFlow();
    	A l = preds.remove(0);
    	A r = preds.remove(0);
    	merge(l, r, out);
    	preds.add(out);
    	return doMerge(preds);
    }
    
    private A getIn(N node) {
    	List<A> fn = graph.getPredsOf(node).stream().map(n -> flowMap.get(n)).collect(Collectors.toList());
		return doMerge(fn);
    }

    protected void doAnalysis() {
    	graph.iterator().forEachRemaining(n -> {
    		flowMap.put(n, newInitialFlow());
    		workList.add(n);
    	});
    	
    	while(!workList.isEmpty()) {
    		N current = workList.pop();
    		A in = getIn(current);
    		A out = newInitialFlow();
    		
    		flowThrough(in, current, out);
    		
    		if(!out.equals(flowMap.get(current))) {
    			flowMap.put(current, out);
    			workList.addAll(graph.getSuccsOf(current));
    		}
    	}
    }
}
