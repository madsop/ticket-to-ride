package ttr.data;

//import java.util.HashSet;
//import java.util.Set;

public enum Destinasjon {
	
	/**
	 * Nordic
	 */
	
	// Norge
	Kristiansand, Stavanger, Bergen, Oslo, 
	Lillehammer, Åndalsnes, Trondheim, 
	MoIRana, Narvik, Tromsø, Honningsvåg, 
	Kirkenes, 
	
	// Sverige
	Gøteborg, Karlskrona, Norrkøping, Stockholm, 
	Ørebro, Sundsvall, Østersund, Umeå, 
	Boden, Kiruna,
	
	// Finland
	Helsinki, Lahti, Tampere, Turku, Imatra,
	Lieksa, Kajaani, Kuopio, Oulo, Rovaniemi,
	Tornio, Vaasa,
	
	// Danmark
	København, Århus, Aalborg,
	
	// Russland
	Murmansk,
	
	// Estland
	Tallinn,
	
	/**
	 * Europe
	 */
	// Frankrike
	Brest, Dieppe, Marseille, Paris, 

	// Russland
	Petrograd, Moskva, Smolensk, Rostov, Kharkov,
	
	// Resten av Aust-Europa
	Warszawa,Sochi,Kyiv, Sofia, Bucuresti,Budapest,
	Athina, Zagrab, Sarajevo, Sevastopol, Wilno, Riga,
	
	// Tyrkia
	Constantinople, Angora, Smyrna, Erzurum,
	
	// Italia
	Roma, Venezia, Palermo, Brindisi,
	
	// Tyskland
	Berlin, Frankfurt, München, Essen,
	
	// Mellom-Europa
	Amsterdam, Bruxelles, London, Edinburgh, 
	Zurich, Wien, Danzig, //Stockholm, København,
	
	// Iberia
	Lisboa, Madrid, Barcelona, Cadiz, Pamplona


    /*private String spel;
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
    */
	// Fyll inn destinasjonar her, med komma mellom.
}