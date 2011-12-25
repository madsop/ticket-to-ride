package ttr.gui.Listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.struktur.Rute;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class ByggHandler {

    public ByggHandler(IHovud hovud, JFrame frame) {
        Rute[] ruterArray = null;
        try {
            ruterArray = hovud.finnFramRuter();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        Rute ubygdeRuter = (Rute) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
                JOptionPane.QUESTION_MESSAGE, null, ruterArray, ruterArray[1]);

        Rute bygd = null;
        for (Rute aRuterArray : ruterArray) {
            if (aRuterArray == ubygdeRuter) {
                bygd = aRuterArray;
            }
        }

        if (bygd!=null) {
            int[] spelarensKort = null;
            try {
                spelarensKort = hovud.getKvenSinTur().getKort();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int kortKrevd = bygd.getLengde()-bygd.getAntaljokrar();
            Farge ruteFarge = bygd.getFarge();
            int krevdJokrar = bygd.getAntaljokrar();

            int plass = Konstantar.finnPosisjonForFarg(ruteFarge);

            int harjokrar = spelarensKort[spelarensKort.length-1];
            if (bygd.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
                if (bygd.isTunnel()) {
                    try {
                        hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
                    }
                    catch (RemoteException re){
                        re.printStackTrace();
                    }
                }
                else {
                    try {
                        hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
                    }
                    catch (RemoteException re){
                        re.printStackTrace();
                    }
                }
            }
            else if (krevdJokrar <= harjokrar && (kortKrevd <= ( (harjokrar-krevdJokrar) + spelarensKort[plass]) ) ){
                try {
                    if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd+krevdJokrar) {
                        if (bygd.isTunnel()) {
                            hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
                        }
                        else {
                            hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Du har ikkje nok tog att til å byggje denne ruta.");
                    }
                } catch (HeadlessException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
            }
        }
    }
}
