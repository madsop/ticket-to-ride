package ttr.gui.hogresida;

import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.data.MeldingarModell;
import ttr.kjerna.IHovud;

import javax.swing.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Meldingspanel extends JPanel implements PropertyChangeListener, IMeldingspanel {
	private static final long serialVersionUID = -2415687333214663696L;
	
	private MeldingarModell messagemodel;
    private final JList<String> messages;
    private final boolean nett;

    public Meldingspanel(boolean nett) {
        this.nett = nett;
        messages = initialiseMessagePanel();
        setUpGUI();
    }

	private JList<String> initialiseMessagePanel() {
		messagemodel = new MeldingarModell();
        messagemodel.addPropertyChangeListener(this);
        return new JList<>(messagemodel);
	}

	private void setUpGUI() {
		this.setBackground(Color.WHITE);
        JScrollPane messagePane = new JScrollPane();
        messagePane.setPreferredSize(new Dimension(Konstantar.MELDINGSPANELBREIDDE, Konstantar.HOGDE - Konstantar.DIFF));
        messagePane.getViewport().add(messages);
        this.add(messagePane);

        this.setPreferredSize(new Dimension(Konstantar.MELDINGSPANEL,Konstantar.HOGDE));
	}
    
    public void prepareChat(IHovud hovud){
        JTextField chat = new JTextField(Infostrengar.starttekst);
        chat.addKeyListener(new ChatListener(nett, chat, messagemodel, hovud));
        chat.setPreferredSize(Konstantar.CHATDIM);
        this.add(chat);
    }

    public MeldingarModell getMeldingarModell(){
        return messagemodel;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        if (arg0.getPropertyName().equals(MeldingarModell.MELDINGAR_PROPERTY)){
            messagemodel = new MeldingarModell(messagemodel.getMeldingar()); //TODO dette - dvs heile denne metoden - må da eigentleg vera overkill
            messagemodel.addPropertyChangeListener(this);
            messages.setModel(messagemodel);
            messages.setSelectedIndex(messagemodel.getSize()-1);
            messages.ensureIndexIsVisible(messages.getSelectedIndex());
        }
    }
}
