package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.data.MeldingarModell;
import ttr.spelar.IPlayer;

import javax.swing.*;

import com.google.inject.Inject;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Meldingspanel extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = -2415687333214663696L;

	private MeldingarModell messagemodel;
	private final JList<String> messages;
	private JTextField chat;
	private ChatListener chatListener;

	private ArrayList<IPlayer> otherPlayers;

	@Inject
	public Meldingspanel() {
		messagemodel = new MeldingarModell();
		messagemodel.addPropertyChangeListener(this);
		messages = new JList<>(messagemodel);
		setUpGUI();
	}

	private void setUpGUI() {
		this.setBackground(Color.WHITE);
		JScrollPane messagePane = new JScrollPane();
		messagePane.setPreferredSize(new Dimension(Konstantar.MELDINGSPANELBREIDDE, Konstantar.HOGDE - Konstantar.DIFF));
		messagePane.getViewport().add(messages);
		this.add(messagePane);

		this.setPreferredSize(new Dimension(Konstantar.MELDINGSPANEL,Konstantar.HOGDE));

		chat = new JTextField(Infostrengar.starttekst);
		chat.setPreferredSize(Konstantar.CHATDIM);
		this.add(chat);
	}
	
	public void addChatListener(IPlayer myPlayer, ArrayList<IPlayer> players) {
		this.otherPlayers = players;
//		this.otherPlayers.remove(myPlayer);
		chatListener = new ChatListener(chat, myPlayer);
		chat.addKeyListener(chatListener);
		chatListener.addPropertyChangeListener(this);
	}

	public MeldingarModell getMeldingarModell(){ //TODO dette bør vera muleg å unngå
		return messagemodel;
	}

	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals(MeldingarModell.MELDINGAR_PROPERTY)){
			remakeMessageModel();
			orientOtherPlayers(arg0.getNewValue() + "");
		}
		else if (arg0.getPropertyName().equals("chat")) {
			String message = arg0.getNewValue() + "";
			messagemodel.nyMelding(message);
			orientOtherPlayers(message);
		}
		else if (arg0.getPropertyName().equals("remote")) {
			remakeMessageModel();
		}
	}

	//TODO andre spelarar får no berre chatmeldingar, ikkje melding om kva resten av spelarane gjer
	private void orientOtherPlayers(String message) {
		if (otherPlayers == null) { return; } //TODO fjern denne når meir elegant løysing på plass
		for (IPlayer spelar : otherPlayers){
			try {
				spelar.receiveMessage(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private void remakeMessageModel() {
		messagemodel = new MeldingarModell(messagemodel.getMeldingar()); //TODO dette - dvs heile denne metoden - må da eigentleg vera overkill
		messagemodel.addPropertyChangeListener(this);
		messages.setModel(messagemodel);
		messages.setSelectedIndex(messagemodel.getSize()-1);
		messages.ensureIndexIsVisible(messages.getSelectedIndex());
//		orientOtherPlayers(messagemodel.getMeldingar().get(messagemodel.getSize()-1));
	}
}
