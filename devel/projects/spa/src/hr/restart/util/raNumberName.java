/****license*****************************************************************
**   file: raNumberName.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raNumberName {
  NameVar[] nums = {
    new NameVar(1000000000, "milijardu", "milijarda", "milijarde", "milijardi", 1),
    new NameVar(1000000, "milijun", "milijun", "milijuna", "milijuna", 0),
    new NameVar(1000, "tisuæu", "tisuæa", "tisuæe", "tisuæa", 1),
    new NameVar(100, "stotinu", "stotina", "stotine", "stotina", 1),
    new NameVar(90, "devedeset"),
    new NameVar(80, "osamdeset"),
    new NameVar(70, "sedamdeset"),
    new NameVar(60, "šezdeset"),
    new NameVar(50, "pedeset"),
    new NameVar(40, "èetrdeset"),
    new NameVar(30, "trideset"),
    new NameVar(20, "dvadeset"),
    new NameVar(19, "devetnaest"),
    new NameVar(18, "osamnaest"),
    new NameVar(17, "sedamnaest"),
    new NameVar(16, "šesnaest"),
    new NameVar(15, "petnaest"),
    new NameVar(14, "èetrnaest"),
    new NameVar(13, "trinaest"),
    new NameVar(12, "dvanaest"),
    new NameVar(11, "jedanaest"),
    new NameVar(10, "deset"),
    new NameVar(9, "devet"),
    new NameVar(8, "osam"),
    new NameVar(7, "sedam"),
    new NameVar(6, "šest"),
    new NameVar(5, "pet"),
    new NameVar(4, "èetiri"),
    new NameVar(3, "tri"),
    new NameVar(2, "dva", "dvije", "dva"),
    new NameVar(1, "jedan", "jedna", "jedno"),
  };

  private NameVar kn = new NameVar(0, " kuna", " kuna", " kune", " kuna", 1);
  private NameVar lp = new NameVar(0, " lipa", " lipa", " lipe", " lipa", 1);

  private static raNumberName inst = new raNumberName();
  private VarStr buf = new VarStr();
  private String sep = "";

  private raNumberName() {
  }

  public static void setSpacing(boolean spc) {
    inst.sep = spc ? " " : "";
  }

  public static String convert(BigDecimal num) {
    num = num.setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal dec = num.setScale(0, BigDecimal.ROUND_DOWN);
    BigDecimal frac = num.subtract(dec).movePointRight(2);
    return convert(dec.intValue(), frac.intValue());
  }

  public static String convert(double num) {
    return convert(new BigDecimal(num));
  }

  public static String convert(int dec, int frac) {
    return inst.convert_impl(dec, frac, inst.kn, inst.lp);
  }

  private String convert_impl(int dec, int frac, NameVar major, NameVar minor) {
    buf.reset();
    if (dec < 0) buf.append("minus ");
    if (dec != 0) convertPart(Math.abs(dec), kn);
    if (dec != 0 && frac != 0) buf.append(" i ");
    if (frac != 0) convertPart(Math.abs(frac), lp);
    return buf.toString();
  }

  private void convertPart(int val, NameVar suf) {
    int orig = val;
    for (int i = 0; i < nums.length; i++) {
      if (val >= nums[i].value) {
        int div = val / nums[i].value;
        if (div > 1 || buf.length() > 0 && val > 100) convertPart(div, nums[i]);
        else if (nums[i].gen >= 0) buf.append(nums[i].s);
        else buf.append(nums[i].gend(suf.gen));
        val %= nums[i].value;
        if (val > 0 || suf.value > 0) buf.append(sep);
      }
    }
    buf.append(suf.quant(orig));
  }

  public static void main(String[] argS) {
    System.out.println(convert(new BigDecimal("1001001431.91")));
    System.out.println(convert(new BigDecimal("181.01")));
    System.out.println(convert(new BigDecimal("1143.23")));
  }

  private class NameVar {
    public int value, gen;
    public String s, s1, s2, s5;
    public NameVar(int val, String n, String n1, String n2, String n5, int r) {
      value = val;
      gen = r;
      s = n;
      s1 = n1;
      s2 = n2;
      s5 = n5;
    }

    public NameVar(int val, String n) {
      this(val, n, n, n, n, 0);
    }

    public NameVar(int val, String n1, String n2, String n5) {
      this(val, null, n1, n2, n5, -1);
    }

    public String quant(int q) {
      int j = q % 10, d = (q / 10) % 10;
      if (j == 1 && d != 1) return s1;
      else if (j > 1 && j < 5 && d != 1) return s2;
      else return s5;
    }

    public String gend(int g) {
      return g == 0 ? s1 : g == 1 ? s2 : s5;
    }
  }
}
