package my.projects.demo.demo.ms.dto;

import java.io.File;
import java.util.List;

import org.springframework.http.codec.multipart.FilePart;

public class MailMessage {
	
	private String from;
	private String fileId;
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private String message;
	private List<FilePart> attachments;
	private String subject;
	
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public List<String> getTo() {
		return to;
	}
	public void setTo(List<String> to) {
		this.to = to;
	}
	public List<String> getCc() {
		return cc;
	}
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	public List<String> getBcc() {
		return bcc;
	}
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<FilePart> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<FilePart> attachments) {
		this.attachments = attachments;
	}

}
