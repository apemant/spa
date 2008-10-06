/****license*****************************************************************
**   file: jpGkMonthRange.java
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
 * Created on Sep 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class jpGkMonthRange extends JPanel {
  JLabel jl = new JLabel();
  JLabel jlcrtica = new JLabel("-");
  JraTextField mfrom = new JraTextField() {
    public boolean maskCheck() {
      if (!super.maskCheck()) return false;
      return m_maskCheck(this);
    }
  };
  JraTextField mto = new JraTextField() {
    public boolean maskCheck() {
      if (!super.maskCheck()) return false;
      return m_maskCheck(this);
    }
  };
  JraTextField year = new JraTextField();
  int mfpos, mfwidth, mtpos, mtwidth, ypos, ywidth;
  String jltext;
  private StorageDataSet ds;
  /**
   * U konstruktoru se zadaju pozicija i sirina komponenti na ekranu,
   * ako je vrijednost manja od 0 koristi se default, osim za stringove 
   * gdje za default treba zadati null  
   * @param _mfpos
   * @param _mfwidth
   * @param _mtpos
   * @param _mtwidth
   * @param _ypos
   * @param _ywidth
   * @param _jltext
   */
  public jpGkMonthRange(int _mfpos, int _mfwidth, int _mtpos, int _mtwidth, int _ypos, int _ywidth, String _jltext) {
    if (_mfpos < 0) {
      mfpos = 150;
    } else {
      mfpos = _mfpos;
    }
    if (_mfwidth < 0) {
      mfwidth = 35;
    } else {
      mfwidth = _mfwidth;
    }
    
    if (_mtpos < 0) {
      mtpos = 215;
    } else {
      mtpos = _mtpos;
    }
    if (_mtwidth < 0) {
      mtwidth = 35;
    } else {
      mtwidth = _mtwidth;
    }

    if (_ypos < 0) {
      ypos = 255;
    } else {
      ypos = _ypos;
    }
    if (_mtwidth < 0) {
      ywidth = 50;
    } else {
      ywidth = _ywidth;
    }
    if (_jltext == null) {
      jltext = "Period (mm - mm gggg)";
    } else {
      jltext = _jltext;
    }
    dsInit();
    jpInit();
  }
  
  public jpGkMonthRange() {
    this(-1,-1,-1,-1,-1,-1,null);
  }

  /**
   * 
   */
  private void dsInit() {
    ds = new StorageDataSet();
    ds.addColumn(dM.createIntColumn("MF"));
    ds.addColumn(dM.createIntColumn("MT"));
    ds.addColumn(dM.createIntColumn("Y"));
    mfrom.setDataSet(ds);
    mfrom.setColumnName("MF");
    mto.setDataSet(ds);
    mto.setColumnName("MT");
    year.setDataSet(ds);
    year.setColumnName("Y");
  }
  public final StorageDataSet getData() {
    return ds;
  }

  /**
   * 
   */
  private void jpInit() {
    XYLayout lay = new XYLayout();
    setLayout(lay);
    lay.setHeight(30);
    lay.setWidth(ypos+ywidth+20);
    jl.setText(jltext);
    add(jl,new XYConstraints(15,0,-1,-1));
    add(mfrom,new XYConstraints(mfpos,0,mfwidth,-1));
    add(mto,new XYConstraints(mtpos,0,mtwidth,-1));
    add(year,new XYConstraints(ypos,0,ywidth,-1));
    add(jlcrtica,new XYConstraints(
        (int)((mfpos+mfwidth)+(mtpos-mfpos-mfwidth-10)/2),0,-1,-1));
  }
  /**
   * Mozda iskoristiti u frmBrutoBilanca - overridati metodu maskCheck od 
   * text fielda i pozvati ovu metodu nakon super.maskCheck().
   * <pre>
   * Ustvari kopy-pejst ovoga se trazi:
   * <code>
   * public boolean maskCheck() {
   *   if (!super.maskCheck()) return false;
   *   return m_maskCheck(this);
   * }
   * </code>
   * </pre>
   * @param _tf 
   * @return
   */
  public static boolean m_maskCheck(JraTextField _tf) {
    try {
      if (_tf.getText().trim().equals("")) _tf.setText("0");
  		if (Integer.parseInt(_tf.getText())>12) {
  			_tf.setErrText("Nepostoje\u0107i mjesec");
  		  _tf.this_ExceptionHandling(new java.lang.Exception());
  		  return false;
  		}      
    } catch (Exception e) {
      return false;
    }
		return true;
  }
}
