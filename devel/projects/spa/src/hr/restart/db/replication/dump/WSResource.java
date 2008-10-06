/****license*****************************************************************
**   file: WSResource.java
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
 * Created on Nov 24, 2004
 *
 */
package hr.restart.db.replication.dump;

import java.rmi.RemoteException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;


/**
 * @author dgradecak
 *
 */
public class WSResource implements IResource{
    
    private String wsdl = null;
    private String operation = null;
    
    
    private Call call = null;

    public WSResource(){
        
    }
    
    public void init () throws Exception{
        
        Service  service = new Service();
        call    = (Call) service.createCall();

        call.setTargetEndpointAddress( new java.net.URL(getWsdl()) );
        call.setOperationName(getOperation());                 
    }
    
    /* (non-Javadoc)
     * @see hr.restart.db.replication.dump.IResource#execute(java.lang.String)
     */
    public boolean execute(String name, String txt[]) throws RuntimeException {
            
        boolean ret = false;        
        
        try {
            ret = ((Boolean) call.invoke(  new Object[] { name, txt } )).booleanValue();            
            
            
            
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
                
        return ret;
    }
    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }
    /**
     * @param operation The operation to set.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return Returns the wsdl.
     */
    public String getWsdl() {
        return wsdl;
    }
    /**
     * @param wsdl The wsdl to set.
     */
    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }
}
