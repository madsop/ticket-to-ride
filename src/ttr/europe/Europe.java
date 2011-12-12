package ttr.europe;

import ttr.Oppdrag;
import ttr.Rute;
import ttr.SpelUtgaave;
import ttr.Utgaave;
import ttr.data.Destinasjon;
import ttr.data.Farge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Europe extends Utgaave implements SpelUtgaave  {
	private static final String tittel = "Europe";
	private static ArrayList<Oppdrag> oppdrag = fyllMedOppdrag();
	private static Set<Rute> ruter = leggTilRuter();
	private static final String bilde = "";
	
	public Europe() {
		super(tittel,ruter,oppdrag,bilde);
	}
	
	private static final Set<Rute> leggTilRuter() {
		ruter = new HashSet<Rute>();
		int ruteId = 0;
		ruter.add(new Rute(ruteId,Destinasjon.Brest,Destinasjon.Dieppe,2,Farge.oransje,false,0));
		ruteId++;
		return ruter;
	}
	
	private static final ArrayList<Oppdrag> fyllMedOppdrag() {
		oppdrag = new ArrayList<Oppdrag>();
		int oppdragsid = 0;
		oppdrag.add(new Oppdrag(oppdragsid,Destinasjon.Brest,Destinasjon.Petrograd,21));
		oppdragsid++;
		return oppdrag;
	}
}
