package ttr.Listeners;

import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.rmi.RemoteException;

class kortBunkeHandler {
    public kortBunkeHandler(IHovud hovud, IGUI gui) {
        ISpelar kvenSinTur = hovud.getKvenSinTur();
        Farge f = null;
        try {
            f = kvenSinTur.trekkFargekort();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        JPanel p = new JPanel();
        JLabel woho = new JLabel("Du trakk eit kort av farge " +f);
        try {
            if (f==null){return;}
            kvenSinTur.faaKort(f);
            hovud.sendKortMelding(true,true,f);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        p.add(woho);
        //				 lagRamme("Du trakk inn kort", p);
        try {
            if (kvenSinTur.getValdAllereie()) {
                try {
                    hovud.nesteSpelar();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else {
                kvenSinTur.setEittKortTrektInn(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}