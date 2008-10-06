/****license*****************************************************************
**   file: jpScroller.java
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
package hr.restart.sisfun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public abstract class jpScroller extends JPanel {
  javax.swing.Timer ticker;
  LinkedList elems = new LinkedList();
  int currenty, width, height, step, tolerance, aligned, space, pause;
  Font font = new JLabel().getFont();
  Color col = new JLabel().getForeground();
  JComponent lastComp;

  public jpScroller(int w, int h) {
    super((LayoutManager) null);
//    this.setBackground(Color.white);
    width = w;
    height = h;
    setSize(w, h);
    ticker = new javax.swing.Timer(32, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tick();
      }
    });
    step = 1;
    space = 5;
    tolerance = 100;
  }

  public Dimension getPreferredSize() {
    return new Dimension(width, height);
  }

  public Dimension getMinimumSize() {
    return new Dimension(width, height);
  }

  public Dimension getMaximumSize() {
    return new Dimension(width, height);
  }

  public jpScroller skip(int vadd) {
    currenty += vadd;
    return this;
  }

  public jpScroller spacing(int spac) {
    space = spac;
    return this;
  }

  public jpScroller append(String text) {
    return append(lastComp == null ? 0 : lastComp.getHeight() + space, new JLabel(text));
  }

  public jpScroller append(int vadd, String text) {
    return append(vadd, new JLabel(text));
  }

  public jpScroller append(int vadd, Image img) {
    return append(vadd, new JLabel(new ImageIcon(img)));
  }

  public jpScroller append(int vadd, JComponent comp) {
    this.add(comp);
    int hpos = aligned;
    if (comp instanceof JLabel) comp.setFont(font);
    if (aligned == 0)
      hpos = width / 2 - comp.getPreferredSize().width / 2;
    if (aligned < 0)
      hpos = width - comp.getPreferredSize().width + aligned;
    comp.setLocation(hpos, currenty += vadd);
    comp.setSize(comp.getPreferredSize());
    comp.setForeground(col);
    elems.add(lastComp = comp);
    return this;
  }

  public jpScroller align(int aligned) {
    this.aligned = aligned;
    return this;
  }

  public jpScroller size(int sz) {
    font = font.deriveFont((float) sz);
    return this;
  }

  public jpScroller bold() {
    font = font.deriveFont(Font.BOLD);
    return this;
  }

  public jpScroller italic() {
    font = font.deriveFont(Font.ITALIC);
    return this;
  }

  public jpScroller plain() {
    font = font.deriveFont(Font.PLAIN);
    return this;
  }

  public jpScroller color(Color c) {
    col = c;
    return this;
  }

  public void setFont(String name) {
    font = Font.decode(name);
  }

  private void tick() {
    if (elems.size() == 0) {
      needData();
      return;
    }
    if (pause > 0) {
      pause -= ticker.getDelay();
      return;
    }
    int bottom = 0, oldstep = step;
    this.setEnabled(false);
    for (ListIterator i = elems.listIterator(); i.hasNext(); ) {
      JComponent c = (JComponent) i.next();
      if (c instanceof Meta && c.getY() <= this.getHeight()) {
        ((Meta) c).activate();
        i.remove();
      } else if (c.getY() + c.getHeight() < oldstep) {
        this.remove(c);
        i.remove();
      } else
        c.setLocation(c.getX(), c.getY() - oldstep);
      bottom = Math.max(bottom, c.getY() + c.getHeight());
    }
    currenty -= oldstep;
//    this.setEnabled(true);
    if (bottom - tolerance < this.getHeight())
      needData();
  }

  public jpScroller speed(int delay, int step) {
    Meta md = new Meta(Meta.SPEED, delay);
    Meta ms = new Meta(Meta.STEP, step);
    md.setLocation(0, currenty);
    ms.setLocation(0, currenty);
    elems.add(md);
    elems.add(ms);
    return this;
  }

  public jpScroller pause(int delay) {
    Meta mp = new Meta(Meta.PAUSE, delay);
    mp.setLocation(0, currenty);
    elems.add(mp);
    return this;
  }

  public void start() {
    ticker.start();
  }

  public void stop() {
    ticker.stop();
  }

  public abstract void needData();

  private class Meta extends JComponent {
    public static final int SPEED = 0;
    public static final int STEP = 1;
    public static final int PAUSE = 2;

    private int type, arg;
    public Meta(int type, int arg) {
      this.type = type;
      this.arg = arg;
    }
    public void activate() {
      if (type == SPEED)
        ticker.setDelay(arg);
      else if (type == STEP)
        step = arg;
      else if (type == PAUSE)
        pause = arg;
    }
  }
}

