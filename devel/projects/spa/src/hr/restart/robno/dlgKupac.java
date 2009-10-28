/****license*****************************************************************
**   file: dlgKupac.java
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

import hr.restart.swing.JraDialog;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.raMatPodaci;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class dlgKupac extends JraDialog {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  //hr.restart.robno.raPanKupac rkp = new raPanKupac();
  jpVlasnik jpvlas;
  StorageDataSet resolvSet;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      pressOK();
    }
    public void jPrekid_actionPerformed() {
      pressCancel();
    }
  };
  
  public OKpanel getOKpanel() {
    return okp;
  }
  
  public dlgKupac(Dialog owner, StorageDataSet _resolvSet) {
    super(owner,true);
    resolvSet = _resolvSet;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public dlgKupac(Frame owner, StorageDataSet _resolvSet) {
    super(owner,true);
    resolvSet = _resolvSet;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public dlgKupac() {
    this((Frame)null, hr.restart.baza.dM.getDataModule().getPos());
  }
  private void jbInit() throws Exception {
    jpvlas = new jpVlasnik(resolvSet,-5,new Insets(5,0,5,0));
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_componentShown(e);
      }
    });
/*    
    JPanel jpBorder = new JPanel();
    jpBorder.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
    jpBorder.add(jpvlas, BorderLayout.CENTER);
 */
    JPanel jpOuter = new JPanel();
    jpOuter.setBorder(null);
    jpOuter.add(jpvlas,BorderLayout.CENTER);
    this.getContentPane().add(raMatPodaci.addScrolledAndCentered(jpOuter,null,true), BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    ((JlrNavField)jpvlas.jraCkupac).setFocusLostOnShow(false);
    okp.registerOKPanelKeys(this);
  }
  void pressOK() {
    if (!jpvlas.updateRecords())
      resolvSet.setUnassignedNull("CKUPAC");
    
    this.hide();
  }
  void pressCancel() {
    if (jpvlas.isUpdated()) {
      jpvlas.dummySet.clearValues();
      jpvlas.setUpdated(false);
    } else {
      this.hide();
    }
  }

  void this_componentShown(ComponentEvent e) {
    jpvlas.setFromSet(resolvSet);

    jpvlas.jraIme.requestFocusLater();
    //System.out.println("width mora biti 700 a sad je "+jpvlas.getWidth()+"X"+jpvlas.getHeight());
  }

public static void main(String[] args) {
  startFrame.raLookAndFeel();
  startFrame.getStartFrame().showFrame("hr.restart.robno.dlgKupac","Kupac");
}
}

// staro & nestandardno

//package hr.restart.robno;
//
//import javax.swing.*;
//import java.awt.*;
//import com.borland.jbcl.layout.*;
//import com.borland.dbswing.*;
//import hr.restart.util.*;
//import hr.restart.swing.*;
//import java.awt.event.*;
//
///**
// * <p>Title: Robno poslovanje</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2000</p>
// * <p>Company: REST-ART</p>
// * @author unascribed
// * @version 1.0
// */
//
//public class dlgKupac extends JraDialog {
//  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
//  hr.restart.robno.raPanKupac rkp = new raPanKupac();
//  OKpanel okp = new OKpanel() {
//    public void jBOK_actionPerformed() {
//      pressOK();
//    }
//    public void jPrekid_actionPerformed() {
//      pressCancel();
//    }
//  };
//
//  public dlgKupac() {
//    try {
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//  private void jbInit() throws Exception {
//    this.setModal(true);
//    this.addKeyListener(new java.awt.event.KeyAdapter() {
//      public void keyPressed(KeyEvent e) {
//        this_keyPressed(e);
//      }
//    });
//    this.addComponentListener(new java.awt.event.ComponentAdapter() {
//      public void componentShown(ComponentEvent e) {
//        this_componentShown(e);
//      }
//    });
//    this.getContentPane().add(rkp, BorderLayout.CENTER);
//    this.getContentPane().add(okp, BorderLayout.SOUTH);
//  }
//  void pressOK() {
//    int i = rkp.manipulateKupci();
//    if (i!=-1){
//      System.out.println("Kupac: "+i);
//      dm.getPos().setInt("CKUPAC", Integer.parseInt(rkp.jlrSifra.getText()));
//    }
//    this.hide();
//  }
//  void pressCancel() {
//    this.hide();
//  }
//
//  void this_componentShown(ComponentEvent e) {
//    rkp.jlrSifra.forceFocLost();
//  }
//
//  void this_keyPressed(KeyEvent e) {
//    if (e.getKeyCode()==e.VK_F10) {
//      okp.jBOK_actionPerformed();
//    }
//    else if (e.getKeyCode()==e.VK_ESCAPE) {
//      okp.jPrekid_actionPerformed();
//    }
//  }
//}
