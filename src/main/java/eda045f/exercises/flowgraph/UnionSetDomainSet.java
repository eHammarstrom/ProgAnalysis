package eda045f.exercises.flowgraph;

import java.util.HashSet;
import java.util.Set;

public abstract class UnionSetDomainSet<T> extends SetDomainSet<T> {
	@Override
	public Set<T> merge(Set<T> t1, Set<T> t2) {
		Set<T> s = new HashSet<>(t1);
		t2.stream().forEach(t -> s.add(t));
		return s;
	}
}
