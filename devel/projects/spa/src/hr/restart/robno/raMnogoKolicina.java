/****license*****************************************************************
**   file: raMnogoKolicina.java
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
import hr.restart.swing.JraTextField;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raMnogoKolicina {
  StorageDataSet tmpSet = new StorageDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JLabel jlb  = new JLabel("Pakiranje");
  JraTextField jtfPakiranjeKol = new JraTextField();
  JraTextField jtfPakiranjeCijena = new JraTextField();
  JLabel jlbKoleta  = new JLabel("Kolete");
  JraTextField jtfKoleteKol = new JraTextField();
  JraTextField jtfKoleteCijena = new JraTextField();
  JPanel jpl = new JPanel();


  public void init(){
    tmpSet.close();
    Column kol = dm.getStdoki().getColumn("KOL").cloneColumn();
    kol.setColumnName("KOL");

    Column kolColeta = dm.getStdoki().getColumn("KOL").cloneColumn();
    kolColeta.setColumnName("KOL_COLETA");

    Column kolPakiranje = dm.getStdoki().getColumn("KOL").cloneColumn();
    kolPakiranje.setColumnName("KOL_PAKIRANJE");

    tmpSet.setColumns(new Column[] {kol,kolColeta,kolPakiranje});
    tmpSet.open();
  }

  public raMnogoKolicina() {
    init();
  }

  public void setForDataSet(int cart){
    tmpSet.clearValues();
    QueryDataSet qsd = hr.restart.util.Util.getNewQueryDataSet("SELECT * from ARTIKLI WHERE cart="+cart);
    if (qsd.getRowCount()!=0){
//      tmpSet

    }
  }

  public void makeDisplay(){

    jpl.setLayout(new XYLayout());
//    (XYLayout) jpl.getLayout();
    jpl.add(jlb,new XYConstraints(15,10,100,-1));






  }


}