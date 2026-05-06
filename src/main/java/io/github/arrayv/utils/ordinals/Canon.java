package io.github.arrayv.utils.ordinals;

public class Canon extends Ordinal { //Building block for canonical ordinal notation
	private final Ordinal base; //If the base is Finite or Canon then this implicitly means Psi(base)
	private final Ordinal exponent;
	private final Ordinal coefficient;
	private final Ordinal rest;
	
	public Canon(Ordinal base, Ordinal exponent, Ordinal coefficient, Ordinal rest) {
		this.base = base;
		this.exponent = exponent;
		this.coefficient = coefficient;
		this.rest = rest;
	}
	
	public Canon(Ordinal base, Ordinal exponent, Ordinal coefficient) {
		this.base = base;
		this.exponent = exponent;
		this.coefficient = coefficient;
		this.rest = new Finite(0);
	}
	
	public Canon(Ordinal base, Ordinal exponent) {
		this.base = base;
		this.exponent = exponent;
		this.coefficient = new Finite(1);
		this.rest = new Finite(0);
	}
	
	public Canon(Ordinal base) {
		this.base = base;
		this.exponent = new Finite(1);
		this.coefficient = new Finite(1);
		this.rest = new Finite(0);
	}

	public Ordinal getBase() {
		return base;
	}
	
	public Ordinal getExponent() {
		return exponent;
	}
	
	public Ordinal getCoefficient() {
		return coefficient;
	}
	
	public Ordinal getRest() {
		return rest;
	}
	
	public Ordinal successor() {
		return new Canon(base, exponent, coefficient, rest.successor());
	}
	
	public Ordinal predecessor() {
		return new Canon(base, exponent, coefficient, rest.predecessor());
	}
	
	
	public boolean hasCountableCofinality() {
		if (isLimit()) {
			if (!base.isOmega(1)) {
				return true;
			} else if (!rest.isZero()) {
				if (rest.hasCountableCofinality()) {
					return true;
				}
			} else if (!coefficient.isOne() && coefficient.hasCountableCofinality()) {
				return true;
			} else if (exponent.isLimit() && exponent.hasCountableCofinality()) {
				return true;
			}
		}
		return false;
	}
	
	public Ordinal funSeq(Finite n) {
		if (!rest.isZero()) {
			return new Canon(base, exponent, coefficient, rest.funSeq(n));
		}
		
		if (coefficient.isLimit() ) {
			return new Canon(base, exponent, coefficient.funSeq(n));
		} else if (coefficient.isOne()) {
			if (exponent.isLimit()) {
				return new Canon(base, exponent.funSeq(n));
			} else if (!exponent.isOne()) {
				return new Canon(base, exponent.predecessor(), n);
			}
		} else {
			return new Canon(base, exponent, coefficient.predecessor(),  new Canon(base, exponent).funSeq(n)); 
			
			/*if (exponent.isLimit()) {
				return new Canon(base, exponent, coefficient.predecessor(), new Canon(base.copy(), exponent.funSeq(n)));
			} else {
				return new Canon(base, exponent, coefficient.predecessor(), new Canon(base.copy(), exponent.predecessor(), new Canon(base.copy()).funSeq(n)));
			}*/
		}
		
		//If we get here rest is zero and exponent and coefficient are both one
		if (base.isOmega()) {
			return n;
		} else if (base.isZero()) { // Implicit Psi(0)
			if (n.isZero()) {
				return new Finite(1);
			}
			return new Canon(new Omega(0), new Canon(base).funSeq((Finite)n.predecessor()));
		} else if (base.isLimit()) { // Implicit Psi(alpha), is alpha limit
			if (base.hasCountableCofinality()) {
				return new Canon(base.funSeq(n));
			} else {
				Canon base = (Canon) this.base;
				return new Canon(base.iterate(n));
			}
		} else { // alpha is successor
			if (n.isZero()) {
				return new Finite(1);
			}
			return new Canon(base.predecessor(), new Canon(base).funSeq((Finite)n.predecessor()));
		}	
	}
	
	private Ordinal iterate(Ordinal n) {
		if (n.isZero()) {
			return new Finite(0);
		}
		return h(iterate(n.predecessor()));
	}
	
	
	
	private Ordinal h(Ordinal a) {
		if (!rest.isZero()) {
			Canon rest = (Canon) this.rest;
			return new Canon(base, exponent, coefficient, rest.h(a));
		}
		
		// if we are here the coefficient can only be a successor.
		if (!coefficient.isOne()) { // if the coefficient is larger than one.
			return new Canon(base, exponent, coefficient.predecessor(), new Canon(a)); 
		} else {
			if (exponent.isLimit()) {
				Canon exponent = (Canon) this.exponent;
				return new Canon(base, exponent.h(a));
			} else if (exponent.isOne()){
				return new Canon(a);
			} else {
				return new Canon(base, exponent.predecessor(), new Canon(a));
			}
		}
	}

	public boolean isLimit() {
		if (rest.isZero()) {
			return true;
		} else if (rest.isLimit()) {
			return true;
		}
		return false;
	}
	
	public boolean isFinite() {
		return false;
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
		return new Canon(base.copy(), exponent.copy(), coefficient.copy(), rest.copy());
	}

	public String toString() {
		String B = "";
		String E = "";
		String C = "";
		String R = "";
		
		if (base.asPsi()) {
			B = "\u03C8(" + base.toString() + ")";
		} else {
			B = base.toString();
		}
		
		if (!exponent.isOne()) {
			if (exponent.needsParenthesesExp()) {
				E = "^(" + exponent.toString() + ")";
			} else {
				E = "^" + exponent.toString();
			}
		}
		
		if (!coefficient.isOne()) {
			if (coefficient.needsParentheseseCoeff()) {
				C = "*(" + coefficient.toString() + ")";
			} else {
				C = "*" + coefficient.toString();
			}
			
		}
		
		if (!rest.isZero()) {
			R = " + " + rest.toString();
		}
		
		return B + E + C + R;
	}
	
	
	public String toLaTeX() {
		String B = "";
		String E = "";
		String C = "";
		String R = "";
		
		if (base.asPsi()) {
			B = "\\psi\\left(" + base.toLaTeX() + "\\right)";
		} else {
			B = base.toLaTeX();
		}
		
		if (!exponent.isOne()) {
			E = "^{" + exponent.toLaTeX() + "}";
		}
		
		if (!coefficient.isOne()) {
			if (coefficient.needsParentheseseCoeff()) {
				C = "\\left(" + coefficient.toLaTeX() + "\\right)";
			} else {
				C = coefficient.toLaTeX();
			}
		}
		
		if (!rest.isZero()) {
			R = "+" + rest.toLaTeX();
		}
		
		return B + E + C + R;
	}

	public boolean needsParenthesesExp() {
		if (coefficient.isOne() && rest.isZero()) {
			return false;
		}
		return true;
	}


	public boolean needsParentheseseCoeff() {
		if (rest.isZero()) {
			return false;
		}
		return true;
	}

	protected boolean isUncountableInitialOrdinal() {
		if (base.isUncountableInitialOrdinal() && exponent.isOne() && coefficient.isOne() && rest.isZero()) {
			return true;
		}
		return false;
	}
	
	protected boolean asPsi() {
		return true;
	}

	public Finite getFiniteRest() {
		return rest.getFiniteRest();
	}
}