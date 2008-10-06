/****license*****************************************************************
**   file: BshLineVerifier.java
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
 * Created on Dec 28, 2004
 */
package hr.restart.util.textconv;

import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * @author andrej
 * verifies line with given code
 */
public class BshLineVerifier implements ILineVerifier {
  private static Logger log = Logger.getLogger(BshLineVerifier.class);
  private String verifyStatement;
  private Interpreter interpreter;
  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILineVerifier#verify(hr.restart.util.textconv.ILine)
   */
  public boolean verify(ILine line) {
    try {
      getInterpreter().set("line",line);
      getInterpreter().eval(getVerifyStatement());
      Boolean b = (Boolean)getInterpreter().get("result");
      if (log.isDebugEnabled()) {
        log.debug("verify statement evaluated with result "+b);
      }
      return b.booleanValue();
    } catch (EvalError e) {
      e.printStackTrace();
      return false;
    }
    
  }
  
  public String getVerifyStatement() {
    return verifyStatement;
  }
  /**
   * Statement must have boolean variable named result, and optionaly ILine variable named line
   * @param statement
   */
  public void setVerifyStatement(String statement) {
    verifyStatement = statement;
  }
  public Interpreter getInterpreter() {
    if (interpreter == null) {
      interpreter = new Interpreter();
    }
    return interpreter;
  }
}
