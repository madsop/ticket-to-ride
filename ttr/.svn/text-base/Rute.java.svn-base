package nordic;

import java.util.Set;

public class Rute {
	private Set<Destinasjon> destinasjonar;
	private static final int[] ruteverdiar = {1,2,4,7,10,15,0,0,27};
	private int lengde;
	private Farge farge;
	private boolean tunnel;
	private int antaljokrar;
	private Spelar builtBy;
	
	public Rute(Set<Destinasjon> destinasjonar, int lengde, Farge farge, boolean tunnel, int antaljokrar) {
		this.destinasjonar = destinasjonar;
		this.lengde = lengde;
		this.farge = farge;
		this.tunnel = tunnel;
		this.antaljokrar = antaljokrar;
	}
	
	public int getVerdi() {
		return ruteverdiar[lengde];
	}
	
	public Set<Destinasjon> getDestinasjonar() {
		return destinasjonar;
	}
	
	public int getLengde() {
		return lengde;
	}

	public Farge getFarge() {
		return farge;
	}

	public boolean isTunnel() {
		return tunnel;
	}

	public int getAntaljokrar() {
		return antaljokrar;
	}

	public Spelar getBuiltBy() {
		return builtBy;
	}

	public void setBuiltBy(Spelar spelar) {
		builtBy = spelar;
	}
}