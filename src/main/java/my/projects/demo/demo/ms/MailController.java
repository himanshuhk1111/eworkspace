package my.projects.demo.demo.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import my.projects.demo.demo.ms.dto.MailMessage;
import my.projects.demo.demo.ms.service.MailService;
import my.projects.demo.demo.utility.DTO.Response;
import reactor.core.publisher.Mono;

@RestController
public class MailController {
	
	@Autowired
	MailService service;
	
	@PostMapping("/mail")
	public Mono<Response<Void>> sendMail(@RequestBody MailMessage message){
		
		service.processSend(message);
		return null;
	}
	
}
