package ttr.kjerna;

import ttr.struktur.IOppdrag;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: madsop
 * Date: 21.12.11
 * Time: 22:39
 * To change this template use File | Settings | File Templates.
 */
public interface IOppdragshandsamar {
    ArrayList<IOppdrag> getGjenverandeOppdrag();

    int getAntalGjenverandeOppdrag();

    IOppdrag getOppdrag();
}
