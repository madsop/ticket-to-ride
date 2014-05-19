package ttr.listeners;

import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import javax.swing.*;

class visBygdeHandler {
    public visBygdeHandler(IHovud hovud, IGUI gui, JFrame frame) {
        JPanel bygde = new JPanel();

        if (hovud.getAlleBygdeRuter().size()>0) {
            JList<Object> bygd = new JList<>(hovud.getAlleBygdeRuter().toArray());

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
            gui.lagRamme("Desse rutene er bygd",bygde);
        }
        else {
            JOptionPane.showMessageDialog(frame, "Det er ikkje bygd noka rute enno. Bli den første!");
        }
    }
}
