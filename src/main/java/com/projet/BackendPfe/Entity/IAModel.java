package com.projet.BackendPfe.Entity;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class IAModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long  id;
	private Date date_hebergement ; 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	private Date date_fin ;
	private long  nbr_utilisation ; 
	private String  data ; 

	@ManyToOne
	private AdminDigitalManager adminD;
	
	
	public IAModel() {
		super();
	}
	
	public IAModel(Date date_hebergement, Date date_fin, long nbr_utilisation ,String data , AdminDigitalManager adminD) {
		super();
		this.date_hebergement = date_hebergement;
		this.date_fin = date_fin;
		this.nbr_utilisation = nbr_utilisation;
		this.adminD= adminD ; 
		this.data= data ; 
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public AdminDigitalManager getAdminD() {
		return adminD;
	}

	public void setAdminD(AdminDigitalManager adminD) {
		this.adminD = adminD;
	}

	public Date getDate_hebergement() {
		return date_hebergement;
	}
	public void setDate_hebergement(Date date_hebergement) {
		this.date_hebergement = date_hebergement;
	}

	public Date getDate_fin() {
		return date_fin;
	}
	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}
	public long getNbr_utilisation() {
		return nbr_utilisation;
	}
	public void setNbr_utilisation(long nbr_utilisation) {
		this.nbr_utilisation = nbr_utilisation;
	}
	
}
