package eda045f.exercises;

import java.util.Map;

import eda045f.exercises.flowgraph.implementation.LiveFlow;
import eda045f.exercises.flowgraph.implementation.LiveFlowDomain;
import soot.Body;
import soot.BodyTransformer;
import soot.toolkits.graph.CompleteUnitGraph;

public class LiveVarAnalysis extends BodyTransformer {
	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		LiveFlowDomain ld = new LiveFlowDomain();
		LiveFlow fg = new LiveFlow(new CompleteUnitGraph(b), ld);
		
		b.getUnits().stream().forEach(u -> {
			System.out.println(u + ":");
			System.out.println("\t" + fg.getFlowBefore(u));
			System.out.println("\t" + fg.getFlowAfter(u));
		});


        // Unit: [  [ y <- ... ], ... [ ]  ]
	}
}
