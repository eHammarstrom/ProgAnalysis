package eda045f.exercises.flowgraph;

public interface DomainSet<T> {
	T bottom();
	T top();
	T merge(T t1, T t2);
}
