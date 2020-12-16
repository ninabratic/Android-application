package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Film implements Parcelable {

    private String naziv;
    private String slika;
    private double cena;

    protected Film(Parcel in) {
        naziv = in.readString();
        slika = in.readString();
        cena = in.readDouble();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;

    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public Film(String naziv, String slika, double cena) {
        this.naziv = naziv;
        this.slika = slika;
        this.cena = cena;
    }

    public Film(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(slika);
        dest.writeDouble(cena);
    }
}
