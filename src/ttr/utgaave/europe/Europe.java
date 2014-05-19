package ttr.utgaave.europe;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionImpl;
import ttr.rute.Route;
import ttr.rute.RouteImpl;
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
	
	protected Set<Route> leggTilRuter() {
		ruter = new HashSet<Route>();
		int ruteId = 0;
		ruter.add(new RouteImpl(ruteId,Destinasjon.Brest,Destinasjon.Dieppe,2,Farge.oransje,false,0));
		//ruteId++;
		return ruter;
	}
	
	protected ArrayList<Mission> fyllMedOppdrag() {
		oppdrag = new ArrayList<Mission>();
		int oppdragsid = 0;
		oppdrag.add(new MissionImpl(oppdragsid,Destinasjon.Brest,Destinasjon.Petrograd,21));
		//oppdragsid++;
		return oppdrag;
	}
}
