package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

class ChatListener implements KeyListener {
	private final PropertyChangeSupport propertyChangeSupport;
	private final JTextField chatJTextField; //TODO denne må vel vekk herifrå? Eller?	
	private IPlayer myPlayer;

	public ChatListener(JTextField chat, IPlayer myPlayer) {
		this.chatJTextField = chat;
		this.myPlayer = myPlayer;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
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
			chatJTextField.setText("");
			
			propertyChangeSupport.firePropertyChange("chat", "", message);
		}
		else if (chatJTextField.getText().contains(Infostrengar.starttekst)){
			chatJTextField.setText(String.valueOf(arg0.getKeyChar()));
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
}