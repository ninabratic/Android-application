package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.client.User;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kupovina implements Parcelable {
    private List<Film> filmovi = new ArrayList<>();
    private double ukupno = 0.0;
    private User korisnik;
//    private Date datum;


    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Kupovina.class);
    }
    protected Kupovina(Parcel in) {
        filmovi = in.createTypedArrayList(Film.CREATOR);
        ukupno = in.readDouble();
        korisnik = in.readParcelable(User.class.getClassLoader());
//        in.writeSerializable(datum);
    }

    public static final Creator<Kupovina> CREATOR = new Creator<Kupovina>() {
        @Override
        public Kupovina createFromParcel(Parcel in) {
            return new Kupovina(in);
        }

        @Override
        public Kupovina[] newArray(int size) {
            return new Kupovina[size];
        }
    };

    public List<Film> getFilmovi() {
        return filmovi;
    }

    public void setFilmovi(List<Film> filmovi) {
        this.filmovi = filmovi;
    }

    public double getUkupno() {
        return ukupno;
    }

    public void setUkupno(double ukupno) {
        this.ukupno = ukupno;
    }

    public User getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(User korisnik) {
        this.korisnik = korisnik;
    }

//    public Date getDatum() {
//        return datum;
//    }
//
//    public void setDatum(Date datum) {
//        this.datum = datum;
//    }

    public Kupovina(List<Film> filmovi, double ukupno, User korisnik) {
        this.filmovi = filmovi;
        this.ukupno = ukupno;
        this.korisnik = korisnik;
//        this.datum = datum;
    }

    public Kupovina(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(filmovi);
        dest.writeDouble(ukupno);
        dest.writeParcelable(korisnik, flags);
        //        dest.writeSerializable(datum);
    }
}
