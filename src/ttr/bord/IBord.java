package ttr.bord;

import ttr.data.Farge;

/**
 * Created by IntelliJ IDEA.
 * User: madsop
 * Date: 19.12.11
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
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
