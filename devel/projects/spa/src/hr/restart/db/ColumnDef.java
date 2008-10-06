/****license*****************************************************************
**   file: ColumnDef.java
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
package hr.restart.db;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ColumnDef {
  private String caption;
  private String columnName;
  private int sqlType;
  private String mask;
  private String defaultValue;
  private int precision;
  private int scale;
  private int width;
  private boolean visible;

  public ColumnDef() {
  }

  public String getCaption() {
    return caption;
  }
  public void setCaption(String newCaption) {
    caption = newCaption;
  }
  public void setColumnName(String newColumnName) {
    columnName = newColumnName;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setSqlType(int newSqlType) {
    sqlType = newSqlType;
  }
  public int getSqlType() {
    return sqlType;
  }
  public void setMask(String newMask) {
    mask = newMask;
  }
  public String getMask() {
    return mask;
  }
  public void setDefaultValue(String newDefaultValue) {
    defaultValue = newDefaultValue;
  }
  public String getDefaultValue() {
    return defaultValue;
  }
  public void setPrecision(int newPrecision) {
    precision = newPrecision;
  }
  public int getPrecision() {
    return precision;
  }
  public void setScale(int newScale) {
    scale = newScale;
  }
  public int getScale() {
    return scale;
  }
  public void setWidth(int newWidth) {
    width = newWidth;
  }
  public int getWidth() {
    return width;
  }
  public void setVisible(boolean newVisible) {
    visible = newVisible;
  }
  public boolean isVisible() {
    return visible;
  }

  public boolean equals(ColumnDef col) {
    return col.getColumnName().equalsIgnoreCase(getColumnName());
  }
}