package hit.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MMULogger {

	public final static String DEFAULT_FILE_NAME = "C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\Michael.txt\\";
	public final static String DEFAULT_FILE_NAME1 = "C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\Nissim.txt\\";
	public final static String DEFAULT_FILE_NAME2 = "C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\Yulia.txt\\";
	public final static String DEFAULT_FILE_NAME3 = "log.txt";
	private final FileHandler handler = new FileHandler(DEFAULT_FILE_NAME);
	private final FileHandler handler1 = new FileHandler(DEFAULT_FILE_NAME1);
	private final FileHandler handler2 = new FileHandler(DEFAULT_FILE_NAME2);
	private final FileHandler handler3 = new FileHandler(DEFAULT_FILE_NAME3);
	private final FileHandler handlerWarnin = new FileHandler("logWarning.txt");
	private static MMULogger instance = null;
	private Logger logger;
	private Logger logger1;

	private MMULogger() throws SecurityException, IOException {   
		FileOutputStream writer1 = new FileOutputStream("C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\Michael.txt\\");
		writer1.flush();              //clear files before writing commands
		writer1.close();
		FileOutputStream writer2 = new FileOutputStream("C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\Nissim.txt\\");
		writer2.flush();
		writer2.close();
		FileOutputStream writer3 = new FileOutputStream("C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\Yulia.txt\\");
		writer3.flush();
		writer3.close();
		FileOutputStream writer4 = new FileOutputStream("C:\\Users\\Michael\\workspace\\MMUServere\\src\\main\\resources\\logcache\\logWarning.txt");
		writer4.flush();
		writer4.close();
		FileOutputStream writer5 = new FileOutputStream("log.txt");
		writer5.flush();
		writer5.close();
		logger = Logger.getLogger("MyLogger");
		handler.setFormatter(new OnlyMessageFormatter());
		handler1.setFormatter(new OnlyMessageFormatter());
		handler2.setFormatter(new OnlyMessageFormatter());
		handler3.setFormatter(new OnlyMessageFormatter());
		handlerWarnin.setFormatter(new OnlyMessageFormatter());
		logger.addHandler(handler3);
		logger.addHandler(handler2);
		logger.addHandler(handler1);
		logger.addHandler(handler);
		logger1 = Logger.getLogger("warning logger");
		logger1.addHandler(handlerWarnin);

	}

	public static MMULogger getInstance() {
		if (instance == null)
			try {
				return instance = new MMULogger();
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}
		return instance;
	}

	public synchronized void write(String comand, Level level) throws SecurityException {
		if (level.toString().equals("SEVERE"))
			logger1.log(level, comand);
		else{
			logger.log(level, comand);
		}
			
	}
	public class OnlyMessageFormatter extends Formatter {

		public OnlyMessageFormatter() {
			super();
		}

		@Override
		public String format(final LogRecord record) {

			return record.getMessage();
		}

	}

}
