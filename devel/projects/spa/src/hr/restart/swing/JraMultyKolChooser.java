/****license*****************************************************************
**   file: JraMultyKolChooser.java
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
/*
 * Created on 2005.04.22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.swing;

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class JraMultyKolChooser {

	private JDialog main;

	private Point location;

	private JraTextMultyKolField glavonja = null;

	private JPanel localpanel = new JPanel();

	private JLabel jlkol1 = new JLabel("Kolièina");

	private JraLabel jrljm1 = new JraLabel();

	private JraTextField jtfkol1 = new JraTextField();

	private JLabel jlkol2 = new JLabel("Kol. pakiranje");

	private JraLabel jrljm2 = new JraLabel();

	private JraTextField jtfkol2 = new JraTextField();

	private JLabel jlkol3 = new JLabel("Kol. kolete");

	private JraLabel jrljm3 = new JraLabel();

	private JraTextField jtfkol3 = new JraTextField();

	private StorageDataSet tmpsds = null;

	private QueryDataSet artikli = null;

	private dM dm = dM.getDataModule();

	private int cart = -1;

	private BigDecimal kol = Aus.zero2;

	private BigDecimal nula = Aus.zero2;

	private OKpanel okp = new OKpanel() {

		public void jBOK_actionPerformed() {
			okpress();

		}

		public void jPrekid_actionPerformed() {
			close();

		}
	};

	public void okpress() {
		close();
		glavonja.getDataSet().setBigDecimal("KOL", tmpsds.getBigDecimal("KOL"));
		glavonja.getDataSet().setBigDecimal("KOL1", tmpsds.getBigDecimal("KOLPAK"));
//		glavonja.getDataSet().setBigDecimal("KOL2", tmpsds.getBigDecimal("KOLKOL"));
		glavonja.getDataSet().post();
		glavonja.myfirePropertyKolChange(new BigDecimal("0.12345678901"), tmpsds.getBigDecimal("KOL"));

		//		glavonja = null;
	}

	public void setupSDS() {
		tmpsds = new StorageDataSet();

		Column colkol1 = dm.getStdoki().getColumn("KOL").cloneColumn();
		Column coljm1 = dm.getArtikli().getColumn("JM").cloneColumn();
		Column colkol2 = dm.getStdoki().getColumn("KOL").cloneColumn();
		colkol2.setColumnName("KOLPAK");
		Column coljm2 = dm.getArtikli().getColumn("JMPAK").cloneColumn();
		Column colbrjed = dm.getArtikli().getColumn("BRJED").cloneColumn();
		Column colkol3 = dm.getStdoki().getColumn("KOL").cloneColumn();
		colkol3.setColumnName("KOLKOL");
		Column coljm3 = dm.getArtikli().getColumn("JMKOL").cloneColumn();
		Column colbrjedkol = dm.getArtikli().getColumn("BRJEDKOL")
				.cloneColumn();
		tmpsds.setColumns(new Column[] { colkol1, coljm1, colkol2, coljm2,
				colbrjed, colkol3, coljm3, colbrjedkol });
		tmpsds.open();

		tmpsds.setString("JM", artikli.getString("JM"));
		tmpsds.setString("JMPAK", artikli.getString("JMPAK"));
		tmpsds.setString("JMKOL", artikli.getString("JMKOL"));
		tmpsds.setBigDecimal("BRJED", artikli.getBigDecimal("BRJED"));
		tmpsds.setBigDecimal("BRJEDKOL", artikli.getBigDecimal("BRJEDKOL"));
		tmpsds.setBigDecimal("KOL", kol);
		if (glavonja.getDataSet().getBigDecimal("KOL1").compareTo(nula)==0){
		
		if (tmpsds.getBigDecimal("BRJED").compareTo(nula) != 0) {
			tmpsds.setBigDecimal("KOLPAK", kol.divide(artikli
					.getBigDecimal("BRJED"), 3, BigDecimal.ROUND_HALF_UP));
		} else {
			tmpsds.setBigDecimal("KOLPAK", nula);
		}
		} else {
			tmpsds.setBigDecimal("KOLPAK",glavonja.getDataSet().getBigDecimal("KOL1"));	
		
		} 
/*		if (glavonja.getDataSet().getBigDecimal("KOL2").compareTo(nula) == 0) {
			if (tmpsds.getBigDecimal("BRJEDKOL").compareTo(nula) != 0) {
				tmpsds.setBigDecimal("KOLKOL", kol
						.divide(artikli.getBigDecimal("BRJEDKOL"), 3,
								BigDecimal.ROUND_HALF_UP));
			} else {
				tmpsds.setBigDecimal("KOLKOL", nula);
			}
		} else {
			tmpsds.setBigDecimal("KOLKOL", glavonja.getDataSet().getBigDecimal(
					"KOL2"));

		} 
		*/

		jtfkol1.setColumnName("KOL");
		jtfkol1.setDataSet(tmpsds);
		jrljm1.setColumnName("JM");
		jrljm1.setDataSet(tmpsds);

		jtfkol2.setColumnName("KOLPAK");
		jtfkol2.setDataSet(tmpsds);
		jrljm2.setColumnName("JMPAK");
		jrljm2.setDataSet(tmpsds);

		jtfkol3.setColumnName("KOLKOL");
		jtfkol3.setDataSet(tmpsds);
		jrljm3.setColumnName("JMKOL");
		jrljm3.setDataSet(tmpsds);
	}

	public void initLP() {

		setupSDS();
		XYLayout xyl = new XYLayout();
		xyl.setHeight(100);
		xyl.setWidth(280);
		localpanel.setLayout(xyl);

		localpanel.setBorder(BorderFactory.createEtchedBorder());
/*
		localpanel.add(jlkol1, new XYConstraints(15, 10, 100, -1));
		localpanel.add(jtfkol1, new XYConstraints(125, 10, 100, -1));
		localpanel.add(jrljm1, new XYConstraints(235, 10, 30, -1));

		localpanel.add(jlkol2, new XYConstraints(15, 35, 100, -1));
		localpanel.add(jtfkol2, new XYConstraints(125, 35, 100, -1));
		localpanel.add(jrljm2, new XYConstraints(235, 35, 30, -1));

		localpanel.add(jlkol3, new XYConstraints(15, 60, 100, -1));
		localpanel.add(jtfkol3, new XYConstraints(125, 60, 100, -1));
		localpanel.add(jrljm3, new XYConstraints(235, 60, 30, -1));
*/		
		
		localpanel.add(jlkol3, new XYConstraints(15, 10, 100, -1));
		localpanel.add(jtfkol3, new XYConstraints(125, 10, 100, -1));
		localpanel.add(jrljm3, new XYConstraints(235, 10, 30, -1));

		localpanel.add(jlkol2, new XYConstraints(15, 35, 100, -1));
		localpanel.add(jtfkol2, new XYConstraints(125, 35, 100, -1));
		localpanel.add(jrljm2, new XYConstraints(235, 35, 30, -1));

		
		localpanel.add(jlkol1, new XYConstraints(15, 60, 100, -1));
		localpanel.add(jtfkol1, new XYConstraints(125, 60, 100, -1));
		localpanel.add(jrljm1, new XYConstraints(235, 60, 30, -1));
		
		

		jtfkol1.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				jtfkol1Change();
			}
		});

		jtfkol2.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				jtfkol2Change();
			}
		});

		jtfkol3.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				jtfkol3Change();
			}
		});

		if (artikli.getBigDecimal("BRJED").compareTo(nula) == 0) {
			raCommonClass.getraCommonClass().setLabelLaF(jtfkol2, false);
		} else {
			raCommonClass.getraCommonClass().setLabelLaF(jtfkol2, true);
		}
		if (artikli.getBigDecimal("BRJEDKOL").compareTo(nula) == 0) {
			raCommonClass.getraCommonClass().setLabelLaF(jtfkol3, false);
		} else {
			raCommonClass.getraCommonClass().setLabelLaF(jtfkol3, true);
		}
	}

	private JraMultyKolChooser() {
	}

	public JraMultyKolChooser(JraTextMultyKolField jtf) {

		try {

			jtf.getDataSet().post();
			glavonja = jtf;
			glavonja.txtBefore = glavonja.getText();
			cart = jtf.getDataSet().getInt("CART");
			kol = jtf.getDataSet().getBigDecimal("KOL");
			artikli = Util
					.getNewQueryDataSet("SELECT * FROM ARTIKLI WHERE CART = "
							+ cart);
			if (artikli.getRowCount() == 0) {
				artikli = null;
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		initDialog(((JComponent) jtf).getTopLevelAncestor());

	}

	public void show() {
		main.setVisible(true);
	}

	private void initDialog(Container owner) {
		Container parent = owner;
		//        main = new JraDialog((Frame) parent, true);
		if (parent instanceof Dialog)
			main = new JraDialog((Dialog) parent, true);
		else
			main = new JraDialog((Frame) parent, true);

		initLP();
		addDialogListeners();
		main.setTitle("Paralelne kolièine");
		main.getContentPane().setLayout(new BorderLayout());
		main.getContentPane().add(localpanel, BorderLayout.CENTER);
		main.getContentPane().add(okp, BorderLayout.SOUTH);
		main.pack();
		if (location == null)
			location = new Point(
					Toolkit.getDefaultToolkit().getScreenSize().width / 2
							- main.getWidth() / 2, Toolkit.getDefaultToolkit()
							.getScreenSize().width / 4);
		main.setLocation(location);
	}

	private void addDialogListeners() {
		main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		main.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		main.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == e.VK_ESCAPE) {
					close();
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});
		okp.registerOKPanelKeys(main);

	}

	private void close() {
		if (main != null) {
			main.setVisible(false);
			main.dispose();
			main = null;
		}
	}

	public void jtfkol1Change() {
/*		
		if (jtfkol1.isValueChanged()) {
			if (tmpsds.getBigDecimal("BRJEDKOL").compareTo(nula) != 0) {
				tmpsds.setBigDecimal("KOLKOL", tmpsds.getBigDecimal("KOL")
						.divide(tmpsds.getBigDecimal("BRJEDKOL"), 3,
								BigDecimal.ROUND_HALF_UP));
			}
			if (tmpsds.getBigDecimal("BRJED").compareTo(nula) != 0) {
				tmpsds.setBigDecimal("KOLPAK", tmpsds.getBigDecimal("KOL")
						.divide(tmpsds.getBigDecimal("BRJED"), 3,
								BigDecimal.ROUND_HALF_UP));
			}
		}
*/		
	}

	public void jtfkol2Change() {
		
		if (jtfkol2.isValueChanged()) {
			tmpsds.setBigDecimal("KOL", tmpsds.getBigDecimal("KOLPAK")
					.multiply(tmpsds.getBigDecimal("BRJED")).setScale(3,
							BigDecimal.ROUND_HALF_UP));
/*
			if (tmpsds.getBigDecimal("BRJEDKOL").compareTo(nula) != 0) {
				tmpsds.setBigDecimal("KOLKOL", tmpsds.getBigDecimal("KOL")
						.divide(tmpsds.getBigDecimal("BRJEDKOL"), 3,
								BigDecimal.ROUND_HALF_UP));
			} else {
				tmpsds.setBigDecimal("KOLKOL", nula);
			}
*/			
		}
		
	}

	public void jtfkol3Change() {
		
		if (jtfkol3.isValueChanged()) {
			tmpsds.setBigDecimal("KOL", tmpsds.getBigDecimal("KOLKOL")
					.multiply(tmpsds.getBigDecimal("BRJEDKOL")).setScale(3,
							BigDecimal.ROUND_HALF_UP));
			if (tmpsds.getBigDecimal("BRJED").compareTo(nula) != 0) {
				tmpsds.setBigDecimal("KOLPAK", tmpsds.getBigDecimal("KOL")
						.divide(tmpsds.getBigDecimal("BRJED"), 3,
								BigDecimal.ROUND_HALF_UP));
			} else {
				tmpsds.setBigDecimal("KOLPAK", nula);
			}
		}
		
	}
}