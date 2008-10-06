/****license*****************************************************************
**   file: frmPregledRacunaTbl.java
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
package hr.restart.robno;

import hr.restart.util.raUpitLite;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYLayout;


public class frmPregledRacunaTbl extends raUpitLite {
  private JPanel jp = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  boolean nemaPodataka;
  QueryDataSet qds, qds2;

  static frmPregledRacunaTbl prt;

  public frmPregledRacunaTbl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    prt = this;
  }

  public static frmPregledRacunaTbl getInstance(){
    return prt;
  }

  public void componentShow() {}

  public void okPress() {
    nemaPodataka = false;
    // ovo ovde su podaci bez veze za potrebe testiranja...
    String qs = "select max (stdoki.cskl) as cskl, sum (stdoki.uirab) as racun, "+
                "sum (stdoki.iprodbp) as virman, sum (stdoki.iprodsp) as cek, "+
                "sum (stdoki.ineto) as kredkart, min(doki.datdok) as datod, "+
                "max(doki.datdok) as datdo from stdoki, doki "+
                "WHERE stdoki.cskl = doki.cskl AND stdoki.vrdok = doki.vrdok "+
                "AND stdoki.god = doki.god AND stdoki.brdok = doki.brdok "+
                "and stdoki.vrdok='GOT' "+
                "group by  cskl";

    qds = hr.restart.util.Util.getNewQueryDataSet(qs);

    String qs2 = "SELECT max(cskl) as cskl, sum(nc) as STZALNC, sum(iprodbp) as UPVNOPDV, "+
                 "sum (iprodsp) as UNABVRI, sum(ineto) as PROVKART, sum(inab) as RUC, "+
                 "sum(imar) as PRUC, sum(ipor) as UKUTR  FROM stdoki WHERE vrdok='GOT' "+
                 "group by cskl";

    qds2 = hr.restart.util.Util.getNewQueryDataSet(qs2);
  }

  public void afterOKPress(){
    if (nemaPodataka){
      firstESC();
      JOptionPane.showConfirmDialog(this.jp,
                                    "Nema podataka koji zadovoljavaju traženi uvjet !",
                                    "Upozorenje",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
    }
//    else {}
  }

  public DataSet getQds(){
    return qds;
  }

  public DataSet getQds2(){
    return qds2;
  }

  public void firstESC() {

  }

  public boolean runFirstESC() {
    return false;
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repPregledRacunaTbl",
                   "hr.restart.robno.repPregledRacunaTbl",
                   "PregledRacunaTbl",
                   "TESTING 1");

    this.addReport("hr.restart.robno.repMjeIzvjMaloprodaja",
                   "hr.restart.robno.repMjeIzvjMaloprodaja",
                   "MjeIzvjMaloprodaja",
                   "TESTING 2");

    xYLayout1.setWidth(555);
    xYLayout1.setHeight(250);
    jp.setLayout(xYLayout1);
    this.setJPan(jp);
  }

}