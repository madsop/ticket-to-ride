package ttr.oppdrag;

import java.io.Serializable;
import ttr.data.Destinasjon;

public class Mission implements Serializable {
	private static final long serialVersionUID = 4423887569420895177L;
	private final Destinasjon start;
	private final Destinasjon end;
	private final int value;
	
	public Mission(int missionId, Destinasjon start, Destinasjon end, int value) {
		this(start, end, value);
	}
	
	public Mission(Destinasjon start, Destinasjon end, int value) {
		this.start = start;
		this.end = end;
		this.value = value;
	}

    public int getValue() {
		return value;
	}
	
	public String toString() {
		return start +" - " +end + "(" +value +")";
	}

	public Destinasjon getStart() {
		return start;
	}

	public Destinasjon getEnd() {
		return end;
	}
}