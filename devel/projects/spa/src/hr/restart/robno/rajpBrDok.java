/****license*****************************************************************
**   file: rajpBrDok.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class rajpBrDok extends JPanel implements com.borland.dx.dataset.DataSetAware {

  XYLayout xYLayout = new XYLayout();
  JLabel jlBRDOK = new JLabel();
  JLabel jlDOK = new JLabel();
  JLabel jlStatusDok = new JLabel();

  JraButton jbSTATUS = new JraButton();
  raStatus raStatus = new raStatus();
  com.borland.dx.dataset.DataSet ds ;
  JLabel jLabel1 = new JLabel();

  public rajpBrDok() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {


    jbSTATUS.setText("...");
    jbSTATUS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSTATUS_actionPerformed(e);
      }
    });

    this.setPreferredSize(new Dimension(534, 40));
    this.setLayout(xYLayout);
    jlDOK.setForeground(Color.red);
    jlDOK.setFont(new java.awt.Font("Dialog", 1, 14));
    jlBRDOK.setText("Broj dokumenta");
    jLabel1.setText("Status"); // Statusi
    jlStatusDok.setFont(new java.awt.Font("Dialog", 1, 14));
    this.add(jlBRDOK, new XYConstraints(16, 10, -1, -1));
    this.add(jlDOK, new XYConstraints(150, 10, -1, -1));
    this.add(jLabel1, new XYConstraints(400, 12, -1, -1));
    this.add(jlStatusDok, new XYConstraints(505, 10, -1, -1));
//    this.add(jbSTATUS, new XYConstraints(611, 10, 21, 21));
//    this.add(jLabel1, new XYConstraints(560, 12, -1, -1));

  }
  public void addBorder(){
      this.setBorder(BorderFactory.createEtchedBorder());
  }
  public void SetTextDOK(String tekst){
    jlDOK.setText(tekst);
  }
  public void SetDefTextDOK(char mode){
    if (mode=='N') {
      jlDOK.setText(ds.getString("VRDOK")+" - "+ds.getString("CSKL").trim()+" / "+
        hr.restart.util.Valid.getValid().findYear(ds.getTimestamp("DATDOK")));
    }
    else {
      SetTextDOK(ds.getString("VRDOK")+" - "+ds.getString("CSKL").trim()+" / "+
          ds.getString("GOD")+" - "+ds.getInt("BRDOK"));
    }
    SetStatus();
  }
  public void SetStatus(){
    String poruka = "Spreman";
    Color boja= Color.green.darker();
    if (ds.getString("STATUS").equalsIgnoreCase("P")) {
      poruka = "Prenesen";
    }
    TypeDoc TD = TypeDoc.getTypeDoc();
    if (ds.hasColumn("STATKNJ")==null) {
      if (ds.hasColumn("STATUS") != null) {
        boja= Color.green.darker();
        if (ds.getString("STATUS").equalsIgnoreCase("P"))
          poruka = "Prijavljen";
        else if (ds.getString("STATUS").equalsIgnoreCase("O"))
          poruka = "Obraðen";
        else if (ds.getString("STATUS").equalsIgnoreCase("F"))
          poruka = "Fakturiran";
        else if (ds.getString("STATUS").equalsIgnoreCase("Z")) {
          poruka = "Zatvoren";
          boja = Color.black;
        }
        jlStatusDok.setText(poruka);
        jlStatusDok.setForeground(boja);
      }
      return; //za tabèice koje nemaju tako lijepo razradjene statuse npr. rn
    }
    if (!TD.isDocMeskla(ds.getString("VRDOK"))) {
      if (!ds.getString("STATKNJ").equalsIgnoreCase("N")) {
        boja = Color.red;
        if (ds.getString("STATKNJ").equalsIgnoreCase("K")) {
          poruka = "Knjižen";
        } else if (ds.getString("STATKNJ").equalsIgnoreCase("P")) {
          poruka = "Privremeno knjižen";
        }
      }
    } else {
      if (ds.getString("VRDOK").equalsIgnoreCase("MES")) {
        boolean isFull = (
            ds.getString("STATKNJI").equalsIgnoreCase("K") &&
            ds.getString("STATKNJU").equalsIgnoreCase("K"));

        boolean isPFull = (
            ds.getString("STATKNJI").equalsIgnoreCase("P") &&
            ds.getString("STATKNJU").equalsIgnoreCase("P"));

        if (!ds.getString("STATKNJI").equalsIgnoreCase("N")) {
          boja = Color.red;
          if (ds.getString("STATKNJI").equalsIgnoreCase("K")) {
            poruka = isFull?"Knjižen":"Djelomièno knjižen";
          } else if (ds.getString("STATKNJI").equalsIgnoreCase("P")) {
            poruka = isPFull?"Privremeno knjižen":"Privremeno djelomièno knjižen";
          }
        }
        if (!ds.getString("STATKNJU").equalsIgnoreCase("N")) {
          boja = Color.red;
          if (ds.getString("STATKNJU").equalsIgnoreCase("K")) {
            poruka = isFull?"Knjižen":"Djelomièno knjižen";
          } else if (ds.getString("STATKNJU").equalsIgnoreCase("P")) {
            poruka = isPFull?"Privremeno knjižen":"Privremeno djelomièno knjižen";
          }
        }
      } else if (ds.getString("VRDOK").equalsIgnoreCase("MEI")) {
        if (!ds.getString("STATKNJI").equalsIgnoreCase("N")) {
          boja = Color.red;
          if (ds.getString("STATKNJI").equalsIgnoreCase("K")) {
            poruka = "Knjižen";
          } else if (ds.getString("STATKNJI").equalsIgnoreCase("P")) {
            poruka = "Privremeno knjižen";
          }
        }
      }
      else if (ds.getString("VRDOK").equalsIgnoreCase("MEU")) {
        if (!ds.getString("STATKNJU").equalsIgnoreCase("N")) {
          boja = Color.red;
          if (ds.getString("STATKNJU").equalsIgnoreCase("K")) {
            poruka = "Knjižen";
          } else if (ds.getString("STATKNJU").equalsIgnoreCase("P")) {
            poruka = "Privremeno knjižen";
          }
        }
      }
    }

    jlStatusDok.setText(poruka);
    jlStatusDok.setForeground(boja);

//    jlStatusDok.setText(ds.getString("STATUS").equalsIgnoreCase("K")?"Knjižen":"Spreman");
//    jlStatusDok.setForeground(ds.getString("STATUS").equalsIgnoreCase("K")?Color.red:Color.green.darker());
  }

  public void setDataSet(com.borland.dx.dataset.DataSet ds){
    this.ds=ds;
  }
  public com.borland.dx.dataset.DataSet getDataSet() {
    return ds;
  }
  void jbSTATUS_actionPerformed(ActionEvent e) {
    raStatus.go(ds,500,100);
  }
}