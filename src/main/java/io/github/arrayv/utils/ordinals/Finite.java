package io.github.arrayv.utils.ordinals;
import java.math.BigInteger;

public class Finite extends Ordinal {
	private final BigInteger val;
	
	public Finite(BigInteger val) {
		this.val = val;
	}
	
	public Finite(long val) {
		this.val = BigInteger.valueOf(val);
	}
	
	public BigInteger getVal() {
		return val;
	}
	
	public boolean isZero() {
		if (val.equals(BigInteger.ZERO)) {
			return true;
		}
		return false;
	}
	
	public boolean isOne() {
		if (val.equals(BigInteger.ONE)) {
			return true;
		}
		return false;
	}
	
	public boolean isFinite() {
		return true;
	}
	
	public boolean isOmega() {
		return false;
	}
	
	public boolean isOmega(long index) {
		return false;
	}
	
	public boolean noCoeffOrRest() {
		return false;
	}

	public boolean isLimit() {
		return false;
	}
	
	protected boolean hasCountableCofinality() {
		return false;
	}
	
	protected boolean isAnyOmega() {
		return false;
	}
	
	public Ordinal funSeq(Finite n) {
		return null;
	}

	public Ordinal funSeq(long n) {
		return null;
	}
	
	public Finite getFiniteRest() {
		return this;
	}

	public Ordinal successor() {
		return new Finite(val.add(BigInteger.ONE));	
	}

	public Ordinal predecessor() {
		if (isZero()) {
			return null;
		}
		return new Finite(val.subtract(BigInteger.ONE));
	}
	
	public Ordinal copy() {
		return new Finite(val);
	}
	
	public String toString() {
		return val.toString();
	}
	
	public String toLaTeX() {
		return val.toString();
	}

	public Ordinal funSeq(BigInteger n) {
		return null;
	}

	public boolean needsParenthesesExp() {
		return false;
	}

	public boolean needsParentheseseCoeff() {
		return false;
	}

	protected boolean isUncountableInitialOrdinal() {
		return false;
	}

	protected boolean asPsi() {
		return true;
	}
	
	public int compareTo(Finite x) {
		return val.compareTo(x.getVal());
	}
	
	public int getValMod(int n) {
		return val.mod(BigInteger.valueOf(n)).intValue();
	}
}