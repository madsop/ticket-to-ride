package ttr.bord;

import ttr.data.Farge;

public interface IBord {
    public void setPaaBordet(Farge[] paaBordet);
    public void setEinPaaBordet(Farge f, int plass);
    public void leggUtFem();
    public int[] getIgjenAvFargekort();
    public Farge[] getPaaBordet();
    public int getAntalFargekortPåBordet();
    public Farge getTilfeldigKortFråBordet(int plass, boolean leggPåBordet);
    public boolean sjekkOmJokrarPaaBordetErOK();
}
