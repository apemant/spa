<#>
//impath=new File("C:\\restart\\putty\\dos");
impath=new File("/home/andrej/db/HTS/20071016/DOS");
/*;
//(cskl, tkal, kol, vri)
ims = Stanje.getDataModule().loadData(impath,"lager");;
arts = Artikli.getDataModule().copyDataSet();;
for (ims.first(); ims.inBounds(); ims.next()) {;
  if (lookupData.getlookupData().raLocate(arts, "CART1", ims.getString("TKAL").trim())) {;
     ims.setInt("CART",arts.getInt("CART"));;
     ims.post();;
  };
};
;
ims;
*/;
//********** URN *********
//(cpar,brojdok,datdok,iznos,osnovica,pdv);
//(cpar,brojdok,datdok,ssaldo, pvssaldo, rsaldo);
ure = Skstavke.getDataModule().loadData(impath,"uracuni","(cpar,brojdok,datdok,ssaldo, pvssaldo, rsaldo)");
pars = Partneri.getDataModule().copyDataSet();
int k=9999;
for (ure.first(); ure.inBounds(); ure.next()) {
  if (ure.getInt("CPAR")==0) {
    ure.setInt("CPAR",2); 
  };
  k++;
  ure.setBigDecimal("IP",ure.getBigDecimal("SSALDO"));
  ure.setBigDecimal("SALDO",ure.getBigDecimal("SSALDO"));
  ure.setBigDecimal("PVSALDO",ure.getBigDecimal("SSALDO"));
  ure.setBigDecimal("ID",Aus.zero2);
  ure.setString("VRDOK","URN");
  ure.setString("BROJKONTA","2200");
  ure.setString("KNJIG","01");
  ure.setString("CORG","01");
  ure.setTimestamp("DATUMKNJ",ure.getTimestamp("DATDOK"));
  ure.setInt("BROJIZV",k);;
//  ure.setString("ORIGDOK",ure.getString("BROJDOK"));;
  ure.setBigDecimal("rsaldo",Aus.zero2);;
  ure.setBigDecimal("PVSSALDO",ure.getBigDecimal("SSALDO"));
  ure.setString("ZIRO","DOS");
  ure.setString("CSKSTAVKE",hr.restart.sk.raSaldaKonti.findCSK(ure));
  ure.setString("OZNVAL","kn");
  ure.setBigDecimal("TECAJ",Aus.one0);
};
try {
ure.saveChanges();
} catch (Exception e_u) {
  System.out.println("*********** URE failed");
  e_u.printStackTrace();
}

//********** IRN *********
//(cpar,brojdok,datdok,iznos,osnovica,pdv);
//(cpar,brojdok,datdok,ssaldo, pvssaldo, rsaldo);
ire = Skstavke.getDataModule().loadData(impath,"racuni","(cpar,brojdok,datdok,ssaldo, pvssaldo, rsaldo)");
pars = Partneri.getDataModule().copyDataSet();
int k=99999;
for (ire.first(); ire.inBounds(); ire.next()) {
  if (ire.getInt("CPAR")==0) {
    ire.setInt("CPAR",2); 
  };
  k++;
  ire.setBigDecimal("ID",ire.getBigDecimal("SSALDO"));
  ire.setBigDecimal("SALDO",ire.getBigDecimal("SSALDO"));
  ire.setBigDecimal("PVSALDO",ire.getBigDecimal("SSALDO"));
  ire.setBigDecimal("IP",Aus.zero2);
  ire.setString("VRDOK","IRN");
  ire.setString("BROJKONTA","1200");
  ire.setString("KNJIG","01");
  ire.setString("CORG","01");
  ire.setTimestamp("DATUMKNJ",ire.getTimestamp("DATDOK"));
  ire.setInt("BROJIZV",k);;
//  ire.setString("ORIGDOK",ire.getString("BROJDOK"));;
  ire.setBigDecimal("rsaldo",Aus.zero2);;
  ire.setBigDecimal("PVSSALDO",ire.getBigDecimal("SSALDO"));
  ire.setString("ZIRO","DOS");
  ire.setString("CSKSTAVKE",hr.restart.sk.raSaldaKonti.findCSK(ire));
  ire.setString("OZNVAL","kn");
  ire.setBigDecimal("TECAJ",Aus.one0);
};
try {
ire.saveChanges();
} catch (Exception e_r) {
  System.out.println("*********** IRE failed");
  e_r.printStackTrace();
}


//************IZVODI**************
//(cpar,brojdok,BROJIZV,ziro,datdok,id,ip)
izv = Skstavke.getDataModule().loadData(impath,"izvodi","(cpar,brojdok,BROJIZV,ziro,datdok,id,ip)");
for (izv.first(); izv.inBounds(); izv.next()) {
  if (izv.getInt("CPAR")==0) {
    izv.setInt("CPAR",2); 
  };
  saldo = izv.getBigDecimal("ID").add(izv.getBigDecimal("IP")).negate().abs();
  String konto = "";
  String vrdok = "";
  if (izv.getBigDecimal("ID").signum() != 0) {
    konto = "2200";
    vrdok = "IPL";
  } else {
    konto = "1200";
    vrdok = "UPL";
  }
  //  if (izv.getInt("BROJIZV") == 0) izv.setInt("BROJIZV",9999);
  k++;
  izv.setInt("BROJIZV",k);
  izv.setBigDecimal("SALDO",saldo);
  izv.setBigDecimal("SSALDO",saldo);
  izv.setBigDecimal("PVSALDO",saldo);
  izv.setBigDecimal("PVSSALDO",saldo);
  izv.setString("VRDOK",vrdok);
  izv.setString("BROJKONTA",konto);
  izv.setString("KNJIG","01");
  izv.setString("CORG","01");
  izv.setString("ZIRO","DOS");
  izv.setTimestamp("DATUMKNJ",izv.getTimestamp("DATDOK"));
  izv.setString("CSKSTAVKE",hr.restart.sk.raSaldaKonti.findCSK(izv));
  izv.setString("OZNVAL","kn");
  izv.setBigDecimal("TECAJ",Aus.one0);
}
try {
izv.saveChanges();
} catch (Exception e_i) {
  System.out.println("*********** IZV failed");
  e_i.printStackTrace();
}
