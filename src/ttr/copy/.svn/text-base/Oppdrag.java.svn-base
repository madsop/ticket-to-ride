package ttr.copy;

import java.util.HashSet;
import java.util.Set;



public class Oppdrag {
	private Set<Destinasjon> destinasjonar;
	private int verdi;

	
	/**
	 * @param d1 - destinasjon 1, "frå"
	 * @param d2 - destinasjon 2, "til"
	 * @param verdi - kor mange poeng oppdraget gir
	 * @return
	 */
	public Oppdrag(Destinasjon d1, Destinasjon d2, int verdi) {
		destinasjonar = new HashSet<Destinasjon>();
		destinasjonar.add(d1);
		destinasjonar.add(d2);
		this.verdi = verdi;
	}

	/**
	 * @return kor oppdraget går frå og til
	 */
	public Set<Destinasjon> getDestinasjonar() {
		return destinasjonar;
	}

	/**
	 * @return verdien av oppdraget
	 */
	public int getVerdi() {
		return verdi;
	}
	
	/**
	 * toString. Format: destinasjon1 - destinasjon 2 (verdi)
	 */
	public String toString() {
		return destinasjonar.toArray()[0] +" - " +destinasjonar.toArray()[1] + "(" +verdi +")";
	}
	
}