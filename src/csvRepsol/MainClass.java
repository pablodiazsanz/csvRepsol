package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);
	public static PropertyFile clientConfig, serverConfig;
	
	public static void main(String[] args) {
		//String src = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\config.properties";
		String clientSrc = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\clientConfig.properties";
		PropertyFile clientConfig = new PropertyFile(clientSrc);
		//String src = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\config.properties";
		String serverSrc = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\serverConfig.properties";
		PropertyFile serverConfig = new PropertyFile(serverSrc);
		if (clientConfig.checkConfigClient() && serverConfig.checkConfigServer()) {
			log.debug("Arranca la aplicación");

			CsvAccess csvAccess = new CsvAccess(serverConfig);
			Manager manager = new Manager(csvAccess);
			csvAccess.createCSV();
			log.trace("Se ha creado o sobreescrito el tercer CSV correctamente");
			csvAccess.setConfig(clientConfig);
			HashMap<String, Employee> clientData = csvAccess.readCSV(clientConfig.getProperty("DEFAULT.File.CSV.client"));
			log.trace("Se han obtenido los datos del  fichero del cliente correctamente");
			csvAccess.setConfig(serverConfig);
			HashMap<String, Employee> serverData = csvAccess.readCSV(serverConfig.getProperty("DEFAULT.File.CSV.server"));
			log.trace("Se han obtenido los datos del  fichero del servidor correctamente");

			manager.compare(clientData, serverData, csvAccess);
			log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
					+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

			log.debug("Finaliza la aplicación");
		}
	}
}
