package csvRepsol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyFile {

	private Properties file;
	private FileInputStream ip;
	private Logger log = Logger.getLogger(PropertyFile.class);
	private String src;

	public PropertyFile(String src) {
		super();
		this.src = src;
		file = new Properties();
	}

	/**
	 * Comprueba los datos del config del cliente para saber que estan todo los
	 * datos y devuelve true de ser correcto y false de no ser asi
	 * 
	 * @return true en caso de encontrar todos los datos y false si no
	 */
	public boolean checkConfigClient() {
		boolean readed = true;

		try {
			ip = new FileInputStream(src);
			file.load(ip);
			file.getProperty("DEFAULT.File.CSV.client");
			file.getProperty("DEFAULT.File.CSV.head.ID");
			file.getProperty("DEFAULT.File.CSV.head.NAME");
			file.getProperty("DEFAULT.File.CSV.head.FIRST_SURNAME");
			file.getProperty("DEFAULT.File.CSV.head.SECOND_SURNAME");
			file.getProperty("DEFAULT.File.CSV.head.PHONE");
			file.getProperty("DEFAULT.File.CSV.head.EMAIL");
			file.getProperty("DEFAULT.File.CSV.head.JOB");
			file.getProperty("DEFAULT.File.CSV.head.HIRING_DATE");
			file.getProperty("DEFAULT.File.CSV.head.YEAR_SALARY");
			file.getProperty("DEFAULT.File.CSV.head.SICK_LEAVE");
			log.trace("Fichero config de cliente leido exitosamente");
		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado", e);
			readed = false;

		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);
			readed = false;
		}
		return readed;
	}

	/**
	 * Comprueba los datos del config server para saber que estan todo los datos y
	 * devuelve true de ser correcto y false de no ser asi
	 * 
	 * @return true en caso de encontrar todos los datos y false si no
	 */
	public boolean checkConfigServer() {
		boolean readed = true;
		try {
			ip = new FileInputStream(src);
			file.load(ip);
			file.getProperty("DEFAULT.File.csv.server");
			file.getProperty("DEFAULT.File.csv.result");
			file.getProperty("DEFAULT.File.CSV.head.ID");
			file.getProperty("DEFAULT.File.CSV.head.NAME");
			file.getProperty("DEFAULT.File.CSV.head.FIRST_SURNAME");
			file.getProperty("DEFAULT.File.CSV.head.SECOND_SURNAME");
			file.getProperty("DEFAULT.File.CSV.head.PHONE");
			file.getProperty("DEFAULT.File.CSV.head.EMAIL");
			file.getProperty("DEFAULT.File.CSV.head.JOB");
			file.getProperty("DEFAULT.File.CSV.head.HIRING_DATE");
			file.getProperty("DEFAULT.File.CSV.head.YEAR_SALARY");
			file.getProperty("DEFAULT.File.CSV.head.SICK_LEAVE");
			log.trace("Fichero config de servidor leido exitosamente");
		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado", e);
			readed = false;

		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);
			readed = false;
		}
		return readed;
	}

	/**
	 * Le introduces el nombre del valor buscado en el fichero propeties y te lo
	 * devulve
	 * 
	 * @param property nombre completo de la variable buscada
	 * @return el valor que le corresponde en el ficheroi properties
	 */
	public String getProperty(String property) {
		String value = null;
		try {
			value = file.getProperty(property);
			log.trace(property + " leida correctamente");
		} catch (Exception e) {
			log.error(property + " no encontrada");
		}
		return value;
	}

}
