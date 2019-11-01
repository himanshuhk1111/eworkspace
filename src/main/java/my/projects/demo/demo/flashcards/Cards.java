package my.projects.demo.demo.flashcards;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Cards {

	String pdfFilePath ="";
	Random r = new Random();
	HashSet<Integer> done;
	ArrayList<String> words;
	
	private int pageStart;
	private int pageEnd;
	private int totalPages;
	private int wordCount;
	private int pageCount;
	public InputStream inputStream;
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages() {
		this.totalPages = pageEnd - pageStart + 1;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	
	
	public void setPageCount(int x) {
		this.pageCount = x;
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	public void setWordCount(int x) {
		this.wordCount = x;
	}
	
	 // include the extremes also
	
	public int getPageStart() {
		return pageStart;
	}
	
	public void setPageStart(int x) {
		this.pageStart = x;
	}
	
	public void setPageEnd(int x) {
		this.pageEnd = x;
	}
	
	public int getPageEnd() {
		return pageEnd;
	}

	public String convertToJson(cardBean card){
		ObjectMapper mapper = new ObjectMapper();
		String res="";
		try {
			res = mapper.writeValueAsString(card);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	@SuppressWarnings("finally")
	public String getCards() throws IOException{
		//pdfFilePath = "D:/jatin/gre/500 Essential Words GRE Vocabulary Flash Cards.pdf";
		//System.out.println(ClassPath.getClassPath());
//		System.out.println();
		//pdfFilePath = "500 Essential Words GRE Vocabulary Flash Cards.pdf";
		
		
		PDDocument pdf = null;
		
		if(pdfFilePath!=null && !pdfFilePath.equals("")) pdf = PDDocument.load(new File(pdfFilePath));
		else pdf = PDDocument.load(inputStream);
		PDFTextStripper textStripper=new PDFTextStripper();
		
		
		cardBean card = new cardBean();
		if(totalPages<=pageCount){
        	card.setStatus(false);
        	return convertToJson(card);
        }
		
		String resText="";
		int page = -1;
		
		System.out.println(totalPages);
		
		while(page == -1||done.contains(page)){
			page = r.nextInt(totalPages) + pageStart;
			if(page%2!=0)
				page--;
		}
		
		card.setStatus(true);
        pageCount+=2;
        wordCount++;
        card.setCid(wordCount);
        this.setPageCount(pageCount);
        done.add(page);
		
        
        
		try{
			System.out.println(page);
	    	StringBuilder text = new StringBuilder(1024);
	    		
//	    	pdf.getPage(page-1).pipe(new OutputTarget(text));
	        
	    	
	    	
	    	textStripper.setStartPage(page);
			textStripper.setEndPage(page);
			resText = textStripper.getText(pdf).trim();
			
//	    	resText = text.toString().trim();
	        card.setPageNo(page);
	        
	        Scanner sc = new Scanner(resText);
	        
	        card.setWord(sc.next());
	        card.setType(sc.next());
	        card.setOther(sc.next());
	        
	        
	        System.out.println("**************************************************************************************");
	        System.out.println("**************************************************************************************");
	        System.out.println(wordCount+"                          "+resText+"                             ");
	        System.out.println("--------------------------------------------------------------------------------------");
	        //String n = sc.next();
	        
	        
	        textStripper.setStartPage(page+1);
			textStripper.setEndPage(page+1);
			
			resText = textStripper.getText(pdf).trim();
	        text = new StringBuilder(1024);
	        text.append(resText);
	        
//	        pdf.getPage(page).pipe(new OutputTarget(text));
	        
	        
	        
	        
	        int start = 0;
	        int end = text.indexOf("Usage:");
	        card.setDef(text.substring(start,end).trim());
	        
	        start = text.indexOf("Usage:");
	        end = text.indexOf("Related Words:");
	        card.setUsage(text.substring(start,end).trim());
	        
	        System.out.println(card.getWord());
	        
	        
	        start = text.indexOf("Related Words:");
	        end = text.indexOf("More Info:");
	        card.setRelWords(text.substring(start,end).trim());
	        
	        start = text.indexOf("More Info:");
	        card.setInfo(text.substring(start,text.length()).trim());
	        
	        
	        
	        
//	        resText = text.toString();
	        //System.out.println(resText);
	        
	        //resText = this.convertToJson(card);

			
		}
		catch(Exception e){
			e.getMessage();
		}
		finally{
			pdf.close();
			this.words.add(this.convertToJson(card));
			return this.words.get(card.getCid()-1);
		}
	}
	/*public static void main(String[] args) {
		Cards c = new Cards();
		c.pageStart = 10;
		c.pageEnd = 20;
		c.setTotalPages();
		System.out.println(c.getCards());
	}*/
}
