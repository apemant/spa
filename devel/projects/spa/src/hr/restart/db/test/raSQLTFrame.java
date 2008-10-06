/****license*****************************************************************
**   file: raSQLTFrame.java
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
package hr.restart.db.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import hr.restart.db.*;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraScrollPane;

public class raSQLTFrame extends JraFrame {
  JTextField jt = new JTextField("select * from agenti");
  JButton jbDrive = new JButton("Get Fast");
  JButton jbDrive2 = new JButton("Get Client");
  JButton jbNext = new JButton("->");
  JButton jbPrev = new JButton("<-");
  JButton jbLast = new JButton(">|");
  JButton jbFirst = new JButton("|<");
  JButton jbDel = new JButton("X");
  JButton jbSave = new JButton("Save!");
  JButton jbPrnCurr = new JButton("Print current");
  JButton jbPrnAll = new JButton("Print all");
  JButton jbPrnAllAll = new JButton("Print hidden");
  JButton jbRowCount = new JButton("Count");
  JButton jbUpd = new JButton("Update");
  JButton jbInsert = new JButton("Insert");
  JButton jbDellAll = new JButton("Del all");
  JButton jbCancel = new JButton("Cancel");
  JButton jbProps = new JButton("Props");

  JTextArea jta = new JTextArea("");
  JPanel jbuts = new JPanel(new GridLayout(2,-1));
  public raSQLTFrame() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }
  private void jbInit() throws Exception {
//    setOut();
    hr.restart.start.startClient();
    JraScrollPane jscr = new JraScrollPane(jta);
    getContentPane().setLayout(new BorderLayout());
    jbFirst.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbFirst_actionPerformed(e);
      }
    });
    jbPrev.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbPrev_actionPerformed(e);
      }
    });
    jbNext.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbNext_actionPerformed(e);
      }
    });
    jbLast.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbLast_actionPerformed(e);
      }
    });
    jbDel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbDel_actionPerformed(e);
      }
    });
    jbSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSave_actionPerformed(e);
      }
    });
    jbPrnCurr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbPrnCurr_actionPerformed(e);
      }
    });
    jbPrnAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbPrnAll_actionPerformed(e);
      }
    });
    jbPrnAllAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbPrnAllAll_actionPerformed(e);
      }
    });
    jbRowCount.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbRowCount_actionPerformed(e);
      }
    });
    jbUpd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbUpd_actionPerformed(e);
      }
    });
    jbInsert.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbInsert_actionPerformed(e);
      }
    });
    jbDellAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbDellAll_actionPerformed(e);
      }
    });
    jbProps.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbProps_actionPerformed(e);
      }
    });
    jbuts.add(jbDrive);
    jbuts.add(jbDrive2);
    jbuts.add(jbFirst);
    jbuts.add(jbPrev);
    jbuts.add(jbNext);
    jbuts.add(jbLast);
    jbuts.add(jbDel);
    jbuts.add(jbRowCount);
    jbuts.add(jbSave);
    jbuts.add(jbPrnCurr);
    jbuts.add(jbPrnAll);
    jbuts.add(jbPrnAllAll);
    jbuts.add(jbPrnAllAll);
    jbuts.add(jbUpd);
    jbuts.add(jbInsert);
    jbuts.add(jbDellAll);
    jbuts.add(jbCancel);
    jbuts.add(jbProps);
    getContentPane().add(jt,BorderLayout.NORTH);
    getContentPane().add(jbuts,BorderLayout.SOUTH);
//    getContentPane().add(jscr,BorderLayout.CENTER);
    jbDrive.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drive(true);
      }
    });
    jbDrive2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        drive(false);
      }
    });
    pack();
    show();
  }
  raSQLSet set;
  void drive(boolean fast) {
    if (fast) {
      if (set instanceof raFastSQLSet) {
        System.out.println("reusing raFastSQLSet ...");
        set.close();
      } else set = new raFastSQLSet();
    } else {
      if (set instanceof raClientSQLSet) {
        System.out.println("reusing raClientSQLSet ...");
        set.close();
      } else set = new raClientSQLSet();
    }
    set.setQuery(jt.getText());
    long start = System.currentTimeMillis();
    System.out.println("Start: "+new java.sql.Timestamp(start));
    try {
      set.open();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
//        System.out.println(set);
    System.out.println("Stop after "+(start - System.currentTimeMillis())+" ms");
    System.out.println(set.getRowCount()+" rows retrieved...");
    System.out.println("Saving changes enabled = "+set.isSaveChangesEnabled());
  }

  void jbFirst_actionPerformed(ActionEvent e) {
    set.first();
  }

  void jbPrev_actionPerformed(ActionEvent e) {
    set.prev();
  }

  void jbNext_actionPerformed(ActionEvent e) {
    set.next();
  }

  void jbLast_actionPerformed(ActionEvent e) {
    set.last();
  }

  void jbDel_actionPerformed(ActionEvent e) {
    set.deleteRow();
  }

  void jbSave_actionPerformed(ActionEvent e) {
    try {
      long start = System.currentTimeMillis();
      System.out.println("Start save changes: "+new java.sql.Timestamp(start));
      set.saveChanges();
      System.out.println("Stop after "+(start - System.currentTimeMillis())+" ms");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbPrnCurr_actionPerformed(ActionEvent e) {
    System.out.println(set.printCurrentRow());
  }

  void jbPrnAll_actionPerformed(ActionEvent e) {
    System.out.println(set.toString());
  }

  void jbPrnAllAll_actionPerformed(ActionEvent e) {
    System.out.println(set.printAll());
  }
  void setOut() throws Exception {
    java.io.File f = new java.io.File("fsettest.txt");
    if (f.exists()) f.delete();
    f.createNewFile();
    System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(f)));
  }

  void jbRowCount_actionPerformed(ActionEvent e) {
    System.out.println("RowCount = "+set.getRowCount());
    System.out.println("cache = "+set.getFirstRowIndex()+" - "+set.getLastRowIndex());
  }
  boolean isMyTable() {
    if (set.getTableName().toUpperCase().trim().equals("AGENTI")) {
      return true;
    } else {
      JOptionPane.showMessageDialog(this,"Sorry samo s agentima se razgovaram");
      return false;
    }
  }
  void jbUpd_actionPerformed(ActionEvent e) {
    if (!isMyTable()) return;
    set.setValue("NAZAGENT","UPDATED BY ANDREJ");
  }

  void jbInsert_actionPerformed(ActionEvent e) {
    if (!isMyTable()) return;
    Integer cag = getCagent();
    System.out.println("cagent = "+cag);
    if (cag !=null) {
      set.insertRow();
      set.setValue("CAGENT",cag);
      set.setValue("NAZAGENT","ADDED BY ANDREJ");
    }
  }
  Integer getCagent() {
    JTextField jtc = new JTextField();
    if (JOptionPane.showOptionDialog(this,jtc,"Unesi cagent",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,null)
        == JOptionPane.OK_OPTION) {
      try {
        return new Integer(jtc.getText());
      }
      catch (Exception ex) {
        return null;
      }
    }
    return null;
  }

  void jbDellAll_actionPerformed(ActionEvent e) {
    set.deleteAll();
  }

  void jbProps_actionPerformed(ActionEvent e) {
    hr.restart.util.startFrame.getStartFrame().SystemPar();
  }

  public static void main(String[] args) {
    new raSQLTFrame();
  }
}