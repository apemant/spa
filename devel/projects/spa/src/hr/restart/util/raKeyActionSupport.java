/****license*****************************************************************
**   file: raKeyActionSupport.java
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
 * raKeyActionSupport.java
 *
 * Created on 2004. lipanj 03, 15:49
 */

package hr.restart.util;

import hr.restart.help.raHelpAware;
import hr.restart.help.raHelpContainer;
import hr.restart.swing.AWTKeyboard;
import hr.restart.util.columnsbean.ColumnsBean;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * Klasa koja handla raKeyActione 
 * inicijalno pisana za relaciju raUpitLite - raHelpContainer
 * trebala bi se implementirati i u okPanel i raMatpodaci
 * @author  andrej
 */
public class raKeyActionSupport implements raHelpAware {
  private ArrayList rakeyactions = new ArrayList();
  private Window owner;
  private raFrame fowner;
  private ColumnsBean columnsBean;
  private raJPNavContainer navContainer;
  /**
   * Creates a new instance of raKeyActionSupport
   * @param _owner Frame ili Dialog u kojem se tipke trose
   */
  public raKeyActionSupport(java.awt.Window _owner) {
    owner = _owner;
    raHelpContainer.registerHelp(this);
  }
  /**
   * Creates a new instance of raKeyActionSupport
   * @param _owner raFrame u kojem se tipke trose
   */  
  public raKeyActionSupport(raFrame _owner) {
    fowner = _owner;
    raHelpContainer.registerHelp(this);
  }
  
  
  /**
   * Dodaj raKeyAction
   * @param ac ispravno konstruiran raKeyAction
   */  
  public void addKeyAction(raKeyAction ac) {
    rakeyactions.add(ac);
  }
  
  /**
   * makni raKeyAkciju
   * @param ac ispravno konstruiran raKeyAction
   */  
  public void removeKeyAction(raKeyAction ac) {
    rakeyactions.remove(ac);
  }
  
  /**
   * mice oldac i stavlja newac na isto mjesto.
   * Korisno za izmjenu texta, slike, tipke on the fly.
   * Ako stara akcija nije dodana, nova se dodaje na kraj
   * @param oldac stara akcija koju ce zamijeniti nova
   * @param newac nova akcija
   */
  public void replaceKeyAction(raKeyAction oldac, raKeyAction newac) {
    int idx = rakeyactions.indexOf(oldac);
    if (idx == -1) {
      addKeyAction(newac);
    } else {
      rakeyactions.remove(idx);
      rakeyactions.add(idx, newac);
    }
  }
  
  /**
   * Pozvati u keylisteneru koji je dodan na komponentu
   * ili jos bolje dodati keylistener sa registerKeyActions(Component)
   * @param ev KeyEvent koji je okinuo
   * @see #registerKeyActions(Component)
   * @see #uregisterKeyActions(Component)
   */  
  public void processKeyActions(KeyEvent ev) {
    for (Iterator i = rakeyactions.iterator(); i.hasNext() && !ev.isConsumed(); ) {
      final raKeyAction key = (raKeyAction)i.next();
      if (ev.getKeyCode()==key.getRaActionKey() 
          && (key.getActionModifiers()==0 || ev.getModifiers()==key.getActionModifiers())) {
        ev.consume();
        key.invokeLater();
        break;
      }
    }
  }
  private raKASkeyListener listener = null;
  private Component lcomp = null; 
  /**
   * Dodaje KeyListener na zadanu komponentu koji zove processKeyActions
   * @param comp komponenta za dodati listener
   * @see #processKeyActions(KeyEvent)
   */  
  public void registerKeyActions(Component comp) {
    if (listener != null) AWTKeyboard.unregisterKeyListener(lcomp, listener);
    AWTKeyboard.registerKeyListener(lcomp = comp, listener = new raKASkeyListener());    
  }
  
  /**
   * Mice KeyListener sa zadane komponente
   * @param comp komponenta za maknuti listener
   */  
  public void unregisterKeyActions(Component comp) {
    /*KeyListener[] kyls = (KeyListener[])comp.getListeners(KeyListener.class);
    for (int i=0;i<kyls.length;i++) {
      if (kyls[i] instanceof raKASkeyListener) {
        comp.removeKeyListener(kyls[i]);
      }
    }*/
    if (listener != null) {
      AWTKeyboard.unregisterKeyListener(comp, listener);
      listener = null;
    }
  }
  /**
   * ColumnsBean ako se koristi na owneru
   * @param cb ColumnsBean ako se koristi na owneru
   */  
  public void setColumnsBean(ColumnsBean cb) {
    columnsBean = cb;
  }
  
  /**
   * raJPNavContainer ako se koristi u _owneru (raNavBar.getNavContainer)
   * @param bar raNavBar ako se koristi u _owneru
   */  
  public void setNavContainer(raJPNavContainer cont) {
    navContainer = cont;
  }
  
  public boolean isVisible() {
    if (owner != null) return owner.isVisible();
    if (fowner != null) return fowner.isVisible();
    return false;
  }
  //raHelpAware
  
  /**
   * raHelpAware
   * @return vraca array dodanih keyAkcija
   */  
  public raKeyAction[] getKeyActions() {
    return (raKeyAction[])rakeyactions.toArray(new raKeyAction[0]);
  }
  
  /**
   * raHelpAware
   * @param wlistener raHelpAware
   */  
  public void addWindowListener(java.awt.event.WindowAdapter wlistener) {
    if (owner != null) owner.addWindowListener(wlistener);
    if (fowner != null) fowner.addWindowListener(wlistener);
  }
  
  /**
   * raHelpAware
   * @return raHelpAware
   */  
  public hr.restart.util.columnsbean.ColumnsBean getColumnsBean() {
    return columnsBean;
  }
  
  /**
   * raHelpAware
   * @param listenerType raHelpAware
   * @return raHelpAware
   */  
  public java.util.EventListener[] getListeners(Class listenerType) {
    if (owner != null) return owner.getListeners(listenerType);
    if (fowner != null) return fowner.getListeners(listenerType);
    return new java.util.EventListener[0];
  }
  
  /**
   * raHelpAware
   * @return raHelpAware
   */  
  public raNavAction[] getNavActions() {
    if (navContainer == null) return null;
    return navContainer.getNavActions();
  }
  
  /**
   * raHelpAware
   * @return raHelpAware
   */  
  public String getTitle() {
    if (fowner != null) return fowner.getTitle();
    if (owner != null) {
      if (owner instanceof Frame) return ((Frame)owner).getTitle();
      if (owner instanceof Dialog) return ((Dialog)owner).getTitle();
    }
    return "";
  }
  
  /**
   * raHelpAware
   * @param l raHelpAware
   */  
  public void removeWindowListener(java.awt.event.WindowAdapter l) {
    if (owner != null) owner.removeWindowListener(l);
    if (fowner != null) fowner.removeWindowListener(l);
  }
  
  class raKASkeyListener extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      processKeyActions(e);
    }
  }
}
