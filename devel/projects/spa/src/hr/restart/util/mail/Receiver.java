/****license*****************************************************************
**   file: Receiver.java
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
 */
package hr.restart.util.mail;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Encrypter;
import hr.restart.util.FileHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

/**
 * rec = new hr.restart.util.mail.Receiver();
 * rec.setPassword("test1234");
 * Message[] msgs = rec.receiveMail();
 * @author andrej
 */
public class Receiver {
  SimpleReceiver srec;
  String senderFilter, subjectFilter;
  String[] headerFilters;
  private ArrayList _mparts;
  /**
   * Constructs receiver with default parameters
   * 
   *
   */
  public Receiver() {
    srec = new SimpleReceiver();
    getProtocol();
    getHost();
    getPort();
    getUser();
    getPassword();
  }
  public Message[] receiveMail() {
    return srec.receiveMail(senderFilter, subjectFilter, headerFilters);
  }
  
  public void deleteReceived() {
    srec.deleteReceived();
  }
  public void closeFolder(boolean expunge) {
    srec.closeFolder(expunge);
  }
  public Part[] getMessageParts(Message m) {
    _mparts = new ArrayList();
    try {
      computeMessageParts(m);
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return (Part[])_mparts.toArray(new Part[_mparts.size()]);
  }
  private void computeMessageParts(Part p) throws IOException, MessagingException {
    if (p.isMimeType("text/plain")) {
      _mparts.add(p);
    } else if (p.isMimeType("multipart/*")) {
      Multipart mp = (Multipart)p.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        Part bp = mp.getBodyPart(i);
        _mparts.add(bp);
        //computeMessageParts(bp);
      }
    } else if (p.isMimeType("message/rfc822")) {
      try {
        Part rp = (Part)p.getContent();
        _mparts.add(rp);
        computeMessageParts(rp);        
      } catch (ClassCastException e) {
        e.printStackTrace();
      }
    }
  }
  /**
   * Saves, verifies and decrypts attachments
   * @param m message with attachment
   * @param e decrypter for decrypting attachment (null - no decryption)
   * @param dir dir to save attachment to
   * @param SHA1 check attachment against this SHA1 (if null no check performed)
   * @return
   */
  public File[] saveAttachments(Message m, Encrypter e, File dir, String SHA1) {
    Part[] parts = getMessageParts(m);
    ArrayList attachments = new ArrayList();
    //get attachment message parts
    HashSet attparts = new HashSet();
    for (int i = 0; i < parts.length; i++) {
      try {
        if (Part.ATTACHMENT.equalsIgnoreCase(parts[i].getDisposition())) {
          attparts.add(parts[i]);
        }
      } catch (MessagingException e1) {
        e1.printStackTrace();
      }
    }
    //create dir if needed
    if (dir.exists()) {
      if (!dir.isDirectory()) {
        dir.delete(); 
      }
    } else {
      dir.mkdirs();
    }
    //save attachments
    for (Iterator iter = attparts.iterator(); iter.hasNext();) {
      Part attpart = (Part) iter.next();
      try {
        //write to file
        File f = new File(dir.getAbsolutePath()+File.separator+attpart.getFileName());
        int ectx = 1;
        String origf = f.getAbsolutePath(); 
        while (f.exists()) {
          String nf = origf+"."+ectx;
          f = new File(nf);
          ectx++;
        }
        File tempf = new File(f.getAbsolutePath()+"__plain.tmp");
        int bs = 2048;
        byte[] buffer = new byte[bs];
        FileOutputStream fos = new FileOutputStream(tempf);
        BufferedOutputStream bos = new BufferedOutputStream(fos, bs);
        InputStream is = attpart.getInputStream();
        int cnt;
        while ((cnt=is.read(buffer))!=-1) {
          bos.write(buffer,0,cnt);
        }
        bos.flush();
        bos.close();
        //verify sha1
        if (SHA1 != null) {
          String _sha1 = FileHandler.getSHA1(tempf.getAbsolutePath());
          if (!SHA1.equalsIgnoreCase(_sha1)) {
            System.out.println("SHA1 not match for "+f.getName()+"! Ignoring...");
            tempf.delete();
            continue;
          }
        }
        //decrypt
        if (e == null) {
          if (!tempf.renameTo(f)) {
            System.out.println("Can't rename "+tempf.getAbsolutePath()+" to "+f.getAbsolutePath());
            //throw new IOException("Can't rename "+tempf.getAbsolutePath()+" to "+f.getAbsolutePath());
          }
        } else {
          e.decrypt(tempf, f);
          if (!tempf.delete()) {
            System.out.println("Can't delete "+tempf.getAbsolutePath());
            //throw new IOException("Can't delete "+tempf.getAbsolutePath());
          }
        }
        attachments.add(f);
      } catch (MessagingException e1) {
        e1.printStackTrace();
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return (File[])attachments.toArray(new File[attachments.size()]);
  }
  /**
   * @return Returns the host.
   */
  public String getHost() {
    if (srec.getHost() == null) {
      srec.setHost(frmParam.getParam("sisfun","recmailhost",Mailer.getMailProperties().getProperty("mailhost"),"Host s kojeg se PRIMA e-mail"));
    }
    return srec.getHost();
  }
  /**
   * @param host The host to set.
   */
  public void setHost(String _host) {
    srec.setHost(_host);
  }
  /**
   * @return Returns the password.
   */
  public String getPassword() {
    if (srec.getPassword() == null) {
      srec.setPassword(frmParam.getParam("sisfun","recmailpasswd","","Password za PRIMANJE e-maila")); 
    }
    return srec.getPassword();
  }
  /**
   * @param password The password to set.
   */
  public void setPassword(String _password) {
    srec.setPassword(_password);
  }
  /**
   * @return Returns the port.
   */
  public int getPort() {
    return srec.getPort();
  }
  /**
   * @param port The port to set.
   */
  public void setPort(int _port) {
    srec.setPort(_port);
  }
  /**
   * @return Returns the protocol.
   */
  public String getProtocol() {
    if (srec.getProtocol() == null) {
      srec.setProtocol(frmParam.getParam("sisfun","recmailprotocol","pop3","Protokol za PRIMANJE e-maila"));
    }
    return srec.getProtocol();
  }
  /**
   * @param protocol The protocol to set.
   */
  public void setProtocol(String _protocol) {
    srec.setProtocol(_protocol);
  }
  /**
   * @return Returns the user.
   */
  public String getUser() {
    if (srec.getUser() == null) {
      srec.setUser(frmParam.getParam("sisfun","recmailuser","testiranje","User za PRIMANJE e-maila"));
    }
    return srec.getUser();
  }
  /**
   * @param user The user to set.
   */
  public void setUser(String _user) {
    srec.setUser(_user);
  }
  /**
   * @param senderFilter The senderFilter to set.
   */
  public void setSenderFilter(String senderFilter) {
    this.senderFilter = senderFilter;
  }
  /**
   * @param subjectFilter The subjectFilter to set.
   */
  public void setSubjectFilter(String subjectFilter) {
    this.subjectFilter = subjectFilter;
  }
  public void setHeaderFilters(String[] headerFilters) {
    this.headerFilters = headerFilters;
  }
  
  //for test browsing message
  public void printHeaders(Part m) {
    try {
      Enumeration headers = m.getAllHeaders();
      while (headers.hasMoreElements()) {
        Header h = (Header) headers.nextElement();
        System.out.println(h.getName()+" : "+h.getValue());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void printMessages(Message[] msgs) {
    for (int i = 0; i < msgs.length; i++) {
      System.out.println("Message:\n---------------------------");
      printHeaders(msgs[i]);
      System.out.println("  Parts:\n---------------------------");
      Part[] parts = getMessageParts(msgs[i]);
      for (int j = 0; j < parts.length; j++) {
        System.out.println("    part["+j+"]:\n---------------------------");
        printHeaders(parts[j]);
        try {
          System.out.println("CONTENT: "+parts[j].getContent());
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (MessagingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
}
