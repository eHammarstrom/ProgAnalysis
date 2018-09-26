package eda045f.exercises.flowgraph.implementation;

import java.util.HashSet;
import java.util.Set;

import eda045f.exercises.flowgraph.AbstractFlowAnalysis;
import eda045f.exercises.flowgraph.DomainSet;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.toolkits.graph.DirectedGraph;

public class LiveFlow extends AbstractFlowAnalysis<Unit, Unit, Set<Value>, Set<Value>>{
	public LiveFlow(DirectedGraph<Unit> graph, DomainSet<Set<Value>> abstractDomain) {
		super(graph, abstractDomain, false);
		doAnalysis();
	}

	@Override
	protected void flowThrough(Set<Value> in, Unit d, Set<Value> out) {
		System.out.println(in);
		copy(in, out);
		d.getDefBoxes().stream().forEach(vb -> out.remove(vb.getValue()));
		d.getUseBoxes().stream().forEach(vb -> {
			Set<Value> up = new HashSet<>();
			ArrayIndexValueSwitch sw = new ArrayIndexValueSwitch(up);
			unpack(vb.getValue(), sw);
			copy(up, out);
		});
		System.out.println(out);
	}
	
	private void unpack(Value v, ArrayIndexValueSwitch sw) {
		v.getUseBoxes().stream().forEach(vb -> vb.getValue().apply(sw));
	}

	@Override
	protected Set<Value> newInitialFlow() {
		return new HashSet<>();
	}

	@Override
	protected void merge(Set<Value> in1, Set<Value> in2, Set<Value> out) {
		copy(abstractDomain.merge(in1, in2), out);
	}

	@Override
	protected void copy(Set<Value> source, Set<Value> dest) {
		source.stream().forEach(v -> dest.add(v));
	}
	
	protected class ArrayIndexValueSwitch extends AbstractJimpleValueSwitch {
		private Set<Value> sv;
		public ArrayIndexValueSwitch(Set<Value> sv) {
			this.sv = sv;
		}
		
		@Override
		public void caseLocal(Local v) {
			sv.add(v);
		}

		@Override
		public void defaultCase(Object v) {
			unpack((Value)v, this);
		}
	}
}