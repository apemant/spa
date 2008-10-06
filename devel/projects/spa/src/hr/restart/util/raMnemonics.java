/****license*****************************************************************
**   file: raMnemonics.java
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
 * raMnemonics.java
 *
 * Created on 2003. studeni 05, 12:11
 */

package hr.restart.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ColumnAware;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;

/** Bazna klasa za mnemonike.
 * <PRE>
 * Depends:
 * abstract class raMnemWorker - u cijoj instanci se dodaju mnemonici (raMnemVar-ovi)
 * abstract class raMnemVar - sa metodom getText vraca replacment za mnemonic
 * Features:
 * dialog za dohvat mnemonica sa parametrom postojeceg text-a koji vraca modificirani text
 * PRIMJER:
 * 1.) Kako napraviti mnemonike
 * raMnemWorker worker = raMnemonics.addWorker(new raMnemWorker("lozhachi"));
 * raMnemVar pero = worker.add(new raMnemVar("pero","Pero Lozach") {
 *    public String getText() {
 *      //neki kod
 *      return "svi znaju tko"
 *    }
 * }
 * raMnemVar pero = worker.add(new raMnemVar("garo","Ugljesa Garawitch") {
 *    public String getText() {
 *      //neki kod
 *      return "dobrocudni starcic"
 *    }
 * }
 * ....
 *
 * 2.) Kako napraviti komponente na ekranu za unos texta s mnemonicima
 * JraButton jb1 = new JraButton();
 * jb1.setText("...");
 * JraTextField jtf1 = JraTextField();
 * ...
 * raMnemonics.createMnemonicLookup(jtf1, "lozachi");// ili raMnemonics.createMnemonicLookup(jtf1, null);
 * raMnemonics.createMnemonicLookupButton(jb1, jtf1);
 * ...
 *
 * 3.) Kako upotrijebiti mnemonike
 * String izmjenjeniText = replaceMnemonics("Mislim da <I>pero</I> nije <I>garo</I>");
 * //Vodeci se primjerom 1 dobivamo izmjenjeniText = "Mislim da <I>svi znaju tko</I> nije <I>dobrocudni starcic</I>"
 *
 * </PRE>
 * @author andrej
 */


public class raMnemonics {
  private static HashMap workers = new HashMap();
  private static StorageDataSet varSet;
  private static int VK_key;  
  private static raMnemonics.MnemonicKeyAdapter mnListener;
  /** Parametar za mod vracanja i trazenja mnemonica:
   * Vrati zamjenski text mnemonica
   * @see raMnemonics.getVarText(String, String)
   * @see raMnemVar.getText()
   */  
  public static int GETTEXT = 0;
  /** Parametar za mod vracanja i trazenja mnemonica:
   * Vrati ime mnemonica
   * @see raMnemVar.getName()
   */  
  public static int GETVARNAME = 1;
  /** Parametar za mod vracanja i trazenja mnemonica:
   * Vrati ime workera i ime mnemonica delimitirano sa tockom
   * npr. virmani.jmbg
   */  
  public static int GETWORKERVARNAME = 2;
  public static int DEFAULTMNMODE = GETVARNAME;
  /** For future use
   * NE KORISTITI ZA SADA BACA IllegalArgumentException
   * Za metodu replaceMenmonic da sam pokusa
   * skuziti u kojem modu su mnemonici insertirani
   * GETVARNAME ili GETWORKERVARNAME
   */  
  public static int DETERMINEMNMODE = 4;//samo za replaceMnemonics
  /** Sve metode su staticke pa nema potrebe za
   * konstruktorom
   */
  protected raMnemonics() {
  }
  /** Dodaje workera u listu workera
   * @param worker - worker koji treba dodati
   * @return isti taj mnemonic tako da bi kod izgledao zgodno npr:
   * <pre>
   * raMnemWorker worker = raMnemonics.addWorker(new raMnemWorker("lozhachi"));
   * raMnemVar pero = worker.add(new raMnemVar("pero","Pero Lozach") {
   *    public String getText() {
   *      //bla bla bla
   *    }
   * }
   * </pre>
   */
  public static raMnemWorker addWorker(raMnemWorker worker) {
    workers.put(worker.getID(), worker);
    return worker;
  }
  
  /** Dohvaca workera preko njegovog ID-a
   * @param workerID ID workera koji se setira u konstruktoru
   * raMnemWorker
   * @return Dodanog workera iz liste workera ako postoji, ako ne vraca null
   * @see java.util.HashMap.get(Object)
   */  
  public static raMnemWorker getWorker(String workerID) {
    return (raMnemWorker)workers.get(workerID);
  }
  
  /** Vraca mnemVar od odredjenog workera
   * @param workerID ID workera u kojem je mnemVar
   * @param varName ime (getName()) mnemVara
   * @return trazeni raMnemVar ako ga ima, u suprotnom null
   */  
  public static raMnemVar getVar(String workerID, String varName) {
    return (raMnemVar)getWorker(workerID).getVars().get(varName);
  }

  /** vraca getText() od zadanog workera i varNamea
   * @param workerID workerID
   * @param varName varName
   * @return etText() od zadanog workera i varNamea
   */  
  public static String getVarText(String workerID, String varName) {
    try {
      return getVar(workerID, varName).getText();
    } catch (Exception ex) {
      //return "Nije moguce dohvatiti mnemonic "+workerID+"."+varName;
      return "";
    }
    
  }
  
  /** Kreira StorageDataset od mnemonika zadanog workera
   * @param workerID ID workera, ako je null uzima sve workere
   * @return StorageDataset od mnemonika zadanog workera
   */  
  public static StorageDataSet getVarSet(String workerID) {
    varSet = new StorageDataSet();
    Column colNAME = new Column("NAME", "Naziv", Variant.STRING);
    Column colDESC = new Column("DESC", "Opis", Variant.STRING);
    Column colWorker = new Column("WORKER", "Izvor", Variant.STRING);
    colNAME.setPrecision(20);
    colDESC.setPrecision(50);
    colWorker.setPrecision(30);
 //   colWorker.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    varSet.setColumns(new Column[] {colNAME,colDESC,colWorker});
    varSet.setTableName("Mnemonics");
    varSet.open();
    raMnemWorker[] workArray = null;
    if (workerID!=null) {
      workArray = new raMnemWorker[] {getWorker(workerID)};
    } else {
      workArray = (raMnemWorker[])workers.values().toArray(new raMnemWorker[0]);
    }
    for (int n=0; n<workArray.length; n++) {
      raMnemWorker worker = workArray[n];
      for (Iterator i = worker.getVars().values().iterator(); i.hasNext(); ) {
        Object item = i.next();
        raMnemVar var = (raMnemVar)item;
        varSet.insertRow(false);
        varSet.setString("NAME",var.getName());
        varSet.setString("DESC", var.getDescription());
        varSet.setString("WORKER", worker.getID());
        varSet.post();
      }
    }
    
    return varSet;
  }
  
  private static lookupFrame getLookupFrame(String workerID, Dialog owner) {
    return lookupFrame.getLookupFrame(owner, getVarSet(workerID), new int[] {0,1});
  }
  
  private static lookupFrame getLookupFrame(String workerID, Frame owner) {
    return lookupFrame.getLookupFrame(owner, getVarSet(workerID), new int[] {0,1});
  }
  
  private static lookupFrame getLookupFrame(String workerID, raFrame raOwner) {
    if (raOwner.getWindow() instanceof Dialog) 
      return lookupFrame.getLookupFrame((Dialog)raOwner.getWindow(), getVarSet(workerID), new int[] {0,1});
    else if (raOwner.getWindow() instanceof Frame) 
      return lookupFrame.getLookupFrame((Frame)raOwner.getWindow(), getVarSet(workerID), new int[] {0,1});
    else throw new UnsupportedOperationException("Owner nije podrzan "+raOwner.getWindow().getClass().getName());
  }
  
  private static String getVarTextFromDlg(lookupFrame lfr, String workerID, String textBefore, int mode) {
    lfr.ShowCenter();
    String varName = null;
    if (lfr.getRetValuesUI()!= null) {
      varName = varSet.getString("NAME");
    }
    if (varName == null) return textBefore;
    if (workerID == null) workerID = varSet.getString("WORKER");
    return textBefore + getVarWithMode(workerID, varName, mode);
  }
  
  /** Vraca zadanu vrijednost zadanog mnemonica od zadanog workera
   * @param workerID ID workera
   * @param varName ime raMnemVara
   * @param mode GETTEXT, GETVARNAME ili GETWORKERVARNAME
   * @return zadanu vrijednost zadanog mnemonica od zadanog workera
   */  
  public static String getVarWithMode(String workerID, String varName, int mode) {
    if (mode == GETTEXT) {
      return getVarText(workerID, varName);
    } else if (mode == GETVARNAME) {
      return varName;
    } else if (mode == GETWORKERVARNAME) {
      return workerID + "." + varName;
    } else throw new IllegalArgumentException("Nepostojeci mod");
  }
  
  /** Prikazuje dialog za odabrati odredjeni mnemonic
   * @param workerID workerID
   * @param textBefore text na koji treba nadodati vrijednost mnemonica
   * obicno JTextField.getText()
   * @param owner owner
   * @param mode GETTEXT, GETVARNAME ili GETWORKERVARNAME
   * @return textBefore + vrijednost odabranog mnemonica
   */  
  public static String getVarTextDlg(String workerID, String textBefore, Dialog owner, int mode) {
    return getVarTextFromDlg(getLookupFrame(workerID, owner), workerID, textBefore, mode);
  }
  
  /** Prikazuje dialog za odabrati odredjeni mnemonic
   * @param workerID workerID
   * @param textBefore text na koji treba nadodati vrijednost mnemonica
   * obicno JTextField.getText()
   * @param owner owner
   * @param mode GETTEXT, GETVARNAME ili GETWORKERVARNAME
   * @return textBefore + vrijednost odabranog mnemonica
   */  
  public static String getVarTextDlg(String workerID, String textBefore, Frame owner, int mode) {
    return getVarTextFromDlg(getLookupFrame(workerID, owner), workerID, textBefore, mode);
  }
  
  /** Prikazuje dialog za odabrati odredjeni mnemonic
   * @param workerID workerID
   * @param textBefore text na koji treba nadodati vrijednost mnemonica
   * obicno JTextField.getText()
   * @param owner owner
   * @param mode GETTEXT, GETVARNAME ili GETWORKERVARNAME
   * @return textBefore + vrijednost odabranog mnemonica
   */  
  public static String getVarTextDlg(String workerID, String textBefore, raFrame owner, int mode) {
    return getVarTextFromDlg(getLookupFrame(workerID, owner), workerID, textBefore, mode);
  }
  
  /** Dodaje listener na zadani button koji ce raditi isto sto i tipka na jtc
   * @param button button
   * @param jtc text field prethodno provucen kroz createMnemonicLookup metodu
   */  
  public static void createMnemonicLookupButton(javax.swing.AbstractButton button, final javax.swing.text.JTextComponent jtc) {
    KeyListener[] keyListeners = (KeyListener[])jtc.getListeners(KeyListener.class);
    mnListener = null;
    for (int i=0; i<keyListeners.length; i++) {
      if (keyListeners[i] instanceof raMnemonics.MnemonicKeyAdapter) {
        mnListener = (raMnemonics.MnemonicKeyAdapter)keyListeners[i];
        break;
      }
    }
    if (mnListener == null) throw new IllegalArgumentException("TextField u parametru mora biti provucen kroz metodu createMnemonicLookup(...");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        mnListener.keyPressed(
          new KeyEvent(jtc, KeyEvent.KEY_PRESSED, System.currentTimeMillis(),0,mnListener.key,KeyEvent.CHAR_UNDEFINED)
        );
      }
    });
  }
  
  /** Dodaje listener na jtc koji ce na pritisnuti F9 uci u dohvat
   * mnemonika i dodati njegovo ime u nastavku vec unesenog teksta
   * @param jtc Text field u koji se unosi i stisce F9
   * @param workerID ID workera ili null za sve
   */  
  public static void createMnemonicLookup(javax.swing.text.JTextComponent jtc, String workerID) {
    createMnemonicLookup(jtc, workerID, KeyEvent.VK_UNDEFINED, DEFAULTMNMODE);
  }
  
  /** Dodaje listener na jtc koji ce na pritisnuti F9 uci u dohvat
   * mnemonika i dodati njegovo ime u nastavku vec unesenog teksta
   * @param jtc Text field u koji se unosi i stisce F9
   * @param workerID ID workera ili null za sve
   * @param mode GETTEXT, GETVARNAME ili GETWORKERVARNAME
   */  
  public static void createMnemonicLookup(javax.swing.text.JTextComponent jtc, String workerID, int mode) {
    createMnemonicLookup(jtc, workerID, KeyEvent.VK_UNDEFINED, mode);
  }
  
  /** Dodaje listener na jtc koji ce na pritisnutu tipku uci u dohvat
   * mnemonika i dodati njegovo ime u nastavku vec unesenog teksta
   * @param jtc Text field u koji se unosi i stisce F9
   * @param workerID ID workera ili null za sve
   * @param key tipka na koju se otvaras ekran za dohvat
   * @param mode GETTEXT, GETVARNAME ili GETWORKERVARNAME
   */  
  public static void createMnemonicLookup(final javax.swing.text.JTextComponent jtc, final String workerID, final int key, final int mode) {
    VK_key = (key == KeyEvent.VK_UNDEFINED)?KeyEvent.VK_F9:key;
    if (jtc instanceof hr.restart.swing.JraTextField) {
      ((hr.restart.swing.JraTextField)jtc).setSelectAllOnFocusGained(false);
    }
    jtc.addKeyListener(new raMnemonics.MnemonicKeyAdapter(jtc,workerID,VK_key,mode));
  }
  
  /** Zamijenjuje mnemonice u zadanom textu sa njegovim getText() vrijednostima
   * i vraca izmjenjeni tekst
   * @param text tekst koji sadrzi mnemonice za izmjenu
   * @param workerID ID workera, null za sve
   * @param varName ime raMnemVara null za sve
   * @param mode mod upisivanja mnemonica:
   * GETVARNAME ili GETWORKERVARNAME
   * @return text sa zamjenjenim mnemonicima
   */  
  public static String replaceMnemonics(String text, String workerID, String varName, int mode) {
    if (varName == null) return replaceMnemonics(text, workerID, mode); //lukavo a?
    String toReplace = getVarWithMode(workerID, varName, mode);
    
    if (text.indexOf(toReplace) == -1) {
      return text;//ako nema sto replacat da ne izvrsava GETTEXT 
    }
    return new VarStr(text).replaceAll(
      toReplace,getVarWithMode(workerID, varName, GETTEXT)
    ).toString();
  }
  
  /** Zamijenjuje mnemonice u zadanom textu sa njegovim getText() vrijednostima
   * i vraca izmjenjeni tekst. Pretrazuje u svim mnemonicima zadanog workera
   * @param text tekst koji sadrzi mnemonice za izmjenu
   * @param workerID ID workera, null za sve
   * @param mode mod upisivanja mnemonica:
   * GETVARNAME ili GETWORKERVARNAME
   * @return text sa zamjenjenim mnemonicima
   */    
  public static String replaceMnemonics(String text,String workerID, int mode) {
    if (workerID == null) return replaceMnemonics(text, mode);
    Set varNames = getWorker(workerID).getVars().keySet();
    for (Iterator i = varNames.iterator(); i.hasNext(); ) {
      String varName = i.next().toString();
      text = replaceMnemonics(text, workerID, varName, mode);
    }
    return text;
  }
  
  /** Zamijenjuje mnemonice u zadanom textu sa njegovim getText() vrijednostima
   * i vraca izmjenjeni tekst. Pretrazuje u svim mnemonicima.
   * @param text tekst koji sadrzi mnemonice za izmjenu
   * @param mode mod upisivanja mnemonica:
   * GETVARNAME ili GETWORKERVARNAME
   * @return text sa zamjenjenim mnemonicima
   */      
  public static String replaceMnemonics(String text, int mode) {
    Set workerIDs = workers.keySet();
    for (Iterator i = workerIDs.iterator(); i.hasNext(); ) {
      String workerID = i.next().toString();
      text = replaceMnemonics(text, workerID, mode);
    }
    return text;
  }
  
  /** Zamijenjuje mnemonice u zadanom textu sa njegovim getText() vrijednostima
   * i vraca izmjenjeni tekst. Pretrazuje u svim mnemonicima u GETVARNAME modu
   * @param text tekst koji sadrzi mnemonice za izmjenu
   * @return text sa zamjenjenim mnemonicima
   */        
  public static String replaceMnemonics(String text) {
    return replaceMnemonics(text, DEFAULTMNMODE);
  }

  static class MnemonicKeyAdapter extends KeyAdapter {
      private javax.swing.text.JTextComponent jtc;
      private String workerID;
      private int mnMode;
      int key;
      public MnemonicKeyAdapter(javax.swing.text.JTextComponent _jtc, String _workerID, int _key, int _mnMode) {
        jtc = _jtc;
        workerID = _workerID;
        key = _key;
        mnMode = _mnMode;
      }
      public void keyPressed(KeyEvent e) {
        if (e.isConsumed()) return;
        if (e.getKeyCode() == key) {
          String jtc_text = jtcGetText();//jtc.getText();
          String var_text = "";
          Container owner = jtc.getTopLevelAncestor();
          int caretPos = jtc.getCaretPosition();
          if (owner instanceof Dialog) {
            //jtc_text = getVarTextDlg(workerID, jtc_text, (Dialog)owner, mnMode);
            var_text = getVarTextDlg(workerID, "", (Dialog)owner, mnMode);
          } else if (owner instanceof Frame) {
            //jtc_text = getVarTextDlg(workerID, jtc_text, (Frame)owner, mnMode);
            var_text = getVarTextDlg(workerID, "", (Frame)owner, mnMode);
          }
          jtc_text = new VarStr(jtc_text).insert(caretPos,var_text).toString();
          jtc.setText(jtc_text);
          try {
            int newCaretPos = caretPos+var_text.length();
            newCaretPos = newCaretPos>jtc_text.length()?jtc_text.length()-1:newCaretPos;
            jtc.setCaretPosition(newCaretPos);
          } catch (Exception ex) {
            System.out.println("setCaretPosition ::> "+ex);
          }
        }
      }
      private String jtcGetText() {
/*        if (jtc instanceof ColumnAware) {
          ColumnAware jctc = (ColumnAware)jtc;
          if (jctc.getDataSet() != null && jctc.getColumnName() != null) {
            return hr.restart.db.raVariant.getDataSetValue(jctc.getDataSet(), jctc.getColumnName()).toString();
          }
        }*/
        return jtc.getText();
      }
      private void jtcSetText(String txt) {
        if (jtc instanceof ColumnAware) {
          ColumnAware jctc = (ColumnAware)jtc;
          if (jctc.getDataSet() != null && jctc.getColumnName() != null) {
            hr.restart.db.raVariant.setDataSetValue(jctc.getDataSet(), jctc.getColumnName(),txt);
          }
        }
        jtc.setText(txt);
      }      
    }
  
  
  
  
  
  
  
  
  
  ///// T E S T
  /** Test
   * @param args ha ha ha
   */  
  public static void main(String[] args) {
    raMnemWorker worker = raMnemonics.addWorker(new raMnemWorker("budale"));
    worker.addVar(new raMnemVar("01", "Pero Periæ") {
      public String getText() {
        return "perin substitut";
      }
    });
    
    worker.addVar(new raMnemVar("02", "Mujo Mujiæ") {
      public String getText() {
        return "mujina zamjena jer je na bolovanju";
      }
    });
    
    worker.addVar(new raMnemVar("03", "Fata") {
      public String getText() {
        return "mujina žena";
      }
    });
    
    worker.addVar(new raMnemVar("04", "Mujica") {
      public String getText() {
        return "jebala ga kliješta";
      }
    });
    
    raMnemWorker worker2 = raMnemonics.addWorker(new raMnemWorker("idioti"));
    worker2.addVar(new raMnemVar("b1", "Pero Periæ") {
      public String getText() {
        return "perin pimpek";
      }
    });
    
    worker2.addVar(new raMnemVar("b2", "Mujo Mujiæ") {
      public String getText() {
        return "mujin pimpek";
      }
    });
    
    worker2.addVar(new raMnemVar("b3", "Fata") {
      public String getText() {
        return "fatin pimpek";
      }
    });
    
    worker2.addVar(new raMnemVar("b4", "Mujica") {
      public String getText() {
        return "mujièin pimpek";
      }
    });
    
    String text = "Sklapam recenicu: ";
    javax.swing.JPanel pan = new javax.swing.JPanel(new BorderLayout());
    final javax.swing.JTextField jtf = new javax.swing.JTextField();
    hr.restart.swing.JraButton b = new hr.restart.swing.JraButton();
    hr.restart.swing.JraButton b2 = new hr.restart.swing.JraButton();
    b.setText("...");
    b2.setText("replacaj");
    b2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent blj) {
        System.out.println(replaceMnemonics(jtf.getText(),raMnemonics.GETWORKERVARNAME));
      }
    });
    createMnemonicLookup(jtf, null, raMnemonics.GETWORKERVARNAME);
    createMnemonicLookupButton(b, jtf);
    pan.add(jtf,BorderLayout.CENTER);
    pan.add(b,BorderLayout.SOUTH);
    pan.add(b2,BorderLayout.NORTH);
    javax.swing.JOptionPane.showMessageDialog(null,pan);
    System.exit(0);
  }
}
