/****license*****************************************************************
**   file: jpZupGrad.java
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

import hr.restart.baza.dM;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;

import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class jpZupGrad extends JPanel {
  private XYLayout lay = new XYLayout();
  private raComboBox rcb = new raComboBox() {
    /*public boolean isFocusTraversable() {
      return !skipme;
    }*/
    public void this_itemStateChanged() {
      if (getSelectedIndex() == 0) agentSelected();
      else if (getSelectedIndex() == 1) zupSelected();
      else if (getSelectedIndex() == 2) mjSelected();
      else grSelected();
    }
  };

  public JlrNavField zup = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      return !skipme;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JlrNavField zupn = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      if (skipme) return (skipme = false);
      return true;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };
  JraButton zbut = new JraButton();
  
  public JlrNavField gr = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      return !skipme;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JlrNavField grn = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      if (skipme) return (skipme = false);
      return true;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };
  JraButton gbut = new JraButton();
  
  public JlrNavField agent = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      return !skipme;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JlrNavField nagent = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      if (skipme) return (skipme = false);
      return true;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };
  JraButton abut = new JraButton();

  public JlrNavField pbr = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      return !skipme;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };

  public JlrNavField mj = new JlrNavField() {
    /*public boolean isFocusTraversable() {
      if (skipme) return (skipme = false);
      return true;
    }*/
    public void after_lookUp() {
      afterLookUp(isLastLookSuccessfull());
    }
  };
  JraButton mbut = new JraButton();

  private int zupmj;
  //protected boolean skipme = false;

  public jpZupGrad() {
    this(100, 350);
  }

  public jpZupGrad(int wnaz) {
    this(100, wnaz);
  }

  public jpZupGrad(int wz, int wnaz) {
    try {
      init(wz, wnaz);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(int wz, int wnaz) throws Exception {
    this.setLayout(lay);
    lay.setHeight(25);
    lay.setWidth(150+wz+5+wnaz+5+25);

    zup.setColumnName("CZUP");
    zup.setColNames(new String[] {"NAZIVZUP"});
    zup.setTextFields(new JTextComponent[] {zupn});
    zup.setVisCols(new int[] {0, 1});
    zup.setSearchMode(0);
    zup.setRaDataSet(dM.getDataModule().getZupanije());
    zup.setNavButton(zbut);

    zupn.setColumnName("NAZIVZUP");
    zupn.setNavProperties(zup);
    zupn.setSearchMode(1);
    
    gr.setColumnName("CGRPAR");
    gr.setNavColumnName("CGRPAR");
    gr.setColNames(new String[] {"NAZIV"});
    gr.setTextFields(new JTextComponent[] {grn});
    gr.setVisCols(new int[] {0, 1});
    gr.setSearchMode(0);
    gr.setRaDataSet(dM.getDataModule().getGruppart());
    gr.setNavButton(gbut);

    grn.setColumnName("NAZIV");
    grn.setNavProperties(gr);
    grn.setSearchMode(1);
    
    agent.setColumnName("CAGENT");
    agent.setColNames(new String[] {"NAZAGENT"});
    agent.setTextFields(new JTextComponent[] {nagent});
    agent.setVisCols(new int[] {0, 1});
    agent.setSearchMode(0);
    agent.setRaDataSet(dM.getDataModule().getAgenti());
    agent.setNavButton(abut);

    nagent.setColumnName("NAZAGENT");
    nagent.setNavProperties(agent);
    nagent.setSearchMode(1);

    pbr.setColumnName("PBR");
    pbr.setColNames(new String[] {"NAZMJESTA"});
    pbr.setTextFields(new JTextComponent[] {mj});
    pbr.setVisCols(new int[] {2, 1});
    pbr.setSearchMode(3);
    pbr.setRaDataSet(dM.getDataModule().getMjesta());
    pbr.setNavButton(mbut);

    mj.setColumnName("NAZMJESTA");
    mj.setNavProperties(pbr);
    mj.setSearchMode(1);

    rcb.setRaItems(new String[][] {
      {"Agent", "A"},
      {"Županija", "Z"},
      {"Grad", "G"},
      {"Grupa", "P"}
    });
    rcb.setSelectedIndex(0);
    zupSelected();

    this.add(rcb, new XYConstraints(15, 0, 130, -1));
    this.add(zup, new XYConstraints(150, 0, wz, -1));
    this.add(zupn, new XYConstraints(155 + wz, 0, wnaz, -1));
    this.add(zbut, new XYConstraints(160 + wz + wnaz, 0, 21, 21));
    this.add(gr, new XYConstraints(150, 0, wz, -1));
    this.add(grn, new XYConstraints(155 + wz, 0, wnaz, -1));
    this.add(gbut, new XYConstraints(160 + wz + wnaz, 0, 21, 21));
    this.add(agent, new XYConstraints(150, 0, wz, -1));
    this.add(nagent, new XYConstraints(155 + wz, 0, wnaz, -1));
    this.add(abut, new XYConstraints(160 + wz + wnaz, 0, 21, 21));
    this.add(pbr, new XYConstraints(150, 0, wz, -1));
    this.add(mj, new XYConstraints(155 + wz, 0, wnaz, -1));
    this.add(mbut, new XYConstraints(160 + wz + wnaz, 0, 21, 21));
  }

/*  public void setSkip() {
    skipme = true;
  }
*/
  public void bind(DataSet ds) {
    zup.setDataSet(ds);
    pbr.setDataSet(ds);
    agent.setDataSet(ds);
    gr.setDataSet(ds);
  }

  public boolean Validacija() {
    return (zupmj == 0 && !Valid.getValid().isEmpty(agent)) ||
           (zupmj == 1 && !Valid.getValid().isEmpty(zup)) ||
           (zupmj == 2 && !Valid.getValid().isEmpty(pbr)) ||
           (zupmj == 3 && !Valid.getValid().isEmpty(gr));
  }

  public void init() {
    zup.setText("");
    zup.forceFocLost();
    pbr.setText("");
    pbr.forceFocLost();
    agent.setText("");
    agent.forceFocLost();
    gr.setText("");
    gr.forceFocLost();
    if (rcb.getSelectedIndex() == 0) agentSelected();
    else if (rcb.getSelectedIndex() == 1) zupSelected();
    else if (rcb.getSelectedIndex() == 2) mjSelected();
    else grSelected();
  }
  
  public boolean isAgent() {
    return zupmj == 0;
  }

  public boolean isZup() {
    return zupmj == 1;
  }

  public boolean isGrad() {
    return zupmj == 2;
  }
  
  public boolean isGrupa() {
    return zupmj == 3;
  }
  
  public String getAgent() {
    return isAgent() && agent.getText() != null && agent.getText().length() > 0 ? 
        nagent.getText() : null;
  }

  public String getGrad() {
    return isGrad() && pbr.getText() != null && pbr.getText().length() > 0 ? mj.getText() : null;
  }

  public String getZupanija() {
    return isZup() && zup.getText() != null && zup.getText().length() > 0 ? zupn.getText() : null;
  }
  
  public String getGrupa() {
    return isGrupa() && gr.getText() != null && gr.getText().length() > 0 ? grn.getText() : null;
  }

  public boolean checkPartner(ReadRow par) {
    return checkPartner(par.getInt("CAGENT"), par.getShort("CZUP"), par.getInt("PBR"), par.getString("CGRPAR")); 
  }
  
  public boolean checkPartner(int pAgent, short pZup, int pPbr, String pGr) {
    return (zupmj == 0 && (agent.getText() == null || agent.getText().length() == 0 ||
            agent.getDataSet().getInt("CAGENT") == pAgent)) ||
            (zupmj == 1 && (zup.getText() == null || zup.getText().length() == 0 ||
            zup.getDataSet().getShort("CZUP") == pZup)) ||
            (zupmj == 2 && (pbr.getText() == null || pbr.getText().length() == 0 ||
            pbr.getDataSet().getInt("PBR") == pPbr)) ||
            (zupmj == 3 && (gr.getText() == null || gr.getText().length() == 0 ||
            gr.getDataSet().getString("CGRPAR").equals(pGr)));
  }

  public void focusCombo() {
    rcb.requestFocus();
  }

  public void focusNav() {
    if (zupmj == 0) agent.requestFocus(); 
    else if (zupmj == 1) zup.requestFocus();
    else if (zupmj == 2) pbr.requestFocus();
    else gr.requestFocus();
  }
  
  private void setZupEnabled(boolean enab) {
    zup.setEnabled(enab);
    zup.setVisible(enab);
    zupn.setEnabled(enab);
    zupn.setVisible(enab);
    zbut.setVisible(enab);
    zbut.setEnabled(enab);
  }
  
  private void setMjEnabled(boolean enab) {
    pbr.setEnabled(enab);
    pbr.setVisible(enab);
    mj.setEnabled(enab);
    mj.setVisible(enab);
    mbut.setVisible(enab);
    mbut.setEnabled(enab);    
  }
  
  private void setAgentEnabled(boolean enab) {
    agent.setEnabled(enab);
    agent.setVisible(enab);
    nagent.setEnabled(enab);
    nagent.setVisible(enab);
    abut.setVisible(enab);
    abut.setEnabled(enab);    
  }
  
  private void setGrupaEnabled(boolean enab) {
    gr.setEnabled(enab);
    gr.setVisible(enab);
    grn.setEnabled(enab);
    grn.setVisible(enab);
    gbut.setVisible(enab);
    gbut.setEnabled(enab);    
  }

  private void agentSelected() {
    zupmj = 0;
    setAgentEnabled(true);
    setZupEnabled(false);
    setMjEnabled(false);
    setGrupaEnabled(false);
  }

  private void zupSelected() {
    zupmj = 1;
    setAgentEnabled(false);
    setZupEnabled(true);
    setMjEnabled(false);
    setGrupaEnabled(false);
  }

  private void mjSelected() {
    zupmj = 2;
    setAgentEnabled(false);
    setZupEnabled(false);
    setMjEnabled(true);
    setGrupaEnabled(false);
  }
  
  private void grSelected() {
    zupmj = 3;
    setAgentEnabled(false);
    setZupEnabled(false);
    setMjEnabled(false);
    setGrupaEnabled(true);
  }

  public void afterLookUp(boolean succ) {}
}
