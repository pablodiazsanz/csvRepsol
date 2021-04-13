package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MainClass {
	
	static Logger log = Logger.getLogger(MainClass.class);

	public static void main(String[] args) {
        log.debug("Arranca la aplicación");
        log.info("this is a information log message");
        log.warn("this is a warning log message");
		
		CsvAccess csvAccess = new CsvAccess();
		csvAccess.createCSV();
		HashMap<String, Employee> clientData = csvAccess.readCSV("client");
		HashMap<String, Employee> serverData = csvAccess.readCSV("server");

		Manager.compare(clientData, serverData);

	}
}
