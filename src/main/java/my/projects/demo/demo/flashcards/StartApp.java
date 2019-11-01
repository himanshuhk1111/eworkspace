package my.projects.demo.demo.flashcards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

import reactor.core.publisher.Mono;

@RestController
public class StartApp{

	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("in here doget");
	}*/
	@PostMapping("/StartApp")
	public Mono<Void> doPost(ServerWebExchange exchange) throws IOException{
		
		
		/*request.formData().subscribe(t->{
			t.toSingleValueMap();
		});
		*/
		Mono<WebSession> session = exchange.getSession();
		ServerHttpResponse response = exchange.getResponse();
		return exchange.getFormData().flatMap(map->{
			int pageStart = Integer.parseInt(map.get("pageStart").toString().replace("[", "").replace("]", ""));
			int pageEnd = Integer.parseInt(map.get("pageEnd").toString().replace("[", "").replace("]", ""));
			System.out.println("here "+pageStart+" "+pageEnd);
			
			return session.flatMap(sess->{
				HashSet<Integer> done = new HashSet<Integer>();
				sess.getAttributes().put("pageEnd", pageEnd);
				sess.getAttributes().put("pageStart", pageStart);
				sess.getAttributes().put("done", done);
				sess.getAttributes().put("wordCount", 0);
				sess.getAttributes().put("pageCount", 0);
				sess.getAttributes().put("words", new ArrayList<String>());
				System.out.println(sess.getId());
				response.setStatusCode(HttpStatus.SEE_OTHER);
//				ServerResponse.permanentRedirect(URI.create("http://localhost:8081/cards.html"));
				response.getHeaders().setLocation(URI.create("/cards.html"));
				return response.setComplete();
//				return Mono.just("redirect:/cards.html");
			});
		});
		
		
	/*	request.getBody().subscribe(t->{
			BufferedReader br = new BufferedReader(new InputStreamReader(t.asInputStream()));
			try {
				System.out.println(br.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});*/
		
	/*	request.getBody().subscribe(t->{
			t.asByteBuffer();
		});
		
		int pageEnd = Integer.parseInt(request.getQueryParams().get("pageEnd").toString());
		int pageStart = Integer.parseInt(request.getQueryParams().get("pageStart").toString());
		*/
	}

}
