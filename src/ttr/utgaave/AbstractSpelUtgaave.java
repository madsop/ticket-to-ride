package ttr.utgaave;

import ttr.Oppdrag;
import ttr.Rute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractSpelUtgaave {
	private final String tittel;
	protected ArrayList<Oppdrag> oppdrag;
	protected Set<Rute> ruter;
	
	private final URL bakgrunnsbildet;

	public URL getBakgrunnsbildet() {
		return bakgrunnsbildet;
	}
	
	protected AbstractSpelUtgaave(String tittel, String adresse) {
		this.tittel = tittel;
		bakgrunnsbildet = getClass().getResource(adresse);
        ruter = leggTilRuter();
        oppdrag = fyllMedOppdrag();
	}

    protected abstract Set<Rute> leggTilRuter();
    protected abstract ArrayList<Oppdrag> fyllMedOppdrag();
	public ArrayList<Oppdrag> getOppdrag() {
		return oppdrag;
	}

	public Set<Rute> getRuter() {
		return ruter;
	}

	public String getTittel() {
		return tittel;
	}
	
	@Override
	public String toString() {
		return tittel;
	}

}
