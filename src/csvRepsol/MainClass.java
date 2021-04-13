package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MainClass {
	
	private static Logger log = Logger.getLogger(MainClass.class);

	public static void main(String[] args) {
        log.debug("Arranca la aplicación");
		
		CsvAccess csvAccess = new CsvAccess();
		csvAccess.createCSV();
		log.info("Se ha creado o sobreescrito el tercer CSV correctamente");
		
		HashMap<String, Employee> clientData = csvAccess.readCSV("client");
		HashMap<String, Employee> serverData = csvAccess.readCSV("server");
		log.info("Se han obtenido los datos del cliente y del servidor correctamente");

		Manager.compare(clientData, serverData);
		log.info("Se han realizado las comparaciones correctamente y en el tercer CSV se "
				+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

		log.debug("Finaliza la aplicación");
	}
}
