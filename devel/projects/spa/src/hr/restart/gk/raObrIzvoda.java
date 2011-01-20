/****license*****************************************************************
**   file: raObrIzvoda.java
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
package hr.restart.gk;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;

import java.math.BigDecimal;

import com.borland.dx.sql.dataset.QueryDataSet;


public class raObrIzvoda {
  private static BigDecimal nula = new BigDecimal(0.0);
  private static raObrIzvoda obrIzv;
  private frmIzvodi fIzvodi;
  private hr.restart.baza.dM dm;
  protected raObrIzvoda() {
    dm = hr.restart.baza.dM.getDataModule();
  }
  public static raObrIzvoda getRaObrIzvoda() {
    if (obrIzv==null) obrIzv = new raObrIzvoda();
    return obrIzv;
  }
  public boolean obradaIzvoda(final frmIzvodi frmI,final boolean pokriv) {
    boolean b = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        return obradaIzvodaTrans(frmI,pokriv);
      }
    }.execTransaction();
    if (!b) return false;
    dM.getSynchronizer().markAsDirty("pokriveni");
/*
    if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKuSK(fIzvodi,pokriv)) {
      javax.swing.JOptionPane.showConfirmDialog(null,"Obrada temeljnice uspjela, ali greška u pokrivanju","Obrada",javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
    }
*/
    return true;
  }
  
  /**
   * kopira gkstavkerad u skstavke pokrivajuci i puneci kumulative
   * @param gk DataSet gkstavkerad
   * @param tecaj tecaj ako nije zapisan na stavci
   * @param jedval jedinica valute
   * @param fiz_ziro ziro ako treba
   * @param fiz_oznval globalna oznaka valute
   * @param pokriv pokrenuti automatsko pokrivanje ili ne
   * @return jel uspio il' ne
   * @deprecated Stara and spora metoda
   */  
  public static boolean smarterCopyGKuSK_deprecated(QueryDataSet gk, BigDecimal tecaj, BigDecimal jedval, String fiz_ziro, String fiz_oznval, boolean pokriv) {
      QueryDataSet gk1 = new QueryDataSet();
      gk1.setColumns(gk.cloneColumns());
      gk1.open();
      
      gk.first();
      while (gk.inBounds()) {
        if (gk.getString("OZNVAL").equals("")) {
          gk.setBigDecimal("TECAJ", tecaj);
          gk.setBigDecimal("DEVID", gk.getBigDecimal("ID").divide(jedval, BigDecimal.ROUND_HALF_UP).multiply(tecaj));
          gk.setBigDecimal("DEVIP", gk.getBigDecimal("IP").divide(jedval, BigDecimal.ROUND_HALF_UP).multiply(tecaj));
          gk1.emptyAllRows();
          gk1.insertRow(false);
          gk.copyTo(gk1);
          gk1.post();
//          System.out.println("KUNSKI");
          sysoutTEST ST = new sysoutTEST(false);
          ST.prn(gk1);
          if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKuSK(gk1,pokriv,fiz_ziro,fiz_oznval)) return false;
          gk.setBigDecimal("ID", gk.getBigDecimal("DEVID"));
          gk.setBigDecimal("IP", gk.getBigDecimal("DEVIP"));     
          //if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKuSK(fIzvodi,pokriv)) return false;
        } else {
//          gk.setBigDecimal("TECAJ", tecaj);
          BigDecimal pvID = gk.getBigDecimal("ID");
          BigDecimal devID = gk.getBigDecimal("DEVID");
          BigDecimal pvIP = gk.getBigDecimal("IP");
          BigDecimal devIP = gk.getBigDecimal("DEVIP");
          gk.setBigDecimal("DEVID", pvID);
          gk.setBigDecimal("DEVIP", pvIP);
          gk.setBigDecimal("ID", devID);
          gk.setBigDecimal("IP", devIP);
          gk1.emptyAllRows();
          gk1.insertRow(false);
          gk.copyTo(gk1);
//          System.out.println("DEVIZNI");
          sysoutTEST ST = new sysoutTEST(false);
          ST.prn(gk1);
          gk1.post();
          if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKuSK(gk1,pokriv,fiz_ziro,gk.getString("OZNVAL"))) return false;
          gk.setBigDecimal("DEVID", devID);
          gk.setBigDecimal("DEVIP", devIP);
          gk.setBigDecimal("ID", pvID);
          gk.setBigDecimal("IP", pvIP);
        }
        gk.next();
      }    
      return true;
  }
  /**
   * kopira gkstavkerad u skstavke pokrivajuci i puneci kumulative
   * @param gk DataSet gkstavkerad
   * @param tecaj tecaj ako nije zapisan na stavci
   * @param jedval jedinica valute
   * @param fiz_ziro ziro ako treba
   * @param fiz_oznval globalna oznaka valute
   * @param pokriv pokrenuti automatsko pokrivanje ili ne
   * @return jel uspio il' ne
   */  
  public static boolean smarterCopyGKuSK(QueryDataSet gk, BigDecimal tecaj, BigDecimal jedval, String fiz_ziro, String fiz_oznval, boolean pokriv) {
    //priprema
    if (!raKnjizenjeSK.getRaKnjizenjeSK().openSKknjizenje()) {
      System.out.println("raKnjizenjeSK.openSKknjizenje FAILED !!!");
      return false;
    }
////////////////////////
//System.out.println("==> smarterCopyGKuSK(");
//System.out.println("tecaj = "+tecaj);
//System.out.println("jedval = "+jedval);
////////////////////////
    for (gk.first(); gk.inBounds(); gk.next()) {
      if (!raSaldaKonti.isDomVal(fiz_oznval)) {
        
        if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKstavka(gk, pokriv, fiz_ziro, fiz_oznval)) {
          System.out.println("raKnjizenjeSK.copyGKstavka FAILED !!!");
          System.out.println("row = "+gk);
          return false;
        }
        BigDecimal pvID = gk.getBigDecimal("ID");
        BigDecimal devID = gk.getBigDecimal("DEVID");
        BigDecimal pvIP = gk.getBigDecimal("IP");
        BigDecimal devIP = gk.getBigDecimal("DEVIP");
        gk.setBigDecimal("DEVID", pvID);
        gk.setBigDecimal("DEVIP", pvIP);
        gk.setBigDecimal("ID", devID);
        gk.setBigDecimal("IP", devIP);
        gk.setString("OZNVAL", fiz_oznval);
      } else if (gk.getString("OZNVAL").equals("")) {
////////////////////////
//System.out.println("KNJIZENJE PO VALUTI: "+fiz_oznval);
//System.out.println("ID = "+gk.getBigDecimal("ID"));
//System.out.println("IP = "+gk.getBigDecimal("IP"));
//System.out.println("DEVID = "+gk.getBigDecimal("ID").divide(jedval, BigDecimal.ROUND_HALF_UP).multiply(tecaj));
//System.out.println("DEVIP = "+gk.getBigDecimal("IP").divide(jedval, BigDecimal.ROUND_HALF_UP).multiply(tecaj));
////////////////////////
        gk.setBigDecimal("TECAJ", tecaj);
        gk.setBigDecimal("DEVID", gk.getBigDecimal("ID").divide(jedval, BigDecimal.ROUND_HALF_UP).multiply(tecaj));
        gk.setBigDecimal("DEVIP", gk.getBigDecimal("IP").divide(jedval, BigDecimal.ROUND_HALF_UP).multiply(tecaj));
        
        if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKstavka(gk, pokriv, fiz_ziro, fiz_oznval)) {
          System.out.println("raKnjizenjeSK.copyGKstavka FAILED !!!");
          System.out.println("row = "+gk);
          return false;
        }
        
        gk.setBigDecimal("ID", gk.getBigDecimal("DEVID"));
        gk.setBigDecimal("IP", gk.getBigDecimal("DEVIP"));
      } else {
        BigDecimal pvID = gk.getBigDecimal("ID");
        BigDecimal devID = gk.getBigDecimal("DEVID");
        BigDecimal pvIP = gk.getBigDecimal("IP");
        BigDecimal devIP = gk.getBigDecimal("DEVIP");
//////////////////////////
//System.out.println("KNJIZENJE PO VISE VALUTA:");
//System.out.println("OZNVAL = "+gk.getString("OZNVAL"));
//System.out.println("TECAJ = "+gk.getString("TECAJ"));
//System.out.println("pvID = "+pvID);
//System.out.println("devID = "+devID);
//System.out.println("pvIP = "+pvIP);
//System.out.println("devIP = "+devIP);
//////////////////////////
        gk.setBigDecimal("DEVID", pvID);
        gk.setBigDecimal("DEVIP", pvIP);
        gk.setBigDecimal("ID", devID);
        gk.setBigDecimal("IP", devIP);
        if (!raKnjizenjeSK.getRaKnjizenjeSK().copyGKstavka(gk, pokriv, fiz_ziro, null)) {
          System.out.println("raKnjizenjeSK.copyGKstavka FAILED !!!");
          System.out.println("oznval = "+gk.getString("OZNVAL"));
          System.out.println("row = "+gk);
          return false;
        }
        gk.setBigDecimal("DEVID", devID);
        gk.setBigDecimal("DEVIP", devIP);
        gk.setBigDecimal("ID", pvID);
        gk.setBigDecimal("IP", pvIP);
      }
    }
    //finisiranje
    if (!raKnjizenjeSK.getRaKnjizenjeSK().saveSKknjizenje()) {
      System.out.println("raKnjizenjeSK.saveSKknjizenje FAILED !!!");
      return false;
    }
    return true;
  }
  private boolean obradaIzvodaTrans(frmIzvodi frmI,boolean pokriv) {
    try {
      fIzvodi = frmI;
      fIzvodi.getZiroParams();
//jel treba konto prometa
      boolean promet = dm.getZirorn().getString("PROMET").equals("D");
//dodaj stavku prometa
      if (promet) addStavkuPrometa();
      /** @todo nadji tecaj za fizvodi.oznval i */
      BigDecimal tecaj = Aus.zero0;//fIzvodi.getDetailSet().getBigDecimal("TECAJ");
      if (tecaj.signum() == 0)
        tecaj = hr.restart.zapod.Tecajevi.getTecaj(fIzvodi.getMasterSet().getTimestamp("DATUM"),fIzvodi.oznval);
      if (tecaj.signum() == 0 && raSaldaKonti.isDomVal(fIzvodi.oznval))
        tecaj = Aus.one0;

      if (tecaj.signum() == 0) {
System.out.println("tecaj.signum() == 0  FAILED !!!!");
        return false;
      }
      if (!lookupData.getlookupData().raLocate(dm.getValute(), new String[] {"OZNVAL"}, new String[] {fIzvodi.oznval})) {
System.out.println("Nema valute FAILED !!!");
        return false;
      }
        

      BigDecimal jedval = new BigDecimal((double) dm.getValute().getInt("JEDVAL"));
      QueryDataSet gk = fIzvodi.getDetailSet();
      if (!smarterCopyGKuSK(gk, tecaj, jedval, fIzvodi.ziro, fIzvodi.oznval, pokriv)) return false;
      raTransaction.saveChanges(gk);
      //zadnja kontrola naloga ID=IP=KONTRIZNOS
      finalNalogCheck(fIzvodi.getKnjizenje().getFNalozi().getMasterSet());
      if (!raObrNaloga.getRaObrNaloga().obradaNaloga(fIzvodi.getKnjizenje().getFNalozi())) return false;
      fIzvodi.getMasterSet().setString("STATUS","K");
//      fIzvodi.getMasterSet().saveChanges();
      raTransaction.saveChanges(fIzvodi.getMasterSet());
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  private void finalNalogCheck(QueryDataSet nalog) throws Exception {
    BigDecimal _ID = Util.getUtil().setScale(nalog.getBigDecimal("ID"),2);
    BigDecimal _IP = Util.getUtil().setScale(nalog.getBigDecimal("IP"),2);
    BigDecimal _KONTRIZNOS = Util.getUtil().setScale(nalog.getBigDecimal("KONTRIZNOS"),2);
    if (_ID.compareTo(_IP) != 0) {
      throw new Exception("Fatalna greska!! Nalog nije u balansu!");
    } else {
      /* da napuni kontrolni iznos koji i nije potreban 
       * samo zeza pri provjeri kumulativa naloga u alatima
       * (raGKKontrole.checkKumul)
       */
      if (_KONTRIZNOS.compareTo(_ID)!=0) nalog.setBigDecimal("KONTRIZNOS", _ID);
    }
  }
  
  private void addStavkaPrometa(String kto, BigDecimal id, BigDecimal ip, String opis) {
    fIzvodi.getDetailSet().insertRow(false);
    fIzvodi.knjizenje.getFNalozi().newStavka();
    fIzvodi.knjizenje.getFNalozi().jpDetail.jpBrNal.noviBrojStavke();
    fIzvodi.getDetailSet().setTimestamp("DATDOK",fIzvodi.getMasterSet().getTimestamp("DATUM"));
    fIzvodi.getDetailSet().setString("CNALOGA",fIzvodi.getMasterSet().getString("CNALOGA"));
    fIzvodi.getDetailSet().setInt("BROJIZV",fIzvodi.getMasterSet().getInt("BROJIZV"));
    fIzvodi.getDetailSet().setBigDecimal("ID",id);
    fIzvodi.getDetailSet().setBigDecimal("IP",ip);
    fIzvodi.getDetailSet().setString("BROJKONTA", kto);
    fIzvodi.getDetailSet().setString("OPIS", opis);
    fIzvodi.getDetailSet().setString("CORG",hr.restart.zapod.OrgStr.getKNJCORG(false));
//    fIzvodi.postStavkaNaloga();
    fIzvodi.knjizenje.getFNalozi().prepareForSaveStavka();
    fIzvodi.knjizenje.getFNalozi().updStavka(false);
    raTransaction.saveChanges(fIzvodi.getDetailSet());
  }
  
  private void addStavkuPrometa() throws Exception {
    String brojkonta = dm.getZirorn().getString("BROJKONTA");
    String brojkomp = dm.getZirorn().getString("KONTOKOMP");
    BigDecimal izID = fIzvodi.getMasterSet().getBigDecimal("ID");
    BigDecimal izIP = fIzvodi.getMasterSet().getBigDecimal("IP");
    // ab.f iskljuci kompenzacije iz proracuna
    BigDecimal kID = Aus.sum("ID", fIzvodi.getDetailSet(), "POKRIVENO", "K");
    BigDecimal kIP = Aus.sum("IP", fIzvodi.getDetailSet(), "POKRIVENO", "K");

    BigDecimal devID = Aus.sum("DEVID", fIzvodi.getDetailSet());
    BigDecimal devIP = Aus.sum("DEVIP", fIzvodi.getDetailSet());
    
    if (brojkomp.length() > 0) {
      addStavkaPrometa(brojkonta, izIP.subtract(kIP), izID.subtract(kID), getOpisPrometaIzvoda());
      addStavkaPrometa(brojkomp, kIP, kID, getOpisKompIzvoda());
    } else addStavkaPrometa(brojkonta, izIP, izID, getOpisPrometaIzvoda());
    
    if (fIzvodi.devizni) {
      fIzvodi.getDetailSet().setBigDecimal("DEVID", devIP);
      fIzvodi.getDetailSet().setBigDecimal("DEVIP", devID);
      fIzvodi.getDetailSet().setString("OZNVAL", fIzvodi.oznval);
    }
    
    // ab.f end
    //    fIzvodi.knjizenje.getFNalozi().getMasterSet().saveChanges();
    raTransaction.saveChanges(fIzvodi.knjizenje.getFNalozi().getMasterSet());
    
//    fIzvodi.getDetailSet().saveChanges();
  }

  private String getOpisPrometaIzvoda() {
    VarStr txtpromizv = new VarStr(frmParam.getParam("gk","txtpromizv","Promet izvoda br. $brojizv - $zrval","Opis stavke prometa izvoda ($brojizv = broj, $zrval = ziro/valuta)"));
    VarStr opispromizv = txtpromizv.replace("$brojizv", fIzvodi.getMasterSet().getInt("BROJIZV")+"")
    															 .replace("$zrval", fIzvodi.idizvod);
    
    //return "Promet izvoda br."+fIzvodi.getMasterSet().getInt("BROJIZV")+" - "+fIzvodi.idizvod;
    return opispromizv.toString();
  }
  
  private String getOpisKompIzvoda() {
    VarStr txtpromizv = new VarStr(frmParam.getParam("gk","txtkompizv","Kompenzacije po izvodu br. $brojizv - $zrval","Opis stavke kompenzacija po izvodu ($brojizv = broj, $zrval = ziro/valuta)"));
    VarStr opispromizv = txtpromizv.replace("$brojizv", fIzvodi.getMasterSet().getInt("BROJIZV")+"")
                                                                 .replace("$zrval", fIzvodi.idizvod);
    
    //return "Promet izvoda br."+fIzvodi.getMasterSet().getInt("BROJIZV")+" - "+fIzvodi.idizvod;
    return opispromizv.toString();
  }

  private static jpBrojNaloga jpbrnal;
  private static QueryDataSet sks;
  private static int oldrbs;
  public static String[] getBrojDok_GKS(com.borland.dx.dataset.DataSet gks) {
    if (jpbrnal == null) jpbrnal = new jpBrojNaloga();

    if (!(sks != null
        && jpbrnal.knjig.equals(gks.getString("KNJIG"))
        && jpbrnal.cvrnal.equals(gks.getString("CVRNAL"))
        && jpbrnal.rbr==gks.getInt("RBR")
        && oldrbs == gks.getInt("RBS")
        )) {
//System.out.println("radim za "+jpbrnal.getCNaloga()+"-"+gks.getInt("RBS"));
      jpbrnal.initJP(gks);
      sks = hr.restart.baza.Skstavke.getDataModule()
          .getTempSet(hr.restart.baza.Condition.equal("CGKSTAVKE",jpbrnal.getCNaloga()+"-"+gks.getInt("RBS")));
      oldrbs = gks.getInt("RBS");
      sks.open();
    }
    return new String[] {sks.getString("BROJDOK"), sks.getString("EXTBRDOK")} ;
  }
}