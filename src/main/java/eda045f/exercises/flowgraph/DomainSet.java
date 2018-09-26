package eda045f.exercises.flowgraph;

import java.util.List;

public interface DomainSet<T> {
	List<T> getOrdered();
	T bottom();
	T top();
	T merge(T t1, T t2);
	T add(T t1, T t2);
	T mul(T t1, T t2);
	T sub(T t1, T t2);
	T div(T t1, T t2);
	T neg(T t1);
	T constant(int c);
	T unknown();
}
