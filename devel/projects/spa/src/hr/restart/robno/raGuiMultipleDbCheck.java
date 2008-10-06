/****license*****************************************************************
**   file: raGuiMultipleDbCheck.java
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

import hr.restart.db.raPreparedStatement;
import hr.restart.swing.JraFrame;
import hr.restart.util.OKpanel;
import hr.restart.util.raLocalTransaction;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryResolver;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raGuiMultipleDbCheck extends JraFrame{

  private raMultipleDatabaseCheck rMDC = new raMultipleDatabaseCheck();
  private Database source = null;
  private Database target = null;
  private QueryResolver qsource = null;
  private QueryResolver qtarget = null;
  private JTextField url1 = new JTextField();
  private JTextField url2 = new JTextField();
  private JTextField tablica = new JTextField();

  private OKpanel okp = new OKpanel(){
    public void jPrekid_actionPerformed(){
      cancelpres();
    }

    public void jBOK_actionPerformed(){
      okpres();
    }
  };

  public raGuiMultipleDbCheck() {
    this.getContentPane().setLayout(new BorderLayout());
    JPanel mypanel = new JPanel();
    this.getContentPane().add(okp,BorderLayout.SOUTH);
    this.getContentPane().add(mypanel,BorderLayout.CENTER);
    mypanel.setLayout(new XYLayout());
    mypanel.setBorder(BorderFactory.createEtchedBorder());


    mypanel.add(new JLabel("Izvorišna baza"), new XYConstraints(15,15,-1,-1));
    mypanel.add(url1, new XYConstraints(150,15,250,-1));
    mypanel.add(new JLabel("Odredišna baza"), new XYConstraints(15,45,-1,-1));
    mypanel.add(url2, new XYConstraints(150,45,250,-1));
    mypanel.add(new JLabel("Tabela za provjeru"), new XYConstraints(15,75,-1,-1));
    mypanel.add(tablica, new XYConstraints(150,75,150,-1));
    url1.setText("jdbc:interbase://161.53.200.99/home/interbase/maximirska.gdb");
    url2.setText("jdbc:interbase://161.53.200.99/home/interbase/prazna.gdb");

  }

  public void cancelpres(){
    System.exit(0);
  }

  public void okpres(){
    if (Validacija()){
      go();
    }
  }



  public boolean setUpDatabaseAndQResolver(String url,boolean isSourceDB ){
    try {
//"jdbc:interbase://161.53.200.99/home/interbase/maximirska.gdb"
      if (isSourceDB) {
        source = rMDC.getDatabase(url,"REST-ART","bingo","interbase.interclient.Driver");
        qsource = rMDC.getQueryResolver(source);
      } else {
        target = rMDC.getDatabase(url,"REST-ART","bingo","interbase.interclient.Driver");
        qtarget = rMDC.getQueryResolver(target);
      }
      return true;
    }
    catch (Exception ex) {
//      ex.printStackTrace();
      return false;
    }
  }

  public boolean TableTest(Database db) {
    try {
      ResultSet rst = db.getMetaData().getTables(null,null,tablica.getText(),null);
      return rst.next();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false;
   }
  }


  public boolean Validacija(){

    if (!setUpDatabaseAndQResolver(url1.getText(),true)) {
      JOptionPane.showConfirmDialog(this,"Neispravan izvorisni URL:"+url1.getText(),
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          url1.setText("");
          url1.requestFocus();
        }
      });
      return false;
    }
    if (!setUpDatabaseAndQResolver(url2.getText(),false)) {
      JOptionPane.showConfirmDialog(this,"Neispravan odredišni URL:"+url2.getText(),
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          url2.setText("");
          url2.requestFocus();
        }
      });
      return false;
    }
    try {
      if (source.getMetaData().getURL().equals(target.getMetaData().getURL())) {
        JOptionPane.showConfirmDialog(this,"Odredišni i izvorišni URL ne smiju biti isti !!!",
                                      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            url1.setText("");
            url2.setText("");
            url1.requestFocus();
          }
        });
      }
    }
    catch (Exception ex) {
      JOptionPane.showConfirmDialog(this,"Nepoznata greška pogledaj LOG",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      return false;
    }

// oðe bi trebala provjera strukture
    if (tablica.getText().equalsIgnoreCase("")) {
      JOptionPane.showConfirmDialog(this,"Polje tabela za provjeru je obavezno",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          tablica.requestFocus();
        }
      });
      return false;
    }

    if (!TableTest(source)) {
      JOptionPane.showConfirmDialog(this,"Ne postoji tabela "+tablica.getText()+" u izvorišnoj bazi",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          tablica.setText("");
          tablica.requestFocus();
        }
      });
      return false;
    }
    if (!TableTest(target)) {
      JOptionPane.showConfirmDialog(this,"Ne postoji tabela "+tablica.getText()+" u odredišnoj bazi",
                                    "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          tablica.setText("");
          tablica.requestFocus();
        }
      });
      return false;
    }
    return true;
  }


  public boolean onoutransakciji(){
    raPreparedStatement[] rps =  rMDC.chackTableInMultipleDatabases(
                   tablica.getText(),source,qsource,target,qtarget,new String[] {});


    for (int i = 0;i<rps.length;i++){
      try {
        rps[i].execute();
      }
      catch (SQLException ex) {
ex.printStackTrace();
        return false;
      }
    }
    return true;
  }
  public void go(){

    raLocalTransaction rLT = new raLocalTransaction(new Database[]{}){
        public boolean transaction(){
          return onoutransakciji();
        }
    };
    rLT.execTransaction();
  }

}
