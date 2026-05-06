package io.github.arrayv.utils.ordinals;
public class Omega extends Ordinal {
	private long index;
	
	public Omega(long index) {
		this.index = index;
	}

	public boolean isLimit() {
		return true;
	}

	public Ordinal predecessor() {
		return null;
	}

	public Ordinal funSeq(Finite n) {
		if (isOmega()) {
			return n;
		}
		return null;
	}

	protected boolean hasCountableCofinality() {
		if (isOmega()) {
			return true;
		}
		return false;
	}

	public boolean isZero() {
		return false;
	}

	public boolean isOne() {
		return false;
	}
	
	public boolean isFinite() {
		return false;
	}

	public boolean isOmega() {
		if (index == 0) {
			return true;
		}
		return false;
	}

	public boolean isOmega(long index) {
		if (this.index == index) {
			return true;
		}
		return false;
	}

	public Ordinal copy() {
		return new Omega(index);
	}

	public Ordinal successor() {
		return new Canon(this).successor();
	}

	public String toString() {
		if (isOmega()) {
			return "\u03C9";
		} else if (isOmega(1)) {
			return "\u03A9";
		} else {
			return "\u03A9_" + index;
		}
	}
	
	public String toLaTeX() {
		if (isOmega()) {
			return "\\omega";
		} else if (isOmega(1)) {
			return "\\Omega";
		} else {
			return "\\Omega_{" + index +"}";
		}
	}

	protected boolean isAnyOmega() {
		return true;
	}

	public boolean needsParenthesesExp() {
		return false;
	}

	public boolean needsParentheseseCoeff() {
		return false;
	}

	protected boolean isUncountableInitialOrdinal() {
		if (isOmega()) {
			return false;
		}
		return true;
	}
	
	protected boolean asPsi() {
		return false;
	}
	
	public Finite getFiniteRest() {
		return Ordinal.ZERO;
	}
}