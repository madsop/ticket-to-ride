package ttr.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ttr.gui.SwingUtils;
import ttr.kjerna.Core;
import javax.swing.*;

public class ShowBuiltRoutesHandler implements ActionListener {
	private Core core;

	public ShowBuiltRoutesHandler(Core core) {
		this.core = core;
			}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (core.getAllBuiltRoutes().size() <= 0) {             
			JOptionPane.showMessageDialog(null, "Det er ikkje bygd noka rute enno. Bli den første!");
			return;
		}

		JPanel bygde = new JPanel();
		JList<Object> bygd = new JList<>(core.getAllBuiltRoutes().toArray());

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
		SwingUtils.createJFrame("Desse rutene er bygd", bygde);

	}
}