/****license*****************************************************************
**   file: raHelpAware.java
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
 * raHelpAware.java
 *
 * Created on 2004. svibanj 31, 11:02
 */

package hr.restart.help;

import hr.restart.util.raKeyAction;
import hr.restart.util.raNavAction;
import hr.restart.util.columnsbean.ColumnsBean;

/**
 * Interface koji trebaju implementirati sve klase (ekrani) koji zele pregled tipaka
 * u helpu koristi se u raHelpContainer.registerHelp(raHelpAware);
 * @author andrej
 */
public interface raHelpAware {
  /**
   * Vraca sve keyActione
   * @return Vraca sve keyActione
   */  
  public raKeyAction[] getKeyActions();
  /**
   * vraca sve navActione
   * @return vraca sve navActione
   */  
  public raNavAction[] getNavActions();
  /**
   * Vraca columnsBean koji opet ima keyactione
   * @return Vraca columnsBean koji opet ima keyactione
   */  
  public ColumnsBean getColumnsBean();
  /**
   * Metoda od JFrame-a, raFrame-a i slicnoga tako da nece biti problema
   * ako klasa naslijedjuje bilo koji java.awt.Window ili raFrame
   * @return naslov ekrana
   */  
  public String getTitle();
  
  /**
   * Metoda od JFrame-a, raFrame-a i slicnoga tako da nece biti problema
   * ako klasa naslijedjuje bilo koji java.awt.Window ili raFrame
   * @param wlistener WindowAdapter koji se dodaje
   */
  public void addWindowListener(java.awt.event.WindowAdapter wlistener);

   /**
   * Metoda od JFrame-a, raFrame-a i slicnoga tako da nece biti problema
   * ako klasa naslijedjuje bilo koji java.awt.Window ili raFrame
   * @param listenerType 
   */
  public java.util.EventListener[] getListeners(Class listenerType);

  /**
   * Metoda od JFrame-a, raFrame-a i slicnoga tako da nece biti problema
   * ako klasa naslijedjuje bilo koji java.awt.Window ili raFrame
   * @param l WindowAdapter koji se remova
   */
  public void removeWindowListener(java.awt.event.WindowAdapter l);
}