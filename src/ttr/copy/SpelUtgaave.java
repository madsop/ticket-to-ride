package ttr.copy;

import java.util.ArrayList;
import java.util.Set;

public interface SpelUtgaave {
	public String getTittel();
//	public int getAntalStarttog(); ??
//	public int getAntalStartoppdrag(); ???
	public Set<Rute> getRuter();
	public ArrayList<Oppdrag> getOppdrag();
	public String toString();
}
