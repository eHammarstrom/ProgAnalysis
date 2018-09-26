package eda045f.exercises.flowgraph.implementation;

import java.util.HashMap;

import eda045f.exercises.flowgraph.AbstractFlowAnalysis;
import eda045f.exercises.flowgraph.ZSubsetDomain;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AssignStmt;
import soot.jimple.DivExpr;
import soot.jimple.IntConstant;
import soot.jimple.MulExpr;
import soot.jimple.NegExpr;
import soot.jimple.SubExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Chain;

public class ArrayIndexFlow extends AbstractFlowAnalysis<Value, Unit, Integer> {
	private final ArrayIndexStmtSwitch stmtSwitch = new ArrayIndexStmtSwitch();
	private final ArrayIndexValueSwitch valueSwitch = new ArrayIndexValueSwitch();
	private Chain<Local> locals;

	/**
	 * Holds values in Visitor Pattern
	 */
	private HashMap<Value, Integer> input;
	private HashMap<Value, Integer> output;
	private Integer res;

	public ArrayIndexFlow(DirectedGraph<Unit> graph, Chain<Local> locals, ZSubsetDomain aiad) {
		super(graph, aiad);
		this.locals = locals;
		doAnalysis();
	}

	@Override
	protected HashMap<Value, Integer> newInitialFlow() {
		HashMap<Value, Integer> aifm = new HashMap<Value, Integer>();
		locals.stream().forEach(v -> aifm.put(v, abstractDomain.top()));
		return aifm;
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
		@Override
		public void caseNegExpr(NegExpr v) {
			v.getOp().apply(this);
			res = abstractDomain.neg(res);
		}

		@Override
		public void caseAddExpr(AddExpr v) {
			v.getOp1().apply(this);
			Integer res1 = res;
			v.getOp2().apply(this);
			res = abstractDomain.add(res1, res);
		}
		
		@Override
		public void caseMulExpr(MulExpr v) {
			v.getOp1().apply(this);
			Integer res1 = res;
			v.getOp2().apply(this);
			res = abstractDomain.mul(res1, res);
		}

		@Override
		public void caseSubExpr(SubExpr v) {
			v.getOp1().apply(this);
			Integer res1 = res;
			v.getOp2().apply(this);
			res = abstractDomain.sub(res1, res);
		}
		
		@Override
		public void caseDivExpr(DivExpr v) {
			v.getOp1().apply(this);
			Integer res1 = res;
			v.getOp2().apply(this);
			res = abstractDomain.div(res1, res);
		}
		
		@Override
		public void caseLocal(Local v) {
			res = input.get(v);
		}

		@Override
		public void caseIntConstant(IntConstant v) {
			res = abstractDomain.constant(v.value);
		}

		@Override
		public void defaultCase(Object v) {
//			System.out.println("Not implemented for case: " + v);
			res = abstractDomain.bottom();
		}
	}

	@Override
	protected void doTransition(HashMap<Value, Integer> in, Unit d,
			HashMap<Value, Integer> out) {
		input = in;
		output = out;
		d.apply(stmtSwitch);
	}
}
