/****license*****************************************************************
**   file: Db2Dialect.java
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
package hr.restart.baza;

import java.util.Properties;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Db2Dialect extends Dialect {

  private static Dialect inst = new Db2Dialect();

  private Db2Dialect() {
    setDDLMap(new String[][] {
      { "CREATE", "create table %NAME (%BODY)" },
      { "CHAR", "varchar(%SIZE)" },
      { "EXTCHAR", "" },
      { "INTEGER", "integer" },
      { "SHORT", "smallint" },
      { "FLOAT", "numeric(%SIZE,%PRECISION)" },
      { "DEFAULT", "" }, // default '%VALUE'
      { "DATE", "timestamp" },
      { "BLOB", "blob" },
      { "PKEY", "Primary Key (%FIELD)" },
      
      { "ADD-COLUMN", "alter table %NAME add %COLUMN %TYPE" },
      { "ALTER-COLUMN", "alter table %NAME alter %COLUMN type %TYPE" },
      { "DROP-COLUMN", "alter table %NAME drop %COLUMN" },
      
      { "INDEX", "create index %NAME on %TABLE (%FIELD)" },
      { "UINDEX", "create unique index %NAME on %TABLE (%FIELD)" }

//      { "INDEX", "" }, // create index %NAME on %TABLE (%FIELD)
//      { "UINDEX", "" } // create unique index %NAME on %TABLE (%FIELD)
    });
  }

  public static Dialect getInstance() {
    return inst;
  }

  protected String getDateYear(String col) {
    return "YEAR("+col+")";
  }

  protected String getDateMonth(String col) {
    return "MONTH("+col+")";
  }

  protected String getDateDay(String col) {
    return "DAY("+col+")";
  }
  
  protected Properties getDialectConnectionProperties() {
    Properties prop = new Properties();
    prop.setProperty("charSet", "Cp1250");
    return prop;
  }

  protected String getServerTime() {
    return "SELECT current_timestamp FROM sysibm.SYSVERSIONS";
  }
}

