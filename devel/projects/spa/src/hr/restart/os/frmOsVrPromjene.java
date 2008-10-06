/****license*****************************************************************
**   file: frmOsVrPromjene.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.robno._Main;
import hr.restart.util.raComboBox;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmOsVrPromjene extends raSifraNaziv
{
  dM dm;
  _Main main;
  JPanel jp = new JPanel();
  raComboBox jcbTipPr = new raComboBox();
  XYLayout xYLayout1= new XYLayout();
  JLabel jLabel1 = new JLabel();

  public frmOsVrPromjene()
  {
    try
    {
      jbInit();
    }catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    dm = dM.getDataModule();

    jp.setLayout(xYLayout1);
    xYLayout1.setWidth(585);
    xYLayout1.setHeight(35);
    jLabel1.setText("Tip");
    jp.add(jcbTipPr,    new XYConstraints(150, 0, 200, -1));
    jp.add(jLabel1,   new XYConstraints(15, 2, -1, -1));
    this.setVisibleCols(new int[] {0,1,2});

    this.setRaDataSet(dm.getOS_Vrpromjene());
    this.setRaColumnSifra("CPROMJENE");
    this.setRaColumnNaziv("NAZPROMJENE");
    this.setRaText("Vrsta");

    jcbTipPr.setRaDataSet(dm.getOS_Vrpromjene());
    jcbTipPr.setRaColumn("TIPPROMJENE");
    jcbTipPr.setRaItems(new String[][] {
       {"Nabava","N"},
       {"Likvidacija","L"},
       {"Obraèun amortizacije","A"},
       {"Obraèun revalorizacije","R"},
       {"Promjena org. jedinice","O"},
       {"Iz pripreme u upotrebu","U"}
   });
   this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);
  }

  public boolean Validacija(char mode)
  {
    if(dm.getOS_Vrpromjene().getString("TIPPROMJENE").trim().equals(""))
      dm.getOS_Vrpromjene().setString("TIPPROMJENE", "N");
    return true;
  }

}