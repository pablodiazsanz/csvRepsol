package csvRepsol.dataAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import csvRepsol.constants.PropertyConstants;
import csvRepsol.exceptions.SiaException;
import csvRepsol.exceptions.SiaExceptionCodes;

public class PropertyFile {

	protected Properties file;
	protected FileInputStream ip;
	protected Logger log = Logger.getLogger(PropertyFile.class);
	protected String src;

	public PropertyFile(String src) {
		super();
		this.src = src;
		file = new Properties();
	}

	/**
	 * Comprueba los datos del config oportuno para saber que estan todo los
	 * datos y devuelve true de ser correcto y false de no ser asi
	 * 
	 * @return true en caso de encontrar todos los datos y false si no
	 */
	public boolean checkConfig() {
		boolean readed = true;
		try {
			ip = new FileInputStream(src);
			file.load(ip);
			file.getProperty(PropertyConstants.CSV_PATH);
			file.getProperty(PropertyConstants.CSV_HEAD_ID);
			file.getProperty(PropertyConstants.CSV_HEAD_NAME);
			file.getProperty(PropertyConstants.CSV_HEAD_SURNAME1);
			file.getProperty(PropertyConstants.CSV_HEAD_SURNAME2);
			file.getProperty(PropertyConstants.CSV_HEAD_PHONE);
			file.getProperty(PropertyConstants.CSV_HEAD_EMAIL);
			file.getProperty(PropertyConstants.CSV_HEAD_JOB);
			file.getProperty(PropertyConstants.CSV_HEAD_HIRING_DATE);
			file.getProperty(PropertyConstants.CSV_HEAD_YEAR_SALARY);
			file.getProperty(PropertyConstants.CSV_HEAD_SICK_LEAVE);
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
	 * Le introduces el nombre del valor buscado en el fichero propeties y te lo
	 * devulve
	 * 
	 * @param property nombre completo de la variable buscada
	 * @return el valor que le corresponde en el ficheroi properties
	 */
	public String getProperty(String property) throws SiaException {
		String value = null;
		try {
			value = file.getProperty(property);
			log.trace(property + " leida correctamente");
		} catch (Exception e) {
			log.error(property + " no encontrada");
			throw new SiaException(SiaExceptionCodes.MISSING_PROPERTY, e);
		}
		return value;
	}

}
