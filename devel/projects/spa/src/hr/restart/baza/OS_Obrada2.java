/****license*****************************************************************
**   file: OS_Obrada2.java
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
package hr.restart.baza;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;



public class OS_Obrada2 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Obrada2 OS_Obrada2class;

  QueryDataSet osob2 = new QueryDataSet();

  Column osob2LOKK = new Column();
  Column osob2AKTIV = new Column();
  Column osob2CORG = new Column();
  Column osob2NAZIV = new Column();
  Column osob2INVBROJ = new Column();
  Column osob2NAZSREDSTVA = new Column();
  Column osob2COBJEKT = new Column();
  Column osob2CLOKACIJE = new Column();
  Column osob2CARTIKLA = new Column();
  Column osob2CPAR = new Column();
  Column osob2BROJKONTA = new Column();
  Column osob2CGRUPE = new Column();
  Column osob2ZAKSTOPA = new Column();
  Column osob2ODLSTOPA = new Column();
  Column osob2OTPSTOPA = new Column();
  Column osob2CSKUPINE = new Column();
  Column osob2KOEFICIJENT = new Column();
  Column osob2MJESEC = new Column();
  Column osob2PORIJEKLO = new Column();
  Column osob2RADNIK = new Column();
  Column osob2DATNABAVE = new Column();
  Column osob2DATAKTIVIRANJA = new Column();
  Column osob2DATPROMJENE = new Column();
  Column osob2DATLIKVIDACIJE = new Column();
  Column osob2OSNOVICA = new Column();
  Column osob2ISPRAVAK = new Column();
  Column osob2SADVRIJED = new Column();
  Column osob2REVOSN = new Column();
  Column osob2REVISP = new Column();
  Column osob2REVSAD = new Column();
  Column osob2AMORTIZACIJA = new Column();
  Column osob2AMOR1 = new Column();
  Column osob2AMOR2 = new Column();
  Column osob2PAMORTIZACIJA = new Column();
  Column osob2PAMOR1 = new Column();
  Column osob2PAMOR2 = new Column();
  Column osob2REVAMOR = new Column();
  Column osob2PREBACAM = new Column();
  Column osob2VK = new Column();

  public static OS_Obrada2 getDataModule() {
    if (OS_Obrada2class == null) {
      OS_Obrada2class = new OS_Obrada2();
    }
    return OS_Obrada2class;
  }

  public QueryDataSet getQueryDataSet() {
    return osob2;
  }

  public OS_Obrada2() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osob2LOKK.setCaption("Status zauzetosti");
    osob2LOKK.setColumnName("LOKK");
    osob2LOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2LOKK.setPrecision(1);
    osob2LOKK.setTableName("OS_OBRADA2");
    osob2LOKK.setServerColumnName("LOKK");
    osob2LOKK.setSqlType(1);
    osob2LOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob2LOKK.setDefault("N");
    osob2AKTIV.setCaption("Aktivan - neaktivan");
    osob2AKTIV.setColumnName("AKTIV");
    osob2AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2AKTIV.setPrecision(1);
    osob2AKTIV.setTableName("OS_OBRADA2");
    osob2AKTIV.setServerColumnName("AKTIV");
    osob2AKTIV.setSqlType(1);
    osob2AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob2AKTIV.setDefault("D");
    osob2CORG.setCaption("OJ");
    osob2CORG.setColumnName("CORG");
    osob2CORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2CORG.setPrecision(12);
    osob2CORG.setRowId(true);
    osob2CORG.setTableName("OS_OBRADA2");
    osob2CORG.setServerColumnName("CORG");
    osob2CORG.setSqlType(1);
    osob2NAZIV.setCaption("Naziv");
    osob2NAZIV.setColumnName("NAZIV");
    osob2NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2NAZIV.setPrecision(50);
    osob2NAZIV.setTableName("OS_OBRADA2");
    osob2NAZIV.setServerColumnName("NAZIV");
    osob2NAZIV.setSqlType(1);
    osob2NAZIV.setWidth(30);
    osob2INVBROJ.setCaption("IBroj");
    osob2INVBROJ.setColumnName("INVBROJ");
    osob2INVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2INVBROJ.setPrecision(10);
    osob2INVBROJ.setRowId(true);
    osob2INVBROJ.setTableName("OS_OBRADA2");
    osob2INVBROJ.setServerColumnName("INVBROJ");
    osob2INVBROJ.setSqlType(1);
    osob2NAZSREDSTVA.setCaption("Naziv sredstva");
    osob2NAZSREDSTVA.setColumnName("NAZSREDSTVA");
    osob2NAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2NAZSREDSTVA.setPrecision(50);
    osob2NAZSREDSTVA.setTableName("OS_OBRADA2");
    osob2NAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    osob2NAZSREDSTVA.setSqlType(1);
    osob2NAZSREDSTVA.setWidth(30);
    osob2COBJEKT.setCaption("Objekt");
    osob2COBJEKT.setColumnName("COBJEKT");
    osob2COBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2COBJEKT.setPrecision(6);
    osob2COBJEKT.setTableName("OS_OBRADA2");
    osob2COBJEKT.setServerColumnName("COBJEKT");
    osob2COBJEKT.setSqlType(1);
    osob2CLOKACIJE.setCaption("Lokacija");
    osob2CLOKACIJE.setColumnName("CLOKACIJE");
    osob2CLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2CLOKACIJE.setPrecision(12);
    osob2CLOKACIJE.setTableName("OS_OBRADA2");
    osob2CLOKACIJE.setServerColumnName("CLOKACIJE");
    osob2CLOKACIJE.setSqlType(1);
    osob2CARTIKLA.setCaption("Artikl");
    osob2CARTIKLA.setColumnName("CARTIKLA");
    osob2CARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2CARTIKLA.setPrecision(6);
    osob2CARTIKLA.setTableName("OS_OBRADA2");
    osob2CARTIKLA.setServerColumnName("CARTIKLA");
    osob2CARTIKLA.setSqlType(1);
    osob2CPAR.setCaption("Partner");
    osob2CPAR.setColumnName("CPAR");
    osob2CPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osob2CPAR.setPrecision(6);
    osob2CPAR.setTableName("OS_OBRADA2");
    osob2CPAR.setServerColumnName("CPAR");
    osob2CPAR.setSqlType(4);
    osob2CPAR.setWidth(6);
    osob2BROJKONTA.setCaption("Konto");
    osob2BROJKONTA.setColumnName("BROJKONTA");
    osob2BROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2BROJKONTA.setPrecision(8);
    osob2BROJKONTA.setTableName("OS_OBRADA2");
    osob2BROJKONTA.setServerColumnName("BROJKONTA");
    osob2BROJKONTA.setSqlType(1);
    osob2CGRUPE.setCaption("Amort. grupa");
    osob2CGRUPE.setColumnName("CGRUPE");
    osob2CGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2CGRUPE.setPrecision(6);
    osob2CGRUPE.setTableName("OS_OBRADA2");
    osob2CGRUPE.setServerColumnName("CGRUPE");
    osob2CGRUPE.setSqlType(1);
    osob2ZAKSTOPA.setCaption("Zakonska stopa");
    osob2ZAKSTOPA.setColumnName("ZAKSTOPA");
    osob2ZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2ZAKSTOPA.setPrecision(10);
    osob2ZAKSTOPA.setScale(4);
    osob2ZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osob2ZAKSTOPA.setDefault("0");
    osob2ZAKSTOPA.setTableName("OS_OBRADA2");
    osob2ZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osob2ZAKSTOPA.setSqlType(2);
    osob2ODLSTOPA.setCaption("Stopa po odluci");
    osob2ODLSTOPA.setColumnName("ODLSTOPA");
    osob2ODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2ODLSTOPA.setPrecision(10);
    osob2ODLSTOPA.setScale(4);
    osob2ODLSTOPA.setDisplayMask("###,###,##0.0000");
    osob2ODLSTOPA.setDefault("0");
    osob2ODLSTOPA.setTableName("OS_OBRADA2");
    osob2ODLSTOPA.setServerColumnName("ODLSTOPA");
    osob2ODLSTOPA.setSqlType(2);
    osob2OTPSTOPA.setCaption("Otp. stopa");
    osob2OTPSTOPA.setColumnName("OTPSTOPA");
    osob2OTPSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2OTPSTOPA.setPrecision(10);
    osob2OTPSTOPA.setScale(4);
    osob2OTPSTOPA.setDisplayMask("###,###,##0.0000");
    osob2OTPSTOPA.setDefault("0");
    osob2OTPSTOPA.setTableName("OS_OBRADA2");
    osob2OTPSTOPA.setServerColumnName("OTPSTOPA");
    osob2OTPSTOPA.setSqlType(2);
    osob2CSKUPINE.setCaption("Rev. skupina");
    osob2CSKUPINE.setColumnName("CSKUPINE");
    osob2CSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2CSKUPINE.setPrecision(6);
    osob2CSKUPINE.setTableName("OS_OBRADA2");
    osob2CSKUPINE.setServerColumnName("CSKUPINE");
    osob2CSKUPINE.setSqlType(1);
    osob2KOEFICIJENT.setCaption("Koeficijent");
    osob2KOEFICIJENT.setColumnName("KOEFICIJENT");
    osob2KOEFICIJENT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2KOEFICIJENT.setPrecision(10);
    osob2KOEFICIJENT.setScale(2);
    osob2KOEFICIJENT.setDisplayMask("###,###,##0.00");
    osob2KOEFICIJENT.setDefault("0");
    osob2KOEFICIJENT.setTableName("OS_OBRADA2");
    osob2KOEFICIJENT.setServerColumnName("KOEFICIJENT");
    osob2KOEFICIJENT.setSqlType(2);
    osob2MJESEC.setCaption("Mjesec");
    osob2MJESEC.setColumnName("MJESEC");
    osob2MJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2MJESEC.setPrecision(2);
    osob2MJESEC.setTableName("OS_OBRADA2");
    osob2MJESEC.setServerColumnName("MJESEC");
    osob2MJESEC.setSqlType(1);
    osob2PORIJEKLO.setCaption("Porijeklo");
    osob2PORIJEKLO.setColumnName("PORIJEKLO");
    osob2PORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2PORIJEKLO.setPrecision(2);
    osob2PORIJEKLO.setTableName("OS_OBRADA2");
    osob2PORIJEKLO.setServerColumnName("PORIJEKLO");
    osob2PORIJEKLO.setSqlType(1);
    osob2RADNIK.setCaption("Radnik");
    osob2RADNIK.setColumnName("RADNIK");
    osob2RADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2RADNIK.setPrecision(6);
    osob2RADNIK.setTableName("OS_OBRADA2");
    osob2RADNIK.setServerColumnName("RADNIK");
    osob2RADNIK.setSqlType(1);
    osob2DATNABAVE.setCaption("Datum nabave");
    osob2DATNABAVE.setColumnName("DATNABAVE");
    osob2DATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob2DATNABAVE.setPrecision(8);
    osob2DATNABAVE.setDisplayMask("dd-MM-yyyy");
//    osob2DATNABAVE.setEditMask("dd-MM-yyyy");
    osob2DATNABAVE.setTableName("OS_OBRADA2");
    osob2DATNABAVE.setServerColumnName("DATNABAVE");
    osob2DATNABAVE.setSqlType(93);
    osob2DATNABAVE.setWidth(10);
    osob2DATAKTIVIRANJA.setCaption("Datum aktiviranja");
    osob2DATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    osob2DATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob2DATAKTIVIRANJA.setPrecision(8);
    osob2DATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    osob2DATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    osob2DATAKTIVIRANJA.setTableName("OS_OBRADA2");
    osob2DATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    osob2DATAKTIVIRANJA.setSqlType(93);
    osob2DATAKTIVIRANJA.setWidth(10);
    osob2DATPROMJENE.setCaption("Datum promjene");
    osob2DATPROMJENE.setColumnName("DATPROMJENE");
    osob2DATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob2DATPROMJENE.setPrecision(8);
    osob2DATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    osob2DATPROMJENE.setEditMask("dd-MM-yyyy");
    osob2DATPROMJENE.setTableName("OS_OBRADA2");
    osob2DATPROMJENE.setServerColumnName("DATPROMJENE");
    osob2DATPROMJENE.setSqlType(93);
    osob2DATPROMJENE.setWidth(10);
    osob2DATLIKVIDACIJE.setCaption("Datum likvidacije");
    osob2DATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    osob2DATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob2DATLIKVIDACIJE.setPrecision(8);
    osob2DATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    osob2DATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    osob2DATLIKVIDACIJE.setTableName("OS_OBRADA2");
    osob2DATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    osob2DATLIKVIDACIJE.setSqlType(93);
    osob2DATLIKVIDACIJE.setWidth(10);
    osob2OSNOVICA.setCaption("Osnovica");
    osob2OSNOVICA.setColumnName("OSNOVICA");
    osob2OSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2OSNOVICA.setPrecision(17);
    osob2OSNOVICA.setScale(2);
    osob2OSNOVICA.setDisplayMask("###,###,##0.00");
    osob2OSNOVICA.setDefault("0");
    osob2OSNOVICA.setTableName("OS_OBRADA2");
    osob2OSNOVICA.setServerColumnName("OSNOVICA");
    osob2OSNOVICA.setSqlType(2);
    osob2ISPRAVAK.setCaption("Ispravak");
    osob2ISPRAVAK.setColumnName("ISPRAVAK");
    osob2ISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2ISPRAVAK.setPrecision(17);
    osob2ISPRAVAK.setScale(2);
    osob2ISPRAVAK.setDisplayMask("###,###,##0.00");
    osob2ISPRAVAK.setDefault("0");
    osob2ISPRAVAK.setTableName("OS_OBRADA2");
    osob2ISPRAVAK.setServerColumnName("ISPRAVAK");
    osob2ISPRAVAK.setSqlType(2);
    osob2SADVRIJED.setCaption("Vrijednost");
    osob2SADVRIJED.setColumnName("SADVRIJED");
    osob2SADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2SADVRIJED.setPrecision(17);
    osob2SADVRIJED.setScale(2);
    osob2SADVRIJED.setDisplayMask("###,###,##0.00");
    osob2SADVRIJED.setDefault("0");
    osob2SADVRIJED.setTableName("OS_OBRADA2");
    osob2SADVRIJED.setServerColumnName("SADVRIJED");
    osob2SADVRIJED.setSqlType(2);
    osob2REVOSN.setCaption("Rev. isnovice");
    osob2REVOSN.setColumnName("REVOSN");
    osob2REVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2REVOSN.setPrecision(17);
    osob2REVOSN.setScale(2);
    osob2REVOSN.setDisplayMask("###,###,##0.00");
    osob2REVOSN.setDefault("0");
    osob2REVOSN.setTableName("OS_OBRADA2");
    osob2REVOSN.setServerColumnName("REVOSN");
    osob2REVOSN.setSqlType(2);
    osob2REVISP.setCaption("Rev. ispravka");
    osob2REVISP.setColumnName("REVISP");
    osob2REVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2REVISP.setPrecision(17);
    osob2REVISP.setScale(2);
    osob2REVISP.setDisplayMask("###,###,##0.00");
    osob2REVISP.setDefault("0");
    osob2REVISP.setTableName("OS_OBRADA2");
    osob2REVISP.setServerColumnName("REVISP");
    osob2REVISP.setSqlType(2);
    osob2REVSAD.setCaption("Revalorizacija");
    osob2REVSAD.setColumnName("REVSAD");
    osob2REVSAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2REVSAD.setPrecision(17);
    osob2REVSAD.setScale(2);
    osob2REVSAD.setDisplayMask("###,###,##0.00");
    osob2REVSAD.setDefault("0");
    osob2REVSAD.setTableName("OS_OBRADA2");
    osob2REVSAD.setServerColumnName("REVSAD");
    osob2REVSAD.setSqlType(2);
    osob2AMORTIZACIJA.setCaption("Amortizacija");
    osob2AMORTIZACIJA.setColumnName("AMORTIZACIJA");
    osob2AMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2AMORTIZACIJA.setPrecision(17);
    osob2AMORTIZACIJA.setScale(2);
    osob2AMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob2AMORTIZACIJA.setDefault("0");
    osob2AMORTIZACIJA.setTableName("OS_OBRADA2");
    osob2AMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    osob2AMORTIZACIJA.setSqlType(2);
    osob2AMOR1.setCaption("Amor1");
    osob2AMOR1.setColumnName("AMOR1");
    osob2AMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2AMOR1.setPrecision(17);
    osob2AMOR1.setScale(2);
    osob2AMOR1.setDisplayMask("###,###,##0.00");
    osob2AMOR1.setDefault("0");
    osob2AMOR1.setTableName("OS_OBRADA2");
    osob2AMOR1.setServerColumnName("AMOR1");
    osob2AMOR1.setSqlType(2);
    osob2AMOR2.setCaption("Amor2");
    osob2AMOR2.setColumnName("AMOR2");
    osob2AMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2AMOR2.setPrecision(17);
    osob2AMOR2.setScale(2);
    osob2AMOR2.setDisplayMask("###,###,##0.00");
    osob2AMOR2.setDefault("0");
    osob2AMOR2.setTableName("OS_OBRADA2");
    osob2AMOR2.setServerColumnName("AMOR2");
    osob2AMOR2.setSqlType(2);
    osob2PAMORTIZACIJA.setCaption("Pov. amortizacija");
    osob2PAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    osob2PAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2PAMORTIZACIJA.setPrecision(17);
    osob2PAMORTIZACIJA.setScale(2);
    osob2PAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob2PAMORTIZACIJA.setDefault("0");
    osob2PAMORTIZACIJA.setTableName("OS_OBRADA2");
    osob2PAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    osob2PAMORTIZACIJA.setSqlType(2);
    osob2PAMOR1.setCaption("Pamor1");
    osob2PAMOR1.setColumnName("PAMOR1");
    osob2PAMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2PAMOR1.setPrecision(17);
    osob2PAMOR1.setScale(2);
    osob2PAMOR1.setDisplayMask("###,###,##0.00");
    osob2PAMOR1.setDefault("0");
    osob2PAMOR1.setTableName("OS_OBRADA2");
    osob2PAMOR1.setServerColumnName("PAMOR1");
    osob2PAMOR1.setSqlType(2);
    osob2PAMOR2.setCaption("Pamor2");
    osob2PAMOR2.setColumnName("PAMOR2");
    osob2PAMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2PAMOR2.setPrecision(17);
    osob2PAMOR2.setScale(2);
    osob2PAMOR2.setDisplayMask("###,###,##0.00");
    osob2PAMOR2.setDefault("0");
    osob2PAMOR2.setTableName("OS_OBRADA2");
    osob2PAMOR2.setServerColumnName("PAMOR2");
    osob2PAMOR2.setSqlType(2);
    osob2REVAMOR.setCaption("Rev. amortizacije");
    osob2REVAMOR.setColumnName("REVAMOR");
    osob2REVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2REVAMOR.setPrecision(17);
    osob2REVAMOR.setScale(2);
    osob2REVAMOR.setDisplayMask("###,###,##0.00");
    osob2REVAMOR.setDefault("0");
    osob2REVAMOR.setTableName("OS_OBRADA2");
    osob2REVAMOR.setServerColumnName("REVAMOR");
    osob2REVAMOR.setSqlType(2);
    osob2PREBACAM.setCaption("Preb. amortizacije");
    osob2PREBACAM.setColumnName("PREBACAM");
    osob2PREBACAM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob2PREBACAM.setPrecision(17);
    osob2PREBACAM.setScale(2);
    osob2PREBACAM.setDisplayMask("###,###,##0.00");
    osob2PREBACAM.setDefault("0");
    osob2PREBACAM.setTableName("OS_OBRADA2");
    osob2PREBACAM.setServerColumnName("PREBACAM");
    osob2PREBACAM.setSqlType(2);
    osob2VK.setCaption("");
    osob2VK.setColumnName("VK");
    osob2VK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob2VK.setPrecision(3);
    osob2VK.setTableName("OS_OBRADA2");
    osob2VK.setServerColumnName("VK");
    osob2VK.setSqlType(1);
    osob2.setResolver(dm.getQresolver());
    osob2.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Obrada2", null, true, Load.ALL));
 setColumns(new Column[] {osob2LOKK, osob2AKTIV, osob2CORG, osob2NAZIV, osob2INVBROJ, osob2NAZSREDSTVA, osob2COBJEKT, osob2CLOKACIJE, osob2CARTIKLA,
        osob2CPAR, osob2BROJKONTA, osob2CGRUPE, osob2ZAKSTOPA, osob2ODLSTOPA, osob2OTPSTOPA, osob2CSKUPINE, osob2KOEFICIJENT, osob2MJESEC, osob2PORIJEKLO,
        osob2RADNIK, osob2DATNABAVE, osob2DATAKTIVIRANJA, osob2DATPROMJENE, osob2DATLIKVIDACIJE, osob2OSNOVICA, osob2ISPRAVAK, osob2SADVRIJED, osob2REVOSN,
        osob2REVISP, osob2REVSAD, osob2AMORTIZACIJA, osob2AMOR1, osob2AMOR2, osob2PAMORTIZACIJA, osob2PAMOR1, osob2PAMOR2, osob2REVAMOR, osob2PREBACAM, osob2VK});
  }

  public void setall() {

    ddl.create("OS_Obrada2")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("naziv", 50)
       .addChar("invbroj", 10, true)
       .addChar("nazsredstva", 50)
       .addChar("cobjekt", 6)
       .addChar("clokacije", 12)
       .addChar("cartikla", 6)
       .addInteger("cpar", 6)
       .addChar("brojkonta", 8)
       .addChar("cgrupe", 6)
       .addFloat("zakstopa", 10, 4)
       .addFloat("odlstopa", 10, 4)
       .addFloat("otpstopa", 10, 4)
       .addChar("cskupine", 6)
       .addFloat("koeficijent", 10, 2)
       .addChar("mjesec", 2)
       .addChar("porijeklo", 2)
       .addChar("radnik", 6)
       .addDate("datnabave")
       .addDate("dataktiviranja")
       .addDate("datpromjene")
       .addDate("datlikvidacije")
       .addFloat("osnovica", 17, 2)
       .addFloat("ispravak", 17, 2)
       .addFloat("sadvrijed", 17, 2)
       .addFloat("revosn", 17, 2)
       .addFloat("revisp", 17, 2)
       .addFloat("revsad", 17, 2)
       .addFloat("amortizacija", 17, 2)
       .addFloat("amor1", 17, 2)
       .addFloat("amor2", 17, 2)
       .addFloat("pamortizacija", 17, 2)
       .addFloat("pamor1", 17, 2)
       .addFloat("pamor2", 17, 2)
       .addFloat("revamor", 17, 2)
       .addFloat("prebacam", 17, 2)
       .addChar("vk", 3)
       .addPrimaryKey("corg,invbroj");


    Naziv = "OS_Obrada2";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
