package csvRepsol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClientPropeties extends PropertyFile {
	
	public static String PATH = "DEFAULT.File.CSV.client";	
	
	public static String src = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\src\\clientConfig.properties";
	//public static String src = "C:\\Users\\mparrap\\git\\csvRepsol\\src\\clientConfig.properties";
	
	public ClientPropeties() {
		super(src);
	}

	@Override
	public boolean checkConfig() {
		boolean readed = true;
		try {
			ip = new FileInputStream(src);
			file.load(ip);
			file.getProperty(PATH);
			file.getProperty(HEAD_ID);
			file.getProperty(HEAD_NAME);
			file.getProperty(HEAD_SURNAME1);
			file.getProperty(HEAD_SURNAME2);
			file.getProperty(HEAD_PHONE);
			file.getProperty(HEAD_EMAIL);
			file.getProperty(HEAD_JOB);
			file.getProperty(HEAD_HIRING_DATE);
			file.getProperty(HEAD_YEAR_SALARY);
			file.getProperty(HEAD_SICK_LEAVE);
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

}
