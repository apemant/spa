
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.JoppdA;
import hr.restart.baza.JoppdB;
import hr.restart.baza.Kumulorgarh;
import hr.restart.baza.Opcine;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Radnici;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.Radplsifre;
import hr.restart.baza.Sifrarnici;
import hr.restart.baza.Vrsteprim;
import hr.restart.baza.dM;
import hr.restart.pl.plUtil;
import hr.restart.pl.raIniciranje;
import hr.restart.pl.raIzvjestaji;
import hr.restart.pl.raOdbici;
import hr.restart.pl.raParam;
import hr.restart.pl.raPlObrRange;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raTransaction;
import hr.restart.util.raTwoTableFrame;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * 
 * fitchr req. - recount, mass update, real dopr 
 * b. fix - proporc. raspodjela
 * @author andrej
 */
public class JOPPDhndlr {
  frmPDV2 fPDV2;
  JraButton jbGet = new JraButton();
  JraButton jbA = new JraButton();
  JraButton jbSave = new JraButton();
  JPanel jpBtns;
  public JOPPDhndlr(frmPDV2 _fpdv2) {
    fPDV2 = _fpdv2;
    jbGet.setText("...");
    jbGet.setToolTipText("Dohvat prethodno snimljenih obrazaca");
    jbA.setText("A");
    jbA.setMargin(new Insets(0, 0, 0, 0));
    jbA.setToolTipText("Stranica A");
    jbSave.setIcon(raImages.getImageIcon(raImages.IMGSAVE));
    jbSave.setToolTipText("Snimi obrazac");
    
    setBSize(jbGet);
    setBSize(jbA);
    setBSize(jbSave);
    setMode("Dohvat");
    
    jbGet.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbGet_action();
      }
    });
    jbA.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbA_action();
      }
    });
    jbSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSave_action();
      }
    });
    jpBtns = new JPanel(new GridLayout(1, 0, 3, 0));
    jpBtns.add(jbGet);
    jpBtns.add(jbA);
    jpBtns.add(jbSave);
  }
  
  private void setBSize(JraButton b) {
    Dimension size = new Dimension(21, 21);
    b.setSize(size);
    b.setPreferredSize(size);
  }

  //JOPPD 
  protected StorageDataSet strAset = null;
  //protected String oib = null;
  public StorageDataSet getStrAset() {
    if (strAset == null) {
      strAset = new StorageDataSet();
      strAset.addColumn(dM.createStringColumn("OZNIZV","Oznaka izvje��a",5));
      strAset.addColumn(dM.createIntColumn("VRSTAIZV", "Vrsta izvje��a"));
      strAset.addColumn(dM.createStringColumn("OZNPOD", "Oznaka podnositelja",1));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRPL","V.1.1.Por.i.prir.na pla�u"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRMIR","V.1.2.Por.i.prir.na mirovinu"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRKAP","V.2.Por.i.prir.na kapital"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRIMO","V.3.Por.i.prir.na imovinu"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIROS","V.4.Por.i.prir.na osiguranje"));
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRDD","V.5.Por.i.prir.na drugi dohodak"));
      
      strAset.addColumn(dM.createBigDecimalColumn("PORPRIRKAM","V.6.Por.i.prir.na dohodak od kamata"));
      
      strAset.addColumn(dM.createBigDecimalColumn("MIO1PL","VI.1.1.MIO 1.stup na pla�u"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1DD","VI.1.2.MIO 1.stup na drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1POD","VI.1.3.MIO 1.stup na poduzetni�ku pla�u"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1PP","VI.1.4.MIO 1.stup posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1OO","VI.1.5.MIO 1.stup odre�ene okolnosti"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO1STAZ","VI.1.6.MIO 1.stup ben.sta�"));
      
      strAset.addColumn(dM.createBigDecimalColumn("MIO1SD","VI.1.7.MIO 1.stup samostalna djelatnost"));
      
      strAset.addColumn(dM.createBigDecimalColumn("MIO2PL","VI.2.1.MIO 2.stup na pla�u"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2DD","VI.2.2.MIO 2.stup na drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2POD","VI.2.3.MIO 2.stup na poduzetni�ku pla�u"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2PP","VI.2.4.MIO 2.stup posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("MIO2STAZ","VI.2.5.MIO 2.stup ben.sta�"));
      
      strAset.addColumn(dM.createBigDecimalColumn("MIO2SD","VI.2.6.MIO 2.stup samostalna djelatnost"));
      
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPL","VI.3.1.Zdravstveno os. iz pla�e"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRPL","VI.3.2.Za�tita na radu iz pla�e"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPOD","VI.3.3.Zdravstveno os. iz poduzetni�ke pla�e"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRPOD","VI.3.4.Za�tita na radu iz poduzetni�ke pla�e"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRDD","VI.3.5.Zdravstveno os. drugi dohodak"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRINO","VI.3.6.Zdravstveno os. u inozemstvu"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPENZ","VI.3.7.Zdravstveno os. umirovljenici"));
      strAset.addColumn(dM.createBigDecimalColumn("ZDRPP","VI.3.8.Zdravstveno os. posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRPP","VI.3.9.Za�tita na radu posebni propisi"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNROO","VI.3.10.Za�tita na radu odre�ene okolnosti"));
      
      strAset.addColumn(dM.createBigDecimalColumn("ZDRSD","VI.3.11.Zdravstveno os. za samostalnu djelatnost"));
      strAset.addColumn(dM.createBigDecimalColumn("ZASNRSD","VI.3.12.Za�tita na radu za samostalnu djelatnost"));
      
      strAset.addColumn(dM.createBigDecimalColumn("ZAP","VI.4.1.Zapo�ljavanje na pla�e"));
      strAset.addColumn(dM.createBigDecimalColumn("ZAPOSINV","VI.4.2.Zapo�ljavanje osoba s invaliditetom"));
      strAset.addColumn(dM.createBigDecimalColumn("ZAPOSPOD","VI.4.3.Zapo�ljavanje poduzetni�ka pla�a"));
      
      strAset.addColumn(dM.createBigDecimalColumn("ZAPOSSD","VI.4.4.Zapo�ljavanje samostalna djelatnost"));
      
      strAset.addColumn(dM.createBigDecimalColumn("NEOP","VII. Neoporezivi primici"));
      strAset.addColumn(dM.createBigDecimalColumn("KAMATA","VIII. Kamata MO2"));
      
      strAset.addColumn(dM.createBigDecimalColumn("NEOPNO","IX. Neoporezivi primici od neprofitnih organizacija"));
      
      strAset.addColumn(dM.createIntColumn("BROJINV", "X.1. Minimalni broj invalida za zaposliti"));
      strAset.addColumn(dM.createBigDecimalColumn("IZNOSINV","X.2. Iznos obra�unate naknade"));
      
      strAset.open();
    }
    return strAset;
  }

  public int getBrojOsoba() {
    try {
      getJPTV().enableEvents(false);
      oibs.clear();
      for (strBset.first();strBset.inBounds(); strBset.next()) {
         oibs.add(strBset.getString("OIB"));
      }
    } finally {
      getJPTV().enableEvents(true);
    }
    return oibs.size();
  }
  
  private raJPTableView getJPTV() {
    return fPDV2.getJPTV();
  }

  public int getBrojRedaka() {
    if (strBset == null) return 0;
    return strBset.rowCount();
  }

  public void sumStrA() {
    if (strAset == null || strBset == null) return;
    strAset.first();
    allZero(strAset);
    //recalc rbr
    int rbr = 0;
    for (strBset.first(); strBset.inBounds(); strBset.next()) {
//      rbr++;
//      strBset.setInt("RBR", rbr);
      //tweakovi 2
      if (isMaxOsnDopReached(strBset.getBigDecimal("BRUTO")) /*&& strBset.getBigDecimal("BRUTO").compareTo(strBset.getBigDecimal("OSNDOP"))>0*/) {
        strBset.setString("JOP", "0002");
        strBset.setBigDecimal("OSNDOP", strBset.getBigDecimal("BRUTO"));
      }
      
      addBigDec(strAset, strBset, new String[] {"PORPRIRPL","POR","PRIR"}, "JOS=0001-0039,0201,5403;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRMIR","POR","PRIR"}, "JOS=0101-0119,0121;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRKAP","POR","PRIR"}, "JOS=1001-1009;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRIMO","POR","PRIR"}, "JOS=2001-2009;");
      addBigDec(strAset, strBset, new String[] {"PORPRIROS","POR","PRIR"}, "JOS=3001-3009;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRDD","POR","PRIR"}, "JOS=4001-4009,5501;");
      addBigDec(strAset, strBset, new String[] {"PORPRIRKAM","POR","PRIR"}, "JOS=1101-1109;");

      addBigDec(strAset, strBset, new String[] {"MIO1PL","MIO1"}, "JOS=0001-0003,0005-0010,0021-0029,5701-5719;");
      addBigDec(strAset, strBset, new String[] {"MIO1DD","MIO1"}, "JOS=0201,4002;");
      addBigDec(strAset, strBset, new String[] {"MIO1POD","MIO1"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"MIO1PP","MIO1"}, "JOS=5401-5403,5608;");
      addBigDec(strAset, strBset, new String[] {"MIO1OO","MIO1"}, "JOS= 5302,5501,5604,5606,5607;");
      addBigDec(strAset, strBset, new String[] {"MIO1STAZ","MIO1STAZ"}, "");
      addBigDec(strAset, strBset, new String[] {"MIO1SD","MIO1"}, "JOS=0041-0049;");

      addBigDec(strAset, strBset, new String[] {"MIO2PL","MIO2"}, "JOS=0001-0003,0005-0010,0021-0029,5701-5719;");
      addBigDec(strAset, strBset, new String[] {"MIO2DD","MIO2"}, "JOS=0201,4002;");
      addBigDec(strAset, strBset, new String[] {"MIO2POD","MIO2"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"MIO2PP","MIO2"}, "JOS=5101-5103,5201-5299,5301,5401-5403,5608;");
      addBigDec(strAset, strBset, new String[] {"MIO2STAZ","MIO2STAZ"}, "");
      addBigDec(strAset, strBset, new String[] {"MIO2SD","MIO2"}, "JOS=0041-0049;");

      addBigDec(strAset, strBset, new String[] {"ZDRPL","ZDR"}, "JOS=0001,0005,0008,0009,0021-0029,5701;");
      addBigDec(strAset, strBset, new String[] {"ZASNRPL","ZASNR"}, "JOS=0001,0005,0008,0009,0021-0029,5701;");
      addBigDec(strAset, strBset, new String[] {"ZDRPOD","ZDR"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"ZASNRPOD","ZASNR"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"ZDRDD","ZDR"}, "JOS=0201,4002;");
      addBigDec(strAset, strBset, new String[] {"ZDRINO","ZDRINO"}, "JOS=0001-0039,5001-5009,5402;");      
      addBigDec(strAset, strBset, new String[] {"ZDRPENZ","ZDR"}, "JOS=0101-0119;");      
      addBigDec(strAset, strBset, new String[] {"ZDRPP","ZDR"}, "JOS=5401,5403,5601,5602,5603,5605,5608;");      
      addBigDec(strAset, strBset, new String[] {"ZASNRPP","ZASNR"}, "JOS=5401,5403,5601,5602,5603,5605,5608;");      
      addBigDec(strAset, strBset, new String[] {"ZASNROO","ZASNR"}, "JOS=5302,5501,5604,5606,5607;");
      addBigDec(strAset, strBset, new String[] {"ZAP","ZAP"}, "");
      addBigDec(strAset, strBset, new String[] {"ZAPOSINV","ZAPOSINV"}, "");
      addBigDec(strAset, strBset, new String[] {"ZDRSD","ZDR"}, "JOS=0041-0049;");
      addBigDec(strAset, strBset, new String[] {"ZASNRSD","ZASNR"}, "JOS=0041-0049;");
      
      addBigDec(strAset, strBset, new String[] {"ZAPOSPOD","ZAP"}, "JOS=0031-0039;");
      addBigDec(strAset, strBset, new String[] {"ZAPOSSD","ZAP"}, "JOS=0041-0049;");

      addBigDec(strAset, strBset, new String[] {"NEOP","NEOP"}, "");
      addBigDec(strAset, strBset, new String[] {"KAMATA","MIO2","MIO2STAZ"}, "JOP=5721;");

      strAset.post();
    }
    Aus.sub(strAset, "ZAP", "ZAPOSPOD");
    Aus.sub(strAset, "ZAP", "ZAPOSSD");
  }

  private void addBigDec(StorageDataSet sA, StorageDataSet sB, String[] augs, String cond) {
    boolean rez = false;
//System.out.println("*** COL: "+augs[0]+" ***");        
    if (cond == null || "".equals(cond.trim())) {
      rez = true;
    } else {
      StringTokenizer condflds = new StringTokenizer(cond,";");
      while (condflds.hasMoreTokens()) {
        String condf = condflds.nextToken();
        StringTokenizer f = new StringTokenizer(condf,"=");
        String fld = f.nextToken();
        String cnd = f.nextToken();
        String val = sB.getString(fld).trim();
        StringTokenizer cnds = new StringTokenizer(cnd, ",");
        while (cnds.hasMoreTokens()) {
          String c = cnds.nextToken();
          if (c.contains("-")) {
            try {
              StringTokenizer oddo = new StringTokenizer(c,"-");
              String _od = oddo.nextToken();
              String _do = oddo.nextToken();
              boolean b = val.compareTo(_od.trim()) >= 0 && val.compareTo(_do.trim()) <= 0;
//  System.out.println(val+" between "+_od+" and "+_do+" ... "+b);
              rez = rez || b;
            } catch (Exception e) {
              e.printStackTrace();
            }
            //range
          } else {
            boolean b = val.equals(c.trim());
//  System.out.println(val+" = "+c+" ... "+b);
            rez = rez || b;
          }
        }
      }
    }
    if (rez) { // do the add
      try {
        String cA = augs[0];
        for (int i = 1; i < augs.length; i++) {
          sA.setBigDecimal(cA, sA.getBigDecimal(cA).add(sB.getBigDecimal(augs[i])));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void allZero(StorageDataSet s) {
    allZero(s, null);
  }
  private void allZero(StorageDataSet s, String[] cols2zero) {
    Column[] c = s.getColumns();
    for (int i = 0; i < c.length; i++) {
      if (c[i].getDataType() == Variant.BIGDECIMAL && 
          (cols2zero==null || Arrays.asList(cols2zero).contains(c[i].getColumnName()))) {
        s.setBigDecimal(c[i].getColumnName(), Aus.zero2);
      }
    }
   if (s.hasColumn("BROJINV") != null)
     s.setInt("BROJINV", 0);
    s.post();
  }

  //Strana B
  protected StorageDataSet strBset = null;
  public StorageDataSet getStrBset() {
    if (strBset == null) {
      strBset = new StorageDataSet();
      strBset.addColumn(dM.createIntColumn("RBR","1.RBR"));
      strBset.addColumn(dM.createStringColumn("COPCINE","2.Op�ina stanovanja",5));
      strBset.addColumn(dM.createStringColumn("COPRADA","3.Op�ina rada",5));
      strBset.addColumn(dM.createStringColumn("OIB","4.OIB",20));
      strBset.addColumn(dM.createStringColumn("IMEPREZ","5.Ime i prezime",100));
      strBset.addColumn(dM.createStringColumn("JOS","6.1.Oznaka stjecatelja/osiguranika",4));
      strBset.addColumn(dM.createStringColumn("JOP","6.2.Oznaka primitka/obveze doprinosa",4));
      strBset.addColumn(dM.createStringColumn("JOB","7.1.Oznaka za sta� s pove�anim trajanjem",1));
      strBset.addColumn(dM.createStringColumn("JOZ","7.2.Obveza dopr.za zapo�ljavanje invalida",1));
      strBset.addColumn(dM.createStringColumn("JOM","8.Oznaka prvog/zadnjeg mj.u osig.po istoj osnovi",1));
      strBset.addColumn(dM.createStringColumn("JRV","9.Oznaka punog/nepunog radnog vremena",1));
      strBset.addColumn(dM.createIntColumn("SATI", "10.Sati rada"));
      strBset.addColumn(dM.createIntColumn("NSATI", "10.0.Neodra�eni sati"));
      strBset.addColumn(dM.createTimestampColumn("ODJ", "10.1.Razdoblje obr. od"));
      strBset.addColumn(dM.createTimestampColumn("DOJ", "10.2.Razdoblje obr. do"));

      strBset.addColumn(dM.createBigDecimalColumn("BRUTO","11.Iznos primitka (oporezivi)"));
      strBset.addColumn(dM.createBigDecimalColumn("OSNDOP","12.Osnovica za doprinose"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO1","12.1. Doprinos MIO 1.stup"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO2","12.2. Doprinos MIO 2.stup"));
      strBset.addColumn(dM.createBigDecimalColumn("ZDR","12.3. Doprinos za zdravstveno osiguranje"));
      strBset.addColumn(dM.createBigDecimalColumn("ZASNR","12.4. Doprinos za za�titu na radu"));
      strBset.addColumn(dM.createBigDecimalColumn("ZAP","12.5. Doprinos za zapo�ljavanje"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO1STAZ","12.6. MIO 1-pove�ani sta�"));
      strBset.addColumn(dM.createBigDecimalColumn("MIO2STAZ","12.7. MIO 2-pove�ani sta�"));
      strBset.addColumn(dM.createBigDecimalColumn("ZDRINO","12.8. Zdravstveno u inozemstvu"));
      strBset.addColumn(dM.createBigDecimalColumn("ZAPOSINV","12.9. Dopr.za zap.invalida"));
      strBset.addColumn(dM.createBigDecimalColumn("IZDATAK","13.1. Izdatak"));
      strBset.addColumn(dM.createBigDecimalColumn("IZDATAKMIO","13.2. Izdatak-upla�eni dopr.za MIO"));
      strBset.addColumn(dM.createBigDecimalColumn("DOHODAK","13.3. Dohodak"));
      strBset.addColumn(dM.createBigDecimalColumn("ISKNEOP","13.4. Osobni odbitak"));
      strBset.addColumn(dM.createBigDecimalColumn("POROSN","13.5. Porezna osnovica"));
      strBset.addColumn(dM.createBigDecimalColumn("POR","14.1. Porez"));
      strBset.addColumn(dM.createBigDecimalColumn("PRIR","14.2. Prirez"));
      strBset.addColumn(dM.createStringColumn("JNP","15.1. Oznaka neoporezivog primitka",2));
      strBset.addColumn(dM.createBigDecimalColumn("NEOP","15.2. Iznos neoporezivog primitka"));
      strBset.addColumn(dM.createStringColumn("JNI","16.1. Oznaka na�ina isplate",1));
      strBset.addColumn(dM.createBigDecimalColumn("NETOPK","16.2. Iznos za isplatu"));
      strBset.addColumn(dM.createBigDecimalColumn("BRUTOOBR","17. Obra�unata pla�a"));
      strBset.open();
    }
    return strBset;  
    
  }
  private void addStrBSet(String cn, BigDecimal aug) {
    if (samojedna) {
      if (multipliers.containsKey(cn)) {
        strBset.setBigDecimal(cn, rs.getBigDecimal((String)multipliers.get(cn)));
      } else {
        strBset.setBigDecimal(cn, rs.getBigDecimal(cn));
      }
    } else {
      Aus.add(strBset, cn, aug);
    }
  }
  QueryDataSet rpls;
  HashSet oibs = null;
  boolean samojedna = false;
  HashMap multipliers = new HashMap();
  private StorageDataSet rs;
  String currOIB = null;
  /**
   * The ... gulp!
   */
  boolean needmatch;
  protected void doJOPPD() {
    
    boolean iszapinv = isZapInv();
    multipliers.clear();
    multipliers.put("BRUTO", "BRUTOMJ");
    multipliers.put("OSNDOP", "BRUTO");
    multipliers.put("ZDR", "ZO");
    multipliers.put("ZAP", "ZAPOS");
    multipliers.put("ISKNEOP", "OSODB");
    multipliers.put("POR", "POREZ");
    multipliers.put("PRIR", "PRIREZ");
    oibs = new HashSet();
    
    String[] p = raOdbici.RAZPOREZA_param;
    String[] r = raOdbici.RAZPRIREZA_param;
    String rpor = raOdbici.getInstance().getVrsteOdbKeysQuery(p[0],p[1],p[2],p[3],false)[0];
    String rprir = raOdbici.getInstance().getVrsteOdbKeysQuery(r[0],r[1],r[2],r[3],false)[0];
    
    String vpor = raOdbici.getInstance().getVrsteOdbKeysQuery("OPVR","S","2","4", false)[0];
    String vprir = raOdbici.getInstance().getVrsteOdbKeysQuery("OP","S","2","5", false)[0];
    
    String mio1naq = raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.JOPPD_6);
    String mio2naq = raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.JOPPD_7);
    
    String mio1vo = raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.RS_IV_8);
    String mio2vo = raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.RS_IV_9);
    
  if (currOIB == null) {
    //strA
    getStrAset();
    strAset.empty();
    strAset.insertRow(false);
    strAset.setString("OZNIZV", getOZNJOPPD(fPDV2.getDatumOd()));
    strAset.setInt("VRSTAIZV", 1);
    strAset.setString("OZNPOD",frmParam.getParam("pl", "jpod"+dlgGetKnjig.getKNJCORG(), "1", "Vrsta podnositelja JOPPD za knjigovodstvo "+dlgGetKnjig.getKNJCORG()));
    //strB
    getStrBset();
    strBset.empty();
    rs = getRSperiod();
    QueryDataSet razl = null;
    
    QueryDataSet porezi = myrange == null ? 
        Aus.q("SELECT cradnik,cvrodb,sum(obriznos) as obriznos from odbiciobr where cvrodb in (" + vpor + "," + vprir + ") and rbr=1 group by cradnik,cvrodb") :
          Aus.q("SELECT cradnik,cvrodb,sum(obriznos) as obriznos from odbiciarh where cvrodb in (" + vpor + "," + vprir + 
              ") and rbr=1 and " + myrange.getQuery() + " group by cradnik,cvrodb");
        
    QueryDataSet mio1 = myrange == null ? 
        Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciobr where " + mio1vo + " and rbr=1 group by cradnik") :
          Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciarh where " + mio1vo + 
              " and rbr=1 and " + myrange.getQuery() + " group by cradnik");
        
    QueryDataSet mio2 = myrange == null ? 
       Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciobr where " + mio2vo + " and rbr=1 group by cradnik") :
          Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciarh where " + mio2vo + 
              " and rbr=1 and " + myrange.getQuery() + " group by cradnik");
        
    QueryDataSet mio1na = mio1naq == null || mio1naq.length() == 0 ? null : (myrange == null ?
        Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciobr WHERE " + mio1naq + " GROUP BY cradnik") :
          Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciarh WHERE " + mio1naq + " AND " + myrange.getQuery() + " GROUP BY cradnik"));
        
    QueryDataSet mio2na = mio2naq == null || mio2naq.length() == 0 ? null : (myrange == null ?
        Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciobr WHERE " + mio2naq + " GROUP BY cradnik") :
          Aus.q("SELECT cradnik,sum(obriznos) as obriznos from odbiciarh WHERE " + mio2naq + " AND " + myrange.getQuery() + " GROUP BY cradnik"));
    
    if (rpor != null && rprir != null) {
      if (myrange == null)
        razl = Aus.q("SELECT cradnik,cvrodb,obriznos from odbiciobr where cvrodb in (" + rpor + "," + rprir + ")");
      else razl = Aus.q("SELECT cradnik,cvrodb,obriznos from odbiciarh where cvrodb in (" + rpor + "," + rprir + 
          ") and " + myrange.getQuery());
    }
    
    HashMap oos = new HashMap();
    for (rs.first(); rs.inBounds(); rs.next()) {
      HashSet oosr = (HashSet) oos.get(rs.getString("CRADNIK"));
      if (oosr == null) oos.put(rs.getString("CRADNIK"), oosr = new HashSet());
      oosr.add(rs.getString("RSOO"));
    }
    HashSet godcrad = new HashSet();
    //needmatch = oos.size() > 1;
    
    int rbr = 0;
    HashMap nari = new HashMap();
    for (rs.first(); rs.inBounds(); rs.next()) 
      if (isNarav(rs.getShort("CVRP")+""))
        nari.put(rs.getString("JMBG"), rs.getBigDecimal("PBTO"));
    
    HashSet maxo = new HashSet();
    HashMap nodb = new HashMap();
    HashMap ndop = new HashMap();
    HashMap npor = new HashMap();
    HashMap nprir = new HashMap();
    
    //1st pass: zbroji pbto i sati vp za redove koji �e ionako bit zbrojeni:
    StorageDataSet nrs = new StorageDataSet();
    nrs.setColumns(rs.cloneColumns());
    nrs.addColumn(dM.createIntColumn("NSATI"));
    nrs.open();
    HashMap oldrow = new HashMap();
    
    for (rs.first(); rs.inBounds(); rs.next()) {
      boolean naknada = isNaknada(rs.getShort("CVRP")+"");
      if (getSifraFor("JOS", rs).trim().equals("0001") && naknada) continue;//prijevoz !?!?!
      
      HashSet oosr = (HashSet) oos.get(rs.getString("CRADNIK"));
      needmatch = oosr.size() > 1;
      if (needmatch && !isOOmatch(rs)) continue;
      
      String _jmbg = rs.getString("JMBG");
      String _jos = getSifraFor("JOS", rs); 
      String _jop = getSifraFor("JOP", rs);
      String _job = getSifraFor("JOB", rs);
      String _joz = getSifraFor("JOZ", rs);
      String _jom = getSifraFor("JOM", rs);
      String _jrv = getSifraFor("JRV", rs);
      String _jni = getSifraFor("JNI", rs);
      String _jnp = getSifraFor("JNP", rs);
      String _datum;
      if (_jni.equals("5")) {
        _datum = getDatum(rs, "FIRST")+"";
      } else {
        _datum = getDatum(rs, "ODDANA")+"";
      }
      
      String key = _jmbg + ":" + _jos + ":" +_jop + ":" + _job + ":" + _joz + ":" + _jom + ":" + _jrv + ":" +_jnp + ":" + _datum;
      System.out.println(key);
      
      if (oldrow.containsKey(key)) {
        System.out.println("key exists!");
        nrs.goToRow(((Integer) oldrow.get(key)).intValue());
        Aus.add(nrs, "PBTO", rs);
        Aus.add(nrs, "SATIVP", rs);
      } else {
        nrs.insertRow(false);
        rs.copyTo(nrs);
        nrs.setInt("NSATI", 0);
        nrs.post();
        oldrow.put(key, new Integer(nrs.getRow()));
      } 
      if (lookupData.getlookupData().raLocate(dM.getDataModule().getVrsteprim(), "CVRP", rs.getShort("CVRP")+"") &&
          dM.getDataModule().getVrsteprim().getString("NEOD").equalsIgnoreCase("D"))
        nrs.setInt("NSATI", (nrs.getInt("NSATI")+rs.getBigDecimal("SATIVP").intValue()));
    }
    
    System.out.println("old rows " + rs.rowCount());
    System.out.println("new rows " + nrs.rowCount());
    rs = nrs;
    
    for (rs.first(); rs.inBounds(); rs.next()) {
      boolean naknada = isNaknada(rs.getShort("CVRP")+"");
      boolean isnarav = isNarav(rs.getShort("CVRP")+"");
      if (getSifraFor("JOS", rs).trim().equals("0001") && naknada) continue;//prijevoz !?!?!
      BigDecimal zasnr = Aus.zero2;
      BigDecimal zapinv = Aus.zero2;
      BigDecimal omjer = Aus.zero2;
      
      HashSet oosr = (HashSet) oos.get(rs.getString("CRADNIK"));
      needmatch = oosr.size() > 1;
      
      if (needmatch && !isOOmatch(rs)) continue;
      //rs.BRUTOMJ = pravibruto
      //rs.BRUTO = osnovica dop. (max)
      if (rs.getBigDecimal("BRUTOMJ").signum()!=0) omjer = rs.getBigDecimal("PBTO").divide(rs.getBigDecimal("BRUTOMJ"),10,BigDecimal.ROUND_HALF_UP);
      else if (rs.getBigDecimal("PBTO").signum()==0) omjer = Aus.one0;
      
      BigDecimal om2 = omjer;
      BigDecimal narir = (BigDecimal) nari.get(rs.getString("JMBG"));
      if (narir != null && narir.compareTo(rs.getBigDecimal("BRUTOMJ")) < 0)
        om2 = rs.getBigDecimal("PBTO").divide(rs.getBigDecimal("BRUTOMJ").subtract(narir),8,BigDecimal.ROUND_HALF_UP);
      if (isnarav) om2 = Aus.zero2;
      
      System.out.println("omjer: " + omjer + "  " + om2);
      
/*****D*E*B*U*G***B*R*I*�*I*******/
//System.out.println(strBset);
//System.err.println(VarStr.join( new String[] {rs.getString("JMBG"), 
//          getSifraFor("JOS", rs),getSifraFor("JOP", rs),getSifraFor("JOB", rs),getSifraFor("JOZ", rs),getSifraFor("JOM", rs), 
//          getSifraFor("JRV", rs),getSifraFor("JNP", rs),getSifraFor("JNI", rs), 
//          getDatum(rs, "ODDANA")+""}, '#'));
/*****D*E*B*U*G***B*R*I*�*I*******/
      String _jmbg = rs.getString("JMBG");
      String _jos = getSifraFor("JOS", rs); 
      String _jop = getSifraFor("JOP", rs);
      String _job = getSifraFor("JOB", rs);
      String _joz = getSifraFor("JOZ", rs);
      String _jom = getSifraFor("JOM", rs);
      String _jrv = getSifraFor("JRV", rs);
      String _jni = getSifraFor("JNI", rs);
      String _jnp = getSifraFor("JNP", rs);
      String _datum;
      if (_jni.equals("5")) {
        _datum = getDatum(rs, "FIRST")+"";
      } else {
        _datum = getDatum(rs, "ODDANA")+"";
      }
      
      System.out.println(_jmbg + ", " + _jos + ", " + _jop + ", " + _job + ", " + _joz + ", " + _jom + ", " + _jrv + ", " + _jnp + ", " + _datum);
      
      if (lookupData.getlookupData().raLocate(strBset, new String[] {"OIB","JOS","JOP","JOB","JOZ","JOM","JRV",/*"JNI",*/"JNP","ODJ"}, 
          new String[] {_jmbg, _jos, _jop, _job, _joz, _jom, _jrv,/*_jni,*/_jnp, _datum})) {
        System.out.println("isti red!");
        if (rs.getBigDecimal("PBTO").add(strBset.getBigDecimal("BRUTO")).subtract(rs.getBigDecimal("BRUTOMJ")).abs().compareTo(new BigDecimal("0.1"))<=0) {
          //sve je zbrajano u jedan red - treba uzeti gotove sume a ne omjer
          samojedna = true;
        } else {
          samojedna = false;
        }
        addStrBSet("BRUTO", rs.getBigDecimal("PBTO"));
        strBset.setInt("SATI", (strBset.getInt("SATI")+rs.getBigDecimal("SATIVP").intValue()));
        
        /*
        if (lookupData.getlookupData().raLocate(dM.getDataModule().getVrsteprim(), "CVRP", rs.getShort("CVRP")+"") &&
            dM.getDataModule().getVrsteprim().getString("NEOD").equalsIgnoreCase("D"))
          strBset.setInt("NSATI", (strBset.getInt("NSATI")+rs.getBigDecimal("SATIVP").intValue()));*/
        
        strBset.setInt("NSATI", (strBset.getInt("NSATI")+rs.getInt("NSATI")));
        
        if (isMaxOsnDopReached(strBset.getBigDecimal("BRUTO")))
          maxo.add(rs.getString("JMBG"));
        
        if (samojedna) {
          strBset.setBigDecimal("OSNDOP", rs.getBigDecimal("BRUTO"));
//          addStrBSet("OSNDOP", rs.getBigDecimal("BRUTO"));
        } else {
          addStrBSet("OSNDOP", rs.getBigDecimal("PBTO"));
        }
        
        if (strBset.getBigDecimal("OSNDOP").compareTo(getMinOsnDop("1")) < 0)
          strBset.setBigDecimal("OSNDOP", getMinOsnDop("1"));
        
        BigDecimal nmio1 = Aus.zero0;
        BigDecimal nmio2 = Aus.zero0;
        if (lookupData.getlookupData().raLocate(mio1, "CRADNIK", rs.getString("CRADNIK"))) 
          nmio1 = mio1.getBigDecimal("OBRIZNOS");
        if (lookupData.getlookupData().raLocate(mio2, "CRADNIK", rs.getString("CRADNIK"))) 
          nmio2 = mio2.getBigDecimal("OBRIZNOS");
        if (isnarav) {
          strBset.setBigDecimal("MIO1", Aus.zero0);
          strBset.setBigDecimal("MIO2", Aus.zero0);
        } else {
          addStrBSet("MIO1", rs.getBigDecimal("MIO1").subtract(nmio1).multiply(om2).setScale(2, BigDecimal.ROUND_HALF_UP));
          addStrBSet("MIO2", rs.getBigDecimal("MIO2").subtract(nmio2).multiply(om2).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        //temp hack
        if (rs.getBigDecimal("ZO").signum() != 0) {
          zasnr = rs.getBigDecimal(samojedna?"BRUTOMJ":"PBTO").multiply(new BigDecimal("0.005")).setScale(2,BigDecimal.ROUND_HALF_UP);
          zapinv = iszapinv ?
              rs.getBigDecimal(samojedna?"BRUTOMJ":"PBTO").multiply(new BigDecimal("0.001")).setScale(2,BigDecimal.ROUND_HALF_UP)
              :Aus.zero2;
        }
        
        if (samojedna) {
          strBset.setBigDecimal("ZDR", rs.getBigDecimal("ZO").subtract(zasnr));
//          strBset.setBigDecimal("ZAP", rs.getBigDecimal("ZAPOS").subtract(zapinv));
          strBset.setBigDecimal("ZASNR", zasnr);
          strBset.setBigDecimal("ZAPOSINV", zapinv);
        } else {
          addStrBSet("ZDR", rs.getBigDecimal("ZO").multiply(omjer).setScale(2,BigDecimal.ROUND_HALF_UP).subtract(zasnr));
//          addStrBSet("ZAP", rs.getBigDecimal("ZAPOS").multiply(omjer).subtract(zapinv));
          addStrBSet("ZASNR", zasnr);
          addStrBSet("ZAPOSINV", zapinv);
        }
        addStrBSet("ZAP", rs.getBigDecimal("ZAPOS").multiply(omjer).setScale(2,BigDecimal.ROUND_HALF_UP));
        if (strBset.getBigDecimal("BRUTO").signum() > 0) {
          strBset.setBigDecimal("IZDATAKMIO", strBset.getBigDecimal("MIO1").add(strBset.getBigDecimal("MIO2")));
          strBset.setBigDecimal("DOHODAK",
              strBset.getBigDecimal("BRUTO")
              .subtract(strBset.getBigDecimal("IZDATAKMIO"))
//            .subtract(strBset.getBigDecimal("IZDATAK"))
              );
          addStrBSet("ISKNEOP",rs.getBigDecimal("OSODB").multiply(om2).setScale(2,BigDecimal.ROUND_HALF_UP));
        }

        strBset.setBigDecimal("POROSN",strBset.getBigDecimal("DOHODAK").subtract(strBset.getBigDecimal("ISKNEOP")));
        if (!isnarav) checkNodb(nodb);
        addStrBSet("POR", rs.getBigDecimal("POREZ").multiply(om2).setScale(2,BigDecimal.ROUND_HALF_UP));
        addStrBSet("PRIR", rs.getBigDecimal("PRIREZ").multiply(om2).setScale(2,BigDecimal.ROUND_HALF_UP));
        if (_jop.equals("0000") && !_jnp.equals("0")) {
          strBset.setString("JNP", _jnp);
          addStrBSet("NEOP", rs.getBigDecimal("PBTO"));
          //strBset.setBigDecimal("NEOP", rs.getBigDecimal("PBTO")); //naknada posebno
        } else if (rs.getBigDecimal("NAKNADE").signum() > 0 && !_jnp.equals("0")) {
          strBset.setString("JNP", _jnp);
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("NAKNADE")); //kumulrad
        } else {
          strBset.setString("JNP", "0");
          strBset.setBigDecimal("NEOP", Aus.zero2);
        }
        strBset.setString("JNI", _jni);
        strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
            .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR")))
            .add(strBset.getBigDecimal("NEOP"))
            );
        if (naknada) strBset.setBigDecimal("NETOPK",strBset.getBigDecimal("NEOP"));
      } else {
        System.out.println("dodajem red!");
        strBset.insertRow(false);
        strBset.setInt("RBR", ++rbr);
        strBset.setString("COPCINE", getOpcinaStanovanja(rs.getString("CRADNIK")));
        strBset.setString("COPRADA", "0"+raIzvjestaji.convertCopcineToRS(rs.getString("COPCINE")));
        strBset.setString("OIB", rs.getString("JMBG"));
        strBset.setString("IMEPREZ", rs.getString("IME")+" "+rs.getString("PREZIME"));
//        strBset.setString("JOS", getSifraFor("JOS",rs));
//        strBset.setString("JOP", getSifraFor("JOP",rs));
//        strBset.setString("JOB", getSifraFor("JOB",rs));
//        strBset.setString("JOZ", getSifraFor("JOZ",rs));
//        strBset.setString("JOM", getSifraFor("JOM",rs));
//        strBset.setString("JRV", getSifraFor("JRV",rs));
        strBset.setString("JOS", _jos);
        strBset.setString("JOP", _jop);
        strBset.setString("JOB", _job);
        strBset.setString("JOZ", _joz);
        strBset.setString("JOM", _jom);
        strBset.setString("JRV", _jrv);
        strBset.setInt("SATI", rs.getBigDecimal("SATIVP").intValue());
        
        strBset.setInt("NSATI", rs.getInt("NSATI"));
        /*if (lookupData.getlookupData().raLocate(dM.getDataModule().getVrsteprim(), "CVRP", rs.getShort("CVRP")+"") &&
            dM.getDataModule().getVrsteprim().getString("NEOD").equalsIgnoreCase("D"))
          strBset.setInt("NSATI", rs.getBigDecimal("SATIVP").intValue());
        else strBset.setInt("NSATI", 0);*/
        
        strBset.setTimestamp("ODJ", getDatum(rs, "ODDANA"));
        strBset.setTimestamp("DOJ", getDatum(rs, "DODANA"));
        strBset.setBigDecimal("BRUTO", rs.getBigDecimal("PBTO"));
        if (isMaxOsnDopReached(rs.getBigDecimal("PBTO"))) {
          strBset.setBigDecimal("OSNDOP", rs.getBigDecimal("BRUTO"));
          maxo.add(rs.getString("JMBG"));
        } else {
          strBset.setBigDecimal("OSNDOP", rs.getBigDecimal("PBTO"));
        }
        if (strBset.getBigDecimal("OSNDOP").compareTo(getMinOsnDop("1")) < 0)
          strBset.setBigDecimal("OSNDOP", getMinOsnDop("1"));
        
        BigDecimal nmio1 = Aus.zero0;
        BigDecimal nmio2 = Aus.zero0;
        if (lookupData.getlookupData().raLocate(mio1, "CRADNIK", rs.getString("CRADNIK"))) 
          nmio1 = mio1.getBigDecimal("OBRIZNOS");
        if (lookupData.getlookupData().raLocate(mio2, "CRADNIK", rs.getString("CRADNIK"))) 
          nmio2 = mio2.getBigDecimal("OBRIZNOS");
        
        if (isnarav) {
          strBset.setBigDecimal("MIO1", nmio1);
          strBset.setBigDecimal("MIO2", nmio2);
        } else {
          strBset.setBigDecimal("MIO1", rs.getBigDecimal("MIO1").subtract(nmio1).multiply(om2).setScale(2, BigDecimal.ROUND_HALF_UP));
          strBset.setBigDecimal("MIO2", rs.getBigDecimal("MIO2").subtract(nmio2).multiply(om2).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        
        // mio na placu hack
        
        if (lookupData.getlookupData().raLocate(mio1na, "CRADNIK", rs)) {
          strBset.setBigDecimal("MIO1STAZ", mio1na.getBigDecimal("OBRIZNOS"));
          strBset.setString("JOB", "1");
        }
        if (lookupData.getlookupData().raLocate(mio2na, "CRADNIK", rs)) {
          strBset.setBigDecimal("MIO2STAZ", mio2na.getBigDecimal("OBRIZNOS"));
          strBset.setString("JOB", "1");
        }
        
        //temp hack
        if (rs.getBigDecimal("ZO").signum() != 0) {
          zasnr = rs.getBigDecimal("PBTO").multiply(new BigDecimal("0.005")).setScale(2,BigDecimal.ROUND_HALF_UP);
//          zasnr = rs.getBigDecimal(samojedna?"BRUTO":"PBTO").multiply(new BigDecimal("0.005")).setScale(2,BigDecimal.ROUND_HALF_UP);
          zapinv = iszapinv ?
              rs.getBigDecimal("PBTO").multiply(new BigDecimal("0.001")).setScale(2,BigDecimal.ROUND_HALF_UP)
              :Aus.zero2;

          System.out.println("ZASNR! " +strBset.getBigDecimal("OSNDOP") + omjer + " " + zasnr);
        }
        strBset.setBigDecimal("ZDR", rs.getBigDecimal("ZO").multiply(omjer).setScale(2,BigDecimal.ROUND_HALF_UP).subtract(zasnr));
        strBset.setBigDecimal("ZASNR", zasnr);
        strBset.setBigDecimal("ZAP", rs.getBigDecimal("ZAPOS").multiply(omjer).setScale(2,BigDecimal.ROUND_HALF_UP));
        strBset.setBigDecimal("ZAPOSINV",zapinv);
        if (strBset.getBigDecimal("BRUTO").signum() > 0) {
          strBset.setBigDecimal("IZDATAKMIO", strBset.getBigDecimal("MIO1").add(strBset.getBigDecimal("MIO2")));
          strBset.setBigDecimal("DOHODAK",
            strBset.getBigDecimal("BRUTO")
            .subtract(strBset.getBigDecimal("IZDATAKMIO"))
//            .subtract(strBset.getBigDecimal("IZDATAK"))
            );
          strBset.setBigDecimal("ISKNEOP",rs.getBigDecimal("OSODB").multiply(om2).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        strBset.setBigDecimal("POROSN",strBset.getBigDecimal("DOHODAK").subtract(strBset.getBigDecimal("ISKNEOP")));
        if (!isnarav) checkNodb(nodb);
        
        strBset.setBigDecimal("POR", rs.getBigDecimal("POREZ").multiply(om2).setScale(2,BigDecimal.ROUND_HALF_UP));
        strBset.setBigDecimal("PRIR", rs.getBigDecimal("PRIREZ").multiply(om2).setScale(2,BigDecimal.ROUND_HALF_UP));
        if (_jop.equals("0000") && !_jnp.equals("0")) {
          strBset.setString("JNP", _jnp);
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("PBTO")); //naknada posebno
        } else if (rs.getBigDecimal("NAKNADE").signum() > 0 && !_jnp.equals("0") && !oibs.contains(rs.getString("JMBG"))) {
          strBset.setString("JNP", _jnp);
          strBset.setBigDecimal("NEOP", rs.getBigDecimal("NAKNADE").multiply(omjer).setScale(2,BigDecimal.ROUND_HALF_UP)); //kumulrad
        } else {
          strBset.setString("JNP", "0");
          strBset.setBigDecimal("NEOP", Aus.zero2);
        }
        strBset.setString("JNI", _jni);
        strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
            .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR")))
            .add(strBset.getBigDecimal("NEOP"))
            //rs.getBigDecimal("NETOPK")
            );
        if (naknada) strBset.setBigDecimal("NETOPK",strBset.getBigDecimal("NEOP"));
        strBset.setBigDecimal("BRUTOOBR", rs.getBigDecimal("BRUTOMJ"));
        if (narir != null)
          strBset.setBigDecimal("BRUTOOBR", rs.getBigDecimal("BRUTOMJ").subtract(narir));
        if (strBset.getString("JNI").equals("5")) {
          strBset.setTimestamp("ODJ", Util.getUtil().getFirstDayOfYear(fPDV2.getDatumOd()));
          strBset.setTimestamp("DOJ", Util.getUtil().getLastDayOfYear(fPDV2.getDatumOd()));
          strBset.setBigDecimal("BRUTOOBR", Aus.zero2);
        }
      }
      //tweakovi
      if (naknada) {
        allZero(strBset, new String[] {"BRUTO","OSNDOP","MIO1","MIO2","ZDR","ZASNR","ZAP","ZAPOSINV","IZDATAKMIO","DOHODAK","ISKNEOP","POROSN","POR","PRIR","BRUTOOBR"});
        if (strBset.getBigDecimal("NEOP").signum() == 0) strBset.setBigDecimal("NEOP", strBset.getBigDecimal("NETOPK"));
      }
      if (strBset.getString("JRV").equals("0")) {//nema radnog vremena
        strBset.setString("COPRADA","00000");
      }

      if (strBset.getString("JOP").equals("0041")) {
        allZero(strBset, new String[] {"BRUTO","IZDATAKMIO","DOHODAK","ISKNEOP","POROSN","POR","PRIR","NEOP","NETOPK"});
        strBset.setString("JNP", "0");
//        strBset.setBigDecimal("BRUTO", Aus.zero2);
//        strBset.setBigDecimal("IZDATAKMIO", Aus.zero2);
//        strBset.setBigDecimal("DOHODAK", Aus.zero2);
//        strBset.setBigDecimal("ISKNEOP", Aus.zero2);
//        strBset.setBigDecimal("POROSN", Aus.zero2);
//        strBset.setBigDecimal("POR", Aus.zero2);
//        strBset.setBigDecimal("PRIR", Aus.zero2);
//        strBset.setBigDecimal("NEOP", Aus.zero2);
//        strBset.setBigDecimal("NETOPK", Aus.zero2);
      }      
      if (strBset.getString("JOP").equals("0051")) {
        allZero(strBset, new String[] {"OSNDOP","MIO1","MIO2","ZDR","ZASNR","ZAP","ZAPOSINV"});
      }
      if (strBset.getString("JOP").equals("0021") || strBset.getString("JOP").startsWith("4")) {
        Timestamp dan = new Timestamp(strBset.getTimestamp("ODJ").getTime());
        strBset.setTimestamp("ODJ", Util.getUtil().getFirstDayOfYear(dan));
        strBset.setTimestamp("DOJ", Util.getUtil().getLastDayOfYear(dan));
        strBset.setBigDecimal("BRUTOOBR", Aus.zero2);
      }
      if (strBset.getString("JOP").startsWith("4")) {
        Aus.add(strBset, "ZDR", "ZASNR");
        Aus.clear(strBset, "ZASNR");
      }
      
      String cradnik = rs.getString("CRADNIK");
      if (strBset.getString("JNI").equals("5")) {
        if (lookupData.getlookupData().raLocate(porezi, new String[] {"CRADNIK", "CVRODB"}, new String[] {cradnik, vpor})) {
          npor.put(rs.getString("JMBG"), porezi.getBigDecimal("OBRIZNOS").subtract(strBset.getBigDecimal("POR")));
          strBset.setBigDecimal("POR", porezi.getBigDecimal("OBRIZNOS"));
        }
        
        if (lookupData.getlookupData().raLocate(porezi, new String[] {"CRADNIK", "CVRODB"}, new String[] {cradnik, vprir})) {
          nprir.put(rs.getString("JMBG"), porezi.getBigDecimal("OBRIZNOS").subtract(strBset.getBigDecimal("PRIR")));
          strBset.setBigDecimal("PRIR", porezi.getBigDecimal("OBRIZNOS"));
        }
        DataRow dops = new DataRow(strBset);
        strBset.copyTo(dops);
        dops.setBigDecimal("ISKNEOP", rs.getBigDecimal("OSODB"));
        ndop.put(rs.getString("JMBG"), dops);
        strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
            .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR"))));
      }
      
      oibs.add(rs.getString("JMBG"));
      strBset.post();
      System.out.println(strBset.rowCount() + " redova");
      
      if (!godcrad.contains(rs.getString("JMBG")) && 
          razl != null && lookupData.getlookupData().raLocate(razl, "CRADNIK", cradnik)) {
        BigDecimal por = Aus.zero0;
        BigDecimal prir = Aus.zero0;
        if (lookupData.getlookupData().raLocate(razl, new String[] {"CRADNIK", "CVRODB"}, 
            new String[] {cradnik, rpor})) por = razl.getBigDecimal("OBRIZNOS");
        if (lookupData.getlookupData().raLocate(razl, new String[] {"CRADNIK", "CVRODB"}, 
            new String[] {cradnik, rprir})) prir = razl.getBigDecimal("OBRIZNOS");
        if (por.add(prir).signum() < 0) {
          godcrad.add(rs.getString("JMBG"));
 /*         Aus.sub(strBset, "POR", por);
          Aus.sub(strBset, "PRIR", prir);
          Aus.add(strBset, "NETOPK", por);
          Aus.add(strBset, "NETOPK", prir);*/
          DataRow old = new DataRow(strBset);
          strBset.copyTo(old);
          Timestamp dan = new Timestamp(strBset.getTimestamp("ODJ").getTime());
          
          strBset.insertRow(false);
          strBset.setInt("RBR", ++rbr);
          dM.copyColumns(old, strBset, new String[] {"COPCINE", "COPRADA", "OIB", "IMEPREZ", "JOB", "JOZ", "JOM", "JRV", "JNI"});
          
          strBset.setString("JOS", "0001");
          strBset.setString("JOP", "0406");
          strBset.setString("JOM", "3");
          strBset.setString("JRV", "1");
          strBset.setInt("SATI",0);
          strBset.setInt("NSATI", 0);
          strBset.setTimestamp("ODJ", Util.getUtil().getFirstDayOfYear(dan));
          strBset.setTimestamp("DOJ", Util.getUtil().getLastDayOfYear(dan));
          allZero(strBset);
          strBset.setString("JNP", "0");
          strBset.setBigDecimal("POR", por);
          strBset.setBigDecimal("PRIR", prir);
          strBset.setBigDecimal("NETOPK", por.add(prir).negate());
          strBset.post();
        }
      }
    }
    
    for (strBset.first(); strBset.inBounds(); strBset.next())
      if (strBset.getString("JNI").equals("5")) {
        BigDecimal odb = (BigDecimal) nodb.get(strBset.getString("OIB"));
        if (odb != null) {
          strBset.setBigDecimal("ISKNEOP", odb);
          strBset.setBigDecimal("POROSN", strBset.getBigDecimal("DOHODAK").subtract(odb));
        }
        if (maxo.contains(strBset.getString("OIB"))) {
          if (strBset.getBigDecimal("IZDATAKMIO").signum() == 0)
            strBset.setString("JOP", "0022");
          /*strBset.setBigDecimal("MIO1", Aus.zero2);
          strBset.setBigDecimal("MIO2", Aus.zero2);
          strBset.setBigDecimal("IZDATAKMIO", Aus.zero2);
          strBset.setBigDecimal("DOHODAK", strBset.getBigDecimal("BRUTO"));
          strBset.setBigDecimal("POROSN",strBset.getBigDecimal("DOHODAK").subtract(strBset.getBigDecimal("ISKNEOP")));
          strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
              .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR")))
              .add(strBset.getBigDecimal("NEOP")));*/
        }
      }

    for (Iterator i = npor.keySet().iterator(); i.hasNext(); ) {
      String oib = (String) i.next();
      if (findBestMatch(oib)) {
      /*if (lookupData.getlookupData().raLocate(strBset, new String[] {"OIB", "JOS", "JOP"},
          new String[] {oib, "0001", "0001"}) ||
          (lookupData.getlookupData().raLocate(strBset, new String[] {"OIB", "JOS", "JOP"},
              new String[] {oib, "0001", "0002"}))) {*/
        Aus.sub(strBset, "POR", (BigDecimal) npor.get(oib));
        Aus.add(strBset, "NETOPK", (BigDecimal) npor.get(oib));
        if (nprir.containsKey(oib)) {
          Aus.sub(strBset, "PRIR", (BigDecimal) nprir.get(oib));
          Aus.add(strBset, "NETOPK", (BigDecimal) nprir.get(oib));
        }
        /*if (maxo.contains(strBset.getString("OIB"))) {
          DataRow dop = (DataRow) ndop.get(strBset.getString("OIB"));
          String[] cc = {"MIO1", "MIO2", "IZDATAKMIO"};
          for (int c = 0; c < cc.length; c++)
            Aus.add(strBset, cc[c], dop);
          strBset.setBigDecimal("DOHODAK", strBset.getBigDecimal("BRUTO")
              .subtract(strBset.getBigDecimal("IZDATAKMIO")));
          strBset.setBigDecimal("POROSN",strBset.getBigDecimal("DOHODAK").subtract(strBset.getBigDecimal("ISKNEOP")));
          strBset.setBigDecimal("NETOPK", strBset.getBigDecimal("DOHODAK")
              .subtract(strBset.getBigDecimal("POR").add(strBset.getBigDecimal("PRIR")))
              .add(strBset.getBigDecimal("NEOP")));
        }*/
      }
    }
    
    for (Iterator i = godcrad.iterator(); i.hasNext(); ) {
      String oib = (String) i.next();
      if (lookupData.getlookupData().raLocate(strBset, new String[] {"OIB", "JOP"},
          new String[] {oib, "0406"})) {
        BigDecimal por = strBset.getBigDecimal("POR");
        BigDecimal prir = strBset.getBigDecimal("PRIR");
        if (lookupData.getlookupData().raLocate(strBset, new String[] {"OIB", "JOS", "JOP"},
            new String[] {oib, "0001", "0001"}) ||
            lookupData.getlookupData().raLocate(strBset, new String[] {"OIB", "JOS", "JOP"},
                new String[] {oib, "0001", "0021"})) {
          Aus.sub(strBset, "POR", por);
          Aus.sub(strBset, "PRIR", prir);
          Aus.add(strBset, "NETOPK", por);
          Aus.add(strBset, "NETOPK", prir);
          strBset.post();
        } else System.out.println("CAN'T FIND OIB " + oib);
      }
    }

    currOIB = getKnjCurrOIB();
    sumStrA();
  } //currOib
    strBset.setSort(new SortDescriptor(new String[] {"RBR"}));
    strBset.setTableName("JOPPDB");
    getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fPDV2.setDataSet(strBset);
    getJPTV().removeAllTableModifiers();
    fPDV2.killAllReports();
    fPDV2.addReport("hr.restart.pl.repJOPPDDisk", "Datoteka JOPPD za e-poreznu");
    fPDV2.setTitle("Obrazac JOPPD za dan "+raDateUtil.getraDateUtil().dataFormatter(fPDV2.getDatumOd()));
    setMode("Strana A");
    jbGet.setEnabled(true);
//    jraPoctDat.setEnabled(true);
    raCommonClass.getraCommonClass().setLabelLaF(fPDV2.jraPoctDat, true);
  }
  
  private boolean findBestMatch(String oib) {
    int best = -1;
    for (strBset.first(); strBset.inBounds(); strBset.next())
      if (strBset.getString("OIB").equals(oib) && !strBset.getString("JNI").equals("5")) {
        if (best < 0) best = strBset.getRow();
        if (strBset.getString("JOS").equals("0001") &&
            (strBset.getString("JOP").equals("0001") ||
            strBset.getString("JOP").equals("0002"))) return true;
      }
    if (best < 0) return false;
    strBset.goToRow(best);
    return true;
  }
  
  private void checkNodb(HashMap nodb) {
    if (strBset.getBigDecimal("POROSN").signum() < 0) {
      BigDecimal old = (BigDecimal) nodb.get(strBset.getString("OIB"));
      if (old == null) old = Aus.zero2;
      nodb.put(strBset.getString("OIB"), old.subtract(strBset.getBigDecimal("POROSN")));
      strBset.setBigDecimal("POROSN", Aus.zero2);
      strBset.setBigDecimal("ISKNEOP", strBset.getBigDecimal("DOHODAK"));
    }
  }

  private boolean isMaxOsnDopReached(BigDecimal bto) {
    try {
      String smaxo = hr.restart.sisfun.frmParam.getParam("pl","maxosn1"/*HC!!!+_doprinosi.getShort("CVRODB")*/, "0", "Maksimalna osnovica za doprinos 1");
      if ("0".equals(smaxo)) return false;
      BigDecimal maxo = new BigDecimal(smaxo);
      return bto.compareTo(maxo) > 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return false;
  }
  
  private BigDecimal getMinOsnDop(String cpr) {
    try {
      String smaxo = hr.restart.sisfun.frmParam.getParam("pl","minosn" + cpr/*HC!!!+_doprinosi.getShort("CVRODB")*/, "0", "Minimalna osnovica za doprinos " + cpr);
      if ("0".equals(smaxo)) return Aus.zero2;
      return new BigDecimal(smaxo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return Aus.zero2;
  }

  private boolean isZapInv() {
    return frmParam.getParam("pl", "jzapinv"+hr.restart.zapod.OrgStr.getKNJCORG(), "N", "Uklju�iti doprinos za zap.inv. u JOPPD za "+hr.restart.zapod.OrgStr.getKNJCORG()).equalsIgnoreCase("D");
  }
  protected String getKnjCurrOIB() {
    QueryDataSet oibset = Aus.q("SELECT OIB from logotipovi WHERE corg = '"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
    oibset.open();
    oibset.first();
    return oibset.getString("OIB");
  }
  
  private boolean isNaknada(String cvrp) {
    Vrsteprim.getDataModule().getQueryDataSet().open();
    if (lookupData.getlookupData().raLocate(Vrsteprim.getDataModule().getQueryDataSet(), "CVRP", cvrp)) {
      return Vrsteprim.getDataModule().getQueryDataSet().getString("PARAMETRI").toUpperCase().startsWith("NN");
    }
    return false;
  }
  
  private boolean isNarav(String cvrp) {
    Vrsteprim.getDataModule().getQueryDataSet().open();
    if (lookupData.getlookupData().raLocate(Vrsteprim.getDataModule().getQueryDataSet(), "CVRP", cvrp)) {
      return !Vrsteprim.getDataModule().getQueryDataSet().isNull("CVRODB");
    }
    return false;
  }

  private Timestamp getDatum(StorageDataSet rs, String col) {
    Calendar c = Calendar.getInstance();
    int y, m, d;
    if (rs.hasColumn("GODOBR") != null) {
      y = rs.getShort("GODOBR");
      m = (col.equals("FIRST"))?1:rs.getShort("MJOBR");
    } else {
      dM.getDataModule().getOrgpl().open();
      raIniciranje.getInstance().posOrgsPl(rs.getString("CORG"));
      y = dM.getDataModule().getOrgpl().getShort("GODOBR");
      m = (col.equals("FIRST"))?1:dM.getDataModule().getOrgpl().getShort("MJOBR");
    }
    d = (col.equals("FIRST"))?1:rs.getShort(col);
    c.set(y, m-1, d);
    return Util.getUtil().getFirstSecondOfDay(new Timestamp(c.getTimeInMillis()));
  }

  private QueryDataSet vrstaprim;
  private QueryDataSet getVrstaPrim(short cvrp) {
    if (vrstaprim == null) {
      vrstaprim = Vrsteprim.getDataModule().copyDataSet();
    }
    vrstaprim.open();
    if (lookupData.getlookupData().raLocate(vrstaprim, "CVRP", cvrp+"")) return vrstaprim;
    return null;
  }
  // da li je RS00 na 
  private boolean isOOmatch(ReadWriteRow rs) {
    try {
      String rs_OO = rs.getString("RSOO");
      String vp_OO = getVrstaPrim(rs.getShort("CVRP")).getString("RSOO");
      if (vp_OO.trim().equals("")) vp_OO = "10";
      //rs.setString("RSOO",  vp_OO);
      return rs_OO.equals(vp_OO);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }
  private String getSifraFor(String col, ReadRow rs) {
    if (rpls == null) {
      rpls = Radplsifre.getDataModule().getTempSet();
      rpls.open();
    }
    String cradnik = rs.getString("CRADNIK");
    String cvrp = "P"+rs.getShort("CVRP");
    if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", cvrp)) {
      String vrstaprim_rsOO = getVrstaPrim(rs.getShort("CVRP")).getString("RSOO");
      if (needmatch && !vrstaprim_rsOO.equals(rs.getString("RSOO"))) {
        //ako se razlikuje RSOO na periodu i vrsti primanja treba pronaci sifru za RSOO sa perioda
        for (vrstaprim.first(); vrstaprim.inBounds(); vrstaprim.next()) {
          if (vrstaprim.getString("RSOO").equals(rs.getString("RSOO"))) {
            if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", "P"+vrstaprim.getShort("CVRP"))) {
System.err.println(
    "cradnik:" + rs.getString("CRADNIK")
    + "  CVRP:" +rs.getShort("CVRP")
    + "  rs.RSOO:" +rs.getString("RSOO")
    + "  vp.RSOO:" +vrstaprim_rsOO
    + "  returnForCol "+col+" = "+rpls.getString(col)
    );
              return rpls.getString(col);
            }
          }
        }
      }
    } else if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", cradnik)) {
//      return rpls.getString(col);
    } else if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", "#"+OrgStr.getKNJCORG())) {//2.7
      //
    } else if (lookupData.getlookupData().raLocate(rpls, "CRADNIK", "#")) {
//      return rpls.getString(col);
    } else {
      rpls.insertRow(false);
//      rpls.setString("CRADNIK", "#");
//      rpls.setString("JOM", "3");
//      rpls.setString("JNP", "19");
      setDefaultRPLS(rpls);
      rpls.post();
      rpls.saveChanges();
    }
    return rpls.getString(col);
  }

  private void getAllSifreFor(String[] cols, ReadWriteRow rs) {
    if (cols == null) cols = new String[] {"JOS","JOP","JOB","JOZ","JOM","JRV","JNP","JNI"};
    for (int i = 0; i < cols.length; i++) {
      if (rs.hasColumn(cols[i]) != null) rs.setString(cols[i], getSifraFor(cols[i], rs)); 
    }
  }
  protected static void setDefaultRPLS(ReadWriteRow r) {
    r.setString("CRADNIK", "#");
    r.setString("JOS", "0001");
    r.setString("JOP", "0001");
    r.setString("JOB", "0");
    r.setString("JOZ", "0");
    r.setString("JOM", "3");
    r.setString("JRV", "1");
    r.setString("JNP", "19");
    r.setString("JNI", "1");
  }
  
  private String getOpcinaStanovanja(String cradnik) {
    QueryDataSet rpl = Aus.q("SELECT copcine from radnicipl where cradnik='"+cradnik+"'");
    rpl.open();
    rpl.first();
    return "0"+raIzvjestaji.convertCopcineToRS(rpl.getString("COPCINE"));
  }

  raPlObrRange myrange;
  private QueryDataSet getRSperiod() {
    int m1, m2;
    String qry = "SELECT rsperiodobr.*, radnici.corg, radnici.ime, radnici.prezime, kumulrad.sati as satiuk, kumulrad.naknade, primanjaobr.cvrp, primanjaobr.bruto as PBTO, primanjaobr.sati as sativp FROM rsperiodobr, radnici, kumulrad, primanjaobr where "+
        " radnici.cradnik = rsperiodobr.cradnik AND kumulrad.cradnik = rsperiodobr.cradnik AND primanjaobr.cradnik = rsperiodobr.cradnik AND ";
    QueryDataSet orgpl = Orgpl.getDataModule().getTempSet(Condition.equal("CORG", OrgStr.getKNJCORG()));
    orgpl.open();
    orgpl.first();
    myrange = null;
//    Calendar c = Calendar.getInstance();
//    c.setTimeInMillis(orgpl.getTimestamp("DATUMISPL").getTime());
//    m1 = c.get(Calendar.MONTH);
//    c.setTimeInMillis(getDatumOd().getTime());
//    m2 = c.get(Calendar.MONTH);
//    if (m1 == m2) {
    if ("O".equals(raParam.getParam(orgpl,raParam.ORGPL_STATUS)) && Util.getUtil().getFirstSecondOfDay(orgpl.getTimestamp("DATUMISPL")).equals(Util.getUtil().getFirstSecondOfDay(fPDV2.getDatumOd()))) {
      String orgqrs = getOrgqrs();
      
      qry = qry+orgqrs;
    } else {      
      qry = qry+" 0=1";
      QueryDataSet orgarh = Kumulorgarh.getDataModule().getTempSet(
          Condition.between("DATUMISPL", Util.getUtil().getFirstDayOfMonth(fPDV2.getDatumOd()), Util.getUtil().getLastDayOfMonth(fPDV2.getDatumOd()))
          .and(Condition.equal("KNJIG", OrgStr.getKNJCORG()))
          );
      orgarh.open();
      orgarh.first();
      raPlObrRange range = new raPlObrRange(orgarh.getShort("GODOBR"),orgarh.getShort("MJOBR"), orgarh.getShort("RBROBR"));
      long diff = Long.MAX_VALUE;
      if (orgarh.getRowCount() > 1) {
        for (orgarh.first(); orgarh.inBounds(); orgarh.next()) {
          long d = Math.abs(orgarh.getTimestamp("DATUMISPL").getTime() - fPDV2.getDatumOd().getTime());
          if (d<diff) {
            range = new raPlObrRange(orgarh.getShort("GODOBR"),orgarh.getShort("MJOBR"), orgarh.getShort("RBROBR"));
            diff = d;
          }
        }
      }
      if (JOptionPane.showConfirmDialog(fPDV2, 
          "Prona�en je obra�un za "+range.getMJOBRfrom()+". mjesec "+range.getGODOBRfrom()+". godine. Izraditi obrazac za taj obra�un?",
          "Pitanje",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        qry = "SELECT rsperiodarh.*, radnici.corg, radnici.ime, radnici.prezime, kumulradarh.sati as satiuk, kumulradarh.naknade, Primanjaarh.cvrp, Primanjaarh.bruto as PBTO, Primanjaarh.sati as sativp FROM rsperiodarh, radnici, kumulradarh, primanjaarh where "+
            " radnici.cradnik = rsperiodarh.cradnik AND kumulradarh.cradnik = rsperiodarh.cradnik AND Primanjaarh.cradnik = rsperiodarh.cradnik AND "
        		+ /*OrgStr.getCorgsKnjigCond().qualified("radnici")*/ getOrgqrs() 
            +" AND rsperiodarh.godobr="+range.getGODOBRfrom()
            +" AND rsperiodarh.mjobr="+range.getMJOBRfrom()
            +" AND rsperiodarh.rbrobr="+range.getRBROBRfrom()
            +" AND kumulradarh.godobr="+range.getGODOBRfrom()
            +" AND kumulradarh.mjobr="+range.getMJOBRfrom()
            +" AND kumulradarh.rbrobr="+range.getRBROBRfrom()
            +" AND Primanjaarh.godobr="+range.getGODOBRfrom()
            +" AND Primanjaarh.mjobr="+range.getMJOBRfrom()
            +" AND Primanjaarh.rbrobr="+range.getRBROBRfrom()
            ;
        raOdbici.getInstance().setObrRange(myrange = range);
      }
    }
    System.out.println(qry);
    QueryDataSet rs = Aus.q(qry);
    rs.open();
    rs.setSort(new SortDescriptor(new String[] {"JMBG"}));
    return rs;
  }

  private String getOrgqrs() {
    String orgqrs = "(";
    orgqrs = orgqrs + OrgStr.getCorgsKnjigCond().qualified("radnici");
    String joincorg = frmParam.getParam("pl", "jopjoin"+OrgStr.getKNJCORG(), "", "Koju jo� O.J. spojiti na isti joppd");
    if (!joincorg.equals("")) {
      orgqrs = orgqrs + " OR " + OrgStr.getCorgsCond(joincorg).qualified("radnici");
    }
    orgqrs = orgqrs + ")";
    return orgqrs;
  }

  public static String getOZNJOPPD(Timestamp datum) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(datum.getTime());
    return String.valueOf(c.get(Calendar.YEAR)).substring(2)
        +Valid.getValid().maskZeroInteger(Integer.valueOf(c.get(Calendar.DAY_OF_YEAR)), 3);
  }
  
  JraDialog getJOPPD_BDialog() {
    JraDialog dlg = getDialogForSet(strBset, true);
    startFrame.getStartFrame().centerFrame(dlg, 0, "JOPPD strana B - izmjena");
    return dlg;
  }
  
  JraDialog getJOPPD_ADialog() {
    JraDialog dlg = getDialogForSet(strAset, false);
    startFrame.getStartFrame().centerFrame(dlg, 0, "JOPPD strana A - izmjena");
    return dlg;
  }

  private JraDialog getDialogForSet(final StorageDataSet set, final boolean recalc) {
    return getDialogForSet(set, recalc, new Runnable() {
      public void run() {
        set.post();
        int pos = set.getRow();
        if (recalc) {
          try {
            getJPTV().enableEvents(false);
            if (!isAutoSumLimit()) sumStrA();
            set.goToRow(pos);
          } finally {
            getJPTV().enableEvents(true);
          }
        } else {
          saveJOPPD();
          fPDV2.getJPTV().fireTableDataChanged();
        }
      }
    }, new Runnable() {
      
      public void run() {
        set.cancel();
      }
    });
  }
  
  public static JraDialog getDialogForSet(final StorageDataSet set, final boolean recalc, final Runnable okAction, final Runnable cancelAction) {
    final JraDialog dlg = new JraDialog();
    final List fields = new ArrayList();
    JPanel contpane = new JPanel(new GridLayout(0,1));
    Column[] cols = set.getColumns();
    for (int i = 0; i < cols.length; i++) {
      XYLayout xyl = new XYLayout(570,30);
      JPanel row = new JPanel(xyl);
      row.add(new JLabel(cols[i].getCaption()), new XYConstraints(15,5,-1,-1));
      JraTextField jt = new JraTextField();
      jt.setColumnName(cols[i].getColumnName());
      jt.setDataSet(cols[i].getDataSet());
      fields.add(jt);
      int size = getJTSize(cols[i]);
      JraButton bget = getJTButton(cols[i], dlg);
      int bsize = 0;
      if (bget!=null) {
        bsize = 26;
        row.add(bget, new XYConstraints(539, 5, 21, 21));
      }
      row.add(jt, new XYConstraints(560-size-bsize, 5, size, 21));
      contpane.add(row);
    }
    OKpanel okp = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        cancelAction.run();
        unregisterOKPanelKeys(dlg);
        for (Iterator i = fields.iterator(); i.hasNext(); ) {
          JraTextField tf = (JraTextField) i.next();
          tf.setDataSet(null);
        }
        dlg.dispose();
      }
      
      public void jBOK_actionPerformed() {
        okAction.run();
        unregisterOKPanelKeys(dlg);
        for (Iterator i = fields.iterator(); i.hasNext(); ) {
          JraTextField tf = (JraTextField) i.next();
          tf.setDataSet(null);
        }
        dlg.dispose();
      }
    };
    dlg.getContentPane().setLayout(new BorderLayout());
    dlg.getContentPane().add(new JScrollPane(contpane), BorderLayout.CENTER);
    dlg.getContentPane().add(okp, BorderLayout.SOUTH);
    dlg.setMinimumSize(new Dimension(600, 500));
    dlg.pack();
    okp.registerOKPanelKeys(dlg);
    return dlg;
  }

  private static JraButton getJTButton(final Column c, final JraDialog dlg) {
    JraButton b = null;
    if (c.getColumnName().toUpperCase().startsWith("COP")) {
      b = new JraButton();
      b.setText("...");
      final QueryDataSet opcine = Opcine.getDataModule().getTempSet();
      opcine.open();
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String[] rez = lookupData.getlookupData().lookUp(dlg, opcine, new int[] {0,1});
            if (rez!=null) c.getDataSet().setString(c.getColumnName(), "0"+raIzvjestaji.convertCopcineToRS(opcine.getString("COPCINE")));
        }
      });
    } else if (c.getColumnName().equalsIgnoreCase("OIB") || c.getColumnName().equalsIgnoreCase("IMEPREZ")) {
      b = new JraButton();
      b.setText("...");
      final QueryDataSet radnici = Aus.q("SELECT radnici.cradnik, radnici.ime, radnici.prezime, radnicipl.oib, radnicipl.copcine "
          + "FROM radnici, radnicipl where radnici.cradnik = radnicipl.cradnik and "
          + OrgStr.getCorgsKnjigCond().qualified("radnici"));
      
      radnici.open();
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String[] rez = lookupData.getlookupData().lookUp(dlg, radnici, new int[] {0,1,2,3});
            if (rez!=null) {
              c.getDataSet().setString("IMEPREZ", radnici.getString("IME")+" "+radnici.getString("PREZIME"));
              c.getDataSet().setString("OIB", radnici.getString("OIB"));
              c.getDataSet().setString("COPCINE", "0"+raIzvjestaji.convertCopcineToRS(radnici.getString("COPCINE")));
            }
        }
      });
      
    } else {
      if (c.getColumnName().equalsIgnoreCase("ODJ") || c.getColumnName().equalsIgnoreCase("DOJ")) return null; 
      b = new JraButton();
      b.setText("...");
      String vrsif = c.getColumnName().equalsIgnoreCase("OZNPOD")?"PLPD":"PL"+c.getColumnName().substring(1);
      if (vrsif.trim().length()>4) return null;
      final QueryDataSet sifre = Sifrarnici.getDataModule().getTempSet(Condition.equal("VRSTASIF", vrsif));
      sifre.open();
      if (sifre.getRowCount() == 0) return null;
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String[] rez = lookupData.getlookupData().lookUp(dlg, sifre, new int[] {0,2,3});
            if (rez!=null) c.getDataSet().setString(c.getColumnName(), sifre.getString("CSIF"));
        }
      });
    }
    return b;
  }

  private static int getJTSize(Column c) {
    if (c.getDataType() == Variant.STRING) {
      if (c.getPrecision() > 5) return 250;
      return 60;
    }
    if (c.getDataType() == Variant.TIMESTAMP) return 100;
    if (c.getDataType() == Variant.INT) return 50;
    return 160;
  }
  
  /**
   * zbrojeno porez i prirez nesamostalni rad /A.P1/
   */
  public BigDecimal getSumPorPrirUk() {
    return strAset.getBigDecimal("PORPRIRPL").add(strAset.getBigDecimal("PORPRIRMIR"));
  }
  /**
   * zbrojeno porez i prirez nesam. rad /A.P11
   */
  public BigDecimal getSumPorPrirPl() {
    return strAset.getBigDecimal("PORPRIRPL");
  }
  /**
   * zbrojeno porez i prirez umrvljenici /A.P12
   */
  public BigDecimal getSumPorPrirMir() {
    return strAset.getBigDecimal("PORPRIRMIR");
  }
  /**
   * porez i prirez na kapitaliste /A.P2
   */
  public BigDecimal getSumPorPrirKap() {
    return strAset.getBigDecimal("PORPRIRKAP");
  }
  
  /**
   * porez i prirez na feudalce /A.P3
   */
  public BigDecimal getSumPorPrirImo() {
    return strAset.getBigDecimal("PORPRIRIMO");
  }
  
  /**
   * porez i prirez na kamatare /A.P4
   */
  public BigDecimal getSumPorPrirOs() {
    return strAset.getBigDecimal("PORPRIROS");
  }
  
  /**
   * porez i prirez na honorarce /A.P5
   */
  public BigDecimal getSumPorPrirDD() {
    return strAset.getBigDecimal("PORPRIRDD");
  }
  
  /**
   * porez i prirez na kamate /A.P6
   */
  public BigDecimal getSumPorPrirKam() {
    return strAset.getBigDecimal("PORPRIRKAM");
  }

  /**
   * doprinos za MIO1 pla�e /A.gen.P1
   */
  public BigDecimal getSumMIO1Pl() {
    return strAset.getBigDecimal("MIO1PL");
  }
  /**
   * doprinos za MIO1 za drugi dohodak - honorarce /A.gen.P2
   */
  public BigDecimal getSumMIO1DD() {
    return strAset.getBigDecimal("MIO1DD");
  }
  /**
   * doprinos za MIO1 poduzetnicka placa /A.gen.P3
   */
  public BigDecimal getSumMIO1Pod() {
    return strAset.getBigDecimal("MIO1POD");
  }
  /**
   * doprinos za MIO1 posebni propisi /A.gen.P4
   */
  public BigDecimal getSumMIO1PP() {
    return strAset.getBigDecimal("MIO1PP");
  }
  /**
   * doprinos za MIO1 odredene okolnosti /A.gen.P5
   */
  public BigDecimal getSumMIO1OO() {
    return strAset.getBigDecimal("MIO1OO");
  }
  /**
   * doprinos za MIO1 staz sa povecanim trajanjem /A.gen.P6
   */
  public BigDecimal getSumMIO1Staz() {
    return strAset.getBigDecimal("MIO1STAZ");
  }
  
  /**
   * doprinos za MIO1 samostalna djelatnost /A.gen.P7
   */
  public BigDecimal getSumMIO1SD() {
    return strAset.getBigDecimal("MIO1SD");
  }
  
  /**
   * doprinos za MIO2 pla�e /A.kap.P1
   */
  public BigDecimal getSumMIO2Pl() {
    return strAset.getBigDecimal("MIO2PL");  
  }
  /**
   * doprinos za MIO2 za drugi dohodak - honorarce /A.kap.P2
   */
  public BigDecimal getSumMIO2DD() {
    return strAset.getBigDecimal("MIO2DD");  
  }
  /**
   * doprinos za MIO2 poduzetnicka placa /A.kap.P3
   */
  public BigDecimal getSumMIO2Pod() {
    return strAset.getBigDecimal("MIO2POD");  
  }
  /**
   * doprinos za MIO2 posebni propisi /A.kap.P4
   */
  public BigDecimal getSumMIO2PP() {
    return strAset.getBigDecimal("MIO2PP");  
  }
  /**
   * doprinos za MIO2 staz sa povecanim trajanjem /A.kap.P5
   */
  public BigDecimal getSumMIO2Staz() {
    return strAset.getBigDecimal("MIO2STAZ");  
  }
  
  /**
   * doprinos za MIO2 samostalna djelatnost /A.gen.P7
   */
  public BigDecimal getSumMIO2SD() {
    return strAset.getBigDecimal("MIO2SD");
  }
  
  /**
   * doprinos zdravstvo pla�e /A.zos.P1
   */
  public BigDecimal getSumZdrPl() {
    return strAset.getBigDecimal("ZDRPL");  
  }
  /**
   * doprinos za�tite na radu pla�e /A.zos.P2
   */
  public BigDecimal getSumZasNRPl() {
    return strAset.getBigDecimal("ZASNRPL");  
  }
  /**
   * doprinos zdravstvo poduzetnicka placa /A.zos.P3
   */
  public BigDecimal getSumZdrPod() {
    return strAset.getBigDecimal("ZDRPOD");  
  }
  /**
   * doprinos zastite na radu poduzetnicka placa /A.zos.P4
   */
  public BigDecimal getSumZasNRPod() {
    return strAset.getBigDecimal("ZASNRPOD");  
  }
  /**
   * doprinos zdravstvo drugi dohodak - honorarci /A.zos.P5
   */
  public BigDecimal getSumZdrDD() {
    return strAset.getBigDecimal("ZDRDD");  
  }
  /**
   * doprinos zdravstvo u inozemstvu /A.zos.P6
   */
  public BigDecimal getSumZdrINO() {
    return strAset.getBigDecimal("ZDRINO");  
  }
  /**
   * doprinos zdravstvo za korisnike mirovina /A.zos.P7
   */
  public BigDecimal getSumZdrPenzici() {
    return strAset.getBigDecimal("ZDRPENZ");  
  }
  /**
   * doprinos zdreavstvo po posebnim propisima /A.zos.P8
   */
  public BigDecimal getSumZdrPP() {
    return strAset.getBigDecimal("ZDRPP");  
  }
  /**
   * doprinos zastite na radu posebni propisi /A.zos.P9
   */
  public BigDecimal getSumZasNRPP() {
    return strAset.getBigDecimal("ZASNRPP");  
  }
  /**
   * doprinos zastite na radu odre�ene okolnosti /A.zos.P10
   */
  public BigDecimal getSumZasNROO() {
    return strAset.getBigDecimal("ZASNROO");  
  }
  
  public BigDecimal getSumZdrSD() {
    return strAset.getBigDecimal("ZDRSD");  
  }
  
  public BigDecimal getSumZasSD() {
    return strAset.getBigDecimal("ZASNRSD");  
  }
  
  /**
   * doprinos za zaposljavanje /A.zap.P1
   */
  public BigDecimal getSumZap() {
    return strAset.getBigDecimal("ZAP");  
  }
  /**
   * doprinos za zaposljavanje osoba s invaliditetom /A.zap.P2
   */
  public BigDecimal getSumZapOsInv() {
    return strAset.getBigDecimal("ZAPOSINV");  
  }
  
  public BigDecimal getSumZapPoduz() {
    return strAset.getBigDecimal("ZAPOSPOD");
  }
  
  public BigDecimal getSumZapSD() {
    return strAset.getBigDecimal("ZAPOSSD");
  }
  
  /**
   * isplaceni neoporezivi primici
   */
  public BigDecimal getSumNeoporeziviPrimici() {
    return strAset.getBigDecimal("NEOP");  
  }
  /**
   * naplacena kamata za MIO2
   */
  public BigDecimal getKamataMO2() {
    return strAset.getBigDecimal("KAMATA");  
  }
  
  public BigDecimal getNeopNeprofit() {
    return strAset.getBigDecimal("NEOPNO");  
  }
  
  public int getBrojInvalida() {
    return strAset.getInt("BROJINV");
  }
  
  public BigDecimal getIznosInv() {
    return strAset.getBigDecimal("IZNOSINV");  
  }
  
  void jbGet_action() {
      QueryDataSet getSet = Aus.q("SELECT IDIZV, DATUM from JoppdA order by idizv");
      lookupFrame lf = lookupFrame.getLookupFrame(fPDV2.getJframe(), getSet, new int[] {0,1});
      lf.setTitle("Dohvat pohranjenih obrazaca");
      lf.ShowCenter();
      if (lf.getRetValuesUI() != null) {
        loadJOPPD(getSet.getString("IDIZV"));
      }
  }
  void jbA_action() {
    /*if (isAutoSumLimit()) {
      if (JOptionPane.showConfirmDialog(null, "Prezbrojiti podatke sa strane B", "Pitanje", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        sumStrA();
      }
    }*/
    
    try {
      getJPTV().enableEvents(false);
      sumStrA();
    } finally {
      getJPTV().enableEvents(true);
    }
    
    strAset.setString("OZNIZV", getOZNJOPPD(fPDV2.getDatumOd()));
    getJOPPD_ADialog().show();
  }
  
  void jbSave_action() {
    saveJOPPD();
  }
  private boolean isAutoSumLimit() {
    int lt = 0; 
    try {
      lt = Integer.parseInt(frmParam.getParam("pl", "jopAutoSum", "200", "Do koliko redova JOPPD-a automatski sumira"));
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return strBset.getRowCount() > lt;
  }
  private String getIDIZV(StorageDataSet a) {
    return a.getString("OZNIZV")+"-"+currOIB+"-"+a.getInt("VRSTAIZV");
  }
  private void saveJOPPD() {
    if (strBset == null || strBset.getRowCount() == 0) return;
    String idizv = getIDIZV(strAset);
    QueryDataSet qsA = JoppdA.getDataModule().getTempSet(Condition.equal("IDIZV", idizv));
    qsA.open();
    boolean isNew = qsA.getRowCount() == 0;
    String message = isNew?"Pohraniti izvje��e "+idizv+" ?":"Zamijeniti postoje�e izvje��e "+idizv+" ?";
    if (JOptionPane.showConfirmDialog(null, message, "Potvrda", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      try {
        getJPTV().enableEvents(false);
        QueryDataSet qsB = JoppdB.getDataModule().getTempSet(Condition.nil);
        qsB.open();
        for (strBset.first();strBset.inBounds(); strBset.next()) {
          qsB.insertRow(false);
          dM.copyColumns(strBset, qsB);
          qsB.setString("IDIZV", idizv);
          qsB.post();
        }
        qsA.insertRow(false);
        dM.copyColumns(strAset, qsA);
        qsA.setString("IDIZV", idizv);
        qsA.setTimestamp("DATUM", fPDV2.getDatumOd());
        qsA.post();
        if (!isNew) {
          Valid.getValid().runSQL("DELETE FROM joppda where IDIZV = '"+idizv+"'");
          Valid.getValid().runSQL("DELETE FROM joppdb where IDIZV = '"+idizv+"'");
        }
        if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {qsA,qsB})) {
          JOptionPane.showMessageDialog(null, "Snimanje uspje�no!");
        } else {
          JOptionPane.showMessageDialog(null, "Transakcija neuspje�na! Ponovite postupak");
        }
      } finally {
        getJPTV().enableEvents(true);
      }
    }
  }
  private void loadJOPPD(String idizv) {
    fPDV2.setDataSet(null);
    getStrAset();
    getStrBset();
    strAset.empty();
    strBset.empty();
    QueryDataSet qsA = JoppdA.getDataModule().getTempSet(Condition.equal("IDIZV", idizv));
    QueryDataSet qsB = JoppdB.getDataModule().getTempSet(Condition.equal("IDIZV", idizv));
    qsA.open();
    qsA.first();
    strAset.insertRow(false);
    dM.copyDestColumns(qsA, strAset);
    strAset.post();
    
    qsB.open();
    for (qsB.first(); qsB.inBounds(); qsB.next()) {
      strBset.insertRow(false);
      dM.copyDestColumns(qsB, strBset);
      strBset.post();
    }
    StringTokenizer toib = new StringTokenizer(idizv,"-");
    toib.nextToken(); //oznaka
    currOIB = toib.nextToken();
    fPDV2.stds.setTimestamp("DATUMOD", qsA.getTimestamp("DATUM"));
    fPDV2.okPress();
  }

  public void firstESC() {
    strBset = null;
    strAset = null;
    rs = null;
  }

  public Component getBtns() {
    return jpBtns;
  }

  public void setMode(String mode) {
    boolean doh = (mode.equals("Dohvat"));
    jbGet.setEnabled(doh);
    jbA.setEnabled(!doh);
    jbSave.setEnabled(!doh);
  }

  public void alati() {
    JraDialog dlgAl = new JraDialog(fPDV2.getJframe(), "Alati");
    dlgAl.setModal(true);
//    XYLayout xyl = new XYLayout(500,500);
    JPanel jp = new JPanel(new GridLayout(0, 1, 5, 5));
    jp.add(new JButton(new AbstractAction("Kopiraj teku�i zapis za radnike") {
      public void actionPerformed(ActionEvent e) {
        multiB();
      }
    }));
    jp.add(new JButton(new AbstractAction("Promijeni svima vrijednost kolone") {
      public void actionPerformed(ActionEvent e) {
        copycolB();
      }
    }));
    jp.add(new JButton(new AbstractAction("Popravi redne brojeve") {
      public void actionPerformed(ActionEvent e) {
        recountB();
      }
    }));
    jp.add(new JButton(new AbstractAction("Neispla�ena pla�a") {
      public void actionPerformed(ActionEvent e) {
        neispl();
      }
    }));
    jp.add(new JButton(new AbstractAction("Storno ispravak") {
      public void actionPerformed(ActionEvent e) {
        storno();
      }
    }));
    dlgAl.setContentPane(jp);
    dlgAl.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    startFrame.getStartFrame().centerFrame(dlgAl, 0, "Alati");
    dlgAl.show();
  }
  private void multiB() {
    System.out.println("multiB");
    raTwoTableFrame ttf = new raTwoTableFrame(fPDV2.getWindow()) {
      
      public boolean runFirstESC() {
        return false;
      }
      
      public void okPress() {
        StorageDataSet rowB = strBset.cloneDataSetStructure();
        rowB.open();
        rowB.insertRow(false);
        strBset.copyTo(rowB);
        rowB.post();
        strBset.setSort(new SortDescriptor(new String[] {"RBR"}));
        strBset.last();
        int rbr = strBset.getInt("RBR");
        for (getTTC().getRightDataSet().first(); getTTC().getRightDataSet().inBounds(); getTTC().getRightDataSet().next()) {
          strBset.insertRow(false);
          rowB.copyTo(strBset);
          rbr++;
          if (!strBset.getString("COPCINE").equals("00000")) {
            strBset.setString("COPCINE", "0"+raIzvjestaji.convertCopcineToRS(getTTC().getRightDataSet().getString("COPCINE")));
          }
          
          strBset.setString("OIB", getTTC().getRightDataSet().getString("OIB"));
          strBset.setString("IMEPREZ", getTTC().getRightDataSet().getString("IME")+" "+getTTC().getRightDataSet().getString("PREZIME"));
          strBset.setInt("RBR", rbr);
          strBset.post();
        }
        fPDV2.getJPTV().fireTableDataChanged();
        setVisible(false);
      }
      public boolean isIspis() {
        return false;
      }
      public void componentShow() {
        // TODO Auto-generated method stub
      }

      public void firstESC() {
        // TODO Auto-generated method stub
      }
    };
    QueryDataSet radnici = new QueryDataSet();
    radnici.addColumn(Radnici.getDataModule().getColumn("CRADNIK").cloneColumn());
    radnici.addColumn(Radnici.getDataModule().getColumn("PREZIME").cloneColumn());
    radnici.addColumn(Radnici.getDataModule().getColumn("IME").cloneColumn());
    radnici.addColumn(Radnicipl.getDataModule().getColumn("OIB").cloneColumn());
    radnici.addColumn(Radnicipl.getDataModule().getColumn("COPCINE").cloneColumn());
    String query = "SELECT radnici.cradnik, radnici.prezime, radnici.ime, radnicipl.oib, radnicipl.copcine"
        + " from radnici, radnicipl where radnici.cradnik = radnicipl.cradnik and "+getOrgqrs();
    radnici.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(), query));
    radnici.open();
    StorageDataSet odabrani = radnici.cloneDataSetStructure();
    odabrani.open();
    ttf.getTTC().setLeftDataSet(radnici);
    ttf.getTTC().setRightDataSet(odabrani);
    startFrame.getStartFrame().centerFrame(ttf,0,"Odabir radnika");
    ttf.show();
  }
  private void copycolB() {
//    ((JraTable2)fPDV2.getTable()).getModelColumnName(fPDV2.getJPTV().getColumnsBean().getComboSelectedItem())
    System.out.println("copycolB  "+fPDV2.getJPTV().getColumnsBean().getSelectedColumnName());
    String colname = fPDV2.getJPTV().getColumnsBean().getSelectedColumnName();
    String colcaption = fPDV2.getJPTV().getColumnsBean().getComboSelectedItem();
    Variant value = new Variant(); 
    strBset.getVariant(colname, value);
    int answ = JOptionPane.showConfirmDialog(fPDV2.getWindow(), "Promijeniti u svim redovima "+colcaption+" u "+value.toString()+"?");
    if (answ == JOptionPane.OK_OPTION) {
      try {
        fPDV2.getJPTV().enableEvents(false);
        for (strBset.first(); strBset.inBounds(); strBset.next()) {
          strBset.setVariant(colname, value);
          strBset.post();
        }
      } finally {
        fPDV2.getJPTV().enableEvents(true);
      }
      fPDV2.getJPTV().fireTableDataChanged();
      
    }
  }
  private void recountB() {
    System.out.println("recountB");
    int answ = JOptionPane.showConfirmDialog(fPDV2.getWindow(), "Popraviti slijed rednih brojeva na stranici B?");
    if (answ == JOptionPane.OK_OPTION) {
      strBset.setSort(null);
      ((raExtendedTable) fPDV2.getJPTV().getMpTable()).resetSortColumns();
      try {
        fPDV2.getJPTV().enableEvents(false);
        int rbr=-1;
        for (strBset.first(); strBset.inBounds(); strBset.next()) {
          if (rbr < 0) rbr = strBset.getInt("RBR");
          strBset.setInt("RBR", rbr++);
          strBset.post();
        }
      } finally {
        fPDV2.getJPTV().enableEvents(true);
      }
      fPDV2.getJPTV().fireTableDataChanged();
    }
  }
  
  private void storno() {
    int answ = JOptionPane.showConfirmDialog(fPDV2.getWindow(), "Stornirati sve redove za ispravak JOPPD-a?");
    if (answ == JOptionPane.OK_OPTION) {
      strBset.setSort(null);
      ((raExtendedTable) fPDV2.getJPTV().getMpTable()).resetSortColumns();
      try {
        fPDV2.getJPTV().enableEvents(false);
        for (strBset.first(); strBset.inBounds(); strBset.next()) {
          strBset.setInt("SATI", 0);
          strBset.setInt("NSATI", 0);
          strBset.setBigDecimal("BRUTO", Aus.zero2);
          strBset.setBigDecimal("OSNDOP", Aus.zero2);
          strBset.setBigDecimal("MIO1", Aus.zero2);
          strBset.setBigDecimal("MIO2", Aus.zero2);
          strBset.setBigDecimal("ZDR", Aus.zero2);
          strBset.setBigDecimal("ZASNR", Aus.zero2);
          strBset.setBigDecimal("ZAP", Aus.zero2);
          strBset.setBigDecimal("MIO1STAZ", Aus.zero2);
          strBset.setBigDecimal("MIO2STAZ", Aus.zero2);
          strBset.setBigDecimal("ZDRINO", Aus.zero2);
          strBset.setBigDecimal("ZAPOSINV", Aus.zero2);          
          strBset.setBigDecimal("IZDATAK", Aus.zero2);
          strBset.setBigDecimal("IZDATAKMIO", Aus.zero2);
          strBset.setBigDecimal("DOHODAK", Aus.zero2);
          strBset.setBigDecimal("ISKNEOP", Aus.zero2);
          strBset.setBigDecimal("POROSN", Aus.zero2);
          strBset.setBigDecimal("POR", Aus.zero2);
          strBset.setBigDecimal("PRIR", Aus.zero2);
          strBset.setBigDecimal("NEOP", Aus.zero2);
          strBset.setBigDecimal("NETOPK", Aus.zero2);
          strBset.setString("JNP", "0");
          strBset.setString("JNI", "0");
          strBset.setBigDecimal("BRUTOOBR", Aus.zero2);
          strBset.post();
        }
        sumStrA();
        strAset.setInt("VRSTAIZV", 2);
      } finally {
        fPDV2.getJPTV().enableEvents(true);
      }
      fPDV2.getJPTV().fireTableDataChanged();
    }
  }
  
  private void neispl() {
    int answ = JOptionPane.showConfirmDialog(fPDV2.getWindow(), "Postaviti pla�u kao neispla�enu?");
    if (answ == JOptionPane.OK_OPTION) {
      strBset.setSort(null);
      ((raExtendedTable) fPDV2.getJPTV().getMpTable()).resetSortColumns();
      try {
        fPDV2.getJPTV().enableEvents(false);
        for (strBset.first(); strBset.inBounds(); strBset.next()) 
          if (!strBset.getString("JOP").equals("0000")) {
          strBset.setString("JOP", "0041");
          /*strBset.setBigDecimal("BRUTO", Aus.zero2);
          strBset.setBigDecimal("IZDATAK", Aus.zero2);
          strBset.setBigDecimal("IZDATAKMIO", Aus.zero2);
          strBset.setBigDecimal("DOHODAK", Aus.zero2);
          strBset.setBigDecimal("ISKNEOP", Aus.zero2);
          strBset.setBigDecimal("POROSN", Aus.zero2);
          strBset.setBigDecimal("POR", Aus.zero2);
          strBset.setBigDecimal("PRIR", Aus.zero2);*/
          strBset.setBigDecimal("IZDATAK", Aus.zero2);
          strBset.setBigDecimal("NEOP", Aus.zero2);
          strBset.setBigDecimal("NETOPK", Aus.zero2);
          strBset.setString("JNP", "0");
          strBset.setString("JNI", "0");
          strBset.post();
        }
      } finally {
        fPDV2.getJPTV().enableEvents(true);
      }
      fPDV2.getJPTV().fireTableDataChanged();
    }
  }
}


/*

*/