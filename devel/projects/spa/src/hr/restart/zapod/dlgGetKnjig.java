/****license*****************************************************************
**   file: dlgGetKnjig.java
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



import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class dlgGetKnjig extends JraDialog {
  
  static lookupData ld = lookupData.getlookupData();

  private static String KNJCORG = null;

  private static dlgGetKnjig dlggetknjig;

  private static JFrame ownerFrame;

  OKpanel okp = new OKpanel() {

    public void jBOK_actionPerformed() {

      okPressed();

    }

    public void jPrekid_actionPerformed() {

      cancelPressed();

    }

  };

  JPanel panel = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  JraButton jbGetOJ = new JraButton();

  JlrNavField jtCORG = new JlrNavField();

  JlrNavField jlNAZORG = new JlrNavField();

  JLabel jlCORG = new JLabel();



  protected dlgGetKnjig(Frame frame, String title, boolean modal) {

    super(frame, title, modal);

    try {

      jbInit();

      pack();

    }

    catch(Exception ex) {

      ex.printStackTrace();

    }

  }



  void jbInit() throws Exception {

    panel.setLayout(xYLayout1);

    jtCORG.setColumnName("CORG");

    jtCORG.setDataSet(null);

//    jtCORG.setRaDataSet(OrgStr.getOrgStr().getKnjigovodstva());

    jtCORG.setVisCols(new int[] {0,1});

    jtCORG.setSearchMode(0);

    jtCORG.setColNames(new String[] {"NAZIV"});

    jtCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlNAZORG});

    jtCORG.setNavButton(jbGetOJ);

    jlNAZORG.setColumnName("NAZIV");

    jlNAZORG.setSearchMode(1);

    jlNAZORG.setNavProperties(jtCORG);

    jlCORG.setText("Knjigovodstvo");

    xYLayout1.setWidth(550);

    xYLayout1.setHeight(60);

    panel.add(jlCORG,   new XYConstraints(15, 20, -1, -1));

    panel.add(jtCORG,  new XYConstraints(150, 20, 100, -1));

    panel.add(jlNAZORG,  new XYConstraints(255, 20, 260, -1));

    panel.add(jbGetOJ,  new XYConstraints(520, 20, 21, 21));

    this.getContentPane().add(panel,  BorderLayout.CENTER);

    this.getContentPane().add(okp, BorderLayout.SOUTH);
    
    okp.registerOKPanelKeys(this);

    addComponentListener(new ComponentAdapter() {

      public void componentShown(ComponentEvent e) {

        cshow();

      }

    });



  }



  void cshow() {
    jtCORG.setRaDataSet(OrgStr.getOrgStr().getKnjigovodstva());
    if (isOK_KNJCORG()) {

      jtCORG.setText(KNJCORG);

    } else {

      jtCORG.setText("");

    }

    jtCORG.forceFocLost();

    jtCORG.requestFocus();

  }



  public static void showDlgGetKnjig() {
    if (hr.restart.dlgExit.isFancySwitch()) {
      hr.restart.dlgExit.switchKnjig();
    } else {
      loadDlgGetKnjig();
      hr.restart.util.startFrame.getStartFrame().centerFrame(dlggetknjig,0,"Odabir knjigovodstva za rad");
      dlggetknjig.show();
    }
  }



  private static boolean isOK_KNJCORG() {

    if (KNJCORG!=null) {

      if (KNJCORG.length() > 0) {

        return true;

      }

    }

    return false;

  }



  public static void loadDlgGetKnjig() {

      if (dlggetknjig==null || ownerFrame == null) {

        if (dlggetknjig!=null) {
          if (dlggetknjig.isShowing()) return; //ako je prikazan ne chachkaj metchku
          dlggetknjig.dispose();
        }
        if (hr.restart.start.isMainFrame()) {

          ownerFrame = hr.restart.mainFrame.getMainFrame();

        } else {

          ownerFrame = hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame();

        }
        if (!ownerFrame.isVisible()) ownerFrame = null;
//        System.out.println("dlggetknjig.owner = "+ownerFrame);
        dlggetknjig = new dlgGetKnjig(ownerFrame,"Odabir knjigovodstva za rad",true);

      }

  }

  public static String getKNJCORG() {

    return getKNJCORG(true);

  }

  public static String getKNJCORG(boolean upit) {

    if (isOK_KNJCORG()) {

        return KNJCORG;

    }
    //ai: hack zbog korisnickih prava i repFuckingMemo
    boolean prvibezpitanja = !upit && OrgStr.getOrgStr().knjigovodstva == null;
    com.borland.dx.dataset.DataSet knjigs = OrgStr.getOrgStr().getKnjigovodstva();

    knjigs.open();

    if (knjigs.getRowCount() == 1) {

      KNJCORG = knjigs.getString("CORG");

      return KNJCORG;

    } else {

      if (upit) {

        showDlgGetKnjig();

      } else {

        knjigs.first();

        String k = knjigs.getString("CORG");
        if (prvibezpitanja) OrgStr.getOrgStr().knjigovodstva = null;
        return k;
      }

      return KNJCORG;

    }

  }

  public static String getTitleText(String titl) {

    int idxof = titl.indexOf(" # ")<0?titl.length():titl.indexOf(" # ");

    String ctitl = titl.substring(0,idxof);

    if (isOK_KNJCORG()) {

      ld.raLocate(OrgStr.getOrgStr().getKnjigovodstva(),new String[] {"CORG"}, new String[] {KNJCORG});

      String nazorg = OrgStr.getOrgStr().getKnjigovodstva().getString("NAZIV");

      return ctitl.concat(" # ").concat(KNJCORG).concat(" ").concat(nazorg);



    } else {

      return ctitl;

    }

  }

  public void okPressed() {

    jtCORG.forceFocLost();

    if (!hr.restart.util.Valid.getValid().isEmpty(jtCORG)) {

      changeKnjig(jtCORG.getText(),false);

//      ownerFrame.setTitle(getTitleText(ownerFrame.getTitle()));

      hide();

    }

  }



  public static void changeKnjig(String newCorg, boolean upit) {

    boolean foundCorg = lookupData.getlookupData().raLocate(OrgStr.getOrgStr().getKnjigovodstva(),new String[] {"CORG"},new String[] {newCorg});

    if (newCorg != null && foundCorg) {

      String oldKnjig = KNJCORG;

      KNJCORG = newCorg;

      OrgStr.getOrgStr().fireKnjigChanged(oldKnjig,KNJCORG);

    } else {

      getKNJCORG(upit);

    }

  }



  public void cancelPressed() {

    hide();

  }

}