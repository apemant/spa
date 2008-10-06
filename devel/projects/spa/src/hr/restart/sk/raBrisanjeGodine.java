/****license*****************************************************************
**   file: raBrisanjeGodine.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.raFrame;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raBrisanjeGodine extends raFrame {
  static hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  static hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  static raBrisanjeGodine frm;

  JraTextField jraGod = new JraTextField();
  JLabel jlGod = new JLabel();
  XYLayout lay = new XYLayout();
  JPanel jp = new JPanel();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };

  boolean delRadni;
  String god;
  
  static int rows;

  public static raBrisanjeGodine getInstance() {
    return frm;
  }

  public raBrisanjeGodine() {
    try {
      frm = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception {
    jp.setLayout(lay);
    lay.setHeight(60);
    lay.setWidth(450);
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);
    jlGod.setText("Godina do koje se briše");
    new raTextMask(jraGod, 4, false, raTextMask.DIGITS);

    jp.add(jlGod, new XYConstraints(75, 20, -1, -1));
    jp.add(jraGod, new XYConstraints(300, 20, 75, -1));
    this.getContentPane().add(jp);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    okp.registerOKPanelKeys(this);
  }

  private void CancelPress() {
    this.hide();
  }

  private void OKPress() {
    god = jraGod.getText();
    int g = Aus.getNumber(god);
    if (g < 1980 || g > Aus.getNumber(Valid.getValid().findYear())) {
      jraGod.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna godina!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    uptoProc(god);
  }


  public boolean process(String godina) {
    god = godina;
    raLocalTransaction del = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          Condition c;
          Condition knjig = Aus.getKnjigCond();
          Condition period = Condition.between("DATUMKNJ", ut.getYearBegin(god), ut.getYearEnd(god));
          raTransaction.runSQL("DELETE FROM skstavke WHERE "+(c = knjig.and(period)));
          if (delRadni)
            raTransaction.runSQL("DELETE FROM skstavkerad WHERE "+c);
          /*raTransaction.runSQL("DELETE FROM skkumulativi WHERE "+
                               knjig.and(Condition.equal("GODINA", god)));*/

          raTransaction.runSQL("DELETE FROM gkstavke WHERE "+
                              (c = knjig.and(Condition.equal("GOD", god))));
          if (delRadni)
            raTransaction.runSQL("DELETE FROM gkstavkerad WHERE "+c);
          raTransaction.runSQL("DELETE FROM gkkumulativi WHERE "+knjig+
                               " AND godmj LIKE '"+god+"%'");
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return del.execTransaction();
  }
  
  public static void uptoProc(final String godina) {
  	raProcess.runChild(new Runnable() {
  		public void run() {
  			upto(godina, true);
  		}
  	});
  }
  
  public static void upto(String god, boolean proc) {
  	if (proc) raProcess.setMessage("Priprema brisanja starih podataka ...", true);
  	
  	Condition cgod = Condition.where("GOD", Condition.LESS_OR_EQUAL, god);
  	Condition cgodina = Condition.where("GODINA", Condition.LESS_OR_EQUAL, god);
  	Condition cgodmj = Condition.where("GODMJ", Condition.LESS_OR_EQUAL, god + "12");
  	Condition cdatknj = Condition.till("DATUMKNJ", ut.getYearEnd(god));
  	Condition cdatizv = Condition.till("DATUMIZV", ut.getYearEnd(god));
  	
  	rows = 0;
  	
  	if (proc) raProcess.setMessage("Otvaranje tablice skstavke ...", false);
  	QueryDataSet sk = Skstavke.getDataModule().getTempSet(
  			"KNJIG CPAR VRDOK BROJKONTA BROJDOK BROJIZV CSKSTAVKE CKNJIGE", cdatknj);
  	sk.open();
  	
  	if (proc) raProcess.setMessage("Brisanje tablice skstavke ...", false);
  	Set csk = new HashSet();
  	Set cui = new HashSet();
  	for (sk.first(); sk.inBounds(); sk.next()) {
  		csk.add(sk.getString("CSKSTAVKE"));
  		cui.add(getKey(sk));
  	}
  	prn("skstavke", sk.rowCount());
  	sk.deleteAllRows();
  	sk.saveChanges();
  	
  	if (proc) raProcess.setMessage("Otvaranje tablice uistavke ...", false);
  	QueryDataSet ui = UIstavke.getDataModule().getTempSet(
  			"KNJIG CPAR VRDOK BROJKONTA BROJDOK RBS CKNJIGE", "");
  	ui.open();
  	int oldui = ui.getRowCount();
  	
  	if (proc) raProcess.setMessage("Brisanje tablice uistavke ...", false);
  	ui.first();
  	while (ui.inBounds())
  		if (cui.contains(getKey(ui)))
  			ui.deleteRow();
  		else ui.next();
  	prn("uistavke", oldui - ui.rowCount());
  	ui.saveChanges();
  	
  	if (proc) raProcess.setMessage("Otvaranje tablice pokriveni ...", false);
  	QueryDataSet pok = Pokriveni.getDataModule().getTempSet();
  	pok.open();
  	int oldpok = pok.getRowCount();
  	
  	if (proc) raProcess.setMessage("Brisanje tablice pokriveni ...", false);
  	pok.first();
  	while (pok.inBounds())
  		if (csk.contains(pok.getString("CRACUNA")) || csk.contains(pok.getString("CUPLATE")))
  			pok.deleteRow();
  		else pok.next();
  	prn("pokriveni", oldpok - pok.rowCount());
  	pok.saveChanges();
  	
  	del("meskla", cgod, proc);
  	del("stmeskla", cgod, proc);
  	del("doki", cgod, proc);
  	del("stdoki", cgod, proc);
  	del("doku", cgod, proc);
  	del("stdoku", cgod, proc);
  	del("pos", cgod, proc);
  	del("stpos", cgod, proc);
  	del("rate", cgod, proc);
  	del("kpr", cgod, proc);
  	del("serbr", cgod, proc);
  	del("stanje", cgod, proc);
  	del("vtrabat", cgod, proc);
  	del("vtzavtr", cgod, proc);
  	del("vtztr", cgod, proc);
  	del("gkstavke", cgod, proc);
  	del("gkkumulativi", cgodmj, proc);
  	del("skkumulativi", cgodina, proc);
  	del("nalozi", cgod, proc);
  	del("izvodi", cgod, proc);
  	del("virmani", cdatizv, proc);
  }
  
  private static String getKey(DataSet ds) {
  	return ds.getString("KNJIG")+ "-" + ds.getInt("CPAR") + ds.getString("VRDOK") +
  				"-" + ds.getString("BROJDOK") + "-" + ds.getString("CKNJIGE");
  }
  
  private static void del(String table, Condition c, boolean proc) {
  	if (proc) raProcess.setMessage("Brisanje tablice "+table+" ...", false);
  	try {
			Statement st = dM.getDatabaseConnection().createStatement();
			int dr = st.executeUpdate("DELETE FROM " + table + " WHERE " + c);
	  	st.close();
	  	prn(table, dr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  private static void prn(String table, int dr) {
  	System.out.println(table + " - " + dr + " rows (" + (rows += dr) + ")");
  }
}