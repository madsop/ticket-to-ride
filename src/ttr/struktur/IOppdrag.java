package ttr.struktur;

import ttr.data.Destinasjon;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: madsop
 * Date: 20.12.11
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public interface IOppdrag extends Serializable {
    int getOppdragsid();

    Set<Destinasjon> getDestinasjonar();

    int getVerdi();

    @Override
    String toString();
}
