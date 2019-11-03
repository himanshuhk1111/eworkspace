package my.projects.demo.demo.ms;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import my.projects.demo.demo.ms.dto.MailMessage;
import my.projects.demo.demo.ms.service.FileService;
import my.projects.demo.demo.ms.service.MailService;
import my.projects.demo.demo.utility.DTO.Response;
import reactor.core.publisher.Mono;

@RestController
public class MailController {
	
	

	@Value("${path.eworkspace}")
	private String eworkspace;
	
	@Autowired
	MailService service;
	
	@Autowired
	FileService fileService;
	
	@PostMapping("/mail")
	public Mono<Response<Void>> sendMail(/*@RequestBody Mono<MailMessage> message*/ @ModelAttribute Mono<MailMessage> message){
	
		return message.map(msg->{
			boolean isSend;
			try {
				isSend = service.processSend(msg);
				if(isSend){
					Response<Void> res = new Response<Void>();
					res.setMessage("SUCCESS");
					res.setStatus(HttpStatus.OK.value());
					return res;
				}
				else{
					Response<Void> res = new Response<Void>();
					res.setMessage("FAILED");
					res.setStatus(HttpStatus.BAD_REQUEST.value());
					return res;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Response<Void> res = new Response<Void>();
				res.setMessage("FAILED");
				res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return res;
			}
			
		});
	}
	
	
	@PostMapping("/mail/upload")
	public Mono<Response<Void>> uploadAttachment(ServerWebExchange exchange){

		return exchange.getMultipartData().map(t->{
			Map<String,Part> e = t.toSingleValueMap();
			e.get("k").content().map(buffer->{
				return buffer.asInputStream();
			});
			return null;
		});
	}
	
	
	@PostMapping("/mail/check")
	public Mono<Response<Void>> checkMailConnection(@ModelAttribute MailMessage msg){
		int port = 587;
	    String host = "smtp.gmail.com";
	    String user = "himanshuhk1111@gmail.com";
	    String pwd = msg.getPassword();

	    try {
	        Properties props = new Properties();
	        // required for gmail 
	        props.put("mail.smtp.starttls.enable","true");
	        props.put("mail.smtp.auth", "true");
	        // or use getDefaultInstance instance if desired...
	        Session session = Session.getInstance(props, null);
	        Transport transport = session.getTransport("smtp");
	        transport.connect(host, port, user, pwd);
	        transport.close();
	        System.out.println("success");
	     } 
	     catch(AuthenticationFailedException e) {
	           System.out.println("AuthenticationFailedException - for authentication failures");
	           e.printStackTrace();

		   		Response<Void> res = new Response<Void>();
		   		res.setMessage(e.getLocalizedMessage());
		   		res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		   		return Mono.just(res);
	     }
	     catch(MessagingException e) {
	           System.out.println("for other failures");
	           e.printStackTrace();

		   		Response<Void> res = new Response<Void>();
		   		res.setMessage("for other failures");
		   		res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		   		return Mono.just(res);
	     }
		Response<Void> res = new Response<Void>();
		res.setMessage("SUCCESS");
		res.setStatus(HttpStatus.OK.value());
		return Mono.just(res);
	}
	
	@GetMapping("/files")
	public Mono<Response<List<String>>> getFiles(){
		
		File file = new File(eworkspace);
		List<String>files = Arrays.asList(file.listFiles()).stream().map(t->t.getName()).collect(Collectors.toList());
		
		Response<List<String>> res = new Response<List<String>>();
		res.setData(files);
		res.setMessage("SUCCESS");
		res.setStatus(HttpStatus.OK.value());
		return Mono.just(res);
	}
	
	@GetMapping("/file")
	public Mono<Resource> getFile(@RequestParam("filename") String fileName){
		Resource file = new FileSystemResource(eworkspace+fileName);
		return Mono.just(file);
	}
	
}
