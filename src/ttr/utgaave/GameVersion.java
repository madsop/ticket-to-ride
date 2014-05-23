package ttr.utgaave;

import ttr.oppdrag.Mission;
import ttr.rute.Route;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public interface GameVersion {
//	public int getAntalStarttog(); ??      //TODO
//	public int getAntalStartoppdrag(); ???       //TODO
	public Set<Route> getRuter();
	public ArrayList<Mission> getOppdrag();
	public String toString();
	public URL getBakgrunnsbildet();
}
