package eda045f.exercises.flowgraph.implementation;

import eda045f.exercises.flowgraph.AbstractDomain;

public class ArrayIndexAbstractDomain extends AbstractDomain<ArrayIndexDomain> {

	private boolean differentSigns(ArrayIndexDomain a1, ArrayIndexDomain a2) {
		return (a1 == ArrayIndexDomain.ANegative && a2 == ArrayIndexDomain.APositive)
				|| (a2 == ArrayIndexDomain.ANegative && a1 == ArrayIndexDomain.APositive);
	}

	@Override
	public ArrayIndexDomain add(ArrayIndexDomain a1, ArrayIndexDomain a2) {
		if (eitherBottom(a1, a2))
			return ArrayIndexDomain.ABottom;
		if (eitherTop(a1, a2))
			return ArrayIndexDomain.ABottom;
		if (a1 == ArrayIndexDomain.AZero)
			return a2;
		if (a2 == ArrayIndexDomain.AZero)
			return a1;
		if (differentSigns(a1, a2))
			return ArrayIndexDomain.ABottom;
		return a1;
	}

	public ArrayIndexDomain fromInt(int x) {
		if (x > 0)
			return ArrayIndexDomain.APositive;
		if (x == 0)
			return ArrayIndexDomain.AZero;
		return ArrayIndexDomain.ANegative;
	}

	@Override
	public ArrayIndexDomain merge(ArrayIndexDomain a1, ArrayIndexDomain a2) {
		if (eitherBottom(a1, a2))
			return ArrayIndexDomain.ABottom;
		if (a1 == ArrayIndexDomain.ATop)
			return a2;
		if (a2 == ArrayIndexDomain.ATop)
			return a1;
		if (a1 == ArrayIndexDomain.AZero)
			return a2;
		if (a2 == ArrayIndexDomain.AZero)
			return a1;
		if (differentSigns(a1, a2))
			return ArrayIndexDomain.ABottom;
		return a1;
	}

	@Override
	public ArrayIndexDomain neg(ArrayIndexDomain o) {
		switch (o) {
		case ABottom:
			return ArrayIndexDomain.ABottom;
		case ATop:
			return ArrayIndexDomain.ABottom;
		case AZero:
			return ArrayIndexDomain.AZero;
		case ANegative:
			return ArrayIndexDomain.APositive;
		case APositive:
			return ArrayIndexDomain.ANegative;
		default:
			return null;
		}
	}

	@Override
	public ArrayIndexDomain mul(ArrayIndexDomain a1, ArrayIndexDomain a2) {
		if (a1 == ArrayIndexDomain.AZero || a2 == ArrayIndexDomain.AZero)
			return ArrayIndexDomain.AZero;
		if (a1 == ArrayIndexDomain.ABottom || a2 == ArrayIndexDomain.ABottom)
			return ArrayIndexDomain.ABottom;
		if (a1 == ArrayIndexDomain.ATop)
			return a2;
		if (a2 == ArrayIndexDomain.ATop)
			return a1;
		if (differentSigns(a1, a2))
			return ArrayIndexDomain.ANegative;
		return ArrayIndexDomain.APositive;
	}

	@Override
	public ArrayIndexDomain bottom() {
		return ArrayIndexDomain.ABottom;
	}

	@Override
	public ArrayIndexDomain top() {
		return ArrayIndexDomain.ATop;
	}
}