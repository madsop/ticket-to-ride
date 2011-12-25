package ttr.struktur;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class Rute {
	private final Set<Destinasjon> destinasjonar;
	private static final int[] ruteverdiar = {1,2,4,7,10,15,0,0,27};
	private final int lengde;
	private final Farge farge;
	private final boolean tunnel;
	private final int antaljokrar;
	private ISpelar bygdAv;
	private final int ruteId;



	public int getRuteId() {
		return ruteId;
	}

	public Rute(int ruteId, Destinasjon d1, Destinasjon d2, int lengde, Farge farge, boolean tunnel, int antaljokrar) {
		this.ruteId = ruteId;
		destinasjonar = new HashSet<Destinasjon>();
		destinasjonar.add(d1);
		destinasjonar.add(d2);
		this.lengde = lengde;
		this.farge = farge;
		this.tunnel = tunnel;
		this.antaljokrar = antaljokrar;
	}

	public int getVerdi() {
		return ruteverdiar[lengde-1];
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

	public ISpelar getBygdAv() {
		return bygdAv;
	}

	public void setBygdAv(ISpelar spelar) {
		bygdAv = spelar;
	}

	@Override
	@SuppressWarnings("finally")
	public String toString() {
		String bygdaf = null;
		try {
			if (bygdAv != null) {
				bygdaf = bygdAv.getNamn();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		finally {
            //noinspection ReturnInsideFinallyBlock,ReturnInsideFinallyBlock
            return destinasjonar.toArray()[0] +" - " +destinasjonar.toArray()[1] +", lengde " +lengde +", av farge " +farge
			+", tunnel? " +tunnel +", og " +antaljokrar +" jokrar krevs for å byggje denne. Ruta er bygd av "
			+bygdaf +", og er verdt " +ruteverdiar[lengde-1] +" poeng.";
		}
	}
}