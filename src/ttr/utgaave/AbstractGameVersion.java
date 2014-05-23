package ttr.utgaave;

import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractGameVersion {
	private final String title;
	protected ArrayList<Mission> missions;
	protected Set<Route> routes;
	
	private final URL backgroundImageURL;

    public URL getBakgrunnsbildet() {
		return backgroundImageURL;
	}
	
	protected AbstractGameVersion(String title, String pathToBackgroundImage) {
		this.title = title;
		backgroundImageURL = getClass().getResource(pathToBackgroundImage);
        routes = leggTilRuter();
        missions = fyllMedOppdrag();
	}

    protected abstract Set<Route> leggTilRuter();
    protected abstract ArrayList<Mission> fyllMedOppdrag();

    public ArrayList<Mission> getOppdrag() {
		return missions;
    }

    public Set<Route> getRuter() {
		return routes;
	}
	
	public String toString() {
		return title;
	}

}