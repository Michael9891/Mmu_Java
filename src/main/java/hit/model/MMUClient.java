package hit.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;

import hit.util.MMULogger;

public class MMUClient {
	private Map<Integer,String> fileFromServer = null;
	
	@SuppressWarnings("unchecked")
	public Map<Integer,String> getData(String[]s) {
		Socket myServer;
		try {
			myServer = new Socket("localhost",9994);
			ObjectOutputStream output = new ObjectOutputStream(myServer.getOutputStream());
			output.writeObject(s[0]+" "+s[1]+" "+s[2]);
			ObjectInputStream input = new ObjectInputStream(myServer.getInputStream());
			fileFromServer = (Map<Integer, String>) input.readObject();
			input.close();
			output.flush();
			output.close();
			myServer.close();

		} catch (IOException | ClassNotFoundException e) {
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return  fileFromServer;

	}

}
