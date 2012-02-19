package ttr.oppdrag.listeners;

import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.oppdrag.IOppdrag;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.rmi.RemoteException;

public class visMineOppdragHandler {

    public visMineOppdragHandler(IHovud hovud, IGUI gui) throws RemoteException {
        JPanel oppdraga = new JPanel();

        ISpelar visSine;
        if (hovud.isNett()) {
            visSine = hovud.getMinSpelar();
        }
        else {
            visSine = hovud.getKvenSinTur();
        }
        String oppdrg = visSine.getNamn() +": ";

            for (int i = 0; i < visSine.getAntalOppdrag(); i++) {
                IOppdrag o = visSine.getOppdrag().get(i);
                oppdrg += o;
                if (visSine.erOppdragFerdig(o.getOppdragsid())){
                    oppdrg += " (OK)";
                }
                if (i == visSine.getAntalOppdrag()-1){
                    oppdrg += ".";
                }
                else{
                    oppdrg += ", ";
                }
            }
        JLabel oppdr = new JLabel(oppdrg);
        oppdraga.add(oppdr);
        gui.lagRamme("Viser oppdraga til " +visSine.getNamn(), oppdraga);
    }
}
