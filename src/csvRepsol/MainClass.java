package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);
	private static PropertyFile clientConfig, serverConfig;
	
	//private static String clientSrc = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\clientConfig.properties";
	private static String clientSrc = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\clientConfig.properties";

	//private static String serverSrc = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\serverConfig.properties";
	private static String serverSrc = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\serverConfig.properties";
	
	public static void main(String[] args) {
		
		// Inicializamos las propiedades del cliente y del servidor
		clientConfig = new PropertyFile(clientSrc);
		serverConfig = new PropertyFile(serverSrc);
		
		// Comprobamos que obtenemos de los ficheros estén rellenos
		if (clientConfig.checkConfigClient() && serverConfig.checkConfigServer()) {
			log.trace("Arranca la aplicación");

			CsvAccess csvAccess = new CsvAccess(serverConfig);
			
			Manager manager = new Manager(csvAccess);
			
			// Creamos o sobreescribimos el fichero CSV que vamos a sacar con las operaciones
			csvAccess.createCSV();
			log.trace("Se ha creado o sobreescrito el tercer CSV correctamente");
			
			// Establecemos en el objeto CsvAccess el fichero de propiedades del cliente
			csvAccess.setConfig(clientConfig);
			// Obtenemos los datos del fichero del cliente
			HashMap<String, Employee> clientData = csvAccess.readCSV(clientConfig.getProperty("DEFAULT.File.CSV.client"));
			log.trace("Se han obtenido los datos del  fichero del cliente correctamente");
			
			// Establecemos en el objeto CsvAccess el fichero de propiedades del servidor
			csvAccess.setConfig(serverConfig);
			// Obtenemos los datos del fichero del servidor
			HashMap<String, Employee> serverData = csvAccess.readCSV(serverConfig.getProperty("DEFAULT.File.CSV.server"));
			log.trace("Se han obtenido los datos del  fichero del servidor correctamente");

			// Comparamos los datos que obtenemos de ambos servidores y sacamos el tercer
			// fichero CSV con las operaciones realizadas por el cliente.
			manager.compare(clientData, serverData, csvAccess);
			log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
					+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

			log.trace("Finaliza la aplicación");
		}
	}
}
