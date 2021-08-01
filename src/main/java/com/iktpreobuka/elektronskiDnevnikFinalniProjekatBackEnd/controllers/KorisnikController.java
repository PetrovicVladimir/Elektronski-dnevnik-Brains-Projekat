package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.utils.Encryption;


import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.exception.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers.util.RESTError;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.NastavnikDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.KorisnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UlogaRepository;

@RestController
@RequestMapping(value = "/korisnik")
public class KorisnikController {

	@Autowired
	private KorisnikRepository korisnikRepository;
	@Autowired
	private UlogaRepository ulogaRepository;
	
	private final Logger LOGGER = LoggerFactory.getLogger(KorisnikController.class);
	

	/***** GET Vrati sve korisnike *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		LOGGER.info("Vraceni su svi korisnici.");
		return new ResponseEntity<>(korisnikRepository.findAll(), HttpStatus.OK);
	}

	/***** GET Vrati korisnika za ID *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getUcenikByID(@PathVariable Integer id) {
		try {
			if (!korisnikRepository.findById(id).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Korisnik nije nadjen"), HttpStatus.NOT_FOUND);
			}

			LOGGER.info("Vracen je korisnik sa ID brojem: " + id);
			return new ResponseEntity<KorisnikEntity>(korisnikRepository.findById(id).get(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** POST Dodaj korisnika *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNovogKorisnika(@Valid @RequestBody KorisnikEntity noviKorisnik,
			@RequestParam Integer ulogaId) {
		try {
//			Encryption.getPassEncoded(noviKorisnik.getSifra());
//			String encodedSifra = Encryption.getPassEncoded(noviKorisnik.getSifra());
//			String encodedPotvrdaSifre = Encryption.getPassEncoded(noviKorisnik.getPotvrdaSifre());
			UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
//			noviKorisnik.setSifra(encodedSifra);
//			noviKorisnik.setPotvrdaSifre(encodedPotvrdaSifre);
			noviKorisnik.setUloga(uloga);
			korisnikRepository.save(noviKorisnik);

			LOGGER.info("Dodat je novi korisnik sa ID brojem: " + noviKorisnik.getId());
			return new ResponseEntity<>(noviKorisnik, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/***** PUT Izmeni korisnika *****/
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{korisnikId}")
	public ResponseEntity<?> izmeniKorisnika(@PathVariable Integer korisnikId, @Valid @RequestBody KorisnikEntity noviKorisnik, 
			@RequestParam Integer ulogaId) {
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		KorisnikEntity korisnik = korisnikRepository.findById(korisnikId).get();
		
		korisnik.setUloga(uloga);
		korisnik.setIme(noviKorisnik.getIme());
		korisnik.setPrezime(noviKorisnik.getPrezime());
		korisnik.setKorisnickoIme(noviKorisnik.getKorisnickoIme());
		korisnik.setSifra(noviKorisnik.getSifra());
		
		korisnikRepository.save(korisnik);

		return new ResponseEntity<>(korisnik, HttpStatus.OK);
	}

	/***** DELETE - Obrisi korisnika *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiKorisnika(@PathVariable Integer id) {
		try {
			KorisnikEntity korisnik = korisnikRepository.findById(id).get();
			korisnikRepository.delete(korisnik);

			LOGGER.info("Obrisan je korisnik sa ID brojem: " + korisnik.getId());

			return new ResponseEntity<>(korisnik, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal server error. Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Validacija
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
