// Main.java
package contenidos;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Utils.cargarDatosIniciales();

		Scanner entrada = new Scanner(System.in);
		int opcion;

		do {
			Utils.menu();
			try {
				opcion = Integer.parseInt(entrada.nextLine());
			} catch (NumberFormatException e) {
				opcion = -1;
			}

			switch (opcion) {
			case 1:
				Utils.altaVehiculo(entrada);
				break;
			case 2:
				Utils.mostrarOcupacion();
				break;
			case 3:
				Utils.buscarVehiculo(entrada);
				break;
			case 4:
				Utils.salidaVehiculo(entrada);
				break;
			case 5:
				Utils.estadisticas();
				break;
			case 0:
				System.out.println("Saliendo...");
				break;
			default:
				System.out.println("Ingrese una opción válida");
			}

		} while (opcion != 0);

		entrada.close();
	}
}