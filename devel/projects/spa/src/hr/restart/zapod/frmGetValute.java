/****license*****************************************************************
**   file: frmGetValute.java
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
package hr.restart.zapod;



import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;


/**

 * <pre>

 * Poseban ekran za dohvat valute

 * POSTUPAK:

 * public class frmBljuf extends raFrame {

 *  JButton Bljuf = new JButton();

 *  hr.restart.zapod.frmGetValute fgv = hr.restart.zapod.frmGetValute.getFrmGetValute(Bljuf);

 *  .

 *  .

 *  .

 *  void jbInit() throws Exception {

 *    fgv.getJpGetValute().setTecajVisible(true);

 *    fgv.getJpGetValute().setDoGetTecaj(true);

 *    fgv.getJpGetValute().setRaDataSet(hr.restart.baza.dM.getDataModule().getDoki());

 *    .

 *    . //vidi hr.restart.zapod.jpGetValute

 *    .

 *  }

 *  void Bljuf_actionPerformed(ActionEvent e) {

 *    if (fgv.showGetValute()) {

 *  //pritisnut je OK

 *  //modalni je dialog  pa nakon toga se moze ubaciti kod iz jpGetValute.afterGet_Val()

 *    } else {

 *  //pritisnuo je prekid pa ako ima nesto...

 *    }

 *  }

 */



public class frmGetValute extends JraDialog {

  static frmGetValute fgetvalute;

  private javax.swing.JComponent startComp;

  private boolean closeFlag;

  jpGetValute jpGetVal = new jpGetValute();

  OKpanel okp = new OKpanel() {

    public void jBOK_actionPerformed() {

      okPressed();

    }

    public void jPrekid_actionPerformed() {

      cancelPressed();

    }

  };

  protected frmGetValute(Dialog owner) {

    super(owner,true);

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  protected frmGetValute(Frame owner) {

    super(owner,true);

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  void jbInit() throws Exception {

    this.setTitle("Dohvat valute");

    this.getContentPane().setLayout(new BorderLayout());

    this.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(KeyEvent e) {

        this_keyPressed(e);

      }

    });

    this.addComponentListener(new java.awt.event.ComponentAdapter() {

      public void componentShown(ComponentEvent e) {

        this_componentShown(e);

      }

    });

    this.getContentPane().add(jpGetVal, BorderLayout.CENTER);

    this.getContentPane().add(okp,BorderLayout.SOUTH);

  }

/**

 * Getter za jpGetValute tako da se mogu setirati parametri kao raDataSet i slicno

 * vidi jpGetValute dokumentaciju

 */

  public jpGetValute getJpGetValute() {

    return jpGetVal;

  }

/**

 * Staticki getter koji uvijek instancira novu klasu.

 * JComponent u parametru je npr. gumbic na ciji se klik pojavljuje ekran,

 * a sluzi zbog postavljanja ownera dialogu i postavljanje koordinata dialoga

 */

  public static frmGetValute getFrmGetValute(javax.swing.JComponent comp) {

    java.awt.Container topContainer = comp.getTopLevelAncestor();

    if (topContainer instanceof java.awt.Dialog) {

      fgetvalute = new frmGetValute((Dialog)topContainer);

    } else if (topContainer instanceof java.awt.Frame) {

      fgetvalute = new frmGetValute((Frame)topContainer);

    } else {

      fgetvalute = new frmGetValute((Frame)null);

    }

    fgetvalute.getJpGetValute().setBorderVisible(true);

    fgetvalute.startComp=comp;

    return fgetvalute;

  }

/**

 * Pozvati na actionPerformed na gumbic comp u ulozi getFrmGetValute(comp)

 */

  public boolean showGetValute() {

    pack();

    locateFrame();

    show();

    return closeFlag;

  };



  void locateFrame() {

    Point ploc = startComp.getLocationOnScreen();

    int xloc = ploc.x - getSize().width + startComp.getSize().width;

    int yloc = ploc.y - getSize().height + startComp.getSize().height;

    int maxy = hr.restart.start.getSCREENSIZE().height;

    xloc = (xloc<0)?0:xloc;

    yloc = (yloc+getSize().height>maxy)?maxy-getSize().height:yloc;

    setLocation(xloc,yloc);

  }



  void okPressed() {

    closeFlag = true;

    hide();

  }

/**

 * Uglavnom zatvara ekran

 */

  void cancelPressed() {

    closeFlag = false;

    hide();

  }



  void this_keyPressed(KeyEvent e) {

    if (e.getKeyCode()==e.VK_F10) {

      okPressed();

    } else if (e.getKeyCode()==e.VK_ESCAPE) {

      cancelPressed();

    }

  }



  void this_componentShown(ComponentEvent e) {

    jpGetVal.jtOZNVAL.forceFocLost();

    if (jpGetVal.isTecajVisible()&&jpGetVal.isTecajEditable()) {

      jpGetVal.jtTECAJ.requestFocus();

    } else {

      jpGetVal.jtOZNVAL.requestFocus();

    }

  }

}