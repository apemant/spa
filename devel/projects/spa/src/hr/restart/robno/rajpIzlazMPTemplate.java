/****license*****************************************************************
**   file: rajpIzlazMPTemplate.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgTotalPromet;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.basic.BasicTableUI;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////
///// M A S T E R P A N E L ////////////////
////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

class rajpIzlazMPTemplate extends JPanel {

	hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass
			.getraCommonClass();

	java.math.BigDecimal Nula = new java.math.BigDecimal("0.00");

	raIzlazTemplate fDI;

	JTabbedPane mainTab = new JTabbedPane() {
		public boolean isFocusTraversable() {
			return false;
		}
	};
	dlgTotalPromet dtp = new dlgTotalPromet("CPAR");

	rajpBrDok rajpBrDok = new rajpBrDok();

	panBasic panelBasic;

	panBasicExt panelBasicExt;
    
    panDodatniExt panelDodExt;

	panPopust panelPopust;

	panZavtr panelZavtr;

	panOstatak panelOstatak;

	panDodatni panelDodatni;

	panRevers panelRevers;
	
	panZah panelZah;

	BorderLayout borderLayout2 = new BorderLayout();

	int version = 0;
	boolean gotpar = false;

	public rajpIzlazMPTemplate(String what_kind_of_dokument, raIzlazTemplate FDI) {
		fDI = FDI;
		try {
		  
		    gotpar = "D".equalsIgnoreCase(
	          frmParam.getParam("robno", "gotPar", "N",
	          "Gotovinski raèuni za partnere (D,N)"));
		    
		    gotpar = gotpar && ("GOT|GRN|PRD".indexOf(what_kind_of_dokument) >= 0);
		    if (what_kind_of_dokument.equals("PRD") && !fDI.bPonudaZaKupca) gotpar = false;
		  
			version = TypeDoc.getTypeDoc().numberPanel(what_kind_of_dokument);
			int version = TypeDoc.getTypeDoc().numberPanel(
					what_kind_of_dokument);
			//      if (version==5 && FDI.bPonudaZaKupca) version=6;

			if (what_kind_of_dokument.equalsIgnoreCase("PRD")  && fDI.bPonudaZaKupca) version = 2;
			
			if (version == 3 && fDI.bPonudaZaKupca) {
				version = 2;
			}
			if (version == 6 && fDI.isDosIzd()) version = 1;
			//System.out.println("version = "+version);
			jbInit(version);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void EnabDisabforChange(boolean b) {
	}

	public void SetajKadMeVidis() {
		//		if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) != 1)
		mainTab.setSelectedIndex(0);
	}

	private void myafterlooseCPAR() {
//System.out.println("myafterlooseCPAR() panelDodatni="+panelDodatni);        
		if (panelDodatni != null) {
//System.out.println("myafterlooseCPAR() radi");            
			panelDodatni.setupKO();
		}
	}

	private void InitComponents(int version) throws Exception {

		if (version != 4 && version != 7)
			panelBasic = new panBasic(version) {
				void afterlooseCPAR() {
					myafterlooseCPAR();
				}
			};
		if (version == 0 || version == 2 || version == 5) {
			//		if (version == 0 || version == 2 ) {
			panelBasicExt = new panBasicExt(version);
			panelPopust = new panPopust();
			panelOstatak = new panOstatak();
			panelDodatni = new panDodatni();
			//		} else if (version == 5) {
			//			panelBasicExt = new panBasicExt(version);
			//			panelPopust = new panPopust();
			//			panelOstatak = new panOstatak();
			//			panelDodatni = new panDodatni();
		} else if (version == 1) {
			panelDodatni = new panDodatni();
		} else if (version == 3) {
			panelBasicExt = new panBasicExt(version);
			panelDodatni = new panDodatni();
		} else if (version == 6) {
            panelBasicExt = new panBasicExt(version);
			panelDodatni = new panDodatni();
            panelDodExt = new panDodatniExt();
            panelOstatak = new panOstatak();
            panelOstatak.jlIZDOK.setText("Broj prijema robe");
		} else if (version == 4) {
			panelRevers = new panRevers() {
				void afterlooseCPAR() {
					myafterlooseCPAR();
				}
			};
			panelDodatni = new panDodatni();
		} else if (version == 7) {
		  panelZah = new panZah();
		  panelOstatak = new panOstatak();
		  panelDodatni = new panDodatni();
		} else if (version == 8) {
          panelOstatak = new panOstatak();
          panelDodatni = new panDodatni();
        }

	}

	private void jbInit(int version) throws Exception {

	  

		setLayout(borderLayout2);
		rajpBrDok.addBorder();
		add(rajpBrDok, BorderLayout.NORTH);
		InitComponents(version);
		if (version == 0 || version == 2 || version == 5) {
			add(mainTab, BorderLayout.CENTER);
			mainTab.add(panelBasic, "Osnovni podaci");
			mainTab.add(panelBasicExt, (version == 0 || 
			    (version==2 && gotpar)) ? "Poslovne jedinice"
					: "Osnovni podaci nastavak");
			mainTab.add(panelPopust, "Popusti");
			//			if (version != 0)
			mainTab.add(panelOstatak, "Dodatni podaci");
			mainTab.add(panelDodatni, "Napomene i ostalo");
		}
		/*
		 * staro za izdatnice else if (version == 1) { add(panelBasic,
		 * BorderLayout.CENTER); }
		 */
		else if (version == 3) {
			add(mainTab, BorderLayout.CENTER);
			mainTab.add(panelBasic, "Osnovni podaci");
			mainTab.add(panelBasicExt, "Poslovne jedinice");
			mainTab.add(panelDodatni, "Napomene i ostalo");
		} else if (version == 1) { /// ODB,TER, POD
			add(mainTab, BorderLayout.CENTER);
			mainTab.add(panelBasic, "Osnovni podaci");
			mainTab.add(panelDodatni, "Napomene i ostalo");
        } else if (version == 6) {
            add(mainTab, BorderLayout.CENTER);
            mainTab.add(panelBasic, "Osnovni podaci");
            mainTab.add(panelDodExt, "Poslovne jedinice");
            mainTab.add(panelOstatak, "Dodatni podaci");
		} else if (version == 4) { /// ODB,TER, POD
			add(mainTab, BorderLayout.CENTER);
			mainTab.add(panelRevers, "Osnovni podaci");
			mainTab.add(panelDodatni, "Napomene i ostalo");
		} else if (version == 7) {
		  add(mainTab, BorderLayout.CENTER);
          mainTab.add(panelZah, "Osnovni podaci");
          mainTab.add(panelOstatak, "Dodatni podaci");
          mainTab.add(panelDodatni, "Napomene i ostalo");
		} else if (version == 8) {
          add(mainTab, BorderLayout.CENTER);
          mainTab.add(panelBasic, "Osnovni podaci");
          mainTab.add(panelOstatak, "Dodatni podaci");
          mainTab.add(panelDodatni, "Napomene i ostalo");
        }
		final int[] keys = {KeyEvent.VK_1, KeyEvent.VK_2, 
		        KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5};
		for (int i = 0; i < mainTab.getComponentCount(); i++) {
		  fDI.raMaster.addKeyAction(new raKeyAction(keys[i], KeyEvent.ALT_MASK) {
            public void keyAction() {
              for (int i = 0; i < 5; i++)
                if (keys[i] == getRaActionKey())
                  mainTab.setSelectedIndex(i);
            }
          });
		}
		setupOnePredef();
	}

	public void setupOnePredef() {

		this.addAncestorListener(new javax.swing.event.AncestorListener() {
			public void ancestorAdded(javax.swing.event.AncestorEvent e) {
				SetajKadMeVidis();
			}

			public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
			}

			public void ancestorMoved(javax.swing.event.AncestorEvent e) {
			}
		});

		mainTab.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				MystateChanged(e);
			}
		});
	}

	public void EnabSetup() {
		if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 0
				|| TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 2) { ///sve
			// osim
			// izdatnica
			hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
					panelBasicExt.jlrNAZFRA, false);
			hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
					panelBasicExt.jlrNAZNAC, false);
			hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
					panelBasicExt.jlrNAZNACPL, false);
			hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
					panelBasicExt.jlrNAZNAMJ, false);
			hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
					panelDodatni.jrfNAZNAP, false);
			hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
					panelBasicExt.jtfPJOPIS, false);
		}
	}

	public void setDefValue() {
		if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 0
				|| TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 2) { ///sve
			// osim
			// izdatnica
			dm.getDefvaluedok().open();
			fDI.getMasterSet().setString(
					"CFRA",
					Aut.getAut().getDefaultValue(fDI.what_kind_of_dokument,
							"CFRA"));
			fDI.getMasterSet().setString(
					"CNACPL",
					Aut.getAut().getDefaultValue(fDI.what_kind_of_dokument,
							"CNACPL"));
			fDI.getMasterSet().setString(
					"CNAMJ",
					Aut.getAut().getDefaultValue(fDI.what_kind_of_dokument,
							"CNAMJ"));
			fDI.getMasterSet().setString(
					"CNAC",
					Aut.getAut().getDefaultValue(fDI.what_kind_of_dokument,
							"CNAC"));
			forceall_focuslost();
		}
	}

	public void MystateChanged(javax.swing.event.ChangeEvent e) {
		if (mainTab == null)
			return;
		if (fDI.raMaster.getMode() == 'I' || fDI.raMaster.getMode() == 'N') {
			if (mainTab.getSelectedIndex() == 0
					&& fDI.raMaster.getMode() == 'N') {
			  if (version == 7) {
			    panelZah.jrfCRADNIK.requestFocusLater();
			  } else if (version != 4) {
					if (fDI.raMaster.getMode() == 'N') {
					  panelBasic.jrfCPAR.requestFocusLater();
					} else if (fDI.raMaster.getMode() == 'I') {
	                   panelBasic.jtfDVO.requestFocusLater();
					}
				} else {
					panelRevers.jrfCPAR.requestFocusLater();

				}
			} else if (mainTab.getSelectedIndex() == 2) {
				if (fDI.getMasterSet().getString("CSHRAB").equals("")) {
					if (panelPopust != null) {
						panelPopust.RabShemaLinear(true);
						panelPopust.jtfUPRAB.requestFocus();
					}
				} else {
					if (panelPopust != null) {
						panelPopust.RabShemaLinear(false);
						panelPopust.jrfCSHRAB.requestFocus();
					}
				}
			}
		} else if (fDI.raMaster.getMode() == 'B') {
			if (mainTab.getSelectedIndex() == 2) {
				if (fDI.getMasterSet().getString("CSHRAB").equals("")) {
					if (panelPopust != null) {
						panelPopust.jrbLinearniRAB.setSelected(true);
						panelPopust.jrbShemaRAB.setSelected(false);
					}
				} else {
					if (panelPopust != null) {
						panelPopust.jrbLinearniRAB.setSelected(false);
						panelPopust.jrbShemaRAB.setSelected(true);
					}
				}
			}
		}
	}

	public void BindComp() {

		rajpBrDok.setDataSet(fDI.getMasterSet());

		if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 0
				|| TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 2
				|| TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 5) { ///sve
			// osim
			// izdatnica
			panelBasic.BindComp();
			panelBasicExt.BindComp();
			panelPopust.BindComp();
			panelOstatak.BindComp();
			panelDodatni.BindComp();
		}
		if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 1 || fDI.isDosIzd()) { ///
			// izdatnice
			panelBasic.BindComp();
			panelDodatni.BindComp();
		} else if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 3) {
			panelBasic.BindComp();
			panelDodatni.BindComp();
			panelBasicExt.BindComp();
		} else if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 6 && !fDI.isDosIzd()) { ///sve
			// osim
			// izdatnica
			panelBasic.BindComp();
            panelBasicExt.BindComp();
			panelDodatni.BindComp();
            panelOstatak.BindComp();
		} else if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 4) {
			panelRevers.BindComp();
			panelDodatni.BindComp();
		} else if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 7) {
          panelZah.BindComp();
          panelOstatak.BindComp();
          panelDodatni.BindComp();
        } else if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 8) {
          panelBasic.BindComp();
          panelOstatak.BindComp();
          panelDodatni.BindComp();
        }
	}

	public void forceall_focuslost() {

		if (TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 0
				|| TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 2
				|| TypeDoc.getTypeDoc().numberPanel(fDI.what_kind_of_dokument) == 5) { ///sve
			// osim
			// izdatnica

			panelBasicExt.jlrFRANKA.forceFocLost();
			panelBasicExt.jlrCNAMJ.forceFocLost();
			panelBasicExt.jlrCNACPL.forceFocLost();
			panelBasicExt.jlrCNAC.forceFocLost();
		}
	}

	public void EnabDisabifStavkeExist() {
	}

	class panBasic extends JPanel {

		int ver = -1;

		hr.restart.zapod.jpGetValute jpgetval = new hr.restart.zapod.jpGetValute();

		XYLayout xYLayout = new XYLayout();

		JLabel jlDATDOSP = new JLabel();

		JLabel jlCPAR = new JLabel();

		JLabel jlDATDOK = new JLabel();

		JLabel jlDDOSP = new JLabel();

		JLabel jlDVO = new JLabel();

		JraButton jdohvatDokumenta = new JraButton();

		JraButton jbCPAR = new JraButton();
		JraButton jbCPARCHECK = new JraButton();

		JlrNavField jrfCPAR = new JlrNavField() {
			public void after_lookUp() {
//System.out.println("jrfCPAR after_lookUp");           
                SwingUtilities.invokeLater(new Runnable(){

                    public void run() {
                        afterlooseCPAR();
                        fDI.after_lookUpCPAR();
                    }
    
                });
			}
		};

		JlrNavField jrfNAZPAR = new JlrNavField() {
			public void after_lookUp() {
//System.out.println("jrfNAZPAR after_lookUp");   
SwingUtilities.invokeLater(new Runnable(){

    public void run() {
        afterlooseCPAR();
        fDI.after_lookUpCPAR();
    }
    
        });
	
			}
		};

		void afterlooseCPAR() {
		}

		JraTextField jtfDATDOK = new JraTextField() {
          public void valueChanged() {
            fDI.jtfDATDOK_focusLost(null);
          }
        }; /// Datum dokumenta

		JraTextField jtfDDOSP = new JraTextField() {
          public void valueChanged() {
            fDI.jtfDDOSP_focusLost(null);
          }
        }; /// Dani dospije\u0107a

		JraTextField jtfDATDOSP = new JraTextField() {
          public void valueChanged() {
            fDI.jtfDATDOSP_focusLost(null);
          }
        }; /// Datum dospije\u0107a

		JraTextField jtfDVO = new JraTextField() {
          public void valueChanged() {
            fDI.jtfDVO_focusLost(null);
          }
        }; /// Datum dvo

		jpVlasnik rpku = new jpVlasnik();

		JraTextField jtfRN = new JraTextField();

		jpRadniNalog jpRN = new jpRadniNalog() {
			public void after_Cancel() {
				fDI.afterCancel();
			}

			public void after_OK() {
				fDI.afterOK();
			}
		};

		JlrNavField jrfCORG = new JlrNavField() {
			public void after_lookUp() {
				fDI.CORGafter_lookUp();
			}
		};

		JlrNavField jrfNAZORG = new JlrNavField();

		JlrNavField jrfCVRTR = new JlrNavField();

		JlrNavField jrfNAZVRTR = new JlrNavField();

		JraButton jbCORG = new JraButton();

		JraButton jbCVRTR = new JraButton();

		//    JraCheckBox chbock = new JraCheckBox("Rezervacija");
		JRadioButton jrbPartner = new JRadioButton("Partner");

		JRadioButton jrbKupac = new JRadioButton("Kupac");

		ButtonGroup jrgPartnerKupac = new ButtonGroup();

		public void partnerSelected(boolean isSelected) {
			if (!isSelected) {
				jrfCPAR.setText("");
				jrfCPAR.emptyTextFields();
                if (panelDodExt != null) {
                  panelBasicExt.jrfPJ.setText("");
                  panelBasicExt.jrfNAZPJ.setText("");
                  panelBasicExt.jtfPJOPIS.setText("");
                  rcc.EnabDisabAll(panelBasicExt, false);
                }
			}
			rcc.setLabelLaF(jrfCPAR, isSelected);
			rcc.setLabelLaF(jrfNAZPAR, isSelected);
			rcc.setLabelLaF(jbCPAR, isSelected);
			rcc.setLabelLaF(jbCPARCHECK, isSelected);
			if (isSelected) {
                if (panelDodExt != null) rcc.EnabDisabAll(panelBasicExt, true);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						jrfCPAR.requestFocus();
					}
				});
			}
		}

		public void enableAll(boolean isEnabled) {
			rcc.setLabelLaF(jrfCPAR, isEnabled);
			rcc.setLabelLaF(jrfNAZPAR, isEnabled);
			rcc.setLabelLaF(jbCPAR, isEnabled);
			rcc.setLabelLaF(jbCPARCHECK, isEnabled);
			rcc.EnabDisabAll(rpku, isEnabled);
		}

		public void kupacSelected(boolean isSelected) {

			if (!isSelected) {
				fDI.getMasterSet().setAssignedNull("CKUPAC");
				rpku.jraAdr.setText("");
				rpku.jraCkupac.setText("");
				rpku.jraEmadr.setText("");
				//				rpku.jlrMj.setText("");
				rpku.jraIme.setText("");
				rpku.jraPrezime.setText("");
				rpku.jraJmbg.setText("");
				//				rpku.jlrPbr.setText("");
				rpku.jraTel.setText("");
			}
			rcc.EnabDisabAll(rpku, isSelected);
			if (isSelected) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						rpku.jraCkupac.requestFocus();
					}
				});
			}
		}

		public void setupRadio() {
			jrgPartnerKupac.add(jrbPartner);
			jrgPartnerKupac.add(jrbKupac);
			jrbPartner.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					partnerSelected(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
			jrbKupac.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					kupacSelected(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}

		public void mjdohvatDokumenta() {
			fDI.keyActionMaster();
		}

		panBasic(int version) {

			ver = version;
			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					if (ver == 0 || ver == 1) {
						if (fDI.raMaster.getMode() == 'N') {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									jrfCPAR.requestFocus();
								}
							});
						} else if (fDI.raMaster.getMode() == 'I') {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									jtfDVO.requestFocus();
								}
							});
						}
					} else if (ver == 2) {
					}
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});
			setBorder(BorderFactory.createEtchedBorder());
			xYLayout.setWidth(647 + 5);
			setLayout(xYLayout);
			jpgetval.setTecajVisible(true);
			jpgetval.setTecajEditable(true);
			jlDDOSP.setText("Dani dospije\u0107a");
			jlDVO.setText("DVO");
			jlDATDOSP.setText("Datum dospije\u0107a");
			jlCPAR.setText("Partner");
			jrfCPAR.setColumnName("CPAR");
			jrfCPAR.setColNames(new String[] { "NAZPAR" });
			jrfCPAR.setVisCols(new int[] { 0, 1, 2 });
			jrfCPAR
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZPAR });
			jrfCPAR.setRaDataSet(dm.getPartneri());
			jrfNAZPAR.setColumnName("NAZPAR");
			jrfNAZPAR.setSearchMode(1);
			jrfNAZPAR.setNavProperties(jrfCPAR);
			jbCPAR.setText("...");
			jbCPARCHECK.setText("...");
			jrfCPAR.setNavButton(jbCPAR);

			jtfDATDOK.setColumnName("DATDOK");
			/*jtfDATDOK.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
					fDI.jtfDATDOK_focusLost(e);
				}
			});*/
			jtfDATDOSP.setColumnName("DATDOSP");

			/*jtfDATDOSP.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
					fDI.jtfDATDOSP_focusLost(e);
				}
			});*/

			jtfDDOSP.setColumnName("DDOSP");
			/*jtfDDOSP.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
					fDI.jtfDDOSP_focusLost(e);
				}
			});*/
			jlDATDOK.setText("Datum dokumenta");
			jtfDVO.setColumnName("DVO");
			/*jtfDVO.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
					fDI.jtfDVO_focusLost(e);
				}
			});*/

			jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
			jtfDVO.setHorizontalAlignment(SwingConstants.CENTER);
			jtfDATDOSP.setHorizontalAlignment(SwingConstants.CENTER);
			jpRN.setMode("I"); // za izdatnice, "R" za ra\u010Dune, "P" za
			// primke
			jpRN.setRastavljeno(true);
			jrfCORG.setColumnName("CORG");
			jrfCORG.setColNames(new String[] { "NAZIV" });
			jrfCORG.setVisCols(new int[] { 0, 1, 2 });
			jrfCORG
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZORG });
			jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
					.getOrgstrAndCurrKnjig());

			jrfNAZORG.setColumnName("NAZIV");
			jrfNAZORG.setSearchMode(1);
			jrfNAZORG.setNavProperties(jrfCORG);

			jbCORG.setText("...");
			jrfCORG.setNavButton(jbCORG);

			jrfCVRTR.setColumnName("CVRTR");
			jrfCVRTR.setColNames(new String[] { "NAZIV" });
			jrfCVRTR.setVisCols(new int[] { 0, 1, 2 });
			jrfCVRTR
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZVRTR });
			jrfCVRTR.setRaDataSet(dm.getVrtros());
			jrfNAZVRTR.setColumnName("NAZIV");
			jrfNAZVRTR.setSearchMode(1);
			jrfNAZVRTR.setNavProperties(jrfCVRTR);
			jbCVRTR.setText("...");
			jrfCVRTR.setNavButton(jbCVRTR);

			/*
			 * chbock.setHorizontalTextPosition(SwingConstants.LEFT );
			 * chbock.setColumnName("REZKOL"); chbock.setSelectedDataValue("D");
			 * chbock.setUnselectedDataValue("N");
			 */

			jdohvatDokumenta.setIcon(raImages
					.getImageIcon(raImages.IMGSENDMAIL));
			jdohvatDokumenta.setAutomaticFocusLost(true);
			jdohvatDokumenta.setToolTipText("Prijenos dokumenata");
			jdohvatDokumenta
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mjdohvatDokumenta();
						}
					});
			jbCPARCHECK
			.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showPromet();
				}
			});

			if ((version == 0 || version == 3) && !gotpar) {

				add(jlCPAR, new XYConstraints(15, 15, -1, -1));
				add(jrfCPAR, new XYConstraints(150, 15, 100, -1));
				add(jrfNAZPAR, new XYConstraints(254, 15, 350, -1));
				add(jbCPAR, new XYConstraints(612, 15, 21, 21));
				add(jbCPARCHECK, new XYConstraints(100, 15, 45, 21));
				add(jlDATDOK, new XYConstraints(15, 40, -1, -1));
				add(jtfDATDOK, new XYConstraints(150, 40, 100, -1));
				add(jlDVO, new XYConstraints(390, 40, -1, -1));
				add(jtfDVO, new XYConstraints(505, 40, 100, -1));
				add(jlDATDOSP, new XYConstraints(390, 65, -1, -1));
				add(jtfDATDOSP, new XYConstraints(505, 65, 100, -1));
				add(jdohvatDokumenta, new XYConstraints(612, 65, 21, 21));
				add(jlDDOSP, new XYConstraints(15, 65, -1, -1));
				add(jtfDDOSP, new XYConstraints(150, 65, 50, -1));
				add(jpgetval, new XYConstraints(0, 85, -1, -1));
			} else if (version == 1) {
				xYLayout.setHeight(120);
				jlDVO.setText("Mjesto troška");
				jlCPAR.setText("Vrsta troška");
				jlDATDOK.setText("Datum");
				add(jdohvatDokumenta, new XYConstraints(255, 75, 21, 21));
				add(jlDVO, new XYConstraints(15, 15, -1, -1));
				add(jlCPAR, new XYConstraints(15, 45, -1, -1));
				add(jrfCVRTR, new XYConstraints(150, 45, 100, -1));
				add(jrfNAZVRTR, new XYConstraints(255, 45, 350, -1));
				add(jbCVRTR, new XYConstraints(611, 45, 21, 21));
				add(jrfCORG, new XYConstraints(150, 15, 100, -1));
				add(jrfNAZORG, new XYConstraints(255, 15, 350, -1));
				add(jbCORG, new XYConstraints(611, 15, 21, 21));
				if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam(
						"robno", "useRadniNalozi", "D",
						"D ako se koriste radni nalozi inaèe slobodan upis"))) {
					add(jpRN, new XYConstraints(356, 75, 280, -1));
				} else {
					jtfRN.setColumnName("CRADNAL");
					add(new JLabel("Broj radnog naloga"), new XYConstraints(
							370, 75, -1, -1));
					add(jtfRN, new XYConstraints(505, 75, 100, -1));
				}

				add(jlDATDOK, new XYConstraints(15, 75, -1, -1));
				add(jtfDATDOK, new XYConstraints(150, 75, 100, -1));
			} else if (version == 2 && !gotpar) {
				add(rpku, new XYConstraints(0, 0, -1, -1));
				add(jlDATDOK, new XYConstraints(15, 115, -1, -1));
				add(jtfDATDOK, new XYConstraints(150, 115, 100, -1));
				add(jdohvatDokumenta, new XYConstraints(255, 115, 21, 21));
				add(jpgetval, new XYConstraints(0, 135, -1, -1));
			} else if (version == 5 || version == 8) {
				if (fDI.bPonudaZaKupca) {
					add(rpku, new XYConstraints(0, 0, -1, -1));
					add(jlDATDOK, new XYConstraints(15, 115, -1, -1));
					add(jtfDATDOK, new XYConstraints(150, 115, 100, -1));
					add(new JLabel("Dani važenja"), new XYConstraints(265, 115,
							-1, -1));
					add(jtfDDOSP, new XYConstraints(360, 115, 60, -1));
					add(new JLabel("Datum važenja"), new XYConstraints(435,
							115, -1, -1));
					add(jtfDATDOSP, new XYConstraints(540, 115, 100, -1));
					//          add(chbock,new XYConstraints(435, 135, 150, -1));
					add(jpgetval, new XYConstraints(0, 160, -1, -1));
				} else {
					add(jlCPAR, new XYConstraints(15, 15, -1, -1));
					add(jrfCPAR, new XYConstraints(150, 15, 100, -1));
					add(jrfNAZPAR, new XYConstraints(254, 15, 350, -1));
					add(jbCPAR, new XYConstraints(612, 15, 21, 21));
					add(jbCPARCHECK, new XYConstraints(100, 15, 45, 21));
					add(jlDATDOK, new XYConstraints(15, 40, -1, -1));
					add(jtfDATDOK, new XYConstraints(150, 40, 100, -1));
					add(new JLabel("Datum važenja"), new XYConstraints(400, 65,
							-1, -1));
					add(jtfDATDOSP, new XYConstraints(505, 65, 100, -1));
					add(new JLabel("Dani važenja"), new XYConstraints(15, 65,
							-1, -1));
					add(jdohvatDokumenta, new XYConstraints(612, 65, 21, 21));
					add(jtfDDOSP, new XYConstraints(150, 65, 50, -1));
					//          add(chbock,new XYConstraints(400, 85, 150, -1));
					add(jpgetval, new XYConstraints(0, 110, -1, -1));
				}
			} else if (version == 6 || (version == 2 || gotpar)) {
				setupRadio();
				add(jrbPartner, new XYConstraints(15, 15, -1, -1));
				add(jlCPAR, new XYConstraints(15, 40, -1, -1));
				add(jrfCPAR, new XYConstraints(150, 40, 100, -1));
				add(jrfNAZPAR, new XYConstraints(254, 40, 360, -1));
				add(jbCPAR, new XYConstraints(620, 40, 21, 21));
				add(jbCPARCHECK, new XYConstraints(100, 40, 45, 21));
				add(jrbKupac, new XYConstraints(15, 65, -1, -1));
				add(rpku, new XYConstraints(3, 90, -1, -1));
				add(jlDATDOK, new XYConstraints(15, 203, -1, -1));
				add(jtfDATDOK, new XYConstraints(150, 203, 100, -1));
				add(jdohvatDokumenta, new XYConstraints(255, 203, 21, 21));				
				xYLayout.setHeight(240);
			}
		}

		public void BindComp() {
			jrfCPAR.setDataSet(fDI.getMasterSet());
			jtfDATDOSP.setDataSet(fDI.getMasterSet());
			jtfDATDOK.setDataSet(fDI.getMasterSet());
			jtfDDOSP.setDataSet(fDI.getMasterSet());
			jtfDVO.setDataSet(fDI.getMasterSet());
			jpgetval.setRaDataSet(fDI.getMasterSet());
			jrfCVRTR.setDataSet(fDI.getMasterSet());
			jtfRN.setDataSet(fDI.getMasterSet());
			jrfCORG.setDataSet(fDI.getMasterSet());
			rpku.setDataSet(fDI.getMasterSet());
			//      chbock.setDataSet(fDI.getMasterSet());
		}
		  private void showPromet() {
			    int cpar = jrfCPAR.getDataSet().getInt("CPAR");
			    if (cpar == 0) return;
			    if (cpar==0) {
			      JOptionPane.showMessageDialog(null, "Nema upisanog partnera!",
			         "Poruka", JOptionPane.WARNING_MESSAGE);
			      return;
			    }
			    dtp.show(null, cpar,
			       "Promet kupca "+jrfCPAR.getDataSet().getInt("CPAR")+" ");
			  }
	}

	class panBasicExt extends JPanel {

		XYLayout xYLayout = new XYLayout();

		JLabel jlPJ = new JLabel();

		JLabel jlFRANK = new JLabel();

		JLabel jlNAMJ = new JLabel();

		JLabel jlNACPL = new JLabel();

		JLabel jlNACOTP = new JLabel();

		JraButton jbPJ = new JraButton();

		JraButton jbCFRA = new JraButton();

		JraButton jbCNACPL = new JraButton();

		JraButton jbCNAMJ = new JraButton();

		JraButton jbCNAC = new JraButton();

		JlrNavField jlrFRANKA = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZFRA.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZFRA = new JlrNavField();

		JlrNavField jlrCNAMJ = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZNAMJ.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZNAMJ = new JlrNavField();

		JlrNavField jlrCNACPL = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZNACPL.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZNACPL = new JlrNavField();

		JlrNavField jlrCNAC = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZNAC.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZNAC = new JlrNavField();

		JlrNavField jrfPJ = new JlrNavField() {

			public void after_lookUp() {
				fDI.after_lookUpPJ();
			}
		};

		JlrNavField jrfNAZPJ = new JlrNavField() {
			public void after_lookUp() {
				fDI.after_lookUpPJ();
			}
		};

		JraTextField jtfPJOPIS = new JraTextField();

		int ver = -1;

		panBasicExt(int version) {

			ver = version;
			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					if (ver == 0) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								jrfPJ.requestFocus();
							}
						});
					} else if (ver == 2) {
						jlrFRANKA.requestFocus();
					}
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});
            
            if (ver != 6) setBorder(BorderFactory.createEtchedBorder());
			setLayout(xYLayout);
			jrfPJ.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(FocusEvent e) {
					fDI.jrfPJ_focusGained(e);
				}
			});
			jlFRANK.setText("Paritet");
			jlNAMJ.setText("Namjena");
			jlNACPL.setText("Naèin plaæanja");
			jlNACOTP.setText("Otprema");
			jlPJ.setText("Poslovna jedinica");
			jrfPJ.setColumnName("PJ");
			jrfPJ.setColNames(new String[] { "NAZPJ" });
			jrfPJ.setVisCols(new int[] { 0, 1, 2 });
			jrfPJ.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZPJ });

			jrfNAZPJ.setColumnName("NAZPJ");
			jrfNAZPJ.setSearchMode(1);
			jrfNAZPJ.setNavProperties(jrfPJ);

			jbPJ.setText("...");
			jrfPJ.setNavButton(jbPJ);
            
            if (ver != 6) {
    			jlrFRANKA.setColumnName("CFRA");
    			jlrFRANKA.setVisCols(new int[] { 0, 1 });
    			jlrFRANKA.setColNames(new String[] { "NAZFRA" });
    			jlrFRANKA
    					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZFRA });
    			jlrFRANKA.setRaDataSet(dm.getFranka());
    
    			jlrNAZFRA.setColumnName("NAZFRA");
    			jlrNAZFRA.setSearchMode(1);
    			jlrNAZFRA.setNavProperties(jlrFRANKA);
    			jlrNAZFRA.setCaretPosition(0);
    
    			jbCFRA.setText("...");
    			jlrFRANKA.setNavButton(jbCFRA);
    			jlrCNAC.setColumnName("CNAC");
    			jlrCNAC.setVisCols(new int[] { 0, 1 });
    			jlrCNAC.setColNames(new String[] { "NAZNAC" });
    			jlrCNAC
    					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAC });
    			jlrCNAC.setRaDataSet(dm.getNacotp());
    
    			jlrNAZNAC.setColumnName("NAZNAC");
    			jlrNAZNAC.setSearchMode(1);
    			jlrNAZNAC.setNavProperties(jlrCNAC);
    			jlrNAZNAC.setCaretPosition(0);
    
    			jbCNAC.setText("...");
    			jlrCNAC.setNavButton(jbCNAC);
    			jlrCNACPL.setColumnName("CNACPL");
    			jlrCNACPL.setVisCols(new int[] { 0, 1 });
    			jlrCNACPL.setColNames(new String[] { "NAZNACPL" });
    			jlrCNACPL
    					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNACPL });
    			jlrCNACPL.setRaDataSet(dm.getNacpl());
    
    			jlrNAZNACPL.setColumnName("NAZNACPL");
    			jlrNAZNACPL.setSearchMode(1);
    			jlrNAZNACPL.setNavProperties(jlrCNACPL);
    			jlrNAZNACPL.setCaretPosition(0);
    
    			jbCNACPL.setText("...");
    			jlrCNACPL.setNavButton(jbCNACPL);
    			jlrCNAMJ.setColumnName("CNAMJ");
    			jlrCNAMJ.setVisCols(new int[] { 0, 1 });
    			jlrCNAMJ.setColNames(new String[] { "NAZNAMJ" });
    			jlrCNAMJ
    					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAMJ });
    			jlrCNAMJ.setRaDataSet(dm.getNamjena());
    
    			jlrNAZNAMJ.setColumnName("NAZNAMJ");
    			jlrNAZNAMJ.setSearchMode(1);
    			jlrNAZNAMJ.setNavProperties(jlrCNAMJ);
    			jlrNAZNAMJ.setCaretPosition(0);
    
    			jbCNAMJ.setText("...");
    			jlrCNAMJ.setNavButton(jbCNAMJ);
            }

			if (version == 0 || version == 5 || (version == 2 && gotpar)) {
				add(jlPJ, new XYConstraints(15, 15, -1, -1));
				add(jrfPJ, new XYConstraints(150, 15, 100, -1));
				add(jrfNAZPJ, new XYConstraints(254, 15, 351, -1));
				add(jbPJ, new XYConstraints(612, 15, 21, 21));
				add(jtfPJOPIS, new XYConstraints(150, 40, 455, -1));
				add(jlFRANK, new XYConstraints(15, 65, -1, -1));
				add(jlrFRANKA, new XYConstraints(150, 65, 30, -1));
				add(jlrNAZFRA, new XYConstraints(185, 65, 130, -1));
				add(jbCFRA, new XYConstraints(321, 65, 21, 21));
				add(jlNACOTP, new XYConstraints(370, 65, -1, -1));
				add(jlrCNAC, new XYConstraints(442, 65, 30, -1));
				add(jlrNAZNAC, new XYConstraints(476, 65, 130, -1));
				add(jbCNAC, new XYConstraints(612, 65, 21, 21));
				add(jlrNAZNACPL, new XYConstraints(185, 95, 130, -1));
				add(jlNACPL, new XYConstraints(15, 95, -1, -1));
				add(jlrCNACPL, new XYConstraints(150, 95, 30, -1));
				add(jbCNACPL, new XYConstraints(321, 95, 21, 21));
				add(jlNAMJ, new XYConstraints(370, 95, -1, -1));
				add(jlrCNAMJ, new XYConstraints(442, 95, 30, -1));
				add(jlrNAZNAMJ, new XYConstraints(476, 95, 130, -1));
				add(jbCNAMJ, new XYConstraints(612, 95, 21, 21));
			} else if (version == 2 && !gotpar) {
				add(jlFRANK, new XYConstraints(15, 15, -1, -1));
				add(jlrFRANKA, new XYConstraints(150, 15, 30, -1));
				add(jlrNAZFRA, new XYConstraints(185, 15, 130, -1));
				add(jbCFRA, new XYConstraints(321, 15, 21, 21));
				add(jlNACOTP, new XYConstraints(353, 15, -1, -1));
				add(jlrCNAC, new XYConstraints(442, 15, 30, -1));
				add(jlrNAZNAC, new XYConstraints(476, 15, 130, -1));
				add(jbCNAC, new XYConstraints(612, 15, 21, 21));
				add(jlrNAZNACPL, new XYConstraints(185, 40, 130, -1));
				add(jlNACPL, new XYConstraints(15, 40, -1, -1));
				add(jlrCNACPL, new XYConstraints(150, 40, 30, -1));
				add(jbCNACPL, new XYConstraints(321, 40, 21, 21));
				add(jlNAMJ, new XYConstraints(353, 40, -1, -1));
				add(jlrCNAMJ, new XYConstraints(442, 40, 30, -1));
				add(jlrNAZNAMJ, new XYConstraints(476, 40, 130, -1));
				add(jbCNAMJ, new XYConstraints(612, 40, 21, 21));
			} else if (version == 3 || version == 6) {
				add(jlPJ, new XYConstraints(15, 15, -1, -1));
				add(jrfPJ, new XYConstraints(150, 15, 100, -1));
				add(jrfNAZPJ, new XYConstraints(254, 15, 351, -1));
				add(jbPJ, new XYConstraints(612, 15, 21, 21));
				add(jtfPJOPIS, new XYConstraints(150, 40, 455, -1));
			}
		}

		public void BindComp() {
			jrfPJ.setDataSet(fDI.getMasterSet());
			jlrFRANKA.setDataSet(fDI.getMasterSet());
			jlrCNAC.setDataSet(fDI.getMasterSet());
			jlrCNACPL.setDataSet(fDI.getMasterSet());
			jlrCNAMJ.setDataSet(fDI.getMasterSet());

		}
	}

	/**
	 * 
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2002
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 * 
	 * @author unascribed
	 * @version 1.0
	 */

	class panPopust extends JPanel {

		JLabel jlUPRAB = new JLabel();

		JraRadioButton jrbLinearniRAB = new JraRadioButton();

		JraRadioButton jrbShemaRAB = new JraRadioButton();

		JraTextField jtfUPRAB = new JraTextField();
		
		JraTextField jtfUIU = new JraTextField();

		JraButton jbRabDetail = new JraButton();

		JLabel jlCSHRAB = new JLabel();

		raDPregRab rDPR = new raDPregRab(null, "Pregled rabata", true);

		JlrNavField jrfCSHRAB = new JlrNavField() {
			public void after_lookUp() {
				after_lookUpCSHRAB();
			}
		};

		JlrNavField jrfNAZSHRAB = new JlrNavField();

		hr.restart.swing.JraButton jbCSHRAB = new JraButton();

		XYLayout xYLayoutPopusti = new XYLayout();

		hr.restart.swing.JraButton jbAplayPopustOnStavke = new JraButton();

		panPopust() {

			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					jrbLinearniRAB.requestFocus();
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});
			setBorder(BorderFactory.createEtchedBorder());
			jlUPRAB.setText("%");
			jlCSHRAB.setText("Šifra sheme popusta");
			jrbLinearniRAB.setText("Linearni popust");
			jrbLinearniRAB
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jrbLinearniRAB_actionPerformed(e);
						}
					});
			jrbShemaRAB.setText("Popusti preko sheme popusta");
			jrbShemaRAB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jrbShemaRAB_actionPerformed(e);
				}
			});
			jbAplayPopustOnStavke.setText("Pop.stav");
			jbAplayPopustOnStavke
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jbAplayPopustOnStavke_actionPerformed(e);
						}
					});

			jtfUPRAB.setColumnName("UPRAB");
			jtfUIU.setColumnName("UIU");
			jrfCSHRAB.setColumnName("CSHRAB");
			jrfCSHRAB.setColNames(new String[] { "NSHRAB" });
			jrfCSHRAB.setVisCols(new int[] { 0, 1 });
			jrfCSHRAB
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZSHRAB });
			jrfCSHRAB.setRaDataSet(dm.getShrab());
			jrfCSHRAB.setNavButton(jbCSHRAB);
			jrfNAZSHRAB.setColumnName("NSHRAB");
			jrfNAZSHRAB.setNavProperties(jrfCSHRAB);
			jrfNAZSHRAB.setSearchMode(1);

			jbRabDetail.setText("Detalji");
			jbRabDetail.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jbRabDetail_actionPerformed(e);
				}
			});
			setLayout(xYLayoutPopusti);
			add(jrbLinearniRAB, new XYConstraints(15, 15, -1, -1));
//			add(jlUPRAB, new XYConstraints(367, 15, -1, -1));
//			add(jtfUPRAB, new XYConstraints(280, 15, 80, -1));
			add(jlUPRAB, new XYConstraints(262, 15, -1, -1));
			add(jtfUPRAB, new XYConstraints(175, 15, 80, -1));
			add(new JLabel("Uèešæe"), new XYConstraints(440, 15, -1, -1));
			add(jtfUIU, new XYConstraints(532, 15, 100, -1));

			add(jrbShemaRAB, new XYConstraints(15, 45, -1, -1));
			add(jlCSHRAB, new XYConstraints(32, 70, -1, -1));

			add(jrfCSHRAB, new XYConstraints(175, 70, 100, -1));
			add(jrfNAZSHRAB, new XYConstraints(280, 70, 241, -1));
			add(jbCSHRAB, new XYConstraints(526, 70, 21, 21));
			add(jbRabDetail, new XYConstraints(552, 70, 80, 21));
			add(jbAplayPopustOnStavke, new XYConstraints(552, 95, 80, 21));

		}

		public void BindComp() {
			jtfUPRAB.setDataSet(fDI.getMasterSet());
			jtfUIU.setDataSet(fDI.getMasterSet());
			jrfCSHRAB.setDataSet(fDI.getMasterSet());
		}

		public void jbRabDetail_actionPerformed(ActionEvent e) {
			rDPR.setUvjet(jrfCSHRAB.getText()); // ?
			rDPR.show();
		}

		public void after_lookUpCSHRAB() {

			if (!jrfCSHRAB.getText().equals("")) {
				fDI.getMasterSet().setBigDecimal("UPRAB",
						dm.getShrab().getBigDecimal("UPRAB"));
			} else if (jrbShemaRAB.isSelected()
					&& jrfCSHRAB.getText().equals("")) {
				fDI.getMasterSet().setBigDecimal("UPRAB", Nula);
			}

		}

		/////////////////////////////// R A B A T I
		// ///////////////////////////////////////////////
		public void RabShemaLinear(boolean linear) {

			jrbLinearniRAB.setSelected(linear);
			rcc.setLabelLaF(jtfUPRAB, linear);
			jrbShemaRAB.setSelected(!linear);
			rcc.setLabelLaF(jbRabDetail, !linear);
			rcc.setLabelLaF(jrfCSHRAB, !linear);
			rcc.setLabelLaF(jrfNAZSHRAB, !linear);
			rcc.setLabelLaF(jbCSHRAB, !linear);

		}

		public void jrbLinearniRAB_actionPerformed(ActionEvent e) {

			RabShemaLinear(true);
			jtfUPRAB.getDataSet().setBigDecimal(jtfUPRAB.getColumnName(), Nula);
			jrfCSHRAB.setText("");
			jrfCSHRAB.emptyTextFields();
			jtfUPRAB.requestFocus();

		}

		public void jbAplayPopustOnStavke_actionPerformed(ActionEvent e) {

			if (javax.swing.JOptionPane
					.showConfirmDialog(
							this,
							"Ovaj popust æe zamijeniti sve dosadašnje popuste na stavkama ovog dokumenta !!!! Nastaviti ?",
							"Upit", javax.swing.JOptionPane.YES_NO_OPTION,
							javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {
				fDI.popust4All();
			}
		}

		public void jrbShemaRAB_actionPerformed(ActionEvent e) {

			RabShemaLinear(false);
			jtfUPRAB.getDataSet().setBigDecimal(jtfUPRAB.getColumnName(), Nula);
			jrfCSHRAB.setText("");
			jrfCSHRAB.emptyTextFields();
			jrfCSHRAB.requestFocus();
		}
	}

	class panZavtr extends JPanel {

		raDPregZT rDPZT = new raDPregZT(null, "Pregled zavisnih troškova", true);

		// Layout
		XYLayout xYLayoutZT = new XYLayout();

		//JraRadioButton
		JraRadioButton jrbLinearniZT = new JraRadioButton();

		JraRadioButton jrbShemaZT = new JraRadioButton();

		//JLabel
		JLabel jlUPZT = new JLabel();

		JLabel jlCSHZT = new JLabel();

		//JraTextField
		JraTextField jtfUPZT = new JraTextField();

		//JlrNavField
		JlrNavField jrfCSHZT = new JlrNavField() {
			public void after_lookUp() {
				after_lookUpCSHZT();
			}
		};

		JraButton jbZtDetail = new JraButton();

		JlrNavField jrfNAZSHZT = new JlrNavField();

		JraButton jbCSHZT = new JraButton();

		panZavtr() {

			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					jrbLinearniZT.requestFocus();
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});
			setLayout(xYLayoutZT);
			setBorder(BorderFactory.createEtchedBorder());
			jlUPZT.setText("%");
			jlCSHZT.setText("Šifra sheme ZT");
			jrbLinearniZT.setText("Linearni zavisni troškovi");
			jrbLinearniZT
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							jrbLinearniZT_actionPerformed(e);
						}
					});

			jtfUPZT.setColumnName("UPZT");

			jrbShemaZT.setText("Linearni troškovi preko sheme ZT");
			jrbShemaZT.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jrbShemaZT_actionPerformed(e);
				}
			});

			jrfCSHZT.setColumnName("CSHZT");

			jrfCSHZT.setColNames(new String[] { "NSHZT" });
			jrfCSHZT.setVisCols(new int[] { 0, 1 });
			jrfCSHZT
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZSHZT });
			jrfCSHZT.setRaDataSet(dm.getShzavtr());
			jrfCSHZT.setNavButton(jbCSHZT);

			jrfNAZSHZT.setColumnName("NSHZT");
			jrfNAZSHZT.setSearchMode(1);
			jrfNAZSHZT.setNavProperties(jrfCSHZT);

			jbZtDetail.setText("Detalji");
			jbZtDetail.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jbZtDetail_actionPerformed(e);
				}
			});

			add(jrbLinearniZT, new XYConstraints(15, 15, -1, -1));
			add(jlUPZT, new XYConstraints(367, 15, -1, -1));
			add(jtfUPZT, new XYConstraints(280, 15, 80, -1));

			add(jrbShemaZT, new XYConstraints(15, 45, -1, -1));
			add(jlCSHZT, new XYConstraints(32, 70, -1, -1));
			add(jrfCSHZT, new XYConstraints(175, 70, 100, -1));
			add(jrfNAZSHZT, new XYConstraints(280, 70, 241, -1));
			add(jbCSHZT, new XYConstraints(526, 70, 21, 21));
			add(jbZtDetail, new XYConstraints(552, 70, 80, 21));

		}

		public void ZTShemaLinear(boolean linear) {

			jrbLinearniZT.setSelected(linear);
			jrbShemaZT.setSelected(!linear);
			rcc.setLabelLaF(jtfUPZT, linear);
			rcc.setLabelLaF(jbZtDetail, !linear);
			rcc.setLabelLaF(jrfCSHZT, !linear);
			rcc.setLabelLaF(jrfNAZSHZT, !linear);
			rcc.setLabelLaF(jbCSHZT, !linear);

		}

		public void jrbLinearniZT_actionPerformed(ActionEvent e) {

			ZTShemaLinear(true);
			jtfUPZT.getDataSet().setBigDecimal(jtfUPZT.getColumnName(), Nula);
			jrfCSHZT.setText("");
			jrfCSHZT.emptyTextFields();
			jtfUPZT.requestFocus();

		}

		public void jrbShemaZT_actionPerformed(ActionEvent e) {
			ZTShemaLinear(false);
			jtfUPZT.getDataSet().setBigDecimal(jtfUPZT.getColumnName(), Nula);
			jrfCSHZT.setText("");
			jrfCSHZT.emptyTextFields();
			jrfCSHZT.requestFocus();
		}

		public void after_lookUpCSHZT() {

			if (!jrfCSHZT.getText().equals("")) {
				fDI.getMasterSet().setBigDecimal("UPZT",
						dm.getShzavtr().getBigDecimal("ZTPUK"));
			} else if (jrbShemaZT.isSelected() && jrfCSHZT.getText().equals("")) {
				fDI.getMasterSet().setBigDecimal("UPZT", Nula);
			}
		}

		public void jbZtDetail_actionPerformed(ActionEvent e) {
			rDPZT.setUvjet(jrfCSHZT.getText());
			rDPZT.show();
		}

		public void BindComp() {
			jtfUPZT.setDataSet(fDI.getMasterSet());
			jrfCSHZT.setDataSet(fDI.getMasterSet());
		}
	}

	class panOstatak extends JPanel {

		XYLayout xYLayoutDODPOD = new XYLayout();

		//  java.util.ResourceBundle res =
		// java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
		JLabel jlIZDOK = new JLabel();

		JLabel jlBRNAR = new JLabel();

		JLabel jlBRUGO = new JLabel();

		JLabel jlBRPRED = new JLabel();

		JraTextField jraBRDOKIZ = new JraTextField();

		JraTextField jraBRNARIZ = new JraTextField();

		JraTextField jraCUG = new JraTextField();

		JraTextField jraBRPRD = new JraTextField();

		JLabel jlIZDOK1 = new JLabel();

		JLabel jlIZDOK2 = new JLabel();

		JLabel jlIZDOK3 = new JLabel();

		JLabel jlIZDOK4 = new JLabel();

		JraTextField jraDATDOKIZ = new JraTextField();

		JraTextField jraDATNARIZ = new JraTextField();

		JraTextField jraDATUG = new JraTextField();

		JraTextField jraDATPRD = new JraTextField();
		
		JraButton jbDod = new JraButton();

		JraTextField jraFBR = new JraTextField();
		JLabel jlFBR = new JLabel();
		

		panOstatak() {

			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					jraBRDOKIZ.requestFocus();
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});
			setLayout(xYLayoutDODPOD);
			setBorder(BorderFactory.createEtchedBorder());

			jlIZDOK.setText("Izlazni dokument");
			jlBRNAR.setText("Broj narudžbe");
			jlBRUGO.setText("Broj ugovora");
			jlBRPRED.setText("Broj predra\u010Duna");
			jraBRDOKIZ.setColumnName("BRDOKIZ");
			jraBRNARIZ.setColumnName("BRNARIZ");
			jraCUG.setColumnName("CUG");
			jraBRPRD.setColumnName("BRPRD");
			jlIZDOK1.setText("Datum izlaznog dokumenta");
			jlIZDOK2.setText("Datum narudžbe");
			jlIZDOK3.setText("Datum ugovora");
			jlIZDOK4.setText("Datum predra\u010Duna");
			jbDod.setText("...");
			jbDod.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                fDI.showDod();
              }            
            });
			jraDATDOKIZ.setColumnName("DATDOKIZ");
			jraDATDOKIZ.setHorizontalAlignment(SwingConstants.CENTER);
			jraDATNARIZ.setColumnName("DATNARIZ");
			jraDATNARIZ.setHorizontalAlignment(SwingConstants.CENTER);
			jraDATUG.setColumnName("DATUG");
			jraDATUG.setHorizontalAlignment(SwingConstants.CENTER);
			jraDATPRD.setColumnName("DATPRD");
			jraDATPRD.setHorizontalAlignment(SwingConstants.CENTER);
			jraFBR.setColumnName("FBR");
			jlFBR.setText("Broj fiskalnog RN");
			System.err.println("jraFBR.setVisible");
			boolean v = repFISBIH.isFISBIH()&&frmParam.getParam("robno", "FBRenabled", "N", "Omoguciti rucni unos fiskalnog broja na dokumentima").equalsIgnoreCase("D");
			jraFBR.setVisible(v);
			jlFBR.setVisible(v);

			add(jlIZDOK, new XYConstraints(15, 15, -1, -1));
			add(jraBRDOKIZ, new XYConstraints(130, 15, 110, -1));
			add(jbDod, new XYConstraints(245, 15, 42, 21));
			add(jlIZDOK1, new XYConstraints(365, 15, -1, -1));
			add(jraDATDOKIZ, new XYConstraints(522, 15, 110, -1));

			add(jlBRNAR, new XYConstraints(15, 40, -1, -1));
			add(jraBRNARIZ, new XYConstraints(130, 40, 110, -1));
			add(jlIZDOK2, new XYConstraints(365, 40, -1, -1));
			add(jraDATNARIZ, new XYConstraints(522, 40, 110, -1));

			add(jlBRUGO, new XYConstraints(15, 65, -1, -1));
			add(jraCUG, new XYConstraints(130, 65, 110, -1));
			add(jlIZDOK3, new XYConstraints(365, 65, -1, -1));
			add(jraDATUG, new XYConstraints(522, 65, 110, -1));

			add(jlBRPRED, new XYConstraints(15, 90, -1, -1));
			add(jraBRPRD, new XYConstraints(130, 90, 110, -1));
			add(jlIZDOK4, new XYConstraints(365, 90, -1, -1));
			add(jraDATPRD, new XYConstraints(522, 90, 110, -1));
			
      add(jlFBR, new XYConstraints(15, 115, -1, -1));
      add(jraFBR, new XYConstraints(130, 115, 110, -1));
		}

		public void BindComp() {
			jraBRDOKIZ.setDataSet(fDI.getMasterSet());
			jraBRNARIZ.setDataSet(fDI.getMasterSet());
			jraCUG.setDataSet(fDI.getMasterSet());
			jraBRPRD.setDataSet(fDI.getMasterSet());
			jraDATDOKIZ.setDataSet(fDI.getMasterSet());
			jraDATNARIZ.setDataSet(fDI.getMasterSet());
			jraDATUG.setDataSet(fDI.getMasterSet());
			jraDATPRD.setDataSet(fDI.getMasterSet());
			jraFBR.setDataSet(fDI.getMasterSet());
		}
	}

	class panDodatni extends JPanel {

		XYLayout xYLayoutNeki = new XYLayout();

		JLabel jlOPIS = new JLabel();

		JraTextField jraOPIS = new JraTextField();

		JraButton jbOPIS = new JraButton();

		JraButton jbCNAP = new JraButton();

		JLabel jlKontaktOsoba = new JLabel("Kontakt osoba");

		JraButton jbKO = new JraButton();

		JlrNavField jrfKO = new JlrNavField();

		JlrNavField jrfNAZKO = new JlrNavField();

		JLabel jlAgent = new JLabel("Agent");

		JraButton jbAgent = new JraButton();

		JlrNavField jrfAgent = new JlrNavField();

		JlrNavField jrfNAZAgent = new JlrNavField();

		JLabel jlZiro = new JLabel("ŽR");

		JraButton jbZiro = new JraButton();

		JlrNavField jrfZiro = new JlrNavField();
		
		JraCheckBox jcbAktivan = new JraCheckBox();

		//    JlrNavField jrfTMPZiro = new JlrNavField();

		JLabel jlCNAP = new JLabel();

		JlrNavField jrfCNAP = new JlrNavField() {
			public void after_lookUp() {
				jrfNAZNAP.setCaretPosition(0);
			}
		};

		JlrNavField jrfNAZNAP = new JlrNavField();
		
		JLabel jlIDPART = new JLabel("ID_partner");
		JraTextField jraIDPART = new JraTextField();
		
		

		panDodatni() {

			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					jraOPIS.requestFocus();
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});

			setLayout(xYLayoutNeki);
			setBorder(BorderFactory.createEtchedBorder());
			jraOPIS.setColumnName("OPIS");
			jraIDPART.setColumnName("CRADNIK");
			jlOPIS.setText("Opis");
			jlCNAP.setText("Napomena");
			jrfCNAP.setRaDataSet(dm.getNapomene());
			jrfCNAP.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZNAP });
			jrfCNAP.setVisCols(new int[] { 0, 1 });
			jrfCNAP.setColNames(new String[] { "NAZNAP" });
			jrfCNAP.setColumnName("CNAP");

			jrfNAZNAP.setNavProperties(jrfCNAP);
			jrfNAZNAP.setSearchMode(1);
			jrfNAZNAP.setColumnName("NAZNAP");

			jbCNAP.setText("...");
			jrfCNAP.setNavButton(jbCNAP);

			jrfKO.setRaDataSet(dm.getKosobe());
			jrfKO
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZKO });
			jrfKO.setVisCols(new int[] { 2, 3 });
			jrfKO.setColNames(new String[] { "IME" });
			jrfKO.setColumnName("CKO");

			jbKO.setText("...");
			jrfKO.setNavButton(jbKO);

			jrfNAZKO.setSearchMode(1);
			jrfNAZKO.setColumnName("IME");
			jrfNAZKO.setNavProperties(jrfKO);

			jrfAgent.setRaDataSet(dm.getAllAgenti());
			jrfAgent
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZAgent });
			jrfAgent.setVisCols(new int[] { 0, 1 });
			jrfAgent.setColNames(new String[] { "NAZAGENT" });
			jrfAgent.setColumnName("CAGENT");

			jrfNAZAgent.setSearchMode(1);
			jrfNAZAgent.setColumnName("NAZAGENT");
			jrfNAZAgent.setNavProperties(jrfAgent);

			jbAgent.setText("...");
			jrfAgent.setNavButton(jbAgent);
			jbOPIS.addActionListener(new java.awt.event.ActionListener() {
		       public void actionPerformed(ActionEvent e) {
					pressOpis();
				}

			});

			jrfZiro.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(
					OrgStr.getKNJCORG(false)));
			OrgStr.getOrgStr().addKnjigChangeListener(
					new raKnjigChangeListener() {
						public void knjigChanged(String oo, String no) {
							jrfZiro.setRaDataSet(OrgStr.getOrgStr()
									.getKnjigziro(OrgStr.getKNJCORG(false)));
						}
					});
			//      jrfZiro.setTextFields(new javax.swing.text.JTextComponent[]
			// {jrfTMPZiro});
			jrfZiro.setVisCols(new int[] { 1 });
			//      jrfZiro.setColNames(new String[] {"CORG"});
			jrfZiro.setColumnName("ZIRO");
			jbZiro.setText("...");
			jrfZiro.setNavButton(jbZiro);

			//      jrfTMPZiro.setSearchMode(1);
			//      jrfTMPZiro.setColumnName("CORG");
			//      jrfTMPZiro.setNavProperties(jrfZiro);

			jbOPIS.setText("...");

			add(jraOPIS, new XYConstraints(150, 15, 456, -1));
			add(jbOPIS, new XYConstraints(612, 15, 21, 21));
			add(jlOPIS, new XYConstraints(15, 15, -1, -1));
			add(jlCNAP, new XYConstraints(15, 40, -1, -1));
			add(jrfCNAP, new XYConstraints(150, 40, 95, -1));
			add(jrfNAZNAP, new XYConstraints(250, 40, 356, -1));
			add(jbCNAP, new XYConstraints(612, 40, 21, 21));
			add(jlKontaktOsoba, new XYConstraints(15, 65, -1, -1));
			add(jrfKO, new XYConstraints(150, 65, 95, -1));
			add(jrfNAZKO, new XYConstraints(250, 65, 356, -1));
			add(jbKO, new XYConstraints(612, 65, 21, 21));
			add(jlAgent, new XYConstraints(15, 90, -1, -1));
			add(jrfAgent, new XYConstraints(150, 90, 95, -1));
			add(jrfNAZAgent, new XYConstraints(250, 90, 356, -1));
			add(jbAgent, new XYConstraints(612, 90, 21, 21));

			add(jlZiro, new XYConstraints(15, 115, -1, -1));
			add(jrfZiro, new XYConstraints(150, 115, 150, -1));
			add(jbZiro, new XYConstraints(612, 115, 21, 21));
			
			if (fDI.hideKup) {
			  jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
			  jcbAktivan.setHorizontalTextPosition(SwingConstants.LEFT);
			  jcbAktivan.setText("Sakrij kupca");
			  jcbAktivan.setColumnName("AKTIV");
			  jcbAktivan.setSelectedDataValue("N");
			  jcbAktivan.setUnselectedDataValue("D");
			  jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
			  add(jcbAktivan, new XYConstraints(350, 140, 256, -1));
			}
			
			if (frmParam.getParam("robno","idpartner","N","ID parner instaliran na dokumentima").
					equalsIgnoreCase("D")) {
			add(jlIDPART, new XYConstraints(15, 140, -1, -1));
			add(jraIDPART, new XYConstraints(150, 140, 150, -1));
			xYLayoutNeki.setHeight(170);
			} else {
				xYLayoutNeki.setHeight(140);	
			}


		}

		public void pressOpis() {
			frmDodatniTxt dtx = new frmDodatniTxt() {
				public void stoakojesnimio(QueryDataSet vtt) {
					vtsnimio(vtt);
				}

				public void stoakonijesnimio(QueryDataSet vtt) {
					vtNijesnimio(vtt);
				}
			};
			dtx.setDodatnaNapomena(true);

			if (fDI.vttextzag == null) {
				dtx.setUP(fDI.raMaster.getWindow(), fDI.getMasterSet(),
						fDI.raDetail.getLocation());
			} else {
				dtx.setUP(fDI.raMaster.getWindow(), fDI.getMasterSet(),
						fDI.raDetail.getLocation(), fDI.vttextzag);
			}

		}

		public void vtNijesnimio(QueryDataSet vtt) {

		}

		public void vtsnimio(QueryDataSet vtt) {
//			System.out.println("VTSNIMIJO");
			fDI.vttextzag = vtt;
			ST.prn(fDI.vttextzag);
			fDI.isVTtextzag = true;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					jrfCNAP.requestFocus();
					jrfCNAP.selectAll();
				}
			});
		}

		public void setupKO() {

			jrfKO.setRaDataSet(hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * from kosobe where cpar="
							+ fDI.getMasterSet().getInt("CPAR"), true));
			jrfNAZKO.setNavProperties(jrfKO);
			if (jrfKO.getRaDataSet().getRowCount() == 0) {
				//cistoca je pola zdravlja
				jrfKO.setText("");
				jrfKO.forceFocLost();
				//        jrfKO.emptyTextFields();
			}
		}

		public void BindComp() {
			jraOPIS.setDataSet(fDI.getMasterSet());
			jrfCNAP.setDataSet(fDI.getMasterSet());
			jrfKO.setDataSet(fDI.getMasterSet());
			jrfAgent.setDataSet(fDI.getMasterSet());
			jrfZiro.setDataSet(fDI.getMasterSet());
			jcbAktivan.setDataSet(fDI.getMasterSet());
			jraIDPART.setDataSet(fDI.getMasterSet());
		}
	}
    
    class panDodatniExt extends JPanel {

        XYLayout lay = new XYLayout();
        
        panDodatniExt() {
          setLayout(lay);
          lay.setWidth(640);
          lay.setHeight(panelDodatni.xYLayoutNeki.getHeight() + 70);
          setBorder(BorderFactory.createEtchedBorder());
          panelBasicExt.xYLayout.setHeight(70);
          panelBasicExt.xYLayout.setWidth(640);
          panelDodatni.setBorder(null);
          add(panelBasicExt, new XYConstraints(0, 0, -1, -1));
          add(panelDodatni, new XYConstraints(0, 70, -1, -1));
        }
    }
    
    class panZah extends JPanel {
      XYLayout lay = new XYLayout();
      
      JLabel jlCORG = new JLabel("Org. jedinica");
      
      JlrNavField jrfCORG = new JlrNavField() {
        public void after_lookUp() {
            fDI.CORGafter_lookUp();
        }
      };

      JlrNavField jrfNAZORG = new JlrNavField();
      
      JraButton jbCORG = new JraButton();
      
      JLabel jlCRADNIK = new JLabel();

      JlrNavField jrfCRADNIK = new JlrNavField() {
          public void after_lookUp() {
              jrfIMERAD.setCaretPosition(0);
              jrfPREZRAD.setCaretPosition(0);
          }
      };

      JlrNavField jrfIMERAD = new JlrNavField();

      JlrNavField jrfPREZRAD = new JlrNavField();

      JraButton jbCRADNIK = new JraButton();
      
      JLabel jlDATDOK = new JLabel();

      JraTextField jtfDATDOK = new JraTextField() {
        public void valueChanged() {
          fDI.findBRDOK();
        }
      };
      
      JLabel jlDATDOSP = new JLabel();

      JraTextField jtfDATDOSP = new JraTextField() {
        public void valueChanged() {
          //
        }
      };
      
      public panZah() {
        addAncestorListener(new AncestorListener() {
          public void ancestorAdded(AncestorEvent e) {
              // focus
          }

          public void ancestorMoved(AncestorEvent e) {
          }

          public void ancestorRemoved(AncestorEvent e) {
          }
        });
        
        setLayout(lay);
        lay.setWidth(647);
        lay.setHeight(105);
        setBorder(BorderFactory.createEtchedBorder());
        jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
        /*jtfDATDOK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                fDI.findBRDOK();
            }
        });*/

        jlCRADNIK.setText("Naruèitelj");
        jrfCRADNIK.setRaDataSet(dm.getRadnici());
        jrfCRADNIK.setTextFields(new javax.swing.text.JTextComponent[] {
                jrfIMERAD, jrfPREZRAD });
        jrfCRADNIK.setVisCols(new int[] { 0, 1 });
        jrfCRADNIK.setColNames(new String[] { "IME", "PREZIME" });
        jrfCRADNIK.setColumnName("CRADNIK");

        jrfIMERAD.setNavProperties(jrfCRADNIK);
        jrfIMERAD.setSearchMode(1);
        jrfIMERAD.setColumnName("IME");
        jrfPREZRAD.setNavProperties(jrfCRADNIK);
        jrfPREZRAD.setSearchMode(1);
        jrfPREZRAD.setColumnName("PREZIME");

        jbCRADNIK.setText("...");
        jrfCRADNIK.setNavButton(jbCRADNIK);

        jrfCORG.setColumnName("CORG");
        jrfCORG.setColNames(new String[] { "NAZIV" });
        jrfCORG.setVisCols(new int[] { 0, 1, 2 });
        jrfCORG
                .setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZORG });
        jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
                .getOrgstrAndCurrKnjig());
        hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
                new hr.restart.zapod.raKnjigChangeListener() {
                    public void knjigChanged(String oldKnjig,
                            String newKnjig) {
                        jrfCORG.getRaDataSet().refresh();
                        jrfNAZORG.getRaDataSet().refresh();
                    }
                });

        jrfNAZORG.setColumnName("NAZIV");
        jrfNAZORG.setSearchMode(1);
        jrfNAZORG.setNavProperties(jrfCORG);
        jrfCORG.setNavButton(jbCORG);
        jlDATDOK.setText("Datum");
        jtfDATDOK.setColumnName("DATDOK");
        jlDATDOSP.setText("Rok isporuke");
        jtfDATDOSP.setColumnName("DATDOSP");
        
        add(jlCORG, new XYConstraints(15, 15, -1, -1));
        add(jrfCORG, new XYConstraints(150, 15, 100, -1));
        add(jrfNAZORG, new XYConstraints(255, 15, 350, -1));
        add(jbCORG, new XYConstraints(612, 15, 21, 21));
        add(jlCRADNIK, new XYConstraints(15, 40, -1, -1));
        add(jrfCRADNIK, new XYConstraints(150, 40, 100, -1));
        add(jrfIMERAD, new XYConstraints(255, 40, 173, -1));
        add(jrfPREZRAD, new XYConstraints(433, 40, 172, -1));
        add(jbCRADNIK, new XYConstraints(610, 40, 21, 21));
        add(jlDATDOK, new XYConstraints(15, 70, -1, -1));
        add(jtfDATDOK, new XYConstraints(150, 70, 100, -1));
        add(jlDATDOSP, new XYConstraints(350, 70, -1, -1));
        add(jtfDATDOSP, new XYConstraints(505, 70, 100, -1));
      }
      
      public void BindComp() {
        jtfDATDOK.setDataSet(fDI.getMasterSet());
        jtfDATDOSP.setDataSet(fDI.getMasterSet());
        jrfCRADNIK.setDataSet(fDI.getMasterSet());
        jrfCORG.setDataSet(fDI.getMasterSet());
      }
    }

	class panRevers extends JPanel {

		XYLayout xYLayoutRev = new XYLayout();

		JLabel jlCPAR = new JLabel();

		JLabel jlCORG = new JLabel("Org. jedinica");

		JLabel jlDATDOK = new JLabel();

		JraTextField jtfDATDOK = new JraTextField() {
		  public void valueChanged() {
            fDI.findBRDOK();
		  }
        }; /// Datum dokumenta

		JraButton jbCPAR = new JraButton();
		JraButton jbCPARCHECK = new JraButton();

		JlrNavField jrfCPAR = new JlrNavField() {
			public void after_lookUp() {
				afterlooseCPAR();
				//      fDI.after_lookUpCPAR();
			}
		};

		JlrNavField jrfNAZPAR = new JlrNavField() {
			public void after_lookUp() {
				afterlooseCPAR();
				//      fDI.after_lookUpCPAR();
			}
		};

		JlrNavField jrfCORG = new JlrNavField() {
			public void after_lookUp() {
				//      fDI.CORGafter_lookUp();
			}
		};

		JlrNavField jrfNAZORG = new JlrNavField();

		JraButton jbCORG = new JraButton();

		JLabel jlCRADNIK = new JLabel();

		JlrNavField jrfCRADNIK = new JlrNavField() {
			public void after_lookUp() {
				jrfIMERAD.setCaretPosition(0);
				jrfPREZRAD.setCaretPosition(0);
			}
		};

		JlrNavField jrfIMERAD = new JlrNavField();

		JlrNavField jrfPREZRAD = new JlrNavField();

		JraButton jbCRADNIK = new JraButton();

		JraRadioButton jrbPartner = new JraRadioButton();

		JraRadioButton jrbCorg = new JraRadioButton();

		panRevers() {

			addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent e) {
					boolean isCORG = !fDI.getMasterSet().getString("CORG")
							.equals("");
					if (fDI.raMaster.getMode() == 'I') {
						if (isCORG) {
							jrbCorg.setSelected(true);
							jrbCorg_actionPerformed(null);
							return;
						}
					} else if (fDI.raMaster.getMode() == 'B') {
						if (isCORG) {
							jrbCorg.setSelected(true);
						} else {
							jrbPartner.setSelected(true);
						}
						return;
					}
					jrbPartner.setSelected(true);
					jrbPartner_actionPerformed(null);
				}

				public void ancestorMoved(AncestorEvent e) {
				}

				public void ancestorRemoved(AncestorEvent e) {
				}
			});

			jrbPartner.setText("Vanjski revers");
			jrbPartner.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jrbPartner_actionPerformed(e);
				}
			});

			jrbCorg.setText("Interni revers");
			jrbCorg.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jrbCorg_actionPerformed(e);
				}
			});

			setLayout(xYLayoutRev);
			setBorder(BorderFactory.createEtchedBorder());
			jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
			/*jtfDATDOK.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
					fDI.findBRDOK();
				}
			});*/

			jlCRADNIK.setText("Osoba");
			jrfCRADNIK.setRaDataSet(dm.getRadnici());
			jrfCRADNIK.setTextFields(new javax.swing.text.JTextComponent[] {
					jrfIMERAD, jrfPREZRAD });
			jrfCRADNIK.setVisCols(new int[] { 0, 1 });
			jrfCRADNIK.setColNames(new String[] { "IME", "PREZIME" });
			jrfCRADNIK.setColumnName("CRADNIK");

			jrfIMERAD.setNavProperties(jrfCRADNIK);
			jrfIMERAD.setSearchMode(1);
			jrfIMERAD.setColumnName("IME");
			jrfPREZRAD.setNavProperties(jrfCRADNIK);
			jrfPREZRAD.setSearchMode(1);
			jrfPREZRAD.setColumnName("PREZIME");

			jbCRADNIK.setText("...");
			jrfCRADNIK.setNavButton(jbCRADNIK);

			jrfCORG.setColumnName("CORG");
			jrfCORG.setColNames(new String[] { "NAZIV" });
			jrfCORG.setVisCols(new int[] { 0, 1, 2 });
			jrfCORG
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZORG });
			jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
					.getOrgstrAndCurrKnjig());
			hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
					new hr.restart.zapod.raKnjigChangeListener() {
						public void knjigChanged(String oldKnjig,
								String newKnjig) {
							jrfCORG.getRaDataSet().refresh();
							jrfNAZORG.getRaDataSet().refresh();
						}
					});

			jrfNAZORG.setColumnName("NAZIV");
			jrfNAZORG.setSearchMode(1);
			jrfNAZORG.setNavProperties(jrfCORG);
			jbCORG.setText("...");
			jrfCORG.setNavButton(jbCORG);
			jlDATDOK.setText("Datum");
			jtfDATDOK.setColumnName("DATDOK");
			jlCPAR.setText("Partner");
			jrfCPAR.setColumnName("CPAR");
			jrfCPAR.setColNames(new String[] { "NAZPAR" });
			jrfCPAR.setVisCols(new int[] { 0, 1, 2 });
			jrfCPAR
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZPAR });
			jrfCPAR.setRaDataSet(dm.getPartneri());
			jrfNAZPAR.setColumnName("NAZPAR");
			jrfNAZPAR.setSearchMode(1);
			jrfNAZPAR.setNavProperties(jrfCPAR);
			jbCPAR.setText("...");
			jbCPARCHECK.setText("...");
			jrfCPAR.setNavButton(jbCPAR);
			xYLayoutRev.setWidth(647);
			xYLayoutRev.setHeight(170);
			add(jrbPartner, new XYConstraints(15, 15, -1, -1));
			add(jlCPAR, new XYConstraints(32, 40, -1, -1));
			add(jrfCPAR, new XYConstraints(150, 40, 100, -1));
			add(jrfNAZPAR, new XYConstraints(255, 40, 350, -1));
			add(jbCPAR, new XYConstraints(612, 40, 21, 21));
			add(jbCPARCHECK, new XYConstraints(100, 40, 45, 21));
			add(jrbCorg, new XYConstraints(15, 65, -1, -1));
			add(jlCORG, new XYConstraints(32, 90, -1, -1));
			add(jrfCORG, new XYConstraints(150, 90, 100, -1));
			add(jrfNAZORG, new XYConstraints(255, 90, 350, -1));
			add(jbCORG, new XYConstraints(612, 90, 21, 21));
			add(jlCRADNIK, new XYConstraints(32, 115, -1, -1));
			add(jrfCRADNIK, new XYConstraints(150, 115, 100, -1));
			add(jrfIMERAD, new XYConstraints(255, 115, 173, -1));
			add(jrfPREZRAD, new XYConstraints(433, 115, 173, -1));
			add(jbCRADNIK, new XYConstraints(612, 115, 21, 21));
			add(jlDATDOK, new XYConstraints(15, 140, -1, -1));
			add(jtfDATDOK, new XYConstraints(150, 140, 100, -1));

		}

		public void BindComp() {
			jtfDATDOK.setDataSet(fDI.getMasterSet());
			jrfCRADNIK.setDataSet(fDI.getMasterSet());
			jrfCORG.setDataSet(fDI.getMasterSet());
			jrfCPAR.setDataSet(fDI.getMasterSet());
		}

		void afterlooseCPAR() {
		}

		public void jrbPartner_actionPerformed(ActionEvent e) {
			if (jrbPartner.isSelected()) {
				jrfCORG.setText("");
				jrfCORG.emptyTextFields();
				jrfCRADNIK.setText("");
				jrfCRADNIK.emptyTextFields();
				EnabDisab(true, false);
				jrbCorg.setSelected(false);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						jrfCPAR.requestFocus();
					}
				});
			}
		}

		public void jrbCorg_actionPerformed(ActionEvent e) {
			if (jrbCorg.isSelected()) {
				jrfCPAR.setText("");
				jrfCPAR.emptyTextFields();
				afterlooseCPAR();
				EnabDisab(false, true);
				jrfCORG.requestFocus();
				jrbPartner.setSelected(false);
			}
		}

		public void EnabDisab(boolean partner, boolean corg) {

			rcc.setLabelLaF(jrfCORG, corg);
			rcc.setLabelLaF(jrfNAZORG, corg);
			rcc.setLabelLaF(jbCORG, corg);
			rcc.setLabelLaF(jrfCRADNIK, corg);
			rcc.setLabelLaF(jrfIMERAD, corg);
			rcc.setLabelLaF(jrfPREZRAD, corg);
			rcc.setLabelLaF(jbCRADNIK, corg);
			rcc.setLabelLaF(jrfCPAR, partner);
			rcc.setLabelLaF(jrfNAZPAR, partner);
			rcc.setLabelLaF(jbCPAR, partner);
			rcc.setLabelLaF(jbCPARCHECK, partner);
		}
	}
}