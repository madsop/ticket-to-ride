package ttr.Listeners;

import ttr.data.Farge;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.rmi.RemoteException;

class kortBunkeHandler {
    public kortBunkeHandler(IHovud hovud) {
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
            kvenSinTur.receiveCard(f);
            hovud.sendKortMelding(true,true,f);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        p.add(woho);

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