package driver;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class CLI {
	private Scanner in;

	CLI(InputStream in, OutputStream out) {
		this.in = new Scanner(in);
	}

	public String[] getConfiguration() {
		String[] configuration = new String[3];
		System.out.println("\n Please enter start for MMU or stop for exit");
		configuration[0] = in.next();
		write(configuration[0]);
		if (configuration[0].equals("stop")){
			System.out.println("Thank You");
			return null;
		}
		else if (configuration[0].equals("start")) {
			System.out.println("Please enter algorithm(lru,mru,rr) ENTER and Ram capacity");
			configuration[1] = in.next();
			write(configuration[1]);
			while (!(configuration[1].equals("lru") || configuration[1].equals("mru") || configuration[1].equals("rr"))){
				System.out.println("**is wrong algorithm** ");
				System.out.println("Again!!! Please enter algorithm(lru,mru,rr) ENTER and Ram capacity");
				configuration[1] = in.next();
			}
			configuration[2] = in.next();
			write(configuration[2]);
			return configuration;
		} else {
			System.out.println("is wrong command");
			return getConfiguration();
		}
	}

	public void write(String string) {
		System.out.println("***"+string + "***");
	}
}
