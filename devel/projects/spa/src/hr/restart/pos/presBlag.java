/****license*****************************************************************
**   file: presBlag.java
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
package hr.restart.pos;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class presBlag extends PreSelect {
	public static presBlag presblag;
	public static String blagajnik;
	public static String stol;
	public static boolean stolovi = false;
	raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  static boolean isSuper=false;
  JPanel jPanel1 = new JPanel();
  JlrNavField jrfNAZSKL = new JlrNavField();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jp = new JPanel();
  JlrNavField jrfCSKL = new JlrNavField();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JraButton jbCSKL = new JraButton();
  JlrNavField jrfCPRODMJ = new JlrNavField();
  JlrNavField jrfNAZPRODMJ = new JlrNavField();
  JraButton jbCPRODMJ = new JraButton();

  JraButton jbCBLAGAJNIK = new JraButton();
  JlrNavField jrfCBLAGAJNIK = new JlrNavField();
  JlrNavField jrfNAZBLAGAJNIK = new JlrNavField();

  JLabel jlStol = new JLabel();
  JraTextField jraStol = new JraTextField();
  JraTextField jraVRDOK = new JraTextField();
  
  JLabel jlDatum = new JLabel();
  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();
	JPasswordField jpswd = new JPasswordField() {
		public void addNotify() {
			super.addNotify();      
			Aus.installEnterRelease(this);
	    }
	};
	JraCheckBox jcbAktiv = new JraCheckBox();

  public presBlag() {
    try {
      jbInit();
      dm.getPos().open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static PreSelect getPres() {
    if (presblag==null) {
    	presblag=new presBlag();

    }
    return presblag;
  }

  public void resetDefaults() {
    jpswd.setText("");
    dm.getSklad().open();
    jcbAktiv.setSelected(true);
    getSelRow().setTimestamp("DATDOK-from", vl.getToday());
    getSelRow().setTimestamp("DATDOK-to", vl.getToday());
    getSelRow().setString("CSKL", raUser.getInstance().getDefSklad());
  }
  
  boolean firstTime = true;
  public void SetFokus() {
    rcc.setLabelLaF(this.getSelPanel(),true);

    if (firstTime) {
      firstTime = false;
      resetDefaults();
    }
/*    QueryDataSet qds = hr.restart.robno.Util.getMPSklDataset();
    qds.open();
    if (qds.rowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema definiranog maloprodajnog skladišta !",
      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.cancelSelect();
    }*/
//    jrfCSKL.setText(qds.getString("CSKL"));
//    jrfNAZSKL.setText(qds.getString("NAZSKL"));
    jrfCSKL.forceFocLost();
    System.out.println("Nasao skladiste: "+dm.getSklad().getString("CSKL"));
    if (dm.getSklad().rowCount()==1) {
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
    }
    if (jrfCSKL.getText().length() == 0) {
      jrfCSKL.requestFocusLater();
    } else if (stolovi && jrfCPRODMJ.getText().length() > 0) {
      jraStol.requestFocusLater();
    } else {
      jrfCPRODMJ.requestFocusLater();
    }
    jraVRDOK.getDataSet().setString("VRDOK", getVRDOK());
  }

  public boolean Validacija() {
    if (vl.isEmpty(jrfCSKL)) return false;
    if (vl.isEmpty(jrfCPRODMJ)) return false;
    //if (stolovi && vl.isEmpty(jraStol)) return false;
    dm.getBlagajnici().open();
    if (!lookupData.getlookupData().raLocate(dm.getBlagajnici(), "LOZINKA", String.valueOf(jpswd.getPassword()))) {
    	JOptionPane.showMessageDialog(null, "Pogrešna lozinka!", "Greška", JOptionPane.ERROR_MESSAGE);
      	return false;
    }
    else {
      blagajnik=dm.getBlagajnici().getString("CBLAGAJNIK");
      if (dm.getBlagajnici().getString("SUPER").equalsIgnoreCase("D")) {
      	isSuper=true;
      }
      else {
      	isSuper=false;
      }
    }

    if (stolovi) stol = jraStol.getText();
/*    String str="SELECT * FROM BLAGAJNICI WHERE LOZINKA='"+String.valueOf(jpswd.getPassword())+"'";
    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(str);
    System.out.println("sql: "+str);
    qds.open();
    if (qds.rowCount()==0) {
	      JOptionPane.showMessageDialog(null, "Pogrešna lozinka!", "Greška",
                JOptionPane.ERROR_MESSAGE);
    	return false;
    }
    else {
    	
    	blagajnik=qds.getString("CBLAGAJNIK");
    	if (qds.getString("SUPER").equalsIgnoreCase("D")) {
    		System.out.println("Supeeeeeeeeeeeeeeeer");
    		isSuper=true;
    	}
    	else {
    		System.out.println("Nijeeeeeeeeeee Supeeeeeeeeeeeeeeeer");
    		isSuper=false;
    	}
    }*/
    //    if (vl.isEmpty(jrfCBLAGAJNIK))
//        return false;
    if (frmMasterBlagajna.allReadyRun==true) {
        JOptionPane.showConfirmDialog(null,"Blagajna veæ pokrenuta !",
        	      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        	      this.cancelSelect();
    	return false;
    }
    return Aus.checkDateRange(jraDatumfrom, jraDatumto);
  }

  void jbInit() throws Exception {
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jrfCSKL.setRaDataSet(dm.getSklad());
      }
    });
    
    stolovi = frmParam.getParam("pos", "posStolovi", "N",
        "Izbor stola na predselekciji blagajne (D,N)").equalsIgnoreCase("D");

    jp.setLayout(xYLayout1);
    xYLayout1.setHeight(120);
    xYLayout1.setWidth(555);
    jrfNAZSKL.setEditable(false);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setColumnName("NAZSKL");
//    qdsSklad.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from sklad where vrzal=\'M\'", null, true, Load.ALL));
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setNavButton(jbCSKL);
    //    jrfCSKL.setRaDataSet(qdsSklad);
    jrfCSKL.setRaDataSet(dm.getSklad());
    jLabel3.setText("Zaporka");
    jLabel2.setText("Blagajna");
    jLabel1.setText("Prodajno mjesto");
    jrfCPRODMJ.setRaDataSet(dm.getProd_mj());
    jrfCPRODMJ.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPRODMJ});
    jrfCPRODMJ.setVisCols(new int[]{0,1});
    jrfCPRODMJ.setColNames(new String[] {"NAZPRODMJ"});
    jrfCPRODMJ.setColumnName("CPRODMJ");
    jrfCPRODMJ.setNavButton(jbCPRODMJ);
    jrfNAZPRODMJ.setColumnName("NAZPRODMJ");
    jrfNAZPRODMJ.setSearchMode(1);
    jrfNAZPRODMJ.setNavProperties(jrfCPRODMJ);

    jrfCBLAGAJNIK.setRaDataSet(dm.getBlagajnici());
    jrfCBLAGAJNIK.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBLAGAJNIK});
    jrfCBLAGAJNIK.setVisCols(new int[]{0,1});
    jrfCBLAGAJNIK.setColNames(new String[] {"NAZBLAG"});
    jrfCBLAGAJNIK.setColumnName("CBLAGAJNIK");
    jrfCBLAGAJNIK.setNavButton(jbCBLAGAJNIK);
    jrfNAZBLAGAJNIK.setColumnName("NAZBLAG");
    jrfNAZBLAGAJNIK.setSearchMode(1);
    jrfNAZBLAGAJNIK.setNavProperties(jrfCBLAGAJNIK);
    
    //    jrfNAZPRODMJ.setEditable(false);
    jlDatum.setText("Datum (od - do)");
    jraDatumfrom.setColumnName("DATDOK");
    jraDatumto.setColumnName("DATDOK");
    
    if (stolovi) {
      jlStol.setText("Stol");
      jraStol.setColumnName("STOL");
      jraVRDOK.setColumnName("VRDOK");
      jcbAktiv.setText(" Samo aktivni raèuni ");
      jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
      jcbAktiv.setHorizontalAlignment(SwingConstants.TRAILING);
      jp.add(jlStol, new XYConstraints(375, 70, -1, -1));
      jp.add(jraStol, new XYConstraints(415, 70, 100, 21));
      jp.add(jcbAktiv, new XYConstraints(375, 95, 140, -1));
    }

    jp.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jp.add(jrfNAZSKL, new XYConstraints(260, 20, 255, -1));
    jp.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jp.add(jLabel3, new XYConstraints(15, 70, -1, -1));
    jp.add(jLabel2, new XYConstraints(15, 45, -1, -1));
    jp.add(jPanel1, new XYConstraints(0, 0, -1, -1));
    jp.add(jbCSKL, new XYConstraints(519, 20, 21, 21));
    jp.add(jrfCPRODMJ, new XYConstraints(150, 45, 100, -1));
    jp.add(jrfNAZPRODMJ, new XYConstraints(260, 45, 255, -1));
    jp.add(jbCPRODMJ, new XYConstraints(519, 45, 21, 21));

    jp.add(jraVRDOK, new XYConstraints(0,0,0,0));
    jraVRDOK.setVisible(false);
  //jp.add(jrfCBLAGAJNIK, new XYConstraints(-1, -1, 100, -1));
//    jp.add(jrfNAZBLAGAJNIK, new XYConstraints(260, 70, 255, -1));
//    jp.add(jbCBLAGAJNIK, new XYConstraints(519, 70, 21, 21));

    jp.add(jlDatum, new XYConstraints(15, 95, -1, -1));
    jp.add(jraDatumfrom, new XYConstraints(150, 95, 100, -1));
    jp.add(jraDatumto, new XYConstraints(260, 95, 100, -1));

    this.setSelDataSet(dm.getPos());
    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jp);
    installResetButton();
    jp.add(jpswd, new XYConstraints(150, 70, 100, -1));
  }
  
  public String refineSQLFilter(String orig) {
    if (!stolovi || !jcbAktiv.isSelected()) return orig;
    return orig + " AND pos.aktiv='D'";
  }
  
  public static String getBlagajnik() {
  	return blagajnik;
  }
  public static boolean isSuper() {
  	return isSuper;
  }
  /**
   * Override za NAR i sl.
   * @return
   */
  protected String getVRDOK() {
    return "GRC";
  }
}
