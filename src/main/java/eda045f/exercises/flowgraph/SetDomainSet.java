package eda045f.exercises.flowgraph;

import java.util.HashSet;
import java.util.Set;

public abstract class SetDomainSet<T> implements DomainSet<Set<T>> {
	private Set<T> bottom = new HashSet<>();
	private Set<T> top = new HashSet<>();
	
	@Override
	public Set<T> bottom() {
		return bottom;
	}

	@Override
	public Set<T> top() {
		return top;
	}
}
