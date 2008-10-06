/****license*****************************************************************
**   file: raObrNaloga.java
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

import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raObrNaloga {
  static raObrNaloga obrNal = null;
  Util ut = Util.getUtil();
  lookupData ld = lookupData.getlookupData();
  QueryDataSet qdsNalog;
  QueryDataSet qdsStavke;
  QueryDataSet qdsKumulativi;
  QueryDataSet qdsGKStavke;
  String[] copyGKStavkeNames;
  String godmj;
  String knjig;
  String god;
  String cvrnal;
  frmKnjizenje fKnjizenje = null;
  int rbr;

  protected raObrNaloga() {
  }
  public static raObrNaloga getRaObrNaloga() {
    if (obrNal == null) obrNal = new raObrNaloga();
    return obrNal;
  }
  boolean pokriti = false;
  public boolean obradaNaloga(raMasterDetail raMD) {
    try {
//inicijalizacija
      initObrNaloga(raMD);
//prolaz kroz stavke
      makeKumul(qdsStavke,qdsGKStavke,true);
//    TU PROMIJENI kljuc ako se radi o doknjizavanju
      doknjizi(raMD);
//status na proknjiženo
      qdsNalog.setString("STATUS","K");
//snimi sve
      saveAllQds();
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      refreshAllQds();
      return false;
    }
  }
  /**
   * @param raMD
   * @param qdsGKStavke
   */
  private void doknjizi(raMasterDetail raMD) {
    if (raMD instanceof frmNalozi) {
      frmNalozi fmd = (frmNalozi)raMD;
      if (fmd.isDoknjizavanje() && fmd.nalogdoknjiz!=null) {
        String _dknj = fmd.nalogdoknjiz.getString("KNJIG");
      	String _dgod = fmd.nalogdoknjiz.getString("GOD");
      	String _dcvrnal = fmd.nalogdoknjiz.getString("CVRNAL");
      	int _drbr = fmd.nalogdoknjiz.getInt("RBR");
//prebaceno u frmNalozi kod dodjeljivanja cnaloga kako bi copyGKuSK dodijelio ispravan CGKSTAVKE na skstavke 
//      	int _drbsplus = gkQuerys.getMaxGkstavke___RBS(_dknj, _dgod, _dcvrnal, _drbr,"gkstavke");
        for (qdsGKStavke.first(); qdsGKStavke.inBounds(); qdsGKStavke.next()) {
          qdsGKStavke.setString("KNJIG", _dknj);
          qdsGKStavke.setString("GOD", _dgod);
          qdsGKStavke.setString("CVRNAL", _dcvrnal);
          qdsGKStavke.setInt("RBR", _drbr);
//        prebaceno u frmNalozi kod dodjeljivanja cnaloga kako bi copyGKuSK dodijelio ispravan CGKSTAVKE na skstavke          
//          qdsGKStavke.setInt("RBS",(qdsGKStavke.getInt("RBS")+_drbsplus));
        }
      }
    }
  }
  /**
   * Izbaceno iz obradaNaloga() jer je posebna transakcija, potrebno pozvati u 
   * frmNalozi i slicno (frmIzvodi? NEEE!) odmah nakon transakcije u kojoj je obradaNaloga
   */
  public boolean commitTransferAfterObradaNaloga() {
    //azuriraj stavke transfera
    if (fKnjizenje != null) {
      if (!fKnjizenje.commitTransfer()) return false;
      if (pokriti) {
        //zasebna transakcija
        //hr.restart.sk.raSaldaKonti.matchThemAll(fKnjizenje.getKnjizenje().skstavke);
        try {
          //u zajednickoj transakciji
          StorageDataSet sksds = fKnjizenje.getKnjizenje().skstavke;
          for (sksds.first(); sksds.inBounds(); sksds.next())
            hr.restart.sk.raSaldaKonti.matchIfYouCan(sksds, true);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    }
    return true;
  }
  public boolean raskNaloga(raMasterDetail raMD) {
    try {
//inicijalizacija
      initObrNaloga(raMD);
//prolaz kroz stavke
      makeKumul(qdsGKStavke,qdsStavke,false);
//TODO TU UGLAVI RASKNJIZAVANJE SK
//status na spremno
      qdsNalog.setString("STATUS","S");
//snimi sve
      saveAllQds();
//obrisi kumulative koji su se sveli na nulu
      gkQuerys.delKumulNul(godmj);
      return true;
    }
    catch (Exception ex) {
      refreshAllQds();
      ex.printStackTrace();
      return false;
    }
  }

  String getGodMj(com.borland.dx.dataset.DataSet qds) {
    if (qds.getTimestamp("DATUMKNJ").equals(ut.getFirstDayOfYear(qds.getTimestamp("DATUMKNJ")))
        && qds.getString("CVRNAL").equals("00")
        ) {
      return qds.getString("GOD").concat("00");
    } else return qds.getString("GOD").concat(ut.getMonth(qds.getTimestamp("DATUMKNJ")));
  }

  private void initObrNaloga(raMasterDetail raMD) throws Exception {
    raMD.refilterDetailSet();
    qdsNalog = raMD.getMasterSet();
    qdsStavke = raMD.getDetailSet();
    godmj = getGodMj(qdsNalog);
    knjig = qdsNalog.getString("KNJIG");
    god = qdsNalog.getString("GOD");
    cvrnal = qdsNalog.getString("CVRNAL");
    rbr = qdsNalog.getInt("RBR");
// zakljucaj
// daj sve kumulative od tekuceg mjeseca u dataset
    qdsKumulativi = gkQuerys.getGKKumulMj(godmj);
// daj gkstavke u nekom null queryu
    qdsGKStavke = gkQuerys.getGKStavkeNal(knjig,god,cvrnal,rbr);
    copyGKStavkeNames = qdsGKStavke.getColumnNames(qdsGKStavke.getColumnCount());
  }

  private void makeKumul(QueryDataSet qdsStavke_iz, QueryDataSet qdsStavke_u, boolean zbrajaj) throws Exception {
    qdsStavke_iz.first();
    do {
//azuriraj kumulativ, ako nema dodaj
      String corg = qdsStavke_iz.getString("CORG");
      String brojkonta = qdsStavke_iz.getString("BROJKONTA");
      double id = qdsStavke_iz.getBigDecimal("ID").doubleValue();
      double ip = qdsStavke_iz.getBigDecimal("IP").doubleValue();
      double kum_id = 0.0;
      double kum_ip = 0.0;
      boolean ima_kumulativa = ld.raLocate(qdsKumulativi,new String[] {"CORG","BROJKONTA","GODMJ"},
                                new String[] {corg,brojkonta,godmj});
      if (ima_kumulativa) {
        kum_id = qdsKumulativi.getBigDecimal("ID").doubleValue();
        kum_ip = qdsKumulativi.getBigDecimal("IP").doubleValue();
      } else {
        qdsKumulativi.insertRow(true);
        qdsKumulativi.setString("KNJIG",knjig);
        qdsKumulativi.setString("CORG",corg);
        qdsKumulativi.setString("BROJKONTA",brojkonta);
        qdsKumulativi.setString("GODMJ",godmj);
      }
      double new_kum_id;
      double new_kum_ip;
      if (zbrajaj) {
        new_kum_id = kum_id+id;
        new_kum_ip = kum_ip+ip;
      } else {
        new_kum_id = kum_id-id;
        new_kum_ip = kum_ip-ip;
      }
      qdsKumulativi.setBigDecimal("ID",Util.getUtil().setScale(new BigDecimal(new_kum_id),2));
      qdsKumulativi.setBigDecimal("IP",Util.getUtil().setScale(new BigDecimal(new_kum_ip),2));
//      qdsKumulativi.setBigDecimal("ID",new BigDecimal(new_kum_id));//ERROR!!
//      qdsKumulativi.setBigDecimal("IP",new BigDecimal(new_kum_ip));//ERROR!!
      qdsKumulativi.post();
//dodaj u gkstavke
      qdsStavke_u.insertRow(true);
//      QueryDataSet.copyTo(copyGKStavkeNames,qdsStavke_iz,copyGKStavkeNames,qdsStavke_u);
      myCopyTo(copyGKStavkeNames,qdsStavke_iz,qdsStavke_u);
      qdsStavke_u.post();
//dodaj u SK!!!
//
    } while (qdsStavke_iz.next());
//obrisi iz gkstavkerad
    qdsStavke_iz.deleteAllRows();
  }
  private void myCopyTo(String[] names,com.borland.dx.dataset.ReadRow r_iz, com.borland.dx.dataset.ReadWriteRow r_u) {
    for (int i=0;i<names.length;i++) {
      com.borland.dx.dataset.Column col_u = r_u.hasColumn(names[i]);
      com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
      if (col_u != null) {
        r_iz.getVariant(names[i],v);
        r_u.setVariant(names[i],v);
      }
    }
  }
  private void saveAllQds() throws Exception {//Znaci od sada OBAVEZNO U TRANSAKCIJI
    raTransaction.saveChanges(qdsGKStavke);
    raTransaction.saveChanges(qdsKumulativi);
    raTransaction.saveChanges(qdsStavke);
    raTransaction.saveChanges(qdsNalog);
//    qdsGKStavke.saveChanges();
//    qdsKumulativi.saveChanges();
//    qdsStavke.saveChanges();
//    qdsNalog.saveChanges();
  }
  private void refreshAllQds() {
    qdsGKStavke.refresh();
    qdsKumulativi.refresh();
    qdsStavke.refresh();
    qdsNalog.refresh();
  }
  
  public boolean hasSkStavke(String cnaloga) {
    String sql = "select count(*) from skstavke where cgkstavke like '"+cnaloga+"%' ";
    QueryDataSet cntds = Util.getNewQueryDataSet(sql,true);
    return Valid.getValid().getSetCount(cntds, 0)>0;
  }
}
