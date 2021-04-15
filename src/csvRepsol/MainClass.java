package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);
	public static PropertyFile a;
	
	public static void main(String[] args) {
		//String src = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\config.properties";
		String src = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\clientConfig.properties";
		PropertyFile a = new PropertyFile(src);
		
		if (a.checkConfig()) {
			log.debug("Arranca la aplicación");

			CsvAccess csvAccess = new CsvAccess(a);
			Manager manager = new Manager(csvAccess);
			csvAccess.createCSV();
			log.trace("Se ha creado o sobreescrito el tercer CSV correctamente");

			HashMap<String, Employee> clientData = csvAccess.readCSV(a.getProperty("client"));
			log.trace("Se han obtenido los datos del  fichero del cliente correctamente");
			HashMap<String, Employee> serverData = csvAccess.readCSV(a.getProperty("server"));
			log.trace("Se han obtenido los datos del  fichero del servidor correctamente");

			manager.compare(clientData, serverData, csvAccess);
			log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
					+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

			log.debug("Finaliza la aplicación");
		}
	}
}
