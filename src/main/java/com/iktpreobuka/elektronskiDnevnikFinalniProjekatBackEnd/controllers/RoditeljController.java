package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.RoditeljEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.RoditeljDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.UcenikDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.KorisnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.RoditeljRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UcenikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UlogaRepository;
//import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.utils.RoditeljCustomValidator;

@RestController
@RequestMapping(path = "/roditelj")
public class RoditeljController {


	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private UlogaRepository ulogaRepository;
	@Autowired
	private OdeljenjeRepository odeljenjeRepository;
	@Autowired
	private UcenikRepository ucenikRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(RoditeljController.class);
//	@Autowired
//	private RoditeljCustomValidator roditeljValidator;

//	@InitBinder
//	protected void initBinder(final WebDataBinder binder) {
//		binder.addValidators(roditeljValidator);
//	}
//	
//	private String createErrorMessage(BindingResult result) {
//		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
//	}

	// Vrati sve
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(roditeljRepository.findAll(), HttpStatus.OK);
	}

	// Dodaj novog sa ulogom
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNovogRoditelja(@Valid @RequestBody RoditeljDTO noviRoditelj,
			@RequestParam Integer ulogaId, BindingResult result) {
//		if (result.hasErrors()) {
//			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
//		} else {
//			roditeljValidator.validate(noviRoditelj, result);
//		}
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		RoditeljEntity roditelj = new RoditeljEntity();
		roditelj.setUloga(uloga);
		roditelj.setIme(noviRoditelj.getIme());
		roditelj.setPrezime(noviRoditelj.getPrezime());
		roditelj.setKorisnickoIme(noviRoditelj.getKorisnickoIme());
		roditelj.setSifra(noviRoditelj.getSifra());
		roditelj.setEmail(noviRoditelj.getEmail());
		roditeljRepository.save(roditelj);

		return new ResponseEntity<>(roditelj, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/decaRoditelja/{roditeljId}")
	public ResponseEntity<?> vratiSveUcenikeRoditelja(@PathVariable Integer roditeljId){
		
		RoditeljEntity roditelj = roditeljRepository.findById(roditeljId).get();
		
		List<UcenikEntity> ucenik = ucenikRepository.findAllByRoditelj(roditelj);
		
		List<UcenikEntity> pregledUcenika = new ArrayList<UcenikEntity>();
		for (UcenikEntity ucenikEntity : ucenik) {
			UcenikEntity uce = new UcenikEntity();
			uce.setId(ucenikEntity.getId());
			uce.setIme(ucenikEntity.getIme());
			uce.setPrezime(ucenikEntity.getPrezime());
			uce.setJMBG(ucenikEntity.getJMBG());//dodati i informacije vezene za ulogu, roditelj, odeljenje...
			pregledUcenika.add(uce);
		}
		return new ResponseEntity<>(pregledUcenika, HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{roditeljId}")
	public ResponseEntity<?> izmeniRoditelja(@PathVariable Integer roditeljId, @Valid @RequestBody RoditeljDTO noviRoditelj, 
			@RequestParam Integer ulogaId) {
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		RoditeljEntity roditelj = roditeljRepository.findById(roditeljId).get();
		
		roditelj.setUloga(uloga);
		roditelj.setIme(noviRoditelj.getIme());
		roditelj.setPrezime(noviRoditelj.getPrezime());
		roditelj.setKorisnickoIme(noviRoditelj.getKorisnickoIme());
		roditelj.setSifra(noviRoditelj.getSifra());
		roditelj.setEmail(noviRoditelj.getEmail());
		roditeljRepository.save(roditelj);

		return new ResponseEntity<>(roditelj, HttpStatus.OK);
	}
	
	
	// Obrisi
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiRoditelja(@PathVariable Integer id) {
		try {
			RoditeljEntity roditelj = roditeljRepository.findById(id).get();
			roditeljRepository.delete(roditelj);
			return new ResponseEntity<>(roditelj, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal server error. Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	
}
