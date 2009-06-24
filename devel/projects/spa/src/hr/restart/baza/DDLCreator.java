/****license*****************************************************************
**   file: DDLCreator.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import hr.restart.util.VarStr;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/*class MyString {
  StringBuffer s;
  public MyString() {
    s = new StringBuffer();
  }
  public MyString(String _init) {
    s = new StringBuffer(_init);
  }
  public void replace(String _orig, String _new) {
    int offset;
    while ((offset = s.toString().indexOf(_orig)) != -1)
      s.replace(offset, offset + _orig.length(), _new);
  }
  public void append(MyString _new) {
    if (s.length() > 0)
      s.append(", ");
    s.append(_new);
  }
  public void append(String _new) {
    s.append(_new.toString());
  }
  public void erase() {
    s.setLength(0);
  }
  public String toString() {
    return s.toString();
  }
}*/

/**
 * Klasa DDLCreator služi za stvaranje SQL naredbi za definicije tabele i indeksa
 * ovisno o bazi.
 */

public class DDLCreator {
/*  private static String databaseType = "DEFAULT";
  private static String driverName = hr.restart.baza.dM.driverName; */
  private static int numIdx;
/*  private ResourceBundle databaseInfo; */
  private String tableName;
  private HashSet fields;
  private VarStr fieldsList;
  private String[] pkey;

  public DDLCreator() {
/*    driverName = hr.restart.baza.dM.driverName;
    if (driverName.equals("interbase.interclient.Driver"))
      databaseType = "INTERBASE";
    else if (driverName.equals("com.microsoft.jdbc.sqlserver.SQLServerDriver"))
      databaseType = "SQLServer";
    else if (driverName.equals("org.postgresql.Driver"))
      databaseType = "postgre";
    else if (driverName.equals("com.mysql.jdbc.Driver"))
      databaseType = "mysql";
    else
      databaseType = "DEFAULT"; */
    init();
  }
/*  public DDLCreator(String _databaseType) {
    databaseType = _databaseType;
    init();
  } */
  private void init() {
    try {
/*      databaseInfo = ResourceBundle.getBundle("hr.restart.baza.Res_" + databaseType.toLowerCase());*/
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
  private Collection getArguments(String _expr) {
    Collection list = new LinkedList();
    int comma, begin = 0;
    while ((comma = _expr.indexOf(',', begin)) >= 0) {
      list.add(_expr.substring(begin, comma));
      begin = comma + 1;
    }
    list.add(_expr.substring(begin));
    return list;
  }
  public DDLCreator create(String _name) {
    tableName = new String(_name);
    fields = new HashSet();
    fieldsList = new VarStr();
    numIdx = 0;
    return this;
  }
//  public String getSystemTimeString() {
//    return databaseInfo.getString("GET-SYSDAT");
//  }
  public DDLCreator addDate(String _name, boolean _not_null) {
    fields.add(_name);
    fieldsList.append(_name).append(" ").
      append(dateComm(_not_null)).append(", ");
    return this;
  }
  public DDLCreator addDate(String _name) {
    return addDate(_name, false);
  }
  
  public DDLCreator addBlob(String _name, boolean _not_null) {
    fields.add(_name);
    fieldsList.append(_name).append(" ").
      append(blobComm(_not_null)).append(", ");
    return this;
  }
  
  public DDLCreator addBlob(String _name) {
    return addBlob(_name, false);
  }
  
  public String dateComm(boolean _not_null) {
    VarStr field = new VarStr(Dialect.getDDL("DATE"));
    if (_not_null == true) field.append(" NOT NULL");
    return field.toString();
  }
  
  public String blobComm(boolean _not_null) {
    VarStr field = new VarStr(Dialect.getDDL("BLOB"));
    if (_not_null == true) field.append(" NOT NULL");
    return field.toString();
  }
  
  public String stringComm(int _size, String _default, boolean _not_null) {
    VarStr field = new VarStr(Dialect.getDDL("CHAR"))
        .append(" ").append(Dialect.getDDL("EXTCHAR"));
    field.replace("%SIZE", Integer.toString(_size));
    if (_default!=null && _default.length() > 0) {
      field.append(" ").append(Dialect.getDDL("DEFAULT"));
      field.replace("%VALUE", _default);
    }
    if (_not_null == true) field.append(" NOT NULL");
    return field.toString();
  }
  
  public String floatComm(int _size, int _precision, boolean _not_null) {
    VarStr field = new VarStr(Dialect.getDDL("FLOAT"));
    field.replace("%SIZE", Integer.toString(_size))
         .replace("%PRECISION", Integer.toString(_precision));
    if (_not_null == true) field.append(" NOT NULL");
    return field.toString();
  }
  
  public String intComm(String type, int _size, boolean _not_null) {
    VarStr field = new VarStr(Dialect.getDDL(type));
    field.replace("%SIZE", Integer.toString(_size));
    if (_not_null == true) field.append(" NOT NULL");
    return field.toString();
  }

  public DDLCreator addChar(String _name, int _size, String _default, boolean _not_null) {
    fields.add(_name);
    fieldsList.append(_name).append(" ").
      append(stringComm(_size, _default, _not_null)).append(", ");
    return this;
  }
  public DDLCreator addChar(String _name, int _size) {
    return addChar(_name, _size, "", false);
  }
  public DDLCreator addChar(String _name, int _size, String _default) {
    return addChar(_name, _size, _default, false);
  }
  public DDLCreator addChar(String _name, int _size, boolean _not_null) {
    return addChar(_name, _size, "", _not_null);
  }
  private DDLCreator addInt(String type, String _name, int _size, boolean _not_null) {
    fields.add(_name);
    fieldsList.append(_name).append(" ").
      append(intComm(type, _size, _not_null)).append(", ");
    return this;
  }
  public DDLCreator addInteger(String _name, int _size, boolean _not_null) {
    return addInt("INTEGER", _name, _size, _not_null);
  }
  public DDLCreator addInteger(String _name, int _size) {
    return addInt("INTEGER", _name, _size, false);
  }
  public DDLCreator addShort(String _name, int _size, boolean _not_null) {
    return addInt("SHORT", _name, _size, _not_null);
  }
  public DDLCreator addShort(String _name, int _size) {
    return addInt("SHORT", _name, _size, false);
  }
  public DDLCreator addFloat(String _name, int _size, int _precision, boolean _not_null) {
    fields.add(_name);
    fieldsList.append(_name).append(" ").
      append(floatComm(_size, _precision, _not_null)).append(", ");
    return this;
  }
  public DDLCreator addFloat(String _name, int _size, int _precision) {
    return addFloat(_name, _size, _precision, false);
  }
  public DDLCreator addPrimaryKey(String _fields) {
    if (!fields.containsAll(getArguments(_fields)))
      throw new NoSuchElementException("Invalid Primary Key (" + _fields + ")");
    VarStr field = new VarStr(Dialect.getDDL("PKEY"));
    field.replace("%FIELD", _fields);
    fieldsList.append(field).append(", ");
    pkey = new VarStr(_fields).splitTrimmed(',');
    for (int i = 0; i < pkey.length; i++)
      pkey[i] = pkey[i].toUpperCase();
    return this;
  }
  public String[] getPrimaryKey() {
    return pkey;
  }
  public String getCreateTableString() {
    VarStr outputText = new VarStr(Dialect.getDDL("CREATE"));
    outputText.replace("%NAME", tableName)
              .replace("%BODY", fieldsList.chop(2).toString());
    return outputText.toString();
  }
  private String getIndexName(String _fields, String _prefix) {
    if (getArguments(_fields).size() > 1) {
      ++numIdx;
      return _prefix + tableName + "key" + numIdx;
    } else
      return _prefix + tableName + _fields;
  }
  private String getIndex(String _fields, String _prefix, String _key) {
    if (fields.containsAll(getArguments(_fields)) == false)
      throw new NoSuchElementException("Invalid index (" + _fields + ")");
    VarStr index = new VarStr(Dialect.getDDL(_key));
    index.replace("%NAME", getIndexName(_fields, _prefix))
         .replace("%TABLE", tableName)
         .replace("%FIELD", _fields);
    return index.toString();
  }
  public String getIndex(String _fields) {
    return getIndex(_fields, "idx", "INDEX");
  }
  public String getUniqueIndex(String _fields) {
    return getIndex(_fields, "uidx", "UINDEX");
  }
  public String[] getIndices(String[] _fieldslist, String[] _ufieldslist) {
    String[] indexList = new String[_fieldslist.length + _ufieldslist.length];
    numIdx = 0;
    for (int i = 0; i < _fieldslist.length; i++)
      indexList[i] = getIndex(_fieldslist[i]);
    for (int i = 0; i < _ufieldslist.length; i++)
      indexList[i + _fieldslist.length] = getUniqueIndex(_ufieldslist[i]);
    return indexList;
  }

  public String[] getIndexNames(String[] _fieldslist, String[] _ufieldslist) {
    String[] indexNameList = new String[_fieldslist.length + _ufieldslist.length];
    numIdx = 0;
    for (int i = 0; i < _fieldslist.length; i++)
      indexNameList[i] = getIndexName(_fieldslist[i], "idx");
    for (int i = 0; i < _ufieldslist.length; i++)
      indexNameList[i + _fieldslist.length] = getIndexName(_ufieldslist[i], "uidx");
    return indexNameList;
  }
  
  public void dispose() {
    if (fields != null) fields.clear();
    fieldsList = null;
    tableName = null;
    pkey = null;
  }
}
