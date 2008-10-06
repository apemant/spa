/****license*****************************************************************
**   file: raValidacijaDocs.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.robno;
import hr.restart.util.Aus;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;

public class raValidacijaDocs {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  static raValidacijaDocs rvd;
  private TypeDoc TD = TypeDoc.getTypeDoc();
  private BigDecimal dozvoljeno_odstupanje = new BigDecimal("0.50");
  private BigDecimal testSkladPartIZL;
  private String vrsklad = "";
  private BigDecimal ppor ;
  private hr.restart.util.lookupData lD = hr.restart.util.lookupData.getlookupData();
  private boolean automatrepair = false;

  com.borland.dx.dataset.StorageDataSet errors ;
  Column iznosgreske;
  Column dokument;

  {
    errors = new com.borland.dx.dataset.StorageDataSet();
//    errors = new QueryDataSet();
    iznosgreske = dm.getStdoku().getColumn("IMAR").cloneColumn();
    iznosgreske.setColumnName("GRESKA");
    iznosgreske.setCaption("Iznos greške");

    dokument  = dm.getDoki().getColumn("OPIS").cloneColumn();
    dokument.setColumnName("DOKUMENT");
    dokument.setCaption("Dokument");
    errors.setColumns(new Column[] {
//      dm.getDoku().getColumn("CSKL").cloneColumn(),
//      dm.getMeskla().getColumn("CSKLIZ").cloneColumn(),
//      dm.getDoku().getColumn("VRDOK").cloneColumn(),
//      dm.getDoku().getColumn("GOD").cloneColumn(),
//      dm.getDoku().getColumn("BRDOK").cloneColumn(),
//      dm.getStdoku().getColumn("RBR").cloneColumn(),
      dokument,
      dm.getDoki().getColumn("OPIS").cloneColumn(),
      iznosgreske});
  }

  private raValidacijaDocs() {
    errors.open();
    errors.emptyAllRows();
  }

  public static raValidacijaDocs getraValidacijaDocs(){
    if (rvd == null ) rvd = new raValidacijaDocs();
    return rvd;
  }


  public void testPorav(DataSet ds) {
    BigDecimal porav;
    BigDecimal diopormar;
    BigDecimal dioporpor;
    BigDecimal skol;
    BigDecimal svc;
    BigDecimal smc;
    BigDecimal vc;
    BigDecimal mc;
    BigDecimal tmpBD;
    try {
      porav = ds.getBigDecimal("PORAV") ;
      diopormar = ds.getBigDecimal("DIOPORMAR");
      dioporpor = ds.getBigDecimal("DIOPORPOR");
      skol = ds.getBigDecimal("SKOL");
      svc = ds.getBigDecimal("SVC");
      smc = ds.getBigDecimal("SMC");
      vc = ds.getBigDecimal("VC");
      mc = ds.getBigDecimal("MC");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return;
    }

    if ("N".equalsIgnoreCase(getVrsklad())) {
      if (porav.doubleValue()>0) {
        insertErrors(ds,
               "Greška ZALIHA : postoji iznos PORAV a zaliha nabavna!!!",porav);
      }
      if (isAutomatrepair()) {
        ds.setBigDecimal("PORAV",Aus.zero2);
        ds.setBigDecimal("DIOPORPOR",Aus.zero2);
        ds.setBigDecimal("DIOPORMAR",Aus.zero2);
        ds.saveChanges();
      }
    }
    else if ("V".equalsIgnoreCase(getVrsklad())) {
      tmpBD = (vc.multiply(skol)).subtract(svc.multiply(skol));
      if (tmpBD.compareTo(porav)!=0) {
        insertErrors(ds,
           "Greška kalkulacija PORAV <> vc*skol - svc * skol !!!",dioporpor);
        if (isAutomatrepair()) {
          ds.setBigDecimal("PORAV",tmpBD);
          ds.setBigDecimal("DIOPORMAR",tmpBD);
          ds.setBigDecimal("DIOPORPOR",Aus.zero2);
          ds.saveChanges();
        }
      }
      if (dioporpor.doubleValue()>0) {
        insertErrors(ds,
                     "Greška ZALIHA : postoji iznos DIOPORPOR a zaliha veleprodajna!!!",dioporpor);
        if (isAutomatrepair()) {
          ds.setBigDecimal("DIOPORPOR",Aus.zero2);
          ds.saveChanges();
        }
      }
    }
    else if ("M".equalsIgnoreCase(getVrsklad())) {
      tmpBD = (mc.multiply(skol)).subtract(smc.multiply(skol));
      if (tmpBD.compareTo(porav)!=0) {
        insertErrors(ds,
           "Greška kalkulacija PORAV <> mc*skol - smc * skol !!! Porav - (mc*skol - smc * skol) = ",porav.subtract(tmpBD));
        if (isAutomatrepair()) {
          BigDecimal tmpBD2 = (vc.multiply(skol)).subtract(svc.multiply(skol));
          ds.setBigDecimal("PORAV",tmpBD);
          ds.setBigDecimal("DIOPORMAR",tmpBD2);
          ds.setBigDecimal("DIOPORPOR",tmpBD.subtract(tmpBD2));
          ds.saveChanges();
        }
      }
    }

    tmpBD = porav.subtract(diopormar.add(dioporpor));
    if (porav.compareTo(diopormar.add(dioporpor))!=0) {
        insertErrors(ds,
                     "Greška u matematici diopormar + dioporpor  = porav",
                     tmpBD);
        if (isAutomatrepair()) {
          BigDecimal tmpBD2 = (vc.multiply(skol)).subtract(svc.multiply(skol));
//          ds.setBigDecimal("PORAV",tmpBD);
          ds.setBigDecimal("DIOPORMAR",tmpBD2);
          ds.setBigDecimal("DIOPORPOR",ds.getBigDecimal("PORAV").subtract(tmpBD2));
          ds.saveChanges();
        }
    }
  }

  public static boolean isPoravOK(DataSet ds,String diopormar,String dioporpor,String porav){
   return (calcPorav(ds,diopormar,dioporpor,porav).doubleValue()!=0);
 }

 public static BigDecimal calcPorav(DataSet ds,String diopormar,String dioporpor,String porav){
   BigDecimal tmpBD = ds.getBigDecimal(diopormar);
   tmpBD = tmpBD.add(ds.getBigDecimal(dioporpor));
   tmpBD = ds.getBigDecimal(porav).subtract(tmpBD);
   return tmpBD;
 }

  public boolean testDocs(DataSet ds,boolean ulaz) {
    try {
      if (TD.isDocStdoku(ds.getString("VRDOK"))) {
        testStdoku(ds);
      }
      else if (TD.isDocStdoki(ds.getString("VRDOK"))) {
        testStdoki(ds);
      }
      else if (TD.isDocStmeskla(ds.getString("VRDOK"))) {
        if (ulaz) testStmesklaU(ds);
        else testStmesklaI(ds);
      }
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  public boolean testWithAllowedDiference(BigDecimal fortest) {

    return  (fortest.abs().compareTo(dozvoljeno_odstupanje) ==0 );

  }

  public boolean testSkladPartIZL(DataSet ds) {

    testSkladPartIZL = ds.getBigDecimal("IRAZ").subtract(ds.getBigDecimal("IPOR"));
    testSkladPartIZL = testSkladPartIZL.subtract(ds.getBigDecimal("IMAR"));
    testSkladPartIZL = testSkladPartIZL.subtract(ds.getBigDecimal("INAB"));
    return testWithAllowedDiference(testSkladPartIZL);

  }

  public BigDecimal getDozvoljeno_odstupanje() {
    return dozvoljeno_odstupanje;
  }

  public void setDozvoljeno_odstupanje(BigDecimal dozvoljeno_odstupanje) {
    this.dozvoljeno_odstupanje = dozvoljeno_odstupanje;
  }

  private void insertErrors(DataSet ds,String opis,BigDecimal bd) {

    errors.insertRow(true);
    errors.setString("OPIS",opis);
    String oppis = ds.getString("VRDOK")+"-";
    try {
      oppis = oppis+ds.getString("CSKLIZ")+"-";
    }
    catch (Exception ex) {}
    try {
      oppis = oppis+ds.getString("CSKLUL")+"-";
    }
    catch (Exception ex) {
      oppis = oppis+ds.getString("CSKL")+"-";
    }
    oppis = oppis+"/"+ds.getString("GOD")+"-"+ds.getInt("BRDOK")+"-"+ds.getShort("RBR");
    errors.setString("DOKUMENT",oppis);

//    errors.setString("VRDOK",ds.getString("VRDOK"));
//    errors.setString("GOD",ds.getString("GOD"));
//    try {
//      errors.setString("CSKL",ds.getString("CSKL"));
//      errors.setString("CSKLIZ",ds.getString("CSKL"));
//    }
//    catch (Exception ex) {
//      errors.setString("CSKL",ds.getString("CSKLUL"));
//      errors.setString("CSKLIZ",ds.getString("CSKLIZ"));
//    }
//    errors.setInt("BRDOK",ds.getInt("BRDOK"));
//    errors.setShort("RBR",ds.getShort("RBR"));
    errors.setBigDecimal("GRESKA",bd);
  }

  private String testSklad(String cskl) {
    return (hr.restart.util.Util.getNewQueryDataSet("select sklad.vrzal as vrzal from sklad where cskl='"+cskl+"'",true)).getString("VRZAL");
  }
  public boolean isAutomatrepair() {
    return automatrepair;
  }
  public void setAutomatrepair(boolean automatrepair) {
    this.automatrepair = automatrepair;
  }

  public static BigDecimal getSkladDifferance(DataSet ds,
      String izadiraz,String inab, String imarpop, String ipor) {

      BigDecimal tmpBD = ds.getBigDecimal(izadiraz);
      tmpBD = tmpBD.subtract(ds.getBigDecimal(inab));
      tmpBD = tmpBD.subtract(ds.getBigDecimal(imarpop));
      tmpBD = tmpBD.subtract(ds.getBigDecimal(ipor));
      return tmpBD;
  }

//  public void testMeskla() {}
  public String getVrsklad() {
    return vrsklad;
  }
  public void setVrsklad(String vrsklad) {
    this.vrsklad = vrsklad;
  }

  public void testStdoku(DataSet ds) {
    java.math.BigDecimal tmpBD;
    if (ds.getString("VRDOK").equalsIgnoreCase("PST")) {
      testTrebaBitiNula(ds,"PORAV");
      testTrebaBitiNula(ds,"DIOPORPOR");
      testTrebaBitiNula(ds,"DIOPORMAR");
      testTrebaBitiNula(ds,"SKOL");
      testTrebaBitiNula(ds,"SVC");
      testTrebaBitiNula(ds,"SMC");

    }
    if (TD.isDocSklad(ds.getString("VRDOK"))) {

      // provjerava da li je NC = 0
      testCijeneNula(ds,"NC");
      // provjerava da li je VC = 0
      testCijeneNula(ds,"VC");
      // provjerava da li je MC = 0
      testCijeneNula(ds,"MC");
      // provjerava da li je MC = 0
      testCijeneNula(ds,"ZC");
      // provjerava da li je ZC postavljen kako treba u odnosu na vrstu zalihe
      testCijenaZalihe(ds,"NC","VC","MC","ZC");

      // provjerava da li je kol*nc=inab
//       testKolicinaPutaCijena(ds,"kol","nc","inab");
      // provjerava da li je kol*vc=ibp
       testKolicinaPutaCijena(ds,"kol","vc","ibp");
      // provjerava da li je kol*mc=isp
       testKolicinaPutaCijena(ds,"kol","mc","isp");
       // provjerava da li je kol*zc=izad
       testKolicinaPutaCijena(ds,"kol","zc","izad");
       // provjera da li je IMARIZ i IPORIZ dobar prema zalihi <> 0
       testSkladZaMar_i_Por_premaZalihi(ds,"IMAR","IPOR");

      if (getVrsklad().equals("V") || getVrsklad().equals("M")) {
        tmpBD = ds.getBigDecimal("IBP");
        tmpBD = tmpBD.subtract(ds.getBigDecimal("INAB"));
        testPoljeiBD(ds,"IMAR",tmpBD,"Greška IMAR != kol*(vc-nc)");
      }
      if (getVrsklad().equals("M")) {
        tmpBD = ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("VC"));
        tmpBD = ds.getBigDecimal("IZAD").subtract(tmpBD);
        testPoljeiBD(ds,"IPOR",tmpBD,"Greška IPOR != kol*(mc-vc)");
      }
//      testpostotaka @todo



      if (TD.isDocFinanc(ds.getString("VRDOK")) && !ds.getString("VRDOK").equalsIgnoreCase("PST")) {

        if (isPoravOK(ds,"DIOPORMAR","DIOPORPOR","PORAV")) {

          insertErrors(ds,
                       "Greška u matematici diopormar + dioporpor  = porav",
                       calcPorav(ds,"DIOPORMAR","DIOPORPOR","PORAV"));

        }
        // idob - irab + izt = inab  // ?
        tmpBD = ds.getBigDecimal("IDOB");
        tmpBD = tmpBD.subtract(ds.getBigDecimal("IRAB"));
        tmpBD = tmpBD.add(ds.getBigDecimal("IZT"));
        tmpBD = tmpBD.subtract(ds.getBigDecimal("INAB"));

        if (!testWithAllowedDiference(tmpBD)) {
          insertErrors(ds,
                       "Greška u matematici idob - irab + izt = inab",
                       tmpBD);
          if (isAutomatrepair()) {
            ds.setBigDecimal("INAB",ds.getBigDecimal("INAB").add(tmpBD));
          }
        }
      }
      // provjera da li je inab + imar + ipor = izad ako ne valja popraviti izad
        testSuma(ds,new String[] {"inab","imar","ipor"},"izad","izad");

    }
  }

  public void testStdoki(DataSet ds) {
    java.math.BigDecimal tmpBD;


    if (!lD.raLocate(dm.getArtikli(),new String[]{"CART"},new String[]{String.valueOf(
    ds.getInt("CART"))})){
      insertErrors(ds,
         "Nepostoje\u0107i artikl cart="+ds.getInt("CART")+", oznaka="+ds.getString("CART1")+", BC="+
               ds.getString("BC"),Aus.zero2);
      return;
    }
    if (!lD.raLocate(dm.getPorezi(),new String[]{"CPOR"},new String[]{dm.getArtikli().getString("CPOR")})){
      insertErrors(ds,
         "Nepostoje\u0107a porezna grupa za artikl cart="+ds.getInt("CART")+", oznaka="+ds.getString("CART1")+", BC="+
               ds.getString("BC"),Aus.zero2);
      return;
    }


    if (TD.isDocSklad(ds.getString("VRDOK")) && raVart.isStanje(dm.getArtikli())) {
      if (!testSkladPartIZL(ds)) {
        insertErrors(ds,
                     "Greška u matematici IRAZ-INAB -IMAR -IPOR (+- odstupanje) != 0",
                     testSkladPartIZL);
        if (isAutomatrepair()) {
          if (!getVrsklad().equalsIgnoreCase("N")) {
            ds.setBigDecimal("IMAR",ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")));
            ds.setBigDecimal("IPOR",ds.getBigDecimal("IRAZ").subtract(ds.getBigDecimal("IBP")));
          }
          else {
            ds.setBigDecimal("IMAR",Aus.zero2);
            ds.setBigDecimal("IPOR",Aus.zero2);
          }
          ds.saveChanges();
        }
      }
      if (getVrsklad().equalsIgnoreCase("N")) {
        if (ds.getBigDecimal("IRAZ").doubleValue()!=ds.getBigDecimal("INAB").doubleValue()) {
          insertErrors(ds,
                       "IRAZ <> INAB a zaliha je nabavna IRAZ -INAB = ",
                       ds.getBigDecimal("IRAZ").subtract(ds.getBigDecimal("INAB")));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IRAZ",ds.getBigDecimal("INAB"));
            ds.setBigDecimal("IMAR",Aus.zero2);
            ds.setBigDecimal("IPOR",Aus.zero2);
            ds.saveChanges();
          }
        }
      } else if (getVrsklad().equalsIgnoreCase("V")) {
        if (ds.getBigDecimal("IRAZ").doubleValue()!=ds.getBigDecimal("IBP").doubleValue()) {
          insertErrors(ds,
                       "IRAZ <> IBP a zaliha je nabavna IRAZ -IBP = ",
                       ds.getBigDecimal("IRAZ").subtract(ds.getBigDecimal("IBP")));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IRAZ",ds.getBigDecimal("IBP"));
            ds.setBigDecimal("IMAR",ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")));
            ds.setBigDecimal("IPOR",Aus.zero2);
            ds.saveChanges();
          }
        }
      } else if (getVrsklad().equalsIgnoreCase("M")) {
        if (ds.getBigDecimal("IRAZ").doubleValue()!=ds.getBigDecimal("ISP").doubleValue()) {
          insertErrors(ds,
                       "IRAZ <> ISP a zaliha je nabavna IRAZ -ISP = ",
                       ds.getBigDecimal("IRAZ").subtract(ds.getBigDecimal("ISBP")));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IRAZ",ds.getBigDecimal("ISP"));
            ds.setBigDecimal("IMAR",ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")));
            ds.setBigDecimal("IPOR",ds.getBigDecimal("IRAZ").subtract(ds.getBigDecimal("IBP")));
            ds.saveChanges();
          }
        }
      }

      testSkladZaMar_i_Por_premaZalihi(ds,"IMAR","IPOR");

      if (!TD.isDocFinanc(ds.getString("VRDOK"))) {
        if (ds.getBigDecimal("POR1").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji POR1 !!!",ds.getBigDecimal("POR1"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("POR1",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("POR2").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji POR2 !!!",ds.getBigDecimal("POR2"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("POR2",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("POR3").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji POR3 !!!",ds.getBigDecimal("POR3"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("POR3",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("UIRAB").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji UIRAB !!!",ds.getBigDecimal("UIRAB"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("UIRAB",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("UIZT").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji UIZT !!!",ds.getBigDecimal("UIZT"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("UIZT",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("INETO").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji INETO !!!",ds.getBigDecimal("INETO"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("INETO",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("IPRODBP").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji IPRODBP !!!",ds.getBigDecimal("IPRODBP"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IPRODBP",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("IPRODSP").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška SKLAD : postoji IPRODSP !!!",ds.getBigDecimal("IPRODSP"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IPRODSP",Aus.zero2);
            ds.saveChanges();
          }
        }
      }
    }
    if (TD.isDocFinanc(ds.getString("VRDOK"))) {
      // provjera poreza POR1+POR2+POR3 = UIPOR
      tmpBD = ds.getBigDecimal("POR1");
      tmpBD = tmpBD.add(ds.getBigDecimal("POR2"));
      tmpBD = tmpBD.add(ds.getBigDecimal("POR3"));
      if (tmpBD.compareTo(ds.getBigDecimal("UIPOR"))!=0) {
        insertErrors(ds,
                     "Greška u matematici POR1+POR2+POR3 != UIPOR",
                     tmpBD.subtract(ds.getBigDecimal("UIPOR")));
        if (isAutomatrepair()) {
          ds.setBigDecimal("UIPOR",tmpBD);
          ds.saveChanges();
        }
      }

      // provjera iprodbp +por1+por2+por3 = iprodsp

      tmpBD = ds.getBigDecimal("IPRODBP");
      tmpBD = tmpBD.add(ds.getBigDecimal("POR1"));
      tmpBD = tmpBD.add(ds.getBigDecimal("POR2"));
      tmpBD = tmpBD.add(ds.getBigDecimal("POR3"));
      if (tmpBD.compareTo(ds.getBigDecimal("IPRODSP"))!=0) {
        insertErrors(ds,
                     "Greška u matematici IPRODBP+POR1+POR2+POR3 != IPRODSP",
                     tmpBD.subtract(ds.getBigDecimal("IPRODSP")));
        if (isAutomatrepair()) {
          ds.setBigDecimal("POR1",ds.getBigDecimal("IPRODSP").subtract(
              ds.getBigDecimal("IPRODBP")));
          ds.saveChanges();
        }
      }

      // provjera INETO - UIRAB + UIZT = IPRODBP
      tmpBD = ds.getBigDecimal("INETO");
      tmpBD = tmpBD.add(ds.getBigDecimal("UIZT"));
      tmpBD = tmpBD.subtract(ds.getBigDecimal("UIRAB"));

      if (tmpBD.compareTo(ds.getBigDecimal("IPRODBP"))!=0) {
        insertErrors(ds,
                     "Greška u matematici INETO - UIRAB + UIZT = IPRODBP",
                     tmpBD.subtract(ds.getBigDecimal("IPRODBP")));
      }
      if (!TD.isDocSklad(ds.getString("VRDOK"))) {
        if (ds.getBigDecimal("INAB").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška FINANC : postoji INAB !!!",ds.getBigDecimal("INAB"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("INAB",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("IMAR").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška FINANC : postoji IMAR !!!",ds.getBigDecimal("IMAR"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IMAR",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("IBP").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška FINANC : postoji IBP !!!",ds.getBigDecimal("IBP"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IBP",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("IPOR").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška FINANC : postoji IPOR !!!",ds.getBigDecimal("IPOR"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IPOR",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("ISP").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška FINANC : postoji ISP !!!",ds.getBigDecimal("ISP"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("ISP",Aus.zero2);
            ds.saveChanges();
          }
        }
        if (ds.getBigDecimal("IRAZ").doubleValue()!=0) {
          insertErrors(ds,
                 "Greška FINANC : postoji IRAZ !!!",ds.getBigDecimal("IRAZ"));
          if (isAutomatrepair()) {
            ds.setBigDecimal("IRAZ",Aus.zero2);
            ds.saveChanges();
          }
        }
      }
    }
  }
  public void testStmesklaU(DataSet ds){

    if (ds.getString("VRDOK").equalsIgnoreCase("MEU")) {
      testTrebaBitiNula(ds,"INABIZ");
      testTrebaBitiNula(ds,"IMARIZ");
      testTrebaBitiNula(ds,"IPORIZ");
      testTrebaBitiNula(ds,"ZADRAZIZ");
    }
    // provjerava da li je NC = 0
    testCijeneNula(ds,"NC");
    // provjerava da li je VC = 0
    testCijeneNula(ds,"VC");
    // provjerava da li je MC = 0
    testCijeneNula(ds,"MC");
    // provjerava da li je ZCUL postavljen kako treba u odnosu na vrstu zalihe
    testCijenaZalihe(ds,"NC","VC","MC","ZCUL");
    // provjerava da li je kol*nc=inabiz
    testKolicinaPutaCijena(ds,"kol","nc","inabul");
    // provjerava da li je kol*zcul=zadrazul
    testKolicinaPutaCijena(ds,"kol","zcul","zadrazul");
    // provjera da li je IMARUL i IPORUL dobar prema zalihi <> 0
    testSkladZaMar_i_Por_premaZalihi(ds,"IMARUL","IPORUL");

    if (isPoravOK(ds,"DIOPORMAR","DIOPORPOR","PORAV")) {
        insertErrors(ds,
                     "Greška u matematici diopormar + dioporpor  = porav",
                     calcPorav(ds,"DIOPORMAR","DIOPORPOR","PORAV"));
    }


    // provjera da li je inabul + imarul + iporul = zadrazul ako ne valja popraviti imariz
    testSuma(ds,new String[] {"inabul","imarul","iporul"},"zadrazul","imarul");
    // provjera diopormar + dioporpor = porav
    testPorav(ds);
  }

  public void testStmesklaI(DataSet ds){
    // provjera postoje li iznosi u ulaznoj strani
    if (ds.getString("VRDOK").equalsIgnoreCase("MEI")) {
      testTrebaBitiNula(ds,"INABUL");
      testTrebaBitiNula(ds,"IMARUL");
      testTrebaBitiNula(ds,"IPORUL");
      testTrebaBitiNula(ds,"PORAV");
      testTrebaBitiNula(ds,"DIOPORMAR");
      testTrebaBitiNula(ds,"DIOPORPOR");
      testTrebaBitiNula(ds,"ZADRAZUL");
    }
    // provjerava da li je NC = 0
    testCijeneNula(ds,"NC");
    // provjerava da li je VC = 0
    testCijeneNula(ds,"VC");
    // provjerava da li je MC = 0
    testCijeneNula(ds,"MC");
    // provjerava da li je ZC postavljen kako treba u odnosu na vrstu zalihe
    testCijenaZalihe(ds,"NC","VC","MC","ZC");
    // provjerava da li je kol*nc=inabiz
    testKolicinaPutaCijena(ds,"kol","nc","inabiz");
    // provjerava da li je kol*zc=zadraziz
    testKolicinaPutaCijena(ds,"kol","zc","zadraziz");
    // provjera da li je IMARIZ i IPORIZ dobar prema zalihi <> 0
    testSkladZaMar_i_Por_premaZalihi(ds,"IMARIZ","IPORIZ");
    if (getVrsklad().equals("V") || getVrsklad().equals("M")) {
      java.math.BigDecimal tmpBD = ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("VC"));
      tmpBD = tmpBD.subtract(ds.getBigDecimal("INABIZ"));
      testPoljeiBD(ds,"IMARIZ",tmpBD,"Greška IMARIZ != kol*(vc-nc)");
    }
    if (getVrsklad().equals("M")) {
      java.math.BigDecimal tmpBD = ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("VC"));
      tmpBD = ds.getBigDecimal("ZADRAZIZ").subtract(tmpBD);
      testPoljeiBD(ds,"IPORIZ",tmpBD,"Greška IPORIZ != kol*(mc-vc)");
    }
    // provjera da li je inabiz + imariz + iporiz = zadraziz ako ne valja popraviti imariz
    testSuma(ds,new String[] {"inabiz","imariz","iporiz"},"zadraziz","imariz");
  }

  private void testPoljeiBD(DataSet ds,String polje,java.math.BigDecimal tmpBD,String poruka) {
    if (ds.getBigDecimal(polje).doubleValue() != tmpBD.doubleValue()) {
      insertErrors(ds,poruka,
                   ds.getBigDecimal(polje).subtract(tmpBD));
      if (isAutomatrepair()) {
        ds.setBigDecimal(polje,tmpBD);
        ds.saveChanges();
      }
    }
  }

  private void testSkladZaMar_i_Por_premaZalihi(DataSet ds,String poljemar,String poljepor) {

    if (getVrsklad().equals("N")) {
      if (ds.getBigDecimal(poljemar).doubleValue()!=0) {
        insertErrors(ds,
               "Greška ZALIHA : postoji "+poljemar+" a zaliha nabavna!!!",ds.getBigDecimal(poljemar));
        if (isAutomatrepair()) {
          ds.setBigDecimal(poljemar,Aus.zero2);
          ds.saveChanges();
        }
      }
      if (ds.getBigDecimal(poljepor).doubleValue()!=0) {
        insertErrors(ds,
               "Greška ZALIHA : postoji "+poljepor+" a zaliha nabavna!!!",ds.getBigDecimal(poljepor));
        if (isAutomatrepair()) {
          ds.setBigDecimal(poljepor,Aus.zero2);
          ds.saveChanges();
        }
      }
    }
    else if (getVrsklad().equals("V")) {
      if (ds.getBigDecimal(poljemar).doubleValue()==0) {
        insertErrors(ds,
               "Greška ZALIHA : ne postoji "+poljemar+" a zaliha maloprodajna!!!",ds.getBigDecimal(poljemar));
        if (isAutomatrepair()) {
          ds.setBigDecimal(poljemar,ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")));
          ds.saveChanges();
        }
      }
      if (ds.getBigDecimal(poljepor).doubleValue()!=0) {
        insertErrors(ds,
               "Greška ZALIHA : postoji "+poljepor+" a zaliha veleprodajna!!!",ds.getBigDecimal(poljepor));
        if (isAutomatrepair()) {
          ds.setBigDecimal(poljepor,Aus.zero2);
          ds.saveChanges();
        }
      }
    }
    else if (getVrsklad().equals("M")) {
      if (ds.getBigDecimal(poljemar).doubleValue()==0) {
        insertErrors(ds,
               "Greška ZALIHA : ne postoji "+poljemar+" a zaliha maloprodajna!!!",ds.getBigDecimal(poljemar));
        if (isAutomatrepair()) {
          ds.setBigDecimal(poljemar,ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")));
          ds.saveChanges();
        }
      }
      if (ds.getBigDecimal(poljepor).doubleValue()==0) {
        insertErrors(ds,
               "Greška ZALIHA : ne postoji "+poljepor+" a zaliha maloprodajna!!!",ds.getBigDecimal(poljepor));
        if (isAutomatrepair()) {
          ds.setBigDecimal(poljepor,ds.getBigDecimal("ISP").subtract(ds.getBigDecimal("IBP")));
          ds.saveChanges();
        }
      }
    }
    else {
      insertErrors(ds,
           "Kriva definicija skladišta vrzal = "+getVrsklad(),
           Aus.zero2);
        }
  }
//// private utilitiji za skracenje muke

  private void testSuma(DataSet ds,String[] sumandi,String suma,String stopopraviti) {
    java.math.BigDecimal tmpBD = new java.math.BigDecimal("0.00");
    String errrMSG = "Greška u matematici ";
    for (int i = 0;i<sumandi.length;i++) {
      tmpBD = tmpBD.add(ds.getBigDecimal(sumandi[i]));
      errrMSG = errrMSG +"+"+sumandi[i];
    }

    if (!testWithAllowedDiference(ds.getBigDecimal(suma).subtract(tmpBD))) {
      insertErrors(ds,
             errrMSG + "(+- odstupanje) = "+suma+ " Ne stima za ->" ,
                   ds.getBigDecimal(suma).subtract(tmpBD));
//      if (isAutomatrepair()) {
//        ds.setBigDecimal(stopopraviti,ds.getBigDecimal(stopopraviti).add(
//                                      ds.getBigDecimal(suma).subtract(tmpBD)));
//        ds.saveChanges();
//      }
    }
  }



  private void testTrebaBitiNula(DataSet ds,String polje) {
    if (ds.isNull(polje)) {
      insertErrors(ds,
                   "Greška "+ds.getString("VRDOK")+" : polje "+polje+" = null !!!",ds.getBigDecimal(polje));
      if (isAutomatrepair()) {
        ds.setBigDecimal(polje,Aus.zero2);
        ds.saveChanges();
      }
    }

    if (ds.getBigDecimal(polje).doubleValue()!=0) {
      insertErrors(ds,
                   "Greška "+ds.getString("VRDOK")+" : postoji "+polje+" !!!",ds.getBigDecimal(polje));
      if (isAutomatrepair()) {
        ds.setBigDecimal(polje,Aus.zero2);
        ds.saveChanges();
      }
    }
  }

  private void testKolicinaPutaCijena(DataSet ds,String kol,String cijena,String iznos) {

    if (ds.getBigDecimal(kol).multiply(ds.getBigDecimal(cijena)).compareTo(
        ds.getBigDecimal(iznos)) !=0) {
        insertErrors(ds,
                   "Greška "+kol+"*"+cijena+" != "+iznos+" "+iznos+"-("+kol+"*"+cijena+")=",
                   ds.getBigDecimal(iznos).subtract(ds.getBigDecimal(kol).multiply(ds.getBigDecimal(cijena))));
     }
     if (isAutomatrepair()) {
       ds.setBigDecimal(iznos,ds.getBigDecimal(kol).multiply(ds.getBigDecimal(cijena)));
       ds.saveChanges();
     }
  }

  public void testCijeneNula(DataSet ds,String cijena) {
    if (ds.getBigDecimal(cijena).doubleValue()==0) {
      insertErrors(ds,
                   "Greška "+cijena+" = 0 ! Ne mogu popraviti ovu grešku !!!",
                   ds.getBigDecimal(cijena));
    }
  }

  public void testCijenaZalihe(DataSet ds,String nc,String vc,String mc, String zc) {
    if (getVrsklad().equals("N") && ds.getBigDecimal(nc).doubleValue()!=0) {
      testZC(ds,zc,nc );
    }
    else if (getVrsklad().equals("V") && ds.getBigDecimal(vc).doubleValue()!=0) {
      testZC(ds,zc,vc);
    }
    else if (getVrsklad().equals("M") && ds.getBigDecimal(mc).doubleValue()!=0) {
      testZC(ds,zc,mc);
    }
  }

  private void testZC(DataSet ds, String zc, String forZC) {
    if (ds.getBigDecimal(forZC).compareTo(ds.getBigDecimal(zc))!=0) {
      insertErrors(ds,
             "Greška ! Cijena zalihe "+zc+" != "+forZC+ " a zaliha je "+getVrskladOpis()+
             " ! "+zc+" = "+ds.getBigDecimal(zc)+forZC+
             " = "+ds.getBigDecimal(forZC),
             ds.getBigDecimal(forZC).subtract(ds.getBigDecimal(zc)));

      if (isAutomatrepair()) {
        ds.setBigDecimal(zc,ds.getBigDecimal(forZC));
        ds.saveChanges();
      }
    }
  }

  private String getVrskladOpis() {
    if (getVrsklad().equalsIgnoreCase("N")) return "nabavna";
    else if (getVrsklad().equalsIgnoreCase("V")) return " prodajna bez poreza ";
    else if (getVrsklad().equalsIgnoreCase("M")) return " prodajna s porezom ";
    else return " !!!neispravna vrsta zalihe!!! ";
  }






}