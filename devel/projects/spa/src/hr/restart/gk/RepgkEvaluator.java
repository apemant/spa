/****license*****************************************************************
**   file: RepgkEvaluator.java
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
 * Created on Aug 3, 2005
 *
 */
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Detrepgk;
import hr.restart.baza.Gkkumulativi;
import hr.restart.baza.Repgk;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.MathEvaluator;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * RepgkEvaluator r = new RepgkEvaluator();
 * r.setCrepgk(int);
 * r.setRange(int m1, int m2, int g)
 * ...
 * #r.setRange(int m1, int m2, int g, int idx)
 * 
 * ds = r.evaluate();
 *
 */
public class RepgkEvaluator {
	private StorageDataSet result = null;
	private int crepgk;
	private QueryDataSet repgkset;
	private HashMap conditions;
	private ArrayList ranges = new ArrayList();
	private QueryDataSet detrepgkset;
	private MathEvaluator mathev = new MathEvaluator();
	/**
	 * 
	 */
	public RepgkEvaluator() {
	}
	public StorageDataSet calcResult(StorageDataSet gk) {
		return null;
	}
	
	public StorageDataSet getResult() {
		return result;
	}
	
	public void setRange(int m1, int m2, int y, int idx) {
	  ranges.add(idx, new RGKRange(m1,m2,y));
	}
	
	public void setRange(int m1, int m2, int y) {
	  setRange(m1,m2,y,0);
	}
	public StorageDataSet evaluate() {
System.out.println("MARKERRRRR");
	  /*TODO validacija*/
	  //apply ranges
	  for (Iterator iter = ranges.iterator(); iter.hasNext();) {
      RGKRange range = (RGKRange) iter.next();
      StorageDataSet set = range.getDataSet();
      
      //collect values in RGKCondition-s
      for (set.first(); set.inBounds(); set.next()) {
        for (Iterator it = conditions.keySet().iterator(); it.hasNext();) {
          String key = (String) it.next();
          RGKCondition gkcond = (RGKCondition)conditions.get(key);
          if (!gkcond.isLocalSum()) gkcond.eval(set);
        }
      }
      //store results
      for (result.first(); result.inBounds(); result.next()) {
        replaceAndPost("DV1","IV1");
        replaceAndPost("DV2","IV2");
        replaceAndPost("DV3","IV3");
/*        String v2 = replaceWithResults(result.getString("DV2"));
        result.setString("IV2",v2);
        String v3 = replaceWithResults(result.getString("DV3"));
        result.setString("IV3",v3);*/
        result.post();
      }
    }
    //evaluate final results via MathEvaluator
	  for (result.first(); result.inBounds(); result.next()) {
	    evaluateAndPost("IV1", "V1");
	    evaluateAndPost("IV2", "V2");
	    evaluateAndPost("IV3", "V3");
	  }
    //evaluate local sums
//to eval local sums of local sums
	  for (int i = 0; i < 2; i++) {
System.out.println("eval local sums of local sums "+i);        
    	for (result.first(); result.inBounds(); result.next()) {
          for (Iterator it = conditions.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            RGKCondition gkcond = (RGKCondition)conditions.get(key);
            if (gkcond.isLocalSum()) {
System.out.println("Evaluating local sum ..."+gkcond);
              gkcond.eval(result);
            }
          }
        }
      }
	  //prije zadnjeg replace-a napuniti sve conditione koji su null sa nulama 
	  //da replaca sve cond.ove u stringovima
      for (Iterator it = conditions.keySet().iterator(); it.hasNext();) {
        String key = (String) it.next();
        RGKCondition gkcond = (RGKCondition)conditions.get(key);
        if (gkcond.getValue()==null) {
          gkcond.value = Aus.zero2;
        }
      }
	  for (result.first(); result.inBounds(); result.next()) {
        replaceAndPost("DV1","IV1");
        replaceAndPost("DV2","IV2");
        replaceAndPost("DV3","IV3");
        result.post();
      }
    //evaluate final final results with local sums
	  for (result.first(); result.inBounds(); result.next()) {
	    evaluateAndPost("IV1", "V1");
	    evaluateAndPost("IV2", "V2");
	    evaluateAndPost("IV3", "V3");
	  }
	  return result;
	}
	
  private void replaceAndPost(String resCol, String destCol) {
    String v = replaceWithResults(result.getString(resCol));
    result.setString(destCol,v);
  }
    /**
   * @param string
   * @param string2
   */
  private void evaluateAndPost(String colfrom, String colto) {
    try {
      mathev.reset();
      mathev.setExpression(result.getString(colfrom));
      Double evaluated = mathev.getValue();
      if (evaluated != null) {
        result.setBigDecimal(colto, new BigDecimal(evaluated.doubleValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
      }
    } catch (Exception e) {
      System.out.println("evaluateAndPost: exception "+e);
      //e.printStackTrace();
    }
  }
    /**
   * @param string
   * @return
   */
  private String replaceWithResults(String dv) {
    VarStr vdv = new VarStr(" "+dv+" "); //" " je zbog toga sto trazi space prije i poslije
System.out.println("*** replaceWithResults (str:"+vdv+") ***");
    for (Iterator iter = conditions.keySet().iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      RGKCondition gkcond = (RGKCondition)conditions.get(key);
      if (!gkcond.isPlnum()) {//condition is plain number
        if (gkcond.getValue()!=null) {//condition is not evaluated (eg. local sums)
System.out.println("replaceWithResults "+gkcond+"\n ("+vdv+")\n "+key+" with "+gkcond.getValue().toString());
					vdv = vdv.replaceAll(" "+key+" "," ("+gkcond.getValue().toString()+") ");
        }
      }
    }
    return vdv.toString();
  }
    /**
	 * @return Returns the crepgk.
	 */
	public int getCrepgk() {
		return crepgk;
	}
	/**
	 * @param crepgk The crepgk to set.
	 */
	public void setCrepgk(int crepgk) {
		this.crepgk = crepgk;
		detrepgkset=null;
		createDefinition();
	}
	/**
	 * create result dataset
	 * create conditions
	 */
	private void createDefinition() {
		findRepgk();
		createResultDataSet();
		createConditions();
	}
	/**
	 * 
	 */
	private void createConditions() {
		conditions = new HashMap();
		if (detrepgkset==null) createDetrepgkset();
		for (detrepgkset.first(); detrepgkset.inBounds();detrepgkset.next()) {
			createConditionsFor(detrepgkset.getString("KTO1"),1);
			createConditionsFor(detrepgkset.getString("KTO2"),2);
			createConditionsFor(detrepgkset.getString("KTO3"),3);
		}
	}
	private void createDetrepgkset() {
    detrepgkset = Detrepgk.getDataModule().getTempSet(Condition.equal("CREPGK",getCrepgk()));
    detrepgkset.open();
  }
  /**
	 * @param string
	 */
	private void createConditionsFor(String string, int kto) {
		if (string == null || "".equals(string)) return;
		String[] toks = new VarStr(string).split(' ');
		int start = 0;
		for (int i = start; i < toks.length; i++) {
			if (!isOperator(toks[i])) {
				conditions.put(toks[i], new RGKCondition(toks[i], kto));
			}
		}
	}
	/**
	 * 
	 */
	private void createResultDataSet() {
		result = new StorageDataSet();
		Detrepgk kdmodule = Detrepgk.getDataModule();
		Column c_crepgk = kdmodule.getColumn("CREPGK").cloneColumn();
		Column c_rbsrepgk = kdmodule.getColumn("RBSREPGK").cloneColumn();
		Column c_desc = kdmodule.getColumn("DESCRIPTION").cloneColumn();
		String h1 = repgkset.getString("HEADER1");
		String h2 = repgkset.getString("HEADER2");
		String h3 = repgkset.getString("HEADER3");
		h1 = h1.equals("")?"NEDEF.1":h1;
		h2 = h2.equals("")?"NEDEF.2":h2;
		h3 = h3.equals("")?"NEDEF.3":h3;
		Column v1 = dM.createBigDecimalColumn("V1", h1);
		Column v2 = dM.createBigDecimalColumn("V2", h2);
		Column v3 = dM.createBigDecimalColumn("V3", h3);
		
		Column iv1 = dM.createStringColumn("IV1", "Izraèun "+h1, 300);
		Column iv2 = dM.createStringColumn("IV2", "Izraèun "+h2, 300);
		Column iv3 = dM.createStringColumn("IV3", "Izraèun "+h3, 300);
		
		Column dv1 = dM.createStringColumn("DV1", "Definicija "+h1, 200);
		Column dv2 = dM.createStringColumn("DV2", "Definicija "+h2, 200);
		Column dv3 = dM.createStringColumn("DV3", "Definicija "+h3, 200);

		result.setColumns(new Column[] {
				c_crepgk, c_rbsrepgk, c_desc, 
				v1, v2, v3, 
				iv1, iv2, iv3, 
				dv1, dv2, dv3});
		result.open();
		if (detrepgkset==null) createDetrepgkset();
		for (detrepgkset.first(); detrepgkset.inBounds(); detrepgkset.next()) {
		  result.insertRow(false);
      result.setInt("CREPGK", getCrepgk());
      result.setInt("RBSREPGK", detrepgkset.getInt("RBSREPGK"));
      result.setString("DESCRIPTION", detrepgkset.getString("DESCRIPTION"));
      result.setString("DV1", detrepgkset.getString("KTO1"));
      result.setString("DV2", detrepgkset.getString("KTO2"));
      result.setString("DV3", detrepgkset.getString("KTO3"));
    }
	}
	/**
	 * @return
	 */
	private void findRepgk() {
		repgkset = Repgk.getDataModule().getTempSet(Condition.equal("CREPGK",getCrepgk()));
		repgkset.open();
		if (repgkset.getRowCount()!=1) {
			repgkset = null;
			throw new RuntimeException("Nije pronadjen zadani izvjestaj (crepgk = "+getCrepgk()+") !!! ");
		}
	}
	private boolean isSpecial(String string) {
		return Util.getUtil().containsArr(specials,string);
	}
  private String getSpecialFromCond(String cond) {
    for (int i = 0; i < specials.length; i++) {
      if (cond.startsWith(specials[i])) {
        return specials[i];
      }
    }
    return null;
  }
	
	String[] specials = new String[] {
			"PS:", //pocetno stanje, dodatni uvijet - cvrnal = '00'
			"BSH:" //bean shell skripta
			};
	/**
	 * @param string
	 * @return
	 */
	private boolean isOperator(String string) {
		return Util.getUtil().containsArr(operators,string);
	}
	String[] operators = new String[] {
													"+",
													"-",
													"*",
													"/",
													"^",
													"%",
													"&",
													"|",
													"cos",
													"sin",
													"tan",
													"acos",
													"asin",
													"atan",
													"sqr",
													"sqrt",
													"log",
													"min",
													"max",
													"exp",
													"floor",
													"ceil",
													"abs",
													"neg",
													"rnd",
													")","(","{","}","[","]" //andrej
													};
	
	/**
	 * TEST METHOD
	 */
	public void prntConds() {
    for (Iterator iter = conditions.keySet().iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      RGKCondition gkcond = (RGKCondition)conditions.get(key);
      System.out.println("key = "+key);
      System.out.println("condition = "+gkcond);
    }
	}
	
	class RGKCondition {
		String cond, special;
		int kto;
		BigDecimal value = null;
		boolean plnum = false;
		RGKCondition(String _cond, int _kto) {
			this(_cond, null, _kto);
		}
		RGKCondition(String _cond, String _special, int _kto) {
			cond = _cond;
			if (_special == null) {
			  special = getSpecialFromCond(cond);
			} else {
			  special = _special;
			}
		  if (special != null) {//remove special from cond
		    cond = cond.substring(special.length());
		  }
			kto = _kto;
			checkPlainNumber();
System.out.println(toString()+" created...");
		}
    void eval(DataSet ds) {
		  if (plnum) return;
		  boolean ok = check(ds);
System.out.println(this+".eval(ds): check "+ok);
			if (ok) {
			  BigDecimal __val = calcValue(ds);
System.out.println(this+".eval(ds):	value added "+__val);		 
				if (value == null) value = Aus.zero2;
				value = value.add(__val);
			}
		}
		BigDecimal getValue() {
		  return value;
		}
		boolean check(DataSet ds) {
			if (ds.hasColumn("ID") != null) {//stavke gk
				//special
				if ("PS:".equals(special)) {
					boolean ps = false;
					if (ds.hasColumn("GODMJ")!=null) {//kumulativi
						ps = ds.getString("GODMJ").endsWith("00");
					}
					if (ds.hasColumn("CVRNAL")!=null) {//kumulativi
						ps = ds.getString("CVRNAL").equals("00");
					}
					if (!ps) return false;
				}
				
				if (cond.toUpperCase().startsWith("S") || cond.toUpperCase().startsWith("D") || cond.toUpperCase().startsWith("P")) {
					int iof_ = cond.indexOf('-');
					if (iof_ >0) {//range
						String bk = ds.getString("BROJKONTA");
						String k1 = cond.substring(1, iof_);
						String k2 = cond.substring(iof_+1)+"999999999";
						return (bk.compareTo(k1) >=0) && (bk.compareTo(k2)<=0);
					} else {
						String k = cond.substring(1, cond.length());
						return ds.getString("BROJKONTA").startsWith(k);//klasa & grupa
					}
				}
			} else {
				if (isLocalSum()) {//local sum
					int li = Integer.parseInt(cond.substring(1,cond.length()));
					boolean b = ds.getInt("RBSREPGK") == li;
if (b) {
  System.out.println("Local sum "+cond+" for "+ds.getInt("RBSREPGK"));
} else {
  System.out.println("Local sum "+cond+" NOT for "+ds.getInt("RBSREPGK"));				  
}
					return b;
				}
			}
			return false;
		}
		boolean isLocalSum() {
		  return cond.toUpperCase().startsWith("L");
		}
		BigDecimal calcValue(DataSet ds) {
			if (ds.hasColumn("ID") != null) {//stavke gk
				if (cond.toUpperCase().startsWith("S")) {//saldo
					return ds.getBigDecimal("ID").add(ds.getBigDecimal("IP").negate());
				}
				if (cond.toUpperCase().startsWith("D")) {//duguje
					return ds.getBigDecimal("ID");
				}
				if (cond.toUpperCase().startsWith("P")) {//potrazuje
					return ds.getBigDecimal("IP");
				}
			} else {//stavke izvjestaja
				if (isLocalSum()) {//local sum
System.out.println("LOCAL__: ds.getBigDecimal(V"+kto+")");
					return ds.getBigDecimal("V"+kto);
				}
			}
			return new BigDecimal(0);
		}
		void checkPlainNumber() {
		  try {
        BigDecimal pnbd = new BigDecimal(cond);
        value = pnbd.setScale(2, BigDecimal.ROUND_HALF_UP);
        plnum = true;
      } catch (Exception e) {
        // TODO: handle exception
        plnum = false;
      }
		}
		public boolean isPlnum() {
		  return plnum;
		}
		public String toString() {
		  return super.toString()+":/  special = "+special+"/  cond = "+cond+"/  kto = "+kto+" value = "+value+" plain = "+plnum;
		}
	}
	class RGKRange {
	  int m1, m2, y;
	  RGKRange(int _m1, int _m2, int _y) {
	    m1 = _m1;
	    m2 = _m2;
	    y = _y;
	  }
	  StorageDataSet getDataSet() {
	    String godmj1 = y+Valid.getValid().maskZeroInteger(new Integer(m1),2);
	    String godmj2 = y+Valid.getValid().maskZeroInteger(new Integer(m2),2);
	    String qry = "SELECT * from gkkumulativi where knjig = '"+OrgStr.getKNJCORG()
	    +"' AND godmj between '"+godmj1+"' AND '"+godmj2+"'";
	    QueryDataSet ds  = Gkkumulativi.getDataModule().getTempSet(
	        Condition.equal("KNJIG", OrgStr.getKNJCORG())+" AND "
	        +Condition.between("GODMJ", godmj1, godmj2));
	    System.out.println(ds.getQuery().getQueryString());
	    ds.open();
	    return ds;
	  }
	}
}