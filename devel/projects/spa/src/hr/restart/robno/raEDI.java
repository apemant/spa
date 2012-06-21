package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Pjpar;
import hr.restart.baza.VTCartPart;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.help.MsgDispatcher;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.jcraft.jsch.ChannelSftp.LsEntry;


public class raEDI {
  
  static DataRow last;
  
  public static void createOrder(String oj, File xml) { 
    try {
      Document doc = new SAXBuilder().build(xml);
      createOrder(oj, doc);
      xml.delete();
      Util.getUtil().showDocs(last.getString("CSKL"), 
          "", "NKU", last.getInt("BRDOK"), last.getString("GOD"));
    } catch (JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Greška: " + e.getMessage(),
          "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private static void createOrder(String oj, Document doc) {
    
    lookupData ld = lookupData.getlookupData();
  	String gsif = frmParam.getParam("robno", "sifGetro", 
        "", "Šifra dobavljaèa za Getro");
  	
  	Element root = doc.getRootElement();
    if (!root.getName().equals("Order")) 
      throw new RuntimeException("Pogrešan format datoteke: '"+root.getName()+"'");
    
//    List docs = root.getChildren("Order");
//    System.out.println("loop started");
//    for (Iterator n = docs.iterator(); n.hasNext(); ) {
      Element nar = root;//(Element) n.next();
      System.out.println("nar: " + nar);

      Element party = nar.getChild("OrderParty");
      Element buyer = party.getChild("BuyerParty");
      
      Element ship = party.getChild("ShipToParty");
      String pj = ship.getChildText("SellerShipToID");
      
      Element head = nar.getChild("OrderHeader");
      
      QueryDataSet zag = doki.getDataModule().getTempSet(Condition.nil);
      QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.nil);
      
      zag.open();
      st.open();
      zag.insertRow(false);
      zag.setString("CSKL", oj);
      zag.setString("VRDOK", "NKU");
      zag.setInt("CPAR", Integer.parseInt(gsif));
      zag.setTimestamp("DATDOK", getTimestamp(head, "OrderIssueDate", null));
      zag.setTimestamp("DATDOSP", getTimestamp(head, "RequestedDeliverDate", null));
      
      zag.setString("GOD", hr.restart.util.Util.getUtil().
          getYear(zag.getTimestamp("DATDOK")));
      if (pj != null && pj.length() > 0) {
        try {
          zag.setInt("PJ", Integer.parseInt(pj));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      
      String vri = head.getChildText("RequestedDeliverByTime");
      if (vri != null) {
        zag.setString("OPIS", "VRIJEME ISPORUKE: " +
            vri.substring(0, 2) + ":" +
            vri.substring(2, 4));
      }
      
      String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
      
      short rbr = 0;
      for (Iterator a = nar.getChild("OrderDetail").getChildren("Item").iterator(); a.hasNext(); ) {
      	Element da = (Element) a.next();
      	
      	st.insertRow(false);
        dM.copyColumns(zag, st, Util.mkey);
        st.setShort("RBR", ++rbr);
        st.setInt("RBSID", rbr);
        
        String kol = da.getChildText("QuantityValue");
        st.setBigDecimal("KOL", Aus.getDecNumber(kol));
        
        String cart = da.getChildText("BuyerItemID");
        if (cart != null && ld.raLocate(dM.getDataModule().getArtikli(), "CART", cart)) {
        	dM.copyColumns(dM.getDataModule().getArtikli(), st, acc);
          st.post();
        } else
          throw new RuntimeException("Nepoznata šifra artikla!");
        
      }
      
      saveOrder(zag, st);
//    }
  }
  
  private static void saveOrder(final QueryDataSet zag, final QueryDataSet st) {
    if (!new raLocalTransaction() {
      public boolean transaction() throws Exception {
        Util.getUtil().getBrojDokumenta(zag);
        for (st.first(); st.inBounds(); st.next()) {
          st.setInt("BRDOK", zag.getInt("BRDOK"));
          st.setString("GOD", zag.getString("GOD"));
        }
        
        raTransaction.saveChanges(zag);
        raTransaction.saveChanges(st);
        
        last = new DataRow(zag, Util.mkey);
        dM.copyColumns(zag, last, Util.mkey);
        
        return true;
      }
    }.execTransaction()) {
      JOptionPane.showMessageDialog(null, "Greška kod snimanja narudžbe!",
          "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private static Timestamp getTimestamp(Element parent, 
      String date, String time) {
    System.out.println(parent);
    String sd = parent.getChildText(date);
    String st = parent.getChildText(time);
    Calendar cal = Calendar.getInstance();
    //2010-03-02T09:25:45
    cal.set(cal.YEAR, Integer.parseInt(sd.substring(0, 4)));
//    cal.set(cal.MONTH, Integer.parseInt(sd.substring(4, 6)) - 1);
    cal.set(cal.MONTH, Integer.parseInt(sd.substring(5, 7)) - 1);
//    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(6, 8)));
    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(8, 10)));
    if (time != null) {
      cal.set(cal.HOUR_OF_DAY, Integer.parseInt(st.substring(0, 2)));
      cal.set(cal.MINUTE, Integer.parseInt(st.substring(2, 4)));
      cal.set(cal.SECOND, Integer.parseInt(st.substring(4, 6)));
    } else {
      cal.set(cal.HOUR_OF_DAY, 0);
      cal.set(cal.MINUTE, 0);
      cal.set(cal.SECOND, 0);
    }
    cal.set(cal.MILLISECOND, 0);
    return new Timestamp(cal.getTime().getTime());
  }
  
  public static void importPanteon(File dir, boolean report) {
    int doc = 0;
  	File pg = new File(dir, "PG.lck");
  	if (pg.exists()) {
  	    if (!report) return;
  		JOptionPane.showMessageDialog(null, "Mapa je zauzeta. Probajte malo kasnije.", "Sinkronizacija", JOptionPane.WARNING_MESSAGE);
  		return;
  	}
  	File sw = new File(dir, "SW.lck");
  	try {
			sw.createNewFile();
		} catch (IOException e) {
		  if (!report) return;
			JOptionPane.showMessageDialog(null, "Greška kod sinkronizacije (sw)!", "Sinkronizacija", JOptionPane.ERROR_MESSAGE);
			return;
		}
  	try {
  		if (pg.exists()) {
  		  if (!report) return;
    		JOptionPane.showMessageDialog(null, "Mapa je zauzeta. Probajte malo kasnije.", "Sinkronizacija", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
  		doc = importPanteonImpl(dir, report);
  	} catch (RuntimeException re) {
  	  re.printStackTrace();
  	  if (!report) return;
  	  JOptionPane.showMessageDialog(null, re.getMessage(), "Sinkronizacija", JOptionPane.ERROR_MESSAGE);
  	} finally {
  		sw.delete();
  	}
  	System.out.println("docs: " + doc);
  	if (report)
  	  Util.getUtil().showDocs(last.getString("CSKL"), 
        "", "NKU", last.getInt("BRDOK"), last.getString("GOD"));
  	else if (doc > 0) {
  		String users = frmParam.getParam("robno", "ediNotify", "", "Popis korisnika za notifikaciju EDI");
  		System.out.println("Users: " + users);
  		String[] us = new VarStr(users).split();
  		for (int i = 0; i < us.length; i++) {
  		  System.out.println("Sending to " + us[i]);
  			MsgDispatcher.send("EDI", us[i], "Dohvaæeno " + doc + " narudžbi putem EDI.");
  		}
  	}
  }
  
  public static void importMagros(boolean report) {
    try {
      System.out.println("dohvat ftp");
      com.jcraft.jsch.JSch j = new com.jcraft.jsch.JSch();
      j.setKnownHosts(IntParam.getTag("sftp.hosts"));
      com.jcraft.jsch.Session sess = j.getSession(
          IntParam.getTag("sftp.user"), 
          IntParam.getTag("sftp.addr"),
          Aus.getNumber(IntParam.getTag("sftp.port")));
      sess.setPassword(IntParam.getTag("sftp.pass"));
      sess.connect();
      System.out.println("spojeno");

      com.jcraft.jsch.Channel channel = sess.openChannel("sftp");
      channel.connect();

      com.jcraft.jsch.ChannelSftp sch = (com.jcraft.jsch.ChannelSftp) channel;
      
      System.out.println("chg dir");
      sch.cd("in");
      System.out.println("remote dir " + sch.pwd());
      Vector v = sch.ls("*");
      for (int i = 0; i < v.size(); i++) {
        String fname = ((LsEntry) v.get(i)).getFilename();
        sch.get(fname, fname);
        importMagrosImpl(fname);
        
        sch.rm(fname);
      }
      
      sess.disconnect();
      
      System.out.println("disconnected");
    } catch (com.jcraft.jsch.JSchException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException("Greška kod slanja ftp-om!");
    } catch (com.jcraft.jsch.SftpException e) {
      e.printStackTrace();
      throw new RuntimeException("Greška kod slanja ftp-om!");
    }
    
  }
  
  private static int importMagrosImpl(String fname) {
    
    String cskl = frmParam.getParam("robno", "ediMagCskl", "100",
      "Šifra skladišta za EDI magros dostavnice");
    
    lookupData ld = lookupData.getlookupData();
    DataSet part = dM.getDataModule().getPartneri();
    DataSet art = dM.getDataModule().getArtikli();
    
    QueryDataSet zag = doki.getDataModule().getTempSet(Condition.nil);
    QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.nil);
    zag.open();
    st.open();
    
    String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
    
    TextFile.setEncoding("UTF-8");
    TextFile tf = TextFile.read(fname);
    String line;
    short rbr = 0;
    while ((line = tf.in()) != null) {
      if (line.startsWith("HD")) {
        zag.insertRow(false);
        
        zag.setString("CUSER", raUser.getInstance().getUser());
        zag.setString("CSKL", cskl);
        zag.setString("VRDOK", "DOS");
        
        String prim = line.substring(335, 348).trim();
        String oj = null;
        if (prim.indexOf("-") >= 0) {
          oj = prim.substring(prim.indexOf("-") + 1);
          prim = prim.substring(0, prim.indexOf("-"));
        }
        
        if (ld.raLocate(part, "CPAR", prim)) {
          zag.setInt("CPAR", part.getInt("CPAR"));
          System.out.println("Partner: " + part.getInt("CPAR"));
          /*DataSet pj = Pjpar.getDataModule().getTempSet(
              Condition.equal("CPAR", part));
          pj.open();
          String md = line.substring(335, 347).trim();
          if (ld.raLocate(pj, "GLN", md)) {
            zag.setInt("PJ", pj.getInt("PJ"));
            System.out.println("PJ: " + pj.getInt("PJ"));
          }*/
          if (oj != null) {
            zag.setInt("PJ", Aus.getNumber(oj));
            System.out.println("PJ: " + zag.getInt("PJ"));
          }
          
          /*cpart = VTCartPart.getDataModule().getTempSet(
              Condition.equal("CPAR", part));
          cpart.open();*/
        }        
      } else if (line.startsWith("DA")) {
        zag.setString("BRDOKIZ", line.substring(2, 22).trim());
        zag.setTimestamp("SYSDAT", getMDate(line.substring(22, 30).trim()));
        zag.setTimestamp("DATDOK", getMDate(line.substring(30, 38).trim()));
        zag.setTimestamp("DVO", getMDate(line.substring(38, 46).trim()));
        zag.setString("BRNARIZ", line.substring(46, 66).trim());
        if (line.substring(66, 74).trim().length() == 8)
          zag.setTimestamp("DATNARIZ", getMDate(line.substring(66, 74).trim()));
      } else if (line.startsWith("IT")) {
        st.insertRow(false);
        dM.copyColumns(zag, st, Util.mkey);
        st.setShort("RBR", ++rbr);
        st.setInt("RBSID", rbr);
        
        String bc = line.substring(42, 62).trim();
        if (bc != null && (ld.raLocate(dM.getDataModule().getArtikli(), "BC", bc) ||
            ld.raLocate(dM.getDataModule().getArtikli(), "BCKOL", bc))) {
            dM.copyColumns(dM.getDataModule().getArtikli(), st, acc);
          st.post();
        } else 
          throw new RuntimeException("Nepoznata šifra artikla! " + bc);
        
        st.setBigDecimal("KOL", Aus.getDecNumber(line.substring(102, 110).trim()));
        Aus.set(st, "KOL2", "KOL");
      }
    }
    System.out.println(zag);
    saveOrder(zag, st);
    
    tf.close();
    return 0;
  }
  
  private static int importPanteonImpl(File dir, boolean report) {
  	lookupData ld = lookupData.getlookupData();
  	DataSet part = dM.getDataModule().getPartneri();
  	
  	File fiz = new File(dir, "Izme_nar.sdf");
  	File fzag = new File(dir, "osnova_n.sdf");
  	File fst = new File(dir, "postavke.sdf");
  	
  	if (!fiz.canRead()) {
  	  if (!report) return -1;
  		JOptionPane.showMessageDialog(null, "Nema novih narudžbi za import.", "Sinkronizacija", JOptionPane.WARNING_MESSAGE);
  		return -1;
  	}
  	if (!fzag.canRead() || !fst.canRead()) {
  	  if (!report) return -2;
  		JOptionPane.showMessageDialog(null, "Greška kod sinkronizacije (zag,st)!", "Sinkronizacija", JOptionPane.ERROR_MESSAGE);
  		return -2;
  	}
  	String line;
  	List lzag = new ArrayList();
  	List lst = new ArrayList();
  	
  	TextFile tzag = TextFile.read(fzag);
  	if (tzag == null) {
  	  if (!report) return -3;
  		JOptionPane.showMessageDialog(null, "Greška kod sinkronizacije (zag)!", "Sinkronizacija", JOptionPane.ERROR_MESSAGE);
  		return -3;
  	}
  	while (null != (line = tzag.in())) lzag.add(line);
  	tzag.close();
  	TextFile tst = TextFile.read(fst);
  	if (tst == null) {
  	  if (!report) return -4;
  		JOptionPane.showMessageDialog(null, "Greška kod sinkronizacije (st)!", "Sinkronizacija", JOptionPane.ERROR_MESSAGE);
  		return -4;
  	}
  	while (null != (line = tst.in())) lst.add(line);
  	tst.close();
  	
  	/*QueryDataSet zag = doki.getDataModule().getTempSet(Condition.nil);
    QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.nil);
    
    zag.open();
    st.open();*/
    String cskl = frmParam.getParam("robno", "ediCskl", "",
      "Šifra OJ ili skladišta za EDI narudžbe");
    
    String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
  	
  	int zi = 0, si = 0;
  	while (zi < lzag.size()) {
  		line = (String) lzag.get(zi);
  		if (line.length() <= 1) continue;
  		
  		QueryDataSet zag = doki.getDataModule().getTempSet(Condition.nil);
  	    QueryDataSet st = stdoki.getDataModule().getTempSet(Condition.nil);
  	    
  	    zag.open();
  	    st.open();
  		
  		zag.insertRow(false);
  	
  		zag.setString("CUSER", raUser.getInstance().getUser());
      zag.setString("CSKL", cskl);
      zag.setString("VRDOK", "NKU");
      DataSet cpart = null;
      if (ld.raLocate(part, "GLN", line.substring(171, 206).trim())) {
        zag.setInt("CPAR", part.getInt("CPAR"));
        System.out.println("Partner: " + part.getInt("CPAR"));
        DataSet pj = Pjpar.getDataModule().getTempSet(
            Condition.equal("CPAR", part));
        pj.open();
        String md = line.substring(241, 276).trim();
        if (ld.raLocate(pj, "GLN", md)) {
          zag.setInt("PJ", pj.getInt("PJ"));
          System.out.println("PJ: " + pj.getInt("PJ"));
        }
        String bnar = line.substring(49, 84).trim();
        if (bnar.startsWith(md)) bnar = bnar.substring(md.length() + 1);
        zag.setString("BRNARIZ", bnar);
        cpart = VTCartPart.getDataModule().getTempSet(
            Condition.equal("CPAR", part));
        cpart.open();
      }
      zag.setTimestamp("DATDOK", getDate(line.substring(101, 136).trim()));
      zag.setTimestamp("DATNARIZ", getDate(line.substring(101, 136).trim()));
      zag.setTimestamp("DATDOSP", getDate(line.substring(136, 171).trim()));
      
      zag.setString("GOD", hr.restart.util.Util.getUtil().
          getYear(zag.getTimestamp("DATDOK")));
      zag.setString("CRADNAL", "EDI");
      
      String nbr = line.substring(49, 84);
      
      short rbr = 0;
  		while (si < lst.size()) {
  			line = (String) lst.get(si);
  			if (line.length() <= 1) break;
  			if (!line.substring(49, 84).equals(nbr)) break;
  			
  			st.insertRow(false);
        dM.copyColumns(zag, st, Util.mkey);
        st.setShort("RBR", ++rbr);
        st.setInt("RBSID", rbr);
        
        String cart = line.substring(90, 125).trim();
        if (cart != null && cpart != null && ld.raLocate(cpart, "BCPAR", cart))
          cart = cpart.getString("BC");
        if (cart != null && ld.raLocate(dM.getDataModule().getArtikli(), "BC", cart)) {
        	dM.copyColumns(dM.getDataModule().getArtikli(), st, acc);
          st.post();
        } else
          throw new RuntimeException("Nepoznata šifra artikla! " + cart);
        
        st.setBigDecimal("KOL", Aus.getDecNumber(line.substring(271, 286).trim()));
        st.setBigDecimal("FC", Aus.getDecNumber(line.substring(324, 339).trim()));
        
  			++si;
  		}
        System.out.println(zag);
  		saveOrder(zag, st);
  			
      ++zi;
  	}
  	
  	fiz.delete();
  	fzag.delete();
  	fst.delete();
  	
  	return zi;
  }
  
  private static Timestamp getDate(String sd) {
  	Calendar cal = Calendar.getInstance();
  	cal.set(cal.YEAR, Integer.parseInt(sd.substring(0, 4)));
  	cal.set(cal.MONTH, Integer.parseInt(sd.substring(4, 6)) - 1);
  	cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(6, 8)));
  	if (sd.length() > 8) {
  		cal.set(cal.HOUR_OF_DAY, Integer.parseInt(sd.substring(8, 10)));
      cal.set(cal.MINUTE, Integer.parseInt(sd.substring(10, 12)));
  	} else {
  		cal.set(cal.HOUR_OF_DAY, 0);
      cal.set(cal.MINUTE, 0);
  	}
  	cal.set(cal.SECOND, 0);
  	cal.set(cal.MILLISECOND, 0);
  	return new Timestamp(cal.getTime().getTime());
  }  
  
  private static Timestamp getMDate(String sd) {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, Integer.parseInt(sd.substring(4, 8)));
    cal.set(cal.MONTH, Integer.parseInt(sd.substring(2, 4)) - 1);
    cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(0, 2)));
    cal.set(cal.HOUR_OF_DAY, 0);
    cal.set(cal.MINUTE, 0);
    cal.set(cal.SECOND, 0);
    cal.set(cal.MILLISECOND, 0);
    return new Timestamp(cal.getTime().getTime());
  }
  
  public static void main(String[] args) {
  	
    MsgDispatcher.install(false);
  	Timer t = new Timer(15*1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = frmParam.getParam("robno", "panteonPath", "/home/abf/tmp/hr/test",
		    	"Putanja mape za EDI preko Panteona");
				try {
                  raEDI.importPanteon(new File(path), false);
                } catch (RuntimeException e1) {
                  e1.printStackTrace();
                }
				
                try {
                  raEDI.importMagros(false);
                } catch (RuntimeException e1) {
                  e1.printStackTrace();
                }
			}
		});
  	t.setDelay(15*60*1000);
  	t.start();
  	
  	try {
      Thread.currentThread().join();
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}
