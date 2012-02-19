package ttr.gui;

import ttr.data.Konstantar;
import ttr.utgaave.ISpelUtgaave;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BildePanel extends JPanel implements IBildePanel {
    private final Image bildet;
    
    public BildePanel(ISpelUtgaave spel){
        URL bakgrunnsbildeURL = spel.getBakgrunnsbildet();
        bildet = new ImageIcon(bakgrunnsbildeURL).getImage();
    }

        @Override
        public void paintComponent(Graphics g) {
            double bilderatio = (double) bildet.getHeight(null) / (double) bildet.getWidth(null);
            int bildehogde = Konstantar.VINDUSSTORLEIK.height * 6/7;
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
