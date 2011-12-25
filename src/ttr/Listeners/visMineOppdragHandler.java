package ttr.Listeners;

import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;
import ttr.struktur.IOppdrag;

import javax.swing.*;
import java.rmi.RemoteException;

class visMineOppdragHandler {

    public visMineOppdragHandler(IHovud hovud, IGUI gui) {
        // vis oppdraga mine
        JPanel oppdraga = new JPanel();

        ISpelar visSine;
        if (hovud.isNett()) {
            visSine = hovud.getMinSpelar();
        }
        else {
            visSine = hovud.getKvenSinTur();
        }
        String oppdrg = "";
        try {
            oppdrg = visSine.getNamn() +": ";
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        try {
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
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        JLabel oppdr = new JLabel(oppdrg);
        oppdraga.add(oppdr);
        try {
            gui.lagRamme("Viser oppdraga til " +visSine.getNamn(), oppdraga);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
