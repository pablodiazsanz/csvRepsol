package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);
	private static PropertyFile clientConfig, serverConfig;
	
	public static void main(String[] args) {
		
		// Inicializamos las propiedades del cliente y del servidor
		clientConfig = new ClientPropeties();
		serverConfig = new ServerProperties();
		
		// Comprobamos que obtenemos de los ficheros est�n rellenos
		if (clientConfig.checkConfig() && serverConfig.checkConfig()) {
			log.trace("Arranca la aplicaci�n");
			
			CsvAccess csvAccess = new CsvAccess(serverConfig);
			
			Manager manager = new Manager(csvAccess);
			
			// Creamos o sobreescribimos el fichero CSV que vamos a sacar con las operaciones
			try {
				csvAccess.createCSV();
			} catch (SiaException e1) {
				log.error("fallo al crear csv", e1);
			}
			log.trace("Se ha creado o sobreescrito el tercer CSV correctamente");
			
			// Establecemos en el objeto CsvAccess el fichero de propiedades del cliente
			try {
				csvAccess.setConfig(clientConfig);
			} catch (SiaException e1) {
				log.error("fallo al buscar propiedades", e1);
			}
			
			try {
				// Obtenemos los datos del fichero del cliente
				HashMap<String, Employee> clientData = csvAccess.readCSV(clientConfig.getProperty(ClientPropeties.PATH));
				log.trace("Se han obtenido los datos del  fichero del cliente correctamente");
				
				// Establecemos en el objeto CsvAccess el fichero de propiedades del servidor
				csvAccess.setConfig(serverConfig);
				
				// Obtenemos los datos del fichero del servidor
				HashMap<String, Employee> serverData = csvAccess.readCSV(serverConfig.getProperty(ServerProperties.PATH_SERVER));
				log.trace("Se han obtenido los datos del  fichero del servidor correctamente");
				
				// Comparamos los datos que obtenemos de ambos servidores y sacamos el tercer
				// fichero CSV con las operaciones realizadas por el cliente.
				manager.compare(clientData, serverData, csvAccess);
				log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
						+ "han metido las creaciones, modificaciones y eliminaciones de empleados");
				
			} catch (SiaException e) {
				log.error("No se ha podido comparar los dos ficheros porque alguno de ellos era nulo");
				
			}
			
			log.trace("Finaliza la aplicaci�n");
		}
	}
}
