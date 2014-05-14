package ttr.oppdrag;

import ttr.data.Destinasjon;

public class Oppdrag implements IOppdrag {
	private static final long serialVersionUID = 4423887569420895177L;
	private final int oppdragsid;
	private final Destinasjon start;
	private final Destinasjon end;
	private final int verdi;
	
	public Oppdrag(int oppdragsid, Destinasjon start, Destinasjon end, int value) {
		this.oppdragsid = oppdragsid;
		this.start = start;
		this.end = end;
		this.verdi = value;
	}

    public int getOppdragsid(){
		return oppdragsid;
	}

    public int getVerdi() {
		return verdi;
	}
	
	public String toString() {
		return start +" - " +end + "(" +verdi +")";
	}

	public Destinasjon getStart() {
		return start;
	}

	@Override
	public Destinasjon getEnd() {
		return end;
	}
}