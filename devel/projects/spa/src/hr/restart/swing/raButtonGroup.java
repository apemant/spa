/****license*****************************************************************
**   file: raButtonGroup.java
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

import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: raButtonGroup</p>
 * <p>Description: Nadogradnja ButtonGroupa.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * <p>Nadogradnja ButtonGroupa za povezivanje JraRadioButtona u kružnu listu,
 * tako da se buttoni prebacuju kurzorskim tipkama lijevo i desno. Implementirane su
 * metode za promjenu odgovarajuæih propertija svim JraRadioButtonima koji
 * pripadaju ovoj grupi.</p>
 * <p>Nakon instanciranja klase i svih pripadajuæih JraRadioButtona, odrediti
 * dataset i ime kolone na koju se buttoni ove grupe bindaju, te metodama
 * add(..) dodati buttone u grupu. Buttone dodavati prirodnim redoslijedom
 * (s lijeva na desno, red po red odozgo prema dolje, kao knjiga), da bi ih
 * se moglo povezati kako treba.</p>
 * <p>Primjer:
 * <pre>
 *   raButtonGroup bg = new raButtonGroup();
 *   JraRadioButton jrbURA = new JraRadioButton();
 *   JraRadioButton jrbIRA = new JraRadioButton();
 *   ...
 *   private void jbInit() throws Exception {
 *     ...
 *     bg.setColumnName("URAIRA");
 *	    bg.setDataSet(this.getRaQueryDataSet());
 *	    bg.add(jrbIRA, "Izlazni", "I");
 *	    bg.add(jrbURA, "Ulazni", "U");
 *     ...
 *     jpDetail.add(jrbURA ...);
 *     jpDetail.add(jrbIRA ...);
 *     ...
 *   }
 * </pre>
 * i to je to.</p>
 * <p>Napomena. Prilikom unosa novog sloga (mode == 'N') negdje je potrebno pozvati metodu
 * setSelected na jednom od radio buttona (npr. defaultnom ili prethodnom). Najbolje u metodi
 * SetFokus().</p>
 * @author ab.f
 * @version 1.0
 */
public class raButtonGroup extends ButtonGroup {

  /**
	* Oznaka za automatsku detekciju odgovarajuæeg naèina povezivanja.
	*/
  public static final int AUTO = -1;

  /**
	* Oznaka za povezivanje buttona u kružnu listu, kroz koju se pomièe
	* kurzorskim tipkama gore-dolje ili lijevo-desno.
	*/
  public static final int NORMAL = 0;

  /**
	* Isto što i NORMAL.
	*/
  public static final int CIRCULAR = 0;

  /**
	* Oznaka za povezivanje buttona u grid. Kurzorske tipke ponašat æe
	* se kako bi se i oèekivalo.
	*/
  public static final int GRID = 1;

  /**
	* Oznaka za povezianje buttona na naèin kako to radi BorderLayout. Svaka
	* kurzorska tipka pali odgovarajuæi button. Buttoni MORAJU biti dodani
	* redoslijedom NORTH, WEST, EAST, SOUTH da bi tipke ispravno radile.
	*/
  public static final int BORDERLIKE = 2;

//  private LinkedList buttons = new LinkedList();
  private DataSet bds = null;
  private String colname = null;
  private int textPos = SwingConstants.LEADING;
  private int align = SwingConstants.RIGHT;
  private int cursorModel = AUTO;
  private int bnum = 0;
  private int rownum = 0, colnum = 0;

  /**
	* Default konstruktor. Default vrijednosti za položaj radio buttona i teksta
	* su SwingConstants.LEADING (tekst prije buttona) i SwingConstants.RIGHT
	* (poravnanje po desnom rubu).
	*/
  public raButtonGroup() {
  }

  /**
	* Konstruktor za eksplicitno zadavanje vrijednosti za položaj radio buttona
	* i teksta.<p>
	* @param textPosition Položaj teksta u odnosu na buttone (SwingConstants.-).
	* @param alignment Poravnanje cijele komponente radio buttona (SwingConstants.-).
	*/
  public raButtonGroup(int textPosition, int alignment) {
	 textPos = textPosition;
	 align = alignment;
  }

  /**
	* Odreðuje položaj teksta u odnosu na button, za sve buttone koji pripadaju
	* ovoj grupi (i one koji æe eventualno kasnije biti dodani).<p>
	* @param textPosition Položaj teksta (SwingConstants.LEADING ili TRAILING).
	*/
  public void setHorizontalTextPosition(int textPosition) {
	 textPos = textPosition;
	 Iterator i = buttons.iterator();
	 while (i.hasNext())
		((AbstractButton) i.next()).setHorizontalTextPosition(textPos);
  }

  /**
	* Odreðuje poravnanje button komponenti koji pripadaju ovoj grupi,
	* i one koji æe eventualno kasnije biti dodani.<p>
	* @param alignment Poravnanje komponenti (SwingConstants.RIGHT, LEFT ili CENTER).
	*/
  public void setHorizontalAlignment(int alignment) {
	 align = alignment;
	 Iterator i = buttons.iterator();
	 while (i.hasNext())
		((AbstractButton) i.next()).setHorizontalAlignment(align);
  }

  /**
	* Dodaje JraRadioButton u ovu grupu.<p>
	* @param b button.
	*/
  public void add(final AbstractButton b) {
    final raAbstractButton rab;
    if (b instanceof raAbstractButton) rab = (raAbstractButton)b;
    else rab = null;
    if (!buttons.isEmpty()) {
      if (rab!=null) rab.setFocusTraversable(true);
	  }
    b.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
        setSelected(b);
        b.requestFocus();
      }
    });
    AWTKeyboard.registerKeyListener(b, new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == e.VK_RIGHT) || (e.getKeyCode() == e.VK_LEFT) ||
          (e.getKeyCode() == e.VK_UP) || (e.getKeyCode() == e.VK_DOWN)) {
         buttonKeyPressed(b, e.getKeyCode());
         e.consume();
        }
      }
    });
    b.addAncestorListener(new javax.swing.event.AncestorListener() {
      public void ancestorAdded(javax.swing.event.AncestorEvent e) {
        if (rab != null)
          if (rab.getBinder().getDataSet() != null) {
            rab.setFocusTraversable(
              rab.getBinder().getBindedValue()==((raButtonDataBinder)rab.getBinder()).getSelectedValue()
            );
          }
      }
      public void ancestorMoved(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
      }
    });
    super.add(b);
    if (rab!=null) {
      rab.setButtonGroup(this);
      rab.getBinder().setDataSet(bds);
      rab.getBinder().setColumnName(colname);
    }
    bnum += 1;
    b.setHorizontalTextPosition(textPos);
    b.setHorizontalAlignment(align);
  }

  /**
	* Dodaje JraRadioButton u ovu grupu, uz setiranje teksta buttona, tako
	* da se ne treba zvati metoda setText na JraRadioButtonu.<p>
	* @param b button.
	* @param text tekst buttona.
	*/
  public void add(AbstractButton b, String text) {
	 add(b);
	 b.setText(text);
  }

  /**
	* Dodaje JraRadioButton u ovu grupu, uz setiranje teksta buttona i
	* vrijednosti odgovarajuæe kolone u DataSet-u koja odgovara ovom buttonu.<p>
	* @param b button.
	* @param text tekst buttona.
	* @param value vrijednost kolone u datasetu koja odgovara ovom buttonu.
	*/
  public void add(AbstractButton b, String text, String value) {
    add(b, text);
    if (b instanceof raAbstractButton)
      ((raButtonDataBinder)((raAbstractButton)b).getBinder()).setSelectedValue(value);
  }

  /**
	* Zadaje DataSet za sve buttone koji pripadaju ovoj grupi, i one koji
	* æe kasnije biti dodani.<p>
	* @param ds DataSet na koji se bajndaju buttoni ove grupe.
	*/
  public void setDataSet(DataSet ds) {
    bds = ds;
  }

  /**
	* Getter za DataSet.<p>
	* @return dataset ove button grupe.
	*/
  public DataSet getDataSet() {
	 return bds;
  }

  /**
	* Zadaje ime kolone na koju se bajndaju buttoni koji pripadaju ovoj grupi,
	* i one koji æe eventualno kasnije biti dodani.<p>
	* @param col ime kolone na koju se buttoni bajndaju.
	*/
  public void setColumnName(String col) {
	 colname = col;
  }

  /**
	* Getter za columnName.<p>
	* @return ime kolone na koju se bajndaju buttoni ove grupe.
	*/
  public String getColumnName() {
	 return colname;
  }

  /**
	* Metoda koja se koristi interno. Nema potrebe zvati.<p>
	* @param sb selektirani radio button.
	*/
  public void setSelected(AbstractButton sb) {
	 AbstractButton b;
	 Iterator i = buttons.iterator();
	 if (cursorModel == AUTO)
		setCursorModel();
	 while (i.hasNext()) {
		b = (AbstractButton) i.next();
		if (b == sb) {
      selectButton(b,true);
		} else
      selectButton(b,false);
	 }
  }

  private void selectButton(AbstractButton b,boolean sel) {
      b.setSelected(sel);
		  if (b instanceof raAbstractButton) {
        raAbstractButton rab = (raAbstractButton)b;
        rab.setFocusTraversable(sel);
        raButtonDataBinder bnder = (raButtonDataBinder)rab.getBinder();
        bnder.actionPerformed(null);
        if (sel) selectedValue = bnder.getBindedValue();
      }
  }
/*
		((AbstractButton) target).setSelected(true);
    if (target instanceof raAbstractButton) {
      raButtonDataBinder bnder = (raButtonDataBinder)((raAbstractButton)target).getBinder();
      bnder.actionPerformed(null);
      selectedValue = bnder.getBindedValue();
    }
*/
  private Object selectedValue;

  public Object getSelectedValue() {
    return selectedValue;
  }

  void buttonKeyPressed(AbstractButton b, int keycode) {
	 Object target = null;
	 if (cursorModel == AUTO)
		setCursorModel();
	 if (cursorModel == NORMAL) {
		if (keycode == KeyEvent.VK_LEFT || keycode == KeyEvent.VK_UP)
		  target = buttons.get((buttons.indexOf(b) + bnum - 1) % bnum);
		else
		  target = buttons.get((buttons.indexOf(b) + 1) % bnum);
	 } else if (cursorModel == BORDERLIKE) {
		if (keycode == KeyEvent.VK_UP)
		  target = buttons.get(0);
		if (keycode == KeyEvent.VK_LEFT)
		  target = buttons.get(1);
		if (keycode == KeyEvent.VK_RIGHT)
		  target = buttons.get(2);
		if (keycode == KeyEvent.VK_DOWN)
		  target = buttons.get(3);
	 } else if (cursorModel == GRID) {
		if (keycode == KeyEvent.VK_UP)
		  target = buttons.get((buttons.indexOf(b) + bnum - colnum) % bnum);
		else if (keycode == KeyEvent.VK_DOWN)
		  target = buttons.get((buttons.indexOf(b) + colnum) % bnum);
		else if (keycode == KeyEvent.VK_LEFT)
		  target = buttons.get((buttons.indexOf(b) / colnum) * colnum +
									  (buttons.indexOf(b) + colnum - 1) % colnum);
		else
		  target = buttons.get((buttons.indexOf(b) / colnum) * colnum +
									  (buttons.indexOf(b) + 1) % colnum);
	 }
	 if (target != null) {
	   AbstractButton abTarget = (AbstractButton) target;
       setSelected(abTarget);
       abTarget.requestFocus();
	 }
  }

  private void setGridModel(java.awt.GridLayout l) {
	 java.awt.Container panel = ((AbstractButton) buttons.firstElement()).getParent();
	 int gridcols = l.getColumns();
	 int gridrows = l.getRows();
	 boolean[] cols = new boolean[gridcols];
	 boolean[] rows = new boolean[gridrows];

	 for (int i = 0; i < panel.getComponentCount(); i++)
		if (buttons.contains(panel.getComponent(i))) {
		  cols[i % gridcols] = true;
		  rows[i / gridcols] = true;
		}
	 for (int i = 0; i < cols.length; i++)
		if (cols[i]) ++colnum;
	 for (int i = 0; i < rows.length; i++)
		if (rows[i]) ++rownum;
	 setCursorModel(GRID);
  }

  private void setCursorModel() {
	 if (bnum == 0) return;
	 AbstractButton b = (AbstractButton) buttons.firstElement();
	 if (!(b.getParent() instanceof javax.swing.JPanel)) return;
	 LayoutManager lm = b.getParent().getLayout();
	 if (lm instanceof java.awt.BorderLayout)
		setCursorModel(bnum == 4 ? BORDERLIKE : NORMAL);
	 else if (lm instanceof java.awt.GridLayout)
		setGridModel((java.awt.GridLayout) lm);
	 else if (lm instanceof XYLayout || lm == null)
		setCursorModel(NORMAL);
	 else
		setCursorModel(NORMAL);
  }

  /**
	* Zadaje model ponašanja kurzorskih tipki unutar ove button grupe.
	* Zvati samo ako se toèno zna što se radi.
	* @param model CIRCULAR ili BORDERLIKE.
	*/
  public void setCursorModel(int model) {
	 cursorModel = model;
  }

  /**
	* Zadaje model ponašanja kurzorskih tipki unutar ove button grupe.
	* Zvati samo ako se toèno zna što se radi.
	* @param model GRID.
	* @param cnum broj kolona.
	*/
  public void setCursorModel(int model, int cnum) {
	 cursorModel = model;
	 colnum = cnum;
  }
}
