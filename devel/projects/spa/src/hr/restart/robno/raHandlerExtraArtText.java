/****license*****************************************************************
**   file: raHandlerExtraArtText.java
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

import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raHandlerExtraArtText extends raFrame{

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JLabel lcart = new JLabel("Opis / specifikacija artikla");
  hr.restart.swing.JraTextArea opis = new hr.restart.swing.JraTextArea();
  OKpanel okp = new OKpanel(){
    public void jPrekid_actionPerformed() {
      myPrekid();
    }
    public void jBOK_actionPerformed() {
      myOK();
    }
  };
  JPanel main = new JPanel();

  public raHandlerExtraArtText() {
  }

  private void jbInit() throws Exception {
    opis.setDataSet(dm.getVTText());
    opis.setColumnName("TEXTFAK");
    this.getContentPane().setLayout(new BorderLayout());
    main.setLayout(new XYLayout());
    main.add(lcart,new XYConstraints(15,10,100,-1));
    main.add(opis,new XYConstraints(125,10,200,70));
    this.getContentPane().add(main,BorderLayout.CENTER);
    this.getContentPane().add(okp,BorderLayout.NORTH);
  }

  public void findVTText(DataSet ds) throws Exception {
    if (ds.getTableName().equalsIgnoreCase("ARTIKLI")) {

    } else if (ds.getTableName().equalsIgnoreCase("STDOKU") ||
               ds.getTableName().equalsIgnoreCase("STMESKLA") ||
               ds.getTableName().equalsIgnoreCase("STDOKI")) {

    } else {
      throw new Exception("Za tabelu "+ds.getTableName()+ "ne mogu naci kljuc za dodatni text");

    }
  }

  public void myPrekid(){}
  public void myOK(){}
}