/****license*****************************************************************
**   file: raMMat.java
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
package hr.restart.robno;

	import hr.restart.util.Aus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

	public class raMMat {

		private HashMap arguments = new HashMap();
		private Operator op_null = null;
		private Operator op_plus = null;
		private Operator op_minus = null;
		private Operator op_puta = null;
		private Operator op_djeljeno = null;
		private Cvor startPoint = null;
		private ArrayList operators = null;
		private int scale = 2;

		
		public int getScale() {
			return scale;
		}
		
		public void setScale(int scale) {
			this.scale = scale;
		}
		
		public void setArgument(String str,BigDecimal bd){
			arguments.put(str,bd);
		}
		
		public void setArguments(HashMap hm){
			arguments = hm;
		}
		
		public HashMap getArgument(){
			return arguments;
		}


		private void initOperators(){
			operators = new ArrayList();
			op_plus = new Operator("+",0);
			operators.add(op_plus);
			op_minus = new Operator("-",0);
			operators.add(op_minus);
			op_puta = new Operator("*",1);
			operators.add(op_puta);
			op_djeljeno = new Operator("/",1);
			operators.add(op_djeljeno);
			op_null = new Operator("Null-1 ",-1);
		}
		
		private Operator getOperator(String str){
			if (str.equalsIgnoreCase("+")){
				return op_plus;
			} else if (str.equalsIgnoreCase("-")){
				return op_minus;
			} else if (str.equalsIgnoreCase("*")){
				return op_puta;
			} else if (str.equalsIgnoreCase("/")){
				return op_djeljeno;
			}
			return null;
		}
		

		private boolean isOperator(String str){
			for (int i = 0;i< operators.size();i++){
				if (((Operator) operators.get(i)).getOperator().equalsIgnoreCase(str))
					return true;
			}
			return false;
		}

		private int isBeginWithOperator(String str){
			
//	System.out.println(str);		
			int iret = 0;
			Operator opt = null;
			for (int i = 0;i< operators.size();i++){
				opt = (Operator) operators.get(i);
				iret = opt.getOperator().length();
				if (str.substring(0,iret).equalsIgnoreCase(opt.getOperator())) {
					return iret;
				} 
				iret = 0;
			}
			return iret;
		}


		private StringBuffer removeChar(String str,char cchar){
			StringBuffer buf = new StringBuffer();
			for (int i =0;i<str.length();i++){
				if (str.charAt(i)!=cchar)
					buf.append(str.charAt(i));
			}
			return buf;
		}
		
		private void initEquilation(String str){
			startPoint= null;
			StringBuffer equilation = removeChar(str,' ');
			StringBuffer buf = new StringBuffer();
			Cvor currCvor = null;
			Cvor lijeviCvor= null;
			Cvor desniCvor = null;
			Operator lijeviOper= null;
			Operator desniOper = null;
			
			
			BigDecimal tmpBd;
			for (int i =0;i<equilation.length();i++){
				int ioper = isBeginWithOperator(equilation.substring(i));
				if (ioper == 0){
					buf.append(equilation.charAt(i));
					if (i==equilation.length()-1){
						if (arguments.containsKey(buf.toString())) {
							tmpBd = (BigDecimal) arguments.get(buf.toString()); 
						} else {
							tmpBd = new BigDecimal(buf.toString());
						}
						currCvor = new Cvor(lijeviCvor,null,
								lijeviCvor != null?lijeviCvor.getOperatorD():op_null,
										op_null,tmpBd);
						if (lijeviCvor!= null){
							lijeviCvor.setDesni(currCvor);
						}
						if (startPoint==null) {
							startPoint = currCvor; 
						}
						buf.delete(0,buf.length());
					}
				} else {
					desniOper = getOperator(equilation.substring(i,i+ioper));
					if (arguments.containsKey(buf.toString())) {
						tmpBd = (BigDecimal) arguments.get(buf.toString()); 
					} else {
						tmpBd = new BigDecimal(buf.toString());
					}
					currCvor = new Cvor(lijeviCvor,null,
							lijeviCvor != null?lijeviCvor.getOperatorD():op_null,
									desniOper,tmpBd);
					if (lijeviCvor!= null){
						lijeviCvor.setDesni(currCvor);
					}
					if (startPoint==null) {
						startPoint = currCvor; 
					}
					lijeviCvor = currCvor;
					buf.delete(0,buf.length());
				}
			}
			if (currCvor.getOperatorD()!= op_null){
				throw new RuntimeException("Neispravna formula završava sa "+currCvor.getOperatorD());
			}
		}

		public BigDecimal evaluate(BigDecimal first,BigDecimal second, Operator op){
			if (op.getOperator().equalsIgnoreCase("+")){
				return first.add(second);
			} else if (op.getOperator().equalsIgnoreCase("-")){
				return first.subtract(second);
			} else if (op.getOperator().equalsIgnoreCase("*")){
				return first.multiply(second);
			} else if (op.getOperator().equalsIgnoreCase("/")){
				if (second.doubleValue()!=0){
					return first.divide(second,scale,BigDecimal.ROUND_HALF_UP);
				} 
				return Aus.zero2;
			} 
			return null;
		}
		
		public void listAllCvors(){
			listCvors(startPoint);
		}
		
		private void listCvors(Cvor cv){
			if (cv !=null){
				System.out.println("Cvor "+cv);
				listCvors(cv.getDesni());
			}
		}
		
//		int errerr = 0;
		
		private BigDecimal evaluate(Cvor cv){
//			if (errerr >10) return null;
//				System.out.println(errerr+" : "+cv);
//				errerr++;
			
			if (cv.getLijevi()==null && cv.getDesni()== null){
				return cv.getBd();
			} else if (cv.getLijevi()!=null && cv.getDesni()== null){
				return evaluate(startPoint);
			} else if (cv.getDesni()!= null){
				// 2+3*4+5
				if ((cv.getOperatorL().getPrioritet() <= cv.getOperatorD().getPrioritet())
					&& (cv.getOperatorD().getPrioritet() >= cv.getDesni().getOperatorD().getPrioritet())){
					cv.bd = evaluate(cv.bd,cv.getDesni().bd,cv.getOperatorD());
					cv.setOperatorD(cv.getDesni().getOperatorD());
					cv.setDesni(cv.getDesni().getDesni());
					return evaluate(cv);
				} else {
					return evaluate(cv.getDesni());				
				}	
			}
			throw new RuntimeException("Opcija kojoj se nismo nadali !!"+cv);
		}
		

		public BigDecimal getResult(String equation,HashMap hm) throws RuntimeException{
//System.out.println("arguments.isEmpty() "+arguments.isEmpty());
//Iterator it = arguments.keySet().iterator();
//Object obj = null;
//while(it.hasNext()){
//obj=it.next();
//				System.out.println(obj+" --- "+arguments.get(obj));
//			
//			}

			
			initOperators();
			if (hm != null) {
				arguments = hm;	
			} 
			
			initEquilation(equation);
			return evaluate(startPoint);
		}
		
		private class Cvor {
			
			private BigDecimal bd = null;
			private Cvor lijevi = null;
			private Cvor desni =  null;
			private Operator operatorL=null;
			private Operator operatorD=null;
			
			private Cvor(){
				init(null,null,null,null,null);
			}
			public Cvor(Cvor lijevi,Cvor desni,Operator operatorL,Operator operatorD,BigDecimal bd){
				init(lijevi,desni,operatorL,operatorD,bd);
			}
			public Cvor(BigDecimal bd){
				init(null,null,null,null,bd);
			}
			
			public void init (Cvor lijevi,Cvor desni,Operator operatorL,Operator operatorD,BigDecimal value){
				setLijevi(lijevi);
				setDesni(desni);
				setOperatorD(operatorD);
				setOperatorL(operatorL);
				setBd(value);
			}
			
			public void remove(){
				if (getDesni()!= null){
					getDesni().setLijevi(getLijevi());
				}
				if (getLijevi()!= null){
					getLijevi().setDesni(getDesni());
				}
			}
			
			public void insertLijevo(Cvor cv){
				cv.setDesni(this);
				setLijevi(cv);
			}
			
			public void insertDesno(Cvor cv){
				cv.setLijevi(this);
				setDesni(cv);
			}
			
			public BigDecimal getBd() {
				return bd;
			}

			public void setBd(BigDecimal bd) {
				this.bd = bd;
			}

			public Cvor getDesni() {
				return desni;
			}

			public void setDesni(Cvor desni) {
				this.desni = desni;
			}

			public Cvor getLijevi() {
				return lijevi;
			}

			public void setLijevi(Cvor lijevi) {
				this.lijevi = lijevi;
			}


			/**
			 * @return Returns the operatorD.
			 */
			public Operator getOperatorD() {
				return operatorD;
			}
			/**
			 * @param operatorD The operatorD to set.
			 */
			public void setOperatorD(Operator operatorD) {
				this.operatorD = operatorD;
			}
			/**
			 * @return Returns the operatorL.
			 */
			public Operator getOperatorL() {
				return operatorL;
			}
			/**
			 * @param operatorL The operatorL to set.
			 */
			public void setOperatorL(Operator operatorL) {
				this.operatorL = operatorL;
			}
			
			public String toString(){
				return getOperatorL()+" : "+bd+" : "+getOperatorD();
			
			}
			
		}
		private class Operator{
			
			private String operator = null;
			int prioritet = -1;
			
			/**
			 * @param operator
			 * @param prioritet
			 */
			public Operator(String operator, int prioritet) {
				super();
				setOperator(operator);
				setPrioritet(prioritet);
			}
			/**
			 * @return Returns the operator.
			 */
			public String getOperator() {
				return operator;
			}
			/**
			 * @param operator The operator to set.
			 */
			public void setOperator(String operator) {
				this.operator = operator;
			}
			/**
			 * @return Returns the prioritet.
			 */
			public int getPrioritet() {
				return prioritet;
			}
			/**
			 * @param prioritet The prioritet to set.
			 */
			public void setPrioritet(int prioritet) {
				this.prioritet = prioritet;
			}
			
			public String toString() {
				return operator;	
			}
			
		}
	}
	


