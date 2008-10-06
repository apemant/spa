/****license*****************************************************************
**   file: KeyAction.java
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
 * Created on Sep 2, 2004
 *
 */
package hr.restart.swing;


/**
 * @author abf
 *
 * Koristi se u paketu s AWTKeyboard. Definira akciju koja se treba
 * izvrsiti ako se neka tipka pritisne.
 */
public interface KeyAction {
  
  /**
   * Metoda koja se poziva ako se pritisne tipka s kojom je ovaj action registriran.
   * @return true ako se tipka treba konzumirati (najcesci slucaj), inace false.
   */
  boolean actionPerformed();
}
