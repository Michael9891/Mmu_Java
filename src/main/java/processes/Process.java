package processes;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import MemoruUnits.MemoryManagementUnit;
import MemoruUnits.Page;
import hit.util.MMULogger;

public class Process implements Runnable {
	private int id;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;

	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles) {
		this.id = id;
		this.mmu = mmu;
		this.processCycles = processCycles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		int j = 0;
		Long[] pageIds;
		Page<byte[]>[] requePage = null;
		boolean[] writePges;
		for (ProcessCycle curProcCycle : processCycles.getProcessCycles()) {      
			pageIds = curProcCycle.getPages().toArray(new Long[curProcCycle.getPages().size()]);  //all the page that current cycle need to get
			writePges = new boolean[pageIds.length];
			List<byte[]> data = curProcCycle.getData();  //data for writing to the pages
			int t = 0;
			for(byte[] b:data){
				if(b!=null)
					writePges[t] = true;
				else
					writePges[t] = false;
				t++;
			}
			synchronized (mmu) {
				requePage = mmu.getPages(pageIds,writePges);	
				for (Long num : pageIds)
					MMULogger.getInstance().write("GP:P" + id + " " + num + " " + Arrays.toString(data.get(j++)) + "\n",Level.INFO);
			}
			j = 0;
			int i = 0;
			for (Page<byte[]> p : requePage) {
				p.setContent(data.get(i));
				i++;
			}
			try {
				Thread.sleep(curProcCycle.getSleepMs());
			} catch (InterruptedException e) {
				e.printStackTrace();
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
		}

	}

}
