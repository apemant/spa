/****license*****************************************************************
**   file: raCalculator.java
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
 * Created on 2005.07.11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.swing;

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;



/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class raCalculator extends JFrame {
  private static raCalculator inst = new raCalculator();
  JraTextField result = new JraTextField();
  JraTextField entry = new JraTextField();
  StorageDataSet data = new StorageDataSet();
  private int d = 2;
  private BigDecimal sto = new BigDecimal(100);
  
  private void setHorizontalSizes(JraTextField field, int min, int pref, int max, int vert) {
    field.setMinimumSize(new Dimension(min, vert));
    field.setPreferredSize(new Dimension(pref, vert));
    field.setMaximumSize(new Dimension(max, vert));
  }
  
  private raCalculator() {
    try {
      data.setColumns(new Column[] {
          dM.createBigDecimalColumn("RESULT", 2),
          dM.createBigDecimalColumn("ENTRY", "Broj", 2)
      });
      data.open();
      data.insertRow(false);
      result.setDataSet(data);
      result.setColumnName("RESULT");
      result.setFont(result.getFont().deriveFont((float) (result.getFont().getSize2D() * 1.2)));
      entry.setDataSet(data);
      entry.setColumnName("ENTRY");
      new raNumberMask(result, 2);
      new CalcMask(entry);
      
      raCommonClass.getraCommonClass().setLabelLaF(result, false);
      
      setHorizontalSizes(result, 75, 150, 450, 25);
      setHorizontalSizes(entry, 75, 150, 450, 21);
      
      JPanel resultPanel = new JPanel(new BorderLayout());
      resultPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      resultPanel.add(result);
      
      JPanel fieldPanel = new JPanel();
      fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
      fieldPanel.add(resultPanel);
      fieldPanel.add(Box.createVerticalStrut(5));
      fieldPanel.add(entry);
      fieldPanel.add(Box.createVerticalStrut(10));
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(fieldPanel, BorderLayout.NORTH);
      getContentPane().add(createButtonPanel());
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      setTitle("Kalkulator");
      pack();
      AWTKeyboard.registerKeyStroke(getContentPane(), AWTKeyboard.ESC, new KeyAction() {
        public boolean actionPerformed() {
          setVisible(false);
          return true;
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private JPanel createButtonPanel() {
    JPanel pan = new JPanel(new GridLayout(4, 5, 3, 3));
    String[] butts = {"1", "2", "3", "/", "%-", "4", "5", "6", "*", "%",
        "7", "8", "9", "-", "-%", "0", ",", "C", "+", "+%"};
    for (int i = 0; i < butts.length; i++)
      pan.add(new CalcButton(butts[i]));

    return pan;
  }
  
  public static raCalculator getInstance() {
    return inst;
  }
  
  public int getPrecision() {
    return d;
  }
  
  void buttonPressed(String text) {
    if (Aus.isDigit(text) || text.equals(",") || text.equals("=") || text.equals("C") ||
        text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/")) {
      entry.getFieldMask().cacheDynamicVariables();
      entry.getFieldMask().keypressCharacter(text.charAt(0));
    } else {
      BigDecimal num = ((CalcMask) entry.getFieldMask()).getNumericValue();
      if (num.signum() <= 0) {
        data.setBigDecimal("ENTRY", num);
        entry.selectAll();
        return;
      }
      
      if (text.equals("%"))
        data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").
            multiply(num).divide(sto, d, BigDecimal.ROUND_HALF_UP));
      else if (text.equals("-%"))
        data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").
            subtract(data.getBigDecimal("RESULT").multiply(num).
            divide(sto, d, BigDecimal.ROUND_HALF_UP)));
      else if (text.equals("+%"))
        data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").
            add(data.getBigDecimal("RESULT").multiply(num).
            divide(sto, d, BigDecimal.ROUND_HALF_UP)));
      else if (text.equals("%-"))
        data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").
            multiply(sto).divide(num.add(sto), d, BigDecimal.ROUND_HALF_UP));
      else return;
      result.copy();
      raNumberMask.normalizeClipboardNumber();
      data.setBigDecimal("ENTRY", num);
      entry.selectAll();
    }
  }
  
  void performCalc(BigDecimal num, char op) {
    if (op == '=')
      data.setBigDecimal("RESULT", num);
    else if (op == 'C')
      data.setBigDecimal("RESULT", new BigDecimal(0));
    else if (op == '+')
      data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").add(num));
    else if (op == '-')
      data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").subtract(num));
    else if (op == '*')
      data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").
          multiply(num).setScale(d, BigDecimal.ROUND_HALF_UP));
    else if (op == '/')
      if (num.signum() == 0) data.setBigDecimal("RESULT", new BigDecimal(0));
      else data.setBigDecimal("RESULT", data.getBigDecimal("RESULT").
          divide(num, d, BigDecimal.ROUND_HALF_UP));
    result.copy();
    raNumberMask.normalizeClipboardNumber();
  }
  
  class CalcMask extends raNumberMask {
    
    public CalcMask(JTextField jtf) {
      super(jtf, 2);
    }
    
    BigDecimal getNumericValue() {
      try {
        return new BigDecimal(nf.parse(tf.getText()).doubleValue()).
                   setScale(decs, BigDecimal.ROUND_HALF_UP);
      } catch (Exception e) {
        return null;
      }
    }
    
    public boolean keypressCode(int code) {
      if (code == KeyEvent.VK_SPACE) keypressCharacter('=');
      return false;
    }
    
    public boolean keypressCharacter(char ch) {
      if (ch != '+' && ch != '-' && ch != '*' && ch != '/' && ch != '=')
        return super.keypressCharacter(ch);
      
      BigDecimal num = getNumericValue();
      if (num == null && ch != '=' && ch != 'C') return super.keypressCharacter(ch);
      performCalc(num, ch);
      data.setBigDecimal("ENTRY", num);
      entry.selectAll();
      return true;
    }
  }
  
  class CalcButton extends JraButton {
    public CalcButton(String text) {
      setText(text);
      addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          buttonPressed(getText());
        }
      });
    }
    
  }
}
