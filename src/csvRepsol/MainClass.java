package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;

import csvRepsol.businessLogic.Manager;
import csvRepsol.dataAccess.CsvAccess;
import csvRepsol.dataAccess.DBAccess;
import csvRepsol.dataAccess.PropertyFile;
import csvRepsol.entities.Employee;
import csvRepsol.exceptions.SiaException;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);
	private static PropertyFile clientConfig, serverConfig, resultConfig;

	public static void main(String[] args) {
		// iniciamos los src de los properties

		//String srcClient = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\clientConfig.properties";
		//String srcServer = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\serverConfig.properties";
		//String srcResult = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\resultConfig.properties";

		String srcClient = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\clientConfig.properties";
		String srcServer = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\serverConfig.properties";
		String srcResult = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\resultConfig.properties";

		// Inicializamos las propiedades del cliente y del servidor
		clientConfig = new PropertyFile(srcClient);
		serverConfig = new PropertyFile(srcServer);
		resultConfig = new PropertyFile(srcResult);

		// Comprobamos que obtenemos de los ficheros estén rellenos
		if (clientConfig.checkConfig() && serverConfig.checkConfig() && resultConfig.checkConfig()) {
			log.trace("Arranca la aplicación");

			CsvAccess csvAccess = new CsvAccess(resultConfig);

			Manager manager = new Manager(csvAccess);

			// Creamos o sobreescribimos el fichero CSV que vamos a sacar con las
			// operaciones
			try {
				csvAccess.createCSV();
			} catch (SiaException e1) {
				log.error("fallo al crear csv", e1);
			}
			log.trace("Se ha creado o sobreescrito el tercer CSV correctamente");

			// Establecemos en el objeto CsvAccess el fichero de propiedades del cliente
			try {
				csvAccess.setConfig(clientConfig);
				// Obtenemos los datos del fichero del cliente
				HashMap<String, Employee> clientData = csvAccess.readCSV(clientConfig.getProperty(PropertyFile.PATH));
				log.trace("Se han obtenido los datos del  fichero del cliente correctamente");

				// Establecemos en el objeto CsvAccess el fichero de propiedades del servidor
				csvAccess.setConfig(serverConfig);

				// Obtenemos los datos del fichero del servidor
				HashMap<String, Employee> serverData = csvAccess.readCSV(serverConfig.getProperty(PropertyFile.PATH));
				HashMap<String, Employee> serverDataDB = DBAccess.getEmployeesFromServer();
				
				log.trace("Se han obtenido los datos del  fichero del servidor correctamente");

				// Comparamos los datos que obtenemos de ambos servidores y sacamos el tercer
				// fichero CSV con las operaciones realizadas por el cliente.
				csvAccess.setConfig(resultConfig);
				manager.compare(clientData, serverData, csvAccess, true);
				log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
						+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

			} catch (SiaException e) {
				log.error("No se ha podido comparar los dos ficheros porque alguno de ellos era nulo");

			}

			log.trace("Finaliza la aplicación");
		}
	}
}
