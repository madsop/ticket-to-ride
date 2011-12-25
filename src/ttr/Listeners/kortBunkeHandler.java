package ttr.Listeners;

import ttr.data.Farge;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;

import javax.swing.*;
import java.rmi.RemoteException;

class kortBunkeHandler {
    public kortBunkeHandler(IHovud hovud, IGUI gui) {
        Farge f = null;
        try {
            f = hovud.getKvenSinTur().trekkFargekort();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        JPanel p = new JPanel();
        JLabel woho = new JLabel("Du trakk eit kort av farge " +f);
        try {
            if (f==null){return;}
            hovud.getKvenSinTur().faaKort(f);
            gui.sendKortMelding(true,true,f);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        p.add(woho);
        //				 lagRamme("Du trakk inn kort", p);
        try {
            if (hovud.getKvenSinTur().getValdAllereie()) {
                try {
                    hovud.nesteSpelar();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else {
                hovud.getKvenSinTur().setEinVald(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
