/****license*****************************************************************
**   file: raPartialIncrementor.java
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
package hr.restart.util;

import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raPartialIncrementor {
  JraTextField dest, nextField;
  ArrayList elems = new ArrayList();

  public raPartialIncrementor(JraTextField dest) {
    this.dest = dest;
    dest.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F9)
          findNextBroj();
      }
    });
  }
  
  public void setNextField(JraTextField next) {
    this.nextField = next;
  }

  public void init(DataSet all, String column) {
    elems.clear();
    all.open();
    for (all.first(); all.inBounds(); all.next())
      elems.add(all.getString(column));
    all.close();
  }

  private void findNextBroj() {
    ArrayList ee = elems;
    String ms = dest.getText(), pref = ms.toLowerCase();
    if (dest.getFieldMask() instanceof raTextMask) {
      raTextMask m = (raTextMask) dest.getFieldMask();
      if (m.isMasked())
        pref = pref.replace(m.getMaskCharacter(), ' ').trim();
    }
    int max = 0;
    for (Iterator i = elems.iterator(); i.hasNext(); ) {
      String el = (String) i.next();
      if (pref.length() == 0 || el.toLowerCase().startsWith(pref)) {
        int n = Aus.getNumber(el.substring(pref.length()));
        if (n > max) {
          max = n;
          ms = el;
        }
      }
    }
    String suf = ms.substring(pref.length());
    if (!Aus.isDigit(suf)) return;
    int ch = Math.max(suf.length(), String.valueOf(max + 1).length());
    suf = Aus.string(ch, '0') + String.valueOf(max + 1);
    String next = ms.substring(0, pref.length()) + suf.substring(suf.length() - ch);
    dest.setText(next);
    if (nextField == null) dest.selectAll();
    else {
      nextField.requestFocusLater();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          raCommonClass.getraCommonClass().setLabelLaF(dest, false);
        }
      });
    }
  }
}
