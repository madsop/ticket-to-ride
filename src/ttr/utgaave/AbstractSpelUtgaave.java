package ttr.utgaave;

import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractSpelUtgaave {
	private final String tittel;
	protected ArrayList<Mission> oppdrag;
	protected Set<Route> ruter;
	
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

    protected abstract Set<Route> leggTilRuter();
    protected abstract ArrayList<Mission> fyllMedOppdrag();

    public ArrayList<Mission> getOppdrag() {
		return oppdrag;
    }

    public Set<Route> getRuter() {
		return ruter;
	}
	
	@Override
	public String toString() {
		return tittel;
	}

}
