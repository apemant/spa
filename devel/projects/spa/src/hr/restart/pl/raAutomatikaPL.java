/****license*****************************************************************
**   file: raAutomatikaPL.java
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
package hr.restart.pl;
import hr.restart.db.raPreparedStatement;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raMasterDetail;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raAutomatikaPL {

  protected raAutomatikaPL() {
  }

  /**
   * dodaje zadani set odbitaka svim siframa nivoa
   * @param odbici4add
   * @param nivoToChange nivo (moze biti ZA,RA,OP,OJ,PO,VR)
   * za koji bi trebalo dodati odbitke (primjer poruke za nivoTochange= 'VR' -> Zelite li za sve vrste radnog odnosa dodati odbitke na ekranu?
   */
  public static boolean autoAddOdbici(final DataSet odbici4add, final String nivoToChange) {
    int answ = showMsg(nivoToChange);
    if (answ == 0) {
      autoAddOdbiciGo(odbici4add,nivoToChange);
    } else return false;
    return true;
  }

  private static void autoAddOdbiciGo(final DataSet odbici4add, final String nivoToChange) {
    new Thread() {
      public void run() {
        hr.restart.sisfun.raDelayWindow dw = hr.restart.sisfun.raDelayWindow.show();
        if (autoAddOdbiciRunnable(odbici4add,nivoToChange)) {
          operationSuccess(dw);
        } else {
          operationFailed(dw);
        }
      }
    }.start();
  }

  private static boolean autoAddOdbiciRunnable(final DataSet odbici4add, final String nivoToChange) {
    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
        return autoAddOdbiciTrans(odbici4add,nivoToChange);
      }
    }.execTransaction();
  }

  private static boolean autoAddOdbiciTrans(final DataSet odbici4add, final String nivoToChange) throws Exception {
    if (odbici4add.getRowCount() == 0) return false;
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    final String nivoKeyCol;
    final DataSet loopSet;
    if (nivoToChange.equals("ZA")) {
      nivoKeyCol = "CVRP";
      loopSet = dm.getVrsteprim();
    } else if (nivoToChange.equals("RA")) {
      nivoKeyCol = "CRADNIK";
      loopSet = dm.getRadnicipl();
    } else if (nivoToChange.equals("OP")) {
      nivoKeyCol = "COPCINE";
      loopSet = dm.getOpcine();
    } else if (nivoToChange.equals("OJ")) {
      nivoKeyCol = "CORG";
      loopSet = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
    } else if (nivoToChange.equals("VR")) {
      nivoKeyCol = "CVRO";
      loopSet = dm.getVrodn();
    } else return false;

    copyOdbici(odbici4add,loopSet,nivoToChange,nivoKeyCol);
    return true;
  }
  private static void copyOdbici(DataSet odbici,DataSet loopSet, String nivoToChange, String nivoKeyCol) throws Exception {
    try {
      QueryDataSet vrsteodb = hr.restart.baza.dM.getDataModule().getVrsteodb();
      QueryDataSet qodb = hr.restart.baza.dM.getDataModule().getOdbici();
      raPreparedStatement selstmt = new raPreparedStatement(qodb,raPreparedStatement.COUNT);
      raPreparedStatement updstmt = new raPreparedStatement(qodb,raPreparedStatement.UPDATE);
      raPreparedStatement addstmt = new raPreparedStatement(qodb,raPreparedStatement.INSERT);
      loopSet.open();
      Variant vky = new Variant();
      odbici.first();
      do {
        lookupData.getlookupData().raLocate(vrsteodb,new String[] {"CVRODB"},new String[] {odbici.getShort("CVRODB")+""});
        String nivo = vrsteodb.getString("NIVOODB");
        String key12 = nivo.startsWith(nivoToChange)?"CKEY":"CKEY2";
        String kyOdb = odbici.getString(key12);
        selstmt.setKeys(odbici);
        updstmt.setKeys(odbici);
        updstmt.setValues(odbici);
        addstmt.setValues(odbici);
        for (int i = 0; i < loopSet.getRowCount(); i++) {
          loopSet.getVariant(nivoKeyCol,i,vky);
          if (!vky.toString().equals(kyOdb)) { // ako nije u odbicima iz kojih dodajem vec ta sifra
            String ckyval = odbici.getString("CKEY");
            String ckyval2 = odbici.getString("CKEY2");
            if (key12.equals("CKEY")) ckyval = vky.toString();
              else ckyval2 = vky.toString();
            //jel postoji or not
            selstmt.setString("CKEY",ckyval,true);
            selstmt.setString("CKEY2",ckyval2,true);
            if (selstmt.isExist()) {
              //update
              updstmt.setString("CKEY",ckyval,true);
              updstmt.setString("CKEY2",ckyval2,true);
              updstmt.setString("CKEY",ckyval,false);
              updstmt.setString("CKEY2",ckyval2,false);
              updstmt.execute();
            } else {
              //add
              addstmt.setString("CKEY",ckyval,false);
              addstmt.setString("CKEY2",ckyval2,false);
              addstmt.execute();
            }
          }
        }
      } while (odbici.next());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * dodaje za svaku vrstu odbitka(= getMasterSet) unesene odbitke(=getDetailSet) svim siframa nivoa
   * @param mdVrsteOdb
   */
  public static void autoAddOdbici(raMasterDetail mdVrsteOdb, String nivoOdb) {
    int answ = showMsg(nivoOdb);
    if (answ == 0) {
      autoAddOdbiciGo(mdVrsteOdb,nivoOdb);
    }
  }

  private static void autoAddOdbiciGo(final raMasterDetail mdVrsteOdb, final String nivoOdb) {
    new Thread() {
      public void run() {
        hr.restart.sisfun.raDelayWindow dw = hr.restart.sisfun.raDelayWindow.show();
        if (autoAddOdbiciRunnable(mdVrsteOdb,nivoOdb)) {
          operationSuccess(dw);
        } else {
          operationFailed(dw);
        }
      }
    }.start();
  }

  private static boolean autoAddOdbiciRunnable(final raMasterDetail mdVrsteOdb, final String nivoOdb) {
    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
        return autoAddOdbiciTrans(mdVrsteOdb,nivoOdb);
      }
    }.execTransaction();
  }

  private static boolean autoAddOdbiciTrans(final raMasterDetail md, final String nivo) throws Exception {
    md.getMasterSet().first();
    do {
      md.refilterDetailSet();
      if (!autoAddOdbiciTrans(md.getDetailSet(),nivo)) return false;
    } while (md.getMasterSet().next());
    return true;
  }
  /**
   * za zadane vrste odbitka dodaje po jedan odbitak sa defaultnim vrijednostima (iznos,stopa) i zadanim kljucevima
   * @param vrsteOdb
   * @param nivo moze bit ZA,RA,OP,OJ,PO,VR
   * @param key vrijednost kljuca
   */
  public static void autoAddOdbiciForVrsteOdb(DataSet vrsteOdb,String nivo,String key) {
    throw new UnsupportedOperationException("not yet implemented!");
  }

  private static int showMsg(String nivoToChange) {
    StringBuffer msg = new StringBuffer("Želite li svim ? dodati / promijeniti odbitke na ekranu?");
    int x = msg.toString().indexOf("?");
    if (nivoToChange.equals("ZA")) {
      msg = msg.replace(x,x+1,"vrstama primanja");
    } else if (nivoToChange.equals("RA")) {
      msg = msg.replace(x,x+1,"radnicima");
    } else if (nivoToChange.equals("OP")) {
      msg = msg.replace(x,x+1,"op\u0107inama");
    } else if (nivoToChange.equals("OJ")) {
      msg = msg.replace(x,x+1,"org. jedinicama");
    } else if (nivoToChange.equals("VR")) {
      msg = msg.replace(x,x+1,"vrstama radnog odnosa");
    } else return -1;
    return JOptionPane.showOptionDialog(null,msg,"Automatika",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[] {"DA","NE"},"NE");
  }

  private static void operationSuccess(hr.restart.sisfun.raDelayWindow dw) {
    dw.close();
    JOptionPane.showMessageDialog(null,"Odbici su uspješno ažurirani!");
  }

  private static void operationFailed(hr.restart.sisfun.raDelayWindow dw) {
    dw.close();
    JOptionPane.showMessageDialog(null,"Ažuriranje odbitaka neuspješno!");
  }

/*  private static void test() {
    frmDetailRS frs = new frmDetailRS(null);
  }*/
}