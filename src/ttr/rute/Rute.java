
package ttr.rute;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class Rute implements IRute {
	private final Set<Destinasjon> destinasjonar;
	private static final int[] ruteverdiar = {1,2,4,7,10,15,0,0,27};
	private final int lengde;
	private final Farge farge;
	private final boolean tunnel;
	private final int antaljokrar;
	private ISpelar bygdAv;
	private final int ruteId;



	@Override
    public int getRuteId() {
		return ruteId;
	}

	public Rute(int ruteId, Destinasjon d1, Destinasjon d2, int lengde, Farge farge, boolean tunnel, int antaljokrar) {
		this.ruteId = ruteId;
		destinasjonar = new HashSet<>();
		destinasjonar.add(d1);
		destinasjonar.add(d2);
		this.lengde = lengde;
		this.farge = farge;
		this.tunnel = tunnel;
		this.antaljokrar = antaljokrar;
	}

	@Override
    public int getVerdi() {
		return ruteverdiar[lengde-1];
	}

	@Override
    public Set<Destinasjon> getDestinasjonar() {
		return destinasjonar;
	}

	@Override
    public int getLengde() {
		return lengde;
	}

	@Override
    public Farge getFarge() {
		return farge;
	}

	@Override
    public boolean isTunnel() {
		return tunnel;
	}

	@Override
    public int getAntaljokrar() {
		return antaljokrar;
	}

	@Override
    public void setBygdAv(ISpelar spelar) {
		bygdAv = spelar;
	}

	@Override
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
            return destinasjonar.toArray()[0] +" - " +destinasjonar.toArray()[1] +", lengde " +lengde +", av farge " +farge
			+", tunnel? " +tunnel +", og " +antaljokrar +" jokrar krevs for å byggje denne. Ruta er bygd av "
			+builtByString +", og er verdt " +ruteverdiar[lengde-1] +" poeng.";
		}
	}
}