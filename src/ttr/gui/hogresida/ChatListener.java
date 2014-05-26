package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.data.MeldingarModell;
import ttr.kjerna.Core;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

class ChatListener implements KeyListener {
	private final Core hovud;
	private final JTextField chatJTextField; //TODO denne må vel vekk herifrå?
	private final MeldingarModell meldingarmodell;

	public ChatListener(JTextField chat, MeldingarModell messagesModel, Core hovud){
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
			sendMessageToPlayers(message);
			chatJTextField.setText("");
		}
		else if (chatJTextField.getText().contains(Infostrengar.starttekst)){
			chatJTextField.setText(String.valueOf(arg0.getKeyChar()));
		}
	}

	private String getPlayerName() throws RemoteException {
		return hovud.findPlayerInAction().getNamn();
	}

	private void sendMessageToPlayers(String message) throws RemoteException {
		meldingarmodell.nyMelding(message);
		for (IPlayer spelar : hovud.getSpelarar()){
			spelar.receiveMessage(message);
		}
	}
}