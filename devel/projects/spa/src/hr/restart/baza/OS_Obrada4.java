/****license*****************************************************************
**   file: OS_Obrada4.java
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



public class OS_Obrada4 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Obrada4 OS_Obrada4class;

  QueryDataSet osob4 = new QueryDataSet();

  Column osob4LOKK = new Column();
  Column osob4AKTIV = new Column();
  Column osob4CORG = new Column();
  Column osob4NAZIV = new Column();
  Column osob4INVBROJ = new Column();
  Column osob4NAZSREDSTVA = new Column();
  Column osob4COBJEKT = new Column();
  Column osob4CLOKACIJE = new Column();
  Column osob4CARTIKLA = new Column();
  Column osob4CPAR = new Column();
  Column osob4BROJKONTA = new Column();
  Column osob4CGRUPE = new Column();
  Column osob4ZAKSTOPA = new Column();
  Column osob4ODLSTOPA = new Column();
  Column osob4OTPSTOPA = new Column();
  Column osob4CSKUPINE = new Column();
  Column osob4KOEFICIJENT = new Column();
  Column osob4MJESEC = new Column();
  Column osob4PORIJEKLO = new Column();
  Column osob4RADNIK = new Column();
  Column osob4DATNABAVE = new Column();
  Column osob4DATAKTIVIRANJA = new Column();
  Column osob4DATPROMJENE = new Column();
  Column osob4DATLIKVIDACIJE = new Column();
  Column osob4OSNOVICA = new Column();
  Column osob4ISPRAVAK = new Column();
  Column osob4SADVRIJED = new Column();
  Column osob4REVOSN = new Column();
  Column osob4REVISP = new Column();
  Column osob4REVSAD = new Column();
  Column osob4AMORTIZACIJA = new Column();
  Column osob4AMOR1 = new Column();
  Column osob4AMOR2 = new Column();
  Column osob4PAMORTIZACIJA = new Column();
  Column osob4PAMOR1 = new Column();
  Column osob4PAMOR2 = new Column();
  Column osob4REVAMOR = new Column();
  Column osob4PREBACAM = new Column();
  Column osob4VK = new Column();
  Column osob4UOSNOVICA = new Column();
  Column osob4UISPRAVAK = new Column();
  Column osob4USADVRIJED = new Column();

  public static OS_Obrada4 getDataModule() {
    if (OS_Obrada4class == null) {
      OS_Obrada4class = new OS_Obrada4();
    }
    return OS_Obrada4class;
  }

  public QueryDataSet getQueryDataSet() {
    return osob4;
  }

  public OS_Obrada4() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osob4LOKK.setCaption("Status zauzetosti");
    osob4LOKK.setColumnName("LOKK");
    osob4LOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4LOKK.setPrecision(1);
    osob4LOKK.setTableName("OS_OBRADA4");
    osob4LOKK.setServerColumnName("LOKK");
    osob4LOKK.setSqlType(1);
    osob4LOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob4LOKK.setDefault("N");
    osob4AKTIV.setCaption("Aktivan - neaktivan");
    osob4AKTIV.setColumnName("AKTIV");
    osob4AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4AKTIV.setPrecision(1);
    osob4AKTIV.setTableName("OS_OBRADA4");
    osob4AKTIV.setServerColumnName("AKTIV");
    osob4AKTIV.setSqlType(1);
    osob4AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob4AKTIV.setDefault("D");
    osob4CORG.setCaption("OJ");
    osob4CORG.setColumnName("CORG");
    osob4CORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4CORG.setPrecision(12);
    osob4CORG.setRowId(true);
    osob4CORG.setTableName("OS_OBRADA4");
    osob4CORG.setServerColumnName("CORG");
    osob4CORG.setSqlType(1);
    osob4NAZIV.setCaption("Naziv");
    osob4NAZIV.setColumnName("NAZIV");
    osob4NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4NAZIV.setPrecision(50);
    osob4NAZIV.setTableName("OS_OBRADA4");
    osob4NAZIV.setServerColumnName("NAZIV");
    osob4NAZIV.setSqlType(1);
    osob4NAZIV.setWidth(30);
    osob4INVBROJ.setCaption("IBroj");
    osob4INVBROJ.setColumnName("INVBROJ");
    osob4INVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4INVBROJ.setPrecision(10);
    osob4INVBROJ.setRowId(true);
    osob4INVBROJ.setTableName("OS_OBRADA4");
    osob4INVBROJ.setServerColumnName("INVBROJ");
    osob4INVBROJ.setSqlType(1);
    osob4NAZSREDSTVA.setCaption("Naziv sredstva");
    osob4NAZSREDSTVA.setColumnName("NAZSREDSTVA");
    osob4NAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4NAZSREDSTVA.setPrecision(50);
    osob4NAZSREDSTVA.setTableName("OS_OBRADA4");
    osob4NAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    osob4NAZSREDSTVA.setSqlType(1);
    osob4NAZSREDSTVA.setWidth(30);
    osob4COBJEKT.setCaption("Objekt");
    osob4COBJEKT.setColumnName("COBJEKT");
    osob4COBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4COBJEKT.setPrecision(6);
    osob4COBJEKT.setTableName("OS_OBRADA4");
    osob4COBJEKT.setServerColumnName("COBJEKT");
    osob4COBJEKT.setSqlType(1);
    osob4CLOKACIJE.setCaption("Lokacija");
    osob4CLOKACIJE.setColumnName("CLOKACIJE");
    osob4CLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4CLOKACIJE.setPrecision(12);
    osob4CLOKACIJE.setTableName("OS_OBRADA4");
    osob4CLOKACIJE.setServerColumnName("CLOKACIJE");
    osob4CLOKACIJE.setSqlType(1);
    osob4CARTIKLA.setCaption("Artikl");
    osob4CARTIKLA.setColumnName("CARTIKLA");
    osob4CARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4CARTIKLA.setPrecision(6);
    osob4CARTIKLA.setTableName("OS_OBRADA4");
    osob4CARTIKLA.setServerColumnName("CARTIKLA");
    osob4CARTIKLA.setSqlType(1);
    osob4CPAR.setCaption("Partner");
    osob4CPAR.setColumnName("CPAR");
    osob4CPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osob4CPAR.setPrecision(6);
    osob4CPAR.setTableName("OS_OBRADA4");
    osob4CPAR.setServerColumnName("CPAR");
    osob4CPAR.setSqlType(4);
    osob4CPAR.setWidth(6);
    osob4BROJKONTA.setCaption("Konto");
    osob4BROJKONTA.setColumnName("BROJKONTA");
    osob4BROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4BROJKONTA.setPrecision(8);
    osob4BROJKONTA.setTableName("OS_OBRADA4");
    osob4BROJKONTA.setServerColumnName("BROJKONTA");
    osob4BROJKONTA.setSqlType(1);
    osob4CGRUPE.setCaption("Amort. grupa");
    osob4CGRUPE.setColumnName("CGRUPE");
    osob4CGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4CGRUPE.setPrecision(6);
    osob4CGRUPE.setTableName("OS_OBRADA4");
    osob4CGRUPE.setServerColumnName("CGRUPE");
    osob4CGRUPE.setSqlType(1);
    osob4ZAKSTOPA.setCaption("Zakonska stopa");
    osob4ZAKSTOPA.setColumnName("ZAKSTOPA");
    osob4ZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4ZAKSTOPA.setPrecision(10);
    osob4ZAKSTOPA.setScale(4);
    osob4ZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osob4ZAKSTOPA.setDefault("0");
    osob4ZAKSTOPA.setTableName("OS_OBRADA4");
    osob4ZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osob4ZAKSTOPA.setSqlType(2);
    osob4ODLSTOPA.setCaption("Stopa po odluci");
    osob4ODLSTOPA.setColumnName("ODLSTOPA");
    osob4ODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4ODLSTOPA.setPrecision(10);
    osob4ODLSTOPA.setScale(4);
    osob4ODLSTOPA.setDisplayMask("###,###,##0.0000");
    osob4ODLSTOPA.setDefault("0");
    osob4ODLSTOPA.setTableName("OS_OBRADA4");
    osob4ODLSTOPA.setServerColumnName("ODLSTOPA");
    osob4ODLSTOPA.setSqlType(2);
    osob4OTPSTOPA.setCaption("Otp. stopa");
    osob4OTPSTOPA.setColumnName("OTPSTOPA");
    osob4OTPSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4OTPSTOPA.setPrecision(10);
    osob4OTPSTOPA.setScale(4);
    osob4OTPSTOPA.setDisplayMask("###,###,##0.0000");
    osob4OTPSTOPA.setDefault("0");
    osob4OTPSTOPA.setTableName("OS_OBRADA4");
    osob4OTPSTOPA.setServerColumnName("OTPSTOPA");
    osob4OTPSTOPA.setSqlType(2);
    osob4CSKUPINE.setCaption("Rev. skupina");
    osob4CSKUPINE.setColumnName("CSKUPINE");
    osob4CSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4CSKUPINE.setPrecision(6);
    osob4CSKUPINE.setTableName("OS_OBRADA4");
    osob4CSKUPINE.setServerColumnName("CSKUPINE");
    osob4CSKUPINE.setSqlType(1);
    osob4KOEFICIJENT.setCaption("Koeficijent");
    osob4KOEFICIJENT.setColumnName("KOEFICIJENT");
    osob4KOEFICIJENT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4KOEFICIJENT.setPrecision(10);
    osob4KOEFICIJENT.setScale(2);
    osob4KOEFICIJENT.setDisplayMask("###,###,##0.00");
    osob4KOEFICIJENT.setDefault("0");
    osob4KOEFICIJENT.setTableName("OS_OBRADA4");
    osob4KOEFICIJENT.setServerColumnName("KOEFICIJENT");
    osob4KOEFICIJENT.setSqlType(2);
    osob4MJESEC.setCaption("Mjesec");
    osob4MJESEC.setColumnName("MJESEC");
    osob4MJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4MJESEC.setPrecision(2);
    osob4MJESEC.setTableName("OS_OBRADA4");
    osob4MJESEC.setServerColumnName("MJESEC");
    osob4MJESEC.setSqlType(1);
    osob4PORIJEKLO.setCaption("Porijeklo");
    osob4PORIJEKLO.setColumnName("PORIJEKLO");
    osob4PORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4PORIJEKLO.setPrecision(2);
    osob4PORIJEKLO.setTableName("OS_OBRADA4");
    osob4PORIJEKLO.setServerColumnName("PORIJEKLO");
    osob4PORIJEKLO.setSqlType(1);
    osob4RADNIK.setCaption("Radnik");
    osob4RADNIK.setColumnName("RADNIK");
    osob4RADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4RADNIK.setPrecision(6);
    osob4RADNIK.setTableName("OS_OBRADA4");
    osob4RADNIK.setServerColumnName("RADNIK");
    osob4RADNIK.setSqlType(1);
    osob4DATNABAVE.setCaption("Datum nabave");
    osob4DATNABAVE.setColumnName("DATNABAVE");
    osob4DATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob4DATNABAVE.setPrecision(8);
    osob4DATNABAVE.setDisplayMask("dd-MM-yyyy");
//    osob4DATNABAVE.setEditMask("dd-MM-yyyy");
    osob4DATNABAVE.setTableName("OS_OBRADA4");
    osob4DATNABAVE.setServerColumnName("DATNABAVE");
    osob4DATNABAVE.setSqlType(93);
    osob4DATNABAVE.setWidth(10);
    osob4DATAKTIVIRANJA.setCaption("Datum aktiviranja");
    osob4DATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    osob4DATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob4DATAKTIVIRANJA.setPrecision(8);
    osob4DATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    osob4DATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    osob4DATAKTIVIRANJA.setTableName("OS_OBRADA4");
    osob4DATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    osob4DATAKTIVIRANJA.setSqlType(93);
    osob4DATAKTIVIRANJA.setWidth(10);
    osob4DATPROMJENE.setCaption("Datum promjene");
    osob4DATPROMJENE.setColumnName("DATPROMJENE");
    osob4DATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob4DATPROMJENE.setPrecision(8);
    osob4DATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    osob4DATPROMJENE.setEditMask("dd-MM-yyyy");
    osob4DATPROMJENE.setTableName("OS_OBRADA4");
    osob4DATPROMJENE.setServerColumnName("DATPROMJENE");
    osob4DATPROMJENE.setSqlType(93);
    osob4DATPROMJENE.setWidth(10);
    osob4DATLIKVIDACIJE.setCaption("Datum likvidacije");
    osob4DATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    osob4DATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob4DATLIKVIDACIJE.setPrecision(8);
    osob4DATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    osob4DATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    osob4DATLIKVIDACIJE.setTableName("OS_OBRADA4");
    osob4DATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    osob4DATLIKVIDACIJE.setSqlType(93);
    osob4DATLIKVIDACIJE.setWidth(10);
    osob4OSNOVICA.setCaption("Osnovica");
    osob4OSNOVICA.setColumnName("OSNOVICA");
    osob4OSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4OSNOVICA.setPrecision(17);
    osob4OSNOVICA.setScale(2);
    osob4OSNOVICA.setDisplayMask("###,###,##0.00");
    osob4OSNOVICA.setDefault("0");
    osob4OSNOVICA.setTableName("OS_OBRADA4");
    osob4OSNOVICA.setServerColumnName("OSNOVICA");
    osob4OSNOVICA.setSqlType(2);
    osob4ISPRAVAK.setCaption("Ispravak");
    osob4ISPRAVAK.setColumnName("ISPRAVAK");
    osob4ISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4ISPRAVAK.setPrecision(17);
    osob4ISPRAVAK.setScale(2);
    osob4ISPRAVAK.setDisplayMask("###,###,##0.00");
    osob4ISPRAVAK.setDefault("0");
    osob4ISPRAVAK.setTableName("OS_OBRADA4");
    osob4ISPRAVAK.setServerColumnName("ISPRAVAK");
    osob4ISPRAVAK.setSqlType(2);
    osob4SADVRIJED.setCaption("Vrijednost");
    osob4SADVRIJED.setColumnName("SADVRIJED");
    osob4SADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4SADVRIJED.setPrecision(17);
    osob4SADVRIJED.setScale(2);
    osob4SADVRIJED.setDisplayMask("###,###,##0.00");
    osob4SADVRIJED.setDefault("0");
    osob4SADVRIJED.setTableName("OS_OBRADA4");
    osob4SADVRIJED.setServerColumnName("SADVRIJED");
    osob4SADVRIJED.setSqlType(2);
    osob4REVOSN.setCaption("Rev. isnovice");
    osob4REVOSN.setColumnName("REVOSN");
    osob4REVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4REVOSN.setPrecision(17);
    osob4REVOSN.setScale(2);
    osob4REVOSN.setDisplayMask("###,###,##0.00");
    osob4REVOSN.setDefault("0");
    osob4REVOSN.setTableName("OS_OBRADA4");
    osob4REVOSN.setServerColumnName("REVOSN");
    osob4REVOSN.setSqlType(2);
    osob4REVISP.setCaption("Rev. ispravka");
    osob4REVISP.setColumnName("REVISP");
    osob4REVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4REVISP.setPrecision(17);
    osob4REVISP.setScale(2);
    osob4REVISP.setDisplayMask("###,###,##0.00");
    osob4REVISP.setDefault("0");
    osob4REVISP.setTableName("OS_OBRADA4");
    osob4REVISP.setServerColumnName("REVISP");
    osob4REVISP.setSqlType(2);
    osob4REVSAD.setCaption("Revalorizacija");
    osob4REVSAD.setColumnName("REVSAD");
    osob4REVSAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4REVSAD.setPrecision(17);
    osob4REVSAD.setScale(2);
    osob4REVSAD.setDisplayMask("###,###,##0.00");
    osob4REVSAD.setDefault("0");
    osob4REVSAD.setTableName("OS_OBRADA4");
    osob4REVSAD.setServerColumnName("REVSAD");
    osob4REVSAD.setSqlType(2);
    osob4AMORTIZACIJA.setCaption("Amortizacija");
    osob4AMORTIZACIJA.setColumnName("AMORTIZACIJA");
    osob4AMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4AMORTIZACIJA.setPrecision(17);
    osob4AMORTIZACIJA.setScale(2);
    osob4AMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob4AMORTIZACIJA.setDefault("0");
    osob4AMORTIZACIJA.setTableName("OS_OBRADA4");
    osob4AMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    osob4AMORTIZACIJA.setSqlType(2);
    osob4AMOR1.setCaption("Amor1");
    osob4AMOR1.setColumnName("AMOR1");
    osob4AMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4AMOR1.setPrecision(17);
    osob4AMOR1.setScale(2);
    osob4AMOR1.setDisplayMask("###,###,##0.00");
    osob4AMOR1.setDefault("0");
    osob4AMOR1.setTableName("OS_OBRADA4");
    osob4AMOR1.setServerColumnName("AMOR1");
    osob4AMOR1.setSqlType(2);
    osob4AMOR2.setCaption("Amor2");
    osob4AMOR2.setColumnName("AMOR2");
    osob4AMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4AMOR2.setPrecision(17);
    osob4AMOR2.setScale(2);
    osob4AMOR2.setDisplayMask("###,###,##0.00");
    osob4AMOR2.setDefault("0");
    osob4AMOR2.setTableName("OS_OBRADA4");
    osob4AMOR2.setServerColumnName("AMOR2");
    osob4AMOR2.setSqlType(2);
    osob4PAMORTIZACIJA.setCaption("Pov. amortizacija");
    osob4PAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    osob4PAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4PAMORTIZACIJA.setPrecision(17);
    osob4PAMORTIZACIJA.setScale(2);
    osob4PAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob4PAMORTIZACIJA.setDefault("0");
    osob4PAMORTIZACIJA.setTableName("OS_OBRADA4");
    osob4PAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    osob4PAMORTIZACIJA.setSqlType(2);
    osob4PAMOR1.setCaption("Pamor1");
    osob4PAMOR1.setColumnName("PAMOR1");
    osob4PAMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4PAMOR1.setPrecision(17);
    osob4PAMOR1.setScale(2);
    osob4PAMOR1.setDisplayMask("###,###,##0.00");
    osob4PAMOR1.setDefault("0");
    osob4PAMOR1.setTableName("OS_OBRADA4");
    osob4PAMOR1.setServerColumnName("PAMOR1");
    osob4PAMOR1.setSqlType(2);
    osob4PAMOR2.setCaption("Pamor2");
    osob4PAMOR2.setColumnName("PAMOR2");
    osob4PAMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4PAMOR2.setPrecision(17);
    osob4PAMOR2.setScale(2);
    osob4PAMOR2.setDisplayMask("###,###,##0.00");
    osob4PAMOR2.setDefault("0");
    osob4PAMOR2.setTableName("OS_OBRADA4");
    osob4PAMOR2.setServerColumnName("PAMOR2");
    osob4PAMOR2.setSqlType(2);
    osob4REVAMOR.setCaption("Rev. amortizacije");
    osob4REVAMOR.setColumnName("REVAMOR");
    osob4REVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4REVAMOR.setPrecision(17);
    osob4REVAMOR.setScale(2);
    osob4REVAMOR.setDisplayMask("###,###,##0.00");
    osob4REVAMOR.setDefault("0");
    osob4REVAMOR.setTableName("OS_OBRADA4");
    osob4REVAMOR.setServerColumnName("REVAMOR");
    osob4REVAMOR.setSqlType(2);
    osob4PREBACAM.setCaption("Preb. amortizacije");
    osob4PREBACAM.setColumnName("PREBACAM");
    osob4PREBACAM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4PREBACAM.setPrecision(17);
    osob4PREBACAM.setScale(2);
    osob4PREBACAM.setDisplayMask("###,###,##0.00");
    osob4PREBACAM.setDefault("0");
    osob4PREBACAM.setTableName("OS_OBRADA4");
    osob4PREBACAM.setServerColumnName("PREBACAM");
    osob4PREBACAM.setSqlType(2);
    osob4VK.setCaption("");
    osob4VK.setColumnName("VK");
    osob4VK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob4VK.setPrecision(3);
    osob4VK.setTableName("OS_OBRADA4");
    osob4VK.setServerColumnName("VK");
    osob4VK.setSqlType(1);
    osob4UOSNOVICA.setCaption("Osnovica");
    osob4UOSNOVICA.setColumnName("UOSNOVICA");
    osob4UOSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4UOSNOVICA.setPrecision(17);
    osob4UOSNOVICA.setScale(2);
    osob4UOSNOVICA.setDisplayMask("###,###,##0.00");
    osob4UOSNOVICA.setDefault("0");
    osob4UOSNOVICA.setTableName("OS_OBRADA4");
    osob4UOSNOVICA.setServerColumnName("UOSNOVICA");
    osob4UOSNOVICA.setSqlType(2);
    osob4UISPRAVAK.setCaption("Ispravak");
    osob4UISPRAVAK.setColumnName("UISPRAVAK");
    osob4UISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4UISPRAVAK.setPrecision(17);
    osob4UISPRAVAK.setScale(2);
    osob4UISPRAVAK.setDisplayMask("###,###,##0.00");
    osob4UISPRAVAK.setDefault("0");
    osob4UISPRAVAK.setTableName("OS_OBRADA4");
    osob4UISPRAVAK.setServerColumnName("UISPRAVAK");
    osob4UISPRAVAK.setSqlType(2);
    osob4USADVRIJED.setCaption("Sadašnja vrijednost");
    osob4USADVRIJED.setColumnName("USADVRIJED");
    osob4USADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob4USADVRIJED.setPrecision(17);
    osob4USADVRIJED.setScale(2);
    osob4USADVRIJED.setDisplayMask("###,###,##0.00");
    osob4USADVRIJED.setDefault("0");
    osob4USADVRIJED.setTableName("OS_OBRADA4");
    osob4USADVRIJED.setServerColumnName("USADVRIJED");
    osob4USADVRIJED.setSqlType(2);
    osob4.setResolver(dm.getQresolver());
    osob4.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Obrada4", null, true, Load.ALL));
 setColumns(new Column[] {osob4LOKK, osob4AKTIV, osob4CORG, osob4NAZIV, osob4INVBROJ, osob4NAZSREDSTVA, osob4COBJEKT, osob4CLOKACIJE, osob4CARTIKLA,
        osob4CPAR, osob4BROJKONTA, osob4CGRUPE, osob4ZAKSTOPA, osob4ODLSTOPA, osob4OTPSTOPA, osob4CSKUPINE, osob4KOEFICIJENT, osob4MJESEC, osob4PORIJEKLO,
        osob4RADNIK, osob4DATNABAVE, osob4DATAKTIVIRANJA, osob4DATPROMJENE, osob4DATLIKVIDACIJE, osob4OSNOVICA, osob4ISPRAVAK, osob4SADVRIJED, osob4REVOSN,
        osob4REVISP, osob4REVSAD, osob4AMORTIZACIJA, osob4AMOR1, osob4AMOR2, osob4PAMORTIZACIJA, osob4PAMOR1, osob4PAMOR2, osob4REVAMOR, osob4PREBACAM, osob4VK,
        osob4UOSNOVICA, osob4UISPRAVAK, osob4USADVRIJED});
  }

  public void setall() {

    ddl.create("OS_Obrada4")
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
       .addFloat("uosnovica", 17, 2)
       .addFloat("uispravak", 17, 2)
       .addFloat("usadvrijed", 17, 2)
       .addPrimaryKey("corg,invbroj");


    Naziv = "OS_Obrada4";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
