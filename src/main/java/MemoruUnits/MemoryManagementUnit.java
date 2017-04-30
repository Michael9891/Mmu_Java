package MemoruUnits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.hit.algorithm.IAlgoCache;

import hit.util.MMULogger;

public class MemoryManagementUnit {

	private IAlgoCache<Long, Long> algo;
	private RAM ram;

	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo) throws SecurityException {
		this.setAlgo(algo);
		try {
			this.ram = new RAM(ramCapacity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IAlgoCache<Long, Long> getAlgo() {
		return algo;
	}
	@SuppressWarnings("unchecked")                       //for all page boolean variable true for write false for read
	public synchronized Page<byte[]>[] getPages(Long[] pageIds ,boolean[] writePges ) {
		List<Page<byte[]>> arrPageToReturn = new ArrayList<Page<byte[]>>();
		Page<byte[]> pageRequired = null;
		Page<byte[]> exitFromRam = null;
		HardDisk hd = null;
		int i = 0;
		try {
			hd = HardDisk.getInstance();
			for (Long curId : pageIds) {
				if (algo.getElement(curId) == null) {                   //if page not in ram
					Long exitKey = (algo.putElement(curId, curId));          //page thad need remove from ram
					if (exitKey!=null) {                                         //ram is full do page replacement
						exitFromRam = ram.getPage(exitKey);
						pageRequired = hd.pageReplacement(exitFromRam, curId);
						ram.removePage(exitFromRam);
						ram.addPage(pageRequired);
						MMULogger.getInstance().write("PR MTH " + exitFromRam + " MTR " + curId + "\n", Level.INFO);
						if(writePges[i])
							arrPageToReturn.add(pageRequired);
					} else {                                              // ram is not full do page fault
						pageRequired = hd.pageFault(curId);
						MMULogger.getInstance().write("PF:" + curId + "\n", Level.INFO);
						algo.putElement(curId, curId);
						ram.addPage(pageRequired);
						if(writePges[i])
							arrPageToReturn.add(pageRequired);
					}
				} else
					if(writePges[i])
						arrPageToReturn.add(ram.getPage(curId));           //page in ram
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return arrPageToReturn.toArray((Page<byte[]>[]) new Page[arrPageToReturn.size()]);  //return page for writing to process
	}

	public void setAlgo(IAlgoCache<Long, Long> algo) {
		this.algo = algo;
	}

	public RAM getRam() {
		return ram;
	}

	public void setRam(RAM ram) {
		this.ram = ram;
	}

	public void shutdown() throws Exception {                    //in the end of mmu put all page from ram to hard disk
		HardDisk hd = HardDisk.getInstance();
		Page<byte[]>[] pagesFromRam = ram.getPages(null);
		ram.removePages(pagesFromRam);
		for (Page<byte[]> page : pagesFromRam)
			hd.pageReplacement(page, null);
	}

	@Override
	public String toString() {
		return ram.toString();
	}

}
