package hr.restart.distrib;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Distkal;
import hr.restart.baza.Distlist;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.zapod.Sifrarnici;

public class presDistList extends PreSelect {

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
	hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
	
  JPanel jpSelDoc = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  
  JLabel jlCSKL = new JLabel();
	JlrNavField jrfCSKL = new JlrNavField();
	JlrNavField jrfNAZSKL = new JlrNavField();
	JraButton jbCSKL = new JraButton();
	
	JLabel jlDist = new JLabel();
	JlrNavField jlrDist = new JlrNavField();
	JlrNavField jlrNazDist = new JlrNavField();
	JraButton jbDist = new JraButton();
	
	JLabel jlDatum = new JLabel();
  JraTextField jtfDATUMDO = new JraTextField();
  JraTextField jtfDATUMOD = new JraTextField();
  
  boolean firstTime = true;
	
	public presDistList() {
    try {
      jbInit();
      installResetButton();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
	
	public void resetDefaults() {
		jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
		getSelRow().setTimestamp("DATUM-from", vl.getToday());
    getSelRow().setTimestamp("DATUM-to", vl.getToday());
    getSelRow().post();
    firstTime = false;
  }
	
	public void SetFokus() {
    if (firstTime) resetDefaults();
    if (jrfCSKL.getText().length() == 0)
    	jrfCSKL.requestFocus();
    else jlrDist.requestFocus();
	}
	
	public boolean Validacija() {
    if (vl.isEmpty(this.jrfCSKL))
      return false;
    
    if (!hr.restart.util.Util.getUtil().isValidRange(getSelRow().getTimestamp("DATUM-from"), getSelRow().getTimestamp("DATUM-to"))) {
      jtfDATUMOD.requestFocus();
      javax.swing.JOptionPane.showConfirmDialog(null,
          "Datum od je veæi od datuma do ili godine nisu identiène", "Greška",
             javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    return true;
	}
	
	private void jbInit() throws Exception {
		this.setSelDataSet(Distlist.getDataModule().getQueryDataSet());
		
    xYLayout1.setHeight(111);
    xYLayout1.setWidth(575);
    jpSelDoc.setLayout(xYLayout1);

    jlDatum.setText("Datum  (od - do)");
    jtfDATUMOD.setColumnName("DATUM");
    jtfDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMDO.setColumnName("DATUM");
    jtfDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    
    jlCSKL.setText("Skladište");
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[] {0,1});
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);

    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    
    jlDist.setText("Distributer");
    jlrDist.setColumnName("SIFDIST");
    jlrDist.setNavColumnName("CSIF");
    jlrDist.setColNames(new String[] {"NAZIV"});
    jlrDist.setTextFields(new JTextComponent[] {jlrNazDist});
    jlrDist.setVisCols(new int[] {0, 1});
    jlrDist.setSearchMode(0);
    jlrDist.setRaDataSet(Sifrarnici.getSifre("DIST"));
    jlrDist.setNavButton(jbDist);

    jlrNazDist.setColumnName("NAZIV");
    jlrNazDist.setNavProperties(jlrDist);
    jlrNazDist.setSearchMode(1);
    
    
    jpSelDoc.add(jlCSKL, new XYConstraints(15, 20, -1, -1));
    jpSelDoc.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jpSelDoc.add(jrfNAZSKL, new XYConstraints(260, 20, 275, -1));
    jpSelDoc.add(jbCSKL, new XYConstraints(539, 20, 21, 21));
    jpSelDoc.add(jlDist, new XYConstraints(15, 45, -1, -1));
    jpSelDoc.add(jlrDist, new XYConstraints(150, 45, 100, -1));
    jpSelDoc.add(jlrNazDist, new XYConstraints(260, 45, 275, -1));
    jpSelDoc.add(jbDist, new XYConstraints(539, 45, 21, 21));
    jpSelDoc.add(jlDatum, new XYConstraints(15, 70, -1, -1));
    jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 70, 100, -1));
    jpSelDoc.add(jtfDATUMDO, new XYConstraints(260, 70, 100, -1));
   
    addSelRange(jtfDATUMOD, jtfDATUMDO);
    
    this.setSelPanel(jpSelDoc);
	}
}
