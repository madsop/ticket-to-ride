package ttr.struktur;

import ttr.data.Destinasjon;

import java.io.Serializable;
import java.util.Set;

public interface IOppdrag extends Serializable {
    int getOppdragsid();

    Set<Destinasjon> getDestinasjonar();

    int getVerdi();

    @Override
    String toString();
}
