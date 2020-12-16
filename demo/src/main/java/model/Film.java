package model;

public class Film {
	private String naziv;
	private double cena;

	public Film(String naziv, double cena) {
		super();
		this.naziv = naziv;
		this.cena = cena;
	}

	public Film() {

	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

}
