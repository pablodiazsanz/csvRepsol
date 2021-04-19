package csvRepsol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public abstract class PropertyFile {
	
	public static String HEAD_ID = "DEFAULT.File.CSV.head.ID";
	public static String HEAD_NAME = "DEFAULT.File.CSV.head.NAME";
	public static String HEAD_SURNAME1 = "DEFAULT.File.CSV.head.FIRST_SURNAME" ;
	public static String HEAD_SURNAME2 = "DEFAULT.File.CSV.head.SECOND_SURNAME";
	public static String HEAD_PHONE = "DEFAULT.File.CSV.head.PHONE";
	public static String HEAD_EMAIL = "DEFAULT.File.CSV.head.EMAIL";
	public static String HEAD_JOB = "DEFAULT.File.CSV.head.JOB";
	public static String HEAD_HIRING_DATE = "DEFAULT.File.CSV.head.HIRING_DATE";
	public static String HEAD_YEAR_SALARY = "DEFAULT.File.CSV.head.YEAR_SALARY";
	public static String HEAD_SICK_LEAVE = "DEFAULT.File.CSV.head.SICK_LEAVE";

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
	public abstract boolean checkConfig();
	

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
