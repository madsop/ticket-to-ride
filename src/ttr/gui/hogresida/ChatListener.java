package ttr.gui.hogresida;

import ttr.data.Infostrengar;
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

	public ChatListener(boolean nett, JTextField chat, MeldingarModell meldingarmodell, IHovud hovud){
		this.nett = nett;
		this.chat = chat;
		this.meldingarmodell = meldingarmodell;
		this.hovud = hovud;
	}


	public void keyPressed(KeyEvent arg0) {	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
			String message = "";
			ISpelar player = nett ? hovud.getMinSpelar() : hovud.getKvenSinTur();
			try {
				message = player.getNamn();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			message += ": " +chat.getText();

			if (nett){
				meldingarmodell.nyMelding(message);
			}

			for (ISpelar spelar : hovud.getSpelarar()){
				try {
					spelar.faaMelding(message);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			chat.setText("");
		}
		else if (chat.getText().contains(Infostrengar.starttekst)){
			chat.setText(String.valueOf(arg0.getKeyChar()));
		}
	}
	public void keyTyped(KeyEvent arg0) {}
}