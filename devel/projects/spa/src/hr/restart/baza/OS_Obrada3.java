/****license*****************************************************************
**   file: OS_Obrada3.java
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



public class OS_Obrada3 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Obrada3 OS_Obrada3class;

  QueryDataSet osob3 = new QueryDataSet();

  Column osob3LOKK = new Column();
  Column osob3AKTIV = new Column();
  Column osob3CORG = new Column();
  Column osob3NAZIV = new Column();
  Column osob3INVBROJ = new Column();
  Column osob3NAZSREDSTVA = new Column();
  Column osob3COBJEKT = new Column();
  Column osob3CLOKACIJE = new Column();
  Column osob3CARTIKLA = new Column();
  Column osob3CPAR = new Column();
  Column osob3BROJKONTA = new Column();
  Column osob3CGRUPE = new Column();
  Column osob3ZAKSTOPA = new Column();
  Column osob3ODLSTOPA = new Column();
  Column osob3OTPSTOPA = new Column();
  Column osob3CSKUPINE = new Column();
  Column osob3KOEFICIJENT = new Column();
  Column osob3MJESEC = new Column();
  Column osob3PORIJEKLO = new Column();
  Column osob3RADNIK = new Column();
  Column osob3DATNABAVE = new Column();
  Column osob3DATAKTIVIRANJA = new Column();
  Column osob3DATPROMJENE = new Column();
  Column osob3DATLIKVIDACIJE = new Column();
  Column osob3OSNOVICA = new Column();
  Column osob3ISPRAVAK = new Column();
  Column osob3SADVRIJED = new Column();
  Column osob3REVOSN = new Column();
  Column osob3REVISP = new Column();
  Column osob3REVSAD = new Column();
  Column osob3AMORTIZACIJA = new Column();
  Column osob3AMOR1 = new Column();
  Column osob3AMOR2 = new Column();
  Column osob3PAMORTIZACIJA = new Column();
  Column osob3PAMOR1 = new Column();
  Column osob3PAMOR2 = new Column();
  Column osob3REVAMOR = new Column();
  Column osob3PREBACAM = new Column();
  Column osob3VK = new Column();

  public static OS_Obrada3 getDataModule() {
    if (OS_Obrada3class == null) {
      OS_Obrada3class = new OS_Obrada3();
    }
    return OS_Obrada3class;
  }

  public QueryDataSet getQueryDataSet() {
    return osob3;
  }

  public OS_Obrada3() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osob3LOKK.setCaption("Status zauzetosti");
    osob3LOKK.setColumnName("LOKK");
    osob3LOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3LOKK.setPrecision(1);
    osob3LOKK.setTableName("OS_OBRADA3");
    osob3LOKK.setServerColumnName("LOKK");
    osob3LOKK.setSqlType(1);
    osob3LOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob3LOKK.setDefault("N");
    osob3AKTIV.setCaption("Aktivan - neaktivan");
    osob3AKTIV.setColumnName("AKTIV");
    osob3AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3AKTIV.setPrecision(1);
    osob3AKTIV.setTableName("OS_OBRADA3");
    osob3AKTIV.setServerColumnName("AKTIV");
    osob3AKTIV.setSqlType(1);
    osob3AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob3AKTIV.setDefault("D");
    osob3CORG.setCaption("OJ");
    osob3CORG.setColumnName("CORG");
    osob3CORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3CORG.setPrecision(12);
    osob3CORG.setRowId(true);
    osob3CORG.setTableName("OS_OBRADA3");
    osob3CORG.setServerColumnName("CORG");
    osob3CORG.setSqlType(1);
    osob3NAZIV.setCaption("Naziv");
    osob3NAZIV.setColumnName("NAZIV");
    osob3NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3NAZIV.setPrecision(50);
    osob3NAZIV.setTableName("OS_OBRADA3");
    osob3NAZIV.setServerColumnName("NAZIV");
    osob3NAZIV.setSqlType(1);
    osob3NAZIV.setWidth(30);
    osob3INVBROJ.setCaption("IBroj");
    osob3INVBROJ.setColumnName("INVBROJ");
    osob3INVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3INVBROJ.setPrecision(10);
    osob3INVBROJ.setRowId(true);
    osob3INVBROJ.setTableName("OS_OBRADA3");
    osob3INVBROJ.setServerColumnName("INVBROJ");
    osob3INVBROJ.setSqlType(1);
    osob3NAZSREDSTVA.setCaption("Naziv sredstva");
    osob3NAZSREDSTVA.setColumnName("NAZSREDSTVA");
    osob3NAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3NAZSREDSTVA.setPrecision(50);
    osob3NAZSREDSTVA.setTableName("OS_OBRADA3");
    osob3NAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    osob3NAZSREDSTVA.setSqlType(1);
    osob3NAZSREDSTVA.setWidth(30);
    osob3COBJEKT.setCaption("Objekt");
    osob3COBJEKT.setColumnName("COBJEKT");
    osob3COBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3COBJEKT.setPrecision(6);
    osob3COBJEKT.setTableName("OS_OBRADA3");
    osob3COBJEKT.setServerColumnName("COBJEKT");
    osob3COBJEKT.setSqlType(1);
    osob3CLOKACIJE.setCaption("Lokacija");
    osob3CLOKACIJE.setColumnName("CLOKACIJE");
    osob3CLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3CLOKACIJE.setPrecision(12);
    osob3CLOKACIJE.setTableName("OS_OBRADA3");
    osob3CLOKACIJE.setServerColumnName("CLOKACIJE");
    osob3CLOKACIJE.setSqlType(1);
    osob3CARTIKLA.setCaption("Artikl");
    osob3CARTIKLA.setColumnName("CARTIKLA");
    osob3CARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3CARTIKLA.setPrecision(6);
    osob3CARTIKLA.setTableName("OS_OBRADA3");
    osob3CARTIKLA.setServerColumnName("CARTIKLA");
    osob3CARTIKLA.setSqlType(1);
    osob3CPAR.setCaption("Partner");
    osob3CPAR.setColumnName("CPAR");
    osob3CPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osob3CPAR.setPrecision(6);
    osob3CPAR.setTableName("OS_OBRADA3");
    osob3CPAR.setServerColumnName("CPAR");
    osob3CPAR.setSqlType(4);
    osob3CPAR.setWidth(6);
    osob3BROJKONTA.setCaption("Konto");
    osob3BROJKONTA.setColumnName("BROJKONTA");
    osob3BROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3BROJKONTA.setPrecision(8);
    osob3BROJKONTA.setTableName("OS_OBRADA3");
    osob3BROJKONTA.setServerColumnName("BROJKONTA");
    osob3BROJKONTA.setSqlType(1);
    osob3CGRUPE.setCaption("Amort. grupa");
    osob3CGRUPE.setColumnName("CGRUPE");
    osob3CGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3CGRUPE.setPrecision(6);
    osob3CGRUPE.setTableName("OS_OBRADA3");
    osob3CGRUPE.setServerColumnName("CGRUPE");
    osob3CGRUPE.setSqlType(1);
    osob3ZAKSTOPA.setCaption("Zakonska stopa");
    osob3ZAKSTOPA.setColumnName("ZAKSTOPA");
    osob3ZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3ZAKSTOPA.setPrecision(10);
    osob3ZAKSTOPA.setScale(4);
    osob3ZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osob3ZAKSTOPA.setDefault("0");
    osob3ZAKSTOPA.setTableName("OS_OBRADA3");
    osob3ZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osob3ZAKSTOPA.setSqlType(2);
    osob3ODLSTOPA.setCaption("Stopa po odluci");
    osob3ODLSTOPA.setColumnName("ODLSTOPA");
    osob3ODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3ODLSTOPA.setPrecision(10);
    osob3ODLSTOPA.setScale(4);
    osob3ODLSTOPA.setDisplayMask("###,###,##0.0000");
    osob3ODLSTOPA.setDefault("0");
    osob3ODLSTOPA.setTableName("OS_OBRADA3");
    osob3ODLSTOPA.setServerColumnName("ODLSTOPA");
    osob3ODLSTOPA.setSqlType(2);
    osob3OTPSTOPA.setCaption("Otp. stopa");
    osob3OTPSTOPA.setColumnName("OTPSTOPA");
    osob3OTPSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3OTPSTOPA.setPrecision(10);
    osob3OTPSTOPA.setScale(4);
    osob3OTPSTOPA.setDisplayMask("###,###,##0.0000");
    osob3OTPSTOPA.setDefault("0");
    osob3OTPSTOPA.setTableName("OS_OBRADA3");
    osob3OTPSTOPA.setServerColumnName("OTPSTOPA");
    osob3OTPSTOPA.setSqlType(2);
    osob3CSKUPINE.setCaption("Rev. skupina");
    osob3CSKUPINE.setColumnName("CSKUPINE");
    osob3CSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3CSKUPINE.setPrecision(6);
    osob3CSKUPINE.setTableName("OS_OBRADA3");
    osob3CSKUPINE.setServerColumnName("CSKUPINE");
    osob3CSKUPINE.setSqlType(1);
    osob3KOEFICIJENT.setCaption("Koeficijent");
    osob3KOEFICIJENT.setColumnName("KOEFICIJENT");
    osob3KOEFICIJENT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3KOEFICIJENT.setPrecision(10);
    osob3KOEFICIJENT.setScale(2);
    osob3KOEFICIJENT.setDisplayMask("###,###,##0.00");
    osob3KOEFICIJENT.setDefault("0");
    osob3KOEFICIJENT.setTableName("OS_OBRADA3");
    osob3KOEFICIJENT.setServerColumnName("KOEFICIJENT");
    osob3KOEFICIJENT.setSqlType(2);
    osob3MJESEC.setCaption("Mjesec");
    osob3MJESEC.setColumnName("MJESEC");
    osob3MJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3MJESEC.setPrecision(2);
    osob3MJESEC.setTableName("OS_OBRADA3");
    osob3MJESEC.setServerColumnName("MJESEC");
    osob3MJESEC.setSqlType(1);
    osob3PORIJEKLO.setCaption("Porijeklo");
    osob3PORIJEKLO.setColumnName("PORIJEKLO");
    osob3PORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3PORIJEKLO.setPrecision(2);
    osob3PORIJEKLO.setTableName("OS_OBRADA3");
    osob3PORIJEKLO.setServerColumnName("PORIJEKLO");
    osob3PORIJEKLO.setSqlType(1);
    osob3RADNIK.setCaption("Radnik");
    osob3RADNIK.setColumnName("RADNIK");
    osob3RADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3RADNIK.setPrecision(6);
    osob3RADNIK.setTableName("OS_OBRADA3");
    osob3RADNIK.setServerColumnName("RADNIK");
    osob3RADNIK.setSqlType(1);
    osob3DATNABAVE.setCaption("Datum nabave");
    osob3DATNABAVE.setColumnName("DATNABAVE");
    osob3DATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob3DATNABAVE.setPrecision(8);
    osob3DATNABAVE.setDisplayMask("dd-MM-yyyy");
//    osob3DATNABAVE.setEditMask("dd-MM-yyyy");
    osob3DATNABAVE.setTableName("OS_OBRADA3");
    osob3DATNABAVE.setServerColumnName("DATNABAVE");
    osob3DATNABAVE.setSqlType(93);
    osob3DATNABAVE.setWidth(10);
    osob3DATAKTIVIRANJA.setCaption("Datum aktiviranja");
    osob3DATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    osob3DATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob3DATAKTIVIRANJA.setPrecision(8);
    osob3DATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    osob3DATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    osob3DATAKTIVIRANJA.setTableName("OS_OBRADA3");
    osob3DATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    osob3DATAKTIVIRANJA.setSqlType(93);
    osob3DATAKTIVIRANJA.setWidth(10);
    osob3DATPROMJENE.setCaption("Datum promjene");
    osob3DATPROMJENE.setColumnName("DATPROMJENE");
    osob3DATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob3DATPROMJENE.setPrecision(8);
    osob3DATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    osob3DATPROMJENE.setEditMask("dd-MM-yyyy");
    osob3DATPROMJENE.setTableName("OS_OBRADA3");
    osob3DATPROMJENE.setServerColumnName("DATPROMJENE");
    osob3DATPROMJENE.setSqlType(93);
    osob3DATPROMJENE.setWidth(10);
    osob3DATLIKVIDACIJE.setCaption("Datum likvidacije");
    osob3DATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    osob3DATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob3DATLIKVIDACIJE.setPrecision(8);
    osob3DATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    osob3DATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    osob3DATLIKVIDACIJE.setTableName("OS_OBRADA3");
    osob3DATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    osob3DATLIKVIDACIJE.setSqlType(93);
    osob3DATLIKVIDACIJE.setWidth(10);
    osob3OSNOVICA.setCaption("Osnovica");
    osob3OSNOVICA.setColumnName("OSNOVICA");
    osob3OSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3OSNOVICA.setPrecision(17);
    osob3OSNOVICA.setScale(2);
    osob3OSNOVICA.setDisplayMask("###,###,##0.00");
    osob3OSNOVICA.setDefault("0");
    osob3OSNOVICA.setTableName("OS_OBRADA3");
    osob3OSNOVICA.setServerColumnName("OSNOVICA");
    osob3OSNOVICA.setSqlType(2);
    osob3ISPRAVAK.setCaption("Ispravak");
    osob3ISPRAVAK.setColumnName("ISPRAVAK");
    osob3ISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3ISPRAVAK.setPrecision(17);
    osob3ISPRAVAK.setScale(2);
    osob3ISPRAVAK.setDisplayMask("###,###,##0.00");
    osob3ISPRAVAK.setDefault("0");
    osob3ISPRAVAK.setTableName("OS_OBRADA3");
    osob3ISPRAVAK.setServerColumnName("ISPRAVAK");
    osob3ISPRAVAK.setSqlType(2);
    osob3SADVRIJED.setCaption("Vrijednost");
    osob3SADVRIJED.setColumnName("SADVRIJED");
    osob3SADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3SADVRIJED.setPrecision(17);
    osob3SADVRIJED.setScale(2);
    osob3SADVRIJED.setDisplayMask("###,###,##0.00");
    osob3SADVRIJED.setDefault("0");
    osob3SADVRIJED.setTableName("OS_OBRADA3");
    osob3SADVRIJED.setServerColumnName("SADVRIJED");
    osob3SADVRIJED.setSqlType(2);
    osob3REVOSN.setCaption("Rev. isnovice");
    osob3REVOSN.setColumnName("REVOSN");
    osob3REVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3REVOSN.setPrecision(17);
    osob3REVOSN.setScale(2);
    osob3REVOSN.setDisplayMask("###,###,##0.00");
    osob3REVOSN.setDefault("0");
    osob3REVOSN.setTableName("OS_OBRADA3");
    osob3REVOSN.setServerColumnName("REVOSN");
    osob3REVOSN.setSqlType(2);
    osob3REVISP.setCaption("Rev. ispravka");
    osob3REVISP.setColumnName("REVISP");
    osob3REVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3REVISP.setPrecision(17);
    osob3REVISP.setScale(2);
    osob3REVISP.setDisplayMask("###,###,##0.00");
    osob3REVISP.setDefault("0");
    osob3REVISP.setTableName("OS_OBRADA3");
    osob3REVISP.setServerColumnName("REVISP");
    osob3REVISP.setSqlType(2);
    osob3REVSAD.setCaption("Revalorizacija");
    osob3REVSAD.setColumnName("REVSAD");
    osob3REVSAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3REVSAD.setPrecision(17);
    osob3REVSAD.setScale(2);
    osob3REVSAD.setDisplayMask("###,###,##0.00");
    osob3REVSAD.setDefault("0");
    osob3REVSAD.setTableName("OS_OBRADA3");
    osob3REVSAD.setServerColumnName("REVSAD");
    osob3REVSAD.setSqlType(2);
    osob3AMORTIZACIJA.setCaption("Amortizacija");
    osob3AMORTIZACIJA.setColumnName("AMORTIZACIJA");
    osob3AMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3AMORTIZACIJA.setPrecision(17);
    osob3AMORTIZACIJA.setScale(2);
    osob3AMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob3AMORTIZACIJA.setDefault("0");
    osob3AMORTIZACIJA.setTableName("OS_OBRADA3");
    osob3AMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    osob3AMORTIZACIJA.setSqlType(2);
    osob3AMOR1.setCaption("Amor1");
    osob3AMOR1.setColumnName("AMOR1");
    osob3AMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3AMOR1.setPrecision(17);
    osob3AMOR1.setScale(2);
    osob3AMOR1.setDisplayMask("###,###,##0.00");
    osob3AMOR1.setDefault("0");
    osob3AMOR1.setTableName("OS_OBRADA3");
    osob3AMOR1.setServerColumnName("AMOR1");
    osob3AMOR1.setSqlType(2);
    osob3AMOR2.setCaption("Amor2");
    osob3AMOR2.setColumnName("AMOR2");
    osob3AMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3AMOR2.setPrecision(17);
    osob3AMOR2.setScale(2);
    osob3AMOR2.setDisplayMask("###,###,##0.00");
    osob3AMOR2.setDefault("0");
    osob3AMOR2.setTableName("OS_OBRADA3");
    osob3AMOR2.setServerColumnName("AMOR2");
    osob3AMOR2.setSqlType(2);
    osob3PAMORTIZACIJA.setCaption("Pov. amortizacija");
    osob3PAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    osob3PAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3PAMORTIZACIJA.setPrecision(17);
    osob3PAMORTIZACIJA.setScale(2);
    osob3PAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob3PAMORTIZACIJA.setDefault("0");
    osob3PAMORTIZACIJA.setTableName("OS_OBRADA3");
    osob3PAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    osob3PAMORTIZACIJA.setSqlType(2);
    osob3PAMOR1.setCaption("Pamor1");
    osob3PAMOR1.setColumnName("PAMOR1");
    osob3PAMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3PAMOR1.setPrecision(17);
    osob3PAMOR1.setScale(2);
    osob3PAMOR1.setDisplayMask("###,###,##0.00");
    osob3PAMOR1.setDefault("0");
    osob3PAMOR1.setTableName("OS_OBRADA3");
    osob3PAMOR1.setServerColumnName("PAMOR1");
    osob3PAMOR1.setSqlType(2);
    osob3PAMOR2.setCaption("Pamor2");
    osob3PAMOR2.setColumnName("PAMOR2");
    osob3PAMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3PAMOR2.setPrecision(17);
    osob3PAMOR2.setScale(2);
    osob3PAMOR2.setDisplayMask("###,###,##0.00");
    osob3PAMOR2.setDefault("0");
    osob3PAMOR2.setTableName("OS_OBRADA3");
    osob3PAMOR2.setServerColumnName("PAMOR2");
    osob3PAMOR2.setSqlType(2);
    osob3REVAMOR.setCaption("Rev. amortizacije");
    osob3REVAMOR.setColumnName("REVAMOR");
    osob3REVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3REVAMOR.setPrecision(17);
    osob3REVAMOR.setScale(2);
    osob3REVAMOR.setDisplayMask("###,###,##0.00");
    osob3REVAMOR.setDefault("0");
    osob3REVAMOR.setTableName("OS_OBRADA3");
    osob3REVAMOR.setServerColumnName("REVAMOR");
    osob3REVAMOR.setSqlType(2);
    osob3PREBACAM.setCaption("Preb. amortizacije");
    osob3PREBACAM.setColumnName("PREBACAM");
    osob3PREBACAM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob3PREBACAM.setPrecision(17);
    osob3PREBACAM.setScale(2);
    osob3PREBACAM.setDisplayMask("###,###,##0.00");
    osob3PREBACAM.setDefault("0");
    osob3PREBACAM.setTableName("OS_OBRADA3");
    osob3PREBACAM.setServerColumnName("PREBACAM");
    osob3PREBACAM.setSqlType(2);
    osob3VK.setCaption("");
    osob3VK.setColumnName("VK");
    osob3VK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob3VK.setPrecision(3);
    osob3VK.setTableName("OS_OBRADA3");
    osob3VK.setServerColumnName("VK");
    osob3VK.setSqlType(1);
    osob3.setResolver(dm.getQresolver());
    osob3.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Obrada3", null, true, Load.ALL));
 setColumns(new Column[] {osob3LOKK, osob3AKTIV, osob3CORG, osob3NAZIV, osob3INVBROJ, osob3NAZSREDSTVA, osob3COBJEKT, osob3CLOKACIJE, osob3CARTIKLA,
        osob3CPAR, osob3BROJKONTA, osob3CGRUPE, osob3ZAKSTOPA, osob3ODLSTOPA, osob3OTPSTOPA, osob3CSKUPINE, osob3KOEFICIJENT, osob3MJESEC, osob3PORIJEKLO,
        osob3RADNIK, osob3DATNABAVE, osob3DATAKTIVIRANJA, osob3DATPROMJENE, osob3DATLIKVIDACIJE, osob3OSNOVICA, osob3ISPRAVAK, osob3SADVRIJED, osob3REVOSN,
        osob3REVISP, osob3REVSAD, osob3AMORTIZACIJA, osob3AMOR1, osob3AMOR2, osob3PAMORTIZACIJA, osob3PAMOR1, osob3PAMOR2, osob3REVAMOR, osob3PREBACAM, osob3VK});
  }

  public void setall() {

    ddl.create("OS_Obrada3")
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


    Naziv = "OS_Obrada3";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
