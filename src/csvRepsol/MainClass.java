package csvRepsol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MainClass {
	
	private static Logger log = Logger.getLogger(MainClass.class);
	public static Properties prop;

	public static void main(String[] args) {
		prop = new Properties();
		FileInputStream ip;
		
		try {
			//ip = new FileInputStream("C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\config.properties");
			ip = new FileInputStream("C:\\Users\\mparrap\\git\\csvRepsol\\src\\config.properties");
			prop.load(ip);
			
		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado", e);

		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);

		}
		try {
			
		} catch (Exception e) {
			
		}
        log.debug("Arranca la aplicación");
		
		CsvAccess csvAccess = new CsvAccess();
		csvAccess.createCSV();
		log.info("Se ha creado o sobreescrito el tercer CSV correctamente");
		
		HashMap<String, Employee> clientData = csvAccess.readCSV(prop.getProperty("client"));
		log.info("Se han obtenido los datos del  fichero del cliente correctamente");
		HashMap<String, Employee> serverData = csvAccess.readCSV(prop.getProperty("server"));
		log.info("Se han obtenido los datos del  fichero del servidor correctamente");

		Manager.compare(clientData, serverData);
		log.info("Se han realizado las comparaciones correctamente y en el tercer CSV se "
				+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

		log.debug("Finaliza la aplicación");
	}
}
