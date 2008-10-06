/****license*****************************************************************
**   file: raCalculatorMask.java
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

import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTextField;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raCalculatorMask extends raNumberMask {
  private int lastOp = Value.UNDEFINED;
  private static boolean active = true;
  private ArrayList memory = new ArrayList();
  private LinkedList stack = new LinkedList();
  private static NumberFormat nf = NumberFormat.getInstance();

  static {
    setActive("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam(
        "sisfun", "calcMask", "N", "Kalkulator na svim decimalnim maskama (D/N)")));
  }

  public raCalculatorMask(JTextField tf) {
    super(tf, 2);
  }
  public raCalculatorMask(JTextField tf, int decimalPlaces) {
    super(tf, decimalPlaces);
  }

  public static boolean isActive() {
    return active;
  }

  public static void setActive(boolean act) {
    active = act;
  }

  private BigDecimal getNumericValue() {
    try {
      return new BigDecimal(nf.parse(tf.getText()).doubleValue()).
                 setScale(decs, BigDecimal.ROUND_HALF_UP);
    } catch (Exception e) {
      return null;
    }
  }

  public boolean keypressBackspace() {
    lastOp = Value.UNDEFINED;
    return super.keypressBackspace();
  }

  public boolean keypressCut() {
    lastOp = Value.UNDEFINED;
    return super.keypressCut();
  }

  public boolean keypressCopy() {
    lastOp = Value.UNDEFINED;
    return super.keypressCopy();
  }

  public boolean keypressPaste() {
    lastOp = Value.UNDEFINED;
    return super.keypressPaste();
  }

  public boolean keypressDelete()  {
    lastOp = Value.UNDEFINED;
    return super.keypressDelete();
  }

  public boolean keypressCharacter(char ch) {
    if (!active) return super.keypressCharacter(ch);
    int op = Value.UNDEFINED;
    int lop = lastOp;
    lastOp = Value.UNDEFINED;
    if (ch == '+') op = Value.PLUS;
    else if (ch == '-') op = Value.MINUS;
    else if (ch == '*') op = Value.MULTIPLY;
    else if (ch == '/') op = Value.DIVIDE;
    if (op == Value.UNDEFINED) return super.keypressCharacter(ch);
    if (op == Value.MINUS && !sel && (cPos == 0 || cPos == 1 && tf.getText().startsWith("-")))
      return super.keypressCharacter(ch);
    BigDecimal num = getNumericValue();
    if (num == null || num.signum() == 0) return super.keypressCharacter(ch);
    lastOp = op;
    if (lop != Value.UNDEFINED && stack.size() > 0) {
      ((Value) stack.getLast()).op = op;
      ((Value) memory.get(memory.size() - 1)).op = op;
    } else {
      Value v = new Value(num, op);
      stack.addLast(v);
      memory.add(v);
    }
    compressStack();
    setText(nf.format(((Value) stack.getLast()).getNumber()));
    normalizeNumber();
    tf.selectAll();
    return true;
  }

  private int getMaxPriority() {
    int max = -1, next = -1;
    for (Iterator i = stack.iterator(); i.hasNext();
         max = Math.max(max, next), next = ((Value) i.next()).getPriority());
    return max;
  }

  private void compressStack() {
    int mp, lp = ((Value) stack.getLast()).getPriority();
    while ((mp = getMaxPriority()) >= lp) {
      Iterator i = stack.iterator();
      Value f = (Value) i.next();
      while (i.hasNext()) {
        Value s = (Value) i.next();
        if (f.getPriority() == mp) {
          f.evaluate(s);
          i.remove();
        } else f = s;
      }
    }
  }

//  public boolean keypressCode(int code) {
//    if (!active) return super.keypressCode(code);
//  }
//  public boolean keypressControl(char ch) {
//    if (!active) return super.keypressControl(ch);
//  }

  public void focusLost(FocusEvent e) {
    if (active && !e.isTemporary()) {
      if (lastOp == Value.UNDEFINED && stack.size() > 0) {
        BigDecimal num = getNumericValue();
        if (num != null && num.signum() != 0)
          stack.addLast(new Value(num, 0));
      }
      if (stack.size() > 1) {
        ((Value) stack.getLast()).op = 0;
        compressStack();
        setText(nf.format(((Value) stack.getLast()).getNumber()));
        normalizeNumber();
      }
      stack.clear();
      memory.clear();
      lastOp = Value.UNDEFINED;
    }
    super.focusLost(e);
  }
//  public void focusGained(FocusEvent e) {
//    super.focusGained(e);
//  }
}

class Value {
  public static final int UNDEFINED = 0;
  public static final int PLUS = 1;
  public static final int MINUS = 2;
  public static final int MULTIPLY = 3;
  public static final int DIVIDE = 4;
  private static final int[] priority = {0, 10, 10, 20, 20};
  int op;
  BigDecimal num;

  public Value(BigDecimal number, int op) {
    num = number;
    this.op = op;
  }

  public Value evaluate(Value other) {
    switch (op) {
      case PLUS:
        num = num.add(other.num);
        break;
      case MINUS:
        num = num.subtract(other.num);
        break;
      case MULTIPLY:
        num = num.multiply(other.num).setScale(num.scale(), BigDecimal.ROUND_HALF_UP);
        break;
      case DIVIDE:
        if (other.num.signum() == 0) num = new BigDecimal(0);
        else num = num.divide(other.num, num.scale(), BigDecimal.ROUND_HALF_UP);
        break;
    }
    op = other.op;
    return this;
  }

  public BigDecimal getNumber() {
    return num;
  }

  public int getOperand() {
    return op;
  }

  public int getPriority() {
    return priority[op];
  }

  public String toString() {
    return "Value: "+num+" operand: "+op;
  }
}
