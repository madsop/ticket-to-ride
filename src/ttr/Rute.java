package ttr;

import ttr.data.Destinasjon;
import ttr.data.Farge;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class Rute {
	private Set<Destinasjon> destinasjonar;
	private static final int[] ruteverdiar = {1,2,4,7,10,15,0,0,27};
	private int lengde;
	private Farge farge;
	private boolean tunnel;
	private int antaljokrar;
	private Spelar bygdAv;
	private int ruteId;



	public int getRuteId() {
		return ruteId;
	}

	/**
	 * Lagar ei ny rute
	 * @param d1 - destinasjon 1, "frå"
	 * @param d2 - destinasjon 2, "til"
	 * @param lengde - kor lang ruta er
	 * @param farge - kva for farge har ruta
	 * @param tunnel - er ruta tunnel?
	 * @param antaljokrar - kor mange jokrar trengs for ruta?
	 * @return ei ny rute
	 */
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

	/**
	 * @return kor mange poeng denne ruta gir
	 */
	public int getVerdi() {
		return ruteverdiar[lengde-1];
	}

	/**
	 * @return kor denne ruta går frå og til
	 */
	public Set<Destinasjon> getDestinasjonar() {
		return destinasjonar;
	}

	/**
	 * @return kor lang ruta er
	 */
	public int getLengde() {
		return lengde;
	}

	/**
	 * @return kva for farge ruta har.
	 */
	public Farge getFarge() {
		return farge;
	}

	/**
	 * @return er ruta tunnellagt?
	 */
	public boolean isTunnel() {
		return tunnel;
	}

	/**
	 * @return kor mange ruter treng du for å bygge ruta?
	 */
	public int getAntaljokrar() {
		return antaljokrar;
	}

	/**
	 * @return kven, om nokon, har bygd ruta?
	 */
	public Spelar getBygdAv() {
		return bygdAv;
	}

	/**
	 * @param spelar har no bygd denne ruta
	 */
	public void setBygdAv(Spelar spelar) {
		bygdAv = spelar;
	}

	/**
	 * toString
	 */
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
			return destinasjonar.toArray()[0] +" - " +destinasjonar.toArray()[1] +", lengde " +lengde +", av farge " +farge 
			+", tunnel? " +tunnel +", og " +antaljokrar +" jokrar krevs for å byggje denne. Ruta er bygd av " 
			+bygdaf +", og er verdt " +ruteverdiar[lengde-1] +" poeng.";
		}
	}

}