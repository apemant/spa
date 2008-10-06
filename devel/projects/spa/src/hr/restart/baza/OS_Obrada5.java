/****license*****************************************************************
**   file: OS_Obrada5.java
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



public class OS_Obrada5 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Obrada5 OS_Obrada5class;

  QueryDataSet osob5 = new QueryDataSet();

  Column osob5LOKK = new Column();
  Column osob5AKTIV = new Column();
  Column osob5CORG = new Column();
  Column osob5NAZIV = new Column();
  Column osob5INVBROJ = new Column();
  Column osob5NAZSREDSTVA = new Column();
  Column osob5COBJEKT = new Column();
  Column osob5CLOKACIJE = new Column();
  Column osob5CARTIKLA = new Column();
  Column osob5CPAR = new Column();
  Column osob5BROJKONTA = new Column();
  Column osob5CGRUPE = new Column();
  Column osob5ZAKSTOPA = new Column();
  Column osob5ODLSTOPA = new Column();
  Column osob5OTPSTOPA = new Column();
  Column osob5CSKUPINE = new Column();
  Column osob5KOEFICIJENT = new Column();
  Column osob5MJESEC = new Column();
  Column osob5PORIJEKLO = new Column();
  Column osob5RADNIK = new Column();
  Column osob5DATNABAVE = new Column();
  Column osob5DATAKTIVIRANJA = new Column();
  Column osob5DATPROMJENE = new Column();
  Column osob5DATLIKVIDACIJE = new Column();
  Column osob5OSNOVICA = new Column();
  Column osob5ISPRAVAK = new Column();
  Column osob5SADVRIJED = new Column();
  Column osob5REVOSN = new Column();
  Column osob5REVISP = new Column();
  Column osob5REVSAD = new Column();
  Column osob5AMORTIZACIJA = new Column();
  Column osob5AMOR1 = new Column();
  Column osob5AMOR2 = new Column();
  Column osob5PAMORTIZACIJA = new Column();
  Column osob5PAMOR1 = new Column();
  Column osob5PAMOR2 = new Column();
  Column osob5REVAMOR = new Column();
  Column osob5PREBACAM = new Column();
  Column osob5VK = new Column();

  public static OS_Obrada5 getDataModule() {
    if (OS_Obrada5class == null) {
      OS_Obrada5class = new OS_Obrada5();
    }
    return OS_Obrada5class;
  }

  public QueryDataSet getQueryDataSet() {
    return osob5;
  }

  public OS_Obrada5() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osob5LOKK.setCaption("Status zauzetosti");
    osob5LOKK.setColumnName("LOKK");
    osob5LOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5LOKK.setPrecision(1);
    osob5LOKK.setTableName("OS_OBRADA5");
    osob5LOKK.setServerColumnName("LOKK");
    osob5LOKK.setSqlType(1);
    osob5LOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob5LOKK.setDefault("N");
    osob5AKTIV.setCaption("Aktivan - neaktivan");
    osob5AKTIV.setColumnName("AKTIV");
    osob5AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5AKTIV.setPrecision(1);
    osob5AKTIV.setTableName("OS_OBRADA5");
    osob5AKTIV.setServerColumnName("AKTIV");
    osob5AKTIV.setSqlType(1);
    osob5AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob5AKTIV.setDefault("D");
    osob5CORG.setCaption("OJ");
    osob5CORG.setColumnName("CORG");
    osob5CORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5CORG.setPrecision(12);
    osob5CORG.setRowId(true);
    osob5CORG.setTableName("OS_OBRADA5");
    osob5CORG.setServerColumnName("CORG");
    osob5CORG.setSqlType(1);
    osob5NAZIV.setCaption("Naziv");
    osob5NAZIV.setColumnName("NAZIV");
    osob5NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5NAZIV.setPrecision(50);
    osob5NAZIV.setTableName("OS_OBRADA5");
    osob5NAZIV.setServerColumnName("NAZIV");
    osob5NAZIV.setSqlType(1);
    osob5NAZIV.setWidth(30);
    osob5INVBROJ.setCaption("IBroj");
    osob5INVBROJ.setColumnName("INVBROJ");
    osob5INVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5INVBROJ.setPrecision(10);
    osob5INVBROJ.setRowId(true);
    osob5INVBROJ.setTableName("OS_OBRADA5");
    osob5INVBROJ.setServerColumnName("INVBROJ");
    osob5INVBROJ.setSqlType(1);
    osob5NAZSREDSTVA.setCaption("Naziv sredstva");
    osob5NAZSREDSTVA.setColumnName("NAZSREDSTVA");
    osob5NAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5NAZSREDSTVA.setPrecision(50);
    osob5NAZSREDSTVA.setTableName("OS_OBRADA5");
    osob5NAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    osob5NAZSREDSTVA.setSqlType(1);
    osob5NAZSREDSTVA.setWidth(30);
    osob5COBJEKT.setCaption("Objekt");
    osob5COBJEKT.setColumnName("COBJEKT");
    osob5COBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5COBJEKT.setPrecision(6);
    osob5COBJEKT.setTableName("OS_OBRADA5");
    osob5COBJEKT.setServerColumnName("COBJEKT");
    osob5COBJEKT.setSqlType(1);
    osob5CLOKACIJE.setCaption("Lokacija");
    osob5CLOKACIJE.setColumnName("CLOKACIJE");
    osob5CLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5CLOKACIJE.setPrecision(12);
    osob5CLOKACIJE.setTableName("OS_OBRADA5");
    osob5CLOKACIJE.setServerColumnName("CLOKACIJE");
    osob5CLOKACIJE.setSqlType(1);
    osob5CARTIKLA.setCaption("Artikl");
    osob5CARTIKLA.setColumnName("CARTIKLA");
    osob5CARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5CARTIKLA.setPrecision(6);
    osob5CARTIKLA.setTableName("OS_OBRADA5");
    osob5CARTIKLA.setServerColumnName("CARTIKLA");
    osob5CARTIKLA.setSqlType(1);
    osob5CPAR.setCaption("Partner");
    osob5CPAR.setColumnName("CPAR");
    osob5CPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osob5CPAR.setPrecision(6);
    osob5CPAR.setTableName("OS_OBRADA5");
    osob5CPAR.setServerColumnName("CPAR");
    osob5CPAR.setSqlType(4);
    osob5CPAR.setWidth(6);
    osob5BROJKONTA.setCaption("Konto");
    osob5BROJKONTA.setColumnName("BROJKONTA");
    osob5BROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5BROJKONTA.setPrecision(8);
    osob5BROJKONTA.setTableName("OS_OBRADA5");
    osob5BROJKONTA.setServerColumnName("BROJKONTA");
    osob5BROJKONTA.setSqlType(1);
    osob5CGRUPE.setCaption("Amort. grupa");
    osob5CGRUPE.setColumnName("CGRUPE");
    osob5CGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5CGRUPE.setPrecision(6);
    osob5CGRUPE.setTableName("OS_OBRADA5");
    osob5CGRUPE.setServerColumnName("CGRUPE");
    osob5CGRUPE.setSqlType(1);
    osob5ZAKSTOPA.setCaption("Zakonska stopa");
    osob5ZAKSTOPA.setColumnName("ZAKSTOPA");
    osob5ZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5ZAKSTOPA.setPrecision(10);
    osob5ZAKSTOPA.setScale(4);
    osob5ZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osob5ZAKSTOPA.setDefault("0");
    osob5ZAKSTOPA.setTableName("OS_OBRADA5");
    osob5ZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osob5ZAKSTOPA.setSqlType(2);
    osob5ODLSTOPA.setCaption("Stopa po odluci");
    osob5ODLSTOPA.setColumnName("ODLSTOPA");
    osob5ODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5ODLSTOPA.setPrecision(10);
    osob5ODLSTOPA.setScale(4);
    osob5ODLSTOPA.setDisplayMask("###,###,##0.0000");
    osob5ODLSTOPA.setDefault("0");
    osob5ODLSTOPA.setTableName("OS_OBRADA5");
    osob5ODLSTOPA.setServerColumnName("ODLSTOPA");
    osob5ODLSTOPA.setSqlType(2);
    osob5OTPSTOPA.setCaption("Otp. stopa");
    osob5OTPSTOPA.setColumnName("OTPSTOPA");
    osob5OTPSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5OTPSTOPA.setPrecision(10);
    osob5OTPSTOPA.setScale(4);
    osob5OTPSTOPA.setDisplayMask("###,###,##0.0000");
    osob5OTPSTOPA.setDefault("0");
    osob5OTPSTOPA.setTableName("OS_OBRADA5");
    osob5OTPSTOPA.setServerColumnName("OTPSTOPA");
    osob5OTPSTOPA.setSqlType(2);
    osob5CSKUPINE.setCaption("Rev. skupina");
    osob5CSKUPINE.setColumnName("CSKUPINE");
    osob5CSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5CSKUPINE.setPrecision(6);
    osob5CSKUPINE.setTableName("OS_OBRADA5");
    osob5CSKUPINE.setServerColumnName("CSKUPINE");
    osob5CSKUPINE.setSqlType(1);
    osob5KOEFICIJENT.setCaption("Koeficijent");
    osob5KOEFICIJENT.setColumnName("KOEFICIJENT");
    osob5KOEFICIJENT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5KOEFICIJENT.setPrecision(10);
    osob5KOEFICIJENT.setScale(2);
    osob5KOEFICIJENT.setDisplayMask("###,###,##0.00");
    osob5KOEFICIJENT.setDefault("0");
    osob5KOEFICIJENT.setTableName("OS_OBRADA5");
    osob5KOEFICIJENT.setServerColumnName("KOEFICIJENT");
    osob5KOEFICIJENT.setSqlType(2);
    osob5MJESEC.setCaption("Mjesec");
    osob5MJESEC.setColumnName("MJESEC");
    osob5MJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5MJESEC.setPrecision(2);
    osob5MJESEC.setTableName("OS_OBRADA5");
    osob5MJESEC.setServerColumnName("MJESEC");
    osob5MJESEC.setSqlType(1);
    osob5PORIJEKLO.setCaption("Porijeklo");
    osob5PORIJEKLO.setColumnName("PORIJEKLO");
    osob5PORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5PORIJEKLO.setPrecision(2);
    osob5PORIJEKLO.setTableName("OS_OBRADA5");
    osob5PORIJEKLO.setServerColumnName("PORIJEKLO");
    osob5PORIJEKLO.setSqlType(1);
    osob5RADNIK.setCaption("Radnik");
    osob5RADNIK.setColumnName("RADNIK");
    osob5RADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5RADNIK.setPrecision(6);
    osob5RADNIK.setTableName("OS_OBRADA5");
    osob5RADNIK.setServerColumnName("RADNIK");
    osob5RADNIK.setSqlType(1);
    osob5DATNABAVE.setCaption("Datum nabave");
    osob5DATNABAVE.setColumnName("DATNABAVE");
    osob5DATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob5DATNABAVE.setPrecision(8);
    osob5DATNABAVE.setDisplayMask("dd-MM-yyyy");
//    osob5DATNABAVE.setEditMask("dd-MM-yyyy");
    osob5DATNABAVE.setTableName("OS_OBRADA5");
    osob5DATNABAVE.setServerColumnName("DATNABAVE");
    osob5DATNABAVE.setSqlType(93);
    osob5DATNABAVE.setWidth(10);
    osob5DATAKTIVIRANJA.setCaption("Datum aktiviranja");
    osob5DATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    osob5DATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob5DATAKTIVIRANJA.setPrecision(8);
    osob5DATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    osob5DATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    osob5DATAKTIVIRANJA.setTableName("OS_OBRADA5");
    osob5DATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    osob5DATAKTIVIRANJA.setSqlType(93);
    osob5DATAKTIVIRANJA.setWidth(10);
    osob5DATPROMJENE.setCaption("Datum promjene");
    osob5DATPROMJENE.setColumnName("DATPROMJENE");
    osob5DATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob5DATPROMJENE.setPrecision(8);
    osob5DATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    osob5DATPROMJENE.setEditMask("dd-MM-yyyy");
    osob5DATPROMJENE.setTableName("OS_OBRADA5");
    osob5DATPROMJENE.setServerColumnName("DATPROMJENE");
    osob5DATPROMJENE.setSqlType(93);
    osob5DATPROMJENE.setWidth(10);
    osob5DATLIKVIDACIJE.setCaption("Datum likvidacije");
    osob5DATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    osob5DATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob5DATLIKVIDACIJE.setPrecision(8);
    osob5DATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    osob5DATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    osob5DATLIKVIDACIJE.setTableName("OS_OBRADA5");
    osob5DATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    osob5DATLIKVIDACIJE.setSqlType(93);
    osob5DATLIKVIDACIJE.setWidth(10);
    osob5OSNOVICA.setCaption("Osnovica");
    osob5OSNOVICA.setColumnName("OSNOVICA");
    osob5OSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5OSNOVICA.setPrecision(17);
    osob5OSNOVICA.setScale(2);
    osob5OSNOVICA.setDisplayMask("###,###,##0.00");
    osob5OSNOVICA.setDefault("0");
    osob5OSNOVICA.setTableName("OS_OBRADA5");
    osob5OSNOVICA.setServerColumnName("OSNOVICA");
    osob5OSNOVICA.setSqlType(2);
    osob5ISPRAVAK.setCaption("Ispravak");
    osob5ISPRAVAK.setColumnName("ISPRAVAK");
    osob5ISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5ISPRAVAK.setPrecision(17);
    osob5ISPRAVAK.setScale(2);
    osob5ISPRAVAK.setDisplayMask("###,###,##0.00");
    osob5ISPRAVAK.setDefault("0");
    osob5ISPRAVAK.setTableName("OS_OBRADA5");
    osob5ISPRAVAK.setServerColumnName("ISPRAVAK");
    osob5ISPRAVAK.setSqlType(2);
    osob5SADVRIJED.setCaption("Vrijednost");
    osob5SADVRIJED.setColumnName("SADVRIJED");
    osob5SADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5SADVRIJED.setPrecision(17);
    osob5SADVRIJED.setScale(2);
    osob5SADVRIJED.setDisplayMask("###,###,##0.00");
    osob5SADVRIJED.setDefault("0");
    osob5SADVRIJED.setTableName("OS_OBRADA5");
    osob5SADVRIJED.setServerColumnName("SADVRIJED");
    osob5SADVRIJED.setSqlType(2);
    osob5REVOSN.setCaption("Rev. isnovice");
    osob5REVOSN.setColumnName("REVOSN");
    osob5REVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5REVOSN.setPrecision(17);
    osob5REVOSN.setScale(2);
    osob5REVOSN.setDisplayMask("###,###,##0.00");
    osob5REVOSN.setDefault("0");
    osob5REVOSN.setTableName("OS_OBRADA5");
    osob5REVOSN.setServerColumnName("REVOSN");
    osob5REVOSN.setSqlType(2);
    osob5REVISP.setCaption("Rev. ispravka");
    osob5REVISP.setColumnName("REVISP");
    osob5REVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5REVISP.setPrecision(17);
    osob5REVISP.setScale(2);
    osob5REVISP.setDisplayMask("###,###,##0.00");
    osob5REVISP.setDefault("0");
    osob5REVISP.setTableName("OS_OBRADA5");
    osob5REVISP.setServerColumnName("REVISP");
    osob5REVISP.setSqlType(2);
    osob5REVSAD.setCaption("Revalorizacija");
    osob5REVSAD.setColumnName("REVSAD");
    osob5REVSAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5REVSAD.setPrecision(17);
    osob5REVSAD.setScale(2);
    osob5REVSAD.setDisplayMask("###,###,##0.00");
    osob5REVSAD.setDefault("0");
    osob5REVSAD.setTableName("OS_OBRADA5");
    osob5REVSAD.setServerColumnName("REVSAD");
    osob5REVSAD.setSqlType(2);
    osob5AMORTIZACIJA.setCaption("Amortizacija");
    osob5AMORTIZACIJA.setColumnName("AMORTIZACIJA");
    osob5AMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5AMORTIZACIJA.setPrecision(17);
    osob5AMORTIZACIJA.setScale(2);
    osob5AMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob5AMORTIZACIJA.setDefault("0");
    osob5AMORTIZACIJA.setTableName("OS_OBRADA5");
    osob5AMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    osob5AMORTIZACIJA.setSqlType(2);
    osob5AMOR1.setCaption("Amor1");
    osob5AMOR1.setColumnName("AMOR1");
    osob5AMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5AMOR1.setPrecision(17);
    osob5AMOR1.setScale(2);
    osob5AMOR1.setDisplayMask("###,###,##0.00");
    osob5AMOR1.setDefault("0");
    osob5AMOR1.setTableName("OS_OBRADA5");
    osob5AMOR1.setServerColumnName("AMOR1");
    osob5AMOR1.setSqlType(2);
    osob5AMOR2.setCaption("Amor2");
    osob5AMOR2.setColumnName("AMOR2");
    osob5AMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5AMOR2.setPrecision(17);
    osob5AMOR2.setScale(2);
    osob5AMOR2.setDisplayMask("###,###,##0.00");
    osob5AMOR2.setDefault("0");
    osob5AMOR2.setTableName("OS_OBRADA5");
    osob5AMOR2.setServerColumnName("AMOR2");
    osob5AMOR2.setSqlType(2);
    osob5PAMORTIZACIJA.setCaption("Pov. amortizacija");
    osob5PAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    osob5PAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5PAMORTIZACIJA.setPrecision(17);
    osob5PAMORTIZACIJA.setScale(2);
    osob5PAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob5PAMORTIZACIJA.setDefault("0");
    osob5PAMORTIZACIJA.setTableName("OS_OBRADA5");
    osob5PAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    osob5PAMORTIZACIJA.setSqlType(2);
    osob5PAMOR1.setCaption("Pamor1");
    osob5PAMOR1.setColumnName("PAMOR1");
    osob5PAMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5PAMOR1.setPrecision(17);
    osob5PAMOR1.setScale(2);
    osob5PAMOR1.setDisplayMask("###,###,##0.00");
    osob5PAMOR1.setDefault("0");
    osob5PAMOR1.setTableName("OS_OBRADA5");
    osob5PAMOR1.setServerColumnName("PAMOR1");
    osob5PAMOR1.setSqlType(2);
    osob5PAMOR2.setCaption("Pamor2");
    osob5PAMOR2.setColumnName("PAMOR2");
    osob5PAMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5PAMOR2.setPrecision(17);
    osob5PAMOR2.setScale(2);
    osob5PAMOR2.setDisplayMask("###,###,##0.00");
    osob5PAMOR2.setDefault("0");
    osob5PAMOR2.setTableName("OS_OBRADA5");
    osob5PAMOR2.setServerColumnName("PAMOR2");
    osob5PAMOR2.setSqlType(2);
    osob5REVAMOR.setCaption("Rev. amortizacije");
    osob5REVAMOR.setColumnName("REVAMOR");
    osob5REVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5REVAMOR.setPrecision(17);
    osob5REVAMOR.setScale(2);
    osob5REVAMOR.setDisplayMask("###,###,##0.00");
    osob5REVAMOR.setDefault("0");
    osob5REVAMOR.setTableName("OS_OBRADA5");
    osob5REVAMOR.setServerColumnName("REVAMOR");
    osob5REVAMOR.setSqlType(2);
    osob5PREBACAM.setCaption("Preb. amortizacije");
    osob5PREBACAM.setColumnName("PREBACAM");
    osob5PREBACAM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob5PREBACAM.setPrecision(17);
    osob5PREBACAM.setScale(2);
    osob5PREBACAM.setDisplayMask("###,###,##0.00");
    osob5PREBACAM.setDefault("0");
    osob5PREBACAM.setTableName("OS_OBRADA5");
    osob5PREBACAM.setServerColumnName("PREBACAM");
    osob5PREBACAM.setSqlType(2);
    osob5VK.setCaption("");
    osob5VK.setColumnName("VK");
    osob5VK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob5VK.setPrecision(3);
    osob5VK.setTableName("OS_OBRADA5");
    osob5VK.setServerColumnName("VK");
    osob5VK.setSqlType(1);
    osob5.setResolver(dm.getQresolver());
    osob5.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Obrada5", null, true, Load.ALL));
 setColumns(new Column[] {osob5LOKK, osob5AKTIV, osob5CORG, osob5NAZIV, osob5INVBROJ, osob5NAZSREDSTVA, osob5COBJEKT, osob5CLOKACIJE, osob5CARTIKLA,
        osob5CPAR, osob5BROJKONTA, osob5CGRUPE, osob5ZAKSTOPA, osob5ODLSTOPA, osob5OTPSTOPA, osob5CSKUPINE, osob5KOEFICIJENT, osob5MJESEC, osob5PORIJEKLO,
        osob5RADNIK, osob5DATNABAVE, osob5DATAKTIVIRANJA, osob5DATPROMJENE, osob5DATLIKVIDACIJE, osob5OSNOVICA, osob5ISPRAVAK, osob5SADVRIJED, osob5REVOSN,
        osob5REVISP, osob5REVSAD, osob5AMORTIZACIJA, osob5AMOR1, osob5AMOR2, osob5PAMORTIZACIJA, osob5PAMOR1, osob5PAMOR2, osob5REVAMOR, osob5PREBACAM, osob5VK});
  }

  public void setall() {

    ddl.create("OS_Obrada5")
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


    Naziv = "OS_Obrada5";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
