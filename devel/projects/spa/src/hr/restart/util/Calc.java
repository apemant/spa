/****license*****************************************************************
**   file: Calc.java
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
package hr.restart.util;

import java.math.BigDecimal;
import java.util.HashMap;

import com.borland.dx.dataset.ReadWriteRow;

public class Calc {
  private String expression;
  private ReadWriteRow values;
  private HashMap vars;

  class Operator {
    private String op;
    private int args;
    private int priority;
    public Operator(String op, int args, int priority) {
      this.op = op;
      this.args = args;
      this.priority = priority;
    }
    public boolean stronger(Operator o) {
      return priority > o.priority;
    }
    public BigDecimal exec(BigDecimal val) {
      return null;
    }
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) {
      return null;
    } 
    public BigDecimal exec(String var, BigDecimal val) {
      BigDecimal temp = exec(values.getBigDecimal(var), val);
      if (priority > 0) return temp;
      values.setBigDecimal(var, temp.setScale(values.getColumn(var).getScale(), BigDecimal.ROUND_HALF_UP));
      return values.getBigDecimal(var);
    }
    public BigDecimal eval(Object o1, Object o2) {
      boolean var = o1 instanceof String;
      if (priority == 0 && !var)
        new IllegalArgumentException("Invalid operand for assignment: " + expression);
      BigDecimal v2 = o2 instanceof BigDecimal ? (BigDecimal) o2 : values.getBigDecimal((String) o2);
      if (var) return exec((String) o1, v2);
      return exec((BigDecimal) o1, v2);
    }
  }
  
  Operator ADD = new Operator("+", 2, 10) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.add(val2); }
  };
  Operator SUB = new Operator("-", 2, 10) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.subtract(val2); }
  };
  Operator MUL = new Operator("*", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.multiply(val2); }
  };
  Operator DIV = new Operator("/", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.divide(val2, val1.scale(), BigDecimal.ROUND_HALF_UP); }
  };
  Operator LSH = new Operator("<<", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.movePointRight(val2.intValue()); }
  };
  Operator RSH = new Operator(">>", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.movePointLeft(val2.intValue()); }
  };
  Operator PERCENT = new Operator("%", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.multiply(val2).movePointLeft(2); }
  };
  Operator ADDPERCENT = new Operator("+%", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.add(val1.multiply(val2).movePointLeft(2)); }
  };
  Operator SUBPERCENT = new Operator("-%", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.subtract(val1.multiply(val2).movePointLeft(2)); }
  };
  Operator INVERTPERCENT = new Operator("~%", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.divide(Aus.one0.add(val2.movePointLeft(2)), val1.scale(), BigDecimal.ROUND_HALF_UP); }
  };
  Operator PRECISION = new Operator(":", 2, 30) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.setScale(val2.intValue(), BigDecimal.ROUND_HALF_UP); }
  };
  Operator ASSIGN = new Operator("=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val2; }
  };
  Operator ASSIGN_ADD = new Operator("+=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.add(val2); }
  };
  Operator ASSIGN_SUB = new Operator("-=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.subtract(val2); }
  };
  Operator ASSIGN_MUL = new Operator("*=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.multiply(val2); }
  };
  Operator ASSIGN_DIV = new Operator("/=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.divide(val2, val1.scale(), BigDecimal.ROUND_HALF_UP); }
  };
  Operator ASSIGN_LSH = new Operator("<<=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.movePointRight(val2.intValue()); }
  };
  Operator ASSIGN_RSH = new Operator(">>=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.movePointLeft(val2.intValue()); }
  };
  Operator ASSIGN_PERCENT = new Operator("%=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.multiply(val2).movePointLeft(2); }
  };
  Operator ASSIGN_ADDPERCENT = new Operator("+%=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.add(val1.multiply(val2).movePointLeft(2)); }
  };
  Operator ASSIGN_SUBPERCENT = new Operator("-%=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.subtract(val1.multiply(val2).movePointLeft(2)); }
  };
  Operator ASSIGN_INVERTPERCENT = new Operator("~%=", 2, 0) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.divide(Aus.one0.add(val2.movePointLeft(2)), val1.scale(), BigDecimal.ROUND_HALF_UP); }
  };
  
    
  public Calc() {
    this(null, null);
  }
    
  public Calc(String expr) {
    this(null, expr);
  }
    
  public Calc(ReadWriteRow ds) {
    this(ds, null);
  }
  
  public Calc(ReadWriteRow ds, String expr) {
    this.values = ds;
    this.expression = expr;
    this.vars = new HashMap();
  }
  
  private Calc(Calc copy) {
    this.values = copy.values;
    this.vars = copy.vars;
  }
  
  public static BigDecimal eval(String expr) {
    return new Calc(expr).eval();
  }
  
  public static BigDecimal eval(ReadWriteRow ds, String expr) {
    return new Calc(ds, expr).eval();
  }
  
  public BigDecimal evaluate(String expr) {
    this.expression = expr;
    return eval();
  }
  
  private BigDecimal getSubCalc(int beg) {
    int depth = 1;
    while (p < l) {
      char cc = expression.charAt(p++);
      if (cc == ')') {
        if (--depth == 0) return new Calc(this).evaluate(expression.substring(beg, p - 1));
      } else if (cc == '(') ++depth;
    }
    throw new IllegalArgumentException("Unmatched bracket: " + expression);
  }
  
  private String getVar(int beg) {
    while (p < l) {
      char ch = expression.charAt(p++);
      if (ch != '_' && (ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z') && (ch < '0' || ch > '9')) {
        --p;
        break;
      }
    }
    return expression.substring(beg, p);
  }
  
  private BigDecimal getNumber(int beg, boolean sign, boolean dot, boolean num) {
    while (p < l) {
      char ch = expression.charAt(p++);
      if (ch == '-')
        if (!sign) sign = true;
        else throw new IllegalArgumentException("Invalid number, double sign: " + expression);
      else if (ch == '.')
        if (!dot) dot = true;
        else throw new IllegalArgumentException("Invalid number, double dot: " + expression);
      else if (ch >= '0' && ch <= '9') num = true;
      else if (!num && !dot && (ch == '_' || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')))
        return values.getBigDecimal(getVar(p - 1)).negate();
      else {
        --p;
        break;
      }
    }
    if (!num) throw new IllegalArgumentException("Invalid number, no digits: " + expression);
    return new BigDecimal(expression.substring(beg, p));
  }

  private Object getOperand() {
    while (p < l) {
      char ch = expression.charAt(p++);
      if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r') continue;
      if (ch == '(') return getSubCalc(p);
      if (ch >= '0' && ch <= '9') return getNumber(p - 1, false, false, true);
      if (ch == '-') return getNumber(p - 1, true, false, false);
      if (ch == '.') return getNumber(p - 1, false, true, false);      
      if (ch == '_' || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
        return getVar(p - 1);
      throw new IllegalArgumentException("Operand expected: " + expression);
    }
    throw new IllegalArgumentException("Operand expected: " + expression);
  }
  
  private Operator skip() {
    while (p < l) {
      char ch = expression.charAt(p++);
      if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r') continue;
      --p;
      return null;
    }
    return null;
  }

  private Operator getOperator() {
    while (p < l) {
      char ch = expression.charAt(p++);
      if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r') continue;
      if (ch == ';') return skip();
      char ch2 = p < l ? expression.charAt(p) : (char) 0;
      char ch3 = p+1 < l ? expression.charAt(p + 1): (char) 0;
      if (ch2 == '=') {
        ++p;
        if (ch == '+') return ASSIGN_ADD;
        if (ch == '-') return ASSIGN_SUB;
        if (ch == '*') return ASSIGN_MUL;
        if (ch == '/') return ASSIGN_DIV;
        if (ch == '%') return ASSIGN_PERCENT;
        new IllegalArgumentException("Illegal operand: " + expression);
      } else if (ch2 == '%') {
        ++p;
        if (ch3 == '=') {
          ++p;
          if (ch == '+') return ASSIGN_ADDPERCENT;
          if (ch == '-') return ASSIGN_SUBPERCENT;
          if (ch == '~') return ASSIGN_INVERTPERCENT;
        } else {
          if (ch == '+') return ADDPERCENT;
          if (ch == '-') return SUBPERCENT;
          if (ch == '~') return INVERTPERCENT;
        }
        new IllegalArgumentException("Illegal operand: " + expression);
      }
      if (ch == '+') return ADD;
      if (ch == '-') return SUB;
      if (ch == '*') return MUL;
      if (ch == '/') return DIV;
      if (ch == '=') return ASSIGN;
      if (ch == ':') return PRECISION;
      if (ch == '%') return PERCENT;
      if (ch == '<' && ch2 == '<') {
        ++p;
        if (ch3 != '=') return LSH;
        ++p;
        return ASSIGN_LSH;
      }
      if (ch == '>' && ch2 == '>') {
        ++p;
        if (ch3 != '=') return RSH;
        ++p;
        return ASSIGN_RSH;
      }
      new IllegalArgumentException("Illegal operand: " + expression);
    }
    return null;
  }
  
  private BigDecimal getVar(String var) {
    if (values.hasColumn(var) != null) return values.getBigDecimal(var);
    return (BigDecimal) vars.get(var);
  }
  
  private BigDecimal getValue(Object o) {
    if (o instanceof BigDecimal) return (BigDecimal) o;
    return getVar((String) o);
  }
  
  int p, l;
  public BigDecimal eval() {
    p = 0;
    l = expression.length();
    boolean oex = true;
    Object ret = null;
    while (p < l) {
      if (oex) ret = getOperand();
      Operator op = getOperator();
      if (oex = (op == null)) ret = getValue(ret);
      o = op.eval(o, getNextOperand(op));
    }
  }
  
  private Object getNextOperand(Operator op) {
    Object o = getOperand();
    Operator nop = getOperator();
    if (nop == null || nop.priority <= op.priority) return o;
    return nop.eval(o, getNextOperand(nop));
  }
}
