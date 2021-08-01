package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.PredavanjaPregled;

public interface PredavanjeService {

	
	public List<PredmetEntity> izlistajPredmeteNastavnika(Integer nastavnikId);
	
	public List<NastavnikEntity> vratiNastavnikePredmeta(Integer predmetId);
	
	public List<PredavanjaPregled> spisakPredavanjaNastavnika(Integer nastavnikId);
	
	public PredavanjeEntity dodajNovoPredavanje(Integer predmetId,Integer nastavnikId);
		
	public PredavanjeEntity izmeniPredavanje(Integer predavanjeId, Integer predmetId,Integer nastavnikId); 
	

}
