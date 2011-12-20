package ttr.utgaave;

import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractSpelUtgaave {
	private final String tittel;
	protected ArrayList<IOppdrag> oppdrag;
	protected Set<Rute> ruter;
	
	private final URL bakgrunnsbildet;

    @SuppressWarnings("UnusedDeclaration")
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
    protected abstract ArrayList<IOppdrag> fyllMedOppdrag();

    @SuppressWarnings("UnusedDeclaration")
    public ArrayList<IOppdrag> getOppdrag() {
		return oppdrag;
    }

    @SuppressWarnings("UnusedDeclaration")
	public Set<Rute> getRuter() {
		return ruter;
	}

    @SuppressWarnings("UnusedDeclaration")
	public String getTittel() {
		return tittel;
	}
	
	@Override
	public String toString() {
		return tittel;
	}

}
