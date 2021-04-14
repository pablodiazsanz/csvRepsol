package csvRepsol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MainClass {
	
	private static Logger log = Logger.getLogger(MainClass.class);
	public static Properties prop;

	public static void main(String[] args) {
		prop = new Properties();
		FileInputStream ip;
		
		try {
			ip = new FileInputStream("C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\config.properties");
			prop.load(ip);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
        log.debug("Arranca la aplicación");
		
		CsvAccess csvAccess = new CsvAccess();
		csvAccess.createCSV();
		log.info("Se ha creado o sobreescrito el tercer CSV correctamente");
		
		HashMap<String, Employee> clientData = csvAccess.readCSV(prop.getProperty("client"));
		HashMap<String, Employee> serverData = csvAccess.readCSV(prop.getProperty("server"));
		log.info("Se han obtenido los datos del cliente y del servidor correctamente");

		Manager.compare(clientData, serverData);
		log.info("Se han realizado las comparaciones correctamente y en el tercer CSV se "
				+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

		log.debug("Finaliza la aplicación");
	}
}
