/****license*****************************************************************
**   file: IColumnFormater.java
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
 * Created on Dec 28, 2004
 */
package hr.restart.util.textconv;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IColumnFormater {
  /**
   * Converts 'content' applying given 'format' into object of given 'type' 
   * @param content value for formating
   * @param format string representaton of format eg. DD-MM-YYYY
   * @param type one of ColumnTypes
   * @return object of given 'type'
   */
  public abstract Object applyFormat(String content, String format, int type);
}