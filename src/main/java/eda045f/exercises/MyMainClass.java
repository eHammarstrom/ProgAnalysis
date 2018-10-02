package eda045f.exercises;

//v2: preserve input line numbers, preload more signatures, more comments
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

/**
* Stub for implementing Soot analyses.
*
* This implementation delegates most initialisation to the Soot main method.  Alternatively,
* you can create the scene object by hand and configure it suitably, see:
*
*   https://stackoverflow.com/questions/12703500/is-it-possible-to-use-the-soot-analyses-without-calling-soot-main-main
*/
public class MyMainClass {
	// Disable most of Soot, especially its backend and more advanced analyses
	//final static String DEFAULT_SETUP_ARGS = "-p jb use-original-names:true -p bop enabled:off -p bb enabled:off -p cg enabled:off -p jop enabled:off -p jap enabled:off -p tag enabled:off -p sop enabled:off -p jb.tr ignore-nullpointer-dereferences";
	final static String DEFAULT_SETUP_ARGS = "-keep-line-number -p jb use-original-names:true -p bop "
										   + "enabled:off -p bb enabled:off -p cg enabled:off -p jop enabled:off -p jap enabled:off "
										   + "-p tag enabled:off -p sop enabled:off -p jb.tr ignore-nullpointer-dereferences "
										   + "-p jb preserve-source-annotations "
										   + "-p jb.uce enabled:off -p jb.dae enabled:off -p jb.ule enabled:off "
										   + "-p jj enabled:off";
	
	//  -p jb.dae enabled:off -p jb.ule enabled:off

	// These classes are preloaded to avoid spurious Soot errors.
	// I am not 100% clear on why these exact classes are needed...
	final static String[] PRELOAD_SIGNATURES = new String[] {
		"java.lang.LinkageError",
		"junit.framework.Assert",
		"java.util.AbstractQueue",
		"java.lang.ReflectiveOperationException",
		"java.util.AbstractCollection"
	};

	public static void main(String[] args) {
		System.out.println("MyMainClass Entry");
		boolean set_output = false;
		// If you pass the `-f' parameter, you want to generate output (jimple or grimp, probably), so we test for that...
		for (String arg : args) {
			if (arg.equals("-f")) {
				set_output = true;
			}
		}
		String setup_args = DEFAULT_SETUP_ARGS;
		if (!set_output) {
			// ...and if you didn't explicitly ask for output, we turn it off.
			setup_args = "-f n " + DEFAULT_SETUP_ARGS; // disable output unless output is specified
		}
		Scene.v().setSootClassPath(null); // ensures that we load the class path from the command line
		for (String sig : PRELOAD_SIGNATURES) {
			Scene.v().addBasicClass(sig, SootClass.SIGNATURES);
		}

		// Install your analysis, which is now executed (by default) on everything passed in the Soot classpath.
//		PackManager.v().getPack("jtp").add(new Transform("jtp.EDA045F.MainFuncAnalysis", new MainFuncAnalysis()));
//		PackManager.v().getPack("jtp").add(new Transform("jtp.EDA045F.DeprFuncAnalysis", new DeprFuncAnalysis()));
		PackManager.v().getPack("jtp").add(new Transform("jtp.EDA045F.ArrayIndexFuncAnalysis", new ArrayIndexFuncAnalysis()));
//		PackManager.v().getPack("jtp").add(new Transform("jtp.EDA045F.LiveVarAnalysis", new LiveVarAnalysis()));

		
		// Now call the Soot infrastructure with our analysis plugged in
		// default args:
		List<String> argsList = new ArrayList<String>(Arrays.asList(setup_args.split(" ")));
		// actual command-line args:
		argsList.addAll(new ArrayList<String>(Arrays.asList(args)));
		soot.Main.main(argsList.toArray(new String[]{}));
	}

}
