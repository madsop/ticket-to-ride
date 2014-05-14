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
        for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
            Farge trekt = trekkFargekort();
            int plass = -1;
            for (int colourPosition = 0; colourPosition < Konstantar.FARGAR.length; colourPosition++) {
                if (trekt == Konstantar.FARGAR[colourPosition]) {
                    plass = colourPosition;
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
            guiSetUp(i);

            int colourPosition = -1;
            for (Farge farge : Konstantar.FARGAR){
                if (fargePåDetTilfeldigeKortet == farge){
                    colourPosition = farge.ordinal();
                }
            }
            if (colourPosition >= 0 && colourPosition < Konstantar.FARGAR.length){ //todo bør ikkje denne vera >0 ?
                kort[colourPosition]--;
            }

            return null;
        }
        hovud.getBord().getPaaBordet()[i] = fargePåDetTilfeldigeKortet;
        return fargePåDetTilfeldigeKortet;
    }

	private void guiSetUp(int i) {
		JOptionPane.showMessageDialog((Component) hovud.getGui(), "Det er ikkje noko kort der, ser du vel.");
		hovud.getGui().getKortButtons()[i].setBackground(Color.GRAY);
		hovud.getGui().getKortButtons()[i].setText("Tom");
	}


    @Override
    public void faaKort(Farge farge) throws RemoteException  {
        int counter = 0;
        for (int i = 0; i < Konstantar.FARGAR.length; i++) {
            if (farge == Konstantar.FARGAR[i]) {
                counter = i;
            }
        }        
        kort[counter]++;
    }

    @Override
    public int[] getKort() throws RemoteException  {
        return kort;
    }

    /** @return eit tilfeldig fargekort frå toppen av stokken */
    @Override
    public Farge trekkFargekort() throws RemoteException {
        if (hovud.getBord().getAntalFargekortPåBordet() > 0) {
            return hovud.getBord().getTilfeldigKortFråBordet(0, false);
        }
        return null;
    }
}