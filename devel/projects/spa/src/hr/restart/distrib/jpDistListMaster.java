package hr.restart.distrib;

import java.awt.BorderLayout;

import hr.restart.baza.dM;
import hr.restart.robno.rapancart;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.Sifrarnici;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpDistListMaster extends JPanel {

	raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmDistList fdl;
  JPanel jpDetail = new JPanel();
  XYLayout lay = new XYLayout();
  
  JLabel jlCSKL = new JLabel();
	JlrNavField jrfCSKL = new JlrNavField();
	JlrNavField jrfNAZSKL = new JlrNavField();
	JraButton jbCSKL = new JraButton();
	
	JLabel jlDist = new JLabel();
	JlrNavField jlrDist = new JlrNavField();
	JlrNavField jlrNazDist = new JlrNavField();
	JraButton jbDist = new JraButton();
	
	JLabel jlDatum = new JLabel();
  JraTextField jtfDATUM = new JraTextField();
  
  rapancart rpc = new rapancart() {
  	public boolean isMyAfterLookup() {
  		 return false;
  	}
  };
  
  
  public jpDistListMaster(frmDistList md) {
    try {
      fdl = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void BindComponents(DataSet ds) {
  	jrfCSKL.setDataSet(ds);
  	jlrDist.setDataSet(ds);
  	jtfDATUM.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    lay.setHeight(230);
    lay.setWidth(655);
    jpDetail.setLayout(lay);

    jlDatum.setText("Datum distribucije");
    jtfDATUM.setColumnName("DATUM");
    jtfDATUM.setHorizontalAlignment(SwingConstants.CENTER);
    
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
    
    
    
    jpDetail.add(jlCSKL, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jrfCSKL, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jrfNAZSKL, new XYConstraints(260, 45, 345, -1));
    jpDetail.add(jbCSKL, new XYConstraints(609, 45, 21, 21));
    jpDetail.add(jlDist, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrDist, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jlrNazDist, new XYConstraints(260, 70, 345, -1));
    jpDetail.add(jbDist, new XYConstraints(609, 70, 21, 21));
    jpDetail.add(jlDatum, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jtfDATUM, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(rpc, new XYConstraints(0, 130, -1, -1));
    
    BindComponents(fdl.getMasterSet());
    
    this.add(jpDetail, BorderLayout.CENTER);
	}
  
  public void initRpc() {
  	rpc.setFocusCycleRoot(false);
    rpc.setMode("DOH");
    rpc.setDefParam();
    rpc.InitRaPanCart();
  }
}
