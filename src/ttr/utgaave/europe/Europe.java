package ttr.utgaave.europe;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.oppdrag.IOppdrag;
import ttr.oppdrag.Oppdrag;
import ttr.rute.IRute;
import ttr.rute.Rute;
import ttr.utgaave.AbstractSpelUtgaave;
import ttr.utgaave.ISpelUtgaave;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Europe extends AbstractSpelUtgaave implements ISpelUtgaave {
	private static final String tittel = "Europe";
	private static final String bilde = "";
	
	public Europe() {
		super(tittel,bilde);
	}
	
	protected Set<IRute> leggTilRuter() {
		ruter = new HashSet<IRute>();
		int ruteId = 0;
		ruter.add(new Rute(ruteId,Destinasjon.Brest,Destinasjon.Dieppe,2,Farge.oransje,false,0));
		//ruteId++;
		return ruter;
	}
	
	protected ArrayList<IOppdrag> fyllMedOppdrag() {
		oppdrag = new ArrayList<IOppdrag>();
		int oppdragsid = 0;
		oppdrag.add(new Oppdrag(oppdragsid,Destinasjon.Brest,Destinasjon.Petrograd,21));
		//oppdragsid++;
		return oppdrag;
	}
}
