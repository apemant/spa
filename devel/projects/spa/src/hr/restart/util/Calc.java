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

import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;

import com.borland.dx.dataset.DataSet;
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
    public boolean strongerThan(Operator o) {
      return priority > o.priority;
    }
    public boolean notWeaker(Operator o) {
    	return o == null || priority >= o.priority;
    }
    public BigDecimal exec(BigDecimal val) {
      return null;
    }
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) {
      return null;
    } 
    public BigDecimal exec(String var, BigDecimal val) {
      BigDecimal temp = exec(getVar(var), val);
      if (priority > 0) return temp;
      if (values == null || values.hasColumn(var) == null) {
      	vars.put(var, temp); 
      	return temp;
      }
      values.setBigDecimal(var, temp.setScale(values.getColumn(var).getScale(), BigDecimal.ROUND_HALF_UP));
      return values.getBigDecimal(var);
    }
    public BigDecimal eval(Object o1, Object o2) {
      boolean var = o1 instanceof String;
      if (priority == 0 && !var)
        new IllegalArgumentException("Invalid operand for assignment: " + expression);
      if (var) return exec((String) o1, getValue(o2));
      return exec((BigDecimal) o1, getValue(o2));
    }
    public String toString() {
    	return op;
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
  Operator GETPERCENT = new Operator("%%", 2, 20) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.divide(val2.movePointLeft(2), val1.scale(), BigDecimal.ROUND_HALF_UP); }
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
  Operator PRECISION = new Operator(",", 2, 30) {
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
  Operator EQ = new Operator("==", 2, 5) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.compareTo(val2) == 0 ? Aus.one0 : Aus.zero0; }
  };
  Operator NEQ = new Operator("!=", 2, 5) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.compareTo(val2) != 0 ? Aus.one0 : Aus.zero0; }
  };
  Operator GT = new Operator(">", 2, 5) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.compareTo(val2) > 0 ? Aus.one0 : Aus.zero0; }
  };
  Operator LT = new Operator("<", 2, 5) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.compareTo(val2) < 0 ? Aus.one0 : Aus.zero0; }
  };
  Operator GE = new Operator(">=", 2, 5) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.compareTo(val2) >= 0 ? Aus.one0 : Aus.zero0; }
  };
  Operator LE = new Operator("<=", 2, 5) {
    public BigDecimal exec(BigDecimal val1, BigDecimal val2) { return val1.compareTo(val2) <= 0 ? Aus.one0 : Aus.zero0; }
  };  
  Operator IF = new Operator("?:", 2, 5);
  
  
  private BigDecimal getVar(String var) {
    if (values != null && values.hasColumn(var) != null) 
      return values.getBigDecimal(var);
    return (BigDecimal) vars.get(var);
  }
  
  private BigDecimal getValue(Object o) {
    if (o instanceof BigDecimal) return (BigDecimal) o;
    return getVar((String) o);
  }
  
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
  
  public Calc(Calc copy) {
    this.values = copy.values;
    this.vars = copy.vars;
  }
  
  public static BigDecimal eval(String expr) {
    return new Calc(expr).eval();
  }
  
  public static BigDecimal eval(ReadWriteRow ds, String expr) {
    return new Calc(ds, expr).eval();
  }
  
  public static BigDecimal run(ReadWriteRow ds, String expr) {
    return new Calc(ds, expr).eval();
  }
  
  public Calc set(String var, BigDecimal val) {
  	vars.put(var,  val);
  	return this;
  }
  
  public BigDecimal get(String var) {
    return (BigDecimal) vars.get(var);
  }
  
  public Calc reset() {
    vars.clear();
    return this;
  }
  
  public Calc setData(ReadWriteRow ds) {
    this.values = ds;
    return this;
  }
  
  public BigDecimal runOn(ReadWriteRow ds, String expr) {
    return new Calc(this).setData(ds).run(expr);
  }
  
  public BigDecimal run(String expr) {
    this.expression = expr;
    return eval();
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
  	String module = null;
    while (p < l) {
      char ch = expression.charAt(p++);
      if (ch == '.') {
      	if (module != null) throw new IllegalArgumentException("Invalid variable, double dot: " + expression);
      	module = expression.substring(beg, p - 1);
      } else if (ch != '_' && (ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z') && (ch < '0' || ch > '9')) {
        --p;
        break;
      }
    }
    String var = expression.substring(beg, p);
    if (module != null) {
    	if (beg == p) throw new IllegalArgumentException("Invalid variable after dot: " + expression);
    	ReadWriteRow ds = dM.getDataByName(module);
    	if (ds == null) throw new IllegalArgumentException("Invalid module: " + expression);
    	String col = expression.substring(beg + module.length() + 1, p);
    	vars.put(var, ds.getBigDecimal(col));
    }
    return var;
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
        return getVar(getVar(p - 1)).negate();
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
      if (ch == '?') return IF;
      char ch2 = p < l ? expression.charAt(p) : (char) 0;
      char ch3 = p+1 < l ? expression.charAt(p + 1): (char) 0;
      if (ch2 == '=') {
        ++p;
        if (ch == '+') return ASSIGN_ADD;
        if (ch == '-') return ASSIGN_SUB;
        if (ch == '*') return ASSIGN_MUL;
        if (ch == '/') return ASSIGN_DIV;
        if (ch == '%') return ASSIGN_PERCENT;
        if (ch == '=') return EQ;
        if (ch == '!') return NEQ;
        if (ch == '>') return GE;
        if (ch == '<') return LE;
        new IllegalArgumentException("Illegal operand: " + expression);
      } else if (ch2 == '%') {
        ++p;
        if (ch3 == '=') {
          ++p;
          if (ch == '+') return ASSIGN_ADDPERCENT;
          if (ch == '-') return ASSIGN_SUBPERCENT;
          if (ch == '~') return ASSIGN_INVERTPERCENT;
        } else {
          if (ch == '%') return GETPERCENT;
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
      if (ch == ',') return PRECISION;
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
      if (ch == '>') return GT;
      if (ch == '<') return LT;
      if (ch == '<' && ch2 == '>') return NEQ;
      new IllegalArgumentException("Illegal operand: " + expression);
    }
    return null;
  }
  
  // d = 5 + 2d + d * 3; (1 ? 2 : 2) 
  
  // D = 3 ? 
  int p, l;
  public BigDecimal eval() {
    p = 0;
    l = expression.length();
    LinkedList stack = new LinkedList();
    Object left = null;
    Operator op = null;
    
    while (p < l) {
      while (op == null) {
    	left = getOperand();
    	op = getOperator();
    	if (op == null && p >= l) return getValue(left);
      }
      if (op == IF) return evalBranch(getValue(left).signum() != 0);
      Object right = getOperand();
      Operator nop = getOperator();
      if (nop == IF && nop.strongerThan(op)) {
        right = evalBranch(getValue(right).signum() != 0);
        nop = null;
      }
      if (nop != null && nop.strongerThan(op)) {
      	stack.addLast(left);
      	stack.addLast(op);
      	left = right;
      	op = nop;
      } else {
      	left = op.eval(left,  right);
      	while (stack.size() > 0 && ((Operator) stack.getLast()).notWeaker(nop)) {
      		op = (Operator) stack.removeLast();
      		left = op.eval(stack.removeLast(), left);
      	}
      	op = nop;
      }
    }
    return getValue(left);
  }

  private BigDecimal evalBranch(boolean first) {
    int depth = 1;
    int beg = p;
    while (p < l) {
      char cc = expression.charAt(p++);
      if (cc == ':') {
        if (--depth == 0)  return new Calc(this).evaluate(
            first ? expression.substring(beg, p - 1) : expression.substring(p));
      } else if (cc == '?') ++depth;
    }
    throw new IllegalArgumentException("Unmatched conditional expression: " + expression);
  }
  
  public static void test() {
  	BigDecimal sto = new java.math.BigDecimal(100).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  	DataSet ds = Stdoku.getDataModule().getTempSet("rbr=2");
  	ds.open();

  	lookupData.getlookupData().raLocate(dM.getDataModule().getArtikli(), "CART1", ds.getString("CART1"));
  	lookupData.getlookupData().raLocate(dM.getDataModule().getPorezi(), "CPOR", dM.getDataModule().getArtikli().getString("CPOR"));
  	
  	long tim = System.currentTimeMillis();
  	for (int i = 0; i < 50000; i++) {
	  	ds.setBigDecimal(
					"DIOPORPOR",
					ds.getBigDecimal("PORAV").multiply(
							dM.getDataModule().getPorezi().getBigDecimal("UKUNPOR"))
							.divide(sto, 1).setScale(2,
									BigDecimal.ROUND_HALF_UP));
			ds.setBigDecimal(
					"DIOPORMAR",
					ds.getBigDecimal("PORAV").add(
							ds.getBigDecimal("DIOPORPOR")
									.negate()).setScale(2,
							BigDecimal.ROUND_HALF_UP));
  	}
  	System.out.println("direct " + (System.currentTimeMillis() - tim));
  	
  	tim = System.currentTimeMillis();
  	for (int i = 0; i < 50000; i++) {
  		Calc c = new Calc(ds);
  		c.evaluate("DIOPORMAR = PORAV ~% Porezi.UKUPOR;" +
  				   "DIOPORPOR = PORAV - DIOPORPOR;" +
  				   "34 > 33 ? 4 : 3"  
  		    );
  		
  		
  		//c.evaluate("DIOPORPOR = PORAV - DIOPORPOR");
  	}
  	System.out.println("calc " + (System.currentTimeMillis() - tim));

  }
}
