package csvRepsol;

import java.util.HashMap;

import org.apache.log4j.Logger;

import csvRepsol.businessLogic.Manager;
import csvRepsol.constants.PropertyConstants;
import csvRepsol.dataAccess.CsvAccess;
import csvRepsol.dataAccess.DBAccess;
import csvRepsol.dataAccess.PropertyFile;
import csvRepsol.entities.Employee;
import csvRepsol.exceptions.SiaException;

public class MainClass {

	private static Logger log = Logger.getLogger(MainClass.class);

	private static boolean csvToDatabase = false;

	public static void main(String[] args) {

		if (csvToDatabase) {
			startDatabaseSynchronization();

		} else {
			startCsvComparation();

		}
	}

	private static void startCsvComparation() {
		// Inicializamos las propiedades del cliente y del servidor
		PropertyFile clientConfig = new PropertyFile(PropertyConstants.PATH_CLIENT_PROPERTY_FILE);
		PropertyFile serverConfig = new PropertyFile(PropertyConstants.PATH_SERVER_PROPERTY_FILE);
		PropertyFile resultConfig = new PropertyFile(PropertyConstants.PATH_RESULT_PROPERTY_FILE);

		// Comprobamos que obtenemos de los ficheros estén rellenos
		if (clientConfig.checkConfig() && serverConfig.checkConfig() && resultConfig.checkConfig()) {
			log.trace("Arranca la aplicación");

			try {
				CsvAccess csvAccess = new CsvAccess(resultConfig);

				Manager manager = new Manager(csvAccess);

				// Creamos o sobreescribimos el fichero CSV que vamos a sacar con las
				// operaciones

				csvAccess.createCSV();

				log.trace("Se ha creado o sobreescrito el tercer CSV correctamente");

				// Establecemos en el objeto CsvAccess el fichero de propiedades del cliente

				csvAccess.setConfig(clientConfig);
				// Obtenemos los datos del fichero del cliente
				HashMap<String, Employee> clientData = csvAccess
						.readCSV(clientConfig.getProperty(PropertyConstants.CSV_PATH));
				log.trace("Se han obtenido los datos del fichero del cliente correctamente");

				// Establecemos en el objeto CsvAccess el fichero de propiedades del servidor
				csvAccess.setConfig(serverConfig);

				// Obtenemos los datos del fichero del servidor
				HashMap<String, Employee> serverData = csvAccess
						.readCSV(serverConfig.getProperty(PropertyConstants.CSV_PATH));

				log.trace("Se han obtenido los datos del fichero del servidor correctamente");

				// Comparamos los datos que obtenemos de ambos servidores y sacamos el tercer
				// fichero CSV con las operaciones realizadas por el cliente.
				csvAccess.setConfig(resultConfig);
				manager.compare(clientData, serverData, csvToDatabase);
				log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
						+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

			} catch (SiaException e) {
				log.error("No se ha podido comparar los dos ficheros porque alguno de ellos era nulo");

			}

			log.trace("Finaliza la aplicación");
		}

	}

	private static void startDatabaseSynchronization() {
		PropertyFile clientConfig = new PropertyFile(PropertyConstants.PATH_CLIENT_PROPERTY_FILE);
		CsvAccess csvAccess;
		Manager manager;

		if (clientConfig.checkConfig() && DBAccess.tryConnection()) {
			log.trace("Arranca la aplicación");

			try {

				csvAccess = new CsvAccess(clientConfig);

				HashMap<String, Employee> clientData = csvAccess
						.readCSV(clientConfig.getProperty(PropertyConstants.CSV_PATH));
				log.trace("Se han obtenido los datos del fichero del cliente correctamente");

				HashMap<String, Employee> serverDataDB = DBAccess.getEmployeesFromServer();

				manager = new Manager();

				manager.compare(clientData, serverDataDB, csvToDatabase);
				log.trace("Se han realizado las comparaciones correctamente y en el tercer CSV se "
						+ "han metido las creaciones, modificaciones y eliminaciones de empleados");

			} catch (SiaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
