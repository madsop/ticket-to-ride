package ttr.oppdrag;

import java.util.ArrayList;

public interface IOppdragshandsamar {
    ArrayList<IOppdrag> getGjenverandeOppdrag();

    int getAntalGjenverandeOppdrag();

    IOppdrag getOppdrag();
}
