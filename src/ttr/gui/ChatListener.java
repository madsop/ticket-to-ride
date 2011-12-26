package ttr.gui;

import ttr.data.MeldingarModell;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

class ChatListener implements KeyListener {
    private final boolean nett;
    private final IHovud hovud;
    private final JTextField chat;
    private final MeldingarModell meldingarmodell;
    static final String starttekst = "Prat her!";

    public ChatListener(boolean nett, JTextField chat, MeldingarModell meldingarmodell, IHovud hovud){
        this.nett = nett;
        this.chat = chat;
        this.meldingarmodell = meldingarmodell;
        this.hovud = hovud;
    }


    public void keyPressed(KeyEvent arg0) {	}

    public void keyReleased(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
            String melding = "";
            if (nett){
                try {
                    melding = hovud.getMinSpelar().getNamn();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    melding = hovud.getKvenSinTur().getNamn();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            melding += ": " +chat.getText();
            if (nett){
                meldingarmodell.nyMelding(melding);
            }

            for (ISpelar s : hovud.getSpelarar()){
                try {
                    s.faaMelding(melding);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            chat.setText("");
        }
        else if (chat.getText().contains(starttekst)){
            chat.setText(String.valueOf(arg0.getKeyChar()));
        }
    }
    public void keyTyped(KeyEvent arg0) {}
}