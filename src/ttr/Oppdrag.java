package ttr;

import ttr.data.Destinasjon;

import java.util.HashSet;
import java.util.Set;



public class Oppdrag {
	private final int oppdragsid;
	private final Set<Destinasjon> destinasjonar;
	private final int verdi;
	
	public int getOppdragsid(){
		return oppdragsid;
	}

	public Oppdrag(int oppdragsid, Destinasjon d1, Destinasjon d2, int verdi) {
		this.oppdragsid = oppdragsid;
		destinasjonar = new HashSet<Destinasjon>();
		destinasjonar.add(d1);
		destinasjonar.add(d2);
		this.verdi = verdi;
	}

	public Set<Destinasjon> getDestinasjonar() {
		return destinasjonar;
	}

	public int getVerdi() {
		return verdi;
	}
	
	@Override
	public String toString() {
		return destinasjonar.toArray()[0] +" - " +destinasjonar.toArray()[1] + "(" +verdi +")";
	}
	
}