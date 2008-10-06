/****license*****************************************************************
**   file: jpBrdokul.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;

import javax.swing.JPanel;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpBrdokul extends JPanel {

  private XYLayout xylayout = new XYLayout();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private JlrNavField jlrBRDOK = new JlrNavField(){
    public void after_lookUp(){
      if (isDodatak) mafter_lookUp();
    }
  };
  public void mafter_lookUp(){

    if(jlrBRDOK.getDataSet() != null && neprPRK != null){
      jlrBRDOK.getDataSet().setTimestamp("DATDOKUL",neprPRK.getTimestamp("DATDOK"));
    }
  }
  private JraButton jbBRdok = new JraButton();
  public JraTextField jtfBRDOK  = new JraTextField();
  private QueryDataSet neprPRK = new QueryDataSet();
  private int version = 0;
  private boolean isDodatak = false;

  public jpBrdokul(int ver) {
    version=ver;
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    this.setLayout(xylayout);
    if (version == 0) {
      jtfBRDOK  = new JraTextField();
      jtfBRDOK.setColumnName("BRDOKUL");
      this.add(jtfBRDOK,new XYConstraints(0,0,200,-1));
    }
    else if (version == 1) {
      jbBRdok.setText("...");
      jlrBRDOK.setColumnName("BRDOKUL");
      jlrBRDOK.setNavColumnName("BRDOK");
      jlrBRDOK.setVisCols(new int[]{0,1,2});
      jlrBRDOK.setNavButton(jbBRdok);
      xylayout.setWidth(250);
      this.add(jlrBRDOK,new XYConstraints(0,0,200,-1));
      this.add(jbBRdok,new XYConstraints(205,0,21,21));
    }
  }

  public void setDataSet(QueryDataSet ds){
    if (version==0){
      jtfBRDOK.setDataSet(ds);
    }
    else if (version==1){
      jlrBRDOK.setDataSet(ds);
    }
  }

  public void preparePRK(String cskl,int cpar){
    String sqlstr="SELECT * FROM Doku where cpar='"+cpar+"' and vrdok='PRI' and status='N' and cskl='"+cskl+"'";
    neprPRK = hr.restart.util.Util.getNewQueryDataSet(sqlstr,true);
    jlrBRDOK.setRaDataSet(neprPRK);
//    ST.prn(neprPRK);
  }

  public QueryDataSet getZaglavPRI(){

    return neprPRK;
  }
  public void setIsDodatak(boolean isDodatak) {
    this.isDodatak = isDodatak;
  }
  public int getVersion() {
    return version;
  }
  public boolean Validacija() {
   if (hr.restart.util.Valid.getValid().isEmpty(jlrBRDOK)) return false;
   return true;
  }

}
