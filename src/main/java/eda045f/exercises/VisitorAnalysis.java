package eda045f.exercises;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.AddExpr;

public class VisitorAnalysis extends BodyTransformer {

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		b.getUnits().stream().forEach(u -> u.getUseBoxes().forEach(use -> use.getValue().apply(new ASwitch())));
		//b.getUnits().forEach(u -> u.app);
	}

	private static class ASwitch extends AbstractJimpleValueSwitch {
		@Override
		public void caseAddExpr(AddExpr v) {
			System.out.println("Found switch AddExpr: " + v);
			super.caseAddExpr(v);
		}
	}
}
