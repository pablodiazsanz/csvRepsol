package csvRepsol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class CsvAccess {

	private static Logger log = Logger.getLogger(CsvAccess.class);

	private static final int ID = 0;
	private static final int NAME = 1;
	private static final int SURNAME1 = 2;
	private static final int SURNAME2 = 3;
	private static final int TLF = 4;
	private static final int MAIL = 5;
	private static final int JOB = 6;
	private static final int HIRING_DATE = 7;
	private static final int YEAR_SALARY = 8;
	private static final int SICK_LEAVE = 9;

	// private String path = "C:\\Users\\mparrap\\IdeaProjects\\csvRepsol2\\csv\\";
	// private String path = "C:\\Users\\pdiazs\\IdeaProjects\\csvRepsol2\\csv\\";
	private String path = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\csv\\";
	// private String path = "C:\\Users\\mparrap\\git\\csvRepsol\\csv\\";

	/**
	 * Lee los empleados de un csv, y devuelme la lista en un HasMap organizado por
	 * <id del empledado, objeto empleado>
	 *
	 * @param nameCSV Nombre del csv que quieres leer
	 * @return HasMap De los empleados con su id como key
	 */
	public HashMap<String, Employee> readCSV(String nameCSV) {

		// Creamos el HashMap y obtenemos el fichero CSV
		HashMap<String, Employee> map = new HashMap<>();
		File f = new File(path + nameCSV + ".csv");

		log.info("Ruta del fichero" + f.getPath());

		FileReader reader = null;
		BufferedReader br = null;

		// Utilizamos un contador de lineas del fichero para obtener informacion
		// acerca de la linea que nos da un error o una excepcion
		int contLine = 2;

		try {
			reader = new FileReader(f);
			br = new BufferedReader(reader);

			// Leemos la primera linea, que es la informacion de las columnas
			String line = br.readLine();

			// Con el bucle while recorremos linea por linea el fichero
			while (line != null) {
				String id = "";
				try {
					line = br.readLine();

					// Creamos un ArrayList para obtener los datos de la linea
					List<String> dataEmployee = new ArrayList<String>();

					// Añadimos el primer dato
					dataEmployee.add("");

					// Utilizamos este booleano para saber si abrimos o cerramos las comillas
					boolean openQuotes = false;

					// Utilizamos un valor auxiliar para saber cuando cambiamos de dato
					int employeeValue = 0;

					// Con este bucle for recorremos caracter por caracter para sacar los datos uno
					// a uno
					for (int i = 0; i < line.length(); i++) {

						/*
						 * Aqui observo si el caracter es una comilla. Si lo es, hago una comprobación
						 * para saber si inicio el dato o lo finalizo
						 */
						if (line.charAt(i) == '"') {
							if (openQuotes) {
								openQuotes = false;
							} else {
								openQuotes = true;
							}
						}

						/*
						 * Aqui decido si hay un cambio de valor o si no lo hay. Si lo hay, añado un
						 * nuevo valor vacio al ArrayList, y si no lo hay, sumo lo que contiene el valor
						 * del ArrayList actual a lo existente
						 */
						if (line.charAt(i) == ';' && openQuotes == false) {
							employeeValue++;
							dataEmployee.add("");

							// Aquí compruebo que si no hay nada en ese dato, me ponga en valor del
							// ArrayList que es un valor nulo
							if (dataEmployee.get(employeeValue - 1).length() == 0) {
								dataEmployee.set(employeeValue - 1, "NULL");
							}

						} else {
							dataEmployee.set(employeeValue, dataEmployee.get(employeeValue) + line.charAt(i));
						}

					}

					id = dataEmployee.get(ID).trim().toUpperCase();

					/*
					 * Aquí formateamos la cadena obtenida, que en el caso ideal es una fecha, a un
					 * tipo Date
					 */

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					formatter.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
					Date hiringDate = formatter.parse(dataEmployee.get(HIRING_DATE));

					// Aquí comprobamos si el empleado está dado de baja o no
					boolean sickLeave = false;
					if (dataEmployee.get(SICK_LEAVE).equals("true")) {
						sickLeave = true;
					}

					// Aqui formateamos el salario anual a numero entero
					int yearSalary = Integer.parseInt(dataEmployee.get(YEAR_SALARY));

					/*
					 * Creamos el objeto empleado normalizando el id en mayusculas, eliminamos los
					 * espacios al principìo y al final
					 */
					Employee emp = new Employee(id, dataEmployee.get(NAME).trim(), dataEmployee.get(SURNAME1).trim(),
							dataEmployee.get(SURNAME2).trim(), dataEmployee.get(TLF).trim(),
							dataEmployee.get(MAIL).trim(), dataEmployee.get(JOB).trim(), hiringDate, yearSalary,
							sickLeave);

					// Añadimos al HashMap el objeto Employee que utiliza de clave el ID de ese
					// empleado
					map.put(emp.getId(), emp);

				} catch (NullPointerException e) {
					log.error("La linea " + contLine + " del CSV " + nameCSV + " esta vacia - ", e);

				} catch (IndexOutOfBoundsException e) {
					log.error("Fallo al leer la linea " + contLine + " del fichero " + nameCSV
							+ ".\n Error causado por falta de columnas en el id[" + id + "] - csvline: {" + line + "}",
							e);

				} catch (ParseException e) {
					log.error("Fallo al leer la linea " + contLine + " del fichero " + nameCSV
							+ ".\n Comprobar si el formato es correcto o faltan columnas en id[" + id + "] - csvline: {"
							+ line + "}\"", e);

				} catch (NumberFormatException e) {
					log.error("Fallo al leer la linea " + contLine + " del fichero " + nameCSV
							+ ".\n Comprobar que el numero introducido sea el correcto id[" + id + "] - csvline: {"
							+ line + "}", e);

				} catch (Exception e) {
					log.error("Fallo generico en la linea " + contLine + " del fichero " + nameCSV, e);

				}

				// Cambiamos la linea del contador
				contLine++;
			}

		} catch (FileNotFoundException e) {
			log.error("Fallo a la hora de encontrar el fichero con los datos", e);

		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);

		} finally {
			try {
				br.close();
				reader.close();
				log.info("Lectura finalizada con " + (contLine - 2) + " lineas leidas en fichero " + nameCSV);

			} catch (IOException e) {
				log.error("Fallo de entrada o salida", e);

			}
		}

		return map;
	}

	/**
	 * Este metodo crea un nuevo csv para guardar la informacion final
	 */
	public void createCSV() {
		try {
			FileWriter fw = new FileWriter(path + "result.csv");
			fw.write("id;name;first surname;second surname;phone;email;job;hiring_date;year_salary;sick_leave;status");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo usado para añadir una linea de datos al archivo csv de resultados
	 *
	 * @param employee El empleado que queremos añadir
	 * @param status   La accion que realizamos con el empleado, UPDATE, DELETE o
	 *                 CREATE
	 */
	public void writeCSV(Employee employee, String status) {
		try {
			FileWriter fw = new FileWriter(path + "result.csv", true);
			fw.write("\n" + employee.toCSV() + ";" + status);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeUpdatedEmployeeCSV(Employee updatedEmployee, List<String> extraData, String status) {

		String updatedData;
		String hiringDate = "", yearSalary = "", sickLeave = "";
		int hdAux = 0, ysAux = 0, slAux = 0;

		if (!extraData.isEmpty()) {
			for (int i = 0; i < extraData.size(); i++) {

				if (extraData.get(i).equals("hiringDate")) {
					hdAux = 1;
				}

				if (extraData.get(i).equals("yearSalary")) {
					ysAux = 1;
				}

				if (extraData.get(i).equals("sickLeave")) {
					slAux = 1;
				}

			}
		}
		
		if (hdAux == 0) {
			hiringDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(updatedEmployee.getHiringDate());;
		}
		if (ysAux == 0) {
			yearSalary = String.valueOf(updatedEmployee.getYearSalary());
		}
		if (slAux == 0) {
			sickLeave = String.valueOf(updatedEmployee.isSickLeave());
		}
		
		updatedData = updatedEmployee.getId() + ";" + updatedEmployee.getName() + ";" +
				updatedEmployee.getSurname1() + ";" + updatedEmployee.getSurname2() + ";" +
				updatedEmployee.getTlf() + ";" + updatedEmployee.getMail() + ";" +
				hiringDate + ";" + yearSalary + ";" + sickLeave;

		try {
			FileWriter fw = new FileWriter(path + "result.csv", true);
			fw.write("\n" + updatedData + ";" + status);
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}
}
