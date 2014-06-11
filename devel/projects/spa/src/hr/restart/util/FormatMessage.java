/*
 * Created on 2004.12.07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;


/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormatMessage {
    
  HashMap vars;
  String format;
  Object parent;
  VarStr buf = new VarStr();
  int bufPos;

  public FormatMessage(String format) {
    this(null, format);
  }
  
  public FormatMessage(Object parent, String format) {
    this.parent = parent;
    this.format = format;
    vars = new HashMap();
  }
  
  public FormatMessage var(String name, int value) {
    vars.put(name, new Integer(value));
    return this;
  }
  
  public FormatMessage var(String name, BigDecimal value) {
    vars.put(name, value);
    return this;
  }
  
  public FormatMessage var(String name, String value) {
    vars.put(name, value);
    return this;
  }
  
  private VarStr getBracketExpression(char bracket) {
    int beg = bufPos;
    bufPos = buf.indexOf(bracket, beg);
    if (bufPos <= 0)
      throw new ParseException("Unmatched "+bracket);
    return buf.copy(beg, bufPos++);
  }  
  
  private void skipSpaces() {
    while (bufPos < buf.length() && Character.isWhitespace(buf.charAt(bufPos)))
      ++bufPos;        
  }
  
  private void require(char ch) {
    if (bufPos < buf.length() && buf.charAt(bufPos++) == ch) return;
    throw new ParseException(ch + " expected");
  }
  
  private void require(String str) {
    for (int i = 0; i < str.length(); i++)
      require(str.charAt(i));
  }
  
  private Object getValue(String name) {
    if (vars.containsKey(name)) return vars.get(name);
    if (parent == null)
      throw new ParseException("Unknown var: "+name);
    
    if (parent instanceof ReadRow) {
      ReadRow ds = (ReadRow) parent;
      if (ds.hasColumn(name) == null)
        throw new ParseException("Unknown field: "+name);
      Variant v = new Variant();
      ds.getVariant(name, v);
      return v.getAsObject();
    }

    Object result = null;    
    try {
      Field f = parent.getClass().getDeclaredField(name);
      f.setAccessible(true);
      result = f.get(parent);
    } catch (Exception e) {      
      //
    }
    if (result != null) return result;
    try {
      Method m = parent.getClass().getDeclaredMethod(name, null);
      m.setAccessible(true);
      result = m.invoke(parent, null);
    } catch (Exception e) {      
      // 
    }
    if (result != null) return result;
    try {
      Field f = parent.getClass().getField(name);      
      result = f.get(parent);
    } catch (Exception e) {      
      //
    }
    if (result != null) return result;    
    try {
      Method m = parent.getClass().getMethod(name, null);      
      result = m.invoke(parent, null);
    } catch (Exception e) {      
      //
    }
    if (result != null) return result;
    throw new ParseException("Unknown var, field or method: "+name);
  }
  
  private String getStringValue(String name) {
    Object val = getValue(name);
    if (val instanceof Timestamp)
      return Aus.formatTimestamp((Timestamp) val);
    if (val instanceof BigDecimal)
      return Aus.formatBigDecimal((BigDecimal) val);      
    if (val instanceof Double)
      return Aus.formatFloat(((Double) val).floatValue());
    if (val instanceof Float)
      return Aus.formatFloat(((Float) val).floatValue());
    return val.toString();
  }
  
  private String getBoolConditional(boolean cond, VarStr choices) {
    if (choices.countOccurences('|') > 1)
      throw new ParseException("Too many conditional clauses! Max. 2 for boolean args"+choices);
    int split = choices.indexOf('|');
    if (split >= 0)
       if (cond) choices.truncate(split);
       else choices.leftChop(split + 1);
    else if (!cond) choices.clear();
    return substituteVars(choices);
  }
  
  private String getIntConditional(int var, VarStr choices) {
    String[] parts = choices.split('|');
    if (parts.length != 3)
      throw new ParseException("Invalid number of conditional clauses for integrals (should be 3)"+choices);
    return substituteVars(new VarStr(Aus.getNumDep(var, parts[0], parts[1], parts[2])));    
  }
  
  private String getConditional(String name, VarStr cond) {
    Object val = getValue(name);
    if (val instanceof Boolean)
      return getBoolConditional(((Boolean) val).booleanValue(), cond);
    if (val instanceof Integer)
      return getIntConditional(((Integer) val).intValue(), cond);
    if (val instanceof Long)
      return getIntConditional(((Long) val).intValue(), cond);
    if (val instanceof Short)
      return getIntConditional(((Short) val).intValue(), cond);
    throw new ParseException("Invalid variable type (should be integral or boolean); "+name);
  }
  
  private String substituteVars(VarStr substr) {
    int pos = 0;
    while (pos < substr.length()) {
      if (substr.charAt(pos) == '$') {
        int beg = pos++;
        while (pos < substr.length() && Character.isJavaIdentifierPart(substr.charAt(pos))) ++pos;
        String sub = getValue(substr.mid(beg + 1, pos)).toString();
        substr.replace(beg - 1, pos, sub);
        pos = beg + sub.length() - 1;
      } else ++pos;
    }
    return substr.toString();
  }
  
  private boolean evaluateExpression(VarStr expr) {
    int pos = 0;
    while (pos < expr.length() && Character.isJavaIdentifierPart(expr.charAt(pos))) ++pos;
    Object val = getValue(expr.left(pos));
    if (pos < expr.length()) {
      if (expr.mid(pos, pos + 2).equals("=="))
        return val.equals(getArg(val, expr.from(pos + 2)));
      else if (expr.mid(pos, pos + 2).equals("!="))
        return !val.equals(getArg(val, expr.from(pos + 2)));
      throw new ParseException("Unknown operator; "+expr);
    }
    if (val instanceof Boolean)
      return ((Boolean) val).booleanValue();
    throw new ParseException("Invalid variable type (should be boolean); "+expr);
  }
  
  private Object getArg(Object type, String val) {
    if (type instanceof String)
      return val;
    if (type instanceof Integer)
      return new Integer(val);
    if (type instanceof Short)
      return new Short(val);
    if (type instanceof Long)
      return new Long(val);
    throw new ParseException("Invalid variable type (should be integral or string); "+val);
  }
  
  private String parse() {
    bufPos = 0;
    buf.clear().append(format);
    while (bufPos < buf.length() - 1) {      
      if (buf.charAt(bufPos) == '$') {
        int beg = bufPos++;
        String sub = "";
        if (buf.charAt(bufPos++) == '{') {
          boolean cond = evaluateExpression(getBracketExpression('}'));                  
          require(":[");
          sub = getBoolConditional(cond, getBracketExpression(']'));          
        } else {
          while (bufPos < buf.length() && Character.isJavaIdentifierPart(buf.charAt(bufPos)))
            ++bufPos;
          String name = buf.mid(beg + 1, bufPos);
          if (bufPos < buf.length() && buf.charAt(bufPos) == ':') {
            ++bufPos;
            require('[');
            sub = getConditional(name, getBracketExpression(']'));
          } else sub = getValue(name).toString();
        }
        buf.replace(beg, bufPos, sub);
        bufPos = beg + sub.length() - 1;
      } else ++bufPos;
    }
    return buf.toString();
  }
  
  public String toString() {
    return parse();    
  }
  
  public static void main(String[] args) {
    new Tester().test();
  }
  
  static class Tester {
    String partner = "Deus racunala";
    int cpar = 1268;
    int broj = 1347;
    public Tester() {}
    public void test() {
      int picnum = 55;      
    }
    public int getBrojLjudi() {
      return 765;
    }
  }
}
