<#>
//init
source(Aus.findFileAnywhere("abs_imp_init.bsh").toURL());

//agenti
source(Aus.findFileAnywhere("abs_imp_agenti.bsh").toURL());

//Partneri
source(Aus.findFileAnywhere("abs_imp_partneri.bsh").toURL());

//Artikli
source(Aus.findFileAnywhere("abs_imp_artikli.bsh").toURL());



//* PROMETNI PODACI *//
HashMap seqMap = new HashMap();

//Racuni
source(Aus.findFileAnywhere("abs_imp_racuni.bsh").toURL());
//seq racuni
if (newd.getRowCount() > 0) seqMap.put(seqopis, new Integer(b));


//Otpremnice - prodaja

source(Aus.findFileAnywhere("abs_imp_otpremnice.bsh").toURL());

//seq otpremnice
if (newotp.getRowCount() > 0) seqMap.put(seqopis2, new Integer(bo));



//sejvanje
raLocalTransaction saver = new raLocalTransaction() {
  public boolean transaction() {
    for (Iterator iter = seqMap.keySet().iterator(); iter.hasNext();) {
      String sqop = (String) iter.next();
      Valid.getValid().setSeqFilter(sqop);
      dM.getDataModule().getSeq().setDouble("BROJ",((Integer)seqMap.get(sqop)).doubleValue());
      Valid.getValid().unlockCurrentSeq(false);
      raTransaction.saveChanges(dM.getDataModule().getSeq());
    }
//    raTransaction.saveChanges(newp);
//    raTransaction.saveChanges(newa);
    raTransaction.saveChanges(newd);
    raTransaction.saveChanges(newstd);
    raTransaction.saveChanges(newotp);
    raTransaction.saveChanges(newstotp);

    return true;
  }
};
if (saver.execTransaction()) {
  hr.restart.robno.BatchRekalkulacijaStanja.start("1000",newotp.getString("GOD"));
  System.out.println( 
      "Preneseno je "
        +newp.getRowCount()+" novih partnera, "
        +newa.getRowCount()+" novih artikala, "
        +ABSrac.getRowCount()+" novih raèuna, "
	+ABSotp.getRowCount()+" novih otpremnica, "
  );
} else {
  System.out.println( 
      "Prijenos dokumenata nije uspio!! Probajte ponovo!");
}
System.exit(0);
