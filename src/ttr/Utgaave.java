package ttr;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class Utgaave {
	private String tittel;
	private ArrayList<Oppdrag> oppdrag;
	private Set<Rute> ruter;
	
	private URL bakgrunnsbildet;

	public URL getBakgrunnsbildet() {
		return bakgrunnsbildet;
	}
	
	public Utgaave(String tittel, Set<Rute> ruter, ArrayList<Oppdrag> oppdrag, String adresse) {
		this.tittel = tittel;
		this.ruter = ruter;
		this.oppdrag = oppdrag;
		bakgrunnsbildet = getClass().getResource(adresse);
	}
	
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
