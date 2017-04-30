package MemoruUnits;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import hit.util.MMULogger;

public class RAM {

	private int	initialCapacity;
	private Map<Long,Page<byte[]>> pages;
	
	public RAM(int initialCapacity) throws SecurityException, IOException{
		setInitialCapacity(initialCapacity);
		pages = new HashMap<Long,Page<byte[]>>(initialCapacity);
	}
	public int getInitialCapacity() {
		return initialCapacity;
	}
	public void setInitialCapacity(int initialCapacity) throws SecurityException, IOException {
		this.initialCapacity = initialCapacity;
		MMULogger.getInstance().write("RC"+initialCapacity+"\n", Level.INFO);
	}
	public void addPages(Page<byte[]>[] addPages){
		for(Page<byte[]>page:addPages)
			pages.put(page.getPageId(), page);
	}
    public synchronized void addPage(Page<byte[]> addPages){
		pages.put(addPages.getPageId(), addPages);
	}
	public Page<byte[]>getPage(Long pageId){
		return pages.get(pageId);
	}
	@SuppressWarnings("unchecked" )
	public Page<byte[]>[] getPages(Long[] pageIds){
		if(pageIds==null)                                    //if get null return all the page in the ram fo shutdown
			return pages.values().toArray((Page<byte[]>[])new Page[pages.size()]);
		List <Page<byte[]>> array = new LinkedList<Page<byte[]>>();
		for(Long id: pageIds)
			array.add (pages.get(id));
		return array.toArray((Page<byte[]>[])new Page[array.size()]);
	}
	public synchronized void	removePage(Page<byte[]> removePage){
		pages.remove(removePage.getPageId());
	}
	public void	removePages(Page<byte[]>[] removePages){
		for(Page<byte[]> remove: removePages)
		pages.remove(remove.getPageId());
	}	
	
}
