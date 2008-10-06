/****license*****************************************************************
**   file: DataReceiver.java
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
 * Created on Oct 11, 2005
 */
package hr.restart.util.mail;

import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.DesEncrypter;
import hr.restart.util.Encrypter;
import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;
import hr.restart.util.sysoutTEST;
import hr.restart.util.mail.ui.DataReceiverShowData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Part;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * Receives mail, saves, decrypts, extracts zip attachments, loads it in database and 
 * runs postreceive runnables.
 * @see source code of test() method
 */
public class DataReceiver extends Receiver {
  
  private ArrayList datapostreceivers = new ArrayList();
  private HashMap loadedData = new HashMap();
  private boolean saveChanges;
  private boolean saveChangesInTransaction = true;
  private PropertyChangeSupport pcs;
  
  /**
   * 
   */
  public DataReceiver() {
    super();
    setSenderFilter(frmParam.getParam("sisfun","drecsendfilter","","Filter za sendera e-maila"));
  }
  
  public DataReceiverLoadedData receiveData() {
    informStatus("Primam poruke...");
    File savedir = new File(Mailer.getMailProperties().getProperty("mailboxdir")+File.separator+"data");
    Message[] mess = receiveMail();
    informStatus("Primljeno "+mess.length+" poruka");
    for (int i = 0; i < mess.length; i++) {
      String sha1 = getSha1(mess[i]);
      Encrypter dec = getSenderNameEncripter(mess[i]);
      informStatus("Poruka "+(i+1)+": \n  SHA1:"+sha1+"\n"+(dec==null?"nije":"")+" kriptirana");
      if (sha1 != null && dec != null) {
        informStatus("Snimam priloge za poruku "+(i+1));
        File[] fatts = saveAttachments(mess[i],dec, savedir, sha1);
        //extract
        for (int j = 0; j < fatts.length; j++) {
					informStatus("Raspakiravam prilog "+fatts[j].getName());
          File unzdir = new File(fatts[j].getAbsolutePath()+".extract");
          FileHandler.unZipFile(fatts[j],unzdir);
          //LOAD data
					informStatus("Uèitavam podatke iz "+fatts[j].getName());
          loadedData.put(fatts[j].getName(), loadData(unzdir));
        }
        informStatus("Prijem poruke "+(i+1)+" uspješan!");
      }
    }
    //saveChanges?
    if (saveChanges) {
      addDataPostReceiver(new DataPostreceiverSaveChanges(saveChangesInTransaction));
    }
    //create DataReceiverLoadedData
    DataReceiverLoadedData data = new DataReceiverLoadedData(this);
    //run postreceivers
    
    DataPostreceiver[] prs = (DataPostreceiver[])datapostreceivers.toArray(new DataPostreceiver[datapostreceivers.size()]);
    for (int i = 0; i < prs.length; i++) {
      informStatus("Pokrecem "+prs[i].getShortInfo()+" ...");
      if (!prs[i].run(data)) {
        informStatus("Neuspjeh! Prekidam!");
        if (DataReceiverShowData.getDRSDInstance()!=null)
          DataReceiverShowData.getDRSDInstance().showErrors();

        throw new RuntimeException("DataPostreceiver no "+i+" failed"); 
      }
    }
    return data;
  }

  /**
   * @param unzdir
   * @return
   */
  private HashMap loadData(File unzdir) {
    HashMap sets = new HashMap();
    dM.getDataModule().loadModules();
    File[] dats = unzdir.listFiles(new FileFilter() {
      public boolean accept(File pathname) {
        return (pathname.isFile() && pathname.getName().toLowerCase().endsWith(".dat"));
      }
    });
    for (int i = 0; i < dats.length; i++) {
      String moduleName = new VarStr(dats[i].getName()).chop(4).toString().toLowerCase();
      try {
        QueryDataSet set = KreirDrop.getModuleByName(moduleName).loadData(unzdir);
        sets.put(moduleName,set);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    
    return sets;
  }

  /**
   * @param message
   * @return
   */
  private Encrypter getSenderNameEncripter(Message message) {
    String sender = SimpleReceiver.getSender(message);
    if (sender == null) return null;
    return new DesEncrypter(sender);
  }

  /**
   * @param message
   * @return
   */
  private String getSha1(Message message) {
    Part[] parts = getMessageParts(message);
    String sha1 = null; 
    for (int i = 0; i < parts.length; i++) {
      try {
        if (parts[i].isMimeType("text/plain")) {
          String content = (String)parts[i].getContent();
          if (content.indexOf("SHA1:")!=-1) {
            int begin = content.indexOf("SHA1:")+5;
            int end = begin+40;
            sha1 = content.substring(begin,end);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return sha1;
  }
 
  private String[] toArray(Set set) {
    String[] o = new String[set.size()];
    int idx = 0;
    for (Iterator iter = set.iterator(); iter.hasNext();) {
      o[idx] = (String)iter.next();
      idx++;
    }
    return o;
  }
  
  String[] getLoadedEmailKeys() {
    return toArray(loadedData.keySet());
  }
  
  String[] getLoadedModules() {
    HashSet modset = new HashSet();
    String[] emkeys = getLoadedEmailKeys();
    for (int i = 0; i < emkeys.length; i++) {
      HashMap setmap = (HashMap)loadedData.get(emkeys[i]);
      Set setset = setmap.keySet();
      for (Iterator iter = setset.iterator(); iter.hasNext();) {
        modset.add((String) iter.next());
      }
    }
    return toArray(modset);
  }
  
  QueryDataSet[] getLoadedSetsByModule(String module) {
    String[] emkeys = getLoadedEmailKeys();
    ArrayList setlist = new ArrayList();
    for (int i = 0; i < emkeys.length; i++) {
      HashMap setmap = (HashMap)loadedData.get(emkeys[i]);
      Set setset = setmap.keySet();
      for (Iterator iter = setset.iterator(); iter.hasNext();) {
        String _mod = (String)iter.next();
        if (module.equalsIgnoreCase(_mod)) {
          setlist.add(setmap.get(_mod));
        }
      }
    }
    return (QueryDataSet[])setlist.toArray(new QueryDataSet[setlist.size()]);
  }
  
  QueryDataSet getLoadedSet(String emailKey, String module) {
    HashMap setmap = (HashMap)loadedData.get(emailKey);
    return (QueryDataSet)setmap.get(module);
  }
  /**
   * Adds given DataPostreceiver on the end of the queue. 
   * It will be executed after receiving and loading data and after previously added receivers
   * execites (and returns success)
   * @param datapostreceiver DataPostreceiver to add
   */
  public void addDataPostReceiver(DataPostreceiver datapostreceiver) {
    datapostreceivers.add(datapostreceiver);
  }
  
  /**
   * Inserts given DataPostreceiver on the specified position in the queue.
   * @see addDataPostReceiver(DataPostreceiver) 
   * @param datapostreceiver DataPostreceiver to add
   * @param index position in queue
   */
  public void addDataPostReceiver(int index, DataPostreceiver datapostreceiver) {
    datapostreceivers.add(index, datapostreceiver);
  }
  
  public boolean isSaveChanges() {
    return saveChanges;
  }
  public boolean isSaveChangesInTransaction() {
    return saveChanges && saveChangesInTransaction;
  }
  
  private PropertyChangeSupport getPcs() {
    if (pcs == null) pcs = new PropertyChangeSupport(this);
    return pcs;
  }
  
  private void informStatus(String s) {
    System.out.println(s);
    getPcs().firePropertyChange("status", "", s);
  }
  /**
   * Adds a PropertyChangeListener. Current available propery is "status" which in nevValue informs
   * about receiving data.
   * @param l listener to add
   */
  public void addPropertyChangeListener(PropertyChangeListener l) {
    getPcs().addPropertyChangeListener(l);
  }
  
  /**
   * Adds a PropertyChangeListener. Current available propery is "status" which in nevValue informs
   * about receiving data.
   * @param propertyName "status" or else
   * @param l listener to add
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
    getPcs().addPropertyChangeListener(propertyName, l);
  }
  
/**
 * Adds new DataPostreceiverSaveChanges(inTransaction) at the end of datapostreceivers queue. 
 * It will run always on the end because it's added just before executing other postreceivers
 * @param saveChanges
 * @param inTransaction
 * @see DataPostreceiverSaveChanges
 */
  public void setSaveChanges(boolean saveChanges, boolean inTransaction) {
    this.saveChanges = saveChanges;
    saveChangesInTransaction = inTransaction;
  }
  
  /**
   * just for test 
   *
   */
  public void browseLoadedData() {
    for (Iterator iter = loadedData.keySet().iterator(); iter.hasNext();) {
      String key = (String)iter.next();
      System.out.println("Email key = "+key+"\n---------------------------");
      HashMap setmap = (HashMap)loadedData.get(key);
      for (Iterator iterator = setmap.keySet().iterator(); iterator.hasNext();) {
        String module = (String) iterator.next();
        System.out.println("  Module "+module);
        DataSet set = (DataSet)setmap.get(module);
        for (set.first(); set.inBounds(); set.next()) {
          System.out.println(set);
        }
      }
    }
  }
  /**
   * another test 
   *
   */
  public static void test() {
    DataReceiver rec = new hr.restart.util.mail.DataReceiver();
    rec.setUser("testiranje");
    rec.setPassword("test1234");
    rec.addDataPostReceiver(new DataPostreceiver() {
      public boolean run(DataReceiverLoadedData data) {
        sysoutTEST s = new sysoutTEST(false);
        s.prn(data.getLoadedEmailKeys());
        s.prn(data.getLoadedModules());
        String[] mods = data.getLoadedModules();
        for (int i = 0; i < mods.length; i++) {
          System.out.println("---------------\nSets for module "+mods[i]+"\n-------------");
          QueryDataSet[] sets = data.getLoadedSetsByModule(mods[i]);
          for (int j = 0; j < sets.length; j++) {
            System.out.println("Set "+j);
            s.prn(sets[i]);
          }
        }
        return true;
      }

      public String getShortInfo() {
        return "pregled dobivenih emailova";
      }
    });
    //rec.setSaveChanges(true, true);
    rec.receiveData();
    rec.deleteReceived();
    //rec.browseLoadedData();
  }
  public static void main(String args[]) {
    try {
      test();      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    System.out.println("[-o-]");
  }
}
