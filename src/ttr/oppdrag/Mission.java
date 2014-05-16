package ttr.oppdrag;

import ttr.data.Destinasjon;

import java.io.Serializable;

public interface Mission extends Serializable {
    int getMissionId();
    Destinasjon getStart();
    Destinasjon getEnd();
    int getValue();
    String toString();
}