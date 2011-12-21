package ttr.gui;

import ttr.data.Konstantar;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

class BildePanel extends JPanel {
    private final Image bildet;
    
    public BildePanel(ISpelUtgaave spel){
        //		ImageIcon a = new ImageIcon("/home/mads/Dropbox/Programmering/Eclipse-workspace/TTR/src/ttr/nordic/nordic_map.jpg");
        //		Image b = a.getImage();
        URL bakgrunnsbildeURL = spel.getBakgrunnsbildet();
        bildet = new ImageIcon(bakgrunnsbildeURL).getImage();
    }



        @Override
        public void paintComponent(Graphics g) {
            double bilderatio = (double) bildet.getHeight(null) / (double) bildet.getWidth(null);
            /*
         Teiknar bakgrunnsbildet.
         */
            int bildehogde = Konstantar.HOGDE;
            int br = (int) ((bildehogde / bilderatio) * 1.3);
            try {
                g.drawImage(bildet, 0, 25, br, bildehogde, null);
                this.setPreferredSize(new Dimension(br + 50, bildehogde + 50));
            }
            catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
}
