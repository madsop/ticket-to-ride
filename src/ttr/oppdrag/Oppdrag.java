package ttr.oppdrag;

import ttr.data.Destinasjon;

import java.util.HashSet;
import java.util.Set;

public class Oppdrag implements IOppdrag {
	private final int oppdragsid;
	private final Set<Destinasjon> destinasjonar;
	private final Destinasjon start;
	private final Destinasjon end;
	private final int verdi;
	
	public Oppdrag(int oppdragsid, Destinasjon d1, Destinasjon d2, int verdi) {
		this.oppdragsid = oppdragsid;
		destinasjonar = new HashSet<>();
		destinasjonar.add(d1);
		destinasjonar.add(d2);
		this.start = d1;
		this.end = d2;
		this.verdi = verdi;
	}

    public int getOppdragsid(){
		return oppdragsid;
	}
	
    public Set<Destinasjon> getDestinasjonar() {
		return destinasjonar;
	}

    public int getVerdi() {
		return verdi;
	}
	
	public String toString() {
		return destinasjonar.toArray()[0] +" - " +destinasjonar.toArray()[1] + "(" +verdi +")";
	}

	public Destinasjon getStart() {
		return start;
	}

	@Override
	public Destinasjon getEnd() {
		return end;
	}
	
}