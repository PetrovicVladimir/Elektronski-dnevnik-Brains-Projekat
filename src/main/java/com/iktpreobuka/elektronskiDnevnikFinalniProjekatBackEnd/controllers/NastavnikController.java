package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

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
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.RoditeljEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.NastavnikDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.RoditeljDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.KorisnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UlogaRepository;

@RestController
@RequestMapping(value = "/nastavnik")
public class NastavnikController {

	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private UlogaRepository ulogaRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(NastavnikController.class);

	/***** GET Vrati sve nastavnike *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> vratiSve() {
		LOGGER.info("Vraceni su svi nastavnici.");
		return new ResponseEntity<>(nastavnikRepository.findAll(), HttpStatus.OK);
	}

	/***** GET Vrati korisnika na osnovu ID *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiNastavnikByID(@PathVariable Integer id) {
		try {
			if (!nastavnikRepository.findById(id).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "Nastavnik nije nadjen!"),
						HttpStatus.BAD_REQUEST);
			}
			LOGGER.info("Vracen je nastavnik sa ID brojem: " + id);
			return new ResponseEntity<KorisnikEntity>(nastavnikRepository.findById(id).get(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Dodaj novog sa ulogom
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNovogNastavnika(@Valid @RequestBody NastavnikDTO noviNastavnik,
			@RequestParam Integer ulogaId) {
//		if (result.hasErrors()) {
//			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
//		} else {
//			roditeljValidator.validate(noviRoditelj, result);
//		}
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		NastavnikEntity nastavnik = new NastavnikEntity();
		nastavnik.setUloga(uloga);
		nastavnik.setIme(noviNastavnik.getIme());
		nastavnik.setPrezime(noviNastavnik.getPrezime());
		nastavnik.setKorisnickoIme(noviNastavnik.getKorisnickoIme());
		nastavnik.setSifra(noviNastavnik.getSifra());
		nastavnik.setEmail(noviNastavnik.getEmail());
		nastavnikRepository.save(nastavnik);

		return new ResponseEntity<>(nastavnik, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{nastavnikId}")
	public ResponseEntity<?> izmeniNastavnika(@PathVariable Integer nastavnikId,
			@Valid @RequestBody NastavnikDTO noviNastavnik, @RequestParam Integer ulogaId) {
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		NastavnikEntity nastavnik = nastavnikRepository.findById(nastavnikId).get();

		nastavnik.setUloga(uloga);
		nastavnik.setIme(noviNastavnik.getIme());
		nastavnik.setPrezime(noviNastavnik.getPrezime());
		nastavnik.setKorisnickoIme(noviNastavnik.getKorisnickoIme());
		nastavnik.setSifra(noviNastavnik.getSifra());
		nastavnik.setEmail(noviNastavnik.getEmail());
		nastavnikRepository.save(nastavnik);

		return new ResponseEntity<>(nastavnik, HttpStatus.OK);
	}

	/***** DELETE - Obrisi nastavnika *****/
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiNastavnika(@PathVariable Integer id) {
		try {
			NastavnikEntity nastavnik = nastavnikRepository.findById(id).get();
			nastavnikRepository.delete(nastavnik);
			LOGGER.info("Obrisan je nastavnik sa ID brojem: " + nastavnik.getId());
			return new ResponseEntity<>(nastavnik, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
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
