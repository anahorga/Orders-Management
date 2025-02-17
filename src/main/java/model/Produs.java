package model;

public class Produs {
    private int id;
    private String denumire;

    private int stoc,pretPeBucata;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public int getPretPeBucata() {
        return pretPeBucata;
    }

    public void setPretPeBucata(int pretPeBucata) {
        this.pretPeBucata = pretPeBucata;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Produs() {
    }

    public Produs(int id, int stoc, int pretPeBucata, String denumire) {
        this.id = id;
        this.stoc = stoc;
        this.pretPeBucata = pretPeBucata;
        this.denumire = denumire;
    }

    public Produs(int stoc, int pretPeBucata, String denumire) {
        this.stoc = stoc;
        this.pretPeBucata = pretPeBucata;
        this.denumire = denumire;
    }
}
