package hr.restart.distrib;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;

import hr.restart.baza.Condition;
import hr.restart.baza.Distkal;
import hr.restart.baza.Pjpar;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.Sifrarnici;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpDistListDetail extends JPanel {

	 raCommonClass rcc = raCommonClass.getraCommonClass();
	  dM dm = dM.getDataModule();
	  Valid vl = Valid.getValid();

	  frmDistList fdl;
	  JPanel jpDetail = new JPanel();
	  
	  XYLayout lay = new XYLayout();
	  JLabel jlAdr = new JLabel();
	  JLabel jlFax = new JLabel();
	  JLabel jlKol = new JLabel();
	  JLabel jlNaziv = new JLabel();
	  JLabel jlPar = new JLabel();

	  JraButton jbSelCpar = new JraButton();

	  JraTextField jraAdr = new JraTextField();
	  JraTextField jraEmadr = new JraTextField();
	  JraTextField jraFax = new JraTextField();
	  JraTextField jraKol = new JraTextField();
	  JraTextField jraMj = new JraTextField();
	  JraTextField jraNaziv = new JraTextField();
	  JraTextField jraPbr = new JraTextField();
	  JraTextField jraTelfax = new JraTextField();
	  JlrNavField jlrCpar = new JlrNavField();
	  JlrNavField jlrNazpar = new JlrNavField();


	  public jpDistListDetail(frmDistList f) {
	    try {
	      fdl = f;
	      jbInit();
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	  }

	  public void BindComponents(DataSet ds) {
	    jraAdr.setDataSet(ds);
	    jraEmadr.setDataSet(ds);
	    jraFax.setDataSet(ds);
	    jraKol.setDataSet(ds);
	    jraMj.setDataSet(ds);
	    jraNaziv.setDataSet(ds);
	    jraPbr.setDataSet(ds);
	    jraTelfax.setDataSet(ds);
	    jlrCpar.setDataSet(ds);
	  }

	  private void jbInit() throws Exception {
	    jpDetail.setLayout(lay);
	    lay.setWidth(546);
	    lay.setHeight(200);

	    jlAdr.setText("Adresa");
	    jlFax.setText("Tel/fax/e-mail");
	    jlKol.setText("Kolièine");
	    jlNaziv.setText("Naziv");
	    jlPar.setText("Partner");
	    jraAdr.setColumnName("ADR");
	    jraEmadr.setColumnName("EMADR");
	    jraFax.setColumnName("TEL");
	    jraKol.setColumnName("KOL");
	    jraMj.setColumnName("MJ");
	    jraNaziv.setColumnName("NAZIV");
	    jraPbr.setColumnName("PBR");
	    jraTelfax.setColumnName("TELFAX");

	    jlrCpar.setColumnName("CPAR");
	    jlrCpar.setColNames(new String[] {"NAZPAR"});
	    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
	    jlrCpar.setVisCols(new int[] {0, 1}); 
	    jlrCpar.setSearchMode(0);
	    jlrCpar.setRaDataSet(dm.getPartneri());
	    jlrCpar.setNavButton(jbSelCpar);

	    jlrNazpar.setColumnName("NAZPAR");
	    jlrNazpar.setNavProperties(jlrCpar);
	    jlrNazpar.setSearchMode(1);

//	***************
	    jpDetail.add(jbSelCpar, new XYConstraints(510, 20, 21, 21));
	    jpDetail.add(jlAdr, new XYConstraints(15, 95, -1, -1));
	    jpDetail.add(jlFax, new XYConstraints(15, 120, -1, -1));
	    jpDetail.add(jlKol, new XYConstraints(15, 150, -1, -1));
	    jpDetail.add(jlNaziv, new XYConstraints(15, 70, -1, -1));
	    jpDetail.add(jlPar, new XYConstraints(15, 20, -1, -1));
	    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 75, -1));
	    jpDetail.add(jlrNazpar, new XYConstraints(230, 20, 275, -1));
	    jpDetail.add(jraAdr, new XYConstraints(150, 95, 200, -1));
	    jpDetail.add(jraEmadr, new XYConstraints(360, 120, 145, -1));
	    jpDetail.add(jraFax, new XYConstraints(150, 120, 100, -1));
	    jpDetail.add(jraKol, new XYConstraints(150, 150, 85, -1));
	    jpDetail.add(jraMj, new XYConstraints(410, 95, 95, -1));
	    jpDetail.add(jraNaziv, new XYConstraints(150, 70, 355, -1));
	    jpDetail.add(jraPbr, new XYConstraints(355, 95, 50, -1));
	    jpDetail.add(jraTelfax, new XYConstraints(255, 120, 100, -1));
	    
	    BindComponents(fdl.getDetailSet());
	    
	    this.add(jpDetail, BorderLayout.CENTER);
	  }	  
}
