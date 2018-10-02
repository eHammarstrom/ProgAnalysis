package eda045f.exercises.flowgraph.implementation;

import java.util.HashSet;
import java.util.Set;

import eda045f.exercises.flowgraph.AbstractFlowAnalysis;
import eda045f.exercises.flowgraph.DomainSet;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ReturnStmt;
import soot.toolkits.graph.DirectedGraph;

public class LiveFlow extends AbstractFlowAnalysis<Unit, Unit, Set<Value>, Set<Value>>{
	public LiveFlow(DirectedGraph<Unit> graph, DomainSet<Set<Value>> abstractDomain) {
		super(graph, abstractDomain, false);
		doAnalysis();
	}

	@Override
    protected void flowThrough(Set<Value> in, Unit d, Set<Value> out) {
		copy(in, out);
        // Kill
		d.getDefBoxes().stream().forEach(vb -> out.remove(vb.getValue()));
        // Gen
		d.getUseBoxes().stream().forEach(vb -> {
			Set<Value> genset = new HashSet<>();
			LiveFlowValueSwitch sw = new LiveFlowValueSwitch(genset);
			vb.getValue().apply(sw);
			copy(genset, out);
		});
	}
	
	private void unpack(Value v, LiveFlowValueSwitch sw) {
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
	protected void copy(Set<Value> source, Set<Value> dest) { dest.addAll(source); }
	
	protected class LiveFlowValueSwitch extends AbstractJimpleValueSwitch {
		private Set<Value> sv;
		public LiveFlowValueSwitch(Set<Value> sv) {
			this.sv = sv;
		}
		
		@Override
		public void caseLocal(Local v) { sv.add(v); }

		@Override
		public void defaultCase(Object v) { unpack((Value)v, this); }
	}
}
