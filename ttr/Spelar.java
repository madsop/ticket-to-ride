package nordic;

import java.util.ArrayList;

public class Spelar {
	private static final int antalTog = 40;
	private Hovud hovud;
	private String namn;
	private int[] kort;
	private ArrayList<Oppdrag> oppdrag;
	private ArrayList<Rute> bygdeRuter;
	
	public Spelar (Hovud hovud, String namn) {
		this.hovud = hovud;
		this.namn = namn;
		// få farge-kort
		// få oppdragskort å velje mellom
	}
	
	/**
	 * Finn ein måte å gjera denne på - dvs sjekke om oppdrag er fullførte eller ikkje.
	 * @return antal oppdragspoeng spelaren har
	 */
	public int getOppdragspoeng() {
		int ret = 0;
	/*	for (int i = 0; i < gjenverandeOppdrag.size(); i++) {
			ret -= gjenverandeOppdrag.get(i).getVerdi();
		}
		for (int i = 0; i < fullførteOppdrag.size(); i++) {
			ret += fullførteOppdrag.get(i).getVerdi();
		}
		*/
		return ret;
	}
	
	public int getRutepoeng() {
		int ret = 0;
		for (int i = 0; i < bygdeRuter.size(); i++) {
			ret += bygdeRuter.get(i).getVerdi();
		}
		return ret;
	}
	
	public int getGjenverandeTog() {
		int brukteTog = 0;
		for (int i = 0; i < bygdeRuter.size(); i++) {
			brukteTog += bygdeRuter.get(i).getLengde();
		}
		return antalTog - brukteTog;
	}
	
	public void trekkOppdragskort() {
		if (hovud.getAntalGjenverandeOppdrag() > 0) {
			hovud.getOppdrag();
		}
	}
}
