/****license*****************************************************************
**   file: jpRnser.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.dM;
import hr.restart.baza.raDataSet;
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRnser extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmRnser frn;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlKOL = new JLabel();
  JraTextField jraKOL = new JraTextField() {
	  public void valueChanged() {
	    frn.calc();
	  }
	};
	JLabel jlZC = new JLabel();
  JraTextField jraZC = new JraTextField() {
	  public void valueChanged() {
	  	frn.calc();
	  }
	};
	JLabel jlVRI = new JLabel();
	JraTextField jraVRI = new JraTextField() {
		public boolean isFocusTraversable() {
			return false;
		}
	};

  protected rapancart rpc = new rapancart() {
  	QueryDataSet arts;
    public QueryDataSet getRaDataSet(){
    	if (arts == null) arts = Artikli.getDataModule().getFilteredDataSet(
          raVart.getStanjeCond().not());
      return arts;
    }
    public void metToDo_after_lookUp() {
      if (!rpcLostFocus && frn.getMode() == 'N') {
        rpcLostFocus = true;
        frn.rpcOut();
      };
    }
  };

  protected boolean rpcLostFocus;

  public jpRnser(frmRnser md) {
    try {
      frn = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EraseFields() {
    rpcLostFocus = false;
    rcc.EnabDisabAll(jpDetail, false);
  }

  public void EnableFields() {
    rcc.EnabDisabAll(jpDetail, true);
  }

  public void BindComponents(DataSet ds) {
    jraKOL.setDataSet(ds);
    jraZC.setDataSet(ds);
    jraVRI.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    jpDetail.setLayout(lay);
    lay.setWidth(560);
    lay.setHeight(65);

    jlKOL.setText("Kolièina");
    jraKOL.setColumnName("KOL");
    
    jlZC.setText("Cijena");
    jlZC.setHorizontalAlignment(SwingConstants.CENTER);
    jraZC.setColumnName("ZC");
    
    jlVRI.setText("Vrijednost");
    jlVRI.setHorizontalAlignment(SwingConstants.CENTER);
    jraVRI.setColumnName("VRI");
    rcc.setLabelLaF(jraVRI, false);

    jpDetail.add(jlKOL, new XYConstraints(15, 25, -1, -1));
    jpDetail.add(jraKOL, new XYConstraints(150, 25, 100, -1));
    jpDetail.add(jlZC, new XYConstraints(355, 5, 100, -1));
    jpDetail.add(jraZC, new XYConstraints(355, 25, 100, -1));
    jpDetail.add(jlVRI, new XYConstraints(460, 5, 100, -1));
    jpDetail.add(jraVRI, new XYConstraints(460, 25, 100, -1));

    BindComponents(frn.getRaQueryDataSet());
//    initRpcart();
    jpDetail.setBorder(BorderFactory.createEtchedBorder());
    this.add(rpc, BorderLayout.NORTH);
    this.add(jpDetail, BorderLayout.CENTER);
  }

  public void initRpcart() {
    rpc.setTabela(frn.getRaQueryDataSet());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setMode("DOH");
    rpc.setDefParam();
    rpc.setFocusCycleRoot(true);
    rpc.setAllowUsluga(false);
    rpc.InitRaPanCart();
  }
}
