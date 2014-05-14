package ttr.utgaave;

import ttr.oppdrag.IOppdrag;
import ttr.rute.IRoute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractSpelUtgaave {
	private final String tittel;
	protected ArrayList<IOppdrag> oppdrag;
	protected Set<IRoute> ruter;
	
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

    protected abstract Set<IRoute> leggTilRuter();
    protected abstract ArrayList<IOppdrag> fyllMedOppdrag();

    public ArrayList<IOppdrag> getOppdrag() {
		return oppdrag;
    }

    public Set<IRoute> getRuter() {
		return ruter;
	}
	
	@Override
	public String toString() {
		return tittel;
	}

}
