package ttr.utgaave.europe;

import ttr.data.Destination;
import ttr.data.Colour;
import ttr.oppdrag.Mission;
import ttr.rute.Route;
import ttr.utgaave.GameVersion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Europe extends GameVersion {
	private static final String tittel = "Europe";
	private static final String bilde = "";
	
	public Europe() {
		super(tittel,bilde);
	}
	
	protected Set<Route> leggTilRuter() {
		routes = new HashSet<>();
		int ruteId = 0;
		routes.add(new Route(ruteId,Destination.Brest,Destination.Dieppe,2,Colour.oransje,false,0));
		//ruteId++;
		return routes;
	}
	
	protected ArrayList<Mission> fyllMedOppdrag() {
		missions = new ArrayList<>();
		int oppdragsid = 0;
		missions.add(new Mission(oppdragsid,Destination.Brest,Destination.Petrograd,21));
		//oppdragsid++;
		return missions;
	}
}
