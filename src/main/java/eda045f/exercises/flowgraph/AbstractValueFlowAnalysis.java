package eda045f.exercises.flowgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.javatuples.Pair;

import soot.Local;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.DivExpr;
import soot.jimple.IntConstant;
import soot.jimple.MulExpr;
import soot.jimple.NegExpr;
import soot.jimple.SubExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Switchable;

public abstract class AbstractValueFlowAnalysis<N extends Switchable, T> extends AbstractFlowAnalysis<Value, N, T, HashMap<Value, T>> {
	private final ArrayIndexStmtSwitch stmtSwitch = new ArrayIndexStmtSwitch();
	private final ArrayIndexValueSwitch valueSwitch = new ArrayIndexValueSwitch();
	private HashMap<Value, T> input;
	private HashMap<Value, T> output;
	private T res;
	
	public AbstractValueFlowAnalysis(DirectedGraph<N> graph, ValueDomainSet<T> abstractDomain, boolean forward) {
		super(graph, abstractDomain, forward);
	}
	
	@Override
	protected void copy(HashMap<Value, T> source, HashMap<Value, T> dest) {
		source.keySet().stream().forEach(v -> dest.put(v, source.get(v)));
	}

	@Override
	protected void flowThrough(HashMap<Value, T> in, N d, HashMap<Value, T> out) {
//		System.out.println("----------------------------------------------------");
//		System.out.println(in);
//		
		copy(in, out);
		input = in;
		output = out;
		d.apply(stmtSwitch);
//		System.out.println(out);
//		System.out.println("----------------------------------------------------");
	}

	@Override
	protected void merge(HashMap<Value, T> in1, HashMap<Value, T> in2, HashMap<Value, T> out) {
//		System.out.println("----------------------------------------------------");
//		System.out.println(in1);
//		System.out.println(in2);
		Set<Value> ks = new HashSet<Value>();

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

	protected class ArrayIndexStmtSwitch extends AbstractStmtSwitch {
		@Override
		public void caseAssignStmt(AssignStmt stmt) {
			stmt.getDefBoxes().stream().forEach(vb -> {
				stmt.getRightOp().apply(valueSwitch);
				output.put(vb.getValue(), res);
			});
		}
	}

	protected class ArrayIndexValueSwitch extends AbstractJimpleValueSwitch {
		
		private Pair<T, T> handleBinop(BinopExpr v) {
			v.getOp1().apply(this);
			T res1 = res;
			v.getOp2().apply(this);
			return new Pair<>(res1, res);
		}
		
		@Override
		public void caseNegExpr(NegExpr v) {
			v.getOp().apply(this);
			res = ((ValueDomainSet<T>)abstractDomain).neg(res);
		}

		@Override
		public void caseAddExpr(AddExpr v) {
			Pair<T, T> p = handleBinop(v);
			res = ((ValueDomainSet<T>)abstractDomain).add(p.getValue0(), p.getValue1());
		}

		@Override
		public void caseMulExpr(MulExpr v) {
			Pair<T, T> p = handleBinop(v);
			res = ((ValueDomainSet<T>)abstractDomain).mul(p.getValue0(), p.getValue1());
		}

		@Override
		public void caseSubExpr(SubExpr v) {
			Pair<T, T> p = handleBinop(v);
			res = ((ValueDomainSet<T>)abstractDomain).sub(p.getValue0(), p.getValue1());
		}

		@Override
		public void caseDivExpr(DivExpr v) {
			Pair<T, T> p = handleBinop(v);
			res = ((ValueDomainSet<T>)abstractDomain).div(p.getValue0(), p.getValue1());
		}

		@Override
		public void caseLocal(Local v) {
			res = input.get(v);
		}

		@Override
		public void caseIntConstant(IntConstant v) {
			res = ((ValueDomainSet<T>)abstractDomain).constant(v.value);
		}

		@Override
		public void defaultCase(Object v) {
//			System.out.println("Not implemented for case: " + v);
			res = abstractDomain.bottom();
		}
	}
}
