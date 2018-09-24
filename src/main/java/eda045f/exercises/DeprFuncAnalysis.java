package eda045f.exercises;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;

public class DeprFuncAnalysis extends BodyTransformer {

	private boolean isMethodDeprecated(SootMethod m) {
		for (Tag t : m.getTags()) {
			if (!(t instanceof VisibilityAnnotationTag))
				continue;
			for (AnnotationTag at : ((VisibilityAnnotationTag) t).getAnnotations()) {
				if (at.getType().equals("Ljava/lang/Deprecated;"))
					return true;
			}
		}
		return false;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		Iterator<Unit> itr = b.getUnits().iterator();

		while (itr.hasNext()) {
			Stmt u = (Stmt) itr.next();
			if (!u.containsInvokeExpr())
				continue;

			SootMethod d = u.getInvokeExpr().getMethod();
			if (isMethodDeprecated(d)) {
				System.out.println("DEP: " + b.getMethod().getSignature() + " at " + u.getJavaSourceStartLineNumber()
						+ " calls deprecated: " + d.getSignature());
			}
		}
	}
}
