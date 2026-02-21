// Vehiculo.java
package contenidos;

public class Vehiculo {

	private String matricula;
	private String marca;
	private String modelo;
	private TipoVehiculo tipo;

	public Vehiculo(String matricula, String marca, String modelo, TipoVehiculo tipo) {
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.tipo = tipo;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getMarca() {
		return marca;
	}

	public String getModelo() {
		return modelo;
	}

	public TipoVehiculo getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return "Vehiculo{matricula='" + matricula + "', marca='" + marca + "', modelo='" + modelo + "', tipo=" + tipo + "}";
	}
}