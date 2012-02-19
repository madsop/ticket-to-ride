package ttr.Listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.rute.IRute;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.HeadlessException;

import java.rmi.RemoteException;

class ByggHandler {

    public ByggHandler(IHovud hovud, JFrame frame) throws RemoteException {
        IRute[] ruterArray = hovud.finnFramRuter();

        IRute ubygdeRuter = (IRute) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
                JOptionPane.QUESTION_MESSAGE, null, ruterArray, ruterArray[1]);

        IRute bygd = null;
        for (IRute aRuterArray : ruterArray) {
            if (aRuterArray == ubygdeRuter) {
                bygd = aRuterArray;
            }
        }

        if (bygd!=null) {
            int[] spelarensKort = hovud.getKvenSinTur().getKort();
            int kortKrevd = bygd.getLengde()-bygd.getAntaljokrar();
            Farge ruteFarge = bygd.getFarge();
            int krevdJokrar = bygd.getAntaljokrar();

            int plass = Konstantar.finnPosisjonForFarg(ruteFarge);

            int harjokrar = spelarensKort[spelarensKort.length-1];
            if (bygd.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
                if (bygd.isTunnel()) {
                        hovud.byggTunnel(bygd, plass, kortKrevd, krevdJokrar);
                }
                else {
                        hovud.bygg(bygd, plass, kortKrevd, krevdJokrar);
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
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
            }
        }
    }
}