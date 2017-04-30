package MemoruUnits;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Page <T> implements Serializable {
	private T content;
	private Long pageId;
	
	public Page(Long id, T content){
		this.setContent(content);
		this.setPageId(id);
	}
	public boolean	equals(Object obj){
		if(obj.hashCode()==this.pageId.hashCode())
			return true;
		return false;
	}
	@Override
	public int hashCode(){
		return pageId.hashCode();
	}

	public synchronized T getContent() {
		return content;
	}

	public synchronized void setContent(T content) {
		this.content = content;
	}

	public synchronized Long getPageId() {
		return pageId;
	}

	public synchronized void setPageId(Long pageId) {
		this.pageId = pageId;
	}
	public String toString(){
		return pageId.toString();
	}
}
