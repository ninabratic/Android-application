package model;

public class DTO {
	private double ukupno;
	private int ukupnoFilmova;
	private String email;
	private String filmovi;

	public DTO(double ukupno, int ukupnoFilmova, String email, String filmovi) {
		super();
		this.ukupno = ukupno;
		this.ukupnoFilmova = ukupnoFilmova;
		this.email = email;
		this.filmovi = filmovi;
	}

	public DTO() {

	}

	public String getFilmovi() {
		return filmovi;
	}

	public void setFilmovi(String filmovi) {
		this.filmovi = filmovi;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getUkupno() {
		return ukupno;
	}

	public void setUkupno(double ukupno) {
		this.ukupno = ukupno;
	}

	public int getUkupnoFilmova() {
		return ukupnoFilmova;
	}

	public void setUkupnoFilmova(int ukupnoFilmova) {
		this.ukupnoFilmova = ukupnoFilmova;
	}

}
