package eda045f.exercises.flowgraph;

public abstract class ZSubsetDomain implements ValueDomainSet<Integer> {
	protected Integer min;
	protected Integer max;
	protected Integer bottom = Integer.MIN_VALUE;
	protected Integer top    = Integer.MAX_VALUE;
	
	
	public ZSubsetDomain(Integer min, Integer max) {
		assert(min < 0);
		assert(max > 0);
		this.min = min;
		this.max = max;
	}

	protected boolean inSpecific(Integer x) {
		return min <= x && x <= max;
	}
	
	protected boolean largePositive(Integer x) {
		return x == lp();
	}
	
	protected boolean largeNegative(Integer x) {
		return x == ln();
	}
	
	protected boolean large(Integer x) {
		return largePositive(x) || largeNegative(x);
	}
	
	protected Integer lp() {
		return max + 1;
	}
	
	protected Integer ln() {
		return min - 1;
	}
	
	protected Integer doRound(Integer t) {
		if(t <= ln()) return ln();
		if(t >= lp()) return lp();
		return t;
	}
	
	public boolean negative(Integer t) {
		return t < 0 && t != bottom();
	}
	
	public boolean positive(Integer  t) {
		return t > 0 && t != top();
	}
	
	protected boolean oppositeLarge(Integer t1, Integer t2) {
		return (largePositive(t1) && largeNegative(t2)) || (largePositive(t2) && largeNegative(t1));
	}
	
	protected boolean oppositeSigns(Integer t1, Integer t2) {
		return negative(t1) && positive(t2) || (negative(t2) && positive(t1));
	}

	@Override
	public Integer bottom() {
		return bottom;
	}

	@Override
	public Integer top() {
		return top;
	}
	
	protected abstract Integer doMerge(Integer t1, Integer t2);
	
	@Override
	public Integer merge(Integer t1, Integer t2) {
		if(t1 == bottom() || t2 == bottom()) return bottom();
		if(t1 == top()) return t2;
		if(t2 == top()) return t1;
		if(oppositeLarge(t1, t2)) return bottom();
		return doMerge(t1, t2);
	}
	
	@Override
	public Integer div(Integer t1, Integer t2) {
		if(t1 == 0) return 0;
		if(t2 == 0) return bottom();
		if(t1 == bottom() || t2 == bottom()) return bottom();
		if(t1 == top() || t2 == top()) return top();
		if(oppositeLarge(t1, t2)) return ln();
		if(large(t1) && large(t2)) return lp();
		return doRound(t1 / t2);
	}
	
	@Override
	public Integer mul(Integer t1, Integer t2) {
		if(t1 == 0 || t2 == 0) return 0;
		if(t1 == bottom() || t2 == bottom()) return bottom();
		if(t1 == top() || t2 == top()) return top();
		if(oppositeLarge(t1, t2)) return ln();
		return doRound(t1 * t2);
	}
	
	@Override
	public Integer add(Integer t1, Integer t2) {
		if(t1 == bottom() || t2 == bottom()) return bottom();
		if(t1 == top() || t2 == top()) return top();
		if(t1 == 0) return t2;
		if(t2 == 0) return t1;
		if(inSpecific(t1) && inSpecific(t2)) return doRound(t1 + t2);
		if(oppositeSigns(t1, t2)) return bottom();
		return doRound(t1 + t2);
	}
	
	@Override
	public Integer sub(Integer t1, Integer t2) {
		return add(t1, neg(t2));
	}
	
	@Override
	public Integer neg(Integer t1) {
		if(bottom() == t1) return bottom();
		if(top() == t1) return top();
		if(large(t1)) return largeNegative(t1) ? lp() : ln();
		return (doRound(-1 * t1));
	}
	
	@Override
	public Integer constant(int c) {
		return doRound(c);
	}
}
