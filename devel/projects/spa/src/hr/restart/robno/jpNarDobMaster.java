/****license*****************************************************************
**   file: jpNarDobMaster.java
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
import hr.restart.robno.rajpIzlazMPTemplate.panOstatak;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.jpGetValute;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpNarDobMaster extends JPanel {
	raCommonClass rcc = raCommonClass.getraCommonClass();

	dM dm = dM.getDataModule();

	frmNarDob fNarDob;

	jpOsnovni jp1 = new jpOsnovni();

	jpOstali jp2 = new jpOstali();
	
	panOstatak jp3 = new panOstatak();
	
	panDodatni jpd = new panDodatni();

	rajpBrDok brdok = new rajpBrDok();

	JTabbedPane tabs = new JTabbedPane() {
		public boolean isFocusTraversable() {
			return false;
		}
	};

	public jpNarDobMaster(frmNarDob md) {
		try {
			fNarDob = md;
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void BindComponents(DataSet ds) {
		jp1.BindComponents(ds);
		jp2.BindComponents(ds);
		jp3.BindComp(ds);
		jpd.BindComp(ds);
		brdok.setDataSet(ds);
	}

	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		tabs.add(jp1, "Osnovni podaci");
		tabs.add(jp2, "Dodatni podaci");
		tabs.add(jp3, "Ostali podaci");
		tabs.add(jpd, "Napomene i ostalo");

		brdok.addBorder();

		BindComponents(fNarDob.getMasterSet());
		/** @todo: Odkomentirati sljedeæu liniju :) */
		this.add(brdok, BorderLayout.NORTH);
		this.add(tabs, BorderLayout.CENTER);
	}

	class jpOsnovni extends JPanel {

		XYLayout lay = new XYLayout();

		JLabel jlAkcija = new JLabel();

		JLabel jlDaniz = new JLabel();

		JLabel jlDatdok = new JLabel();

		JLabel jlDatiz = new JLabel();

		JLabel jlIsp = new JLabel();

		JLabel jlPar = new JLabel();

		JLabel jlTem = new JLabel();

		JraButton jbSelCorg = new JraButton();

		JraButton jbSelCpar = new JraButton();

		raComboBox rcbAkcija = new raComboBox();
		raComboBox rcbUvjet = new raComboBox();

		JraTextField jraDaniz = new JraTextField() {
          public void valueChanged() {
            frmNarDob.getInstance().afterDaniz();
          }
        };

		JraTextField jraDatdok = new JraTextField() {
          public void valueChanged() {
            val.setTecajDate(jraDatdok.getDataSet().getTimestamp(
            "DATDOK"));
            frmNarDob.getInstance().afterDatdok();
          }
        };

		JraTextField jraDatiz = new JraTextField() {
          public void valueChanged() {
            frmNarDob.getInstance().afterDatiz();
          }
        };

		JraTextField jraTem = new JraTextField();

		JlrNavField jlrNaziv = new JlrNavField() {
			public void after_lookUp() {
			}
		};

		JlrNavField jlrCpar = new JlrNavField() {
			public void after_lookUp() {
				frmNarDob.getInstance().setPJ(false);
			}
		};

		JlrNavField jlrNazpar = new JlrNavField() {
			public void after_lookUp() {
				frmNarDob.getInstance().setPJ(false);
			}
		};

		JlrNavField jlrCorg = new JlrNavField() {
			public void after_lookUp() {
			}
		};

		jpGetValute val = new jpGetValute();

		public jpOsnovni() {
			try {
				jbInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void BindComponents(DataSet ds) {
			rcbAkcija.setRaDataSet(ds);
			rcbUvjet.setRaDataSet(ds);
			jraDaniz.setDataSet(ds);
			jraDatdok.setDataSet(ds);
			jraDatiz.setDataSet(ds);
			jraTem.setDataSet(ds);
			jlrCpar.setDataSet(ds);
			jlrCorg.setDataSet(ds);
			val.setRaDataSet(ds);
		}

		private void jbInit() throws Exception {
			setLayout(lay);
			lay.setWidth(646);
			lay.setHeight(185);

			val.setTecajVisible(true);
			val.setTecajEditable(true);

			jbSelCorg.setText("...");
			jbSelCpar.setText("...");
			jlAkcija.setText("Izvolite");
			jlDaniz.setText("Dani izvršenja");
			jlDatdok.setText("Datum");
			jlDatiz.setText("Datum izvršenja");
			jlIsp.setText("Isporuka na naslov");
			jlPar.setText("Dobavlja\u010D");
			jlTem.setText("Na temelju");
            
			rcbAkcija.setRaColumn("CSHZT");
			rcbAkcija.setRaItems(new String[][] {{ "", "0" }, { "izvolite dobaviti", "D" },
					{ "izvolite izraditi", "I" }, { "izvolite popraviti", "P" } });
            rcbUvjet.setRaColumn("CSHRAB");
            rcbUvjet.setRaItems(new String[][] {{ "", "0" }, { "Na temelju", "1" },
                    { "Za potrebe", "2" }});
            
			jraDaniz.setColumnName("DDOSP");
			jraDatdok.setColumnName("DATDOK");
			/*jraDatdok.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(FocusEvent e) {
					val.setTecajDate(jraDatdok.getDataSet().getTimestamp(
					"DATDOK"));
				}
			});*/
			
			jraDatiz.setColumnName("DATDOSP");
			jraTem.setColumnName("OPIS");

			jlrCpar.setColumnName("CPAR");
			jlrCpar.setColNames(new String[] { "NAZPAR" });
			jlrCpar.setTextFields(new JTextComponent[] { jlrNazpar });
			jlrCpar.setVisCols(new int[] { 0, 1 });
			jlrCpar.setSearchMode(0);
			jlrCpar.setRaDataSet(dm.getPartneri());
			jlrCpar.setNavButton(jbSelCpar);

			jlrNazpar.setColumnName("NAZPAR");
			jlrNazpar.setNavProperties(jlrCpar);
			jlrNazpar.setSearchMode(1);

			jlrCorg.setColumnName("CSKL");
			jlrCorg.setNavColumnName("CORG");
			jlrCorg.setColNames(new String[] { "NAZIV" });
			jlrCorg.setTextFields(new JTextComponent[] { jlrNaziv });
			jlrCorg.setVisCols(new int[] { 0, 1 });
			jlrCorg.setSearchMode(0);
			jlrCorg.setRaDataSet(dm.getOrgstruktura());
			jlrCorg.setNavButton(jbSelCorg);

			jlrNaziv.setColumnName("NAZIV");
			jlrNaziv.setNavProperties(jlrCorg);
			jlrNaziv.setSearchMode(1);

			add(jbSelCorg, new XYConstraints(610, 45, 21, 21));
			add(jbSelCpar, new XYConstraints(610, 20, 21, 21));
			//add(jlAkcija, new XYConstraints(460, 71, -1, -1));
			add(jlDaniz, new XYConstraints(265, 96, -1, -1));
			add(jlDatdok, new XYConstraints(15, 95, -1, -1));
			add(jlDatiz, new XYConstraints(410, 96, -1, -1));
			add(jlIsp, new XYConstraints(15, 45, -1, -1));
			add(jlPar, new XYConstraints(15, 20, -1, -1));
            
			add(rcbUvjet, new XYConstraints(15, 70, 120, -1));
            
			add(jlrCorg, new XYConstraints(150, 45, 100, -1));
			add(jlrCpar, new XYConstraints(150, 20, 100, -1));
			add(jlrNaziv, new XYConstraints(255, 45, 350, -1));
			add(jlrNazpar, new XYConstraints(255, 20, 350, -1));
			add(rcbAkcija, new XYConstraints(460, 70, 145, -1));
			add(jraDaniz, new XYConstraints(350, 95, 50, -1));
			add(jraDatdok, new XYConstraints(150, 95, 100, -1));
			add(jraDatiz, new XYConstraints(505, 95, 100, -1));
			add(jraTem, new XYConstraints(150, 70, 300, -1));
			add(val, new XYConstraints(0, 125, -1, -1));

			/*jraDatdok.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					frmNarDob.getInstance().afterDatdok();
				}
			});*/
			/*jraDaniz.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					frmNarDob.getInstance().afterDaniz();
				}
			});*/
			/*jraDatiz.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					frmNarDob.getInstance().afterDatiz();
				}
			});*/
		}
	}

	class jpOstali extends JPanel {

		XYLayout xYLayout = new XYLayout();

		JLabel jlPJ = new JLabel();

		//JLabel jlNAP = new JLabel();

		JLabel jlNACPL = new JLabel();

		JLabel jlNACOTP = new JLabel();

		//JraButton

		JraButton jbPJ = new JraButton();

		JraButton jbCNACPL = new JraButton();

		JraButton jbCNAC = new JraButton();

		/*JraButton jbNAP = new JraButton();

		JlrNavField jlrCNAP = new JlrNavField();

		JlrNavField jlrNAZNAP = new JlrNavField();*/

		JlrNavField jlrCNACPL = new JlrNavField();

		JlrNavField jlrNAZNACPL = new JlrNavField();

		JlrNavField jlrCNAC = new JlrNavField();

		JlrNavField jlrNAZNAC = new JlrNavField();

		JlrNavField jrfPJ = new JlrNavField() {
			public void after_lookUp() {
			}
		};

		JlrNavField jrfNAZPJ = new JlrNavField() {
			public void after_lookUp() {
			}
		};

		JraTextField jtfPJOPIS = new JraTextField();

		public jpOstali() {
			try {
				jbInit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void jbInit() throws Exception {
			/*
			 * addAncestorListener(new AncestorListener() { public void
			 * ancestorAdded(AncestorEvent e) { if (ver==0) {
			 * SwingUtilities.invokeLater(new Runnable() { public void run() {
			 * jrfPJ.requestFocus(); } }); } else if (ver == 2) {
			 * jlrFRANKA.requestFocus(); } } public void
			 * ancestorMoved(AncestorEvent e) {} public void
			 * ancestorRemoved(AncestorEvent e) {} });
			 */
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(xYLayout);

			jrfPJ.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(FocusEvent e) {
				}
			});

			//jlNAP.setText("Napomena");
			jlNACPL.setText("Naèin plaæanja");
			jlNACOTP.setText("Naèin otpreme");
			jlPJ.setText("Poslovna jedinica");

			///// DOHVAT I OBRADA PJ

			jrfPJ.setColumnName("PJ");
			jrfPJ.setColNames(new String[] { "NAZPJ" });
			jrfPJ.setVisCols(new int[] { 0, 1, 2 });
			jrfPJ
					.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZPJ });
			jrfPJ.setNavButton(jbPJ);

			jrfNAZPJ.setColumnName("NAZPJ");
			jrfNAZPJ.setSearchMode(1);
			jrfNAZPJ.setNavProperties(jrfPJ);

			/*jlrCNAP.setColumnName("CNAP");
			jlrCNAP.setVisCols(new int[] { 0, 1 });
			jlrCNAP.setColNames(new String[] { "NAZNAP" });
			jlrCNAP
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAP });
			jlrCNAP.setRaDataSet(dm.getNapomene());
			jlrCNAP.setNavButton(jbNAP);

			jlrNAZNAP.setColumnName("NAZNAP");
			jlrNAZNAP.setSearchMode(1);
			jlrNAZNAP.setNavProperties(jlrCNAP);*/

			/////// DOHVAT I OBRADA NA\u010CINA OTPREME

			jlrCNAC.setColumnName("CNAC");
			jlrCNAC.setVisCols(new int[] { 0, 1 });
			jlrCNAC.setColNames(new String[] { "NAZNAC" });
			jlrCNAC
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAC });
			jlrCNAC.setRaDataSet(dm.getNacotp());
			jlrCNAC.setNavButton(jbCNAC);

			jlrNAZNAC.setColumnName("NAZNAC");
			jlrNAZNAC.setSearchMode(1);
			jlrNAZNAC.setNavProperties(jlrCNAC);
			jlrNAZNAC.setCaretPosition(0);

			/////// DOHVAT I OBRADA NA\u010CINA PLA\u0106ANJA

			jlrCNACPL.setColumnName("CNACPL");
			jlrCNACPL.setVisCols(new int[] { 0, 1 });
			jlrCNACPL.setColNames(new String[] { "NAZNACPL" });
			jlrCNACPL
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNACPL });
			jlrCNACPL.setRaDataSet(dm.getNacpl());
			jlrCNACPL.setNavButton(jbCNACPL);

			jlrNAZNACPL.setColumnName("NAZNACPL");
			jlrNAZNACPL.setSearchMode(1);
			jlrNAZNACPL.setNavProperties(jlrCNACPL);
			jlrNAZNACPL.setCaretPosition(0);

			add(jlPJ, new XYConstraints(15, 20, -1, -1));
			add(jrfPJ, new XYConstraints(150, 20, 100, -1));
			add(jrfNAZPJ, new XYConstraints(255, 20, 350, -1));
			add(jbPJ, new XYConstraints(610, 20, 21, 21));
			add(jtfPJOPIS, new XYConstraints(150, 45, 455, -1));
			/*add(jlNAP, new XYConstraints(15, 70, -1, -1));
			add(jlrCNAP, new XYConstraints(150, 70, 100, -1));
			add(jlrNAZNAP, new XYConstraints(255, 70, 350, -1));
			add(jbNAP, new XYConstraints(610, 70, 21, 21));*/
			add(jlNACOTP, new XYConstraints(352, 100, -1, -1));
			add(jlrCNAC, new XYConstraints(440, 100, 30, -1));
			add(jlrNAZNAC, new XYConstraints(475, 100, 130, -1));
			add(jbCNAC, new XYConstraints(610, 100, 21, 21));
			add(jlrNAZNACPL, new XYConstraints(185, 100, 130, -1));
			add(jlNACPL, new XYConstraints(15, 100, -1, -1));
			add(jlrCNACPL, new XYConstraints(150, 100, 30, -1));
			add(jbCNACPL, new XYConstraints(320, 100, 21, 21));
		}

		public void BindComponents(DataSet ds) {
			jrfPJ.setDataSet(ds);
			//jlrCNAP.setDataSet(ds);
			jlrCNAC.setDataSet(ds);
			jlrCNACPL.setDataSet(ds);
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

      public void BindComp(DataSet ds) {
          jraBRDOKIZ.setDataSet(ds);
          jraBRNARIZ.setDataSet(ds);
          jraCUG.setDataSet(ds);
          jraBRPRD.setDataSet(ds);
          jraDATDOKIZ.setDataSet(ds);
          jraDATNARIZ.setDataSet(ds);
          jraDATUG.setDataSet(ds);
          jraDATPRD.setDataSet(ds);
          jraFBR.setDataSet(ds);
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
          jbOPIS.addMouseListener(new MouseAdapter() {
              public void mousePressed(MouseEvent e) {
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

          if (fNarDob.vttextzag == null) {
              dtx.setUP(this.getTopLevelAncestor(), fNarDob.getMasterSet(),
                  fNarDob.raDetail.getLocation());
          } else {
              dtx.setUP(this.getTopLevelAncestor(), fNarDob.getMasterSet(),
                  fNarDob.raDetail.getLocation(), fNarDob.vttextzag);
          }

      }

      public void vtNijesnimio(QueryDataSet vtt) {

      }

      public void vtsnimio(QueryDataSet vtt) {
//        System.out.println("VTSNIMIJO");
        fNarDob.vttextzag = vtt;
         // ST.prn(fDI.vttextzag);
        fNarDob.isVTtextzag = true;
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
                          + fNarDob.getMasterSet().getInt("CPAR"), true));
          jrfNAZKO.setNavProperties(jrfKO);
          if (jrfKO.getRaDataSet().getRowCount() == 0) {
              //cistoca je pola zdravlja
              jrfKO.setText("");
              jrfKO.forceFocLost();
              //        jrfKO.emptyTextFields();
          }
      }

      public void BindComp(DataSet ds) {
          jraOPIS.setDataSet(ds);
          jrfCNAP.setDataSet(ds);
          jrfKO.setDataSet(ds);
          jrfAgent.setDataSet(ds);
          jrfZiro.setDataSet(ds);
          jraIDPART.setDataSet(ds);
      }
  }
}
