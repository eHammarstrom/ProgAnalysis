package eda045f.exercises.flowgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Switchable;

public abstract class AbstractFlowAnalysis<V extends Switchable, N extends Switchable, T>
		extends ForwardFlowAnalysis<N, HashMap<V, T>> {
	protected DomainSet<T> abstractDomain;

	public AbstractFlowAnalysis(DirectedGraph<N> graph, DomainSet<T> abstractDomain) {
		super(graph);
		this.abstractDomain = abstractDomain;
	}

	@Override
	protected void copy(HashMap<V, T> source, HashMap<V, T> dest) {
		source.keySet().stream().forEach(v -> dest.put(v, source.get(v)));
	}

	protected abstract void doTransition(HashMap<V, T> in, N d, HashMap<V, T> out);

	@Override
	protected void flowThrough(HashMap<V, T> in, N d, HashMap<V, T> out) {
//		System.out.println("----------------------------------------------------");
//		System.out.println(in);
//		
		copy(in, out);
		doTransition(in, d, out);
//		System.out.println(out);
//		System.out.println("----------------------------------------------------");
	}

	@Override
	protected void merge(HashMap<V, T> in1, HashMap<V, T> in2, HashMap<V, T> out) {
//		System.out.println("----------------------------------------------------");
//		System.out.println(in1);
//		System.out.println(in2);
		Set<V> ks = new HashSet<V>();

		in1.keySet().stream().forEach(v -> ks.add(v));
		in2.keySet().stream().forEach(v -> ks.add(v));

		ks.stream().forEach(v -> {
			if (!in1.containsKey(v))
				out.put(v, in2.get(v));
			else if (!in2.containsKey(v))
				out.put(v, in1.get(v));
			else {
				out.put(v, abstractDomain.merge(in1.get(v), in2.get(v)));
			}
		});
//		System.out.println(out);
//		System.out.println("----------------------------------------------------");
	}
}