package my.projects.demo.demo.ms.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import my.projects.demo.demo.ms.dto.MailMessage;
import reactor.core.publisher.Mono;

@Component
public class MailService {

	
	@Value("${path.eworkspace}")
	private String eworkspace;
	
	
	private static Properties getProperties(){
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";
	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.imaps.partialfetch", false);
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "587");
	      return props;
	}
	
	
	public boolean processSend(MailMessage msg) throws IOException{

	      // Sender's email ID needs to be mentioned
	      String from = "himanshuhk1111@gmail.com";
	      final String username = "himanshuhk1111";//change accordingly
	      final String password = msg.getPassword();//change accordingly

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
		   if(!ObjectUtils.isEmpty(msg.getBcc())){
			   for(String bcc : msg.getBcc()){
				   message.setRecipients(Message.RecipientType.BCC,
			               InternetAddress.parse(bcc));
			   }
		   }
		   
		   // Set CC: header field of the header.

		   if(!ObjectUtils.isEmpty(msg.getCc())){
			   for(String cc : msg.getCc()){
				   message.setRecipients(Message.RecipientType.CC,
			               InternetAddress.parse(cc));
			   }
		   }
		   
		   // Set Subject: header field
		   message.setSubject(msg.getSubject());
		
		   // Now set the actual message
		   message.setText(msg.getMessage());

		   //set attachment
		   /*message.setContent(transformMailAttachments(msg.getAttachments()));
		   
		   Multipart parts = (Multipart)message.getContent();

		   MimeBodyPart text = new MimeBodyPart();
		   text.setText(msg.getMessage(),null,"html");
		   parts.addBodyPart(text);
			
*/		   // Send message
		   Transport.send(message);

		   System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	         e.printStackTrace();
	         return false;
	      }
		
		return true;
	}
	
	private Multipart transformMailAttachments(List<FilePart> files) throws MessagingException{
		Multipart multipart = new MimeMultipart();
		if(!ObjectUtils.isEmpty(files)){
			for(FilePart file:files){
				
				file.transferTo(new File(eworkspace+file.filename()))
				.then(Mono.just("")).subscribe(t->{
					MimeBodyPart attachment;
					attachment = new MimeBodyPart();
					try {
						attachment.setFileName(file.filename());
						attachment.setDataHandler(new DataHandler(new FileDataSource(new File(eworkspace+file.filename()))));
						multipart.addBodyPart(attachment);
						System.out.println("Attachment :: "+file.filename());
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				});
			}
		}
		return multipart;
	}
}
