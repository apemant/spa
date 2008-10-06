/****license*****************************************************************
**   file: ColumnTypes.java
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
 * Created on Dec 27, 2004
 */
package hr.restart.util.textconv;

import org.apache.log4j.Logger;

/**
 * @author andrej
 * Types of columns in formated text file
 */
public abstract class ColumnTypes {
  private static Logger log = Logger.getLogger(ColumnTypes.class);
  /**
   * If IColumn.setType is set to INTEGER resulting object of IColumn.getValue() should be java.lang.Integer
   */
  public static int INTEGER = 0;
  /**
   * If IColumn.setType is set to TIMESTAMP resulting object of IColumn.getValue() should be java.sql.Timestamp
   */
  public static int TIMESTAMP = 1;
  /**
   * If IColumn.setType is set to BIGDECIMAL resulting object of IColumn.getValue() should be java.math.BigDecimal 
   */
  public static int BIGDECIMAL = 2;
  
  public static int STRING = 4;
  
  public static boolean validateType(int t) throws IllegalArgumentException {
    if (t == INTEGER || t == TIMESTAMP || t == BIGDECIMAL || t == STRING) {
      return true;
    } else {
      throw new IllegalArgumentException("Column type is not valid! Choose one defined in hr.restart.util.textconv.ColumnTypes");
    }
  }
  public static boolean validateType(String t) throws IllegalArgumentException {
    return validateType(getType(t));
  }

  public static int getType(String fieldName) throws IllegalArgumentException {
    try {
      int it = ColumnTypes.class.getField(fieldName).getInt(null);
      if (log.isDebugEnabled()) {
        log.debug("type "+fieldName+" = "+it);
      }
      return it;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }
  
  public static void main(String[] args) {
    log.debug(args[0]+" = "+validateType(args[0]));
  }
}
