/****license*****************************************************************
**   file: FrmCarKnjZapis.java
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

public class FrmCarKnjZapis extends raMasterDetail {
    dM dm = dM.getDataModule();
	hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

    JCarKnjZapisPanelMaster jcpm = new JCarKnjZapisPanelMaster();

    JCarKnjZapisPanellDetail jcpd;
	raCommonClass rCC = raCommonClass.getraCommonClass();
    raTableColumnModifier TCM;

    QueryDataSet repQDS = new QueryDataSet();
    
    private static FrmCarKnjZapis instanceOfMe = null;
    
    public static FrmCarKnjZapis getInstance(){
      if (instanceOfMe == null) instanceOfMe = new FrmCarKnjZapis();
      return instanceOfMe;
    }

    public FrmCarKnjZapis() {

        super(1, 2);

        /*
         * setUserCheck(hr.restart.sisfun.frmParam .getParam("robno",
         * "userCheck", "D", "Da li se provjerava korisnik kod izmjene dokumenta
         * (D/N)") .equalsIgnoreCase("D"));
         */

        TCM = new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
                "CPAR", "NAZPAR" }, new String[] { "CPAR" }, dm.getPartneri()) {
            public int getMaxModifiedTextLength() {
                return 27;
            }
        };

        dm.getCarizlaz().open();
        dm.getCarizlazst().open();
        jcpm.init();

        setNaslovMaster("Knjigovodstveni zapis");
        setNaslovDetail("Stavke knjigovodstvenog zapisa");
        
        setVisibleColsMaster(new int[] { 1, 2, 3, 6, 7 });
//        setVisibleColsMaster(new int[] { 1,2,3 });
        setVisibleColsDetail(new int[] { 0, 1, 3, 5, 6, 10, 13,14 });
        raMaster.installSelectionTracker("ID_IZLAZ_ZAG");
        java.lang.String [] key = { "ID_IZLAZ_ZAG" };
        this.setMasterKey(key);
        this.setDetailKey(key);
        raMaster.getNavBar().getColBean().setSaveSettings(true);
        setMasterSet(dm.getCarizlaz());
        setDetailSet(dm.getCarizlazst());
        setJPanelMaster(jcpm);
        jcpd = new JCarKnjZapisPanellDetail();
        //            jcpd.init();
        setJPanelDetail(jcpd);
        jcpm.Bind(dm.getCarizlaz());
        jcpd.Bind(dm.getCarizlazst());
        raMaster.getJpTableView().addTableModifier(TCM);
        
        this.raMaster.getRepRunner().addReport("hr.restart.cstd.RepCarKnjZapis","hr.restart.cstd.RepCarKnjZapis","CarKnjizniZapis","Carinski knjižni zapis");
        this.raDetail.getRepRunner().addReport("hr.restart.cstd.RepCarKnjZapis","hr.restart.cstd.RepCarKnjZapis","CarKnjizniZapis","Carinski knjižni zapis");
        instanceOfMe = this;
    }

    public boolean doBeforeSaveMaster(char mode) {
        if (mode != 'N')
            return true;
        int counter = Util.getNewQueryDataSet(
                "select max(ID_IZLAZ_ZAG) as ID_IZLAZ_ZAG from Carizlaz")
                .getInt("ID_IZLAZ_ZAG");
        getMasterSet().setInt("ID_IZLAZ_ZAG", ++counter);
        return true;

    }

    public boolean doBeforeSaveDetail(char mode) {
        if (mode != 'N')
            return true;
        int counter = Util.getNewQueryDataSet(
                "select max(ID_IZLAZ_STAV) as ID_IZLAZ_STAV from Carizlazst")
                .getInt("ID_IZLAZ_STAV");
        short rbr = Util.getNewQueryDataSet(
                "select max(rbr) as rbr from Carizlazst").getShort("rbr");

        getDetailSet().setInt("ID_IZLAZ_STAV", ++counter);
        getDetailSet().setShort("rbr", ++rbr);
        getDetailSet().setInt("ID_IZLAZ_ZAG",
                getMasterSet().getInt("ID_IZLAZ_ZAG"));

        return true;
        
    }

    public boolean doWithSaveDetail(char mode) {
        if (mode == 'B') {
            QueryDataSet st = Util
                    .getNewQueryDataSet("select * from Carizlazst where ID_IZLAZ_ZAG="
                            + getMasterSet().getInt("ID_IZLAZ_ZAG"));
            short i = 1;
            for (st.first(); st.inBounds(); st.next()) {
                st.setShort("RBR", i);
                i++;
            }

            raTransaction.saveChanges(st);
        }
        jcpd.tecaj=getMasterSet().getBigDecimal("TECAJ");

        return true;
    }

    public void afterSetModeMaster(char oldm, char newm) {
        if (newm == 'B')
            jcpm.jpgetval.disableDohvat();
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
        jcpd.tecaj=getMasterSet().getBigDecimal("TECAJ");
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
                    jcpd.jrtfCTG.requestFocus();
                }});
            
        }
        
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
		if (val.isEmpty(jcpm.jpgetval.jtTECAJ))
			return false;

		if (mode=='N') {
		if (hr.restart.util.Util.getNewQueryDataSet("select * from carizlaz where cbrdok='"+
		        getMasterSet().getString("CBRDOK")+"'").getRowCount()!=0){
			javax.swing.JOptionPane.showMessageDialog(null,
					"Vec postoji knjig. zapis sa brojem "+getMasterSet().getString("CBRDOK")+" !", "Greška",
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
	      "SELECT * FROM carizlaz,carizlazst WHERE carizlazst.id_izlaz_zag = carizlaz.id_izlaz_zag"+
	      " AND carizlaz.ID_IZLAZ_ZAG = "+this.getMasterSet().getInt("ID_IZLAZ_ZAG")
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

class JCarKnjZapisPanelMaster extends JPanel {

    dM dm = dM.getDataModule();

    JLabel jBroj = new JLabel("Broj");

    JLabel jDatum = new JLabel("Datum ");

    JLabel jPrincipal = new JLabel("Principal");

    JLabel jSklad = new JLabel("Skladište");

    JLabel jODGOSOB = new JLabel("Odgovorna osoba");

    JLabel jKoment = new JLabel("Komentar");

    JLabel jPS = new JLabel("Prijevozno sredstvo");

    JLabel jRP = new JLabel("Raèun principala");

    JraTextField jrtfCBRDOK = new JraTextField();

    JraTextField jrtfDATDOK = new JraTextField();

    JlrNavField jrtfCPAR = new JlrNavField();

    JlrNavField jrtfNAZIVPAR = new JlrNavField();

    JraButton jrtfCPARB = new JraButton();

    JlrNavField jrtfCSKL = new JlrNavField();

    JlrNavField jrtfNAZIVSKL = new JlrNavField();

    JraButton jrtfCSKLB = new JraButton();

    JraTextField jrtfRACPRI = new JraTextField();

    hr.restart.zapod.jpGetValute jpgetval = new hr.restart.zapod.jpGetValute();

    JraTextField jrtfODGOSOBA = new JraTextField();

    JraTextField jrtfCOMMENT = new JraTextField();

    public void Bind(DataSet ds) {
        jrtfCBRDOK.setDataSet(ds);
        jrtfDATDOK.setDataSet(ds);
        jrtfCPAR.setDataSet(ds);
        jrtfCSKL.setDataSet(ds);
        jrtfRACPRI.setDataSet(ds);
        jpgetval.setRaDataSet(ds);
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

        jrtfRACPRI.setColumnName("RACPRI");
        jrtfRACPRI.addFocusListener(new java.awt.event.FocusAdapter() {
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
        xy.setHeight(230);
        this.setLayout(xy);
        this.add(jBroj, new XYConstraints(15, 15, -1, -1));
        this.add(jrtfCBRDOK, new XYConstraints(150, 15, 100, -1));
        this.add(jDatum, new XYConstraints(450, 15, -1, -1));
        this.add(jrtfDATDOK, new XYConstraints(505, 15, 100, -1));
        this.add(jSklad, new XYConstraints(15, 40, -1, -1));
        add(jrtfCSKL, new XYConstraints(150, 40, 100, -1));
        add(jrtfNAZIVSKL, new XYConstraints(254, 40, 327, -1));
        add(jrtfCSKLB, new XYConstraints(584, 40, 21, 21));

        this.add(jPrincipal, new XYConstraints(15, 65, -1, -1));
        add(jrtfCPAR, new XYConstraints(150, 65, 100, -1));
        add(jrtfNAZIVPAR, new XYConstraints(254, 65, 327, -1));
        add(jrtfCPARB, new XYConstraints(584, 65, 21, 21));

        this.add(jRP, new XYConstraints(15, 90, -1, -1));
        this.add(jrtfRACPRI, new XYConstraints(150, 90, 150, -1));
        add(jpgetval, new XYConstraints(0, 115, -1, -1));
        //        this.add(jPS, new XYConstraints(15, 340, -1, -1));
        this.add(jODGOSOB, new XYConstraints(15, 165, -1, -1));
        this.add(jrtfODGOSOBA, new XYConstraints(150, 165, 455, -1));
        this.add(jKoment, new XYConstraints(15, 190, -1, -1));
        this.add(jrtfCOMMENT, new XYConstraints(150, 190, 455, -1));
    }

}









class JCarKnjZapisPanellDetail extends JPanel {
    dM dm = dM.getDataModule();

    rapancartSimple rpcart = new rapancartSimple() {
        public void Myafter_lookUp() {
            setupPPorez();
            SwingUtilities.invokeLater(new Runnable(){
                public void run() {
                    jrtfCTG.requestFocus();
                }});
    	}
        
    };
    

    JLabel jlCTG = new JLabel("Tarifna grupa");
    JLabel jlJCDCAR = new JLabel("Carinarnica");

    JLabel jlJCDBRO =new JLabel("Broj");

    JLabel jlJCDDAT =new JLabel("Datum");

    JLabel jlJCDPOS =new JLabel("Postupak");



    JlrNavField jrtfCTG = new JlrNavField();

    JraButton jrtfCTGB = new JraButton();

    JLabel jlCSIFDRV = new JLabel("Država");

    JlrNavField jrtfCSIFDRV = new JlrNavField();

    JraButton jrtfCSIFDRVB = new JraButton();

    JLabel jlPREF = new JLabel("Preferencijal");

    JraTextField jrtfPREF = new JraTextField();

    JLabel jlCPOTPOR = new JLabel("Potvrda o porijeklu");

    JlrNavField jrtfCPOTPOR = new JlrNavField();

    JraButton jrtfCPOTPORB = new JraButton();

    JLabel jlKOL = new JLabel("Kolièina");

    JraTextField jrtfKOL = new JraTextField();

    JLabel jlNETTO_KG = new JLabel("Težina kg");

    JraTextField jrtfNETTO_KG = new JraTextField();

    JLabel jlKOLKOM = new JLabel("Kolièina (kom)");

    JraTextField jrtfKOL_KOM = new JraTextField();

    JLabel jlCIJENA_VAL = new JLabel("Cijena valutna");

    JraTextField jrtfCIJENA_VAL = new JraTextField();

    JLabel jlVRIJEDNOST_VAL = new JLabel("Vrijednost valutna");

    JraTextField jrtfVRIJEDNOST_VAL = new JraTextField();

    JlrNavField jrtfJCD_CCAR = new JlrNavField();

    JraButton jrtfJCD_CCARB = new JraButton();

    JraTextField jrtfJCD_BROJ = new JraTextField();

    JraTextField jrtfJCD_DATUM = new JraTextField();

    JlrNavField jrtfJCD_CPP37 = new JlrNavField();
    JraButton jrtfJCD_CPP37B = new JraButton();
    JlrNavField jrtfJCD_CCAR_KONAC = new JlrNavField();
    JraButton jrtfJCD_CCAR_KONACB = new JraButton();
    JraTextField jrtfJCD_BROJ_KONAC = new JraTextField();
    JraTextField jrtfJCD_DATUM_KONAC = new JraTextField();
    JlrNavField jrtfJCD_CPP37_KONAC = new JlrNavField();
    JraButton jrtfJCD_CPP37_KONACB = new JraButton();
    JraTextField jrtfSTOPA1 = new JraTextField();
    JraTextField jrtfOSNOVICA= new JraTextField();
    JraTextField jrtfIZNOSCAR= new JraTextField();
    JraTextField jrtfPOREZ= new JraTextField();
    JraTextField jrtfPPOREZ= new JraTextField();
    JraTextField jrtfTROS= new JraTextField();
    BigDecimal compareBD = Aus.zero2;
    BigDecimal tecaj = Aus.zero2;

    public void racOsnovice(){
        
        jrtfKOL.getDataSet().setBigDecimal("OSNOVICA",
                jrtfKOL.getDataSet().getBigDecimal("VRIJEDNOST_VAL").multiply(tecaj).setScale(2,BigDecimal.ROUND_HALF_UP));
        jrtfKOL.getDataSet().setBigDecimal("OSNOVICA",
                jrtfKOL.getDataSet().getBigDecimal("VRIJEDNOST_VAL").multiply(tecaj).setScale(2,BigDecimal.ROUND_HALF_UP));

        jrtfKOL.getDataSet().setBigDecimal("IZNOSCAR",
                jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").
        multiply(jrtfKOL.getDataSet().getBigDecimal("STOPA1")).setScale(2,BigDecimal.ROUND_HALF_UP));
        jrtfKOL.getDataSet().setBigDecimal("IZNOSCAR",
                jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR").divide(
                        new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
        jrtfKOL.getDataSet().setBigDecimal("POREZ",
                jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").multiply(
                        new BigDecimal("100.00")).setScale(2,BigDecimal.ROUND_HALF_UP));
        jrtfKOL.getDataSet().setBigDecimal("POREZ",
                jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").divide(
                        jrtfKOL.getDataSet().getBigDecimal("PPOREZ"),
                        2,BigDecimal.ROUND_HALF_UP));

        
    
    }
    
    public void setupPPorez(){
        String cpor = Util.getNewQueryDataSet(
                "select cpor  from artikli where cart ="+rpcart.jrfCART.getText()).getString("CPOR");
        BigDecimal pporez = Util.getNewQueryDataSet(
                "select UKUPOR  from Porezi where cpor ='"+cpor+"'").getBigDecimal("UKUPOR");
        jrtfKOL.getDataSet().setBigDecimal("PPOREZ",pporez); 
    }
    
    public void Bind(QueryDataSet ds) {

        rpcart.jrfCART.setDataSet(ds);
        rpcart.jrfCART1.setDataSet(ds);
        rpcart.jrfBC.setDataSet(ds);
        rpcart.jrfNAZART.setDataSet(ds);
        jrtfCTG.setDataSet(ds);
        jrtfCSIFDRV.setDataSet(ds);
        jrtfPREF.setDataSet(ds);
        jrtfCPOTPOR.setDataSet(ds);
        jrtfKOL.setDataSet(ds);
        jrtfNETTO_KG.setDataSet(ds);
        jrtfKOL_KOM.setDataSet(ds);
        jrtfCIJENA_VAL.setDataSet(ds);
        jrtfVRIJEDNOST_VAL.setDataSet(ds);
        jrtfJCD_CCAR.setDataSet(ds);
        jrtfJCD_BROJ.setDataSet(ds);
        jrtfJCD_DATUM.setDataSet(ds);
        jrtfJCD_CPP37.setDataSet(ds);
        jrtfJCD_CCAR_KONAC.setDataSet(ds);
        jrtfJCD_BROJ_KONAC.setDataSet(ds);
        jrtfJCD_DATUM_KONAC.setDataSet(ds);
        jrtfJCD_CPP37_KONAC.setDataSet(ds);
        jrtfSTOPA1.setDataSet(ds);
        jrtfOSNOVICA.setDataSet(ds);
        jrtfIZNOSCAR.setDataSet(ds);
        jrtfPOREZ.setDataSet(ds);
        jrtfTROS.setDataSet(ds);
        jrtfPPOREZ.setDataSet(ds);

    }

    public JCarKnjZapisPanellDetail() {
        init();
    }

    public void setuTextField() {

        jrtfSTOPA1.setColumnName("STOPA1");
        jrtfSTOPA1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("STOPA1").compareTo(compareBD)!=0) {
                    jrtfKOL.getDataSet().setBigDecimal("IZNOSCAR",
                            jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").
                    multiply(jrtfKOL.getDataSet().getBigDecimal("STOPA1")).setScale(2,BigDecimal.ROUND_HALF_UP));
                    jrtfKOL.getDataSet().setBigDecimal("IZNOSCAR",
                            jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR").divide(
                                    new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
                }
            }
            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("STOPA1"); 
            }
            
        });


        jrtfOSNOVICA.setColumnName("OSNOVICA");
        jrtfOSNOVICA.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").compareTo(compareBD)!=0) {
                    jrtfKOL.getDataSet().setBigDecimal("IZNOSCAR",
                            jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").
                    multiply(jrtfKOL.getDataSet().getBigDecimal("STOPA1")).setScale(2,BigDecimal.ROUND_HALF_UP));
                    jrtfKOL.getDataSet().setBigDecimal("IZNOSCAR",
                            jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR").divide(
                                    new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
                }
            }
            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("OSNOVICA"); 
            }
        });
        
        jrtfIZNOSCAR.setColumnName("IZNOSCAR");
        jrtfIZNOSCAR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR").compareTo(compareBD)!=0) {
                    jrtfKOL.getDataSet().setBigDecimal("STOPA1",
                            jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR").multiply(
                                    new BigDecimal("100.00")).setScale(2,BigDecimal.ROUND_HALF_UP));
                    jrtfKOL.getDataSet().setBigDecimal("STOPA1",
                            jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR").divide(
                                    jrtfKOL.getDataSet().getBigDecimal("OSNOVICA"),
                                    2,BigDecimal.ROUND_HALF_UP));
                }
            }
            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR"); 
            }
        });
        
        
        jrtfPPOREZ.setColumnName("PPOREZ");
        jrtfPPOREZ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (jrtfKOL.getDataSet().getBigDecimal("PPOREZ").compareTo(compareBD)!=0) {
                    jrtfKOL.getDataSet().setBigDecimal("POREZ",
                            jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").multiply(
                                    new BigDecimal("100.00")).setScale(2,BigDecimal.ROUND_HALF_UP));
                    jrtfKOL.getDataSet().setBigDecimal("POREZ",
                            jrtfKOL.getDataSet().getBigDecimal("OSNOVICA").divide(
                                    jrtfKOL.getDataSet().getBigDecimal("PPOREZ"),
                                    2,BigDecimal.ROUND_HALF_UP));
                }
            }
            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("IZNOSCAR"); 
            }
        });
        
        jrtfPOREZ.setColumnName("POREZ");
        jrtfTROS.setColumnName("TROS");
        
        jrtfCTG.setNavColumnName("CTG");
        jrtfCTG.setRaDataSet(dm.getCarTG());
        jrtfCTG.setColumnName("CTG");
        
        jrtfCTG.setColNames(new String[] { "STOPA1" });
        jrtfCTG.setTextFields(new javax.swing.text.JTextComponent[] { jrtfSTOPA1 });

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
                racOsnovice();
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
                racOsnovice();
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
                    racOsnovice();
                    }
                }
            }

            public void focusGained(FocusEvent e) {
                compareBD = jrtfKOL.getDataSet().getBigDecimal("VRIJEDNOST_VAL");
            }
        });
        
        jrtfJCD_CCAR.setNavColumnName("CCAR");
        jrtfJCD_CCAR.setRaDataSet(dm.getCarinarnica());
        jrtfJCD_CCAR.setColumnName("JCD_CCAR");
        jrtfJCD_CCAR.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CCARB.setText("...");
        jrtfJCD_CCAR.setNavButton(jrtfJCD_CCARB);
        
        jrtfJCD_BROJ.setColumnName("JCD_BROJ");
        jrtfJCD_BROJ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfJCD_DATUM.setColumnName("JCD_DATUM");
        jrtfJCD_DATUM.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        
        jrtfJCD_CPP37.setNavColumnName("CPP37");
        jrtfJCD_CPP37.setRaDataSet(dm.getCarpostpp37());
        jrtfJCD_CPP37.setColumnName("JCD_CPP37");
        jrtfJCD_CPP37.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CPP37B.setText("...");
        jrtfJCD_CPP37.setNavButton(jrtfJCD_CPP37B);
//
        jrtfJCD_CCAR_KONAC.setNavColumnName("CCAR");
        jrtfJCD_CCAR_KONAC.setRaDataSet(dm.getCarinarnica());
        jrtfJCD_CCAR_KONAC.setColumnName("JCD_CCAR_KONAC");
        jrtfJCD_CCAR_KONAC.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CCAR_KONACB.setText("...");
        jrtfJCD_CCAR_KONAC.setNavButton(jrtfJCD_CCAR_KONACB);
        
        jrtfJCD_BROJ_KONAC.setColumnName("JCD_BROJ_KONAC");
        jrtfJCD_BROJ_KONAC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });

        jrtfJCD_DATUM_KONAC.setColumnName("JCD_DATUM_KONAC");
        jrtfJCD_DATUM_KONAC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e) {
            }

            public void focusGained(FocusEvent e) {
            }
        });
        
        jrtfJCD_CPP37_KONAC.setNavColumnName("CPP37");
        jrtfJCD_CPP37_KONAC.setRaDataSet(dm.getCarpostpp37());
        jrtfJCD_CPP37_KONAC.setColumnName("JCD_CPP37_KONAC");
        jrtfJCD_CPP37_KONAC.setVisCols(new int[] { 0, 1 });
        jrtfJCD_CPP37_KONACB.setText("...");
        jrtfJCD_CPP37_KONAC.setNavButton(jrtfJCD_CPP37_KONACB);
    }

    public void init() {
        setuTextField();
        jlJCDCAR.setHorizontalAlignment(SwingUtilities.CENTER);
        jlJCDBRO.setHorizontalAlignment(SwingUtilities.CENTER);
        jlJCDDAT.setHorizontalAlignment(SwingUtilities.CENTER);
        jlJCDPOS.setHorizontalAlignment(SwingUtilities.CENTER);
        
        XYLayout xy = new XYLayout();
        xy.setWidth(650);
        xy.setHeight(380);
        this.setLayout(xy);

        this.add(rpcart, new XYConstraints(0, 0, 640, 75));
        this.add(jlCTG, new XYConstraints(15, 75, -1, -1));
        this.add(jrtfCTG, new XYConstraints(150, 75, 100, -1));
        this.add(jrtfCTGB, new XYConstraints(255, 75, 21, 21));
        this.add(new JLabel("Stopa car. %"), new XYConstraints(300, 75, -1,-1));
        this.add(jrtfSTOPA1, new XYConstraints(480, 75, 100,-1));
        this.add(jlCSIFDRV, new XYConstraints(15, 100, -1, -1));
        this.add(jrtfCSIFDRV, new XYConstraints(150, 100, 100, -1));
        this.add(jrtfCSIFDRVB, new XYConstraints(255, 100, 21, 21));

        this.add(jlPREF, new XYConstraints(15, 125, -1, -1));
        this.add(jrtfPREF, new XYConstraints(150, 125, 100, -1));
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
        this.add(jlJCDCAR, new XYConstraints(150, 225, 100, -1));
        this.add(jlJCDBRO, new XYConstraints(277, 225, 97, -1));
        this.add(jlJCDDAT, new XYConstraints(380, 225, 97, -1));
        this.add(jlJCDPOS, new XYConstraints(483, 225, 97, -1));

        this.add(new JLabel("JCD"), new XYConstraints(15, 250, -1, -1));
        this.add(jrtfJCD_CCAR, new XYConstraints(150, 250, 100, -1));
        this.add(jrtfJCD_CCARB, new XYConstraints(253, 250, 21, 21));
        this.add(jrtfJCD_BROJ,new XYConstraints(277, 250, 97, -1));
        this.add(jrtfJCD_DATUM,new XYConstraints(380, 250, 97, -1));
        this.add(jrtfJCD_CPP37,new XYConstraints(483, 250, 97, -1));
        this.add(jrtfJCD_CPP37B, new XYConstraints(584, 250, 21, 21));

        this.add(new JLabel("JCD konaèni"), new XYConstraints(15, 275, -1, -1));
        this.add(jrtfJCD_CCAR_KONAC, new XYConstraints(150, 275, 100, -1));
        this.add(jrtfJCD_CCAR_KONACB, new XYConstraints(253, 275, 21, 21));
        this.add(jrtfJCD_BROJ_KONAC,new XYConstraints(277, 275, 97, -1));
        this.add(jrtfJCD_DATUM_KONAC,new XYConstraints(380, 275, 97, -1));
        this.add(jrtfJCD_CPP37_KONAC,new XYConstraints(483, 275, 97, -1));
        this.add(jrtfJCD_CPP37_KONACB, new XYConstraints(584, 275, 21, 21));
        
        this.add(new JLabel("Carinska osnovica"), new XYConstraints(15, 300, -1, -1));
        this.add(jrtfOSNOVICA, new XYConstraints(150, 300, 100, -1));
        this.add(new JLabel("Iznos carine"),new XYConstraints(300, 300, -1, -1));
        this.add(jrtfIZNOSCAR, new XYConstraints(480, 300, 100, -1));
        this.add(new JLabel("% poreza"), new XYConstraints(15, 325, -1, -1));
        this.add(jrtfPPOREZ, new XYConstraints(150, 325, 100, -1));
        this.add(new JLabel("Porez"), new XYConstraints(300, 325, -1, -1));
        this.add(jrtfPOREZ, new XYConstraints(480, 325, 100, -1));
        this.add(new JLabel("Pos. porez"), new XYConstraints(15, 350, -1, -1));
        this.add(jrtfTROS, new XYConstraints(150, 350, 100, -1));


     /*1206-2005-2664*/   
    }

}
