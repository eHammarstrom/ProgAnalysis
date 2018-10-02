package eda045f.exercises;

import java.util.ArrayList;
import java.util.Map;

import org.javatuples.Pair;

import eda045f.exercises.flowgraph.implementation.ArrayIndexDomainSet;
import eda045f.exercises.flowgraph.implementation.ArrayIndexFlow;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootMethod;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.util.Chain;

public class ArrayIndexFuncAnalysis extends BodyTransformer {

	private static ArrayList<Pair<ArrayRef, Stmt>> getArrayRefsStmt(Stmt s) {
		ArrayList<Pair<ArrayRef, Stmt>> arefsStmt = new ArrayList<Pair<ArrayRef, Stmt>>();
		if (s.containsArrayRef())
			arefsStmt.add(new Pair<ArrayRef, Stmt>(s.getArrayRef(), s));
		return arefsStmt;
	}

	private static ArrayList<Pair<ArrayRef, Stmt>> getArrayRefs(Body b) {
		ArrayList<Pair<ArrayRef, Stmt>> arefs = new ArrayList<>();
		b.getUnits().stream().forEach(u -> arefs.addAll(getArrayRefsStmt((Stmt) u)));
		return arefs;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		Chain<Local> locals = b.getLocals();
		ArrayIndexDomainSet aiad = new ArrayIndexDomainSet(-10, 10);
		ArrayIndexFlow fg = new ArrayIndexFlow(new CompleteUnitGraph(b), locals, aiad);
		getArrayRefs(b).stream().forEach(pca -> pca.getValue0().getIndex()
				.apply(new ArrayIndexCheckSwitch(aiad, fg, pca.getValue1(), b.getMethod())));
	}

	protected class ArrayIndexCheckSwitch extends AbstractJimpleValueSwitch {
		private ArrayIndexFlow fg;
		private Stmt s;
		private ArrayIndexDomainSet aiad;
		private SootMethod m;

		public ArrayIndexCheckSwitch(ArrayIndexDomainSet aiad, ArrayIndexFlow fg, Stmt s, SootMethod m) {
			this.fg = fg;
			this.aiad = aiad;
			this.s = s;
			this.m = m;
		}

		@Override
		public void caseLocal(Local v) {
			if (aiad.negative(fg.getFlowBefore(s).get(v)))
				System.out.println("NBV " + m.getSignature() + " "
						+ s.getJavaSourceStartLineNumber() + " VARIABLE");
		}

		@Override
		public void caseIntConstant(IntConstant v) {
			if (v.value < 0) {
				System.out.println("NBV " + m.getSignature() + " "
						+ s.getJavaSourceStartLineNumber() + " CONSTANT");
			}
		}

		@Override
		public void defaultCase(Object v) {
			System.err.println("Weird Case: " + v);
		}
	}
}
