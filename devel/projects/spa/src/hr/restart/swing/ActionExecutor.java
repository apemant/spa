/****license*****************************************************************
**   file: ActionExecutor.java
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

import javax.swing.SwingUtilities;

public abstract class ActionExecutor implements Runnable {
	protected SharedFlag ticket;
	protected Object obj;

	public void invoke() {
	  invoke(null);
	}
	
	public void invoke(Object o) {
		if (!ticket.request()) return;
		try {
		  obj = o;
			run();
		} finally {
          ticket.release();
		}
	}
	
	public void invokeLater() {
	  invokeLater(null);
	}
	
	public void invokeLater(Object o) {
      if (!ticket.request()) return;
		try {
		  obj = o;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						ActionExecutor.this.run();
					} finally {
                      ticket.release();
					}
				}
			});
		} catch (RuntimeException t) {
            ticket.release();
			throw t;
		}
	}
    
    public ActionExecutor() {
      this(new SharedFlag());
    }
    
    public ActionExecutor(SharedFlag ticket) {
      this.ticket = ticket;
    }
	
	public abstract void run();
}
