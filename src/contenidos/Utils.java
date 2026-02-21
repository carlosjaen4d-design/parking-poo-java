// Utils.java
package contenidos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Utils {

	private static final int MAX = 50;

	private static int contadorVehiculos = 0;
	private static int contadorGestion = 0;
	private static int contadorPlaza = 0;

	private static Vehiculo[] vehiculos = new Vehiculo[MAX];
	private static Gestion[] gestiones = new Gestion[MAX];

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	public static void menu() {
		System.out.println("\n==============================");
		System.out.println("       GESTIÓN PARKING");
		System.out.println("==============================");
		System.out.println("Ingrese una opción");
		System.out.println("(1) Entrada de vehículo");
		System.out.println("(2) Mostrar ocupación del parking");
		System.out.println("(3) Buscar vehículo por matrícula");
		System.out.println("(4) Salida de vehículo");
		System.out.println("(5) Estadísticas");
		System.out.println("(0) Salir");
		System.out.print("> ");
	}

	public static void cargarDatosIniciales() {

		vehiculos[contadorVehiculos] = new Vehiculo("4860GVD", "CITROEN", "Picasso", TipoVehiculo.COCHE);
		contadorVehiculos++;

		vehiculos[contadorVehiculos] = new Vehiculo("2239BZR", "VOLKSWAGEN", "Golf", TipoVehiculo.FURGON);
		contadorVehiculos++;

		vehiculos[contadorVehiculos] = new Vehiculo("0001ABC", "KTM", "DUKE", TipoVehiculo.MOTO);
		contadorVehiculos++;

		gestiones[contadorGestion] = new Gestion(LocalDateTime.of(2026, 2, 18, 15, 35), vehiculos[0], contadorPlaza + 1);
		gestiones[contadorGestion].setHoraSalida(LocalDateTime.of(2026, 2, 18, 16, 45));
		contadorGestion++;
		contadorPlaza++;

		gestiones[contadorGestion] = new Gestion(LocalDateTime.of(2026, 2, 18, 14, 59), vehiculos[1], contadorPlaza + 1);
		gestiones[contadorGestion].setHoraSalida(LocalDateTime.of(2026, 2, 18, 15, 33));
		contadorGestion++;
		contadorPlaza++;

		gestiones[contadorGestion] = new Gestion(LocalDateTime.of(2026, 2, 18, 13, 15), vehiculos[2], contadorPlaza + 1);
		gestiones[contadorGestion].setHoraSalida(LocalDateTime.of(2026, 2, 18, 16, 10));
		contadorGestion++;
		contadorPlaza++;
	}

	public static void altaVehiculo(Scanner sc) {

		if (contadorVehiculos >= MAX) {
			System.out.println("No se pueden registrar más vehículos (límite alcanzado).");
			return;
		}
		if (contadorGestion >= MAX) {
			System.out.println("Parking lleno (no se pueden registrar más entradas).");
			return;
		}

		System.out.println("\nCargando registro de vehículo...");

		String matricula = pedirMatricula(sc);

		// Evitar duplicados (si ya está dentro sin salir, no permitimos entrada)
		if (buscarGestionActivaPorMatricula(matricula) != -1) {
			System.out.println("Ese vehículo ya está dentro del parking. No se puede dar entrada otra vez.");
			return;
		}

		System.out.print("Marca: ");
		String marca = sc.nextLine().trim();

		System.out.print("Modelo: ");
		String modelo = sc.nextLine().trim();

		TipoVehiculo tipoVehiculo = pedirTipoVehiculo(sc);

		vehiculos[contadorVehiculos] = new Vehiculo(matricula, marca, modelo, tipoVehiculo);
		gestiones[contadorGestion] = new Gestion(LocalDateTime.now(), vehiculos[contadorVehiculos], contadorPlaza + 1);

		contadorVehiculos++;
		contadorGestion++;
		contadorPlaza++;

		System.out.println("Vehículo registrado correctamente en parking.");
	}

	public static void mostrarOcupacion() {
		System.out.println("\n--- OCUPACIÓN ACTUAL ---");

		int activos = 0;

		for (int i = 0; i < contadorGestion; i++) {
			if (gestiones[i] != null && gestiones[i].getHoraSalida() == null) {
				activos++;
				System.out.println(
						"Plaza " + gestiones[i].getPlaza()
						+ " | " + gestiones[i].getVehiculo().getMatricula()
						+ " | " + gestiones[i].getVehiculo().getTipo()
						+ " | Entrada: " + gestiones[i].getHoraEntrada().format(FMT)
				);
			}
		}

		if (activos == 0) {
			System.out.println("No hay vehículos actualmente dentro del parking.");
		} else {
			System.out.println("Total dentro: " + activos);
		}
	}

	public static void buscarVehiculo(Scanner sc) {
		System.out.println("\n--- BUSCAR VEHÍCULO ---");

		String matricula = pedirMatricula(sc);

		int idxActiva = buscarGestionActivaPorMatricula(matricula);
		if (idxActiva != -1) {
			Gestion g = gestiones[idxActiva];
			System.out.println("ENCONTRADO (DENTRO):");
			System.out.println("Plaza: " + g.getPlaza());
			System.out.println("Vehículo: " + g.getVehiculo());
			System.out.println("Entrada: " + g.getHoraEntrada().format(FMT));
			return;
		}

		// Si no está dentro, buscamos si existe el vehículo registrado
		int idxVeh = buscarVehiculoRegistradoPorMatricula(matricula);
		if (idxVeh != -1) {
			System.out.println("Vehículo registrado, pero ahora mismo NO está dentro del parking:");
			System.out.println(vehiculos[idxVeh]);
		} else {
			System.out.println("No existe ningún vehículo con esa matrícula.");
		}
	}

	public static void salidaVehiculo(Scanner sc) {
		System.out.println("\n--- SALIDA DE VEHÍCULO ---");

		String matricula = pedirMatricula(sc);

		int idx = buscarGestionActivaPorMatricula(matricula);
		if (idx == -1) {
			System.out.println("No se ha encontrado ese vehículo dentro del parking.");
			return;
		}

		Gestion g = gestiones[idx];
		g.setHoraSalida(LocalDateTime.now());

		long minutos = g.getMinutosEstancia();
		double importe = g.calcularEstancia();

		System.out.println("Salida registrada correctamente.");
		System.out.println("Vehículo: " + g.getVehiculo());
		System.out.println("Entrada: " + g.getHoraEntrada().format(FMT));
		System.out.println("Salida: " + g.getHoraSalida().format(FMT));
		System.out.println("Minutos: " + minutos);
		System.out.printf("Importe: %.2f €%n", importe);
	}

	public static void estadisticas() {
		System.out.println("\n--- ESTADÍSTICAS ---");

		int activos = 0;
		int finalizados = 0;
		double ingresos = 0.0;

		int coches = 0, motos = 0, furgones = 0;

		for (int i = 0; i < contadorGestion; i++) {
			if (gestiones[i] == null) continue;

			TipoVehiculo t = gestiones[i].getVehiculo().getTipo();
			if (t == TipoVehiculo.COCHE) coches++;
			else if (t == TipoVehiculo.MOTO) motos++;
			else if (t == TipoVehiculo.FURGON) furgones++;

			if (gestiones[i].getHoraSalida() == null) {
				activos++;
			} else {
				finalizados++;
				ingresos += gestiones[i].calcularEstancia();
			}
		}

		System.out.println("Vehículos registrados (total): " + contadorVehiculos);
		System.out.println("Gestiones (entradas totales): " + contadorGestion);
		System.out.println("Dentro ahora mismo: " + activos);
		System.out.println("Salidas finalizadas: " + finalizados);
		System.out.printf("Ingresos (solo finalizados): %.2f €%n", ingresos);

		System.out.println("\nDistribución por tipo (según gestiones):");
		System.out.println("COCHE: " + coches);
		System.out.println("MOTO: " + motos);
		System.out.println("FURGON: " + furgones);
	}

	// ==========================
	// Helpers (privados)
	// ==========================

	private static String pedirMatricula(Scanner sc) {
		// Formato como tus datos iniciales: 4860GVD / 2239BZR / 0001ABC
		String regexMatricula = "^[0-9]{4}[A-Z]{3}$";
		String matricula;

		do {
			System.out.print("Matrícula (ej: 4860GVD): ");
			matricula = sc.nextLine().trim().toUpperCase();

			if (!matricula.matches(regexMatricula)) {
				System.out.println("Matrícula no válida. Formato: 4 números + 3 letras (ej: 4860GVD).");
			}
		} while (!matricula.matches(regexMatricula));

		return matricula;
	}

	private static TipoVehiculo pedirTipoVehiculo(Scanner sc) {
		System.out.print("Tipo vehículo (coche/moto/furgon): ");
		String tipo = sc.nextLine().trim();

		if (tipo.equalsIgnoreCase("coche")) return TipoVehiculo.COCHE;
		if (tipo.equalsIgnoreCase("moto")) return TipoVehiculo.MOTO;
		if (tipo.equalsIgnoreCase("furgon") || tipo.equalsIgnoreCase("furgón")) return TipoVehiculo.FURGON;

		System.out.println("Tipo no válido. Se usará COCHE por defecto.");
		return TipoVehiculo.COCHE;
	}

	private static int buscarVehiculoRegistradoPorMatricula(String matricula) {
		for (int i = 0; i < contadorVehiculos; i++) {
			if (vehiculos[i] != null && vehiculos[i].getMatricula().equalsIgnoreCase(matricula)) {
				return i;
			}
		}
		return -1;
	}

	private static int buscarGestionActivaPorMatricula(String matricula) {
		for (int i = 0; i < contadorGestion; i++) {
			if (gestiones[i] != null
					&& gestiones[i].getHoraSalida() == null
					&& gestiones[i].getVehiculo().getMatricula().equalsIgnoreCase(matricula)) {
				return i;
			}
		}
		return -1;
	}
}