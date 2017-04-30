package MemoruUnits;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HardDisk {

	final static int SIZE = 1000;
	final static String DEFAULT_FILE_NAME = "hdPages.txt";
	private Map <Long,Page<byte[]>> hDk;
	private static HardDisk instance = null;
	private static int i = 0;
	
	private HardDisk() throws IOException {
		if(i==0){
			hDk = new HashMap<Long, Page<byte[]>>(SIZE);
			for (int i = 1; i <= SIZE; i++) {
				Long id = new Long(i);
				hDk.put(id, new Page<byte[]>(id, null));
			}
			writeHd();
			i = 1;
		}
	}
	public static HardDisk getInstance() throws IOException{
		if(instance==null)
			return instance = new HardDisk();
		return instance;
	}
	@SuppressWarnings("unchecked")
	public  Page<byte[]> pageFault(Long pageId) throws FileNotFoundException, IOException, ClassNotFoundException {
		Page<byte[]> pageRequired = null;
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("src\\main\\resources\\harddisk\\"+DEFAULT_FILE_NAME));
		hDk = (HashMap<Long, Page<byte[]>>) in.readObject();
		in.close();
		pageRequired = hDk.get(pageId);
		writeHd();
		return pageRequired;
	}
	@SuppressWarnings("unchecked")
	public  Page<byte[]>pageReplacement(Page<byte[]> moveToHdPage,Long moveToRamId) throws FileNotFoundException, IOException, ClassNotFoundException{
		Page<byte[]> pageRequired = null;
		synchronized (this){
		ObjectInputStream inHd = new ObjectInputStream(new FileInputStream("src\\main\\resources\\harddisk\\"+DEFAULT_FILE_NAME));
		hDk = (HashMap<Long, Page<byte[]>>) inHd.readObject();
		inHd.close();
		}
		pageRequired = hDk.get(moveToRamId);
		hDk.put(moveToHdPage.getPageId(),moveToHdPage);
		writeHd();
		return pageRequired;
	}
	
	public  void writeHd(){
		
		try {
			ObjectOutputStream out = null;
			out = new ObjectOutputStream(new FileOutputStream("src\\main\\resources\\harddisk\\"+DEFAULT_FILE_NAME));
			out.writeObject(hDk);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hDk.clear();
	}

}
