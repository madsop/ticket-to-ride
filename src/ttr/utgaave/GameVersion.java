package ttr.utgaave;

import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public abstract class GameVersion {
	private final String title;
	private final URL backgroundImageURL;
	protected ArrayList<Mission> missions;
	protected Set<Route> routes;

	protected GameVersion(String title, String pathToBackgroundImage) {
		this.title = title;
		backgroundImageURL = getClass().getResource(pathToBackgroundImage);
        routes = leggTilRuter();
        missions = fyllMedOppdrag();
	}

    public URL getBakgrunnsbildet() {
		return backgroundImageURL;
	}
	
    public ArrayList<Mission> getOppdrag() {
		return missions;
    }

    public Set<Route> getRuter() {
		return routes;
	}
	
	public String toString() {
		return title;
	}

    protected abstract Set<Route> leggTilRuter();
    protected abstract ArrayList<Mission> fyllMedOppdrag();
}