package ttr.utgaave;

import ttr.oppdrag.IOppdrag;
import ttr.rute.IRoute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public interface ISpelUtgaave {
//	public int getAntalStarttog(); ??      //TODO
//	public int getAntalStartoppdrag(); ???       //TODO
	public Set<IRoute> getRuter();
	public ArrayList<IOppdrag> getOppdrag();
	public String toString();
	public URL getBakgrunnsbildet();
}
