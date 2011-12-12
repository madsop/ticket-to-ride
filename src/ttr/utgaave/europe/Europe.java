package ttr.utgaave.europe;

import ttr.Oppdrag;
import ttr.Rute;
import ttr.utgaave.ISpelUtgaave;
import ttr.utgaave.SpelUtgaave;
import ttr.data.Destinasjon;
import ttr.data.Farge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Europe extends SpelUtgaave implements ISpelUtgaave {
	private static final String tittel = "Europe";
	private static final String bilde = "";
	
	public Europe() {
		super(tittel,bilde);
	}
	
	protected Set<Rute> leggTilRuter() {
		ruter = new HashSet<Rute>();
		int ruteId = 0;
		ruter.add(new Rute(ruteId,Destinasjon.Brest,Destinasjon.Dieppe,2,Farge.oransje,false,0));
		//ruteId++;
		return ruter;
	}
	
	protected ArrayList<Oppdrag> fyllMedOppdrag() {
		oppdrag = new ArrayList<Oppdrag>();
		int oppdragsid = 0;
		oppdrag.add(new Oppdrag(oppdragsid,Destinasjon.Brest,Destinasjon.Petrograd,21));
		//oppdragsid++;
		return oppdrag;
	}
}
