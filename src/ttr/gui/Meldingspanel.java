package ttr.gui;

import ttr.IHovud;
import ttr.data.Konstantar;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

/**
 * User: mop
 * Date: 13.12.11
 * Time: 10:13
 */
class Meldingspanel extends JPanel implements PropertyChangeListener {
    private MeldingarModell meldingarmodell;
    private JList meldingar;
    private JTextField chat;
    private final boolean nett;

    public Meldingspanel(boolean nett) {
        this.setBackground(Color.WHITE);
        this.nett = nett;
    }
    public void setHovud(IHovud hovud){
        ChatListener cl = (ChatListener) chat.getKeyListeners()[0];
        cl.setHovud(hovud);
    }
    
    public void createModell(){
        //meldingsboks.setPreferredSize(new Dimension(150,Konstantar.HOGDE));
    
        meldingarmodell = new MeldingarModell();
        meldingarmodell.addPropertyChangeListener(this);
        //noinspection unchecked,unchecked
        meldingar = new JList(meldingarmodell);
    
        JScrollPane mp = new JScrollPane();
        mp.setPreferredSize(new Dimension(Konstantar.MELDINGSPANELBREIDDE, Konstantar.HOGDE - Konstantar.DIFF));
        mp.getViewport().add(meldingar);
        this.add(mp);
    
        this.setPreferredSize(new Dimension(Konstantar.MELDINGSPANEL,Konstantar.HOGDE));

        chat = new JTextField(ChatListener.starttekst);
        chat.addKeyListener(new ChatListener(nett,chat,meldingarmodell));
        chat.setPreferredSize(Konstantar.CHATDIM);
        this.add(chat);
    
        meldingarmodell.nyMelding("Spelet startar. Velkommen!");
    }

    public MeldingarModell getMeldingarModell(){
        return meldingarmodell;
    }




    public void propertyChange(PropertyChangeEvent arg0) {
        if (arg0.getPropertyName().equals(MeldingarModell.MELDINGAR_PROPERTY)){
            meldingarmodell = new MeldingarModell(meldingarmodell.getMeldingar());
            meldingarmodell.addPropertyChangeListener(this);
            //noinspection unchecked
            meldingar.setModel(meldingarmodell);
            meldingar.setSelectedIndex(meldingarmodell.getSize()-1);
            meldingar.ensureIndexIsVisible(meldingar.getSelectedIndex());
        }
    }
}
