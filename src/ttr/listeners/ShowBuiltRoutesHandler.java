package ttr.listeners;

import java.util.Set;

import ttr.gui.SwingUtils;
import ttr.rute.Route;

import javax.swing.*;

class ShowBuiltRoutesHandler {
	public ShowBuiltRoutesHandler(Set<Route> allBuiltRoutes, JFrame frame) {
		if (allBuiltRoutes.size() <= 0) {             
			JOptionPane.showMessageDialog(frame, "Det er ikkje bygd noka rute enno. Bli den første!");
			return;
		}

		JPanel bygde = new JPanel();
		JList<Object> bygd = new JList<>(allBuiltRoutes.toArray());

		/*	if (hovud.isNett()){
               // Finn spel-verten
               for (int i = 0; i < bygd.getModel().getSize(); i++){
                   for (int j = 0; j < hovud.getSpelarar().size()+1; j++)
                   try {
                      if (hovud.getAlleBygdeRuter().get(i).getBygdAv().getSpelarNummer()==j){
                          bygd.set
                       }
                  } catch (RemoteException e) {
                      e.printStackTrace();
                  }
               }
           }
		 */

		bygde.add(bygd);
		SwingUtils.createJFrame("Desse rutene er bygd",bygde);
	}
}