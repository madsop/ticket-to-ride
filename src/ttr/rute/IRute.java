package ttr.rute;

import ttr.data.Destinasjon;
import ttr.data.Farge;
import ttr.spelar.ISpelar;

import java.util.Set;

public interface IRute {
    int getRuteId();

    int getVerdi();

    Set<Destinasjon> getDestinasjonar();

    int getLengde();

    Farge getFarge();

    boolean isTunnel();

    int getAntaljokrar();

    void setBygdAv(ISpelar spelar);

    @Override
    @SuppressWarnings("finally")
    String toString();
}
