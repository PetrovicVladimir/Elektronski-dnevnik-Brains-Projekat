package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.validation.Valid;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.KorisnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.RoditeljEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.UcenikEntity;

public interface UcenikRepository extends CrudRepository<UcenikEntity, Integer> {

	void save(@Valid KorisnikEntity newUser);

	List<UcenikEntity> findAllByRoditelj(RoditeljEntity roditelj);

}
