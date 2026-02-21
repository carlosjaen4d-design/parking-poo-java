// Gestion.java
package contenidos;

import java.time.Duration;
import java.time.LocalDateTime;

public class Gestion {

	private LocalDateTime horaEntrada;
	private LocalDateTime horaSalida;
	private Vehiculo vehiculo;
	private int plaza;

	// Tarifas (€/minuto) -> Si tu profe lo entiende por hora, lo cambiamos fácil.
	private static final double TARIFA_FURGON = 0.11;
	private static final double TARIFA_COCHE = 0.09;
	private static final double TARIFA_MOTO = 0.08;

	// Descuento nocturno simple (20%)
	private static final double DESCUENTO_NOCHE = 0.20;

	public Gestion(LocalDateTime horaEntrada, Vehiculo vehiculo, int plaza) {
		this.horaEntrada = horaEntrada;
		this.vehiculo = vehiculo;
		this.plaza = plaza;
	}

	public LocalDateTime getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(LocalDateTime horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public LocalDateTime getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(LocalDateTime horaSalida) {
		this.horaSalida = horaSalida;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public int getPlaza() {
		return plaza;
	}

	public void setPlaza(int plaza) {
		this.plaza = plaza;
	}

	public static double getTarifaFurgon() {
		return TARIFA_FURGON;
	}

	public static double getTarifaCoche() {
		return TARIFA_COCHE;
	}

	public static double getTarifaMoto() {
		return TARIFA_MOTO;
	}

	public static double getDescuentoNoche() {
		return DESCUENTO_NOCHE;
	}

	/**
	 * Devuelve minutos de estancia (si no hay salida, usa "ahora").
	 * Mínimo 1 minuto para evitar 0€ si entra y sale instantáneo.
	 */
	public long getMinutosEstancia() {
		LocalDateTime salida = (horaSalida == null) ? LocalDateTime.now() : horaSalida;
		long minutos = Duration.between(horaEntrada, salida).toMinutes();
		return Math.max(1, minutos);
	}

	/**
	 * Calcula el importe de la estancia.
	 * - Se usa tarifa por minuto según tipo de vehículo.
	 * - Descuento nocturno simple: si la entrada es nocturna.
	 */
	public double calcularEstancia() {

		long minutos = getMinutosEstancia();

		double tarifa = TARIFA_COCHE;
		if (vehiculo.getTipo() == TipoVehiculo.MOTO) tarifa = TARIFA_MOTO;
		if (vehiculo.getTipo() == TipoVehiculo.FURGON) tarifa = TARIFA_FURGON;

		double importe = minutos * tarifa;

		// Nocturno simple: 22:00–06:00 según hora de entrada
		int h = horaEntrada.getHour();
		boolean esNocturna = (h >= 22 || h < 6);

		if (esNocturna) {
			importe = importe * (1.0 - DESCUENTO_NOCHE);
		}

		return importe;
	}
}