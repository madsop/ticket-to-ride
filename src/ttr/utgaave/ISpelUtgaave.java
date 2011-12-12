package ttr.utgaave;

import ttr.Oppdrag;
import ttr.Rute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public interface ISpelUtgaave {
	public String getTittel();
//	public int getAntalStarttog(); ??
//	public int getAntalStartoppdrag(); ???
	public Set<Rute> getRuter();
	public ArrayList<Oppdrag> getOppdrag();
	public String toString();
	public URL getBakgrunnsbildet();
}
