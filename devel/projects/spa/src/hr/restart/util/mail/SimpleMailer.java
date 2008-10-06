/****license*****************************************************************
**   file: SimpleMailer.java
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
package hr.restart.util.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author dgradecak
 *
 */
public class SimpleMailer {
			
	private List listAttachements = new ArrayList();
	private String mailHost = null;
	private String from = null;
	private String recipients[] = null;
	private String subject = null;
	private String message = null;
	
	public SimpleMailer(){		
	}
	
	public SimpleMailer(String mailHost){
		setMailHost(mailHost);
	}
	
	synchronized public void addAttachement(String filePath){
		if(filePath.equals(""))
			throw new IllegalArgumentException("The file path can not be empty");
		else if(filePath == null)
			throw new NullPointerException("The file path can not be null");
		
		listAttachements.add(filePath);
	}
	
	synchronized public void sendMail () throws MessagingException {
		sendMail(getRecipients(), getSubject(), getMessage(), getFrom());
	}
	
	synchronized public void sendMail (String recipients[]) throws MessagingException {
		sendMail(recipients, getSubject(), getMessage(), getFrom());
	}	
	
	synchronized public void sendMail (String recipients[], String from) throws MessagingException {
		sendMail(recipients, getSubject(), getMessage(), from);
	}
	
	synchronized public void sendMail (String recipients[], String message , String from) throws MessagingException {
		sendMail(recipients, getSubject(), message, from);
	}
	
	synchronized public void sendMail (String recipients[], String subject, String message , String from) throws MessagingException {
		if (recipients == null)
			throw new NullPointerException("Recipients can not be null");		
		
	     boolean debug = true;
	 
	      try {
			//Set the host smtp address
			  Properties props = new Properties();
			  props.put("mail.smtp.host", getMailHost());
 
			 // create some properties and get the default Session
			 Session session = Session.getDefaultInstance(props, null);
			 session.setDebug(debug);
 
			 // create a message
			 Message msg = new MimeMessage(session);
 
			 // set the from and to address
			 InternetAddress addressFrom = new InternetAddress(from);
			 msg.setFrom(addressFrom);
 
			 InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
			 for (int i = 0; i < recipients.length; i++){
			     addressTo[i] = new InternetAddress(recipients[i]);
			 }
			 msg.setRecipients(Message.RecipientType.TO, addressTo);	 

			// Setting the Subject and Content Type
			 msg.setSubject(subject);			 			 
			 msg.setSentDate(new Date());	 			 
			 msg.setHeader("X-Mailer", "SPA ERP Version: "+hr.restart.util.versions.raVersionInfo.getCurrentVersion());
			 MimeMultipart multipart = new MimeMultipart();
			 
			 MimeBodyPart bodyMessage = new MimeBodyPart();
			 bodyMessage.setText(message);			 			 
			 multipart.addBodyPart(bodyMessage);			
			 
			 Iterator iterator = listAttachements.iterator();
			 while(iterator.hasNext()){
			 	String key = (String)iterator.next();			 	
			 
			 	MimeBodyPart attachement = new MimeBodyPart();
			 	File file = new File(key);
				attachement.setFileName(file.getName());
				attachement.setDataHandler(new DataHandler(file.toURL()));
				multipart.addBodyPart(attachement);
			 }			 

			 msg.setContent(multipart);			 
			 Transport.send(msg);
		
		} catch (Throwable e) {
			e.printStackTrace();
			throw new MessagingException(e.getLocalizedMessage());
		}
	 }

	/**
	 * @return Returns the mailHost.
	 */
	public String getMailHost() {
		return mailHost;
	}
	/**
	 * @param mailHost The mailHost to set.
	 */
	synchronized public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	/**
	 * @return Returns the from.
	 */
	public String getFrom(){
		if(from == null || from.equals(""))
			return "anonymous@anonymous.com";
		
		return from;
	}
	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return Returns the recipients.
	 */
	public String[] getRecipients() {
		return recipients;
	}
	/**
	 * @param recipients The recipients to set.
	 */
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		if(subject == null || subject.equals(""))
			return "no subject";
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		if(message == null || message.equals(""))
			return "";
		return message;
	}
	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
