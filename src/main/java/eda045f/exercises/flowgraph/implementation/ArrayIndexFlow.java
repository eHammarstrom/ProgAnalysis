package eda045f.exercises.flowgraph.implementation;

import java.util.HashMap;

import eda045f.exercises.flowgraph.AbstractValueFlowAnalysis;
import eda045f.exercises.flowgraph.ValueDomainSet;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Chain;

public class ArrayIndexFlow extends AbstractValueFlowAnalysis<Unit, Integer> {
	private Chain<Local> locals;

	public ArrayIndexFlow(DirectedGraph<Unit> graph, Chain<Local> locals, ValueDomainSet<Integer> aiad) {
		super(graph, aiad, true);
		this.locals = locals;
		doAnalysis();
	}

	@Override
	protected HashMap<Value, Integer> newInitialFlow() {
		HashMap<Value, Integer> aifm = new HashMap<Value, Integer>();
		locals.stream().forEach(v -> aifm.put(v, abstractDomain.top()));
		return aifm;
	}

}
