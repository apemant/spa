/****license*****************************************************************
**   file: FrmCarPrimka.java
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
package hr.restart.cstd;

import hr.restart.baza.dM;
import hr.restart.robno.rapancartSimple;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raTransaction;

import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class FrmCarPrimka extends raMasterDetail {
    dM dm = dM.getDataModule();

    JCarinaPanelMaster jcpm = new JCarinaPanelMaster();

    JCarinaPanelDetail jcpd;
	hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
	raCommonClass rCC = raCommonClass.getraCommonClass();
    raTableColumnModifier TCM;	
    
    QueryDataSet repQDS = new QueryDataSet();
    
    private static FrmCarPrimka instanceOfMe = null;
    
    public static FrmCarPrimka getInstance(){
      if (instanceOfMe == null) instanceOfMe = new FrmCarPrimka();
      return instanceOfMe;
    }
    

    public FrmCarPrimka() {
        super(1, 2);
        
        TCM = new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
                "CPAR", "NAZPAR" }, new String[] { "CPAR" }, dm.getPartneri()) {
            public int getMaxModifiedTextLength() {
                return 27;
            }
        };
        dm.getCarulaz().open();
        dm.getCarulazst().open();
        jcpm.init();
        setNaslovMaster("Primka");
        setNaslovDetail("Stavke primke");
        setVisibleColsMaster(new int[] { 1,2,3 });
        setVisibleColsDetail(new int[] { 0,
        				1, 3, 5, 6, 10, 13,14 });
        raMaster.installSelectionTracker("ID_ULAZ_ZAG");
        java.lang.String [] key = { "ID_ULAZ_ZAG" };
        this.setMasterKey(key);
        this.setDetailKey(key);
        raMaster.getNavBar().getColBean().setSaveSettings(true);
        setMasterSet(dm.getCarulaz());
        setDetailSet(dm.getCarulazst());
        setJPanelMaster(jcpm);
        jcpd = new JCarinaPanelDetail();
//        jcpd.init();
        setJPanelDetail(jcpd);

        jcpm.Bind(dm.getCarulaz());
        jcpd.Bind(dm.getCarulazst());
        raMaster.getJpTableView().addTableModifier(TCM);
        this.raMaster.getRepRunner().addReport("hr.restart.cstd.RepCarPrimka","hr.restart.cstd.RepCarPrimka","CarinskaPrimka","Primka");
        this.raDetail.getRepRunner().addReport("hr.restart.cstd.RepCarPrimka","hr.restart.cstd.RepCarPrimka","CarinskaPrimka","Primka");
        instanceOfMe = this;
    }

    public boolean doBeforeSaveMaster(char mode) {
        if (mode != 'N')
            return true;
        int counter = Util.getNewQueryDataSet(
                "select max(ID_ULAZ_ZAG) as ID_ULAZ_ZAG from Carulaz").getInt(
                "ID_ULAZ_ZAG");
        getMasterSet().setInt("ID_ULAZ_ZAG", ++counter);
        return true;

    }

    public void SetFokusMaster(char mode) {
        jcpm.jpgetval.initJP(mode);
        if (mode =='N') {
            rCC.setLabelLaF(jcpm.jrtfCBRDOK,true);
//            rCC.setLabelLaF(jcpm.jrtfDATDOK,true);
            rCC.setLabelLaF(jcpm.jrtfCPAR,true);
            rCC.setLabelLaF(jcpm.jrtfNAZIVPAR,true);
            rCC.setLabelLaF(jcpm.jrtfCPARB,true);
            rCC.setLabelLaF(jcpm.jrtfCSKL,true);
            rCC.setLabelLaF(jcpm.jrtfCSKLB,true);

            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    jcpm.jrtfCBRDOK.requestFocus();
                }});


        } else if (mode =='I'){
            rCC.setLabelLaF(jcpm.jrtfCBRDOK,false);
//            rCC.setLabelLaF(jcpm.jrtfDATDOK,false);
            rCC.setLabelLaF(jcpm.jrtfCPAR,false);
            rCC.setLabelLaF(jcpm.jrtfNAZIVPAR,false);
            rCC.setLabelLaF(jcpm.jrtfCPARB,false);
            rCC.setLabelLaF(jcpm.jrtfCSKL,false);
            rCC.setLabelLaF(jcpm.jrtfCSKLB,false);

            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    jcpm.jrtfDATDOK.requestFocus();
                }});
        }
    }
    public void SetFokusDetail(char mode) {
        if (mode =='N') {
            rCC.EnabDisabAllLater(jcpd.rpcart,true);
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    jcpd.rpcart.jrfCART1.requestFocus();
                }});
        }else if (mode =='I'){
            rCC.EnabDisabAllLater(jcpd.rpcart,false);
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    jcpd.jlCTG.requestFocus();
                }});
            
        }
        
    }
    
	public void afterSetModeMaster(char oldm, char newm) {
		if (newm == 'B')
		    jcpm.jpgetval.disableDohvat();
	}

    public boolean doBeforeSaveDetail(char mode) {
        if (mode != 'N')
            return true;
        int counter = Util.getNewQueryDataSet(
                "select max(ID_ULAZ_STAV) as ID_ULAZ_STAV from Carulazst")
                .getInt("ID_ULAZ_STAV");
        short rbr = Util.getNewQueryDataSet(
                "select max(rbr) as rbr from Carulazst where "+
                "ID_ULAZ_ZAG="+getMasterSet().getInt("ID_ULAZ_ZAG")).getShort("rbr");

        getDetailSet().setInt("ID_ULAZ_STAV", ++counter);
        getDetailSet().setShort("rbr", ++rbr);
        getDetailSet().setInt("ID_ULAZ_ZAG",
                getMasterSet().getInt("ID_ULAZ_ZAG"));

        return true;

    }

    public boolean doWithSaveDetail(char mode) {
        if (mode == 'B') {
            QueryDataSet st = Util
                    .getNewQueryDataSet("select * from Carulazst where ID_ULAZ_ZAG="
                            + getMasterSet().getInt("ID_ULAZ_ZAG"));
            short i = 1;
            for (st.first(); st.inBounds(); st.next()) {
                st.setShort("RBR", i);
                i++;
            }
            raTransaction.saveChanges(st);
        }

        return true;
    }
    
	public boolean DeleteCheckMaster() {
		boolean returnValue = true;
		if (returnValue) {
			if (!this.getDetailSet().isEmpty()) {
				javax.swing.JOptionPane.showConfirmDialog(null,
						"Nisu pobrisane stavke dokumenta !", "Gre\u0161ka",
						javax.swing.JOptionPane.DEFAULT_OPTION,
						javax.swing.JOptionPane.ERROR_MESSAGE);
				returnValue = false;
			}
		}
		return returnValue;
	}
	public boolean ValidacijaMaster(char mode) {

		if (val.isEmpty(jcpm.jrtfCBRDOK))
			return false;
		if (val.isEmpty(jcpm.jrtfDATDOK))
			return false;
		if (val.isEmpty(jcpm.jrtfCPAR))
			return false;
		if (val.isEmpty(jcpm.jrtfCSKL))
				return false;
		if (mode=='N') {
		if (hr.restart.util.Util.getNewQueryDataSet("select * from carulaz where cbrdok='"+
		        getMasterSet().getString("CBRDOK")+"'").getRowCount()!=0){
			javax.swing.JOptionPane.showMessageDialog(null,
					"Vec postoji primka sa brojem "+getMasterSet().getString("CBRDOK")+" !", "Greška",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		    return false;
		}
		}
		
		return true;
	}
	public boolean ValidacijaDetail(char mode) {

		if (val.isEmpty(jcpd.rpcart.jrfCART))
			return false;
		if (val.isEmpty(jcpd.jrtfCTG))
			return false;
		if (val.isEmpty(jcpd.jrtfKOL))
			return false;
		if (val.isEmpty(jcpd.jrtfCIJENA_VAL))
			return false;
		if (val.isEmpty(jcpd.jrtfVRIJEDNOST_VAL))
			return false;

		return true;
	}	
	
	private void createReportSet(QueryDataSet qds){
	    qds.close();
	    qds.closeStatement();
	    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),
	      "SELECT * FROM carulaz,carulazst WHERE carulazst.id_ulaz_zag = carulaz.id_ulaz_zag"+
	      " AND carulaz.ID_ULAZ_ZAG = "+this.getMasterSet().getInt("ID_ULAZ_ZAG")
	    ));
	    qds.open();
	}
	

  public void Funkcija_ispisa_detail() {
    createReportSet(repQDS);
    super.Funkcija_ispisa_detail();
  }
  
  public void Funkcija_ispisa_master() {
    createReportSet(repQDS);
    super.Funkcija_ispisa_master();
  }
  
  public QueryDataSet getReportData(){
    return repQDS;
  }
}





































class JCarinaPanelMaster extends JPanel {

    dM dm = dM.getDataModule();

    JLabel jBroj = new JLabel("Broj");

    JLabel jDatum = new JLabel("Datum ");

    JLabel jPrincipal = new JLabel("Principal");

    JLabel jOdobrenje = new JLabel("Odobrenje");

    JLabel jDatOdob = new JLabel("Datum ");

    JLabel jSklad = new JLabel("Skladište");

    JLabel j7 = new JLabel("JCD     Carinarnica / broj / datum / postupak ");

    JLabel j8 = new JLabel("Prethodni");

    JLabel j9 = new JLabel("Provozni");

    JLabel jODGOSOB = new JLabel("Odgovorna osoba");

    JLabel jKoment = new JLabel("Komentar");

    JLabel jPS = new JLabel("Prijevozno sredstvo");

    JLabel jRP = new JLabel("Raèun principala");

    JLabel jNadknjiga = new JLabel("Nadzorna knjiga");

    JLabel jParitet = new JLabel("Paritet");

    JLabel jVal = new JLabel("Valuta");

    JraTextField jrtfCBRDOK = new JraTextField();

    JraTextField jrtfDATDOK = new JraTextField();

    JlrNavField jrtfCPAR = new JlrNavField();

    JlrNavField jrtfNAZIVPAR = new JlrNavField();

    JraButton jrtfCPARB = new JraButton();

    JraTextField jrtfODOB = new JraTextField();

    JraTextField jrtfDATODOB = new JraTextField();

    JlrNavField jrtfCSKL = new JlrNavField();

    JlrNavField jrtfNAZIVSKL = new JlrNavField();

    JraButton jrtfCSKLB = new JraButton();

    JlrNavField jrtfJCD_CCAR_PRET = new JlrNavField();
    JraButton jrtfJCD_CCAR_PRETB = new JraButton();
    JraTextField jrtfJCD_BROJ_PRET = new JraTextField();
    JraTextField jrtfJCD_DATUM_PRET = new JraTextField();
    JlrNavField jrtfJCD_CPP37_PRET = new JlrNavField();
    JraButton jrtfJCD_CPP37_PRETB = new JraButton();
    JlrNavField jrtfJCD_CCAR_PROV = new JlrNavField();
    JraButton jrtfJCD_CCAR_PROVB = new JraButton();
    JraTextField jrtfJCD_BROJ_PROV = new JraTextField();
    JraTextField jrtfJCD_DATUM_PROV = new JraTextField();
    JlrNavField jrtfJCD_CPP37_PROV = new JlrNavField();
    JraButton jrtfJCD_CPP37_PROVB = new JraButton();
    JraTextField jrtfRACPRI = new JraTextField();
    JraTextField jrtfBRNAZK = new JraTextField();
    JraTextField jrtfDATNAZK = new JraTextField();
    JlrNavField jrtfCPAR1PP  = new JlrNavField();
    JraButton jrtfCPAR1PPB = new JraButton();
    hr.restart.zapod.jpGetValute jpgetval = new hr.restart.zapod.jpGetValute();
    JraTextField jrtfPRS = new JraTextField();
    JraTextField jrtfODGOSOBA = new JraTextField();
    JraTextField jrtfCOMMENT = new JraTextField();
    
    public void Bind(DataSet ds) {
        jrtfCBRDOK.setDataSet(ds);
        jrtfDATDOK.setDataSet(ds);
        jrtfCPAR.setDataSet(ds);
        jrtfODOB.setDataSet(ds);
        jrtfDATODOB.setDataSet(ds);
        jrtfCSKL.setDataSet(ds);
        jrtfJCD_CCAR_PRET.setDataSet(ds);
        jrtfJCD_BROJ_PRET.setDataSet(ds);
        jrtfJCD_DATUM_PRET.setDataSet(ds);
        jrtfJCD_CPP37_PRET.setDataSet(ds);
        jrtfJCD_CCAR_PROV.setDataSet(ds);
        jrtfJCD_BROJ_PROV.setDataSet(ds);
        jrtfJCD_DATUM_PROV.setDataSet(ds);
        jrtfJCD_CPP37_PROV.setDataSet(ds);
        jrtfRACPRI.setDataSet(ds);
        jrtfBRNAZK.setDataSet(ds);
        jrtfDATNAZK.setDataSet(ds);
        jrtfCPAR1PP.setDataSet(ds);
        jpgetval.setRaDataSet(ds);
        jrtfPRS.setDataSet(ds);
        jrtfODGOSOBA.setDataSet(ds);
        jrtfCOMMENT.setDataSet(ds);

    }

    public void setupTextFields() {
        jpgetval.setTecajVisible(true);
        
        jrtfCBRDOK.setColumnName("CBRDOK");
        jrtfCBRDOK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfDATDOK.setColumnName("DATDOK");
        jrtfDATDOK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
        		jpgetval.setTecajDate(jrtfDATDOK.getDataSet().getTimestamp(
				"DATDOK"));
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfCPAR.setColumnName("CPAR");
        jrtfCPAR.setColNames(new String[] { "NAZPAR" });
        jrtfCPAR.setVisCols(new int[] { 0, 1, 2 });
        jrtfCPAR
                .setTextFields(new javax.swing.text.JTextComponent[] { jrtfNAZIVPAR });
        jrtfCPAR.setRaDataSet(dm.getPartneri());
        jrtfNAZIVPAR.setColumnName("NAZPAR");
        jrtfNAZIVPAR.setSearchMode(1);
        jrtfNAZIVPAR.setNavProperties(jrtfCPAR);
        jrtfCPARB.setText("...");
        jrtfCPAR.setNavButton(jrtfCPARB);

        jrtfODOB.setColumnName("ODOB");
        jrtfODOB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfDATODOB.setColumnName("DATODOB");
        jrtfDATODOB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfCSKL.setColumnName("CSKL");
        jrtfCSKL.setColNames(new String[] { "NAZSKL" });
        jrtfCSKL.setVisCols(new int[] { 0, 1, 2 });
        jrtfCSKL
                .setTextFields(new javax.swing.text.JTextComponent[] { jrtfNAZIVSKL });
        jrtfCSKL.setRaDataSet(dm.getSklad());
        jrtfNAZIVSKL.setColumnName("NAZSKL");
        jrtfNAZIVSKL.setSearchMode(1);
        jrtfNAZIVSKL.setNavProperties(jrtfCSKL);
        jrtfCSKLB.setText("...");
        jrtfCSKL.setNavButton(jrtfCSKLB);

        jrtfJCD_CCAR_PRET.setNavColumnName("CCAR");
        jrtfJCD_CCAR_PRET.setRaDataSet(dm.getCarinarnica());
        jrtfJCD_CCAR_PRET.setColumnName("JCD_CCAR_PRET");
        jrtfJCD_CCAR_PRET.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CCAR_PRETB.setText("...");
        jrtfJCD_CCAR_PRET.setNavButton(jrtfJCD_CCAR_PRETB);
        
        jrtfJCD_BROJ_PRET.setColumnName("JCD_BROJ_PRET");
        jrtfJCD_BROJ_PRET.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfJCD_DATUM_PRET.setColumnName("JCD_DATUM_PRET");
        jrtfJCD_DATUM_PRET.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        
        jrtfJCD_CPP37_PRET.setNavColumnName("CPP37");
        jrtfJCD_CPP37_PRET.setRaDataSet(dm.getCarpostpp37());
        jrtfJCD_CPP37_PRET.setColumnName("JCD_CPP37_PRET");
        jrtfJCD_CPP37_PRET.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CPP37_PRETB.setText("...");
        jrtfJCD_CPP37_PRET.setNavButton(jrtfJCD_CPP37_PRETB);
//
        jrtfJCD_CCAR_PROV.setNavColumnName("CCAR");
        jrtfJCD_CCAR_PROV.setRaDataSet(dm.getCarinarnica());
        jrtfJCD_CCAR_PROV.setColumnName("JCD_CCAR_PROV");
        jrtfJCD_CCAR_PROV.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CCAR_PROVB.setText("...");
        jrtfJCD_CCAR_PROV.setNavButton(jrtfJCD_CCAR_PROVB);
        
        jrtfJCD_BROJ_PROV.setColumnName("JCD_BROJ_PROV");
        jrtfJCD_BROJ_PROV.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfJCD_DATUM_PROV.setColumnName("JCD_DATUM_PROV");
        jrtfJCD_DATUM_PROV.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        
        jrtfJCD_CPP37_PROV.setNavColumnName("CPP37");
        jrtfJCD_CPP37_PROV.setRaDataSet(dm.getCarpostpp37());
        jrtfJCD_CPP37_PROV.setColumnName("JCD_CPP37_PROV");
        jrtfJCD_CPP37_PROV.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CPP37_PROVB.setText("...");
        jrtfJCD_CPP37_PROV.setNavButton(jrtfJCD_CPP37_PROVB);
        
        jrtfRACPRI.setColumnName("RACPRI");
        jrtfRACPRI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfBRNAZK.setColumnName("BRNAZK");
        jrtfBRNAZK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        jrtfDATNAZK.setColumnName("DATNAZK");
        jrtfDATNAZK.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfCPAR1PP.setNavColumnName("CPAR1PP");
        jrtfCPAR1PP.setRaDataSet(dm.getParitet1PP());
        jrtfCPAR1PP.setColumnName("CPAR1PP");
        jrtfCPAR1PP.setVisCols(new int[] { 0, 1 });
        jrtfCPAR1PPB.setText("...");
        jrtfCPAR1PP.setNavButton(jrtfCPAR1PPB);
        
        jrtfPRS.setColumnName("PRS");
        jrtfPRS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        jrtfODGOSOBA.setColumnName("ODGOSOBA");
        jrtfODGOSOBA.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        
        jrtfCOMMENT.setColumnName("COMMENT");
        jrtfCOMMENT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });



    }

    public void init() {
        setupTextFields();
        XYLayout xy = new XYLayout();
        xy.setWidth(650);
        xy.setHeight(450);
        this.setLayout(xy);
        this.add(jBroj, new XYConstraints(15, 15, -1, -1));
        this.add(jrtfCBRDOK, new XYConstraints(150, 15, 100, -1));
        this.add(jDatum, new XYConstraints(450, 15, -1, -1));
        this.add(jrtfDATDOK, new XYConstraints(505, 15, 100, -1));
        this.add(jOdobrenje, new XYConstraints(15, 45, -1, -1));
        this.add(jrtfODOB, new XYConstraints(150, 45, 100, -1));
        this.add(jDatOdob, new XYConstraints(450, 45, -1, -1));
        this.add(jrtfDATODOB, new XYConstraints(505, 45, 100, -1));

        this.add(jSklad, new XYConstraints(15, 75, -1, -1));
        add(jrtfCSKL, new XYConstraints(150, 75, 100, -1));
        add(jrtfNAZIVSKL, new XYConstraints(254, 75, 327, -1));
        add(jrtfCSKLB, new XYConstraints(584, 75, 21, 21));

        this.add(j7, new XYConstraints(15, 105, -1, -1));
        this.add(j8, new XYConstraints(15, 135, -1, -1));
        this.add(jrtfJCD_CCAR_PRET, new XYConstraints(150, 135, 100, -1));
        this.add(jrtfJCD_CCAR_PRETB, new XYConstraints(253, 135, 21, 21));
        this.add(jrtfJCD_BROJ_PRET,new XYConstraints(277, 135, 97, -1));
        this.add(jrtfJCD_DATUM_PRET,new XYConstraints(380, 135, 97, -1));
        this.add(jrtfJCD_CPP37_PRET,new XYConstraints(483, 135, 97, -1));
        this.add(jrtfJCD_CPP37_PRETB, new XYConstraints(584, 135, 21, 21));
        this.add(j9, new XYConstraints(15, 165, -1, -1));
        this.add(jrtfJCD_CCAR_PROV, new XYConstraints(150, 165, 100, -1));
        this.add(jrtfJCD_CCAR_PROVB, new XYConstraints(253, 165, 21, 21));
        this.add(jrtfJCD_BROJ_PROV,new XYConstraints(277, 165, 97, -1));
        this.add(jrtfJCD_DATUM_PROV,new XYConstraints(380, 165, 97, -1));
        this.add(jrtfJCD_CPP37_PROV,new XYConstraints(483, 165, 97, -1));
        this.add(jrtfJCD_CPP37_PROVB, new XYConstraints(584, 165, 21, 21));
        
        
        this.add(jPrincipal, new XYConstraints(15, 195, -1, -1));
        add(jrtfCPAR, new XYConstraints(150, 195, 100, -1));
        add(jrtfNAZIVPAR, new XYConstraints(254, 195, 327, -1));
        add(jrtfCPARB, new XYConstraints(584, 195, 21, 21));

        this.add(jRP, new XYConstraints(15, 225, -1, -1));
        this.add(jrtfRACPRI, new XYConstraints(150, 225, 150, -1));
        this.add(jNadknjiga, new XYConstraints(15, 255, -1, -1));
        this.add(jrtfBRNAZK, new XYConstraints(150, 255, 100, -1));
        this.add(new JLabel("Datum"), new XYConstraints(450, 255, -1, -1));
        this.add(jrtfDATNAZK, new XYConstraints(505, 255, 100, -1));
        this.add(jParitet, new XYConstraints(15, 285, -1, -1));        
        this.add(jrtfCPAR1PP, new XYConstraints(150, 285, 100, -1));
        this.add(jrtfCPAR1PPB, new XYConstraints(253, 285, 21, 21));
                
//        this.add(jVal, new XYConstraints(15, 325, -1, -1));
		add(jpgetval, new XYConstraints(0, 305, -1, -1));
        
        this.add(jPS, new XYConstraints(15, 355, -1, -1));
        this.add(jrtfPRS, new XYConstraints(150, 355, 455, -1));

        this.add(jODGOSOB, new XYConstraints(15, 385, -1, -1));
        this.add(jrtfODGOSOBA, new XYConstraints(150, 385, 455, -1));
        
        this.add(jKoment, new XYConstraints(15, 415, -1, -1));
		this.add(jrtfCOMMENT, new XYConstraints(150, 415, 455, -1));

    }

}


























class JCarinaPanelDetail extends JPanel {
    dM dm = dM.getDataModule();
    BigDecimal compareBD = Aus.zero2;

    rapancartSimple rpcart = new rapancartSimple(){
        public void Myafter_lookUp() {
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    jrtfCTG.requestFocus();
                }});
    	}
    };

    JLabel jlCTG = new JLabel("Tarifna grupa");
    JlrNavField  jrtfCTG = new JlrNavField ();
    JraButton jrtfCTGB = new JraButton();
    JLabel jlCSIFDRV  = new JLabel("Država");
    JlrNavField jrtfCSIFDRV  = new JlrNavField();
    JraButton jrtfCSIFDRVB = new JraButton();
    JLabel jlPREF = new JLabel("Preferencijal");
    JraTextField jrtfPREF = new JraTextField();
    JLabel jlCPOTPOR = new JLabel("Potvrda o porijeklu");
    JlrNavField jrtfCPOTPOR  = new JlrNavField();
    JraButton jrtfCPOTPORB = new JraButton();
    JLabel jlKOL = new JLabel("Kolièina");
    JraTextField jrtfKOL = new JraTextField();
    JLabel jlNETTO_KG = new JLabel("Težina kg");
    JraTextField jrtfNETTO_KG= new JraTextField();
    JLabel jlKOLKOM = new JLabel("Kolièina (kom)");
    JraTextField jrtfKOL_KOM= new JraTextField();
    JLabel jlCIJENA_VAL = new JLabel("Cijena valutna");
    JraTextField jrtfCIJENA_VAL= new JraTextField();
    JLabel jlVRIJEDNOST_VAL = new JLabel("Vrijednost valutna");
    JraTextField jrtfVRIJEDNOST_VAL= new JraTextField();
    JLabel jlTROSKOVI_VAL = new JLabel("Troškovi valutni");
    JraTextField jrtfTROSKOVI_VAL= new JraTextField();
    JLabel jlTROS1_KN = new JLabel("Troškovi kunski");
    JraTextField jrtfTROS1_KN= new JraTextField();
    JLabel jlTROS2_KN = new JLabel("Ostali troškovi (kn)");
    JraTextField jrtfTROS2_KN= new JraTextField();

    public void Bind(QueryDataSet ds) {

        rpcart.jrfCART.setDataSet(ds);
        rpcart.jrfCART1.setDataSet(ds);
        rpcart.jrfBC.setDataSet(ds);
        rpcart.jrfNAZART.setDataSet(ds);
        rpcart.jrfJM.setDataSet(ds);
        jrtfCTG.setDataSet(ds);
        jrtfCSIFDRV.setDataSet(ds);
        jrtfPREF.setDataSet(ds);
        jrtfCPOTPOR.setDataSet(ds);
        jrtfKOL.setDataSet(ds);
        jrtfNETTO_KG.setDataSet(ds);
        jrtfKOL_KOM.setDataSet(ds);
        jrtfCIJENA_VAL.setDataSet(ds);
        jrtfVRIJEDNOST_VAL.setDataSet(ds);
        jrtfTROSKOVI_VAL.setDataSet(ds);
        jrtfTROS1_KN.setDataSet(ds);
        jrtfTROS2_KN.setDataSet(ds);
    }

    public JCarinaPanelDetail(){
        init();
    }
    
    public void setuTextField(){
        

        jrtfCTG.setNavColumnName("CTG");
        jrtfCTG.setRaDataSet(dm.getCarTG());
        jrtfCTG.setColumnName("CTG");
        jrtfCTG.setVisCols(new int[] { 0, 1 });
        jrtfCTGB.setText("...");
        jrtfCTG.setNavButton(jrtfCTGB);

        jrtfCPOTPOR.setNavColumnName("CPOTPOR");
        jrtfCPOTPOR.setRaDataSet(dm.getCarpotpor());
        jrtfCPOTPOR.setColumnName("CPOTPOR");
        jrtfCPOTPOR.setVisCols(new int[] { 0, 1 });
        jrtfCPOTPORB.setText("...");
        jrtfCPOTPOR.setNavButton(jrtfCPOTPORB);

        jrtfCSIFDRV.setNavColumnName("CSIFDRV");
        jrtfCSIFDRV.setRaDataSet(dm.getCardrzava());
        jrtfCSIFDRV.setColumnName("CSIFDRV");
        jrtfCSIFDRV.setVisCols(new int[] { 0, 1 });
        jrtfCSIFDRVB.setText("...");
        jrtfCSIFDRV.setNavButton(jrtfCSIFDRVB);
        
        
        jrtfPREF.setColumnName("PREF");
        jrtfPREF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        
        jrtfKOL.setColumnName("KOL");
        jrtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("KOL").compareTo(compareBD)!=0) {
                jrtfKOL.getDataSet().setBigDecimal("VRIJEDNOST_VAL",
                        jrtfKOL.getDataSet().getBigDecimal("KOL").
                        multiply(jrtfKOL.getDataSet().getBigDecimal("CIJENA_VAL")).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
            }

            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("KOL");
            }
        });

        jrtfNETTO_KG.setColumnName("NETTO_KG");
        jrtfNETTO_KG.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfKOL_KOM.setColumnName("KOL_KOM");
        jrtfKOL_KOM.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfCIJENA_VAL.setColumnName("CIJENA_VAL");
        jrtfCIJENA_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("CIJENA_VAL").compareTo(compareBD)!=0) {
                jrtfKOL.getDataSet().setBigDecimal("VRIJEDNOST_VAL",
                        jrtfKOL.getDataSet().getBigDecimal("KOL").
                        multiply(jrtfKOL.getDataSet().getBigDecimal("CIJENA_VAL")).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
            }

            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("CIJENA_VAL"); 
            }
        });

        jrtfVRIJEDNOST_VAL.setColumnName("VRIJEDNOST_VAL");
        jrtfVRIJEDNOST_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("VRIJEDNOST_VAL").compareTo(compareBD)!=0) {
                    if (jrtfKOL.getDataSet().getBigDecimal("KOL").compareTo(Aus.zero2)!=0) {
                    jrtfKOL.getDataSet().setBigDecimal("CIJENA_VAL",
                            jrtfKOL.getDataSet().getBigDecimal("VRIJEDNOST_VAL").
                            divide(jrtfKOL.getDataSet().getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
                    }
                }
            }

            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("VRIJEDNOST_VAL");
            }
        });

        jrtfTROSKOVI_VAL.setColumnName("TROSKOVI_VAL");
        jrtfTROSKOVI_VAL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfTROS1_KN.setColumnName("TROS1_KN");
        jrtfTROS1_KN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfTROS2_KN.setColumnName("TROS2_KN");
        jrtfTROS2_KN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
    }
    
    public void init() {
        setuTextField();
        XYLayout xy = new XYLayout();
        xy.setWidth(650);
        xy.setHeight(280);
        this.setLayout(xy);
            
        this.add(rpcart, new XYConstraints(0, 0, 640, 75));
        this.add(jlCTG, new XYConstraints(15, 75, -1, -1));
        this.add(jrtfCTG, new XYConstraints(150, 75, 100,-1));
        this.add(jrtfCTGB, new XYConstraints(255, 75, 21, 21));
        this.add(jlCSIFDRV, new XYConstraints(15, 100, -1, -1));
        this.add(jrtfCSIFDRV, new XYConstraints(150, 100, 100, -1));
        this.add(jrtfCSIFDRVB, new XYConstraints(255, 100, 21, 21));
        
        this.add(jlPREF, new XYConstraints(15, 125, -1, -1));
        this.add(jrtfPREF, new XYConstraints(150,125, 100,-1));
        this.add(jlCPOTPOR, new XYConstraints(300, 125, -1, -1));
        this.add(jrtfCPOTPOR, new XYConstraints(480, 125, 100, -1));
        this.add(jrtfCPOTPORB, new XYConstraints(585, 125, 21, 21));
        this.add(jlKOL, new XYConstraints(15, 150, -1, -1));
        this.add(jrtfKOL, new XYConstraints(150, 150, 100, -1));
        this.add(jlNETTO_KG, new XYConstraints(300, 150, -1, -1));
        this.add(jrtfNETTO_KG, new XYConstraints(480, 150, 100, -1));
        
        this.add(jlKOLKOM, new XYConstraints(15, 175, -1, -1));
        this.add(jrtfKOL_KOM, new XYConstraints(150, 175, 100, -1));
        
        this.add(jlCIJENA_VAL, new XYConstraints(15, 200, -1, -1));
        this.add(jrtfCIJENA_VAL, new XYConstraints(150, 200, 100, -1));
        this.add(jlVRIJEDNOST_VAL, new XYConstraints(300, 200, -1, -1));
        this.add(jrtfVRIJEDNOST_VAL, new XYConstraints(480, 200, 100, -1));
        this.add(jlTROSKOVI_VAL, new XYConstraints(15, 225, -1, -1));
        this.add(jrtfTROSKOVI_VAL, new XYConstraints(150, 225, 100, -1));
        this.add(jlTROS1_KN, new XYConstraints(300, 225, -1, -1));
        this.add(jrtfTROS1_KN, new XYConstraints(480, 225, 100, -1));
        this.add(jlTROS2_KN, new XYConstraints(300, 250, -1, -1));
        this.add(jrtfTROS2_KN, new XYConstraints(480, 250, 100, -1));
            
    }

}
