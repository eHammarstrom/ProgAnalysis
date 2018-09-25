package eda045f.exercises;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.ArithmeticConstant;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArrayFlowUniverse;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.util.Chain;

public class ArrayIndexFuncAnalysis extends BodyTransformer {

	private static ArrayList<ArrayRef> getArrayRefsStmt(Stmt s) {
		ArrayList<ArrayRef> arefsStmt = new ArrayList<ArrayRef>();
		if (s.containsArrayRef())
			arefsStmt.add(s.getArrayRef());
		return arefsStmt;
	}

	private static ArrayList<ArrayRef> getArrayRefs(Body b) {
		ArrayList<ArrayRef> arefs = new ArrayList<>();
		b.getUnits().stream().forEach(u -> arefs.addAll(getArrayRefsStmt((Stmt) u)));
		return arefs;
	}

	private static ArrayList<ArrayRef> getConstantRef(ArrayList<ArrayRef> arefs) {
		return (ArrayList<ArrayRef>) arefs.stream().filter(ar -> {
			return ar.getIndex() instanceof ArithmeticConstant;
		}).collect(Collectors.toList());
	}

	private static ArrayList<ArrayRef> getNonConstantRef(ArrayList<ArrayRef> arefs, ArrayList<ArrayRef> acrefs) {
		ArrayList<ArrayRef> nonConstants = new ArrayList<>(arefs);
		nonConstants.removeAll(acrefs);
		return nonConstants;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		Chain<Local> locals = b.getLocals();
		ArrayIndexForwardFlow fg = new ArrayIndexForwardFlow(new BriefUnitGraph(b), locals);
		b.getUnits().stream().forEach(l -> System.out.println(l + ": \n\t" + fg.getFlowBefore(l)));
	}

	public static enum ArrayIndexElement {
		ATop, ANegative, AZero, APositive, ABottom
	}

	public static class ArrayIndexUniverse extends ArrayFlowUniverse<ArrayIndexElement> {
		public ArrayIndexUniverse(ArrayIndexElement[] elements) {
			super(elements);
		}

		public static ArrayIndexElement fromInt(int x) {
			if (x > 0)
				return ArrayIndexElement.APositive;
			if (x == 0)
				return ArrayIndexElement.AZero;
			return ArrayIndexElement.ANegative;
		}

		private static boolean differentSigns(ArrayIndexElement a1, ArrayIndexElement a2) {
			return (a1 == ArrayIndexElement.ANegative && a2 == ArrayIndexElement.APositive)
					|| (a2 == ArrayIndexElement.ANegative && a1 == ArrayIndexElement.APositive);
		}

		public static ArrayIndexElement add(ArrayIndexElement a1, ArrayIndexElement a2) {
			if (a1 == ArrayIndexElement.ABottom || a2 == ArrayIndexElement.ABottom)
				return ArrayIndexElement.ABottom;
			if (a1 == ArrayIndexElement.ATop || a2 == ArrayIndexElement.ATop)
				return ArrayIndexElement.ABottom;
			if (a1 == ArrayIndexElement.AZero)
				return a2;
			if (a2 == ArrayIndexElement.AZero)
				return a1;
			if (differentSigns(a1, a2))
				return ArrayIndexElement.ABottom;
			return a1;
		}

		public static ArrayIndexElement merge(ArrayIndexElement a1, ArrayIndexElement a2) {
			if (a1 == ArrayIndexElement.ABottom || a2 == ArrayIndexElement.ABottom)
				return ArrayIndexElement.ABottom;
			if (a1 == ArrayIndexElement.ATop)
				return a2;
			if (a2 == ArrayIndexElement.ATop)
				return a1;
			if (a1 == ArrayIndexElement.AZero)
				return a2;
			if (a2 == ArrayIndexElement.AZero)
				return a1;
			if (differentSigns(a1, a2))
				return ArrayIndexElement.ABottom;
			return a1;
		}
	}

	@SuppressWarnings("serial")
	public static class ArrayIndexFlowMap extends HashMap<Value, ArrayIndexElement> {
	}

	public static class ArrayIndexForwardFlow extends ForwardFlowAnalysis<Unit, ArrayIndexFlowMap> {

		private Chain<Local> locals;

		public ArrayIndexForwardFlow(DirectedGraph<Unit> graph, Chain<Local> locals) {
			super(graph);
			this.locals = locals;
			doAnalysis();
		}

		private ArrayIndexElement handleBinop(ArrayIndexFlowMap in, BinopExpr be) {
			if (be instanceof AddExpr) {
				return ArrayIndexUniverse.add(handleUnaryOp(in, be.getOp1()), handleUnaryOp(in, be.getOp2()));
			}
			
			/**
			 * TODO: Implement merge of other binary arithmetic expressions.
			 */
			return ArrayIndexElement.ABottom;
		}

		private ArrayIndexElement handleUnaryOp(ArrayIndexFlowMap in, Value op) {
			if (op instanceof IntConstant) {
				return ArrayIndexUniverse.fromInt(((IntConstant) op).value);
			} else if (op instanceof Local) {
				return in.get(op);
			} else {
				System.out.println("NOT IMPLEMENTED, DEFAULT TO BOTTOM: " + op);
				return ArrayIndexElement.ABottom;
			}
		}

		@Override
		protected void flowThrough(ArrayIndexFlowMap in, Unit d, ArrayIndexFlowMap out) {
			System.out.println("------------------------");
			System.out.println(d);
			System.out.println(in);
			copy(in, out);
			Stmt s = (Stmt) d;
			s.getDefBoxes().stream().forEach(vb -> {
				DefinitionStmt def = (DefinitionStmt) s;
				Value rhs = def.getRightOp();

				if (rhs instanceof BinopExpr) {
					out.put(vb.getValue(), handleBinop(in, (BinopExpr) rhs));
				} else if (rhs instanceof IntConstant) {
					out.put(vb.getValue(), handleUnaryOp(in, rhs));
				} else if (rhs instanceof Local) {
					out.put(vb.getValue(), handleUnaryOp(in, rhs));
				} else {
					System.out.println("NOT IMPLEMENTED, DEFAULT TO BOTTOM: " + s);
					out.put(vb.getValue(), ArrayIndexElement.ABottom);
				}
			});
			System.out.println(out);
			System.out.println("------------------------");
		}

		@Override
		protected ArrayIndexFlowMap newInitialFlow() {
			ArrayIndexFlowMap aifm = new ArrayIndexFlowMap();
			locals.stream().forEach(v -> aifm.put(v, ArrayIndexElement.ATop));
			return aifm;
		}

		@Override
		protected void merge(ArrayIndexFlowMap in1, ArrayIndexFlowMap in2, ArrayIndexFlowMap out) {
			System.out.println("------------------------");
			System.out.println("Merge: ");
			System.out.println("in1: " + in1);
			System.out.println("in2: " + in2);
			in1.keySet().stream().forEach(v -> out.put(v, ArrayIndexUniverse.merge(in1.get(v), in2.get(v))));
			System.out.println("out: " + out);
			System.out.println("------------------------");
		}

		@Override
		protected void copy(ArrayIndexFlowMap source, ArrayIndexFlowMap dest) {
			source.keySet().stream().forEach(v -> dest.put(v, source.get(v)));
		}
	}
}
