package io.github.arrayv.utils.ordinals;


public class BHO extends Ordinal {
	public boolean isLimit() {
		return true;
	}

	public Ordinal successor() {
		return null;
	}

	public Ordinal predecessor() {
		return null;
	}

	public Ordinal funSeq(Finite n) {
		return new Canon(iterate(n));
		
	}
	
	private Ordinal iterate(Ordinal n) {
		if (n.isZero()) {
			return new Finite(1);
		} else {
			return new Canon(new Omega(1), iterate(n.predecessor()));
		}
	}


	protected boolean hasCountableCofinality() {
		return true;
	}

	public boolean isZero() {
		return false;
	}

	public boolean isOne() {
		return false;
	}

	public boolean isOmega() {
		return false;
	}

	public boolean isOmega(long index) {
		return false;
	}

	protected boolean isAnyOmega() {
		return false;
	}

	public Ordinal copy() {
		return new BHO();
	}

	public String toString() {
		return "\u03C8(\u03B5_(\u03A9 + 1))";
	}
	
	public String toLaTeX() {
		return "\\psi(\\varepsilon_{\\Omega+1})";
	}

	public boolean needsParenthesesExp() {
		return false;
	}

	public boolean needsParentheseseCoeff() {
		return false;
	}

	public boolean isFinite() {
		return false;
	}

	protected boolean asPsi() {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean isUncountableInitialOrdinal() {
		return false;
	}
	
	public Finite getFiniteRest() {
		return Ordinal.ZERO;
	}

}
