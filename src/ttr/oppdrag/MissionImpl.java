package ttr.oppdrag;

import ttr.data.Destinasjon;

public class MissionImpl implements Mission {
	private static final long serialVersionUID = 4423887569420895177L;
	private final int missionId;
	private final Destinasjon start;
	private final Destinasjon end;
	private final int value;
	
	public MissionImpl(int missionId, Destinasjon start, Destinasjon end, int value) {
		this.missionId = missionId;
		this.start = start;
		this.end = end;
		this.value = value;
	}

    public int getMissionId(){
		return missionId;
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

	@Override
	public Destinasjon getEnd() {
		return end;
	}
}