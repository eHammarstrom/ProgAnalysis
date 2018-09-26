package eda045f.exercises.flowgraph;

import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.InverseGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Switchable;

public abstract class AbstractFlowAnalysis<V extends Switchable, N extends Switchable, T, S>
		extends ForwardFlowAnalysis<N, S> {
	
	protected DomainSet<T> abstractDomain;

	public AbstractFlowAnalysis(DirectedGraph<N> graph, DomainSet<T> abstractDomain, boolean forward) {
		super(forward ? graph : new InverseGraph<>(graph));
		this.abstractDomain = abstractDomain;
	}
	
}
