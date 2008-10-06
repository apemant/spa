/****license*****************************************************************
**   file: ParameterAdapter.java
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
 * Created on Dec 3, 2004
 */
package hr.restart.db.replication.logging;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

/**
 * @author andrej
 * adapts parameter value for writing in string representation of prepared statement
 */
public abstract class ParameterAdapter {
  private static Logger log = Logger.getLogger(ParameterAdapter.class); 
	
  public static String adaptNumberParameterValue(PreparedStatement pstmt, int parameterIndex, Number x) throws SQLException {
	  Object ret = null;
    try {
      if (log.isDebugEnabled()) {
          log.debug("*** adaptParameterValue: ");
      }
      //1.3
      //FIXME
      int parType = Types.DOUBLE;
      //1.4 
      //int parType = pstmt.getParameterMetaData().getParameterType(parameterIndex);
      
      if (log.isDebugEnabled()) {
        log.debug("*** real database sql type of "+x.getClass().getName()+" : "+parType);
      }
      switch (parType) {
      	case Types.DOUBLE:
      	  ret = new Double(x.doubleValue());
      	  if (log.isDebugEnabled()) {
            log.debug("*** adapting "+x.getClass().getName()+" to DOUBLE !!");
          }
      	  break;
      	case Types.FLOAT:
      	  ret = new Float(x.floatValue());
      	  break;
        	//continue with types if necessary
      	default:
         ret = x;
         break;
      }
      if (log.isDebugEnabled()) {
        log.debug("*** END OF adaptParameterValue");
      }
    } catch (Exception e) {
      log.fatal("!!!!!! adaptParameterValue could not adapt !!!!!!");
      e.printStackTrace();
      ret = x;
      throw new SQLException("!!!!!! adaptParameterValue could not adapt !!!!!!"+e.getMessage());
    }
	  return ret.toString();
  }
}
