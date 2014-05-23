package ttr.utgaave.europe;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.oppdrag.Mission;
import ttr.oppdrag.MissionImpl;
import ttr.rute.Route;
import ttr.rute.RouteImpl;
import ttr.utgaave.AbstractGameVersion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Europe extends AbstractGameVersion {
	private static final String tittel = "Europe";
	private static final String bilde = "";
	
	public Europe() {
		super(tittel,bilde);
	}
	
	protected Set<Route> leggTilRuter() {
		routes = new HashSet<>();
		int ruteId = 0;
		routes.add(new RouteImpl(ruteId,Destinasjon.Brest,Destinasjon.Dieppe,2,Farge.oransje,false,0));
		//ruteId++;
		return routes;
	}
	
	protected ArrayList<Mission> fyllMedOppdrag() {
		missions = new ArrayList<>();
		int oppdragsid = 0;
		missions.add(new MissionImpl(oppdragsid,Destinasjon.Brest,Destinasjon.Petrograd,21));
		//oppdragsid++;
		return missions;
	}
}
