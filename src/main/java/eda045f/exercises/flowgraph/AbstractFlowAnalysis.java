package eda045f.exercises.flowgraph;

import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Switchable;

public abstract class AbstractFlowAnalysis<V extends Switchable, N extends Switchable, T, A>
		extends ForwardFlowAnalysis<N, A> {
	
	protected DomainSet<T> abstractDomain;

	public AbstractFlowAnalysis(DirectedGraph<N> graph, DomainSet<T> abstractDomain, boolean forward) {
		super(forward ? graph : new InverseGraph<>(graph));
		this.abstractDomain = abstractDomain;
	}

    /* protected abstract void copy(A src, A dst); */
    /* protected abstract A newInitialFlow(); */
    /*  */
    /* public A getFlowBefore(N inSet) { */
    /*  */
    /* } */
    /*  */
    /* public A getFlowAfter(N inSet) { */
    /*  */
    /* } */
    /* public void flowThrough(A in, N node, A out); */
    /* public void merge(A in1, A in2, A out); */
    /*  */
    /*  */
    /* protected void doAnalysis() { */
    /*  */
    /* } */
}
