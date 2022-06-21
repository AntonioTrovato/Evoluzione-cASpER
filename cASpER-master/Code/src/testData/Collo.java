package it.unisa.casper;

public class Collo {
    // dati
    private int x_size, y_size, z_size;
    protected int weight;
    // funzione getter di Weight
    public int getWeight() { return weight; }
    // Costruttore
    public Collo(int w, int xs, int ys, int zs) {
        this.weight = w;
        this.x_size = xs;
        this.y_size = ys;
        this.z_size = zs;
    }
    public int getVolume() {
        return x_size * y_size * z_size;
    }
}
