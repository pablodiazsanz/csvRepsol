package csvRepsol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CsvAccess {
	
	//private String path = "C:\\Users\\mparrap\\IdeaProjects\\csvRepsol2\\csv\\";
    //private String path = "C:\\Users\\pdiazs\\IdeaProjects\\csvRepsol2\\csv\\";
	private String path = "C:\\Users\\pdiazs\\eclipse-workspace\\csvRepsol\\csv\\";

    /**
     * Lee los empleados de un csv, y devuelme la lista en un HasMap organizado por <id del empledado, objeto empleado>
     *
     * @param nameCSV Nombre del csv que quieres leer
     * @return HasMap De los empleados con su id como key
     */
    public HashMap<String, Employee> readCSV(String nameCSV) {
        HashMap<String, Employee> map = new HashMap<>();
        File f = new File(path + nameCSV + ".csv");
        FileReader reader = null;
        BufferedReader br = null;

        try {
            reader = new FileReader(f);
            br = new BufferedReader(reader);
            String line = br.readLine();

            while (line != null) {
                try {
                    line = br.readLine();
                    /*Separamos los valores para separar y eliminar por ";" asi solo separa por los ; que sabemos que son final de dato */
                    String[] data_employee = new String[7];
                    boolean comillasAbiertas = false;
                    int ultimoCorte = 0;
                    int valorEmpleado = 0;
                    data_employee[0] = "";
                    for (int i = 0; i < line.length(); i++) {
                        /*Aqui observo si tengo una comillas abiertas*/
                        if (line.charAt(i) == '"') {
                            if (comillasAbiertas) {
                                comillasAbiertas = false;
                            } else {
                                comillasAbiertas = true;
                            }

                        }
                        /*aqui decido si salto de valor*/
                        if (line.charAt(i) == ';' && comillasAbiertas == false) {
                            valorEmpleado++;
                            data_employee[valorEmpleado] = "";
                            if(data_employee[valorEmpleado-1].length() == 0){
                                data_employee[valorEmpleado-1] = "NULL";
                            }
                        } else {
                            data_employee[valorEmpleado] += line.charAt(i);
                        }


                    }

                    /*Creamos el objeto empleado normalizando el id en mayusculas, eliminamos los espacion al princip�o y al final y eliminamos
                     * la primera comilla de la linea y la ultima*/
                    Employee emp = new Employee(data_employee[0].trim().toUpperCase(),data_employee[1].trim(), data_employee[2].trim(),
                            data_employee[3].trim(), data_employee[4].trim(),
                            data_employee[5].trim(), data_employee[6].trim());
                    map.put(emp.getId(), emp);
                } catch (Exception e) {
                    line = null;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                reader.close();
            } catch (IOException e) {
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
            fw.write("id;name;first surname;second surname;phone;email;job;status");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo usado para a�adir una linea de datos al archivo csv de resultados
     *
     * @param employee El empleado que queremos a�adir
     * @param status   La accion que realizamos con el empleado, UPDATE, DELETE o CREATE
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
