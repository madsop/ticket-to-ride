package ttr.oppdrag;

import java.io.Serializable;
import ttr.data.Destination;

public class Mission implements Serializable {
	private static final long serialVersionUID = 4423887569420895177L;
	private final Destination start;
	private final Destination end;
	private final int value;
	
	public Mission(int missionId, Destination start, Destination end, int value) {
		this(start, end, value);
	}
	
	public Mission(Destination start, Destination end, int value) {
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

	public Destination getStart() {
		return start;
	}

	public Destination getEnd() {
		return end;
	}
}