/****license*****************************************************************
**   file: raInventurnaLista.java
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

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;

public class raInventurnaLista extends raPopListaInv {

  static private QueryDataSet repSet = new QueryDataSet();
  static public String sortBy;

  public raInventurnaLista() {
    ril = this;
  }

  static raInventurnaLista ril;

  public static raInventurnaLista getInstanceOf(){
    if (ril == null){
      ril = new raInventurnaLista();
    }
    return ril;
  }
  
  public void componentShow(){
    setCsklFields();
  }

  public void setCsklFields() {
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setDataSet(fieldSet);
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(sgQuerys.getSgQuerys().getSkladistaUInventuri(hr.restart.zapod.OrgStr.getKNJCORG()));
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCskl.setRaDataSet(sgQuerys.getSgQuerys().getSkladistaUInventuri(hr.restart.zapod.OrgStr.getKNJCORG()));
      }
    });
  }

  public void setPanels() {
    xYLayout1.setWidth(595);
    xYLayout1.setHeight(120);

    mainPanel.removeAll();
    mainPanel.add(jlaCskl, new XYConstraints(151, 13, 98, -1));
    mainPanel.add(jlaNazskl, new XYConstraints(256, 13, 293, -1));
    mainPanel.add(jbSelCskl, new XYConstraints(555, 30, 21, 21));
    mainPanel.add(jlCskl, new XYConstraints(15, 30, -1, -1));
    mainPanel.add(jlrCskl, new XYConstraints(150, 30, 100, -1));
    mainPanel.add(jlrNazskl, new XYConstraints(255, 30, 295, -1));
    mainPanel.add(jlSljed, new XYConstraints(15, 65, -1, -1));
    mainPanel.add(jcbIspisStanjeNula,       new XYConstraints(291, 95, 259, 19));

    mainPanel.add(panel01, new XYConstraints(150, 55, 400, -1));

    panel01.add(jrbSortiranjePoCART1, new XYConstraints(15, 0, -1, -1));
    panel01.add(jrbSortiranjePoNAZART, new XYConstraints(215, 0, -1, -1));
  }

  public void setReportProviders(){
    this.addReport("hr.restart.robno.repInventurnaLista","hr.restart.robno.repInventurnaLista","InventurnaLista","Inventurna lista");
//    this.addReport("hr.restart.robno.repInventurnaLista", "Inventurna lista", 2);
  }

  public void makeRepSet(){
    if (jrbSortiranjePoNAZART.isSelected()) sortBy = "NAZART";
    else sortBy = hr.restart.sisfun.frmParam.getParam("robno","indiCart");

    repSet.close();
     
    if (jcbIspisStanjeNula.isSelected()){
    
        repSet.setQuery(new QueryDescriptor(dm.getDatabase1(),ss.getInventuraInventurnaLista(fieldSet.getString("CSKL"), sortBy)));
    } else {
        repSet.setQuery(new QueryDescriptor(dm.getDatabase1(),ss.getInventuraInventurnaListabezNula(fieldSet.getString("CSKL"), sortBy)));    
    }

    repSet.open();

//    repSet = ss.getInventuraInventurnaLista(fieldSet.getString("CSKL"));
  }

  public QueryDataSet getRepSet(){
    return repSet;
  }

  public String getSORTER(){
    return sortBy;
  }

}