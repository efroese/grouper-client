/*
 * @author mchyzer
 * $Id$
 */
package edu.internet2.middleware.grouperClientExt.edu.internet2.middleware.grouperClientMail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.internet2.middleware.grouperClient.util.GrouperClientUtils;
import edu.internet2.middleware.grouperClientExt.edu.internet2.middleware.morphString.Morph;


/**
 * use this chaining utility to send email
 */
public class GrouperClientEmail {

  /** who this email is going to (comma separated) */
  private String to;
  
  /** subject of email */
  private String subject;
  
  /** email address this is from */
  private String from;
  
  /** body of email (currently HTML is not supported, only plain text) */
  private String body;
  
  /**
   * 
   */
  public GrouperClientEmail() {
    //empty 
  }
  
  /**
   * set the to address
   * @param theToAddress 
   * @return this for chaining
   */
  public GrouperClientEmail setTo(String theToAddress) {
    this.to = theToAddress;
    return this;
  }
  
  /**
   * set subject
   * @param theSubject
   * @return this for chaining
   */
  public GrouperClientEmail setSubject(String theSubject) {
    this.subject = theSubject;
    return this;
  }
  
  /**
   * set the body
   * @param theBody
   * @return this for chaining
   */
  public GrouperClientEmail setBody(String theBody) {
    this.body = theBody;
    return this;
  }
 
  /**
   * set the from address
   * @param theFrom
   * @return the from address
   */
  public GrouperClientEmail setFrom(String theFrom) {
    this.from = theFrom;
    return this;
  }

  /**
   * try an email
   * @param args
   */
  public static void main(String[] args) {
    new GrouperClientEmail().setBody("hey").setSubject("subject").setTo("mchyzer@yahoo.com,mchyzer@isc.upenn.edu").send();
  }
  
  /**
   * send the email
   */
  public void send() {
    
    try {
      //mail.smtp.server = whatever.school.edu
      //#mail.from.address = noreply@school.edu
      String theFrom = GrouperClientUtils.defaultIfEmpty(this.from, GrouperClientUtils.propertiesValue("grouperClient.mail.from.address", false));
      if (GrouperClientUtils.isBlank(theFrom)) {
        throw new RuntimeException("You need to specify the from email address mail.from.address in grouper.client.properties");
      }
      
      String smtpServer = GrouperClientUtils.propertiesValue("grouperClient.mail.smtp.server", false);
      if (GrouperClientUtils.isBlank(smtpServer)) {
        throw new RuntimeException("You need to specify the from smtp server mail.smtp.server in grouper.client.properties");
      }
      
      String subjectPrefix = GrouperClientUtils.defaultString(GrouperClientUtils.propertiesValue("grouperClient.mail.subject.prefix", false));
      
      final String SMTP_USER = GrouperClientUtils.propertiesValue("grouperClient.mail.smtp.user", false); 
      
      String smtpPass = GrouperClientUtils.propertiesValue("grouperClient.mail.smtp.pass", false); 
      
      final String SMTP_PASS = GrouperClientUtils.isBlank(smtpPass) ? null : Morph.decryptIfFile(smtpPass);
      
      Properties properties = new Properties();
      
      properties.put("mail.host", smtpServer);
      properties.put("mail.transport.protocol", "smtp");
      
      Authenticator authenticator = null;
  
      //this has never been tested... :)
      if (!GrouperClientUtils.isBlank(SMTP_USER)) {
        properties.setProperty("mail.smtp.submitter", SMTP_USER);
        properties.setProperty("mail.smtp.auth", "true");
        
        authenticator = new Authenticator() {
          /**
           * 
           * @see javax.mail.Authenticator#getPasswordAuthentication()
           */
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
          }
        };
      }
      
      Session session = Session.getInstance(properties, authenticator);
      Message message = new MimeMessage(session);
      
      String theTo = this.to;
      
      if (!GrouperClientUtils.isBlank(theTo)) {
        
        theTo = GrouperClientUtils.replace(theTo, ";", ",");
        String[] theTos = GrouperClientUtils.splitTrim(theTo, ",");
        for (String aTo : theTos) {
          message.addRecipient(RecipientType.TO, new InternetAddress(aTo));
        }
        
      }
      message.addFrom(new InternetAddress[] { new InternetAddress(theFrom) });
  
      String theSubject = GrouperClientUtils.defaultString(subjectPrefix) + this.subject;
      message.setSubject(theSubject);
      
      message.setContent(this.body, "text/plain");
      Transport.send(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
}
