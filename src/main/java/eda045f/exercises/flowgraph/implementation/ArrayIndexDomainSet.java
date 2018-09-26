package eda045f.exercises.flowgraph.implementation;

import eda045f.exercises.flowgraph.ZSubsetDomain;

public class ArrayIndexDomainSet extends ZSubsetDomain {

	public ArrayIndexDomainSet(Integer min, Integer max) {
		super(min, max);
	}

	@Override
	protected Integer doMerge(Integer t1, Integer t2) {
		// Pick larger one for conservative
		return t1 <= t2 ? t2 : t1;
	}
}
