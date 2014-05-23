package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.data.MeldingarModell;
import ttr.kjerna.IHovud;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

class ChatListener implements KeyListener {
	private final boolean nett;
	private final IHovud hovud;
	private final JTextField chatJTextField; //TODO denne må vel vekk herifrå?
	private final MeldingarModell meldingarmodell;

	public ChatListener(boolean nett, JTextField chat, MeldingarModell messagesModel, IHovud hovud){
		this.nett = nett;
		this.chatJTextField = chat;
		this.meldingarmodell = messagesModel;
		this.hovud = hovud;
	}

	public void keyPressed(KeyEvent arg0) {	}
	public void keyTyped(KeyEvent arg0) {}

	public void keyReleased(KeyEvent arg0) {
		try {
			sendMessage(arg0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(KeyEvent arg0) throws RemoteException {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
			String message = getPlayerName() + ": " + chatJTextField.getText();;
			if (nett){
				meldingarmodell.nyMelding(message);
			}
			sendMessageToPlayers(message);
			chatJTextField.setText("");
		}
		else if (chatJTextField.getText().contains(Infostrengar.starttekst)){
			chatJTextField.setText(String.valueOf(arg0.getKeyChar()));
		}
	}

	private String getPlayerName() throws RemoteException {
		PlayerAndNetworkWTF player = nett ? hovud.getMinSpelar() : hovud.getKvenSinTur();
		return player.getNamn();	
	}

	private void sendMessageToPlayers(String message) throws RemoteException {
		for (PlayerAndNetworkWTF spelar : hovud.getSpelarar()){
			spelar.receiveMessage(message);
		}
	}
}