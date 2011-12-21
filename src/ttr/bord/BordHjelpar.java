package ttr.bord;

import ttr.data.Konstantar;

class BordHjelpar {

    int tilfeldigFarge(int fargekortpåbordet, int[] igjenAvFargekort){
        int valtkort = (int) (Math.random() * fargekortpåbordet);

        int teljar = 0;
        int midlertidigverdi = 0;
        while ((midlertidigverdi < valtkort)
                && (teljar < Konstantar.ANTAL_FARGAR)
                && (midlertidigverdi <= fargekortpåbordet)) {
            midlertidigverdi += igjenAvFargekort[teljar];
            teljar++;
        }
        teljar--;

        return teljar;
    }
}
