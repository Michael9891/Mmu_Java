package processes;
import java.util.List;

public class RunConfiguration {
	private List<ProcessCycles>processesCycles;
	public RunConfiguration(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}
	public List<ProcessCycles> getProcessesCycles(){
		//System.out.println(processCycles.get(0).getProcessCycles().get(0).getSleepMs());
		return processesCycles;
	}
	public void setProcessesCycles(List<ProcessCycles> processesCycles){
		this.processesCycles = processesCycles;
	}
	@Override
	public String toString(){
		return null;
	}
	

}
