package hr.restart.distrib;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import com.borland.jbcl.layout.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;
import hr.restart.swing.*;
import hr.restart.baza.*;
import hr.restart.util.*;


public class presDistkal extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCgrart = new JLabel();
  JraButton jbSelCgrart = new JraButton();
  JlrNavField jlrNazgrart = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCgrart = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public presDistkal() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    jlrCgrart.requestFocus();
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(Distkal.getDataModule().getQueryDataSet());
    jpDetail.setLayout(lay);
    lay.setWidth(546);
    lay.setHeight(60);

    jbSelCgrart.setText("...");
    jlCgrart.setText("Grupa");

    jlrCgrart.setColumnName("CGRART");
    jlrCgrart.setDataSet(getSelDataSet());
    jlrCgrart.setColNames(new String[] {"NAZGRART"});
    jlrCgrart.setTextFields(new JTextComponent[] {jlrNazgrart});
    jlrCgrart.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCgrart.setSearchMode(0);
    jlrCgrart.setRaDataSet(dm.getGrupart());
    jlrCgrart.setNavButton(jbSelCgrart);

    jlrNazgrart.setColumnName("NAZGRART");
    jlrNazgrart.setNavProperties(jlrCgrart);
    jlrNazgrart.setSearchMode(1);

    jpDetail.add(jbSelCgrart, new XYConstraints(510, 20, 21, 21));
    jpDetail.add(jlCgrart, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCgrart, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlrNazgrart, new XYConstraints(230, 20, 275, -1));

    this.setSelPanel(jpDetail);
  }
}
