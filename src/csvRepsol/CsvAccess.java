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
	private PropertyFile config;

	private String ID = "", NAME = "", SURNAME1 = "", SURNAME2 = "", PHONE = "", EMAIL = "", JOB = "", HIRING_DATE = "",
			YEAR_SALARY = "", SICK_LEAVE = "";

	public CsvAccess(PropertyFile config) {
		this.config = config;
	}

	public void setConfig(PropertyFile config) {
		this.config = config;
		ID = config.getProperty("DEFAULT.File.CSV.head.ID");
		NAME = config.getProperty("DEFAULT.File.CSV.head.NAME");
		SURNAME1 = config.getProperty("DEFAULT.File.CSV.head.FIRST_SURNAME");
		SURNAME2 = config.getProperty("DEFAULT.File.CSV.head.SECOND_SURNAME");
		PHONE = config.getProperty("DEFAULT.File.CSV.head.PHONE");
		EMAIL = config.getProperty("DEFAULT.File.CSV.head.EMAIL");
		JOB = config.getProperty("DEFAULT.File.CSV.head.JOB");
		HIRING_DATE = config.getProperty("DEFAULT.File.CSV.head.HIRING_DATE");
		YEAR_SALARY = config.getProperty("DEFAULT.File.CSV.head.YEAR_SALARY");
		SICK_LEAVE = config.getProperty("DEFAULT.File.CSV.head.SICK_LEAVE");
	}

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
		File f = new File(nameCSV);

		log.info("Ruta del fichero: " + f.getPath());

		FileReader reader = null;
		BufferedReader br = null;

		// Utilizamos un contador de lineas del fichero para obtener informacion
		// acerca de la linea que nos da un error o una excepcion
		int contLine = 2;
		Employee emp = null;

		try {
			reader = new FileReader(f);
			br = new BufferedReader(reader);
			log.info("Accedemos al fichero");
			// Leemos la primera linea, que es la informacion de las columnas
			String line = br.readLine();
			String[] columns = line.split(";");
			HashMap<Integer, String> ordenColumnas = new HashMap<Integer, String>();
			for (int i = 0; i < columns.length; i++) {
				ordenColumnas.put(i, columns[i]);
			}
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
							log.info("[" + dataEmployee.get(0).trim().toUpperCase() + "] - " + dataEmployee.toString());
							dataEmployee.add("");

							// Aquí compruebo que si no hay nada en ese dato, me ponga en valor del
							// ArrayList que es un valor nulo
							if (dataEmployee.get(employeeValue - 1).length() == 0) {
								dataEmployee.set(employeeValue - 1, "NULL");
							}

						} else {
							dataEmployee.set(employeeValue, dataEmployee.get(employeeValue) + line.charAt(i));
						}

						if (i == line.length() - 1) {
							log.info("[" + dataEmployee.get(0).trim().toUpperCase() + "] - " + dataEmployee.toString());
						}

					}

					// Añadimos al HashMap el objeto Employee que utiliza de clave el ID de ese
					// empleado
					emp = crearEmpleado(dataEmployee, ordenColumnas);
					map.put(emp.getId(), emp);

				} catch (NullPointerException e) {
					log.error("Linea (" + contLine + ") del Fichero \"" + nameCSV + "\" esta vacia", e);

				} catch (IndexOutOfBoundsException e) {
					log.error("ID: [" + id + "] - NºLinea: (" + contLine + ") - Fichero: \"" + nameCSV + "\" - Linea: {"
							+ line + "}\nNo se ha podido crear el objeto empleado. Fallo al leer linea", e);

				} catch (ParseException e) {
					log.error("ID: [" + emp.getId() + "] - NºLinea: (" + contLine + ") - Fichero: \"" + nameCSV
							+ "\" - Linea: {" + line + "}\nNo se ha podido crear el objeto empleado.", e);

				} catch (NumberFormatException e) {
					log.error("ID: [" + id + "] - NºLinea: (" + contLine + ") - Fichero: \"" + nameCSV + "\" - Linea: {"
							+ line + "}\nNo se ha podido crear el objeto empleado. Numero introducido incorrecto", e);

				} catch (Exception e) {
					log.error("Fallo generico en la linea (" + contLine + ") del Fichero \"" + nameCSV + "\"", e);

				}

				// Cambiamos la linea del contador
				contLine++;
			}

		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado: \"" + nameCSV + "\"", e);

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
	 * Devuelve un empleado sacando los datos de la list de dataEmployee y la
	 * organizacion de orderColumns
	 * 
	 * @param dataEmployee datos del empleado no organizados
	 * @param orderColumns nombre de las columnas con la posicion que tienen
	 * @return objeto empleado con los datos correspondientes
	 * @throws ParseException
	 */
	private Employee crearEmpleado(List<String> dataEmployee, HashMap<Integer, String> orderColumns)
			throws ParseException {

		String id = "", name = "", surname1 = "", surname2 = "", tlf = "", email = "", job = "";
		Date hiringDate = null;
		int yearSalary = -1;
		boolean sickLeave = false;

		for (int i = 0; i < dataEmployee.size(); i++) {
			if (orderColumns.get(i).equals(ID)) {
				id = dataEmployee.get(i).trim().toUpperCase();
			} else if (orderColumns.get(i).equals(NAME)) {
				name = dataEmployee.get(i).trim();
			} else if (orderColumns.get(i).equals(SURNAME1)) {
				surname1 = dataEmployee.get(i).trim();
			} else if (orderColumns.get(i).equals(SURNAME2)) {
				surname2 = dataEmployee.get(i).trim();
			} else if (orderColumns.get(i).equals(PHONE)) {
				tlf = dataEmployee.get(i).trim();
			} else if (orderColumns.get(i).equals(EMAIL)) {
				email = dataEmployee.get(i).trim();
			} else if (orderColumns.get(i).equals(JOB)) {
				job = dataEmployee.get(i).trim();
			} else if (orderColumns.get(i).equals(HIRING_DATE)) {
				/*
				 * Aquí formateamos la cadena obtenida, que en el caso ideal es una fecha, a un
				 * tipo Date
				 */
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				formatter.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
				hiringDate = formatter.parse(dataEmployee.get(i));
			} else if (orderColumns.get(i).equals(YEAR_SALARY)) {
				// Aqui formateamos el salario anual a numero entero
				yearSalary = Integer.parseInt(dataEmployee.get(i));
			} else if (orderColumns.get(i).equals(SICK_LEAVE)) {
				if (dataEmployee.get(i).equals("true")) {
					sickLeave = true;
				}
			}
			/*
			 * switch (orderColumns.get(i)) { case : id =
			 * dataEmployee.get(i).trim().toUpperCase(); break; case "name": name =
			 * dataEmployee.get(i).trim(); break; case "surname1": surname1 =
			 * dataEmployee.get(i).trim(); break; case "surname2": surname2 =
			 * dataEmployee.get(i).trim(); break; case "tlf": tlf =
			 * dataEmployee.get(i).trim(); break; case "email": email =
			 * dataEmployee.get(i).trim(); break; case "job": job =
			 * dataEmployee.get(i).trim(); break; case "hiring_date": /* Aquí formateamos la
			 * cadena obtenida, que en el caso ideal es una fecha, a un tipo Date
			 */
			/*
			 * SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			 * formatter.setTimeZone(TimeZone.getTimeZone("Europe/Madrid")); hiringDate =
			 * formatter.parse(dataEmployee.get(i)); break; case "year_salary": // Aqui
			 * formateamos el salario anual a numero entero yearSalary =
			 * Integer.parseInt(dataEmployee.get(i)); break; case "sick_leave": // Aquí
			 * comprobamos si el empleado está dado de baja o no if
			 * (dataEmployee.get(i).equals("true")) { sickLeave = true; } break;
			 * 
			 * default: break; }
			 */
		}

		/*
		 * Creamos el objeto empleado normalizando el id en mayusculas, eliminamos los
		 * espacios al principìo y al final
		 */

		Employee emp = new Employee(id, name, surname1, surname2, tlf, email, job, hiringDate, yearSalary, sickLeave);

		log.trace("[" + dataEmployee.get(0).trim().toUpperCase() + "] - Empleado creado: " + emp);
		return emp;
	}

	/**
	 * Este metodo crea o sobreescribe el fichero result.csv para guardar la
	 * información final.
	 * 
	 * @param config
	 */
	public void createCSV() {
		try {
			FileWriter fw = new FileWriter(config.getProperty("DEFAULT.File.CSV.result"));
			fw.write("id;name;first surname;second surname;phone;email;job;hiring_date;year_salary;sick_leave;status");
			fw.close();
		} catch (IOException e) {
			log.error("Fallo al escribir la información de la cabecera");
		}
	}

	/**
	 * Metodo usado para añadir una linea de datos al archivo CSV de resultados
	 *
	 * @param employee El empleado que queremos añadir
	 * @param status   La accion que realizamos con el empleado, DELETE o CREATE
	 */
	public void writeCSV(Employee employee, String status) {
		try {
			FileWriter fw = new FileWriter(config.getProperty("DEFAULT.File.CSV.result"), true);
			fw.write("\n" + employee.toCSV() + ";" + status);
			fw.close();
			log.info("[" + employee.getId() + "] - \"" + status + "\"");
		} catch (IOException e) {
			log.error("[" + employee.getId() + "] - Fallo al escribir al usuario");
		}
	}

	/**
	 * Método utilizado para para añadir una linea de datos de un empleado
	 * modificado al archivo CSV de resultados. Cómo para actualizar necesitamos que
	 * los datos que no son una cadena no aparezcan como null o, en el caso del
	 * boolean, false, traemos una lista de si estos datos están actualizados o no.
	 * 
	 * @param updatedEmployee El empleado que queremos añadir
	 * @param extraData       Lista con información de los datos que no son una
	 *                        cadena, los que no se van a modificar.
	 * @param status          La accion que realizamos con el empleado, en este caso
	 *                        UPDATE.
	 */
	public void writeUpdatedEmployeeCSV(Employee updatedEmployee, List<String> extraData, String status) {

		// Creamos esta variable para enviarle al fichero CSV el contenido.
		String updatedData;

		/*
		 * Estas variables son las que vamos a darle a la cadena updatedData para pasar
		 * los datos. Estas cadenas se van a modificar si la lista extraData no contiene
		 * el dato
		 */
		String hiringDate = "", yearSalary = "", sickLeave = "";

		// Estas variables son las vamos a utilizar para comprobar si el dato ha
		// cambiado o no.
		boolean hiringDateChanged = true, yearSalaryChanged = true, sickLeaveChanged = true;

		// Comprobamos que la lista no está vacia.
		if (!extraData.isEmpty()) {

			// Recorremos la lista para saber que datos no se han cambiado.
			for (int i = 0; i < extraData.size(); i++) {

				if (extraData.get(i).equals("hiringDate")) {
					hiringDateChanged = false;
				}

				if (extraData.get(i).equals("yearSalary")) {
					yearSalaryChanged = false;
				}

				if (extraData.get(i).equals("sickLeave")) {
					sickLeaveChanged = false;
				}

			}
		}

		// Si los datos han sido cambiados, establecemos el dato para pasarselo al
		// fichero CSV.
		if (hiringDateChanged) {
			hiringDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(updatedEmployee.getHiringDate());
		}
		if (yearSalaryChanged) {
			yearSalary = String.valueOf(updatedEmployee.getYearSalary());
		}
		if (sickLeaveChanged) {
			sickLeave = String.valueOf(updatedEmployee.isSickLeave());
		}

		// Metemos los datos en la cadena que vamos a darle al fichero CSV con los datos
		// correctos.
		updatedData = updatedEmployee.getId() + ";" + updatedEmployee.getName() + ";" + updatedEmployee.getSurname1()
				+ ";" + updatedEmployee.getSurname2() + ";" + updatedEmployee.getTlf() + ";" + updatedEmployee.getMail()
				+ ";" + hiringDate + ";" + yearSalary + ";" + sickLeave;

		// Añadimos la linea de datos al fichero CSV.
		try {
			FileWriter fw = new FileWriter(config.getProperty("DEFAULT.File.CSV.result"), true);
			fw.write("\n" + updatedData + ";" + status);
			fw.close();
			log.info("[" + updatedEmployee.getId() + "] - \"" + status + "\"");

		} catch (IOException e) {
			log.error("[" + updatedEmployee.getId() + "] - Fallo al escribir al usuario");
		}

	}

}
