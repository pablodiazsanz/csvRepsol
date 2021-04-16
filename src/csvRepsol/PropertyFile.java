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

	public boolean checkConfigClient() {
		boolean readed = true;
		
		try {
			ip = new FileInputStream(src);
			file.load(ip);
			String client = file.getProperty("DEFAULT.File.CSV.client");
			String id = file.getProperty("DEFAULT.File.CSV.head.ID");
			String name = file.getProperty("DEFAULT.File.CSV.head.NAME");
			String surname1 = file.getProperty("DEFAULT.File.CSV.head.FIRST_SURNAME");
			String surname2 = file.getProperty("DEFAULT.File.CSV.head.SECOND_SURNAME");
			String phone = file.getProperty("DEFAULT.File.CSV.head.PHONE");
			String mail = file.getProperty("DEFAULT.File.CSV.head.EMAIL");
			String job = file.getProperty("DEFAULT.File.CSV.head.JOB");
			String hiringDate = file.getProperty("DEFAULT.File.CSV.head.HIRING_DATE");
			String yearSalary = file.getProperty("DEFAULT.File.CSV.head.YEAR_SALARY");
			String sickLeave = file.getProperty("DEFAULT.File.CSV.head.SICK_LEAVE");
			

		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado", e);
			readed = false;

		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);
			readed = false;
		}

		return readed;
	}
	
	public boolean checkConfigServer() {
		boolean readed = true;
		
		try {
			ip = new FileInputStream(src);
			file.load(ip);
			String server = file.getProperty("DEFAULT.File.csv.server");
			String result = file.getProperty("DEFAULT.File.csv.result");
			String id = file.getProperty("DEFAULT.File.CSV.head.ID");
			String name = file.getProperty("DEFAULT.File.CSV.head.NAME");
			String surname1 = file.getProperty("DEFAULT.File.CSV.head.FIRST_SURNAME");
			String surname2 = file.getProperty("DEFAULT.File.CSV.head.SECOND_SURNAME");
			String phone = file.getProperty("DEFAULT.File.CSV.head.PHONE");
			String mail = file.getProperty("DEFAULT.File.CSV.head.EMAIL");
			String job = file.getProperty("DEFAULT.File.CSV.head.JOB");
			String hiringDate = file.getProperty("DEFAULT.File.CSV.head.HIRING_DATE");
			String yearSalary = file.getProperty("DEFAULT.File.CSV.head.YEAR_SALARY");
			String sickLeave = file.getProperty("DEFAULT.File.CSV.head.SICK_LEAVE");

		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado", e);
			readed = false;

		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);
			readed = false;
		}

		return readed;
	}
	
	public String getProperty(String property) {
        String value = null;
        try {
            value = file.getProperty(property);
        } catch (Exception e) {
            log.error(property + " no encontrada");
        }
        return value;
    }

}
