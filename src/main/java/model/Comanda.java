package model;

public class Comanda {

   private int id,idProdus,idClient,cantitate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public Comanda() {
    }

    public Comanda(int id, int idProdus, int idClient, int cantitate) {
        this.id = id;
        this.idProdus = idProdus;
        this.idClient = idClient;
        this.cantitate = cantitate;
    }

    public Comanda(int idProdus, int idClient, int cantitate) {
        this.idProdus = idProdus;
        this.idClient = idClient;
        this.cantitate = cantitate;
    }
}
