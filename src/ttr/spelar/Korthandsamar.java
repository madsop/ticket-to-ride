package ttr.spelar;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Korthandsamar extends UnicastRemoteObject implements IKorthandsamar {
    private IHovud hovud;
    private int[] kort;

    Korthandsamar(IHovud hovud) throws RemoteException {
        super();
        this.hovud = hovud;
        kort = new int[Konstantar.ANTAL_FARGAR];

        for (int i = 0; i < Konstantar.ANTAL_STARTKORT; i++) {
            kort[i] = 0;
        }
        faaInitielleFargekort();
    }

    private void faaInitielleFargekort() throws  RemoteException{

        // Gir spelaren fargekort i byrjinga
        for (int i = 0; i < Konstantar.ANTAL_STARTKORT; i++) {
            Farge trekt = trekkFargekort();
            int plass = -1;
            for (int j = 0; j < Konstantar.FARGAR.length; j++) {
                if (trekt == Konstantar.FARGAR[j]) {
                    plass = j;
                }
            }
            if (plass >= 0) {
                kort[plass]++;
            }
        }
    }

    @Override
    public Farge getTilfeldigKortFråBordet(int i) throws RemoteException {
        Farge fargePåDetTilfeldigeKortet = hovud.getBord().getTilfeldigKortFråBordet(i, true);

        if (fargePåDetTilfeldigeKortet == null){
            JOptionPane.showMessageDialog((Component) hovud.getGui(), "Det er ikkje noko kort der, ser du vel.");
            hovud.getGui().getKortButtons()[i].setBackground(Color.GRAY);
            hovud.getGui().getKortButtons()[i].setText("Tom");

            int fargePosisjon = -1;
            for (Farge farge : Konstantar.FARGAR){
                if (fargePåDetTilfeldigeKortet == farge){
                    //noinspection ConstantConditions
                    fargePosisjon = farge.ordinal();
                }
            }
            if (fargePosisjon >= 0 && fargePosisjon < Konstantar.FARGAR.length){
                kort[fargePosisjon]--;
            }

            return null;
        }
        hovud.getBord().getPaaBordet()[i] = fargePåDetTilfeldigeKortet;
        return fargePåDetTilfeldigeKortet;
    }


    @Override
    public void faaKort(Farge farge) throws RemoteException  {
        int tel = 0;
        for (int i = 0; i < Konstantar.FARGAR.length; i++) {
            if (farge == Konstantar.FARGAR[i]) {
                tel = i;
            }
        }
        kort[tel]++;
    }



    @Override
    public int[] getKort() throws RemoteException  {
        return kort;
    }

    /** @return eit tilfeldig fargekort frå toppen av stokken */
    @Override
    public Farge trekkFargekort() throws RemoteException {
        Farge trekt;
        if (hovud.getBord().getAntalFargekortPåBordet() > 0) {
            trekt = hovud.getBord().getTilfeldigKortFråBordet(0, false);
        }
        else {
            trekt = null;
        }
        return trekt;
    }
}
