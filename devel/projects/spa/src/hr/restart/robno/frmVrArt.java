/****license*****************************************************************
**   file: frmVrArt.java
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
package hr.restart.robno;

import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class frmVrArt extends raSifraNaziv {
	hr.restart.baza.dM dm;

	JPanel jp = new JPanel();

	XYLayout xYLayout1 = new XYLayout();

	JLabel jlKontoPrihoda = new JLabel();

	JlrNavField jrfKontoPrihoda = new JlrNavField();

	JlrNavField jrfNazivKontaPrihoda = new JlrNavField();

	JraButton jbKontoPrihoda = new JraButton();

	JLabel jlKontoPoreza = new JLabel();

	JlrNavField jrfKontoPoreza = new JlrNavField();

	JlrNavField jrfNazivKontaPoreza = new JlrNavField();

	JraButton jbKontoPoreza = new JraButton();

	public frmVrArt() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void raQueryDataSet_navigated(
			com.borland.dx.dataset.NavigationEvent e) {
//		jrfKontoPrihoda.forceFocLost();
//		jrfKontoPoreza.forceFocLost();
	}

	private void jbInit() throws Exception {

		dm = hr.restart.baza.dM.getDataModule();
		xYLayout1.setHeight(60);
		jp.setLayout(xYLayout1);
		this.setRaDataSet(dm.getVrart());
		this.setRaColumnSifra("CVRART");
		this.setRaColumnNaziv("NAZVRART");
		this.setRaText("Vrste artikla");

		jlKontoPrihoda.setText("Konto prihoda");
		jrfKontoPrihoda.setColumnName("KONTOPRI");
		jrfKontoPrihoda.setNavColumnName("BROJKONTA");
		jrfKontoPrihoda.setDataSet(dm.getVrart());
		jrfKontoPrihoda.setColNames(new String[] { "NAZIVKONTA" });
		jrfKontoPrihoda.setRaDataSet(hr.restart.zapod.raKonta
				.getAnalitickaKonta());
		jrfKontoPrihoda
				.setTextFields(new javax.swing.text.JTextComponent[] { jrfNazivKontaPrihoda });
		jrfKontoPrihoda.setSearchMode(3);
		jrfKontoPrihoda.setVisCols(new int[] { 0, 1 });
		jrfKontoPrihoda.setNavButton(this.jbKontoPrihoda);

		jrfNazivKontaPrihoda.setColumnName("NAZIVKONTA");
		jrfNazivKontaPrihoda.setSearchMode(1);
		jrfNazivKontaPrihoda.setNavProperties(jrfKontoPrihoda);
		jbKontoPrihoda.setText("...");

		jlKontoPoreza.setText("Konto poreza");
		jrfKontoPoreza.setColumnName("KONTOPDV");
		jrfKontoPoreza.setNavColumnName("BROJKONTA");
		jrfKontoPoreza.setDataSet(dm.getVrart());
		jrfKontoPoreza.setColNames(new String[] { "NAZIVKONTA" });
		jrfKontoPoreza.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
		jrfKontoPoreza
				.setTextFields(new javax.swing.text.JTextComponent[] { jrfNazivKontaPoreza });
		jrfKontoPoreza.setSearchMode(3);
		jrfKontoPoreza.setVisCols(new int[] { 0, 1 });
		jrfKontoPoreza.setNavButton(this.jbKontoPoreza);

		jrfNazivKontaPoreza.setNavProperties(jrfKontoPoreza);
		jrfNazivKontaPoreza.setSearchMode(1);
		jrfNazivKontaPoreza.setColumnName("NAZIVKONTA");
		jbKontoPoreza.setText("...");

		jp.add(jlKontoPrihoda, new XYConstraints(15, 0, -1, -1));
		jp.add(jrfKontoPrihoda, new XYConstraints(150, 0, 100, -1));
		jp.add(jrfNazivKontaPrihoda, new XYConstraints(255, 0, 260, -1));
		jp.add(jbKontoPrihoda, new XYConstraints(519, 0, 21, 21));
		jp.add(jlKontoPoreza, new XYConstraints(15, 25, -1, -1));
		jp.add(jrfKontoPoreza, new XYConstraints(150, 25, 100, -1));
		jp.add(jrfNazivKontaPoreza, new XYConstraints(255, 25, 260, -1));
		jp.add(jbKontoPoreza, new XYConstraints(519, 25, 21, 21));
		this.jpRoot.add(jp, java.awt.BorderLayout.SOUTH);
	}

	public void SetFokus2(char c) {
		if (c == 'N') {
			jrfKontoPrihoda.setText("");
			jrfKontoPoreza.setText("");
			jrfKontoPrihoda.emptyTextFields();
			jrfKontoPoreza.emptyTextFields();
		}
	}

}