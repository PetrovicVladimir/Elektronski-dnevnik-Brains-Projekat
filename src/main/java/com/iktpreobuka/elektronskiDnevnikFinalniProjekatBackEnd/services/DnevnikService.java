package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OcenaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredmetiOceneUcenikaDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.models.EmailObject;

public interface DnevnikService {

	public List<PredmetiOceneUcenikaDTO> vratiOceneUcenikaUceniku(Integer ucenikId); 
	
	public List<PredmetiOceneUcenikaDTO> vratiOceneUcenikaRoditelju(Integer ucenikId, Integer roditeljId);
	
	public List<PredmetiOceneUcenikaDTO> vratiOceneUcenikaIzPredmeta(Integer ucenikId,String predmet); 
	
	public DnevnikEntity obavestavanjeRoditelja(Integer ucenikId,Integer predavanjeId, Integer ocenaId); 
		
	public List<PredmetiOceneUcenikaDTO> vratiOceneNastavnikaDatihUcenikuZaPredavanjeKojiPredaje(Integer predavanjeId); 
		
	public List<PredmetiOceneUcenikaDTO> OceneDatieUcenikucimaZaPredavanjeIPolugosite(Integer predavanjeId, String polugodiste);
	
	public DnevnikEntity izmeniPredmet(Integer dnevnikid, Integer predavanjeId, Integer ucenikId, Integer ocenaId); 
	
	public DnevnikEntity deleteDnevnik(Integer id);
}
