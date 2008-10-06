/****license*****************************************************************
**   file: DataReceiverUI.java
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
/*
 * Created on Oct 13, 2005
 */
package hr.restart.util.mail.ui;

import hr.restart.start;
import hr.restart.baza.Condition;
import hr.restart.baza.Parametri;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.util.startFrame;
import hr.restart.util.mail.DataPostreceiverSaveChanges;
import hr.restart.util.mail.DataReceiver;
import hr.restart.util.mail.DataReceiverLoadedData;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * hr.restart.util.mail.ui.DataReceiverUI
 * <pre>
 * poruka - da - ne - opcije
 *  - da - DataReceiver.receiveData(); showData (tree?) - prihvat - prekid
 *                  prihvat - savechanges()?DataReceiver.deleteReceived():prekid
 *  - ne 
 *  - opcije - root autorizacija 
 *               - DataView:select * from parametri where app=sisfun and param in (sve vezano uz receiver)
 *           - povratak na poruku
 * 
 * </pre>
 * @author andrej
 */
public abstract class DataReceiverUI {

  private String[] paramkeys = {
      "recmailhost","recmailpasswd","recmailprotocol","recmailuser","drecsendfilter"
  };
  private DataReceiverShowData sd;
  private DataReceiver drec;
  private DataReceiverLoadedData data;
  private ArrayList pclisteners = new ArrayList();
  /**
   * 
   */
  public DataReceiverUI() {
    //go();
    sd = new DataReceiverShowData(this);
    initShowData();
    startFrame.getStartFrame().centerFrame(sd, 0, "Prijem podataka putem e-maila");
    sd.show();
  }
  /**
   * Tu treba definirati visible kolone za module (tablice) preko metode
   * addVisibleCols(module, int[]) 
   */
  public abstract void initShowData();
  
  boolean go() {
    int answ = 0;//confirmation();
    if (answ == 0) {
      receiveAndShowData();
      return true;
    } else if (answ == 1) {
      showOptions();
      return false;
    }
    return false;
  }
  /**
   * 
   */
  private void receiveAndShowData() {
    try {
      drec = createDataReceiver();
      addListeners();
      data = drec.receiveData();
      showData(data);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null,"Prijem neuspješan. Ponovite postupak!");
    }
  }
  private void addListeners() {
    for (int i = 0; i < pclisteners.size(); i++) {
      drec.addPropertyChangeListener("status", (PropertyChangeListener)pclisteners.get(i));
    } 
  }
  void addPropertyChangeListener(PropertyChangeListener l) {
    pclisteners.add(l);
  }
  /**
   *  
   */
  private void showData(DataReceiverLoadedData data) {
    sd.setData(data);
  }
  /**
   * E tu treba dodati DataPostReceiver-e, i NE SEJVATI PROMJENE!! 
   * jer poslije ovoga ide ekran koji prikazuje sto je dobiveno
   * <pre>
   * Primjer:
   * DataReceiver rec = new DataReceiver();
   * rec.addDataPostReceiver(new MyDataPostreceiver());
   * return rec;
   * </pre>
   * @return
   */
  public abstract DataReceiver createDataReceiver();
  /**
   * 
   */
  void showOptions() {
    if (rootAuth()) {
    System.out.println("Options");
    QueryDataSet parametri = Parametri.getDataModule()
    	.getFilteredDataSet(Condition.equal("APP","sisfun")+" AND "+Condition.in("PARAM",paramkeys));
    parametri.open();
    if (parametri.getRowCount() != paramkeys.length) {
      //treba dodati parametre
      new DataReceiver();
      parametri.refresh(); //notifier?
    }
    frmTableDataView dw = new frmTableDataView(true, true, false) {
      public void hide() {
			  super.hide();
//			  go();
			}
    };
    dw.setDataSet(parametri);
    dw.show();
    } else {
//      go();
    }
  }
  
  /**
   * @return
   */
  private boolean rootAuth() {
    return start.getFrmPassword().askLogin("root");
  }
  private int confirmation() {
    return JOptionPane.showOptionDialog(null, "Prihvatiti podatke e-mailom","Prihvat podataka",
        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        new String[] {"Prihvat", "Opcije", "Prekid"}, "Prihvat");
  }
  

  private boolean askCommit() {
    return JOptionPane.showConfirmDialog(null, "Potvrditi prihvat podataka?", "Prihvat", JOptionPane.YES_NO_OPTION)
    	== JOptionPane.YES_OPTION;
  }
  /**
   * 
   */
  public boolean commit() {
    if (data == null) return true;
    if (askCommit()) {
      try {
        DataPostreceiverSaveChanges dprsc = new DataPostreceiverSaveChanges(true);
        if (dprsc.run(data)) {
          drec.deleteReceived();
          return true;
        } else {
          //poruka
        }
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
    }
    return false;
  }
  
  public void addVisibleCols(String module, int[] viscols) {
    if (sd == null) return;
    sd.addVisibleCols(module, viscols);
  }
  
  //TEST
  public static void test() {
    DataReceiverUI d = new DataReceiverUI() {

      public DataReceiver createDataReceiver() {
        DataReceiver rec = new DataReceiver();
        //rec.addDataPostReceiver(new MyDataPostreceiver());
        return rec;
      }

      public void initShowData() {
        addVisibleCols("orgstruktura", new int[] {0,1});
        addVisibleCols("partneri", new int[] {0,1,5});
      }
      
    };
  }
  public static void raRobnoMailReceiver() {
	  DataReceiverUI d = new DataReceiverUI() {
		public DataReceiver createDataReceiver() {
			DataReceiver rec = new DataReceiver();
		    rec.addDataPostReceiver(new hr.restart.robno.RobnoPostreceiver()); 
		    return rec;
		}
		
		public void initShowData() {
/*			addVisibleCols("doki", new int[] {1,2,3,4,7}); 
			addVisibleCols("doku", new int[] {0,1,3,4,7});
			addVisibleCols("stdoki", new int[] {3,4,5,6,8,9,11});
			addVisibleCols("stdoku", new int[] {3,4,5,6,8,9,10});*/
			addVisibleCols("doki", new int[] {1,2,3,4,7}); 
			addVisibleCols("doku", new int[] {0,1,3,4,7});
			addVisibleCols("meskla", new int[] {0,1,2,5,8});
			addVisibleCols("partneri", new int[] {0,1,5});
			addVisibleCols("stdoki", new int[] {3,4,5,6,8,9,11});
			addVisibleCols("stdoku", new int[] {3,4,5,6,8,9,10});
			addVisibleCols("stmeskla", new int[] {0,1,2,4,5,7,9});
		}
	  };
  }
}
