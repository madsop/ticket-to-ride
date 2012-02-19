package ttr.oppdrag.listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.oppdrag.Oppdragshandsamar;

import java.rmi.RemoteException;

public class TrekkOppdragHandler {
    public TrekkOppdragHandler(IHovud hovud, IGUI gui) throws RemoteException {
            hovud.sendKortMelding(false, false, Konstantar.FARGAR[0]);
            Oppdragshandsamar.trekkOppdrag(gui, hovud.getKvenSinTur(), false);
            hovud.nesteSpelar();
    }
}
