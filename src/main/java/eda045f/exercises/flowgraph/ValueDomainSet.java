package eda045f.exercises.flowgraph;

public interface ValueDomainSet<T> extends DomainSet<T> {
	T add(T t1, T t2);
	T mul(T t1, T t2);
	T sub(T t1, T t2);
	T div(T t1, T t2);
	T neg(T t1);
	T constant(int c);
}
