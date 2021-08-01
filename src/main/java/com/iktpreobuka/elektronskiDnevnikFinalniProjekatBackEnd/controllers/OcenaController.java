package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OcenaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.DnevnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.OcenaRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredavanjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.PredmetRepository;

@RestController
@RequestMapping(path = "/ocena")
public class OcenaController {
	@Autowired
	private DnevnikRepository dnevnikRepository;
	@Autowired
	private OcenaRepository ocenaRepository;
	private final Logger LOGGER = LoggerFactory.getLogger(OcenaController.class);

	@RequestMapping(method = RequestMethod.GET)
	@Secured({ "ROLE_ADMIN", "ROLE_NASTAVNIK" })
	public ResponseEntity<?> vratiSve() {
		LOGGER.info("Vracene sve ocene");
		return new ResponseEntity<>(ocenaRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> vratiOcenuZaId(@PathVariable Integer id) {
		try {
			if (!ocenaRepository.findById(id).isPresent()) {
				return new ResponseEntity<RESTError>(new RESTError(1, "User not found"), HttpStatus.BAD_REQUEST);
			}
			OcenaEntity ocena = ocenaRepository.findById(id).get();
			LOGGER.info("Pronadjena ocena na osnovu ID: " + id);
			return new ResponseEntity<>(ocena, HttpStatus.OK);
		} catch (Exception e) { // u slucaju izuzetka vratiti 500
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// POST Dodaj novu ocenu
	@Secured({ "ROLE_ADMIN", "ROLE_NASTAVNIK" })
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNovuOcenu(@RequestBody @Valid OcenaEntity nOcena) {

		nOcena.setDatumOcenjivanja(LocalDate.now());
		ocenaRepository.save(nOcena);

		LOGGER.info("Dodata nova ocena: " + nOcena.getId());

		return new ResponseEntity<>(nOcena, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_NASTAVNIK" })
	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{id}")
	public ResponseEntity<?> izmeniOcenu(@PathVariable Integer id, @Valid @RequestBody OcenaEntity nOcena) {
		OcenaEntity ocena = ocenaRepository.findById(id).get();
		ocena.setVrednost(nOcena.getVrednost());
		ocena.setTip(nOcena.getTip());
		ocena.setPolugodiste(nOcena.getPolugodiste());
		ocena.setDatumOcenjivanja(LocalDate.now());

		ocenaRepository.save(ocena);

		LOGGER.info("Izmenjana ocena na osnovu ID: " + id);

		return new ResponseEntity<>(ocena, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_NASTAVNIK" })
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deletePredavanje(@PathVariable Integer id) {
		try {
			OcenaEntity ocena = ocenaRepository.findById(id).get();
			ocenaRepository.delete(ocena);
			LOGGER.info("Obrisana ocena za ID: " + id);
			return new ResponseEntity<>(ocena, HttpStatus.OK);
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
