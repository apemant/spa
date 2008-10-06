/****license*****************************************************************
**   file: textDataBinder.java
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
package hr.restart.swing;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class textDataBinder extends com.borland.dbswing.DBTextDataBinder {
  public textDataBinder() {
    super();
  }
  public textDataBinder(javax.swing.text.JTextComponent textComponent) {
    super(textComponent);
  }
  public void keyPressed(java.awt.event.KeyEvent e) {
  };
}
/*
import com.borland.dx.dataset.*;
import com.borland.dx.text.*;
import com.borland.jb.util.StringArrayResourceBundle;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.*;

public class textDataBinder
    implements DocumentListener,
    FocusListener, MouseListener,
    KeyListener,
    AccessListener,
    DataChangeListener,
    NavigationListener,
    UndoableEditListener,
    PropertyChangeListener,
//    HyperlinkListener,
    ColumnAware,
    Designable,
    Serializable {
  public textDataBinder() {
  }
  public textDataBinder(JTextComponent t) {
  }
////koristi se
  public void setPostOnFocusLost(boolean b) {
  }
  public boolean isTextModified() {
    return true;
  }
  public void postText2() {
  }
  public void postText() {
  }
  public void updateText() {
  }
////
/////// INTERFACEI
    public void insertUpdate(DocumentEvent e) {
    }

    public void removeUpdate(DocumentEvent e) {
    }

    public void changedUpdate(DocumentEvent e) {
    }
    public void focusLost(FocusEvent e) {
    }
    public void focusGained(FocusEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e)  {
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent e)  {
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
    }
    public void accessChange(AccessEvent event) {
    }
    public void dataChanged(DataChangeEvent event) {
    }
    public void postRow(DataChangeEvent event) {
    }
    public void navigated(NavigationEvent event) {
    }
    public void undoableEditHappened(UndoableEditEvent e) {
    }
    public void propertyChange(PropertyChangeEvent e) {
    }
    private String colName;
    private DataSet dataSet;
    public void setColumnName(String columnName) {
      colName = columnName;
    }
    public String getColumnName() {
      return colName;
    }
    public void setDataSet(DataSet dSet) {
      dataSet = dSet;
//      dataSet.open();
    }
    public DataSet getDataSet() {
      return dataSet;
    }
/////////////////////////////
}
*/