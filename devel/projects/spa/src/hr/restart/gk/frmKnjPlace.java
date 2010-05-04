/****license*****************************************************************
**   file: frmKnjPlace.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Kumulorgarh;
import hr.restart.baza.Vrsteprim;
import hr.restart.baza.dM;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraTextField;
import hr.restart.util.MathEvaluator;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKnjPlace extends frmKnjizenje {
  private JPanel period = new JPanel();
  JraTextField jtfGOD = new JraTextField();
  JraTextField jtfMJ = new JraTextField();
  JraTextField jtfRBR = new JraTextField();
  private JLabel jl1 = new JLabel("Obrada za God/Mj/Rbr");
  StorageDataSet viewSet = new StorageDataSet();
  public frmKnjPlace() {
    initUI();
  }

  private void initUI() {
    viewSet.addColumn(dM.createShortColumn("GODOBR"));
    viewSet.addColumn(dM.createShortColumn("MJOBR"));
    viewSet.addColumn(dM.createShortColumn("RBROBR"));
    
    jtfGOD.setColumnName("GODOBR");
    jtfGOD.setDataSet(viewSet);
    jtfMJ.setColumnName("MJOBR");
    jtfMJ.setDataSet(viewSet);
    jtfRBR.setColumnName("RBROBR");
    jtfRBR.setDataSet(viewSet);

    XYLayout lay = new XYLayout();
    period.setLayout(lay);
    period.add(jl1,   new XYConstraints(15, 0, -1, -1));
    period.add(jtfGOD,   new XYConstraints(150, 0, 55, -1));
    period.add(jtfMJ,   new XYConstraints(210, 0, 40, -1));
    period.add(jtfRBR,  new XYConstraints(255, 0, 40, -1));

    jp.add(period, BorderLayout.CENTER);
  }
  public boolean Validacija() {
    Valid vl = Valid.getValid();
    if (vl.isEmpty(jtfGOD)) return false;
    if (vl.isEmpty(jtfMJ)) return false;
    if (vl.isEmpty(jtfRBR)) return false;
    return true;
  }
  
  public boolean okPress() throws Exception {
    QueryDataSet shk_PL = Asql.getShkonta("pl","PL","PL");
    shk_PL.open();

    QueryDataSet shk_PR = Asql.getShkonta("pl","1","PL");
    shk_PR.open();
    HashMap ins_PR = new HashMap();
    for (shk_PR.first(); shk_PR.inBounds(); shk_PR.next()) {
      QueryDataSet vrp = Vrsteprim.getDataModule().getTempSet(Condition.equal("STAVKA",shk_PR.getShort("STAVKA")+""));
      vrp.open();
      if (vrp.getRowCount()>0) {
        String in = "(";
        for (vrp.first(); vrp.inBounds(); vrp.next()) {
          in = in + vrp.getShort("CVRP")+",";
        }
        in = in.substring(0, in.length()-1)+")";
        DataRow row = new DataRow(shk_PR);
        shk_PR.copyTo(row);
        ins_PR.put(row, in);
      }
    }
    
    String cnd = Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR"}, viewSet)
      +" AND (corg in "+OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig())+")";
//System.out.println(cnd);
    StorageDataSet kumorg = Kumulorgarh.getDataModule().getTempSet(cnd);
    kumorg.open();
    if (kumorg.getRowCount() == 0) {
      getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
      return false;
    }

    if (!getKnjizenje().startKnjizenje(this)) return false;
    
    for (kumorg.first(); kumorg.inBounds(); kumorg.next()) {
      for (shk_PL.first(); shk_PL.inBounds(); shk_PL.next()) {
        //orgs:
        if (shk_PL.getString("POLJE").indexOf(":") < 0) {//nema ':', dakle nije iz druge tablice
          BigDecimal value = MathEvaluator.getEvaluatedBigDecimal(shk_PL.getString("POLJE"), kumorg);
          if (!addToNewStavka(value, shk_PL, kumorg)) return false;
        } else {
          StringTokenizer tok = new StringTokenizer(shk_PL.getString("POLJE"),":");
          String tab = tok.nextToken();
          String key = tok.nextToken();
          if (tab.equalsIgnoreCase("ODB") || tab.toUpperCase().indexOf("ODB") >= 0) {
//            String qry = "SELECT cradnik, obriznos from odbiciarh WHERE cvrodb = "+key+
//              " AND "+Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR"}, viewSet)+
//              " AND EXISTS (SELECT cradnik FROM radnici where radnici.cradnik = odbiciarh.cradnik " +
//              " AND radnici.corg='"+kumorg.getString("CORG")+"')";
            //MORAJU BITI RADNICIPL WHERE GOD, MJ, RBR ZBOG PROMJENA O.J. 
            //BAGA JE I NA REKAPITULACIJI IZ ARHIVE
            String qry = "SELECT cradnik, obriznos from odbiciarh WHERE cvrodb = "+key+
            " AND "+Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR"}, viewSet)+
            " AND EXISTS (SELECT cradnik FROM kumulradarh where kumulradarh.cradnik = odbiciarh.cradnik " +
            " AND kumulradarh.corg='"+kumorg.getString("CORG")+"' AND "+
            "kumulradarh.cvro='"+kumorg.getString("CVRO")+"' AND "+
            Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR"}, viewSet)
            +")";
            
//System.out.println(qry);
            QueryDataSet odb = Util.getNewQueryDataSet(qry, true);
            BigDecimal sumodb = new BigDecimal(0);
            for (odb.first(); odb.inBounds(); odb.next()) {
              sumodb = sumodb.add(odb.getBigDecimal("OBRIZNOS"));
            }
            BigDecimal value;
            if (tab.equalsIgnoreCase("ODB")) {
              value = sumodb;
            } else {
              String mpolje = new VarStr(shk_PL.getString("POLJE")).replace("ODB:"+key, sumodb.toString()).toString();
              value = MathEvaluator.getEvaluatedBigDecimal(mpolje, kumorg);
            }
            if (!addToNewStavka(value, shk_PL, kumorg)) return false;
          }
        }
      }
      //primanja
      for (Iterator iter = ins_PR.keySet().iterator(); iter.hasNext();) {
        DataRow row = (DataRow) iter.next();
        //npr. NETO:NETOPK
        StringTokenizer polja = new StringTokenizer(row.getString("POLJE"), ":");
        String cl = polja.nextToken();
        String clsum = polja.nextToken();
        String qry = "SELECT cradnik, "+cl+" FROM primanjaarh where cvrp in "+(String)ins_PR.get(row)+
          " AND "+Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR"}, viewSet)+
          " AND "+Condition.equal("CORG", kumorg);
//System.out.println(qry);
        QueryDataSet prim = Util.getNewQueryDataSet(qry, true);
        if (prim.getRowCount() > 0) {
          BigDecimal sumprim = new BigDecimal(0);
          for (prim.first(); prim.inBounds(); prim.next()) {
            sumprim = sumprim.add(prim.getBigDecimal(cl));
          }
          if (!addToNewStavka(sumprim, row, kumorg)) return false;
          //oduzmi od ukupno konta sa te sheme
          boolean loc = lookupData.getlookupData().raLocate(shk_PL, "POLJE", clsum);
          System.out.println("LOCATED :: "+loc);
          System.out.println(shk_PL);
          System.out.println(clsum);
          if (loc) {
            if (!addToNewStavka(sumprim.negate(), shk_PL, kumorg)) return false;
          }
        }
      }
    }
    
    if (!getKnjizenje().saveAll()) return false;
    return true;
  }

  private boolean addToNewStavka(BigDecimal value, ReadRow shk, ReadRow kumorg) {
    if (value.signum()!=0) {
      String corg = OrgStr.getKNJCORG();
      if (raKonta.isOrgStr(shk.getString("BROJKONTA"))) { 
        corg = kumorg.getString("CORG");
      }
      StorageDataSet stavka = getKnjizenje().getNewStavka(shk.getString("BROJKONTA"), corg);
      if (shk.getString("KARAKTERISTIKA").equalsIgnoreCase("D")) {
        getKnjizenje().setID(value);
      } else {
        getKnjizenje().setIP(value);
      }
      stavka.setString("OPIS","Plaæa za "+kumorg.getShort("MJOBR")+"/"+kumorg.getShort("GODOBR")+" rbr."+kumorg.getShort("RBROBR"));
      stavka.setTimestamp("DATDOK",kumorg.getTimestamp("DATUMISPL"));
      if (!getKnjizenje().saveStavka()) return false;
    }
    return true;
  }
}