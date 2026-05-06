package io.github.arrayv.utils.ordinals;
import java.math.BigInteger;

public abstract class Ordinal {
	public static Finite ZERO = new Finite(0);
	public static Finite ONE = new Finite(1);
	public static Omega OMEGA = new Omega(0);
	public static Omega OMEGA1 = new Omega(1);
	public static Canon EPSILON0 = new Canon(new Finite(0));
	public static BHO BHO = new BHO();
	
	public abstract boolean isLimit();
	
	public abstract boolean isFinite();
	
	public abstract Ordinal successor();
	
	public abstract Ordinal predecessor();
	
	public abstract Ordinal funSeq(Finite n);
	
	public Ordinal funSeq(BigInteger n) {
		return  funSeq(new Finite(n));
	}
	
	public Ordinal funSeq(long n) {
		return  funSeq(new Finite(n));
	}
	
	protected abstract boolean hasCountableCofinality();

	public abstract boolean isZero();

	public abstract boolean isOne();

	public abstract boolean isOmega();
	
	public abstract boolean isOmega(long index);
	
	protected abstract boolean isAnyOmega();
	
	protected abstract boolean asPsi();

	public abstract Ordinal copy();
	
	public abstract String toString();
	
	public abstract String toLaTeX();

	public abstract boolean needsParenthesesExp();
	
	public abstract boolean needsParentheseseCoeff();
	
	protected abstract boolean isUncountableInitialOrdinal();
	
	public abstract Finite getFiniteRest();
}