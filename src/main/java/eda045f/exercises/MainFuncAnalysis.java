package eda045f.exercises;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.SootMethod;

public class MainFuncAnalysis extends BodyTransformer {
	@Override
	protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
		SootMethod m = body.getMethod();
		if (m.isPublic() && m.isStatic() && m.getName().equals("main") && m.getParameterCount() == 1
				&& m.getParameterType(0).toQuotedString().equals("java.lang.String[]")) {
			System.out.println("Decl: " + m.getDeclaration() + ", Sig: " + m.getSignature());
			assert (m.isMain());
		}
	}
}