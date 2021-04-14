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
	// private String path =
	// "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\csv\\";
	private String path = "C:\\Users\\mparrap\\git\\csvRepsol\\csv\\";

	/**
	 * Lee los empleados de un csv, y devuelme la lista en un HasMap organizado por
	 * <id del empledado, objeto empleado>
	 *
	 * @param nameCSV Nombre del csv que quieres leer
	 * @return HasMap De los empleados con su id como key
	 */
	public HashMap<String, Employee> readCSV(String nameCSV) {
		HashMap<String, Employee> map = new HashMap<>();
		File f = new File(path + nameCSV + ".csv");
		log.info("Ruta del fichero" + f.getPath());
		FileReader reader = null;
		BufferedReader br = null;
		int contLine = 2;
		try {
			reader = new FileReader(f);
			br = new BufferedReader(reader);
			String line = br.readLine();
			
			while (line != null) {
				try {
					line = br.readLine();
					/*
					 * Separamos los valores para separar y eliminar por ";" asi solo separa por los
					 * ; que sabemos que son final de dato
					 */
					List<String> dataEmployee = new ArrayList<String>();
					dataEmployee.add("");
					boolean comillasAbiertas = false;
					int employeeValue = 0;
					for (int i = 0; i < line.length(); i++) {
						/* Aqui observo si tengo una comillas abiertas */
						if (line.charAt(i) == '"') {
							if (comillasAbiertas) {
								comillasAbiertas = false;
							} else {
								comillasAbiertas = true;
							}

						}
						/* aqui decido si salto de valor */
						if (line.charAt(i) == ';' && comillasAbiertas == false) {
							employeeValue++;
							dataEmployee.add("");
							if (dataEmployee.get(employeeValue - 1).length() == 0) {
								dataEmployee.set(employeeValue - 1, "NULL");
							}
						} else {
							dataEmployee.set(employeeValue, dataEmployee.get(employeeValue) + line.charAt(i));
						}

					}
					Date hiringDate = new SimpleDateFormat("dd/MM/yyyy").parse(dataEmployee.get(HIRING_DATE));
					boolean sickLeave = false;
					if (dataEmployee.get(SICK_LEAVE).equals("true")) {
						sickLeave = true;
					}
					int yearSalary = -1;
					try {
						yearSalary = Integer.parseInt(dataEmployee.get(YEAR_SALARY));
					} catch (Exception e) {
						e.printStackTrace();
					}
					/*
					 * Creamos el objeto empleado normalizando el id en mayusculas, eliminamos los
					 * espacion al principìo y al final y eliminamos la primera comilla de la linea
					 * y la ultima
					 */
					Employee emp = new Employee(dataEmployee.get(ID).trim().toUpperCase(),
							dataEmployee.get(NAME).trim(), dataEmployee.get(SURNAME1).trim(),
							dataEmployee.get(SURNAME2).trim(), dataEmployee.get(TLF).trim(),
							dataEmployee.get(MAIL).trim(), dataEmployee.get(JOB).trim(), hiringDate, yearSalary,
							sickLeave);
					map.put(emp.getId(), emp);
				} catch (NullPointerException e) {
					log.error("La linea " + contLine + " del CSV " + nameCSV + " esta vacia", e);

				}catch (IndexOutOfBoundsException e) {
					log.error("Fallo al leer la siguiente linea: " + contLine + " del CSV:" + nameCSV + " obtenido por falta de columnas", e);

				}catch (ParseException e) {
					log.error("Fallo paseado:" + contLine + " del CSV:" + nameCSV + ". Comprobar si el formato es correcto o faltan columnas", e);
				}catch (Exception e) {
					log.error("Fallo generico en linea:" + contLine + " del CSV:" + nameCSV, e);
				}
				contLine++;
			}

		} catch (FileNotFoundException e) {
			log.error("Fallo a la hora de encontrar el fichero con los datos", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Fallo de entrada o salida", e);
			e.printStackTrace();
		} finally {
			try {
				br.close();
				reader.close();
				log.info("Lectura finalizada con " + (contLine-2) + " lineas leidas en fichero " + nameCSV);
			} catch (IOException e) {
				log.error("Fallo de entrada o salida", e);
				e.printStackTrace();
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
}
