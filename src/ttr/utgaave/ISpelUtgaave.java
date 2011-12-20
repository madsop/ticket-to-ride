package ttr.utgaave;

import ttr.struktur.IOppdrag;
import ttr.struktur.Rute;

import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public interface ISpelUtgaave {
	public String getTittel();
//	public int getAntalStarttog(); ??      //TODO
//	public int getAntalStartoppdrag(); ???       //TODO
	public Set<Rute> getRuter();
	public ArrayList<IOppdrag> getOppdrag();
	public String toString();
	public URL getBakgrunnsbildet();
}
