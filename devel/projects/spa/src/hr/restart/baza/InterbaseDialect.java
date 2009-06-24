/****license*****************************************************************
**   file: InterbaseDialect.java
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

public class InterbaseDialect extends Dialect {

  private static Dialect inst = new InterbaseDialect();

  protected InterbaseDialect() {
    setDDLMap(new String[][] {
      { "CREATE", "create table %NAME (%BODY)" },
      { "CHAR", "char(%SIZE)" },
      { "EXTCHAR", "character set win1250" },
      { "INTEGER", "numeric(%SIZE,0)" },
      { "SHORT", "numeric(%SIZE,0)" },
      { "FLOAT", "numeric(%SIZE,%PRECISION)" },
      { "DEFAULT", "default '%VALUE'" },
      { "DATE", "date" },
      { "BLOB", "blob" },
      { "PKEY", "Primary Key (%FIELD)" },
      
      { "ADD-COLUMN", "alter table %NAME add %COLUMN %TYPE" },
      { "ALTER-COLUMN", "alter table %NAME alter %COLUMN type %TYPE" },
      { "DROP-COLUMN", "alter table %NAME drop %COLUMN" },

      { "INDEX", "create index %NAME on %TABLE (%FIELD)" },
      { "UINDEX", "create unique index %NAME on %TABLE (%FIELD)" }
    });
  }

  public static Dialect getInstance() {
    return inst;
  }

  public String getDateYear(String col) {
    return "EXTRACT(YEAR FROM "+col+")";
  }

  public String getDateMonth(String col) {
    return "EXTRACT(MONTH FROM "+col+")";
  }

  public String getDateDay(String col) {
    return "EXTRACT(DAY FROM "+col+")";
  }
  
  protected Properties getDialectConnectionProperties() {
    Properties prop = new Properties();
    prop.setProperty("charSet", "Cp1250");
    return prop;
  }

  public String getServerTime() {
    return "SELECT CURRENT_TIMESTAMP FROM RDB$DATABASE";
  }
}

/*
 * Message from daniel@rest-art at 15:38:52
    CREATE TABLE ra_log
 (
 log_id integer not null,
 log_ts timestamp,
 log_sql varchar(2000),
 PRIMARY KEY (log_id)
 );
 Message from daniel@rest-art at 15:39:02
    CREATE GENERATOR log_id;
   */
