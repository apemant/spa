/****license*****************************************************************
**   file: JraTextField.java
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

import hr.restart.baza.Condition;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.FocusManager;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.Document;

import com.borland.dbswing.DBPlainDocument;
import com.borland.dx.dataset.ColumnAware;
import com.borland.dx.dataset.DataSet;


public class JraTextField extends JTextField  implements ColumnAware, Serializable {
//  hr.restart.util.sysoutTEST ST=new hr.restart.util.sysoutTEST(true);
  String txtBefore=getText();
  JraTextField this_JraTextField;
  boolean exceptionThrown = false;
  private String helpText=null;
  private javax.swing.ImageIcon helpImage=null;
  private String errText=null;
  private javax.swing.ImageIcon errImage=null;
  private Color defBackground = getBackground();
  private Color defForeground = getForeground();
  private boolean selectAllOnFocusGained = true;
  private boolean disableEnter = true;
  private String maskCheckString = null;
  public static JraTextField currentFocus = null; 
  
  protected boolean isSpecialPopUpAllowed(){
  	return false;
  }
  private raFieldMask mask;  // ante

  com.borland.dx.dataset.Variant varValue = new com.borland.dx.dataset.Variant();
    public JraTextField()
    {
        this(null, null, 0);
    }

    public JraTextField(String text)
    {
        this(null, text, 0);
    }

    public JraTextField(int columns)
    {
        this(null, null, columns);
    }

    public JraTextField(String text, int columns)
    {
        this(null, text, columns);
    }

    public JraTextField(Document doc, String text, int columns)
    {
        super(doc, text, columns);
        commonInit();
        this_JraTextField = this;
    }

    protected void commonInit()
    {
        //Insets margin = getMargin();
        //setMargin(new Insets(margin.top, margin.left + 2, margin.bottom, margin.right + 2));
//        setFont(getFont().deriveFont(java.awt.Font.BOLD));
/*
        dataBinder = new DBTextDataBinder(this) {
          public void keyPressed(KeyEvent e) {};
        };
*/
      dataBinder = new textDataBinder(this);
//RA
      dataBinder.setPostOnFocusLost(false);
      dataBinder.setPostOnRowPosted(false);
      
      addFocusListener(new JraTextField_FocusListener());
      setInputVerifier(new InputVerifier() {      
        public boolean verify(JComponent input) {
          if (JraTextField.this.isEnabled()) maskCheck();
          return true;
        }
      });
      /*addKeyListener(new JraTextField_KeyListener());
      addKeyListener(new hr.restart.swing.JraKeyListener());*/
      setPreferredSize(new java.awt.Dimension(100,21));
      raDisabledPopup.installFor(this);
      this.setEnablePopupMenu(false);
      installTouchKeyboard();
    }
    public void installTouchKeyboard() {
      try {
        Class clz = Class.forName("hr.restart.spapos.touch.TouchKeyboardPopup");
        clz.getMethod("installFor", new Class[] {JraTextField.class}).invoke(this, new Object[] {this});
        System.out.println("TouchKeyboardPopup installed");
      } catch (Exception e) {
//        System.out.println(e);
      }
    }

    public void setDataSet(DataSet dataSet)
    {
        dataBinder.setDataSet(dataSet);
        setRaPopup();

        // ante
        if (dataSet == null) {
          if (mask != null) mask.uninstall();
          mask = null;
        } else setFieldMask();
        // ante
    }

    public DataSet getDataSet()
    {
        return dataBinder.getDataSet();
    }

    public void setColumnName(String columnName)
    {
    	if (dataBinder.getDataSet() != null && 
    			dataBinder.getDataSet().hasColumn(columnName) != null &&
    			dataBinder.getDataSet().hasColumn(columnName).getPrecision() <= 0 &&
    			getDocument() instanceof DBPlainDocument)
    		    ((DBPlainDocument) getDocument()).setMaxLength(32768);
        dataBinder.setColumnName(columnName);
        setRaPopup();
        setFieldMask(); // ante
    }
    
  public Condition getCondition() {
    if (getText().length() == 0) return Condition.none;
    if (getDataSet() == null)
      return Condition.equal(getColumnName(), getText());
    return Condition.equal(getColumnName(), getDataSet());
  }
    
  public Condition getConditionQ() {
    return getCondition().qualified(getDataSet().getColumn(0).getTableName());
  }

    public String getColumnName()
    {
        return dataBinder.getColumnName();
    }

    public void setEnablePopupMenu(boolean popupEnabled)
    {
        dataBinder.setEnablePopupMenu(popupEnabled);
    }

    public boolean isEnablePopupMenu()
    {
        return dataBinder.isEnablePopupMenu();
    }

    public void setPostOnFocusLost(boolean postOnFocusLost)
    {
        dataBinder.setPostOnFocusLost(postOnFocusLost);
    }

    public boolean isPostOnFocusLost()
    {
        return dataBinder.isPostOnFocusLost();
    }

    public void setPostOnRowPosted(boolean postOnRowPosted)
    {
        dataBinder.setPostOnRowPosted(postOnRowPosted);
    }

    public boolean isPostOnRowPosted()
    {
        return dataBinder.isPostOnRowPosted();
    }

    public void setEnableClearAll(boolean enableClearAll)
    {
        dataBinder.setEnableClearAll(enableClearAll);
    }

    public boolean isEmpty() {
      return getDocument().getLength() == 0;
    }
    
    public boolean isClear() {
      return getText().trim().length() == 0;
    }
    
    public boolean isEnableClearAll()
    {
        return dataBinder.isEnableClearAll();
    }

    public void setEnableUndoRedo(boolean enableUndoRedo)
    {
        dataBinder.setEnableUndoRedo(enableUndoRedo);
    }

    public boolean isEnableUndoRedo()
    {
        return dataBinder.isEnableUndoRedo();
    }

    public void setNextFocusOnEnter(boolean nextFocusOnEnter)
    {
        dataBinder.setNextFocusOnEnter(nextFocusOnEnter);
    }

    public boolean isNextFocusOnEnter()
    {
        return dataBinder.isNextFocusOnEnter();
    }
/*-
    public DBTextDataBinder getDataBinder()
    {
        return dataBinder;
    }
-*/
    public textDataBinder getDataBinder()
    {
        return dataBinder;
    }
//    protected DBTextDataBinder dataBinder;
    protected textDataBinder dataBinder;

//RA
    boolean ignoreNextHelpMessage = false;
  public void focusGained(java.awt.event.FocusEvent e) {
    if (ignoreNextHelpMessage) ignoreNextHelpMessage = false;
    else if (getDataSet()==null)
      MsgHandler.createHelpText(helpText,helpImage,null);
    else
      MsgHandler.createHelpText(helpText,helpImage,getDataSet().hasColumn(getColumnName()));

      if (isSelectAllOnFocusGained()) selectAll();

    if (!exceptionThrown) {
      if (doScrollToVisible()) scrollToVisible(this);
      txtBefore = getText();
    }
    maskCheckString = null;
    currentFocus = this;
    if (mask != null) mask.focusGained(e);
  }


  /** Da li da izvrsi selectAll na focusGained ili ne
   * @param b ako je true pri svakom ulasku u JraTextField zove se selectAll(), default je true
   */
  public void setSelectAllOnFocusGained(boolean b) {
    selectAllOnFocusGained = b;
  }
  
  public void setEnterDisabled(boolean disable) {
    disableEnter = disable;
  }

  /** Da li da izvrsi selectAll na focusGained ili ne
   * @return ako je true pri svakom ulasku u JraTextField zove se selectAll(), default je true
   */
  public boolean isSelectAllOnFocusGained() {
    return selectAllOnFocusGained;
  }
  /** da li da JraTextField radi scrollToVisible
   * @return true po defaultu
   */
  public boolean doScrollToVisible() {
    return true;
  }

  /** Ako se zadana komponenta nalazi u nekom scrollajucem panelu pomice panel
   * kako bi ista bila vidljiva
   * @param comp komponenta koja bi trebala biti vidljiva
   */
  public static void scrollToVisible(java.awt.Component comp) {
    if (comp.getParent() instanceof javax.swing.JComponent) ((javax.swing.JComponent)comp.getParent()).scrollRectToVisible(comp.getBounds());
  }
  
  public void setText(String txt) {
    maskCheckString = null;
    super.setText(txt);
  }
  
  public void focusLost(FocusEvent e) {
    if (currentFocus == this) currentFocus = null;
    if (mask != null) mask.focusLost(e);
    if (maskCheck() && (e== null || !e.isTemporary())) select(0, 0);
  }

  void validateText() throws Exception {
    if (!dataBinder.isTextModified()) {
      return;
    }
    if (getText().equals("")) {
      return;
    }
    com.borland.dx.dataset.Column col = getDataSet().getColumn(getColumnName());
    if (Valid.getValid().isNumeric(col)) {
      if (isLetters(getText())) throw new java.lang.NumberFormatException();
    }
    col.getFormatter().parse(getText(),varValue);
    col.validate(getDataSet(),varValue);
  }
  
  boolean isLetters(String str) {
    char[] chrs = str.toCharArray();
    for (int i=0;i<chrs.length;i++) {
      if (Character.isLetter(chrs[i])) return true;
    }
    return false;
  }

  // ante
  public void setErrorColors() {
    setBackground(java.awt.Color.red);
    setForeground(java.awt.Color.white);
  }

  public void setNormalColors() {
    setBackground(defBackground);
    setForeground(defForeground);
  }
  // ante

/**
 * Kontrola unosa. Okida se na focusLost o vraca false ako nije uspjela
 */
  public boolean maskCheck() {
    if (maskCheckString != null && maskCheckString.equals(getText())) return true;
    return maskCheck_impl();
  }
  
  private boolean maskCheck_impl() {
    try {
//      System.out.println("maskcheck " + getColumnName());
//      validateText();
//      dataBinder.postText2();
      validateWithException();
//      txtBefore=getText();
      setNormalColors();
//      if (getText().length() == 0 && getDataSet() != null) {
//        int dty = getDataSet().getColumn(getColumnName()).getDataType();
//        if (dty == Variant.INT || dty == Variant.SHORT || dty == Variant.LONG)
//          getDataSet().setAssignedNull(getColumnName());
//      }
      exceptionThrown=false;
      maskCheckString = getText();
      if (!ignoreNextHelpMessage) MsgHandler.clearMsgText();
    } catch (Exception e) {
      exceptionThrown = true;
      this_ExceptionHandling(e);
//      System.out.println(e);
    }
    valueChanged();
    return !exceptionThrown;
  }
  
  public void valueChanged() {
    // za overridanje: ulancavanje 
  }
  
  private void validateWithException() throws Exception {
    boolean validat = Validacija();
     if (!validat) throw new Exception();
  }
  public boolean Validacija() {
    try {
      validateText();
      dataBinder.postText2();
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }
  public static boolean navFieldFlag = false;
/**
 * Metoda izvodi ponasanje JraTextFielda kada je unos neispravan:
 * stavlja pozadinu crvenu, slova bijela, requesta fokus u komponentu, vraca prije upisani text i baca poruku
 */
  public void this_ExceptionHandling(Exception e) {

    setErrorColors();
    dataBinder.updateText();
    navFieldFlag = true;
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        ignoreNextHelpMessage = true;
        if (!JraButton.isInsideAction()) requestFocus();        
        if (getDataSet()==null) {
          MsgHandler.createErrText(errText,errImage,null);
        } else {
          MsgHandler.createErrText(errText,errImage,getDataSet().hasColumn(getColumnName()));
        }
        navFieldFlag = false;
      }
    });
  }
/**
 * U varijabli txtBefore cuva se zadnja upisana vrijednost koja je prosla validaciju
 */
  public String getTxtBefore() {
    return txtBefore;
  }
  
  public void setTxtBefore(String txt) {
    txtBefore = txt;
  }
/**
 * vraca !this.getTxtBefore().equals(this.getText())
 */
  public boolean isValueChanged() {
    return !txtBefore.equals(getText());
  }

  public void requestFocusLater() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        requestFocus();
      }
    });
  }
/**
 * HelpText se pojavljuje u srednjem help panelu sa slikom u panel modu
 */
  public void setHelpText(String newHelpText) {
    helpText = newHelpText;
  }
  public String getHelpText() {
    return helpText;
  }
/**
 * Slika koja se pojavljuje u srednjem help panelu za mode raFrame.PANEL
 */
  public void setHelpImage(javax.swing.ImageIcon newHelpImage) {
    helpImage = newHelpImage;
  }
  public javax.swing.ImageIcon getHelpImage() {
    return helpImage;
  }
/**
 * ErrText se pojavljuje u srednjem help panelu sa slikom u panel modu ili u status baru kada je unos neispravan
 */
  public void setErrText(String newErrText) {
    errText = newErrText;
  }
  public String getErrText() {
    return errText;
  }
/**
 * Slika koja se pojavljuje u srednjem help panelu za mode raFrame.PANEL kada je unos neispravan
 */
  public void setErrImage(javax.swing.ImageIcon newErrImage) {
    errImage = newErrImage;
  }
  public javax.swing.ImageIcon getErrImage() {
    return errImage;
  }
//popupmenu sa ctrl tipkama za datume etc..
  javax.swing.JPopupMenu raPopup = null;
  public javax.swing.JPopupMenu getRaPopup() {
    return raPopup;
  }

  // ante
  public raFieldMask getFieldMask() {
    return mask;
  }

  public void removeFieldMask() {
    if (mask != null) mask.uninstall();
    mask = null;
  }

  void setFieldMask(raFieldMask mask) {
    removeFieldMask();
    this.mask = mask;
  }
  
  public void setProtected(boolean prot) {
    if (mask != null) mask.setProtected(prot);
  }

  private void setFieldMask() {
    if (getDataSet() == null || getColumnName() == null) return;
    com.borland.dx.dataset.Column col = getDataSet().hasColumn(getColumnName());
    String ddm;
    if (col == null) return;
    if (col.getDataType() == com.borland.dx.dataset.Variant.TIMESTAMP) {
      if (mask != null && !(mask instanceof raDateMask)) mask.uninstall();
      if (mask == null || !(mask instanceof raDateMask))
        if ((ddm = col.getDisplayMask()) != null &&
            ddm.length() >= 10 && ddm.charAt(2) == ddm.charAt(5) &&
            "dd".equalsIgnoreCase(ddm.substring(0,2)) &&
            "mm".equalsIgnoreCase(ddm.substring(3,5)) &&
            "yyyy".equalsIgnoreCase(ddm.substring(6,10))) {
          mask = new raDateMask(this);
          ((raDateMask) mask).setMaskCharacters('_', ddm.charAt(2));
        }
      return;
    }
    if (col.getDataType() == com.borland.dx.dataset.Variant.BIGDECIMAL) {
      if (mask != null) mask.uninstall();
      mask = Aus.installNumberMask(this, col.getScale());
      return;
    }
    if (col.getDataType() == com.borland.dx.dataset.Variant.INT) {
      if (mask != null && !(mask instanceof raTextMask)) mask.uninstall();
      if (mask == null || !(mask instanceof raTextMask))
        mask = new raTextMask(this, 9, false, raTextMask.NUMERIC);
      return;
    }
    if (col.getDataType() == com.borland.dx.dataset.Variant.SHORT) {
      if (mask != null && !(mask instanceof raTextMask)) mask.uninstall();
      if (mask == null || !(mask instanceof raTextMask))
        mask = new raTextMask(this, 4, false, raTextMask.NUMERIC);
      return;
    }
  }
  // ante

  protected void setRaPopup() {
    if (getDataSet() == null || getColumnName() == null) return;
    com.borland.dx.dataset.Column col = getDataSet().hasColumn(getColumnName());
    if (col == null) return;
    if (col.getSqlType() == java.sql.Types.TIMESTAMP) {
      if (raPopup != null) {
        if (raPopup instanceof raDatePopup) return;
      } else removeRaPopup();
      raPopup = new raDatePopup(this);
    } else removeRaPopup();
  }
  public void removeRaPopup() {
    if (raPopup == null) return;
    raPopup.removeAll();
    raPopup = null;
  }
  /**
   * Focus Listener
   */
  class JraTextField_FocusListener extends java.awt.event.FocusAdapter {
    public void focusGained(java.awt.event.FocusEvent e) {
      this_JraTextField.focusGained(e);
    }
    public void focusLost(java.awt.event.FocusEvent e) {
      if (this_JraTextField.isEnabled())
        this_JraTextField.focusLost(e);
    }
  }
  /**
   * Key listener
   */
  class JraTextField_KeyListener extends java.awt.event.KeyAdapter {
    public void keyReleased(KeyEvent e) {
      if  (e.getKeyCode()==e.VK_ENTER) {
        try {
//        this_JraTextField.validateText();
//        validateWithException();
          maskCheck();  
        } catch (Exception ex){
          e.consume();
        }
        //JraTextField.this.maskCheck();
        if (!e.isConsumed()) {
          FocusManager.getCurrentManager().focusNextComponent(JraTextField.this);
          e.consume();
        }
      }    
    }
    public void keyPressed(KeyEvent e) {
    	if  (e.getKeyCode()==e.VK_F4) {
    		try {
    			copyPrevious();
    			e.consume();
    		} catch (Exception ex){
    			ex.printStackTrace();
    		}
    	} else if  (e.getKeyCode()==e.VK_F10) {
          try {
//            this_JraTextField.validateText();
//            validateWithException();
            maskCheck();
          } catch (Exception ex){
            e.consume();
          }
          this_JraTextField.maskCheck();
      } else if  (e.getKeyCode()==e.VK_ESCAPE) {
        if (raDisabledPopup.hideInstance()) {
          e.consume();
        }
        if (raPopup instanceof raDatePopup && raPopup.isVisible()) {
          raPopup.setVisible(false);
          e.consume();
        }
        if (isSpecialPopUpAllowed() && raPopup.isVisible()) {
            raPopup.setVisible(false);
            e.consume();
          }        
        if (!this_JraTextField.maskCheck()) {
          this_JraTextField.dataBinder.updateText();
          e.consume();
        }
      } else if (e.getKeyCode()==e.VK_PAGE_DOWN) e.consume();
      else if (e.getKeyCode()==e.VK_PAGE_UP) e.consume();
      else if (e.getKeyCode()==e.VK_DOWN) e.consume();
      else if (e.getKeyCode()==e.VK_UP) e.consume();
      else if (disableEnter && e.getKeyCode()==e.VK_ENTER) e.consume();
/*
      else {
        if (!Character.isLetterOrDigit(e.getKeyChar())) return;
        com.borland.dx.dataset.Column cl = getDataSet().hasColumn(getColumnName());
        if (cl == null) return;
        if (Valid.getValid().isNumeric(cl)) {
          if (!Character.isDigit(e.getKeyChar())) {
            System.out.println("fuj to tipkati");
            e.setKeyCode(e.VK_UNDEFINED);
            e.consume();
          }
        }
      }
*/
    }
  }
  
  void copyPrevious() {
  	Container c = this;
  	while (c != null) {
  		if (c instanceof Window) break;
  		if (c instanceof JraPanel && ((JraPanel) c).getOwner() != null)
  			break;
  		c = c.getParent();
  	}
  	if (c instanceof JraPanel) {
  		raMatPodaci owner = ((JraPanel) c).getOwner();
  		owner.restorePreviousValue(this);
  	}
  }
  
  public void addNotify() {
    super.addNotify();
    AWTKeyboard.registerKeyListener(this, new JraTextField_KeyListener());    
  }
  
  public void removeNotify() {
    super.removeNotify();
    AWTKeyboard.unregisterComponent(this);
  }
}
