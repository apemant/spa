/****license*****************************************************************
**   file: Dialect.java
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

public abstract class Dialect {

  public static String getYear(String col) {
    return currentDialect.getDateYear(col);
  }

  public static String getMonth(String col) {
    return currentDialect.getDateMonth(col);
  }

  public static String getDay(String col) {
    return currentDialect.getDateDay(col);
  }

  public static String getNow() {
    return currentDialect.getServerTime();
  }

  public static String getDDL(String key) {
    return currentDialect.getDDL_impl(key);
  }
  
  public static Properties getConnectionProperties() {
    return currentDialect.getDialectConnectionProperties();
  }
  
  public static int getSqlType(int dataType) {
    return currentDialect.getSqlDataType(dataType);
  }

  public static void setCurrentDialect(Dialect d) {
    currentDialect = d;
  }

  public static String getCurrentDialectClassName() {
    return currentDialect.getClass().getName();
  }
  
  private static Dialect currentDialect = DefaultDialect.getInstance();

  private String[][] ddl;

  protected Dialect() {}

  protected void setDDLMap(String[][] comms) {
    ddl = comms;
  }

  private String getDDL_impl(String key) {
    for (int i = 0; i < ddl.length; i++)
      if (ddl[i][0].equalsIgnoreCase(key))
        return ddl[i][1];
    throw new IllegalArgumentException("Unknown DDL key '"+key+"'");
  }
  
  protected int getSqlDataType(int dataType) {
    return DefaultDialect.getInstance().getSqlDataType(dataType);
  }

  protected abstract String getDateYear(String col);

  protected abstract String getDateMonth(String col);

  protected abstract String getDateDay(String col);

  protected abstract String getServerTime();
  
  protected abstract Properties getDialectConnectionProperties();
}

