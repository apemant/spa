/****license*****************************************************************
**   file: HsqlDialect.java
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
 * Created on May 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.baza;

import hr.restart.util.IntParam;

import java.util.Properties;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HsqlDialect extends Dialect {

  private static Dialect inst = new HsqlDialect(!Boolean.valueOf(IntParam.getTag("hsqlinmemory")).booleanValue());
  
  private HsqlDialect() {
    this(true);
  }
  private HsqlDialect(boolean cached) {
    String c = cached?" CACHED ":" ";
    setDDLMap(new String[][] {
        { "CREATE", "create"+c+"table %NAME (%BODY)" },
        { "CHAR", "char(%SIZE)" },
        { "EXTCHAR", "" },
        { "INTEGER", "integer" },
        { "SHORT", "smallint" },
        { "FLOAT", "numeric(%SIZE,%PRECISION)" },
        { "DEFAULT", "default '%VALUE'" },
        { "DATE", "timestamp" },
        { "PKEY", "Primary Key (%FIELD)" },

        { "INDEX", "create index %NAME on %TABLE (%FIELD)" },
        { "UINDEX", "create unique index %NAME on %TABLE (%FIELD)" }
      });    
  }
  /* (non-Javadoc)
   * @see hr.restart.baza.Dialect#getDateYear(java.lang.String)
   */
  protected String getDateYear(String col) {
    return DefaultDialect.getInstance().getDateYear(col);
  }
  /* (non-Javadoc)
   * @see hr.restart.baza.Dialect#getDateMonth(java.lang.String)
   */
  protected String getDateMonth(String col) {
    return DefaultDialect.getInstance().getDateMonth(col);
  }
  /* (non-Javadoc)
   * @see hr.restart.baza.Dialect#getDateDay(java.lang.String)
   */
  protected String getDateDay(String col) {
    return DefaultDialect.getInstance().getDateDay(col);
  }
  
  protected Properties getDialectConnectionProperties() {
    Properties prop = new Properties();
    prop.setProperty("charSet", "Cp1250");
    return prop;
  }
  
  /* (non-Javadoc)
   * @see hr.restart.baza.Dialect#getServerTime()
   */
  protected String getServerTime() {
    return DefaultDialect.getInstance().getServerTime();
  }
  public static Dialect getInstance() {
    return inst;
  }
}
