package my.projects.demo.demo.flashcards;

public class cardBean {

	private int cid;
	private String word;
	private String type;
	private String other;
	private String def;
	private String usage;
	private String relWords;
	private String info;
	private boolean status;
	private int pageNo;
	
	public int getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(int x) {
		this.pageNo = x;
	}
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getRelWords() {
		return relWords;
	}
	public void setRelWords(String relWords) {
		this.relWords = relWords;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
