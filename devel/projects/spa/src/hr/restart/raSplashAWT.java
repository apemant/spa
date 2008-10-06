/****license*****************************************************************
**   file: raSplashAWT.java
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
package hr.restart;

import hr.restart.util.raImages;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raSplashAWT extends Window {
  private static raSplashAWT spla;
  PopupMenu pmenu;
  java.awt.Image im;
  Label lClose = new Label("x");
  Color l_bc;// = new Color(230,230,230);
  Color l_fc;//new Color(192,192,192);// = new Color(61,61,61);
  public raSplashAWT() {
    super(new Frame("Rest Art running..."));
    try {
//      showPreSplash();
      initDialog();
      initImage();
      initLabel();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public void paint(Graphics g) {
    raSplashWorker.paint(g,im,message,this);
  }
  synchronized public static void showSplash() {
    if (hr.restart.start.checkArgs("nosplash")) return;
    if (spla == null) spla = new raSplashAWT();
    spla.setLocation(raSplashWorker.getCenter(spla));
    spla.show();
  }
  synchronized public static void hideSplash() {
    if (spla == null) return;
    spla.dispose();
    spla = null;
  }
  private String message = "Rest Art poslovne aplikacije ... prièekajte";
  synchronized public static void splashMSG(String messg) {
    if (spla == null) return;
    spla.message = messg;
    raSplashWorker.drawMess(spla.getGraphics(),spla.message);
    //spla.repaint();
  }
  int l_x;
  int l_y;
  Point l_s;
  private void initDialog() {
    setSize(raSplashWorker.spl_width,raSplashWorker.spl_height);
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        Point p = dragSplash(e.getX(),e.getY());
        splashMSG("Pomièem splash ("+l_s.x+","+l_s.y+") - ("+p.x+","+p.y+") ["+e.getX()+","+e.getY()+"]");
      }
    });
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isMetaDown()) {
          pmenu.show(raSplashAWT.this,e.getX(),e.getY());
        } else {
          l_x = e.getX();
          l_y = e.getY();
          l_s = raSplashAWT.this.getLocationOnScreen();
        }
      }
      public void mouseReleased(MouseEvent e) {
        if (e.isMetaDown()) return;
        setLocation(dragSplash(e.getX(),e.getY()));
        splashMSG("Modul: "+start.getRunArg());
      }
      
    });
    setLayout(null);
  }
  private Point dragSplash(int mx, int my) {
    int _x = l_x - mx;
    int _y = l_y - my;
    return new Point(l_s.x-_x,l_s.y-_y);
  }
  private String getPicResource() {
    return raImages.getPicResource(raImages.IMGSPLASH);
    //return "hr/restart/util/images/splash.jpg"; //da ne moram imati raImages u igri
  }
  private void initImage() throws Exception {
    im = raSplashWorker.loadSplashImage();
  }
  private void initLabel() throws Exception {
//    int lw = lClose.getFontMetrics(lClose.getFont()).charWidth('X')+2;
//    int lh = lClose.getFontMetrics(lClose.getFont()).getHeight()+2;
    int lw = 20;
    int lh = 20;
//    System.out.println("lw = "+lw+" lh = "+lh);
    pmenu = new PopupMenu("Opcije");
    MenuItem miStartAgain = new MenuItem("Pokreni ponovo");
    miStartAgain.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev0) {
        ArrayList parList = new ArrayList(Arrays.asList(hr.restart.start.runtimeArgs));
        parList.add("nosplash");
        String[] params = new String[parList.size()];
        params = (String[])parList.toArray(params);
        hr.restart.start.main(params);
      }
    });
    MenuItem miLog = new MenuItem("Log");
    miLog.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev1) {
        hr.restart.util.startFrame.showLog();
      }
    });
    MenuItem miExit = new MenuItem("Izlaz");
    miExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev2) {
        System.exit(0);
      }
    });
    pmenu.add(miStartAgain);
    pmenu.add(miLog);
    pmenu.addSeparator();
    pmenu.add(miExit);
    this.add(pmenu);
    //lClose.add(pmenu);
    if (l_bc == null) l_bc = raSplashWorker.backColor;
    if (l_fc == null) l_fc = raSplashWorker.textColor;
    //lClose.setBackground(l_bc);
    //lClose.setForeground(l_fc);
    //lClose.setBounds(spl_width-lw,0,lw,lh);
    /*
    lClose.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e0) {
        pmenu.show(lClose,e0.getX(),e0.getY());
        e0.consume();
      }
      public void mouseEntered(MouseEvent e1) {
        lClose.setFont(lClose.getFont().deriveFont(Font.BOLD));
        e1.consume();
      }
      public void mouseExited(MouseEvent e2) {
        lClose.setFont(lClose.getFont().deriveFont(Font.PLAIN));
        e2.consume();
      }
    });
    add(lClose);
     */
    
  }
  /*
  private void showPreSplash() {
    preSplash = new Window(this) {
      public void paint(Graphics g) {
        super.paint(g);
        g.drawString("Pokreæem aplikaciju",spl_margin,spl_margin);
      }
    };
    preSplash.setSize(200,50);
    preSplash.show();
  }
  private void hidePreSplash() {
    preSplash.dispose();
    preSplash = null;
  }
  */
}