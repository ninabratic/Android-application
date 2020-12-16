package model;

import java.util.List;

import com.google.gson.GsonBuilder;

public class Kupovina {
	private double ukupno;
	private List<Film> filmovi;

	public Kupovina(double ukupno, List<Film> filmovi) {
		super();
		this.ukupno = ukupno;
		this.filmovi = filmovi;
	}

	public Kupovina() {

	}

	public double getUkupno() {
		return ukupno;
	}

	public void setUkupno(double ukupno) {
		this.ukupno = ukupno;
	}

	public List<Film> getFilmovi() {
		return filmovi;
	}

	public void setFilmovi(List<Film> filmovi) {
		this.filmovi = filmovi;
	}
	
	

}
