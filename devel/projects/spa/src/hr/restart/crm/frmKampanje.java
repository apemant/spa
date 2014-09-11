/****license*****************************************************************
**   file: frmKampanje.java
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
package hr.restart.crm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Kampanje;
import hr.restart.baza.Klijenti;
import hr.restart.baza.Kontakti;
import hr.restart.baza.dM;
import hr.restart.help.MsgDispatcher;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raTransaction;


public class frmKampanje extends raMasterDetail {
  
  public static final String OPEN = "K";
  public static final String CLOSED = "Z";
  public static final String DELAYED = "U";
  public static final String PENDING = "A";
  
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  jpKampanjeMaster jpMaster;
  jpKampanjeDetail jpDetail;
  
  JLabel status = new JLabel();
  JraButton jbObradi = new JraButton();
  JraButton jbPonisti = new JraButton();
  JraButton jbOdlozi = new JraButton();
  JraComboBox jcb = new JraComboBox();
  JraTextField jtf = new JraTextField();
  JraCheckBox alarm = new JraCheckBox();
  
  Color colY = Color.yellow.darker().darker();
  Color colF = Color.green.darker().darker();
  Color colU = Color.red.darker().darker();

  
  public frmKampanje() {
    super(1, 2);
    
    setMasterSet(Kampanje.getDataModule().getQueryDataSet());
    setDetailSet(Kontakti.getDataModule().getFilteredDataSet(Condition.nil));
    setMasterKey(new String[] {"UID"});
    setDetailKey(new String[] {"KAMPANJA"});
    jpMaster = new jpKampanjeMaster(true);
    jpMaster.BindComponents(getMasterSet());
    setJPanelMaster(jpMaster);
    setVisibleColsMaster(new int[] {0, 1, 4, 5});
    raMaster.setTitle("Kampanje");
    
    
    jpDetail = new jpKampanjeDetail();
    jpDetail.BindComponents(getDetailSet());
    
    jcb.addItem(new VariableNum("dan", "dana", "dana"));
    jcb.addItem(new VariableNum("sat", "sata", "sati"));
    jcb.setPreferredSize(new Dimension(100,21));
    jcb.setEditable(false);
    
    
    jtf.setHorizontalAlignment(SwingConstants.TRAILING);
    new raTextMask(jtf, 3, false, raTextMask.DIGITS);
    
    jtf.getDocument().addDocumentListener(new DocumentListener() {
      public void removeUpdate(DocumentEvent e) {
        jcb.repaint();
      }
      public void insertUpdate(DocumentEvent e) {
        jcb.repaint();
      }
      public void changedUpdate(DocumentEvent e) {
        jcb.repaint();
      }
    });
    
    status.setFont(status.getFont().deriveFont(Font.ITALIC, status.getFont().getSize2D() * 1.2f));
    
    jbObradi.setText("Obradi");
    jbObradi.setIcon(raImages.getImageIcon(raImages.IMGPROPERTIES));
    jbObradi.setPreferredSize(new Dimension(100, 21));
    
    jbPonisti.setText("Poništi");
    jbPonisti.setIcon(raImages.getImageIcon(raImages.IMGX));
    jbPonisti.setPreferredSize(new Dimension(100, 21));
    
    jbOdlozi.setText("Odgodi");
    jbOdlozi.setIcon(raImages.getImageIcon(raImages.IMGSENDMAIL));
    jbOdlozi.setPreferredSize(new Dimension(205, 21));
    
    jbObradi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        obradi();
      }
    });
    jbPonisti.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ponisti();
      }
    });
    jbOdlozi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        odlozi();
      }
    });
    
    alarm.setText(" Alarm ");
    alarm.setHorizontalAlignment(SwingConstants.TRAILING);
    alarm.setHorizontalTextPosition(SwingConstants.LEADING);
    
    JPanel outer = new JPanel(new BorderLayout());
    JPanel control = new JPanel(new XYLayout(570, 80));
    control.setBorder(BorderFactory.createRaisedBevelBorder());
    control.add(new JLabel("Status"), new XYConstraints(15, 15, -1, -1));
    control.add(status, new XYConstraints(150, 15, -1, -1));
    control.add(jbObradi, new XYConstraints(345, 15, 100, -1));
    control.add(jbPonisti, new XYConstraints(450, 15, 100, -1));
    control.add(new JLabel("Odgoditi za"), new XYConstraints(15, 45, -1, -1));
    control.add(jtf, new XYConstraints(150, 45, 75, -1));
    control.add(jcb, new XYConstraints(230, 45, 100, -1));
    control.add(jbOdlozi, new XYConstraints(345, 45, 100, -1));
    control.add(alarm, new XYConstraints(450, 45, 100, -1));
    outer.add(control, BorderLayout.NORTH);
    outer.add(raDetail.jpDetailView);
    setJPanelDetail(jpDetail);
    raDetail.jSplitPaneMP.setBottomComponent(outer);
    
    setVisibleColsDetail(new int[] {0, 3});
    raDetail.getJpTableView().addTableModifier(new raTableColumnModifier("CKLIJENT", 
        new String[] {"NAZIV"}, Klijenti.getDataModule().getQueryDataSet()));
    raDetail.setTitle("Kampanja");
    raDetail.getJpTableView().getMpTable().setPreferredScrollableViewportSize(new Dimension(450, 250));
    
    raMaster.setSort(new String[] {"DATPOC"});
    raDetail.setSort(new String[] {"STATUS", "DATUM"});
    
    getDetailSet().getColumn("DATUM").setDisplayMask("dd-MM-yyyy  'u' HH:mm");
    
    raMaster.removeRnvCopyCurr();
    raDetail.removeRnvCopyCurr();
    
    raDetail.getJpTableView().addTableModifier(new raTableModifier() {
      Variant v = new Variant();
      public void modify() {
        if (getTable() instanceof JraTable2) {
          ((JraTable2)getTable()).getDataSet().getVariant("STATUS", getRow(), v);
          String stat = v.getString();
          if (stat.equals(OPEN)) {
            renderComponent.setBackground(isSelected() ? getTable().getSelectionBackground() : getTable().getBackground());
            renderComponent.setForeground(isSelected() ? getTable().getSelectionForeground() : getTable().getForeground());
          } else if (stat.equals(CLOSED)) {
            renderComponent.setBackground(isSelected() ? colF : getTable().getBackground());
            renderComponent.setForeground(isSelected() ? getTable().getSelectionForeground() : colF);
          } else if (stat.equals(DELAYED)) {
            renderComponent.setBackground(isSelected() ? colU : getTable().getBackground());
            renderComponent.setForeground(isSelected() ? getTable().getSelectionForeground() : colU);
          } else {
            renderComponent.setBackground(isSelected() ? colY : getTable().getBackground());
            renderComponent.setForeground(isSelected() ? getTable().getSelectionForeground() : colY);
          }
        }
      }
      public boolean doModify() {
        return true;
      }
    });
    
    jtf.setText(frmParam.getParam("crm", "danaOdgode", "7", "Default broj dana za odgodu na kampanjama", true));
    jcb.setSelectedIndex(0);
    alarm.setSelected(frmParam.getParam("crm", "alarm", "D", "Je li alarm na kampanjama po defaultu ukljuèen (D,N)", true).equalsIgnoreCase("D"));
  }
  
  public void SetFokusMaster(char mode) {
    jpMaster.showEditPanel(mode == 'I');
    if (mode == 'N') {
      jpMaster.jpc.setCorg(getPreSelect().getSelRow().getString("CORG"));
      jpMaster.jlrAgent.setText(raUser.getInstance().getUser());
      jpMaster.jlrAgent.forceFocLost();
      if (jpMaster.rcbKanal.getSelectedIndex() < 0)
        jpMaster.rcbKanal.setSelectedIndex(0);
      jpMaster.rcbKanal.this_itemStateChanged();
      getMasterSet().setTimestamp("DATPOC", Valid.getValid().getPresToday(getPreSelect().getSelRow(), "DATPOC"));
      jpMaster.jraNaslov.requestFocus();
    } else if (mode == 'I')
      jpMaster.opis.requestFocus();
  }
  
  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') {
      getMasterSet().setInt("UID", Valid.getValid().findSeqInt("CRM-kampanje"));
    }
    return true;
  }
  
  public boolean doWithSaveMaster(char mode) {
    if (mode == 'I' && jpMaster.jbUpdate.isSelected()) {
      QueryDataSet ds = Kontakti.getDataModule().getTempSet(Condition.equal("KAMPANJA", getMasterSet(), "UID"));
      ds.open();
      boolean all = jpMaster.jcb.getSelectedIndex() == 1;
      for (ds.first(); ds.inBounds(); ds.next()) 
        if (all || ds.getString("STATUS").equals(OPEN)) {
          if (jpMaster.jbNaslov.isSelected()) 
            ds.setString("NASLOV", getMasterSet().getString("NASLOV"));
          if (jpMaster.jbAgent.isSelected()) 
            ds.setString("CUSER", getMasterSet().getString("CUSER"));
          if (jpMaster.jbOpis.isSelected() && ds.getString("OPIS").length() == 0)
            ds.setString("OPIS", getMasterSet().getString("OPIS"));
        }
      raTransaction.saveChanges(ds);
      dM.getSynchronizer().markAsDirty("kontakti");
    }
    return true;
  }
  
  public void beforeShowDetail() {
    super.beforeShowDetail();
    fixButtons();
  }
    
  void fixButtons() {
    boolean empty = getDetailSet().rowCount() == 0;
    boolean obr = getDetailSet().getString("STATUS").equals(CLOSED);
    boolean act = getDetailSet().getString("STATUS").equals(OPEN);
    rcc.setLabelLaF(jtf, !empty && !obr);
    rcc.setLabelLaF(jcb, !empty && !obr);
    rcc.setLabelLaF(jbObradi, !empty && !obr);
    rcc.setLabelLaF(jbPonisti, !empty && !act);
    rcc.setLabelLaF(jbOdlozi, !empty && !obr);
    rcc.setLabelLaF(alarm, !empty && !obr);
    
    if (getDetailSet().rowCount() == 0) status.setText("");
    else if (getDetailSet().getString("STATUS").equals(CLOSED)) {
      status.setText("Obraðen");
      status.setForeground(colF);
    } else if (getDetailSet().getString("STATUS").equals(OPEN)) {
      status.setText("Neobraðen");
      status.setForeground(Color.black);
    } else if (getDetailSet().getString("STATUS").equals(DELAYED)) {
      status.setText("Odložen");
      status.setForeground(colU);
    } else if (getDetailSet().getString("STATUS").equals(PENDING)) {
      status.setText("Zaostatak");
      status.setForeground(colY);
    }
  }
  
  public void detailSet_navigated(NavigationEvent e) {
    fixButtons();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jpDetail.jlrCklijent.forceFocLost();
      }
    });    
  }
  
  public boolean doBeforeSaveDetail(char mode) {
    if (mode == 'N') {
      getDetailSet().setInt("UID", Valid.getValid().findSeqInt("CRM-kontakti"));
    }
    return true;
  }
  
  
  void obradi() {
    if (getDetailSet().getRowCount() == 0) return;
    if (getDetailSet().getString("STATUS").equals(CLOSED)) return;
    
    setTime = new Timestamp(System.currentTimeMillis());
    setStatus = CLOSED;
    nextUid = findNextUid();
    System.out.println("next uid " + nextUid);
    if (raDetail.getMode() == 'N') {
      autoEdit = false;
      nextUid = -1;
      raDetail.jBOK_action();
    } else if (raDetail.getMode() == 'I') {      
      autoEdit = true;
      raDetail.jBOK_action();
    } else {
      autoEdit = false;
      delayedSetStatusAndTime();
    }
  }
  
  void ponisti() {
    if (getDetailSet().getRowCount() == 0) return;
    if (getDetailSet().getString("STATUS").equals(OPEN)) return;
    
    setTime = new Timestamp(System.currentTimeMillis());
    setStatus = OPEN;
    nextUid = -1;
    autoEdit = false;
    if (raDetail.getMode() != 'B') {
      raDetail.jBOK_action();
    } else {
      delayedSetStatusAndTime();
    }
  }
  
  void odlozi() {
    if (getDetailSet().getRowCount() == 0) return;
    if (getDetailSet().getString("STATUS").equals(CLOSED)) return;
    if (Aus.getAnyNumber(jtf.getText()) == 0) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Nije upisan broj " + (jcb.getSelectedIndex() == 0 ? "dana" : "sati") + " za odgodu!",
          "Odgaðanje kontakta", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    Timestamp now = new Timestamp(System.currentTimeMillis());
    if (jcb.getSelectedIndex() == 0) {
      now = Util.getUtil().addDays(now, Aus.getAnyNumber(jtf.getText()));
      now = Util.getUtil().getFirstSecondOfDay(now);
      now = Util.getUtil().addHours(now, 6);
    } else 
      now = Util.getUtil().addMinutes(now, Aus.getAnyNumber(jtf.getText()));
    
    setTime = now;
    setStatus = DELAYED;
    nextUid = findNextUid();
    if (raDetail.getMode() == 'N') {
      autoEdit = false;
      nextUid = -1;
      raDetail.jBOK_action();
    } else if (raDetail.getMode() == 'I') {      
      autoEdit = true;
      raDetail.jBOK_action();
    } else {
      autoEdit = false;
      delayedSetStatusAndTime();
    }
  }
  
  Timestamp setTime = null;
  String setStatus = null;
  int nextUid = -1;
  boolean autoEdit = false;
  public void AfterSaveDetail(char mode) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        delayedSetStatusAndTime();
      }
    });
  }
  
  void delayedSetStatusAndTime() {
    boolean updateMaster = false; 
    if (setStatus != null) {
      updateMaster = getDetailSet().getString("STATUS").equals(DELAYED) || setStatus.equals(DELAYED);
      if (getDetailSet().getString("STATUS").equals(DELAYED)) removeOldMessage();
      if (setStatus.equals(DELAYED) && alarm.isSelected()) addMessage();
      getDetailSet().setString("STATUS", setStatus);
      if (setTime != null) getDetailSet().setTimestamp("DATUM", setTime);
      getDetailSet().saveChanges();
      raDetail.getJpTableView().fireTableDataChanged();
      setStatus = null;
      setTime = null;
    }
    if (nextUid >= 0) {
      lookupData.getlookupData().raLocate(getDetailSet(), "UID", Integer.toString(nextUid));
      if (getDetailSet().getString("STATUS").equals(CLOSED)) autoEdit = false;
      nextUid = -1;
      if (autoEdit)
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            raDetail.rnvUpdate_action();
          }
        });
    }
    autoEdit = false;
    if (updateMaster) findNextAlarm();
  }
  
  String getCall() {
    return "hr.restart.robno.Util.getUtil().showKampanja(" +
        getMasterSet().getInt("UID") + "," + getDetailSet().getInt("UID") + ")";
  }
  
  void removeOldMessage() {
    Valid.getValid().runSQL("DELETE FROM Mesg WHERE mtext LIKE '%" + getCall() + "%'"); 
  }
  
  void addMessage() {
    String msg = "<html>Kampanja " + getMasterSet().getInt("UID") + " - " + getMasterSet().getString("NASLOV") + ", " +
              "pokrenuta " + Aus.formatTimestamp(getMasterSet().getTimestamp("DATPOC")) + "<p>" +
        "Klijent " + jpDetail.jlrKlijent.getText() + " je bio odložen za " + jtf.getText() + " " + jcb.getSelectedItem() +
        " (" + Aus.formatTimestamp(setTime) + "  u " + setTime.toString().substring(11, 16) + ")<p>" +
        
        Aus.createLink("Kliknuti ovdje za pregled.", getCall()) + "</html>";
    
    MsgDispatcher.sendOut("crm", raUser.getInstance().getUser(), setTime, msg);
  }
  
  void findNextAlarm() {
    QueryDataSet next = Aus.q("SELECT MIN(datum) as datum FROM kontakti WHERE " + Condition.equal("KAMPANJA", getMasterSet(), "UID")
        .and(Condition.equal("STATUS", DELAYED)));
    if (next.isNull("DATUM")) getMasterSet().setAssignedNull("DATNEXT");
    else getMasterSet().setTimestamp("DATNEXT", next.getTimestamp("DATUM"));
    getMasterSet().saveChanges();
  }
  
  void updatePending() {
    String q = "UPDATE kontakti SET status='" + PENDING + "' WHERE "
        + Condition.equal("KAMPANJA", getMasterSet(), "UID")
        .and(Condition.equal("STATUS", DELAYED)).and(Condition.till("DATUM", 
            new Timestamp(System.currentTimeMillis())));
    System.out.println(q);
    Valid.getValid().runSQL(q);
    dM.getSynchronizer().markAsDirty("kontakti");
    findNextAlarm();
  }
  
  public void refilterDetailSet() {
    Timestamp next = getMasterSet().getTimestamp("DATNEXT");
    if (next.before(new Timestamp(System.currentTimeMillis()))) updatePending();
    super.refilterDetailSet();
  }
  
  int findNextUid() {
    if (getDetailSet().getRow() + 1 >= getDetailSet().rowCount()) return -1;
    
    Variant v = new Variant();
    getDetailSet().getVariant("UID", getDetailSet().getRow() + 1, v);
    return v.getInt();
  }
  
  public void SetFokusDetail(char mode) {
    System.out.println("set fokus");
    System.out.println(getDetailSet());
    autoEdit = false;
    setStatus = null;
    nextUid = -1;
    if (mode == 'N') {
      String[] cols = {"NASLOV", "CKANAL", "CUSER", "OPIS"};
      dM.copyColumns(getMasterSet(), getDetailSet(), cols);
      jpDetail.jlrAgent.forceFocLost();
      jpDetail.jlrCklijent.forceFocLost();
      getDetailSet().setTimestamp("DATUM", new Timestamp(System.currentTimeMillis()));
      getDetailSet().setInt("KAMPANJA", getMasterSet().getInt("UID"));
      getDetailSet().setString("STATUS", OPEN);
      jpDetail.jlrKlijent.requestFocus();
    } else if (mode == 'I')
      jpDetail.opis.requestFocus();
  }
  
  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jlrKlijent) || vl.isEmpty(jpDetail.jraNaslov))
      return false;
    
    return true;
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jlrAgent) || vl.isEmpty(jpMaster.jraNaslov))
      return false;
    
    return true;
  }
  
  public void afterSetModeMaster(char oldMod, char newMod) {
    if (newMod == 'B') jpMaster.showEditPanel(false);
  }
  
  class VariableNum {
    String num1, num2, num5;
    
    public VariableNum(String t1, String t2, String t5) {
      num1 = t1;
      num2 = t2;
      num5 = t5;
    }
    
    public String toString() {
      if (jtf.isEmpty()) return num5;
      int num = Aus.getAnyNumber(jtf.getText());
      return Aus.getNumDep(num, num1, num2, num5);
    }
  }
}
