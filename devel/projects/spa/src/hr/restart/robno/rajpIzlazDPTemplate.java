/****license*****************************************************************
**   file: rajpIzlazDPTemplate.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraTextMultyKolField;
import hr.restart.swing.JraToggleButton;
import hr.restart.util.Aus;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
///// D E T A I L P A N E L ////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

public class rajpIzlazDPTemplate extends JPanel {

	JraCheckBox jrcbRezervacija = new JraCheckBox("Rezervacija");

	JraToggleButton jrtbRezervacija = new JraToggleButton() {

		public void paint(Graphics g) {
			super.paint(g);
			/*
			 * System.out.println("left "+getInsets().left+"right
			 * "+getInsets().right+ "top "+getInsets().top+"bottom
			 * "+getInsets().bottom);
			 * 
			 * System.out.println("left "+jrtbRezervacija.getInsets().left+
			 * "right "+jrtbRezervacija.getInsets().right+ "top
			 * "+jrtbRezervacija.getInsets().top+"bottom
			 * "+jrtbRezervacija.getInsets().bottom);
			 */

			g.drawString(isSelected() ? "D" : "N", 6, 15);

			//						jrtbRezervacija.getInsets().right,
			//						jrtbRezervacija.getInsets().top);
		}

	};

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	raCommonClass rcc = raCommonClass.getraCommonClass();

	String tmpText = "";

	String what_kind_of_dokument;

	char mode;

	raIzlazTemplate fDI;

	hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	//  boolean isGOTGRN = false;

	///////////////////////////
	//// JPanel

	JPanel jpDetailCenter = new JPanel();

	rapancart rpcart;

	public void whattodo() {
		TypeDoc.getTypeDoc().isPrikaz(what_kind_of_dokument);
	}

	public void presetupRapancart(int version) {

		rpcart = new rapancart(version) {
			public void nextTofocus() {
				fDI.MyNextToFocus();
				jtfKOL.requestFocus();
			}

			public void metToDo_after_lookUp() {
			  if (fDI.raDetail.getMode() == 'B') return;
				metToDo_after_lookUpA();
				metToDo_after_lookUp2();
			}

			public void jbDODTXT_actionPerformed(ActionEvent e) {
				jbDODTXT_action();
			}
		};
        
        focusField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(FocusEvent e) {
            if (jtfKOL.isEnabled())
              if (fDI.raDetail.getOKpanel().isOkTraversable())
                fDI.raDetail.getOKpanel().jBOK.requestFocus();
              else jtfKOL.requestFocusLater();
            }
        });
	}

	public void metToDo_after_lookUpA() {
		//    SwingUtilities.invokeLater(new Runnable() {
		//      public void run() {

		//    System.out.println("metodometToDo_after_lookUpA");
		fDI.findCPOR();
		fDI.findCStanje();
		fDI.updateTxt();
		//      }
		//    });
		//      fDI.restoreDummySet();
	}

	public void metToDo_after_lookUp2() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if ( fDI instanceof raDOS) {
					jtfKOL2.requestFocus();
					jtfKOL2.selectAll();
				} else if (jraFC.isShowing() && fDI.isSingleKOL) {
				  fDI.getDetailSet().setBigDecimal("KOL", Aus.one0);
				  fDI.Kalkulacija(jtfKOL, "KOL");
				  jraFC.requestFocus();
				  jraFC.selectAll();
				} else {
					jtfKOL.requestFocus();
					jtfKOL.selectAll();
				}
				System.out.println("afterlook2");
			}
		});

	}

	//// Layout
	BorderLayout borderLayout2 = new BorderLayout();

	XYLayout xYLayoutDC = new XYLayout();
	
	XYLayout tlay = new XYLayout(660, 180);

	//// JraTextField
	JraTextField jraFC = new JraTextField() {
      public void valueChanged() {
        fDI.Kalkulacija(jraFC, "FC");
      }
    };

	JraTextField jraFMC = new JraTextField() {
      public void valueChanged() {
        fDI.Kalkulacija(jraFMC, "FMC");
      }
    };

	JraTextField jraFMCPRP = new JraTextField() {
      public void valueChanged() {
        fDI.Kalkulacija(jraFMCPRP, "FMCPRP");
      }
    };

	JraTextField jtfKOL2 = new JraTextField() {
      public void valueChanged() {
        fDI.getDetailSet().setBigDecimal("KOL", fDI.getDetailSet().getBigDecimal("KOL2"));
      }
    };
    
	JraTextMultyKolField jtfKOL = new JraTextMultyKolField() {

		public void propertyChange(PropertyChangeEvent evt) {
//System.out.println("evt.getPropertyName()"+
//        evt.getPropertyName());
			if (evt.getPropertyName().equalsIgnoreCase("KOL"))
				myKalkul(new FocusEvent(jtfKOL, FocusEvent.FOCUS_FIRST));

		}
        
        public void valueChanged() {
          myKalkul(jtfKOL);
          //fDI.getDetailSet().setBigDecimal("KOL", fDI.getDetailSet().getBigDecimal("KOL2"));
        }
        
	};

	JraTextField jraUPRAB = new JraTextField() {
      public void valueChanged() {
        fDI.Kalkulacija(jraUPRAB, "UPRAB");
      }
    };

	JraTextField jraIPROBDP = new JraTextField() {
		public boolean isFocusTraversable() {
			return false;
		}
	};

	JraTextField jraZC = new JraTextField() {
	  public void valueChanged() {
	    fDI.Kalkulacija(jraZC, "KOL");
	  }
	};

	JraTextField jraIRAZ = new JraTextField() {
		public boolean isFocusTraversable() {
			return false;
		}
	};
	JraTextField jraLOT = new JraTextField();
	
	JraTextField jraPAK = new JraTextField();
	
	JraTextField jraRNC = new JraTextField() {
    public void valueChanged() {
      fDI.nabKal("RNC");
    }
  };
	JraTextField jraRINAB = new JraTextField() {
    public void valueChanged() {
      fDI.nabKal("RINAB");
    }
  };

	JraTextField jraIPRODSP = new JraTextField() {
		/*public boolean isFocusTraversable() {
			return false;
		}*/
        public void valueChanged() {
            fDI.Kalkulacija(jraIPRODSP, "IPRODSP");
        }
	};

	JTextField focusField = new JTextField();

	JLabel jlRABATI = new JLabel();

	JLabel jlZAVTR = new JLabel();

	JLabel jlCBP = new JLabel();

	JLabel jlPostotak = new JLabel();

	JLabel jlFC1 = new JLabel();

	JLabel jlFC = new JLabel();

	JLabel jlZAKOL = new JLabel();
	
	JLabel jlNABTR = new JLabel();

	JLabel jlKOL = new JLabel();
	JLabel jlKOL1 = new JLabel();
	JLabel jlKOL2 = new JLabel();

	JraButton jbRabat = new JraButton();

	private JLabel jLabel1 = new JLabel();

	private JraTextField jraPORER = new JraTextField() {
		public boolean isFocusTraversable() {
			return false;
		}
	};

	/////////////////// ZAVRSETAK DEKLARACIJE VARIJABLI ///////////////////

	public rajpIzlazDPTemplate(String what, raIzlazTemplate FDI) {
		//System.out.println("what "+what);
		fDI = FDI;
		if (fDI.equals(null))
			System.out.println("FDI je null i treba nesto uciniti");
		what_kind_of_dokument = what;
		goFromHere();
	}

	public void goFromHere() {
		presetupRapancart(TypeDoc.getTypeDoc().isPrikaz(what_kind_of_dokument) ? 2
				: 1);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void InitRaPanCartDP() {

		rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(
				fDI.getMasterSet().getTimestamp("DATDOK")));
		rpcart.setCskl(fDI.getDetailSet().getString("CSKL"));
		/*
		 * rpcart.setTabela(fDI.getDetailSet());
		 * rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
		 * rpcart.InitRaPanCart();
		 */
		rpcart.setDefParam();
		rpcart.setMode("N");
		rpcart.dodText();

		//    rpcart.setSearchable(false);
	}

	public void instalRezervaciju() {
		/*
		 * jpDetailCenter .add(jrcbRezervacija, new XYConstraints(500, 40, 109,
		 * -1));
		 */
		//		jrtbRezervacija.setSelected();
		//		jrtbRezervacija.setText(isUserSelected()?"D":"N");
	  boolean nd = raIzlazTemplate.isNabDirect();
		JLabel rezervacija = new JLabel("Rezervacija");
		rezervacija.setBorder(BorderFactory.createEtchedBorder());
		rezervacija.setHorizontalAlignment(SwingConstants.CENTER);
		rcc.setLabelLaF(rezervacija, false);
		jpDetailCenter.add(rezervacija, new XYConstraints(510, nd ? 75: 40, 112, -1));
		jpDetailCenter.add(jrtbRezervacija, new XYConstraints(627, nd ? 75 : 40, 21, 21));
	}

	public void setRezervacija() {

		if (!hr.restart.sisfun.frmParam.getParam("robno", "rezkol",
				"Rezerviranje kolièine D/N/O", "D").equalsIgnoreCase("N")) {
			fDI.getDetailSet().setString("REZKOL", "D");
			jrtbRezervacija.setSelected(true);
			//		jrtbRezervacija.setText("D");

		} else {
			fDI.getDetailSet().setString("REZKOL", "N");
			jrtbRezervacija.setSelected(false);
			//		jrtbRezervacija.setText("N");
		}
	}

	private frmDodatniTxt dtx;

	public void jbDODTXT_action() {

		if (rpcart.getCART().equalsIgnoreCase("")) {
			JOptionPane
					.showConfirmDialog(
							fDI.raDetail,
							"Artikl mora biti upisan da bi se mogao unijeti dodatni tekst",
							"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return;
/*		} else if (!rpcart.isUsluga()) {
			JOptionPane
					.showConfirmDialog(
							fDI.raDetail,
							"Artikl mora biti tipa usluge da bi se mogao unijeti dodatni tekst",
							"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return;*/
		}
		//    Aus.dumpClassName(this);
		dtx = new frmDodatniTxt() {
			public void stoakojesnimio(QueryDataSet vtt) {
				vtsnimio(vtt);
			}

			public void stoakonijesnimio(QueryDataSet vtt) {
				vtsnimio(vtt);
				//        vtnijesnimio(vtt);
			}
		};

		//    dtx.setUP((java.awt.Frame)fDI.getParent(),fDI.getDetailSet(),fDI.raDetail.getLocation());
		if (fDI.vttext == null) {
			dtx.setUP(this.getTopLevelAncestor(), fDI.getDetailSet(),
					fDI.raDetail.getLocation());
		} else {
			dtx.setUP(this.getTopLevelAncestor(), fDI.getDetailSet(),
					fDI.raDetail.getLocation(), fDI.vttext);
		}
	}

	public void vtsnimio(QueryDataSet vtt) {
		fDI.vttext = vtt;
		fDI.isVTtext = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jtfKOL.requestFocus();
				jtfKOL.selectAll();
			}
		});
	}

	public void myKalkul(FocusEvent e) {
		if (fDI.TD.isDocNegateKol(fDI.getDetailSet().getString("VRDOK"))) {
			if (fDI.getDetailSet().getBigDecimal("KOL").doubleValue() > 0) {
				fDI.getDetailSet().setBigDecimal("KOL",
						fDI.getDetailSet().getBigDecimal("KOL").negate());
			}
		}
		fDI.Kalkulacija(e, "KOL");
	}
    
    public void myKalkul(JraTextField tf) {
        if (fDI.TD.isDocNegateKol(fDI.getDetailSet().getString("VRDOK"))) {
            if (fDI.getDetailSet().getBigDecimal("KOL").doubleValue() > 0) {
                fDI.getDetailSet().setBigDecimal("KOL",
                        fDI.getDetailSet().getBigDecimal("KOL").negate());
            }
        }
        fDI.Kalkulacija(tf, "KOL");
    }

	private void jbInit() throws Exception {

		setLayout(tlay);
		rpcart.setMyAfterLookupOnNavigate(false);
		rpcart.setFocusCycleRoot(true);
		/*
		 * jrcbRezervacija.setHorizontalTextPosition(SwingConstants.LEFT);
		 * jrcbRezervacija.setHorizontalAlignment(SwingConstants.RIGHT);
		 * 
		 * jrcbRezervacija.setColumnName("REZKOL");
		 * jrcbRezervacija.setSelectedDataValue("D");
		 * jrcbRezervacija.setUnselectedDataValue("N");
		 */
		jrtbRezervacija.setColumnName("REZKOL");
		jrtbRezervacija.setSelectedDataValue("D");
		jrtbRezervacija.setUnselectedDataValue("N");
		jrtbRezervacija.setIcon(null);
		//		jrtbRezervacija.setBorder(BorderFactory.createEtchedBorder());

		//jrtbRezervacija.getInsets().¸

		//		jrtbRezervacija.setFont(jrtbRezervacija.getFont().deriveFont((float)8.0));
		/*
		 * jrtbRezervacija.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) { boolean s = e.getStateChange() ==
		 * e.SELECTED; ((JraToggleButton)e.getSource()).setText(s?"D":"N"); }
		 * });
		 */
		jlZAVTR.setText("Zavisni troškovi");
		jlPostotak.setText("Popust (%)");
		jlPostotak.setHorizontalAlignment(SwingConstants.RIGHT);

		jlCBP.setText("Cijena bez poreza");
		jlCBP.setHorizontalAlignment(SwingConstants.RIGHT);

		jlRABATI.setText("Bez poreza");
		jlRABATI.setHorizontalAlignment(SwingConstants.RIGHT);

		jlZAKOL.setText("S porezom");
		jlZAKOL.setHorizontalAlignment(SwingConstants.RIGHT);
		jlFC.setText("Prodajna cijena");
		jlFC.setHorizontalAlignment(SwingConstants.RIGHT);
		
		jlNABTR.setText("Nabavna cijena i iznos (za tranzit)");
		jlNABTR.setHorizontalAlignment(SwingConstants.RIGHT);
		//    jraIPRODSP.setEditable(false);
		
		jraRNC.setColumnName("RNC");
		jraRNC.setDataSet(fDI.getDetailSet());

		jraRINAB.setColumnName("RINAB");
		jraRINAB.setDataSet(fDI.getDetailSet());
		
		jraIPRODSP.setColumnName("IPRODSP");
		jraIPRODSP.setDataSet(fDI.getDetailSet());
		/*jraIPRODSP.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (jraIPRODSP.isValueChanged()) {
					fDI.Kalkulacija(e, "IPRODSP");
				}
			}
		});*/

		jtfKOL2.setColumnName("KOL2");
		jtfKOL2.setDataSet(fDI.getDetailSet());
		/*jtfKOL2.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (jtfKOL2.isValueChanged()) {
					fDI.getDetailSet().setBigDecimal("KOL", fDI.getDetailSet().getBigDecimal("KOL2"));
				}
			}
		});*/
//		jtfKOL.addPropertyChangeListener(jtfKOL);
		jtfKOL.setColumnName("KOL");
		jtfKOL.setDataSet(fDI.getDetailSet());
		jtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
			/*public void focusLost(FocusEvent e) {
				if (jtfKOL.isValueChanged()) {
					myKalkul(e);
				}
			}*/

			public void focusGained(FocusEvent e) {
				fDI.MfocusGained(e);
			}
		});
		jtfKOL.addPropertyChangeListener(jtfKOL);

		//    jraFC.setNextFocusableComponent(jraUPRAB); zakomentirano zbog 1.4
		jraFC.setColumnName("FC");
		jraFC.setDataSet(fDI.getDetailSet());
		jraFC.addFocusListener(new java.awt.event.FocusAdapter() {
			/*public void focusLost(FocusEvent e) {
				if (jraFC.isValueChanged()) {
					fDI.Kalkulacija(e, "FC");
				}
			}*/

			public void focusGained(FocusEvent e) {
				fDI.MfocusGained(e);
			}
		});
		//    jraUPRAB.setNextFocusableComponent(jtfKOL); // zakomentirano zbog 1.4
		jraUPRAB.setColumnName("UPRAB");
		jraUPRAB.setDataSet(fDI.getDetailSet());
		jraUPRAB.addFocusListener(new java.awt.event.FocusAdapter() {
			/*public void focusLost(FocusEvent e) {
				if (jraUPRAB.isValueChanged()) {
					fDI.Kalkulacija(e, "UPRAB");
				}
				//        if (!TypeDoc.getTypeDoc().isGOTGRN(what_kind_of_dokument)){
				//          jtfKOL.requestFocus();
				//        }
			}*/
			public void focusGained(FocusEvent e) {
				fDI.MfocusGained(e);
			}
		});

		jpDetailCenter.setLayout(xYLayoutDC);
		xYLayoutDC.setHeight(50);
		if (fDI.what_kind_of_dokument.equalsIgnoreCase("PON"))
		  xYLayoutDC.setHeight(raIzlazTemplate.isNabDirect() ? 105 : 70);
		if ((fDI.what_kind_of_dokument.equalsIgnoreCase("RAC") ||
				fDI.what_kind_of_dokument.equalsIgnoreCase("ODB")) && raIzlazTemplate.isNabDirect())
			xYLayoutDC.setHeight(80);
		xYLayoutDC.setWidth(660);
		/*jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());
		if (rpcart.isbPrikazKolicina()) {
			if (rpcart.isExtraSklad()) {
				jpDetailCenter.setMinimumSize(new Dimension(400, 700));
			} else {
				jpDetailCenter.setMinimumSize(new Dimension(400, 550));
			}
		} else {
			jpDetailCenter.setMinimumSize(new Dimension(400, 250));
		}
		jpDetailCenter.setPreferredSize(new Dimension(400, 300));*/
		jlKOL.setHorizontalAlignment(SwingConstants.RIGHT);
		jlKOL.setText("Koli\u010Dina ");
		jlKOL1.setHorizontalAlignment(SwingConstants.RIGHT);
		jlKOL1.setText("Naruèeno");
		jlKOL2.setHorizontalAlignment(SwingConstants.RIGHT);
		jlKOL2.setText("Isporuèeno");

		jbRabat.setText("...");
		jbRabat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fDI.jbRabat_actionPerformed(e);
			}
		});

		jraIPROBDP.setEditable(false);
		jraIPROBDP.setColumnName("IPRODBP");
		jraIPROBDP.setDataSet(fDI.getDetailSet());
		//    rpcart.setnextFocusabile(this.jtfKOL);

		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText("Porez (%)");
		jraPORER.setEditable(false);
		jraPORER.setDataSet(fDI.getDetailSet());
		jraPORER.setColumnName("UPPOR");

		int ah = rpcart.getTotalHeight();
		System.out.println("AH="+ah);
		tlay.setHeight(ah + xYLayoutDC.getHeight());
		add(rpcart, new XYConstraints(0, 0, -1, -1));
        add(jpDetailCenter, new XYConstraints(0, ah, -1, -1));
		/*if (rpcart.isbPrikazKolicina()) {
			if (rpcart.isExtraSklad()) {
				setPreferredSize(new Dimension(660, 280));
			} else {
				setPreferredSize(new Dimension(660, 210));
			}
			this.add(rpcart, new XYConstraints(0, 0, 660, 130));
			add(jpDetailCenter, new XYConstraints(0, 130, 660, 90));
		} else {
			setPreferredSize(new Dimension(660, 140));
			add(rpcart, new XYConstraints(0, 0, 660, 90));
			add(jpDetailCenter, new XYConstraints(0, 90, 660, 90));
		}*/
	}

	public void addRestOnlyKol() {
		JTextField dummyTF = new JTextField();

		dummyTF.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
              if (jtfKOL.isEnabled())
                if (fDI.raDetail.getOKpanel().isOkTraversable())
                  fDI.raDetail.getOKpanel().jBOK.requestFocus();
                else jtfKOL.requestFocusLater();
			}
		});

		jpDetailCenter.add(jlKOL, new XYConstraints(380, 17, 100, -1)); // 15,0
		jpDetailCenter.add(jtfKOL, new XYConstraints(500, 17, 108, -1));//15,15
		jpDetailCenter.add(dummyTF, new XYConstraints(550, 200, 1, 1));

	}
	public void addRestOnlyKol2() {
		JTextField dummyTF = new JTextField();

		dummyTF.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
              if (jtfKOL2.isEnabled())
                if (fDI.raDetail.getOKpanel().isOkTraversable())
                  fDI.raDetail.getOKpanel().jBOK.requestFocus();
                else jtfKOL2.requestFocusLater();
			}
		});

		jpDetailCenter.add(jlKOL1, new XYConstraints(145, 17, 100, -1)); // 15,0
		jpDetailCenter.add(jtfKOL2, new XYConstraints(260, 17, 108, -1));//15,15
		jpDetailCenter.add(jlKOL2, new XYConstraints(380, 17, 100, -1)); // 15,0
		jpDetailCenter.add(jtfKOL, new XYConstraints(500, 17, 108, -1));//15,15
		jpDetailCenter.add(dummyTF, new XYConstraints(550, 200, 1, 1));

	}

	public void addRest() {
		//		if (rpcart.isbPrikazKolicina()) {
		//			setPreferredSize(new Dimension(660, 210));
		//		} else {
		//			setPreferredSize(new Dimension(660, 140));
		//		}

		jpDetailCenter.add(jlKOL, new XYConstraints(15, 0, 100, -1)); // 15,0
		jpDetailCenter.add(jtfKOL, new XYConstraints(15, 17, 100, -1));//15,15
		jpDetailCenter.add(jlFC, new XYConstraints(120, 0, 110, -1)); //120
		jpDetailCenter.add(jraFC, new XYConstraints(120, 17, 110, -1));//120
		jpDetailCenter.add(jlPostotak, new XYConstraints(235, 0, 75, -1));
		// Request by Mladen (Siniša)
		//    jpDetailCenter.add(jraUPRAB, new XYConstraints(312, 15, 40, -1));
		//    jpDetailCenter.add(jbRabat, new XYConstraints(357, 15, 21, 21));
		jpDetailCenter.add(jraUPRAB, new XYConstraints(235, 17, 75, -1));
		jpDetailCenter.add(jbRabat, new XYConstraints(635, 17, 21, 21));
		jpDetailCenter.add(jlRABATI, new XYConstraints(315, 0, 110, -1));
		jpDetailCenter.add(jraIPROBDP, new XYConstraints(315, 17, 110, -1));
		jpDetailCenter.add(jLabel1, new XYConstraints(430, 0, 75, -1));
		jpDetailCenter.add(jraPORER, new XYConstraints(430, 17, 75, -1));
		jpDetailCenter.add(jlZAKOL, new XYConstraints(510, 0, 110, -1));
		jpDetailCenter.add(jraIPRODSP, new XYConstraints(510, 17, 110, -1));
        jpDetailCenter.add(focusField, new XYConstraints(560, 501, 1, -1));
        
    if ((fDI.what_kind_of_dokument.equalsIgnoreCase("RAC") ||
        fDI.what_kind_of_dokument.equalsIgnoreCase("PON") ||
    	fDI.what_kind_of_dokument.equalsIgnoreCase("ODB")) && raIzlazTemplate.isNabDirect()) {
    	jpDetailCenter.add(jlNABTR, new XYConstraints(15, 50, 360, -1));
    	jpDetailCenter.add(jraRNC, new XYConstraints(395, 50, 110, -1));
    	jpDetailCenter.add(jraRINAB, new XYConstraints(510, 50, 110, -1));
    }	
  }

	public void addRestGRNGOT() {
		//this.setPreferredSize(new Dimension(650, 140));
		jlFC.setText("Prodajna cijena");
		jlFC.setHorizontalAlignment(SwingConstants.RIGHT);
		jlFC1.setText("Cijena s popustom");
		jlFC1.setHorizontalAlignment(SwingConstants.RIGHT);
		jlZAKOL.setText("Iznos");
		jlZAKOL.setHorizontalAlignment(SwingConstants.RIGHT);
		jraFMCPRP.setColumnName("FMCPRP");
        jraFMCPRP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(FocusEvent e) {
                fDI.MfocusGained(e);
            }
        });
		//    jraFMCPRP.setDataSet(fDI.getDetailSet());

		jraFMC.setColumnName("FMC");
		//    jraFMC.setDataSet(fDI.getDetailSet());
		jraFMC.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				fDI.MfocusGained(e);
			}
		});

		jpDetailCenter.add(jlKOL, new XYConstraints(15, 0, 100, -1));
		jpDetailCenter.add(jtfKOL, new XYConstraints(15, 17, 100, -1));
		jpDetailCenter.add(jlFC, new XYConstraints(120, 0, 130, -1));
		jpDetailCenter.add(jraFMCPRP, new XYConstraints(120, 17, 130, -1));
		jpDetailCenter.add(jlPostotak, new XYConstraints(255, 0, 65, -1));
		jpDetailCenter.add(jraUPRAB, new XYConstraints(255, 17, 65, -1));
		jpDetailCenter.add(jlFC1, new XYConstraints(325, 0, 130, -1));
		jpDetailCenter.add(jraFMC, new XYConstraints(325, 17, 130, -1));
		jpDetailCenter.add(jlZAKOL, new XYConstraints(478, 0, 130, -1));
		jpDetailCenter.add(jraIPRODSP, new XYConstraints(478, 17, 130, -1));
        jpDetailCenter.add(focusField, new XYConstraints(560, 501, 1, -1));
	}

	public void resizeDP() {
	  remove(jpDetailCenter);
	  int ah = rpcart.getTotalHeight();
      tlay.setHeight(ah + xYLayoutDC.getHeight());
      add(jpDetailCenter, new XYConstraints(0, ah, -1, -1));
		/*remove(rpcart);
		remove(jpDetailCenter);
		if (rpcart.isExtraSklad()) {
			jpDetailCenter.setMinimumSize(new Dimension(400, 720));

			if (rpcart.isbPrikazKolicina()) {
				setPreferredSize(new Dimension(660, 260));
				add(rpcart, new XYConstraints(0, 0, 660, 180));
				add(jpDetailCenter, new XYConstraints(0, 180, 660, 120));
			} else {
				setPreferredSize(new Dimension(660, 210));
				add(rpcart, new XYConstraints(0, 0, 660, 130));
				add(jpDetailCenter, new XYConstraints(0, 130, 660, 90));
			}
		} else {
			if (fDI.what_kind_of_dokument.equalsIgnoreCase("PON")) {
				jpDetailCenter.setMinimumSize(new Dimension(400, 200));
				setPreferredSize(new Dimension(660, 205));//ai 185 -> 205
				if (rpcart.isbPrikazKolicina()) {
					add(rpcart, new XYConstraints(0, 0, 660, 135));
					add(jpDetailCenter, new XYConstraints(0, 135, 660, 90));
				} else {
					add(rpcart, new XYConstraints(0, 0, 660, 105));
					add(jpDetailCenter, new XYConstraints(0, 105, 660, 120));
				}
			} else {
				jpDetailCenter.setMinimumSize(new Dimension(400, 550));
				setPreferredSize(new Dimension(660, 185));
				add(rpcart, new XYConstraints(0, 0, 660, 135));
				add(jpDetailCenter, new XYConstraints(0, 135, 660, 90));
			}
		}*/
	}

	public void addRestOTP() {

		if (rpcart.isbPrikazKolicina()) {
			setPreferredSize(new Dimension(660, 180));
		} else {
			setPreferredSize(new Dimension(660, 140));
		}
		

		jraZC.setColumnName("ZC");
		jraIRAZ.setColumnName("IRAZ");
		rcc.setLabelLaF(jraZC, true);
		rcc.setLabelLaF(jraIRAZ, true);
		jlKOL.setText("Izlaz");
		jlKOL.setHorizontalAlignment(SwingConstants.LEFT);
		jlRABATI.setText("Koli\u010Dina");
		jlRABATI.setHorizontalAlignment(SwingConstants.RIGHT);
		jlZAVTR.setText("Cijena");
		jlZAVTR.setHorizontalAlignment(SwingConstants.RIGHT);
		jlCBP.setText("Iznos");
		jlCBP.setHorizontalAlignment(SwingConstants.RIGHT);

		jpDetailCenter.add(jlKOL, new XYConstraints(18, 16, -1, -1));
		jpDetailCenter.add(jlRABATI, new XYConstraints(153, 0, 150, -1));
		jpDetailCenter.add(jtfKOL, new XYConstraints(153, 16, 150, -1));
		jpDetailCenter.add(jlZAVTR, new XYConstraints(307, 0, 149, -1));
		jpDetailCenter.add(jraZC, new XYConstraints(307, 16, 149, -1));
		jpDetailCenter.add(jlCBP, new XYConstraints(460, 0, 150, -1));
		jpDetailCenter.add(jraIRAZ, new XYConstraints(460, 16, 150, -1));
		jpDetailCenter.add(focusField, new XYConstraints(360, 501, 1, -1));
	}
	
	public void addLOT() {
		if (rpcart.isbPrikazKolicina()) {
			setPreferredSize(new Dimension(660, 210));
		} else {
			setPreferredSize(new Dimension(660, 170));
		}
		xYLayoutDC.setHeight(70);
		jraLOT.setColumnName("LOT");
		jraPAK.setColumnName("KOL1");
		jpDetailCenter.add(new JLabel("Šarža"), new XYConstraints(380, 41, -1, -1));
		jpDetailCenter.add(jraLOT, new XYConstraints(460, 41, 150, -1));
		jpDetailCenter.add(new JLabel("Pakiranja"), new XYConstraints(18, 41, -1, -1));
		jpDetailCenter.add(jraPAK, new XYConstraints(153, 41, 150, -1));
	}

	void setEnabledAll(boolean trut) {

		rcc.EnabDisabAll(jpDetailCenter, trut);
//		rcc.setLabelLaF(jraZC, false);
		if (raIzlazTemplate.allowPriceChange() && fDI.getDetailSet().getString("VRDOK").equalsIgnoreCase("OTP")) {
		  rcc.setLabelLaF(jraZC, trut);
		} else {
		  rcc.setLabelLaF(jraZC, false);
		}
		rcc.setLabelLaF(jraIRAZ, false);

		if (raIzlazTemplate.allowIznosChange())
		  rcc.setLabelLaF(jraIPRODSP, trut);
		else rcc.setLabelLaF(jraIPRODSP, false);
		
		rcc.setLabelLaF(jraPORER, false);
		rcc.setLabelLaF(jraIPROBDP, false);
		if (raIzlazTemplate.isNabDirect() && fDI.allowNabedit) {
		  if (!fDI.checkAccess()) {
		    rcc.setLabelLaF(jtfKOL, false);
	        rcc.setLabelLaF(jraFC, false);
	        rcc.setLabelLaF(jraUPRAB, false);
		  }
		}

		if (fDI.getDetailSet().getString("VRDOK").equalsIgnoreCase("GOT")
				|| fDI.getDetailSet().getString("VRDOK")
						.equalsIgnoreCase("GRN")
				|| fDI.isMaloprodajnaKalkulacija) {

			if (!hr.restart.sisfun.frmParam.getParam("robno", "extMalpDozvola",
					"N", "Posebna dozvola za mijenjanje cijene u malop.")
					.equalsIgnoreCase("D")) {
				rcc.setLabelLaF(jraFMCPRP, false);
			}
		}
	}

	public void BindComp() {

		//		jrcbRezervacija.setDataSet(fDI.getDetailSet());
		jrtbRezervacija.setDataSet(fDI.getDetailSet());
		jraFMCPRP.setDataSet(fDI.getDetailSet());
		jraFMC.setDataSet(fDI.getDetailSet());

		jraIPRODSP.setDataSet(fDI.getDetailSet());
		jtfKOL.setDataSet(fDI.getDetailSet());
		jtfKOL2.setDataSet(fDI.getDetailSet());
		jraFC.setDataSet(fDI.getDetailSet());
		jraUPRAB.setDataSet(fDI.getDetailSet());
		jraIPROBDP.setDataSet(fDI.getDetailSet());
		jraZC.setDataSet(fDI.getDetailSet());
		jraIRAZ.setDataSet(fDI.getDetailSet());
		jraPORER.setDataSet(fDI.getDetailSet());
		jraRNC.setDataSet(fDI.getDetailSet());
		jraRINAB.setDataSet(fDI.getDetailSet());
		rpcart.setTabela(fDI.getDetailSet());
		rpcart.setParam(hr.restart.sisfun.frmParam
				.getParam("robno", "indiCart"));
		rpcart.InitRaPanCart();
	}

	//	public void nada(){
	//		
	//		Thread th = new Thread(new Runnable(){
	//
	//			public void run() {
	//			}});
	//	}

}