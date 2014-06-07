package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.data.MeldingarModell;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

class ChatListener implements KeyListener {
	private final JTextField chatJTextField; //TODO denne må vel vekk herifrå?
	private final MeldingarModell meldingarmodell; //todo pcs
	private final ArrayList<IPlayer> players;
	private IPlayer myPlayer;

	public ChatListener(JTextField chat, MeldingarModell messagesModel, IPlayer myPlayer, ArrayList<IPlayer> players) {
		this.chatJTextField = chat;
		this.meldingarmodell = messagesModel;
		this.players = players;
		this.myPlayer = myPlayer;
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
			String message = myPlayer.getNamn() + ": " + chatJTextField.getText();;
			sendMessageToPlayers(message);
			chatJTextField.setText("");
		}
		else if (chatJTextField.getText().contains(Infostrengar.starttekst)){
			chatJTextField.setText(String.valueOf(arg0.getKeyChar()));
		}
	}

	private void sendMessageToPlayers(String message) throws RemoteException {
		meldingarmodell.nyMelding(message);
		for (IPlayer spelar : players){
			spelar.receiveMessage(message);
		}
	}
}