/****license*****************************************************************
**   file: frmMnemonics.java
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
package hr.restart.pl;

import hr.restart.swing.JraFrame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/** Zamijenjeno sa hr.restart.util.raMnemonics
 * @deprecated zamijenjeno sa hr.restart.util.raMnemonics
 */
public class frmMnemonics extends JraFrame {
  private JPanel jp = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  /** Zamijenjeno sa hr.restart.util.raMnemonics
   * @deprecated zamijenjeno sa hr.restart.util.raMnemonics
   */  
  public frmMnemonics() {
    try {
      jbInit();
    }
    catch (Exception ex) {}
  }

  private void jbInit() throws Exception
  {
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-230;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-355;
    this.setLocation((int)x/2,(int)y/2);

//    this.setResizable(false);
    this.setTitle("Popis mnemonika");
//    this.setSize(230, 355);
    jp.setLayout(xYLayout1);
    jp.setBorder(BorderFactory.createEtchedBorder());
    frmVirmaniPl fvpl = (frmVirmaniPl)hr.restart.util.raLoader.load("hr.restart.pl.frmVirmaniPl");
//    fvpl.addVars();
    int ly = 15;
    for (java.util.Iterator i = fvpl.vars.iterator(); i.hasNext(); ) {
      raVirVar ravv = (raVirVar)i.next();
      JLabel jlvar = new JLabel(ravv.getVar());
      JLabel jlOpis = new JLabel(ravv.getOpis());
      System.out.println("adding "+ravv.getOpis());
      jp.add(jlOpis,   new XYConstraints(15, ly, -1, -1));
      jp.add(jlvar,   new XYConstraints(150, ly, -1, -1));
      ly = ly + 20;
    }
    xYLayout1.setWidth(230);
    xYLayout1.setHeight(ly+20);//325
    this.getContentPane().add(jp, BorderLayout.NORTH);
    pack();
  }
}