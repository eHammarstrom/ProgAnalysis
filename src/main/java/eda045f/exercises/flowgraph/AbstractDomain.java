package eda045f.exercises.flowgraph;

/**
 * @param <L> The underLying set on which the lattice is based.
 */
public abstract class AbstractDomain<L> {
	public abstract L merge(L o1, L o2);
	public abstract L add(L o1, L o2);
	public abstract L mul (L o1, L o2);
	public abstract L neg(L o);
	public abstract L bottom();
	public abstract L top();
	public abstract L fromInt(int v);
	
	public boolean eitherBottom(L o1, L o2) {
		return o1 == bottom() || o2 == bottom();
	}
	
	public boolean eitherTop(L o1, L o2) {
		return o1 == top() || o2 == top();
	}
}