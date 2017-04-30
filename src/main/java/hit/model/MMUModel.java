package hit.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import hit.util.MMULogger;

public class MMUModel implements Model {

	public int numProcesses;
	public int ramCapacity;
	private String file;
	private Map<Integer, String> commandsFromServere = null;
	private List<String> command;
	private List<String[]> commandOfReplace = new ArrayList<String[]>();;
	private List<String> gpTemp;
	private int j;
	private Integer z;
	private int t;

	public MMUModel(String filePath) {
		file = filePath;
		gpTemp = new ArrayList<String>();
		j = 0;
		z = 0;
		t = 0;
	}

	public int getRamCapacity() {
		return ramCapacity;
	}

	public boolean fileIsFound(String[] userRequest) {       //search file in server and copy to log.txt 
		MMUClient client = new MMUClient();
		commandsFromServere = client.getData(userRequest);
		if (commandsFromServere != null) {
			try {
				BufferedWriter bf = new BufferedWriter(new FileWriter("remotelogs\\" + file));
				String line = null ;
				for (int i = 0; i < commandsFromServere.size(); i++) {
					line = commandsFromServere.get(i);
					bf.write(line + "\n");
				}
				bf.flush();
				bf.close();
			} catch (IOException e) {
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
			return true;
		} else
			return false;
	}

	public List<String> getCommands() {
		j = 0;
		z = 0;
		t = 0;
		command = new ArrayList<String>();
		readData();
		return command;
	}

	@Override
	public void readData() {                         //get all the commands from mmu and prepare them for view
		BufferedReader br;
		String currentCommand = "";
		try {
			if(commandsFromServere !=null)
			br = new BufferedReader(new FileReader("remotelogs\\"+file));
			else{
				br = new BufferedReader(new FileReader(file));
			}
			
			try {
				currentCommand =  br.readLine();
				ramCapacity = Integer.parseInt(currentCommand.substring(currentCommand.length()-1, currentCommand.length())) ;
				while ((currentCommand = br.readLine()) != null)
					splitPlaces(currentCommand);
				br.close();
				for (String temp : gpTemp) {
					currentCommand = splitLine(temp);
					if(currentCommand != null)
					command.add(currentCommand);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
	}
	private String splitLine(String command) {
		int i;
		String splittedCommand = "";
		i = 0;
		if (command.startsWith("GP")) {
			List<String> line = new ArrayList<String>();
			for (String l : command.split(" ")) {
				if (i == 0)
					line.add(i, l.substring(3, l.length()));
				if (i == 1)
					line.add(i, l);
				if (i > 1) {
					if (l.startsWith("["))
						line.add(i, l.substring(1, l.length() - 1));
					else
						line.add(i, l.substring(0, l.length() - 1));
				}
				i++;
			}
			for (String s : line)
				splittedCommand += s + " ";
			if (z >= ramCapacity) {
				if (!line.get(1).equals(commandOfReplace.get(t)[1])){
					splittedCommand += line.get(1);
				}
				else{
					splittedCommand += commandOfReplace.get(t)[0];
					if(t!=commandOfReplace.size()-1)
						t++;
						
				}
			} else{
				splittedCommand += z.toString();
				z++;
			}
			return splittedCommand;
		} else {
			return null;
		}
	}

	private void splitPlaces(String command) {
		int i = 0;
		int k = 0;
		if (command.startsWith("PR")) {
			String[] temp = new String[2];
			for (String l : command.split(" ")) {
				if (i == 2 || i == 4)
					temp[k++] = l;
				i++;
			}
			commandOfReplace.add(j, temp);
			j++;
		}if (command.startsWith("GP")) {
			gpTemp.add(command);
		}
	}

}
