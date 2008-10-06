/****license*****************************************************************
**   file: IColumn.java
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

/**
 * @author andrej
 * This interface describes column definition of a line in formated text file
 */
public interface IColumn {
  
  /**
   * Unique id of this column
   * @return unique id of this column
   */
  public String getId();
  /**
   * Sets unique id of this column 
   * @param id
   */
  public void setId(String id);
  
  /**
   * Type of column from java.sql.Types
   * @return
   */
  public int getType();
  /**
   * Sets type of column. One of ColumnTypes
   * @param type
   */
  public void setType(int type);
  
  /**
   * Format of the field (for BIGDECIMAL and TIMESTAMP types)
   * @param format
   */
  public void setFormat(String format);
  /**
   * Format of the field (for BigDecimal, Timestamp etc. types)
   * @return
   */
  public String getFormat();
  
  /**
   * Returns value of parsed column
   * @return
   */
  public Object getValue(); 
  
  /**
   * Parses this column against line content.
   * After this command <code>getValue(String)</code> should return some meaningfull value 
   * @param line line of this column
   * @return success
   */
  public boolean parse(ILine line);
  
  public IColumn cloneColumn();
}
