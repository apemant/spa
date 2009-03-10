/****license*****************************************************************
**   file: jpNarDobDetail.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpNarDobDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  String oldValue;

  frmNarDob fNarDob;
  JPanel jpDetail = new JPanel();
  JPanel jpSklRpcDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlaInab = new JLabel();
  JLabel jlaKol = new JLabel();
  JLabel jlaNc = new JLabel();
  
  JraTextField jraKol = new JraTextField() {
    public void valueChanged() {
      if (!oldValue.equals(jraKol.getText().toString())) {
          frmNarDob.getInstance().afterKOL();
      }
    }
  };
  JraTextField jraNc = new JraTextField() {
    public void valueChanged() {
      if (!oldValue.equals(jraNc.getText().toString())) {
          frmNarDob.getInstance().afterNC();
      }
    }
  };
  JraTextField jraInab = new JraTextField();
  JraTextField jraNcDOB = new JraTextField() {
    public void valueChanged() {
      if (!oldValue.equals(jraNcDOB.getText().toString())) {
            frmNarDob.getInstance().afterNCDOB();
        }
    }
  };
  JraTextField jraInabDOB = new JraTextField();

  JraTextField jraPop = new JraTextField() {
    public void valueChanged() {
      if (!oldValue.equals(jraPop.getText().toString())) {
            frmNarDob.getInstance().afterPop();
        }
    }
  };
  JraTextField jraVc = new JraTextField() {
    public void valueChanged() {
      if (!oldValue.equals(jraVc.getText().toString())) {
            frmNarDob.getInstance().afterVc();
        }
    }
  };
  JraTextField jraIvc = new JraTextField() {
    public void valueChanged() {
      if (!oldValue.equals(jraIvc.getText().toString())) {
            frmNarDob.getInstance().afterIvc();
        }
    }
  };

/**
 * @param fdi The fDI to set.
 */
  rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
      fNarDob.updateTxt();
      if (!rpcLostFocus && frmNarDob.getInstance().raDetail.getMode() == 'N') {
        rpcLostFocus = true;
        frmNarDob.getInstance().afterArt();
      }
    }
    public void jbDODTXT_actionPerformed(ActionEvent e){
    	myDodText();
    }
  };
/*
  JlrNavField jrfCSKL = new JlrNavField() {
    public void after_lookUp() {
      if (!jrfCSKL.getText().trim().equals("")) {
        disabCSKL();

        rpc.setGodina(hr.restart.util.Valid.getValid().findYear(
    	    frmNarDob.getInstance().getMasterSet().getTimestamp("DATDOK")));
    	rpc.setCskl(jrfCSKL.getText().trim());
    	rpc.jrfCART.requestFocus();
      }
    }
  };
  JlrNavField jrfNAZSKL = new JlrNavField() {
    public void after_lookUp() {
      if (!jrfCSKL.getText().trim().equals("")) {
        disabCSKL();

        rpc.setGodina(hr.restart.util.Valid.getValid().findYear(
    	    frmNarDob.getInstance().getMasterSet().getTimestamp("DATDOK")));
    	rpc.setCskl(jrfCSKL.getText().trim());
    	rpc.jrfCART.requestFocus();
      }
    }
  };
  JraButton jbCSKL = new JraButton();
  JLabel jlCSKL = new JLabel();
  */
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  
  JraButton trans = new JraButton();
  
//  JPanel jpCSKL = new JPanel();
  
  private void disabCSKL() {
//    rcc.EnabDisabAll(this.jpCSKL, false);
  }
  
  boolean rpcLostFocus;
  frmDodatniTxt dtx;
  public void myDodText(){
	if (rpc.getCART().equalsIgnoreCase("")) {
		JOptionPane
				.showConfirmDialog(
						fNarDob.raDetail,
						"Artikl mora biti upisan da bi se mogao unijeti dodatni tekst",
						"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE);
		return;
	}
	//    Aus.dumpClassName(this);
	dtx = new frmDodatniTxt() {
		public void stoakojesnimio(QueryDataSet vtt) {
			vtsnimio(vtt);
		}

		public void stoakonijesnimio(QueryDataSet vtt) {
			vtsnimio(vtt);
		}
	};

	if (fNarDob.vttext == null) {
		dtx.setUP(this.getTopLevelAncestor(), fNarDob.getDetailSet(),
				fNarDob.raDetail.getLocation());
	} else {
		dtx.setUP(this.getTopLevelAncestor(), fNarDob.getDetailSet(),
				fNarDob.raDetail.getLocation(), fNarDob.vttext);
	}
  }
	public void vtsnimio(QueryDataSet vtt) {
		fNarDob.vttext = vtt;
		fNarDob.isVTtext = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jraKol.requestFocus();
				jraKol.selectAll();
			}
		});
	}
  
  public jpNarDobDetail(frmNarDob md) {
    try {
      fNarDob = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void initJP(char mode) {
    rcc.setLabelLaF(jraInab, false);
    rcc.setLabelLaF(jraInabDOB, false);
    if (mode == 'N') {
      rcc.setLabelLaF(jraKol, false);
      rcc.setLabelLaF(jraNc, false);
      rcc.setLabelLaF(jraNcDOB, false);
      rcc.setLabelLaF(jraPop, false);
      rcc.setLabelLaF(jraVc, false);
      rcc.setLabelLaF(jraIvc, false);

      rpc.EnabDisab(true);
      rpcLostFocus = false;
      rpc.SetDefFocus();
//      jrfCSKL.requestFocus();
      //rpc.jlrSklad.requestFocus();
    } else if (mode == 'I') {
//      rcc.EnabDisabAll(jpCSKL,false);
      rpc.EnabDisab(false);
      jraKol.requestFocus();
    }
  }

  public void BindComponents(DataSet ds) {
    jraInab.setDataSet(ds);
    jraKol.setDataSet(ds);
    jraNc.setDataSet(ds);
    jraNcDOB.setDataSet(ds);
    jraInabDOB.setDataSet(ds);
    jraPop.setDataSet(ds);
    jraVc.setDataSet(ds);
    jraIvc.setDataSet(ds);
  }
  XYLayout xYLayout2 = new XYLayout();

  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    jpDetail.setLayout(lay);
    lay.setWidth(475);
    lay.setHeight(160);
    
    jpSklRpcDetail.setLayout(new BorderLayout());
    jpSklRpcDetail.setBorder(BorderFactory.createEtchedBorder());


//  jrfNAZSKL.setNextFocusableComponent(jrfCSKL); zakomentirano zbog JDK1.4
//    jrfNAZSKL.setColumnName("NAZSKL");
//    jrfNAZSKL.setSearchMode(1);
//    jrfNAZSKL.setNavProperties(jrfCSKL);
//    
//    jlCSKL.setText("Skladište");
    jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel4.setText("Naziv");
    //  jrfCSKL.setNextFocusableComponent(jrfNAZSKL); zakomentirano zbog JDK1.4
//    jrfCSKL.setColumnName("CSKL");
//    jrfCSKL.setColNames(new String[] {"NAZSKL"});
//    jrfCSKL.setVisCols(new int[]{0,1});
//    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
//    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
//    jrfCSKL.setNavButton(jbCSKL);
    jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel3.setText("Šifra");
//    jpCSKL.setLayout(xYLayout2);
//    jpCSKL.add(jLabel3, new XYConstraints(150, 8, 100, -1));
//    jpCSKL.add(jLabel4, new XYConstraints(255, 8, 260, -1));
//    jpCSKL.add(jbCSKL, new XYConstraints(609, 25, 21, 21));
//    jpCSKL.add(jrfNAZSKL, new XYConstraints(255, 25, 349, -1));
//    jpCSKL.add(jlCSKL, new XYConstraints(15, 25, -1, -1));
//    jpCSKL.add(jrfCSKL, new XYConstraints(150, 25, 100, -1));
    
//    jpSklRpcDetail.add(jpCSKL, BorderLayout.NORTH);
    jpSklRpcDetail.add(rpc, BorderLayout.CENTER);

    jlaInab.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaInab.setText("Iznos");
    //jlaKol.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaKol.setText("Koli\u010Dina");
    jlaNc.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaNc.setText("Cijena");
    jraInab.setColumnName("INAB");
    jraInabDOB.setColumnName("IPRODBP");
    jraKol.setColumnName("KOL");
    jraNc.setColumnName("NC");
    jraNcDOB.setColumnName("FVC");
    jraPop.setColumnName("UPRAB");
    jraVc.setColumnName("VC");
    jraIvc.setColumnName("IBP");
    
    JLabel jlZaKolicinu = new JLabel();
    jlZaKolicinu.setHorizontalAlignment(SwingConstants.CENTER);
    jlZaKolicinu.setText("Za kolièinu");
    JLabel jlZaJedinicu = new JLabel();
    jlZaJedinicu.setHorizontalAlignment(SwingConstants.CENTER);
    jlZaJedinicu.setText("Za jedinicu");

    jpDetail.add(jlaKol, new XYConstraints(360, 20, -1, -1));
    jpDetail.add(new JLabel("Popust"), new XYConstraints(15, 125, -1, -1));
    jpDetail.add(jraKol, new XYConstraints(500, 20, 130, -1));
    jpDetail.add(trans, new XYConstraints(470, 20, 21, 21));
    jpDetail.add(jlZaKolicinu, new XYConstraints(500, 55, 130, -1));
    jpDetail.add(jlZaJedinicu, new XYConstraints(360, 55, 130, -1));
    jpDetail.add(new JLabel("Dobavlja\u010Deva cijena (valutna)"),
    		new XYConstraints(15, 75, -1, -1));
    jpDetail.add(jraNcDOB, new XYConstraints(360, 75, 130, -1));
    jpDetail.add(jraInabDOB, new XYConstraints(500, 75, 130, -1));
    jpDetail.add(jraPop, new XYConstraints(270, 125, 80, -1));
    jpDetail.add(jraVc, new XYConstraints(360, 125, 130, -1));
    jpDetail.add(jraIvc, new XYConstraints(500, 125, 130, -1));
    jpDetail.add(new JLabel("Dobavlja\u010Deva cijena"),
    		new XYConstraints(15, 100, -1, -1));
    jpDetail.add(jraNc, new XYConstraints(360, 100, 130, -1));
    jpDetail.add(jraInab, new XYConstraints(500, 100, 130, -1));


//    jpDetail.add(jlaInab, new XYConstraints(506, 3, 98, -1));
//    jpDetail.add(jlaNc, new XYConstraints(401, 3, 98, -1));
//    jpDetail.add(jraInab, new XYConstraints(505, 20, 100, -1));
//    jpDetail.add(jraNc, new XYConstraints(400, 20, 100, -1));
    
    trans.setIcon(raImages.getImageIcon(raImages.IMGSENDMAIL));
    trans.setAutomaticFocusLost(true);
    trans.setToolTipText("Dohvat stavki trebovanja");
    trans.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fNarDob.zahDohvat();
      }    
    });

    addTextFocus();

    jpDetail.setBorder(BorderFactory.createEtchedBorder());
    BindComponents(fNarDob.getDetailSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */

//    rpc.xYLayoutDH.setHeight(120);
    this.add(jpSklRpcDetail, BorderLayout.CENTER);
    this.add(jpDetail, BorderLayout.SOUTH);
  }

  private void addTextFocus() {
    jraKol.addFocusListener(new FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
    	  if (!oldValue.equals(jraKol.getText().toString())) {
    		  frmNarDob.getInstance().afterKOL();
    	  }
      }*/
      public void focusGained(FocusEvent e) {
      	oldValue=jraKol.getText().toString();
      }
    });
    jraNc.addFocusListener(new FocusAdapter() {
      /*public void focusLost(FocusEvent e) {
    	  if (!oldValue.equals(jraNc.getText().toString())) {
    		  frmNarDob.getInstance().afterNC();
    	  }
      }*/
      public void focusGained(FocusEvent e) {
      	oldValue=jraNc.getText().toString();
      }
    });
    jraPop.addFocusListener(new FocusAdapter() {
    	/*public void focusLost(FocusEvent e) {
      	  	if (!oldValue.equals(jraPop.getText().toString())) {
      	  		frmNarDob.getInstance().afterPop();
      	  	}
        }*/
        public void focusGained(FocusEvent e) {
        	oldValue=jraPop.getText().toString();
        }
      });
    jraVc.addFocusListener(new FocusAdapter() {
        /*public void focusLost(FocusEvent e) {
      	  	if (!oldValue.equals(jraVc.getText().toString())) {
      	  		frmNarDob.getInstance().afterVc();
      	  	}
        }*/
        public void focusGained(FocusEvent e) {
        	oldValue=jraVc.getText().toString();
        }
      });
    jraIvc.addFocusListener(new FocusAdapter() {
        /*public void focusLost(FocusEvent e) {
      	  	if (!oldValue.equals(jraIvc.getText().toString())) {
      	  		frmNarDob.getInstance().afterIvc();
      	  	}
        }*/
        public void focusGained(FocusEvent e) {
        	oldValue=jraIvc.getText().toString();
        }
      });
    jraNcDOB.addFocusListener(new FocusAdapter() {
        /*public void focusLost(FocusEvent e) {
      	  	if (!oldValue.equals(jraNcDOB.getText().toString())) {
      	  		frmNarDob.getInstance().afterNCDOB();
      	  	}
        }*/
        public void focusGained(FocusEvent e) {
        	oldValue=jraNcDOB.getText().toString();
        }
      });
    
  }

  void initRpcart() {

    rpc.setGodina(hr.restart.util.Valid.getValid().findYear(
	    frmNarDob.getInstance().getMasterSet().getTimestamp("DATDOK")));
	rpc.setCskl(frmNarDob.getInstance().getDetailSet().getString("CSKL"));
	
    rpc.setTabela(frmNarDob.getInstance().getDetailSet());
    rpc.setMode("N");
    rpc.setBorder(null);
    rpc.dodText();
    rpc.setDefParam();
    rpc.InitRaPanCart();
    rpc.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
  }
  public void enabdisabValute(boolean how){
  	rcc.EnabDisabAll(jraNcDOB, how);
//  	rcc.EnabDisabAll(jraInabDOB, how);
  	}
  
}
