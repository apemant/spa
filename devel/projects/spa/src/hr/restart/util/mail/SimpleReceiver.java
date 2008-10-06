/****license*****************************************************************
**   file: SimpleReceiver.java
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
 * Created on Oct 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.mail;

import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

/**
 * @author andrej
 */
public class SimpleReceiver {
  String protocol, host, user, password;
  Message[] receivedMessages;
  int[] receivedMessageNums;
  int port = -1;
  private Folder folder;
//mail.store.protocol, mail.transport.protocol, mail.host, mail.user, and mail.from
  /**
   * Constructs receiver with mandatory params
   */
  public SimpleReceiver(String _protocol, String _host, int _port, String _user, String _password) {
    setProtocol(_protocol);
    setHost(_host);
    setPort(_port);
    setUser(_user);
    setPassword(_password);
  }
  /**
   * Constructs receiver with no values
   */
  public SimpleReceiver() {
   
  }
  public synchronized Message[] receiveMail(String senderFilter, String subjectFilter, String[] headerFilters) {
    if (getHost()==null || getProtocol() == null || getUser() == null || getPassword() == null) {
      throw new IllegalArgumentException("Parameters not set!!");
    }
    try {
      ArrayList arr_messages = new ArrayList();
      Properties props = new Properties();
      Session sess = Session.getInstance(props);
      Store store = sess.getStore(getProtocol());
      store.connect(getHost(),getPort(), getUser(), getPassword());
      folder = store.getDefaultFolder().getFolder("INBOX");
      try {
        folder.open(Folder.READ_WRITE);
      } catch (Exception fe) {
        fe.printStackTrace();
        System.out.println("W: Opening folder "+folder+" read only");
        folder.open(Folder.READ_ONLY);
      }
      int mcount = folder.getMessageCount();
      System.out.println(mcount+" messages ...");
      Message[] msgs = folder.getMessages();

      FetchProfile fp = new FetchProfile();
      fp.add(FetchProfile.Item.ENVELOPE);
      folder.fetch(msgs, fp);
      for (int i = 0; i < folder.getMessageCount(); i++) {
        if (checkDeleted(msgs[i]) && checkSender(senderFilter,msgs[i]) 
            && checkSubject(subjectFilter, msgs[i])
            && checkHeaderFilter(headerFilters, msgs[i]) ) {
          arr_messages.add(msgs[i]);
        }
      }
      receivedMessages = (Message[])arr_messages.toArray(new Message[arr_messages.size()]);
      receivedMessageNums = new int[receivedMessages.length];
      for (int i = 0; i < receivedMessages.length; i++) {
        receivedMessageNums[i] = receivedMessages[i].getMessageNumber();
      }
      return receivedMessages;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  /**
   * Flags all received messages deleted and closes folder so call it as last method.
   * @see receiveMail(String, String, String[])
   */
  public void deleteReceived() {
    try {
      checkFlags();
      folder.setFlags(receivedMessageNums, new Flags(Flags.Flag.DELETED), true);
      closeFolder(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Closes inbox folder
   * @param expunge delete messages flaged for deletion or not
   */
  public void closeFolder(boolean expunge) {
    try {
      folder.close(expunge);
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
  private void checkFlags() throws NullPointerException {
    if (folder == null) throw new NullPointerException("Inbox folder not set! Did you receiveMail(String, String, String[])?");
    if (receivedMessageNums == null) throw new NullPointerException("No received messages! Did you receiveMail(String, String, String[])?");
  }
  /**
   * @param message
   * @return
   */
  private boolean checkDeleted(Message message) {
    try {
      return !message.isSet(Flags.Flag.DELETED);
    } catch (MessagingException e) {
      e.printStackTrace();
      System.out.println("Cannot check if message is deleted, probably not.");
      return true;
    }
  }
  /**
   * @param headerFilters
   * @param message
   * @return
   */
  private boolean checkHeaderFilter(String[] headerFilters, Message message) {
    if (headerFilters == null || headerFilters.length == 0) return true;
    for (int i = 0; i < headerFilters.length; i++) {
      StringTokenizer tok = new StringTokenizer(headerFilters[i],"=");
      String key = tok.nextToken().trim();
      String val = tok.nextToken().trim();
      try {
        if (message.getHeader(key)[0].indexOf(val)==-1) return false;
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
    return true;
  }
  /**
   * @param senderFilter
   * @param message
   * @return
   */
  private boolean checkSender(String senderFilter, Message message) {
    if (senderFilter == null || "".equals(senderFilter)) return true;
    try {
      return getSender(message).indexOf(senderFilter) != -1;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  public static String getSender(Message message) {
    try {
      Address[] addr = message.getFrom();
	    for (int j = 0; j < addr.length; j++) {
	      if (addr[j] instanceof InternetAddress) {
	        String iaddr = ((InternetAddress)addr[j]).getAddress();
	        return iaddr;
	      } else {
	      }
	    }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  /**
   * @param subjectFilter
   * @param message
   * @return
   */
  private boolean checkSubject(String subjectFilter, Message message) {
    if (subjectFilter == null || "".equals(subjectFilter)) return true;
    try {
      String subj = message.getSubject();
//System.out.println("Subject "+subj);
      return subj.indexOf(subjectFilter) != -1;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  /**
   * @return Returns the host.
   */
  public String getHost() {
    return host;
  }
  /**
   * @param host The host to set.
   */
  public void setHost(String host) {
    this.host = host;
  }
  /**
   * @return Returns the password.
   */
  public String getPassword() {
    return password;
  }
  /**
   * @param password The password to set.
   */
  public void setPassword(String password) {
    this.password = password;
  }
  /**
   * @return Returns the port.
   */
  public int getPort() {
    return port;
  }
  /**
   * @param port The port to set.
   */
  public void setPort(int port) {
    this.port = port;
  }
  /**
   * @return Returns the protocol.
   */
  public String getProtocol() {
    return protocol;
  }
  /**
   * @param protocol The protocol to set.
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }
  /**
   * @return Returns the user.
   */
  public String getUser() {
    return user;
  }
  /**
   * @param user The user to set.
   */
  public void setUser(String user) {
    this.user = user;
  }
}