package my.projects.demo.demo.flashcards;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

import reactor.core.publisher.Mono;

@RestController
public class CardsController{

	
	@GetMapping("/CardsController")
	public Mono<String> doGet(ServerWebExchange exchange){
		Mono<WebSession> session = exchange.getSession();
		
		int cid = Integer.parseInt(exchange.getRequest().getQueryParams().get("cid").toString().replace("[", "").replace("]", ""));
		
		return session.map(sess->{
			
			System.out.println(sess.getId());
			HashSet<Integer> done = (HashSet<Integer>) sess.getAttribute("done");
			ArrayList<String> words = (ArrayList<String>) sess.getAttribute("words");
			String ans = "";
			Cards cards = new Cards();
			
			if(cid<=0){
				cardBean cb = new cardBean();
				cb.setStatus(false);
				ans = cards.convertToJson(cb);
			}
			else if(words.size()>=cid)
			{
				ans = words.get(cid-1);
			}
			else
			{
				cards.done = done;
				cards.inputStream = getClass().getResourceAsStream("/static/resources/pdfs/500 Essential Words GRE Vocabulary Flash Cards.pdf");
				cards.setPageStart((int)sess.getAttribute("pageStart"));
				cards.setPageEnd((int)sess.getAttribute("pageEnd"));
				cards.setWordCount((int)sess.getAttribute("wordCount"));
				cards.setPageCount((int)sess.getAttribute("pageCount"));
				cards.words = words;
				cards.setTotalPages();
				
				try {
					ans = cards.getCards();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sess.getAttributes().put("pageEnd", cards.getPageEnd());
				sess.getAttributes().put("pageStart", cards.getPageStart());
				sess.getAttributes().put("done", cards.done);
				sess.getAttributes().put("wordCount", cards.getWordCount());
				sess.getAttributes().put("pageCount", cards.getPageCount());
				sess.getAttributes().put("words", cards.words);
			}
			return ans;
		});
		
		/*
		HashSet<Integer> done = (HashSet<Integer>) sess.getAttribute("done");
		ArrayList<String> words = (ArrayList<String>) sess.getAttribute("words");
		String ans = "";
		Cards cards = new Cards();
		
		if(cid<=0){
			cardBean cb = new cardBean();
			cb.setStatus(false);
			ans = cards.convertToJson(cb);
		}
		else if(words.size()>=cid)
		{
			ans = words.get(cid-1);
		}
		else
		{
			cards.done = done;
			cards.pdfFilePath = CardsController.class.getResource("/pdfs/500 Essential Words GRE Vocabulary Flash Cards.pdf").getPath();
			cards.setPageStart((int)sess.getAttribute("pageStart"));
			cards.setPageEnd((int)sess.getAttribute("pageEnd"));
			cards.setWordCount((int)sess.getAttribute("wordCount"));
			cards.setPageCount((int)sess.getAttribute("pageCount"));
			cards.words = words;
			cards.setTotalPages();
			
			try {
				ans = cards.getCards();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sess.setAttribute("pageEnd", cards.getPageEnd());
			sess.setAttribute("pageStart", cards.getPageStart());
			sess.setAttribute("done", cards.done);
			sess.setAttribute("wordCount", cards.getWordCount());
			sess.setAttribute("pageCount", cards.getPageCount());
			sess.setAttribute("words", cards.words);
		}
		
		response.setCharacterEncoding("UTF-8"); // so that encode string properly
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(ans);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
