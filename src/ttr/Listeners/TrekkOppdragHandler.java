package ttr.Listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.kjerna.Oppdragshandsamar;

import java.rmi.RemoteException;

class TrekkOppdragHandler {
    public TrekkOppdragHandler(IHovud hovud, IGUI gui) {
        try {
            hovud.sendKortMelding(false, false, Konstantar.FARGAR[0]);
            Oppdragshandsamar.trekkOppdrag(gui, hovud.getKvenSinTur(), false);
            hovud.nesteSpelar();
        }
        catch (RemoteException re) {
            re.printStackTrace();
        }
    }
}
