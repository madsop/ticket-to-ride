package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

class ChatListener implements KeyListener {
	private final PropertyChangeSupport propertyChangeSupport;
	private final JTextField chatJTextField;
	private String myPlayerName;

	public ChatListener(JTextField chat, String myPlayerName) {
		this.chatJTextField = chat;
		this.myPlayerName = myPlayerName;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void keyPressed(KeyEvent arg0) {	}
	public void keyTyped(KeyEvent arg0) {}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){			
			propertyChangeSupport.firePropertyChange("chat", "", myPlayerName + ": " + chatJTextField.getText());
			chatJTextField.setText("");
		}
		else if (chatJTextField.getText().contains(Infostrengar.starttekst)){
			chatJTextField.setText(String.valueOf(arg0.getKeyChar()));
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
}