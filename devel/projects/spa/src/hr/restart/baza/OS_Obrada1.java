/****license*****************************************************************
**   file: OS_Obrada1.java
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



public class OS_Obrada1 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Obrada1 OS_Obrada1class;

  QueryDataSet osob1 = new QueryDataSet();

  Column osob1LOKK = new Column();
  Column osob1AKTIV = new Column();
  Column osob1CORG = new Column();
  Column osob1NAZIV = new Column();
  Column osob1INVBROJ = new Column();
  Column osob1NAZSREDSTVA = new Column();
  Column osob1COBJEKT = new Column();
  Column osob1CLOKACIJE = new Column();
  Column osob1CARTIKLA = new Column();
  Column osob1CPAR = new Column();
  Column osob1BROJKONTA = new Column();
  Column osob1CGRUPE = new Column();
  Column osob1ZAKSTOPA = new Column();
  Column osob1ODLSTOPA = new Column();
  Column osob1OTPSTOPA = new Column();
  Column osob1CSKUPINE = new Column();
  Column osob1KOEFICIJENT = new Column();
  Column osob1MJESEC = new Column();
  Column osob1PORIJEKLO = new Column();
  Column osob1RADNIK = new Column();
  Column osob1DATNABAVE = new Column();
  Column osob1DATAKTIVIRANJA = new Column();
  Column osob1DATPROMJENE = new Column();
  Column osob1DATLIKVIDACIJE = new Column();
  Column osob1OSNOVICA = new Column();
  Column osob1ISPRAVAK = new Column();
  Column osob1SADVRIJED = new Column();
  Column osob1REVOSN = new Column();
  Column osob1REVISP = new Column();
  Column osob1REVSAD = new Column();
  Column osob1AMORTIZACIJA = new Column();
  Column osob1AMOR1 = new Column();
  Column osob1AMOR2 = new Column();
  Column osob1PAMORTIZACIJA = new Column();
  Column osob1PAMOR1 = new Column();
  Column osob1PAMOR2 = new Column();
  Column osob1REVAMOR = new Column();
  Column osob1PREBACAM = new Column();
  Column osob1VK = new Column();
  Column osob1COBRADA1 = new Column();

  public static OS_Obrada1 getDataModule() {
    if (OS_Obrada1class == null) {
      OS_Obrada1class = new OS_Obrada1();
    }
    return OS_Obrada1class;
  }

  public QueryDataSet getQueryDataSet() {
    return osob1;
  }

  public OS_Obrada1() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osob1LOKK.setCaption("Status zauzetosti");
    osob1LOKK.setColumnName("LOKK");
    osob1LOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1LOKK.setPrecision(1);
    osob1LOKK.setTableName("OS_OBRADA1");
    osob1LOKK.setServerColumnName("LOKK");
    osob1LOKK.setSqlType(1);
    osob1LOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob1LOKK.setDefault("N");
    osob1AKTIV.setCaption("Aktivan - neaktivan");
    osob1AKTIV.setColumnName("AKTIV");
    osob1AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1AKTIV.setPrecision(1);
    osob1AKTIV.setTableName("OS_OBRADA1");
    osob1AKTIV.setServerColumnName("AKTIV");
    osob1AKTIV.setSqlType(1);
    osob1AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osob1AKTIV.setDefault("D");
    osob1CORG.setCaption("OJ");
    osob1CORG.setColumnName("CORG");
    osob1CORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1CORG.setPrecision(12);
    osob1CORG.setTableName("OS_OBRADA1");
    osob1CORG.setServerColumnName("CORG");
    osob1CORG.setSqlType(1);
    osob1NAZIV.setCaption("Naziv");
    osob1NAZIV.setColumnName("NAZIV");
    osob1NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1NAZIV.setPrecision(50);
    osob1NAZIV.setTableName("OS_OBRADA1");
    osob1NAZIV.setServerColumnName("NAZIV");
    osob1NAZIV.setSqlType(1);
    osob1NAZIV.setWidth(30);
    osob1INVBROJ.setCaption("IBroj");
    osob1INVBROJ.setColumnName("INVBROJ");
    osob1INVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1INVBROJ.setPrecision(10);
    osob1INVBROJ.setTableName("OS_OBRADA1");
    osob1INVBROJ.setServerColumnName("INVBROJ");
    osob1INVBROJ.setSqlType(1);
    osob1NAZSREDSTVA.setCaption("Naziv sredstva");
    osob1NAZSREDSTVA.setColumnName("NAZSREDSTVA");
    osob1NAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1NAZSREDSTVA.setPrecision(50);
    osob1NAZSREDSTVA.setTableName("OS_OBRADA1");
    osob1NAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    osob1NAZSREDSTVA.setSqlType(1);
    osob1NAZSREDSTVA.setWidth(30);
    osob1COBJEKT.setCaption("Objekt");
    osob1COBJEKT.setColumnName("COBJEKT");
    osob1COBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1COBJEKT.setPrecision(6);
    osob1COBJEKT.setTableName("OS_OBRADA1");
    osob1COBJEKT.setServerColumnName("COBJEKT");
    osob1COBJEKT.setSqlType(1);
    osob1CLOKACIJE.setCaption("Lokacija");
    osob1CLOKACIJE.setColumnName("CLOKACIJE");
    osob1CLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1CLOKACIJE.setPrecision(12);
    osob1CLOKACIJE.setTableName("OS_OBRADA1");
    osob1CLOKACIJE.setServerColumnName("CLOKACIJE");
    osob1CLOKACIJE.setSqlType(1);
    osob1CARTIKLA.setCaption("Artikl");
    osob1CARTIKLA.setColumnName("CARTIKLA");
    osob1CARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1CARTIKLA.setPrecision(6);
    osob1CARTIKLA.setTableName("OS_OBRADA1");
    osob1CARTIKLA.setServerColumnName("CARTIKLA");
    osob1CARTIKLA.setSqlType(1);
    osob1CPAR.setCaption("Partner");
    osob1CPAR.setColumnName("CPAR");
    osob1CPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osob1CPAR.setPrecision(6);
    osob1CPAR.setTableName("OS_OBRADA1");
    osob1CPAR.setServerColumnName("CPAR");
    osob1CPAR.setSqlType(4);
    osob1CPAR.setWidth(6);
    osob1BROJKONTA.setCaption("Konto");
    osob1BROJKONTA.setColumnName("BROJKONTA");
    osob1BROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1BROJKONTA.setPrecision(8);
    osob1BROJKONTA.setTableName("OS_OBRADA1");
    osob1BROJKONTA.setServerColumnName("BROJKONTA");
    osob1BROJKONTA.setSqlType(1);
    osob1CGRUPE.setCaption("Amort. grupa");
    osob1CGRUPE.setColumnName("CGRUPE");
    osob1CGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1CGRUPE.setPrecision(6);
    osob1CGRUPE.setTableName("OS_OBRADA1");
    osob1CGRUPE.setServerColumnName("CGRUPE");
    osob1CGRUPE.setSqlType(1);
    osob1ZAKSTOPA.setCaption("Zakonska stopa");
    osob1ZAKSTOPA.setColumnName("ZAKSTOPA");
    osob1ZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1ZAKSTOPA.setPrecision(10);
    osob1ZAKSTOPA.setScale(4);
    osob1ZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osob1ZAKSTOPA.setDefault("0");
    osob1ZAKSTOPA.setTableName("OS_OBRADA1");
    osob1ZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osob1ZAKSTOPA.setSqlType(2);
    osob1ODLSTOPA.setCaption("Stopa po odluci");
    osob1ODLSTOPA.setColumnName("ODLSTOPA");
    osob1ODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1ODLSTOPA.setPrecision(10);
    osob1ODLSTOPA.setScale(4);
    osob1ODLSTOPA.setDisplayMask("###,###,##0.0000");
    osob1ODLSTOPA.setDefault("0");
    osob1ODLSTOPA.setTableName("OS_OBRADA1");
    osob1ODLSTOPA.setServerColumnName("ODLSTOPA");
    osob1ODLSTOPA.setSqlType(2);
    osob1OTPSTOPA.setCaption("Otp. stopa");
    osob1OTPSTOPA.setColumnName("OTPSTOPA");
    osob1OTPSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1OTPSTOPA.setPrecision(10);
    osob1OTPSTOPA.setScale(4);
    osob1OTPSTOPA.setDisplayMask("###,###,##0.0000");
    osob1OTPSTOPA.setDefault("0");
    osob1OTPSTOPA.setTableName("OS_OBRADA1");
    osob1OTPSTOPA.setServerColumnName("OTPSTOPA");
    osob1OTPSTOPA.setSqlType(2);
    osob1CSKUPINE.setCaption("Rev. skupina");
    osob1CSKUPINE.setColumnName("CSKUPINE");
    osob1CSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1CSKUPINE.setPrecision(6);
    osob1CSKUPINE.setTableName("OS_OBRADA1");
    osob1CSKUPINE.setServerColumnName("CSKUPINE");
    osob1CSKUPINE.setSqlType(1);
    osob1KOEFICIJENT.setCaption("Koeficijent");
    osob1KOEFICIJENT.setColumnName("KOEFICIJENT");
    osob1KOEFICIJENT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1KOEFICIJENT.setPrecision(10);
    osob1KOEFICIJENT.setScale(2);
    osob1KOEFICIJENT.setDisplayMask("###,###,##0.00");
    osob1KOEFICIJENT.setDefault("0");
    osob1KOEFICIJENT.setTableName("OS_OBRADA1");
    osob1KOEFICIJENT.setServerColumnName("KOEFICIJENT");
    osob1KOEFICIJENT.setSqlType(2);
    osob1MJESEC.setCaption("Mjesec");
    osob1MJESEC.setColumnName("MJESEC");
    osob1MJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1MJESEC.setPrecision(2);
    osob1MJESEC.setTableName("OS_OBRADA1");
    osob1MJESEC.setServerColumnName("MJESEC");
    osob1MJESEC.setSqlType(1);
    osob1PORIJEKLO.setCaption("Porijeklo");
    osob1PORIJEKLO.setColumnName("PORIJEKLO");
    osob1PORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1PORIJEKLO.setPrecision(2);
    osob1PORIJEKLO.setTableName("OS_OBRADA1");
    osob1PORIJEKLO.setServerColumnName("PORIJEKLO");
    osob1PORIJEKLO.setSqlType(1);
    osob1RADNIK.setCaption("Radnik");
    osob1RADNIK.setColumnName("RADNIK");
    osob1RADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1RADNIK.setPrecision(6);
    osob1RADNIK.setTableName("OS_OBRADA1");
    osob1RADNIK.setServerColumnName("RADNIK");
    osob1RADNIK.setSqlType(1);
    osob1DATNABAVE.setCaption("Datum nabave");
    osob1DATNABAVE.setColumnName("DATNABAVE");
    osob1DATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob1DATNABAVE.setPrecision(8);
    osob1DATNABAVE.setDisplayMask("dd-MM-yyyy");
//    osob1DATNABAVE.setEditMask("dd-MM-yyyy");
    osob1DATNABAVE.setTableName("OS_OBRADA1");
    osob1DATNABAVE.setServerColumnName("DATNABAVE");
    osob1DATNABAVE.setSqlType(93);
    osob1DATNABAVE.setWidth(10);
    osob1DATAKTIVIRANJA.setCaption("Datum aktiviranja");
    osob1DATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    osob1DATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob1DATAKTIVIRANJA.setPrecision(8);
    osob1DATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    osob1DATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    osob1DATAKTIVIRANJA.setTableName("OS_OBRADA1");
    osob1DATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    osob1DATAKTIVIRANJA.setSqlType(93);
    osob1DATAKTIVIRANJA.setWidth(10);
    osob1DATPROMJENE.setCaption("Datum promjene");
    osob1DATPROMJENE.setColumnName("DATPROMJENE");
    osob1DATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob1DATPROMJENE.setPrecision(8);
    osob1DATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    osob1DATPROMJENE.setEditMask("dd-MM-yyyy");
    osob1DATPROMJENE.setTableName("OS_OBRADA1");
    osob1DATPROMJENE.setServerColumnName("DATPROMJENE");
    osob1DATPROMJENE.setSqlType(93);
    osob1DATPROMJENE.setWidth(10);
    osob1DATLIKVIDACIJE.setCaption("Datum likvidacije");
    osob1DATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    osob1DATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osob1DATLIKVIDACIJE.setPrecision(8);
    osob1DATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    osob1DATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    osob1DATLIKVIDACIJE.setTableName("OS_OBRADA1");
    osob1DATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    osob1DATLIKVIDACIJE.setSqlType(93);
    osob1DATLIKVIDACIJE.setWidth(10);
    osob1OSNOVICA.setCaption("Osnovica");
    osob1OSNOVICA.setColumnName("OSNOVICA");
    osob1OSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1OSNOVICA.setPrecision(17);
    osob1OSNOVICA.setScale(2);
    osob1OSNOVICA.setDisplayMask("###,###,##0.00");
    osob1OSNOVICA.setDefault("0");
    osob1OSNOVICA.setTableName("OS_OBRADA1");
    osob1OSNOVICA.setServerColumnName("OSNOVICA");
    osob1OSNOVICA.setSqlType(2);
    osob1ISPRAVAK.setCaption("Ispravak");
    osob1ISPRAVAK.setColumnName("ISPRAVAK");
    osob1ISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1ISPRAVAK.setPrecision(17);
    osob1ISPRAVAK.setScale(2);
    osob1ISPRAVAK.setDisplayMask("###,###,##0.00");
    osob1ISPRAVAK.setDefault("0");
    osob1ISPRAVAK.setTableName("OS_OBRADA1");
    osob1ISPRAVAK.setServerColumnName("ISPRAVAK");
    osob1ISPRAVAK.setSqlType(2);
    osob1SADVRIJED.setCaption("Vrijednost");
    osob1SADVRIJED.setColumnName("SADVRIJED");
    osob1SADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1SADVRIJED.setPrecision(17);
    osob1SADVRIJED.setScale(2);
    osob1SADVRIJED.setDisplayMask("###,###,##0.00");
    osob1SADVRIJED.setDefault("0");
    osob1SADVRIJED.setTableName("OS_OBRADA1");
    osob1SADVRIJED.setServerColumnName("SADVRIJED");
    osob1SADVRIJED.setSqlType(2);
    osob1REVOSN.setCaption("Rev. isnovice");
    osob1REVOSN.setColumnName("REVOSN");
    osob1REVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1REVOSN.setPrecision(17);
    osob1REVOSN.setScale(2);
    osob1REVOSN.setDisplayMask("###,###,##0.00");
    osob1REVOSN.setDefault("0");
    osob1REVOSN.setTableName("OS_OBRADA1");
    osob1REVOSN.setServerColumnName("REVOSN");
    osob1REVOSN.setSqlType(2);
    osob1REVISP.setCaption("Rev. ispravka");
    osob1REVISP.setColumnName("REVISP");
    osob1REVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1REVISP.setPrecision(17);
    osob1REVISP.setScale(2);
    osob1REVISP.setDisplayMask("###,###,##0.00");
    osob1REVISP.setDefault("0");
    osob1REVISP.setTableName("OS_OBRADA1");
    osob1REVISP.setServerColumnName("REVISP");
    osob1REVISP.setSqlType(2);
    osob1REVSAD.setCaption("Revalorizacija");
    osob1REVSAD.setColumnName("REVSAD");
    osob1REVSAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1REVSAD.setPrecision(17);
    osob1REVSAD.setScale(2);
    osob1REVSAD.setDisplayMask("###,###,##0.00");
    osob1REVSAD.setDefault("0");
    osob1REVSAD.setTableName("OS_OBRADA1");
    osob1REVSAD.setServerColumnName("REVSAD");
    osob1REVSAD.setSqlType(2);
    osob1AMORTIZACIJA.setCaption("Amortizacija");
    osob1AMORTIZACIJA.setColumnName("AMORTIZACIJA");
    osob1AMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1AMORTIZACIJA.setPrecision(17);
    osob1AMORTIZACIJA.setScale(2);
    osob1AMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob1AMORTIZACIJA.setDefault("0");
    osob1AMORTIZACIJA.setTableName("OS_OBRADA1");
    osob1AMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    osob1AMORTIZACIJA.setSqlType(2);
    osob1AMOR1.setCaption("Amor1");
    osob1AMOR1.setColumnName("AMOR1");
    osob1AMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1AMOR1.setPrecision(17);
    osob1AMOR1.setScale(2);
    osob1AMOR1.setDisplayMask("###,###,##0.00");
    osob1AMOR1.setDefault("0");
    osob1AMOR1.setTableName("OS_OBRADA1");
    osob1AMOR1.setServerColumnName("AMOR1");
    osob1AMOR1.setSqlType(2);
    osob1AMOR2.setCaption("Amor2");
    osob1AMOR2.setColumnName("AMOR2");
    osob1AMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1AMOR2.setPrecision(17);
    osob1AMOR2.setScale(2);
    osob1AMOR2.setDisplayMask("###,###,##0.00");
    osob1AMOR2.setDefault("0");
    osob1AMOR2.setTableName("OS_OBRADA1");
    osob1AMOR2.setServerColumnName("AMOR2");
    osob1AMOR2.setSqlType(2);
    osob1PAMORTIZACIJA.setCaption("Pov. amortizacija");
    osob1PAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    osob1PAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1PAMORTIZACIJA.setPrecision(17);
    osob1PAMORTIZACIJA.setScale(2);
    osob1PAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osob1PAMORTIZACIJA.setDefault("0");
    osob1PAMORTIZACIJA.setTableName("OS_OBRADA1");
    osob1PAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    osob1PAMORTIZACIJA.setSqlType(2);
    osob1PAMOR1.setCaption("Pamor1");
    osob1PAMOR1.setColumnName("PAMOR1");
    osob1PAMOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1PAMOR1.setPrecision(17);
    osob1PAMOR1.setScale(2);
    osob1PAMOR1.setDisplayMask("###,###,##0.00");
    osob1PAMOR1.setDefault("0");
    osob1PAMOR1.setTableName("OS_OBRADA1");
    osob1PAMOR1.setServerColumnName("PAMOR1");
    osob1PAMOR1.setSqlType(2);
    osob1PAMOR2.setCaption("Pamor2");
    osob1PAMOR2.setColumnName("PAMOR2");
    osob1PAMOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1PAMOR2.setPrecision(17);
    osob1PAMOR2.setScale(2);
    osob1PAMOR2.setDisplayMask("###,###,##0.00");
    osob1PAMOR2.setDefault("0");
    osob1PAMOR2.setTableName("OS_OBRADA1");
    osob1PAMOR2.setServerColumnName("PAMOR2");
    osob1PAMOR2.setSqlType(2);
    osob1REVAMOR.setCaption("Rev. amortizacije");
    osob1REVAMOR.setColumnName("REVAMOR");
    osob1REVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1REVAMOR.setPrecision(17);
    osob1REVAMOR.setScale(2);
    osob1REVAMOR.setDisplayMask("###,###,##0.00");
    osob1REVAMOR.setDefault("0");
    osob1REVAMOR.setTableName("OS_OBRADA1");
    osob1REVAMOR.setServerColumnName("REVAMOR");
    osob1REVAMOR.setSqlType(2);
    osob1PREBACAM.setCaption("Preb. amortizacije");
    osob1PREBACAM.setColumnName("PREBACAM");
    osob1PREBACAM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osob1PREBACAM.setPrecision(17);
    osob1PREBACAM.setScale(2);
    osob1PREBACAM.setDisplayMask("###,###,##0.00");
    osob1PREBACAM.setDefault("0");
    osob1PREBACAM.setTableName("OS_OBRADA1");
    osob1PREBACAM.setServerColumnName("PREBACAM");
    osob1PREBACAM.setSqlType(2);
    osob1VK.setCaption("");
    osob1VK.setColumnName("VK");
    osob1VK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1VK.setPrecision(3);
    osob1VK.setTableName("OS_OBRADA1");
    osob1VK.setServerColumnName("VK");
    osob1VK.setSqlType(1);
    osob1COBRADA1.setCaption("Šifra");
    osob1COBRADA1.setColumnName("COBRADA1");
    osob1COBRADA1.setDataType(com.borland.dx.dataset.Variant.STRING);
    osob1COBRADA1.setPrecision(22);
    osob1COBRADA1.setRowId(true);
    osob1COBRADA1.setTableName("OS_OBRADA1");
    osob1COBRADA1.setServerColumnName("COBRADA1");
    osob1COBRADA1.setSqlType(1);
    osob1.setResolver(dm.getQresolver());
    osob1.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Obrada1", null, true, Load.ALL));
 setColumns(new Column[] {osob1LOKK, osob1AKTIV, osob1CORG, osob1NAZIV, osob1INVBROJ, osob1NAZSREDSTVA, osob1COBJEKT, osob1CLOKACIJE, osob1CARTIKLA,
        osob1CPAR, osob1BROJKONTA, osob1CGRUPE, osob1ZAKSTOPA, osob1ODLSTOPA, osob1OTPSTOPA, osob1CSKUPINE, osob1KOEFICIJENT, osob1MJESEC, osob1PORIJEKLO,
        osob1RADNIK, osob1DATNABAVE, osob1DATAKTIVIRANJA, osob1DATPROMJENE, osob1DATLIKVIDACIJE, osob1OSNOVICA, osob1ISPRAVAK, osob1SADVRIJED, osob1REVOSN,
        osob1REVISP, osob1REVSAD, osob1AMORTIZACIJA, osob1AMOR1, osob1AMOR2, osob1PAMORTIZACIJA, osob1PAMOR1, osob1PAMOR2, osob1REVAMOR, osob1PREBACAM, osob1VK,
        osob1COBRADA1});
  }

  public void setall() {

    ddl.create("OS_Obrada1")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12)
       .addChar("naziv", 50)
       .addChar("invbroj", 10)
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
       .addChar("cobrada1", 22, true)
       .addPrimaryKey("cobrada1");


    Naziv = "OS_Obrada1";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
