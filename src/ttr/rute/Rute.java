package ttr.rute;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;

public class Rute implements IRoute {
	private static final int[] ruteverdiar = {1,2,4,7,10,15,0,0,27};
	private final int lengde;
	private final Farge farge;
	private final boolean tunnel;
	private final int antaljokrar;
	private ISpelar bygdAv;
	private final int ruteId;
	private final Destinasjon start;
	private final Destinasjon end;

	public Rute(int ruteId, Destinasjon start, Destinasjon end, int lengde, Farge farge, boolean tunnel, int antaljokrar) {
		this.ruteId = ruteId;
		this.start = start;
		this.end = end;
		
		this.lengde = lengde;
		this.farge = farge;
		this.tunnel = tunnel;
		this.antaljokrar = antaljokrar;
	}

	public Destinasjon getStart() {
		return start;
	}

	public Destinasjon getEnd() {
		return end;
	}

	public int getRouteId() {
		return ruteId;
	}

	public int getValue() {
		return ruteverdiar[lengde-1];
	}

    public int getLength() {
		return lengde;
	}

    public Farge getColour() {
		return farge;
	}

    public boolean isTunnel() {
		return tunnel;
	}

    public int getNumberOfRequiredJokers() {
		return antaljokrar;
	}

    public void setBuiltBy(ISpelar spelar) {
		bygdAv = spelar;
	}

	@SuppressWarnings("finally")
	public String toString() {
		String builtByString = null;
		try {
			if (bygdAv != null) {
				builtByString = bygdAv.getNamn();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		finally {
            //noinspection ReturnInsideFinallyBlock,ReturnInsideFinallyBlock
            return start + " - " + end + ", lengde " + lengde + ", av farge " + farge
			+ ", tunnel? " + tunnel + ", og " +antaljokrar +" jokrar krevs for å byggje denne. Ruta er bygd av "
			+ builtByString + ", og er verdt " + ruteverdiar[lengde-1] + " poeng.";
		}
	}
}