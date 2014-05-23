package ttr.gui.hogresida;

import ttr.data.IMeldingarModell;
import ttr.kjerna.Core;

public interface IMeldingspanel {
    void prepareChat(Core hovud);

    IMeldingarModell getMeldingarModell();
}
