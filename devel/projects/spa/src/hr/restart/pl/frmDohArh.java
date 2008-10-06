/****license*****************************************************************
**   file: frmDohArh.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Kumulorg;
import hr.restart.baza.Kumulorgarh;
import hr.restart.baza.Kumulrad;
import hr.restart.baza.Kumulradarh;
import hr.restart.baza.Odbici;
import hr.restart.baza.Odbiciarh;
import hr.restart.baza.Odbiciobr;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Parametripl;
import hr.restart.baza.Primanjaarh;
import hr.restart.baza.Primanjaobr;
import hr.restart.baza.RSPeriod;
import hr.restart.baza.RSPeriodarh;
import hr.restart.baza.RSPeriodobr;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.Vrsteodb;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmDohArh extends frmIzvjestajiPL {

  public frmDohArh() {
    super('A');
  }
  public void okPress() {
    com.borland.dx.sql.dataset.QueryDataSet qds=Util.getNewQueryDataSet(sjQuerys.getOrgplFromArhiv(getWhereQuery()));
    qds.close();
    dm.getKumulorgarh().open();
    try {
      qds.setColumns(new Column[] {
        ((Column) dm.getKumulorgarh().getColumn("GODOBR").clone()),
        (Column) dm.getKumulorgarh().getColumn("MJOBR").clone(),
        (Column) dm.getKumulorgarh().getColumn("RBROBR").clone(),
//        (Column) dm.getKumulorgarh().getColumn("CORG").clone(),
        (Column) dm.getKumulorgarh().getColumn("SATI").clone(),
        (Column) dm.getKumulorgarh().getColumn("BRUTO").clone(),
        (Column) dm.getKumulorgarh().getColumn("DOPRINOSI").clone(),
        (Column) dm.getKumulorgarh().getColumn("NETO").clone(),
        (Column) dm.getKumulorgarh().getColumn("NEOP").clone(),
        (Column) dm.getKumulorgarh().getColumn("ISKNEOP").clone(),
        (Column) dm.getKumulorgarh().getColumn("POROSN").clone(),
        (Column) dm.getKumulorgarh().getColumn("POR1").clone(),
        (Column) dm.getKumulorgarh().getColumn("POR2").clone(),
        (Column) dm.getKumulorgarh().getColumn("POR3").clone(),
        (Column) dm.getKumulorgarh().getColumn("PRIR").clone(),
        (Column) dm.getKumulorgarh().getColumn("PORIPRIR").clone(),
        (Column) dm.getKumulorgarh().getColumn("NETO2").clone(),
        (Column) dm.getKumulorgarh().getColumn("NAKNADE").clone(),
        (Column) dm.getKumulorgarh().getColumn("NETOPK").clone(),
        (Column) dm.getKumulorgarh().getColumn("KREDITI").clone(),
        (Column) dm.getKumulorgarh().getColumn("NARUKE").clone(),
        (Column) dm.getKumulorgarh().getColumn("DOPRPOD").clone(),
        (Column) dm.getKumulorgarh().getColumn("DATUMISPL").clone()
      });

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    qds.open();
    hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(qds, 1, "Pregled obraèuna", getWhereQuery());
    startFrame.getStartFrame().showFrame(fpa);


//    startFrame.getStartFrame().showFrame("hr.restart.pl.frmPregledArhive", "Pregled arhive");
//    System.out.println("A: "+super.getWhereQuery());
//    System.out.println("B: "+super.getBetweenAhrQuery());
  }
  public boolean ispisNow() {
    return false;
  }

/*  private boolean makeTransaction() {
    hr.restart.db.raPreparedStatement p;
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.runSQL(sjQuerys.delKUMULORGARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delKUMULRADARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delPRIMANJAARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delODBICIARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          return true;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw ex;
        }

      }
    };
    boolean b=trans.execTransaction();
    if (b) {
      JOptionPane.showConfirmDialog(jp,"Obraèun je prenešen iz arhive !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      this.hide();
    }
    return b;
  }*/

  public static void dohvatArhive(String corg, short god, short mj, short rbr, boolean chparam) {
    String filter = "GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
                    " and "+sjQuerys.getPripOrg(corg, "", "");
    String filter2 =  "GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
      " and CRADNIK in (select cradnik from radnicipl where "+sjQuerys.getPripOrg(corg, "", "")+")";
    
    QueryDataSet orgarh = Kumulorgarh.getDataModule().getFilteredDataSet(filter);
    QueryDataSet radarh = Kumulradarh.getDataModule().getFilteredDataSet(filter);
    QueryDataSet primarh = Primanjaarh.getDataModule().getFilteredDataSet(filter);
    QueryDataSet odbarh = Odbiciarh.getDataModule().getFilteredDataSet(filter2+" AND CKEY2!='$SYS'");
    QueryDataSet rsarh = RSPeriodarh.getDataModule().getFilteredDataSet(filter2);
    orgarh.open();
    radarh.open();
    primarh.open();
    odbarh.open();
    rsarh.open();
    //OJ - PARAMETRI - KUMULORG
    QueryDataSet orgpl = Orgpl.getDataModule().getFilteredDataSet(sjQuerys.getPripOrg(corg, "", ""));
    QueryDataSet param = Parametripl.getDataModule().copyDataSet();
    QueryDataSet kumulorg = Kumulorg.getDataModule().getFilteredDataSet(sjQuerys.getPripOrg(corg, "", ""));
    param.open();
    kumulorg.open();
    orgpl.open();

    String[] orgplcols = new VarStr("GODOBR, MJOBR, RBROBR,PARAMETRI,COPCINE,NACOBRS,NACOBRB,SATIMJ,OSNKOEF,SATNORMA,BROJDANA,STOPAK,DATUMISPL").splitTrimmed(",");
    String[] parametricols = new VarStr("MINPL,MINOSDOP,OSNPOR1,OSNPOR2,OSNPOR3,OSNPOR4,OSNPOR5").splitTrimmed(",");
    String[] kumulorgcols = new VarStr("CORG,CVRO,KNJIG,SATI,BRUTO,DOPRINOSI,NETO,NEOP,ISKNEOP,POROSN," +
            "POR1,POR2,POR3,POR4,POR5,PORUK,PRIR,PORIPRIR,NETO2,NAKNADE,NETOPK,KREDITI,NARUKE,DOPRPOD").splitTrimmed(",");
    
    for (orgpl.first(); orgpl.inBounds(); orgpl.next()) {
      if (lookupData.getlookupData().raLocate(orgarh, "CORG", orgpl.getString("CORG"))) {
        dM.copyColumns(orgarh, orgpl, orgplcols);
        orgpl.post();
      }
    }
    if (chparam) {
      dM.copyColumns(orgarh, param, parametricols);
      param.post();
    }
    kumulorg.deleteAllRows();
    for (orgarh.first(); orgarh.inBounds(); orgarh.next()) {
      kumulorg.insertRow(false);
      dM.copyColumns(orgarh, kumulorg, kumulorgcols);
      kumulorg.post();
    }
    
    //radnici - kumulrad
    QueryDataSet radnicipl = Radnicipl.getDataModule().getFilteredDataSet(sjQuerys.getPripOrg(corg, "", ""));
    radnicipl.open();
    QueryDataSet kumulrad = Kumulrad.getDataModule().getFilteredDataSet(Condition.in("CRADNIK", radnicipl));
    kumulrad.open();
    
    String[] kumulradcols = new VarStr("CRADNIK, SATI, BRUTO, "+
        "DOPRINOSI, NETO, NEOP, ISKNEOP, POROSN, POR1, POR2, POR3, POR4, POR5, PORUK, PRIR, "+
        "PORIPRIR, NETO2, NAKNADE, NETOPK, KREDITI, NARUKE").splitTrimmed(",");
    String[] radniciplcols = new VarStr("CRADMJ, CSS, CVRO, CISPLMJ, COPCINE, "+
        "RSINV, RSOO, BRUTOSN, BRUTDOD, BRUTMR, BRUTUK, GODSTAZ, STOPASTAZ, DATSTAZ, PODSTAZ, "+
        "DATPODSTAZ, NACOBRB, KOEF, KOEFZAR, OLUK, OLOS, CLANOMF, CORG, PARAMETRI").splitTrimmed(",");
    
    for (radnicipl.first(); radnicipl.inBounds(); radnicipl.next()) {
      if (lookupData.getlookupData().raLocate(radarh, "CRADNIK", radnicipl.getString("CRADNIK"))) {
        dM.copyColumns(radarh, radnicipl, radniciplcols);
        radnicipl.setString("AKTIV", "D");
        radnicipl.post();
      } else {
        radnicipl.setString("AKTIV", "N");
        radnicipl.post();        
      }
    }
    kumulrad.deleteAllRows();
    for (radarh.first(); radarh.inBounds(); radarh.next()) {
      kumulrad.insertRow(false);
      dM.copyColumns(radarh, kumulrad, kumulradcols);
    }
    
    
    //primanja
    QueryDataSet primanjaobr = Primanjaobr.getDataModule().getFilteredDataSet(Condition.in("CRADNIK", radarh));
    primanjaobr.open();
    
    String[] primanjaobrcols = new VarStr("CRADNIK, CVRP, RBR, CORG, SATI, "+
                "KOEF, BRUTO, DOPRINOSI, NETO").splitTrimmed(",");
    //ne da mi se vrsteprim dirat a mozda bi i trebalo
    
    primanjaobr.deleteAllRows();
    for (primarh.first(); primarh.inBounds(); primarh.next()) {
      primanjaobr.insertRow(false);
      dM.copyColumns(primarh, primanjaobr, primanjaobrcols);
      primanjaobr.post();
    }
    
    //odbici
    QueryDataSet odbiciobr = Odbiciobr.getDataModule().getFilteredDataSet(Condition.in("CRADNIK", radarh));
    QueryDataSet odbici = Odbici.getDataModule().getFilteredDataSet("CKEY != '$DEF' AND CKEY2 != '$DEF'");
    QueryDataSet vrsteodb = Vrsteodb.getDataModule().copyDataSet();
    
    odbiciobr.open();
    odbici.open();
    vrsteodb.open();
    
    String[] odbiciobrcols = new VarStr("cradnik, cvrp, rbr, cvrodb, ckey, ckey2, rbrodb, obrosn, obrstopa, obriznos, glavnica, saldo").splitTrimmed(",");
    String[] odbicicols = new VarStr("cvrodb, ckey, ckey2, rbrodb, pnb1, pnb2, iznos, stopa, datpoc, datzav, glavnica, rata, saldo, stavka").splitTrimmed(",");
    String[] vrsteodbcols = new VarStr("cvrodb, nivoodb, tipodb, vrstaosn, osnovica, cpov, iznos, stopa, parametri").splitTrimmed(",");
    
    odbiciobr.deleteAllRows();
    for (odbarh.first(); odbarh.inBounds(); odbarh.next()) {
      odbiciobr.insertRow(false);
      dM.copyColumns(odbarh, odbiciobr, odbiciobrcols);
      odbiciobr.post();
      if (!lookupData.getlookupData().raLocate(odbici, new VarStr("CVRODB,CKEY,CKEY2,RBRODB").splitTrimmed(","), 
          new VarStr(odbarh.getShort("CVRODB")+","+odbarh.getString("CKEY")+","+odbarh.getString("CKEY2")+","+odbarh.getShort("RBRODB")).splitTrimmed(",")
          )) {
        odbici.insertRow(false);
      }
      dM.copyColumns(odbarh, odbici, odbicicols);
      odbici.setString("AKTIV", "D");
      odbici.post();
      //jooj valjda nisu mijenjane vrste odbitaka
    }
    QueryDataSet vrsteodbRA = Vrsteodb.getDataModule().getFilteredDataSet(Condition.equal("nivoodb", "RA"));
    vrsteodbRA.open();
    QueryDataSet odbiciRA = Odbici.getDataModule().getFilteredDataSet(Condition.in("CVRODB", vrsteodbRA).and(Condition.in("CKEY", radarh,"CRADNIK")));
    odbiciRA.open();
    
    for (odbiciRA.first(); odbiciRA.inBounds(); odbiciRA.next()) {
      if (!lookupData.getlookupData().raLocate(odbarh, new VarStr("CVRODB,CKEY,RBRODB").splitTrimmed(","), 
          new VarStr(odbiciRA.getShort("CVRODB")+","+odbiciRA.getString("CKEY")+","+odbiciRA.getShort("RBRODB")).splitTrimmed(",")
          )) {
        odbiciRA.setString("AKTIV", "N");//program bas i ne gleda taj flag, ali budemo poslije obrisali
        odbiciRA.post();
      }
    }
    
    //rsperiod 
    
    QueryDataSet rs = RSPeriod.getDataModule().getFilteredDataSet(Condition.in("CRADNIK", radarh));
    rs.open();
    QueryDataSet rso = RSPeriodobr.getDataModule().getFilteredDataSet(Condition.in("CRADNIK", radarh));
    rso.open();
    rs.deleteAllRows();
    rso.deleteAllRows();
    for (rsarh.first(); rsarh.inBounds(); rsarh.next()) {
      rs.insertRow(false);
      dM.copyColumns(rsarh, rs, new VarStr("cradnik, rbr, rsoo, oddana, dodana, copcine").splitTrimmed(","));
      rs.post();
      
      rso.insertRow(false);
      dM.copyColumns(rsarh, rso, new VarStr( 
      "cradnik, rbr, rsoo, oddana, dodana, jmbg, copcine, rsinv, rsb, rsz, sati, bruto, brutomj, mio1, mio1mj, mio2, mio2mj, zo, zomj, zapos, zaposmj, premos, osodb, porez, porezmj, prirez, prirezmj, netopk, mjesec, godina, identifikator, vrstaupl, vrobv"
          ).splitTrimmed(","));
      rso.post();
    }
    
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {
        param,
        kumulorg,
        orgpl,
        radnicipl,
        kumulrad,
        primanjaobr,
        odbiciobr,
        odbici,
        rs
    });
  }
}