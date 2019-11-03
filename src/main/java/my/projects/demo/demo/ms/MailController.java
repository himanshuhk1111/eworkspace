package my.projects.demo.demo.ms;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import my.projects.demo.demo.ms.dto.MailMessage;
import my.projects.demo.demo.ms.service.FileService;
import my.projects.demo.demo.ms.service.MailService;
import my.projects.demo.demo.utility.DTO.Response;
import reactor.core.publisher.Mono;

@RestController
public class MailController {
	
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
	
}
