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

	public boolean checkConfig() {
		boolean readed = true;
		
		try {
			ip = new FileInputStream(src);
			file.load(ip);
			String client = file.getProperty("client");
			String server = file.getProperty("server");
			String result = file.getProperty("result");

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
