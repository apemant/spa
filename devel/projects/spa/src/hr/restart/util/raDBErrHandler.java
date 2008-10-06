/****license*****************************************************************
**   file: raDBErrHandler.java
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
import javax.swing.JOptionPane;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raDBErrHandler {

  public raDBErrHandler() {
  }
  static void handleErr(Exception e,com.borland.dx.dataset.DataSet ds) {
    e.printStackTrace();
    if (e instanceof SanityException)
    	JOptionPane.showMessageDialog(null,"Snimanje nije uspjelo: "+ e.getMessage(),
    			"Greška",JOptionPane.ERROR_MESSAGE);
    else
    	JOptionPane.showMessageDialog(null,"Pohrana u bazu neuspješna! Greška : "+ 
    			e.getMessage(),"Greška",JOptionPane.ERROR_MESSAGE);
    if (ds!=null) {
      ds.refetchRow(ds);
    }
  }
}