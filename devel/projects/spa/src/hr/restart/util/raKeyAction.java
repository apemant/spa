/****license*****************************************************************
**   file: raKeyAction.java
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
package hr.restart.util;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class raKeyAction {
  private int raActionKey;
  private int raActionModifiers;
  private String raActionDesc;
  private boolean helperVisible;
  private Icon icon;
  
  private boolean busy;

/**
 * Konstruira raKeyAction koja se izvrsava pritiskom na key, bez modifiera,
 * s tim da je key jedna od varijabli definiranih java.awt.KeyEvent
 */
  public raKeyAction(int key) {
    this(key,0,"");
  }

/**
 * Konstruira raKeyAction koja se izvrsava pritiskom na key, s modifierom modifiers.
 * <pre>
 * key je jedna od varijabli definiranih java.awt.event.KeyEvent.
 * modifiers je jedna od varijabli definiranih u java.awt.event.InputEvent (naslijedjuje ga i keyevent pa ih ima i tamo)
 * </pre>
 */
  public raKeyAction(int key,int modifiers) {
    this(key,modifiers,"");
  }

/**
 * Konstruira raKeyAction koja se izvrsava pritiskom na key, bez modifiera i ima opis desc koji se pojavljuje na help ekranima.
 * <pre>
 * key je jedna od varijabli definiranih java.awt.event.KeyEvent.
 * desc je strimg u kojemu je opis koji se pojavljuje na help ekranima
 * </pre>
 */
  public raKeyAction(int key,String desc) {
    this(key,0,desc);
  }

/**
 * Konstruira raKeyAction koja se izvrsava pritiskom na key, s modifierom modifiers i ima opis desc koji se pojavljuje na help ekranima.
 * <pre>
 * key je jedna od varijabli definiranih java.awt.event.KeyEvent.
 * modifiers je jedna od varijabli definiranih u java.awt.event.InputEvent (naslijedjuje ga i keyevent pa ih ima i tamo)
 * desc je strimg u kojemu je opis koji se pojavljuje na help ekranima
 * </pre>
 */
  public raKeyAction(int key,int modifiers,String desc) {
    raActionKey = key;
    raActionDesc = desc;
    raActionModifiers = modifiers;
    mannageVisibilityInHelper();
  }
  /**
   * Ikona koja se vidi u helperu
   * @param ic ikona nalbolje iz raImages.getImageIcon(...
   */
  public void setIcon(Icon ic) {
    icon = ic;
  }
  
  /**
   * Ikona koja se vidi u helperu
   * @return ic ikonu za helper
   */  
  public Icon getIcon() {
    return icon;
  }
  public int getRaActionKey() {
    return raActionKey;
  }

  public void setActionDesc(String newActionDesc) {
    raActionDesc = newActionDesc;
    mannageVisibilityInHelper();
  }

  public String getActionDesc() {
    return raActionDesc;
  }

  public String getActionText() {
    return raActionDesc+" "+getModifierText()+KeyEvent.getKeyText(raActionKey);
  }
  
  public String getModifierText() {
    if (raActionModifiers==0) return "";
    return KeyEvent.getKeyModifiersText(raActionModifiers)+"+";
  }

  public void setActionModifiers(int newModifiers) {
    raActionModifiers = newModifiers;
  }

  public int getActionModifiers() {
    return raActionModifiers;
  }

  public boolean equals(int actionKey) {
    return raActionKey == actionKey;
  }
  
  public boolean equals(KeyEvent ke) {
    return (raActionKey == ke.getKeyCode())&&(raActionModifiers == ke.getModifiers());
  }
  /** da li se raKeyAction vidi u pomocniku sa strane
   * @return da li se raKeyAction vidi u pomocniku sa strane
   */
  public boolean isVisibleInHelper() {
    return helperVisible;
  }
  
  /** Parametar da li se raKeyAction vidi u pomocniku sa strane
   * Postavlja se automatski ako se definira actionDesc
   * (u konstruktoru ili posebno) ako je actionDesc null ili ""
   * visibleInHelper je false, u protivnom je true
   * @param visibleInHelper da li se raKeyAction vidi u pomocniku sa strane
   */  
  public void setVisibleInHelper(boolean visibleInHelper) {
    helperVisible = visibleInHelper;
  }
  private void mannageVisibilityInHelper() {
    if (raActionDesc == null) raActionDesc = "";
    setVisibleInHelper(!raActionDesc.equals(""));
  }
  
  public void invoke() {
		if (busy) return;
		try {
			busy = true;
			keyAction();
		} finally {
			busy = false;
		}
	}
	
	public void invokeLater() {
		if (busy) return;
		busy = true;
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						keyAction();
					} finally {
						busy = false;
					}
				}
			});
		} catch (RuntimeException t) {
			busy = false;
			throw t;
		}
	}
	
/**
 * Doga\u0111a se na pritisak tipke
 */
  public abstract void keyAction();

}