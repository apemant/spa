/****license*****************************************************************
**   file: XYPanel.java
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
package hr.restart.swing;

import hr.restart.robno.rapancart;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.VarStr;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class XYPanel extends JraPanel {
	public XYLayout lay;
	public int up = 15;
	public int bottom = 15;
	public int above = 18;
	public int left = 15;
	public int text = 150;
	public int xspac = 5;
	public int yadd = 25;
	public int sheight = 21;
	public int cwidth = 100;
	public int twidth = 325;
	
	public int s_wid = 50;
	public int i_wid = 75;
	public int n_wid = 100;
	public int t_wid = 100;
	public int s3_wid = 50;
	public int s12_wid = 100;
	public int s30_wid = 250;
	public int s50_wid = 325;
	public int ss_wid = 375;
	
	public int y = 15;
	public int x = 0;
	
	private int maxx = 0, maxy = 0, lastx = 0;
	
	HashMap texts = new HashMap();
	HashMap navs = new HashMap();
	HashMap buts = new HashMap();
	HashMap chks = new HashMap();
	
	public rapancart rpc;
	
	public DataSet ds;
	
	public XYPanel() {
		setLayout(lay = new XYLayout());
	}
	
	public XYPanel(DataSet ds) {
		this();
		this.ds = ds;
	}
	
	public void setDataSet(DataSet ds) {
		this.ds = ds;
	}
	
	public XYPanel nl() {
		x = 0;
		y += yadd;
		check(0, 0);
		return this;
	}
	
	public XYPanel down(int d) {
		y += d;
		x = 0;
		check(0, 0);
		return this;
	}
	
	public XYPanel skip(int w) {
		x += w;
		lastx = x;
		check(0, 0);
		return this;
	}
	
	private void check(int w, int h) {
		if (x + w > maxx) maxx = x + w;
		if (y + h > maxy) maxy = y + h;
	}
	
	private JLabel createLab(String lab) {
		JLabel label = new JLabel(lab);
		if (lab.startsWith("~")) {
			label.setText(lab.substring(1));
			label.setHorizontalAlignment(SwingConstants.RIGHT);
		} else if (lab.endsWith("~")) {
			label.setText(lab.substring(0, lab.length() - 1));
			label.setHorizontalAlignment(SwingConstants.LEFT);
		} else 
			label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
	private JLabel createLab(DataSet ds, String colName) {
		JLabel label = new JLabel(ds.getColumn(colName).getCaption());
		if (ds.getColumn(colName).getDataType() != Variant.STRING)
			label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
	private JlrNavField createNav() {
		return new JlrNavField() {
			public void valueChanged() {
				changed(this);
			}
		};
	}
	
	private JraTextField createText() {
		return new JraTextField() {
			public void valueChanged() {
				changed(this);
			}
		};
	}
	
	private JraCheckBox createCheck() {
		JraCheckBox cb = new JraCheckBox();
		cb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changed((JraCheckBox) e.getSource());
			}
		});
		return cb;
	}
	
	public JLabel addLabel(String label) {
		if (x == 0) x = left;
		JLabel lab = new JLabel(label);
		add(lab, new XYConstraints(x, y, -1, -1));
		if (x == left) x = text;
		check(0, sheight);
		return lab;
	}
	
	public XYPanel label(String label) {
		addLabel(label);
		return this;
	}
	
	public JLabel addLabel(String label, int width) {
		JLabel lab = createLab(label);
		add(lab, new XYConstraints(x, y, width, -1));
		x += width;
		check(0, sheight);
		return lab;
	}
	
	public XYPanel label(String label, int width) {
		addLabel(label, width);
		return this;
	}
	
	private int getWidth(DataSet ds, String colName) {
		Column col = ds.getColumn(colName);
		if (col.getDataType() == Variant.STRING) {
			if (col.getPrecision() <= 3) return s3_wid;
			if (col.getPrecision() <= 12) return s12_wid;
			if (col.getPrecision() <= 30) return s30_wid;
			if (col.getPrecision() <= 50) return s50_wid;
			return ss_wid;
		} 
		if (col.getDataType() == Variant.INT) return i_wid;
		if (col.getDataType() == Variant.SHORT) return s_wid;
		if (col.getDataType() == Variant.BIGDECIMAL) return n_wid;
		if (col.getDataType() == Variant.TIMESTAMP) return t_wid;

		return cwidth;
	}
	
	public JraTextField addText(String colName) {
		if (x == 0 && ds != null) {
			addLabel(ds.getColumn(colName).getCaption());
			x = text;
		}
		int wid = getWidth(ds, colName);
		JraTextField tf = createText();
		texts.put(colName, tf);
		tf.setDataSet(ds);
		tf.setColumnName(colName);
		add(tf, new XYConstraints(x, y, wid, -1));
		x += wid + xspac;
		lastx = x - xspac;
		check(0, sheight);
		return tf;
	}
	
	public XYPanel text(String colName) {
		addText(colName);
		return this;
	}
	
	public JraTextField addText(String colName, String lab, int tw) {
		if (tw < 0) tw = lastx - x;
		if (lab != null) {
			JLabel label = createLab(lab);
			add(label, new XYConstraints(x + 1, y - above, tw - 2, -1));
		}
		
		JraTextField tf = createText();
		texts.put(colName, tf);
		tf.setDataSet(ds);
		tf.setColumnName(colName);
		add(tf, new XYConstraints(x, y, tw, -1));
		x += tw + xspac;
		lastx = x - xspac;
		check(0, sheight);
		return tf;
	}
	
	public XYPanel text(String colName, String lab, int tw) {
		addText(colName, lab, tw);
		return this;
	}
	
	public XYPanel text(String colName, int tw) {
		addText(colName, null, tw);
		return this;
	}
	
	public JlrNavField addNav(String cols, DataSet raSet, int[] visCols, boolean isAbove) {
		String[] colNames = cols.indexOf(',') > 0 ? new VarStr(cols).splitTrimmed(',') : new VarStr(cols).split();
		
		return addNav(colNames, null, raSet, visCols, isAbove);
	}
	
	public XYPanel nav(String cols, DataSet raSet, int[] visCols, boolean isAbove) {
		addNav(cols, raSet, visCols, isAbove);
		return this;
	}
	
	public JlrNavField addNav(String[] colNames, int[] wids, DataSet raSet, int[] visCols, boolean isAbove) {
		
		String colName = colNames[0];
		String navName = colName;
		int np = colName.indexOf(':');
		if (np > 0) {
			navName = colName.substring(np + 1);
			colName = colName.substring(0, np);
		}
		
		int wid = wids != null ? wids[0] : getWidth(raSet, navName);
		if (isAbove) add(createLab(raSet, navName), new XYConstraints(x + 1, y - above, wid - 2, -1));
		
		JlrNavField jlr = createNav();
		navs.put(colName, jlr);
		jlr.setDataSet(ds);
		jlr.setNavColumnName(navName);
		jlr.setColumnName(colName);
		jlr.setDoh(raSet, visCols != null ? visCols : new int[] {0,1});
		jlr.setNavButton(new JraButton());
		buts.put(colName, jlr.getNavButton());

		add(jlr, new XYConstraints(x, y, wid, -1));
		x += wid + xspac;
		
		for (int i = 1; i < colNames.length; i++) {
			int nwid = wids != null ? wids[i] : getWidth(raSet, colNames[i]);
			if (isAbove) add(createLab(raSet, colNames[i]), new XYConstraints(x + 1, y - above, nwid - 2, -1));
			
			JlrNavField naz = createNav();
			jlr.addNav(naz, colNames[i]);
			navs.put(colNames[i], naz);
			
			add(naz, new XYConstraints(x, y, nwid, -1));
			x += nwid + xspac;
		}
		lastx = x - xspac;
		add(jlr.getNavButton(), new XYConstraints(x, y, sheight, sheight));
		check(sheight, sheight);
		return jlr;
	}
	
	public XYPanel nav(String[] colNames, int[] wids, DataSet raSet, int[] visCols, boolean isAbove) {
	  addNav(colNames, wids, raSet, visCols, isAbove);
	  return this;
	}
	
	public XYPanel nav(String[] colNames, DataSet raSet, int[] visCols, boolean above) {
		addNav(colNames, null, raSet, visCols, above);
		return this;
	}
	
	public XYPanel nav(String[] colNames, int[] wids, DataSet raSet, int[] visCols) {
      addNav(colNames, wids, raSet, visCols, false);
      return this;
    }
	
	public XYPanel nav(String[] colNames, int[] wids, DataSet raSet) {
      addNav(colNames, wids, raSet, null, false);
      return this;
    }
	
	public JlrNavField addNav(String colName, DataSet raSet) {
		return addNav(colName, raSet, null, false);
	}
	
	public XYPanel nav(String colName, DataSet raSet) {
		addNav(colName, raSet);
		return this;
	}
	
	public JlrNavField addNav(String colName, DataSet raSet, String nazName) {
		return addNav(new String[] {colName, nazName}, null, raSet, null, false);
	}
	
	public XYPanel nav(String colName, DataSet raSet, String nazName) {
		addNav(colName, raSet, nazName);
		return this;
	}
	
	public XYPanel comp(JComponent comp) {
		add(comp, new XYConstraints(x, y, -1, -1));
		return this;
	}
	
	public XYPanel comp(JComponent comp, int cw) {
		add(comp, new XYConstraints(x, y, cw, -1));
		x += cw + xspac;
		lastx = x - xspac;
		check(0, sheight);
		return this;
	}
	
	public XYPanel combo(JraComboBox jcb, int cw) {
		add(jcb, new XYConstraints(x, y, cw, sheight));
		x += cw + xspac;
		lastx = x - xspac;
		check(0, sheight);
		return this;
	}
	
	public JraCheckBox addCheck(String label, String colName) {
		if (x == 0) x = text;
		
		JraCheckBox cb = createCheck();
		cb.setText(label);
		if (colName != null) {
			cb.setDataSet(ds);
			cb.setColumnName(colName);
			cb.setSelectedDataValue("D");
			cb.setUnselectedDataValue("N");
			chks.put(colName, cb);
		}
		cb.setHorizontalAlignment(SwingConstants.LEADING);
		cb.setHorizontalTextPosition(SwingConstants.TRAILING);
		add(cb, new XYConstraints(x, y, -1, -1));
		return cb;
	}
	
	public XYPanel check(String label, String colName) {
		addCheck(label, colName);
		return this;
	}
	
	public XYPanel parse(String def) {
		if (def.indexOf('(') > 0 && def.indexOf(')') > 0) {
			if (y == up) y += above / 2;
			else y += above;
		}
		int lp = def.indexOf(':');
		if (lp > 0) {
			if (x == 0) x = left;
			String lab = def.substring(0, lp);
			def = def.substring(lp + 1);
			int wp = lab.indexOf('/');
			if (wp < 0) {
				add(new JLabel(lab), new XYConstraints(x, y, -1, -1));
				if (x == left) x = text;
			} else {
				String wid = lab.substring(wp + 1);
				JLabel label = new JLabel(lab.substring(0, wp));
				if (Aus.isDigit(wid)) {
					label.setHorizontalAlignment(SwingConstants.CENTER);
					add(new JLabel(lab), new XYConstraints(x + 1, y, Aus.getNumber(wid), -1));
					x += Aus.getNumber(wid);
				} else {
					int pp = wid.indexOf('+');
					x += Aus.getNumber(wid.substring(0, pp));
					add(new JLabel(lab), new XYConstraints(x, y, -1, -1));
					x += Aus.getNumber(wid.substring(pp + 1));
				}
			}
		}
		
		// todo
		
		return this;
	}
	
	public XYPanel expand() {
		lay.setWidth(maxx + left);
		lay.setHeight(maxy + bottom);
		return this;
	}
	
	public JraTextField getText(String colName) {
		return (JraTextField) texts.get(colName);
	}
	
	public JlrNavField getNav(String colName) {
		return (JlrNavField) navs.get(colName);
	}
	
	public JraButton getBut(String colName) {
		return (JraButton) buts.get(colName);
	}
	
	public JraCheckBox getCheck(String colName) {
		return (JraCheckBox) chks.get(colName);
	}
	
	protected void changed(JraTextField tf) {
		
	}
	
	protected void changed(JlrNavField tf) {
		changed((JraTextField) tf);
	}
	
	protected void changed(JraCheckBox cb) {
		
	}
}
