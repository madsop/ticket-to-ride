package ttr.oppdrag;

import ttr.data.Destinasjon;

import java.io.Serializable;
import java.util.Set;

public interface IOppdrag extends Serializable {
    int getOppdragsid();

    Set<Destinasjon> getDestinasjonar();
    Destinasjon getStart();
    Destinasjon getEnd();

    int getVerdi();

    @Override
    String toString();
}
