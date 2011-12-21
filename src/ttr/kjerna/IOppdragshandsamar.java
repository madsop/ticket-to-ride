package ttr.kjerna;

import ttr.struktur.IOppdrag;

import java.util.ArrayList;

public interface IOppdragshandsamar {
    ArrayList<IOppdrag> getGjenverandeOppdrag();

    int getAntalGjenverandeOppdrag();

    IOppdrag getOppdrag();
}
