package my.projects.demo.demo.ms.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import my.projects.demo.demo.ms.dto.MailMessage;

@Component
public class MailService {

	
	private static Properties getProperties(){
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";
	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "587");
	      return props;
	}
	
	
	public boolean processSend(MailMessage msg){

	      // Sender's email ID needs to be mentioned
	      String from = "himanshuhk1111@gmail.com";
	      final String username = "himanshuhk1111";//change accordingly
	      final String password = "erudite@2017";//change accordingly

	      Properties props = getProperties();
	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
		   }
	         });

	      try {
		   // Create a default MimeMessage object.
		   Message message = new MimeMessage(session);
		
		   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));
		
		   // Set To: header field of the header.
		   for(String to : msg.getTo()){
			   message.setRecipients(Message.RecipientType.TO,
		               InternetAddress.parse(to));
		   }
		   
		   // Set BCC: header field of the header.
		   for(String bcc : msg.getBcc()){
			   message.setRecipients(Message.RecipientType.BCC,
		               InternetAddress.parse(bcc));
		   }
		   
		   // Set CC: header field of the header.
		   for(String cc : msg.getCc()){
			   message.setRecipients(Message.RecipientType.CC,
		               InternetAddress.parse(cc));
		   }
		   
		   
		
		   // Set Subject: header field
		   message.setSubject(msg.getSubject());
		
		   // Now set the actual message
		   message.setText(msg.getMessage());

		   // TODO: implement email sending
		   
		   // Send message
		   Transport.send(message);

		   System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	         e.printStackTrace();
	      }
		
		return true;
	}
}
