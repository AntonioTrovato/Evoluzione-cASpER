public class ProdottoBean {
	private String objectId, categoria, nome, descrizione, urlImmagine, brand, specifiche;
	private double prezzo;

	private int quantita;
	
	public ProdottoBean() {
		this.quantita=1000;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita){this.quantita=quantita;}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSpecifiche() {
		return specifiche;
	}

	public void setSpecifiche(String specifiche) {
		this.specifiche = specifiche;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		int temp = (int)(prezzo*100.0);
		this.prezzo = ((double)temp)/100.0;
	}

	public String getUrlImmagine() {
		return urlImmagine;
	}

	public void setUrlImmagine(String urlImmagine) {
		this.urlImmagine = urlImmagine;
	}

	@Override
	public String toString() {
		return "ProdottoBean{" +
				"objectId='" + objectId + '\'' +
				", categoria='" + categoria + '\'' +
				", nome='" + nome + '\'' +
				", descrizione='" + descrizione + '\'' +
				", urlImmagine='" + urlImmagine + '\'' +
				", brand='" + brand + '\'' +
				", specifiche='" + specifiche + '\'' +
				", prezzo=" + prezzo +
				'}';
	}
}
