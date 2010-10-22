package ttr.copy;

import java.util.HashSet;
import java.util.Set;

public enum Destinasjon {
	
	/**
	 * Nordic
	 */
	
	// Norge
	Kristiansand("Nordic countries"), Stavanger("Nordic countries"), Bergen("Nordic countries"), Oslo("Nordic countries"), 
	Lillehammer("Nordic countries"), Åndalsnes("Nordic countries"), Trondheim("Nordic countries"), 
	MoIRana("Nordic countries"), Narvik("Nordic countries"), Tromsø("Nordic countries"), Honningsvåg("Nordic countries"), 
	Kirkenes("Nordic countries"), 
	
	// Sverige
	Gøteborg("Nordic countries"), Karlskrona("Nordic countries"), Norrkøping("Nordic countries"), Stockholm("Nordic countries"), 
	Ørebro("Nordic countries"), Sundsvall("Nordic countries"), Østersund("Nordic countries"), Umeå("Nordic countries"), 
	Boden("Nordic countries"), Kiruna("Nordic countries"),
	
	// Finland
	Helsinki("Nordic countries"), Lahti("Nordic countries"), Tampere("Nordic countries"), Turku("Nordic countries"), Imatra("Nordic countries"),
	Lieksa("Nordic countries"), Kajaani("Nordic countries"), Kuopio("Nordic countries"), Oulo("Nordic countries"), Rovaniemi("Nordic countries"),
	Tornio("Nordic countries"), Vaasa("Nordic countries"),
	
	// Danmark
	København("Nordic countries"), Århus("Nordic countries"), Aalborg("Nordic countries"),
	
	// Russland
	Murmansk("Nordic countries"),
	
	// Estland
	Tallinn("Nordic countries"),
	
	/**
	 * Europe
	 */
	// Frankrike
	Brest("Europe"), Dieppe("Europe"), 
	
	// Russland
	Petrograd("Europe");
	

	
	private String spel;
	private Destinasjon(String spel) {
		this.spel = spel;
	}
	
	public Set<Destinasjon> getFraaSpel(String spelnamn) {
		Set<Destinasjon> dest = new HashSet<Destinasjon>();
		for (int i = 0; i < values().length; i++) {
			if (values()[i].spel == spelnamn) {
				dest.add(values()[i]);
			}
		}
		return dest;
	}
	
	// Fyll inn destinasjonar her, med komma mellom.
}