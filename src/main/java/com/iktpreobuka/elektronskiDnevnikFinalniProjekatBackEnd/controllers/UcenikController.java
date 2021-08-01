package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
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
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.RoditeljEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UlogaEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.UcenikDTO;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.KorisnikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.RoditeljRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UcenikRepository;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories.UlogaRepository;

@RestController
@RequestMapping(path = "/ucenik")
public class UcenikController {

	@Autowired
	private KorisnikRepository korisnikRepository;
	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private UlogaRepository ulogaRepository;
	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private OdeljenjeRepository odeljenjeRepository;

	// Vrati sve
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(ucenikRepository.findAll(), HttpStatus.OK);
	}

	// Vrati na osnovu id-a
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getUcenikByID(@PathVariable Integer id) {
		try {
			KorisnikEntity korisnik = korisnikRepository.findById(id).get();
			if (korisnikRepository.existsById(id)) {
				return new ResponseEntity<KorisnikEntity>(korisnik, HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Ucenik nije nadjen"), HttpStatus.NOT_FOUND);
		} catch (Exception e) { // u slucaju izuzetka vratiti 500
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Dodaj novog sa ulogom
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/dodaj")
	public ResponseEntity<?> dodajNovogUcenika(@RequestBody UcenikDTO noviucenik, @RequestParam Integer ulogaId) {
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		UcenikEntity ucenik = new UcenikEntity();
		ucenik.setUloga(uloga);
		ucenik.setIme(noviucenik.getIme());
		ucenik.setPrezime(noviucenik.getPrezime());
		ucenik.setKorisnickoIme(noviucenik.getKorisnickoIme());
		ucenik.setSifra(noviucenik.getSifra());
		ucenik.setJMBG(noviucenik.getJMBG());
		ucenikRepository.save(ucenik);

		return new ResponseEntity<>(ucenik, HttpStatus.OK);
	}

	// Dodaj novog sa ulogom, odeljenjem i roditelja
	@RequestMapping(method = RequestMethod.POST, path = "/dodajSve")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> dodajNovogUcenikaSve(@RequestBody UcenikDTO noviucenik, @RequestParam Integer ulogaId,
			@RequestParam Integer odeljenjeId, @RequestParam Integer roditeljId) {
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		RoditeljEntity roditelj = roditeljRepository.findById(roditeljId).get();
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(odeljenjeId).get();
		UcenikEntity ucenik = new UcenikEntity();
		ucenik.setUloga(uloga);
		ucenik.setPohadjaRazred(odeljenje);
		ucenik.setRoditelj(roditelj);
		ucenik.setIme(noviucenik.getIme());
		ucenik.setPrezime(noviucenik.getPrezime());
		ucenik.setKorisnickoIme(noviucenik.getKorisnickoIme());
		ucenik.setSifra(noviucenik.getSifra());
		ucenik.setJMBG(noviucenik.getJMBG());
		ucenikRepository.save(ucenik);

		return new ResponseEntity<>(ucenik, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/izmeni/{ucenikId}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> izmeniUcenika(@PathVariable Integer ucenikId, @RequestBody UcenikDTO noviucenik,
			@RequestParam Integer ulogaId, @RequestParam Integer odeljenjeId, @RequestParam Integer roditeljId) {
		UlogaEntity uloga = ulogaRepository.findById(ulogaId).get();
		RoditeljEntity roditelj = roditeljRepository.findById(roditeljId).get();
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(odeljenjeId).get();
		UcenikEntity ucenik = ucenikRepository.findById(ucenikId).get();
		ucenik.setUloga(uloga);
		ucenik.setPohadjaRazred(odeljenje);
		ucenik.setRoditelj(roditelj);
		ucenik.setIme(noviucenik.getIme());
		ucenik.setPrezime(noviucenik.getPrezime());
		ucenik.setKorisnickoIme(noviucenik.getKorisnickoIme());
		ucenik.setSifra(noviucenik.getSifra());
		ucenik.setJMBG(noviucenik.getJMBG());
		ucenikRepository.save(ucenik);

		return new ResponseEntity<>(ucenik, HttpStatus.OK);
	}

	// Obrisi
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> obrisiUcenika(@PathVariable Integer id) {
		try {
			UcenikEntity ucenik = ucenikRepository.findById(id).get();
			ucenikRepository.delete(ucenik);
			return new ResponseEntity<>(ucenik, HttpStatus.OK);
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
