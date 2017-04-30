package driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.algorithm.RandomReplacementLAlgoCacheImpl;

import MemoruUnits.MemoryManagementUnit;
import hit.controller.Controller;
import hit.controller.MMUController;
import hit.model.MMUModel;
import hit.util.MMULogger;
import hit.view.MMUView;
import hit.view.View;
import processes.ProcessCycles;
import processes.RunConfiguration;
import processes.Process;

public class MMUDriver {
	private final static String CONFIG_FILE_NAME = "Configuration.json";
	private final static String LOG_FILE_NAME = "log.txt";

	public static void main(String[] args)
			throws java.lang.InterruptedException, java.lang.reflect.InvocationTargetException, JsonIOException,
			JsonSyntaxException, SecurityException, IOException {
		CLI cli = new CLI(System.in, System.out);
		String[] configuration;
		IAlgoCache<Long, Long> algo = null;
		int capacity = 0;
		while ((configuration = cli.getConfiguration()) != null) {   //run until get stop
			capacity = Integer.parseInt(configuration[2]);
			if (configuration[1].equals("lru"))
				algo = new LRUAlgoCacheImpl<Long, Long>(capacity);
			else if (configuration[1].equals("mru"))
				algo = new MRUAlgoCacheImpl<Long, Long>(capacity);
			else
				algo = new RandomReplacementLAlgoCacheImpl<Long, Long>(capacity);

			MemoryManagementUnit mmu = new MemoryManagementUnit(capacity, algo);
			RunConfiguration runConfig = readConfigurationFile();             //get data for processes from json file
			List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
			List<Process> processes = createProcesses(processCycles, mmu);          //create processes and transfer them mmu and process cycles 
			runProcesses(processes);             //multithreading run processes
			try {
				mmu.shutdown();                    //clean the ram and put back all the pages into the hard disk
			} catch (Exception e) {
				e.printStackTrace();
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
			MMUModel model = new MMUModel(LOG_FILE_NAME);            
			View view = new MMUView();
			@SuppressWarnings("unused")
			Controller controller = new MMUController(model, view);       //create graphic interface and get file from server
			view.open();
		}
	}

	private static List<Process> createProcesses(List<ProcessCycles> processCycles, MemoryManagementUnit mmu) {
		List<Process> proc = new ArrayList<Process>();
		for (int i = 0; i < processCycles.size(); i++) {
			proc.add(new Process(i + 1, mmu, processCycles.get(i)));
		}
		return proc;
	}

	private static void runProcesses(List<Process> processes2) throws InterruptedException {
		Thread[] thread = new Thread[processes2.size()];
		for (int i = 0; i < processes2.size(); i++) {
			thread[i] = new Thread(processes2.get(i));
			thread[i].start();
		}
		for (int i = 0; i < processes2.size(); i++)     //main wait till all prcesses is finished
			thread[i].join();
	}

	private static RunConfiguration readConfigurationFile()
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		RunConfiguration run = new Gson().fromJson(new JsonReader(new FileReader("src\\main\\resources\\configuration"
				+ "\\"+CONFIG_FILE_NAME)),
				RunConfiguration.class);
		return run;
	}

}
