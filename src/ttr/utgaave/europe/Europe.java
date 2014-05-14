package ttr.utgaave.europe;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.oppdrag.IOppdrag;
import ttr.oppdrag.Mission;
import ttr.rute.IRoute;
import ttr.rute.Route;
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
	
	protected Set<IRoute> leggTilRuter() {
		ruter = new HashSet<IRoute>();
		int ruteId = 0;
		ruter.add(new Route(ruteId,Destinasjon.Brest,Destinasjon.Dieppe,2,Farge.oransje,false,0));
		//ruteId++;
		return ruter;
	}
	
	protected ArrayList<IOppdrag> fyllMedOppdrag() {
		oppdrag = new ArrayList<IOppdrag>();
		int oppdragsid = 0;
		oppdrag.add(new Mission(oppdragsid,Destinasjon.Brest,Destinasjon.Petrograd,21));
		//oppdragsid++;
		return oppdrag;
	}
}
