package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.NastavnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredavanjeEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.PredmetEntity;

public interface PredavanjeRepository extends CrudRepository<PredavanjeEntity, Integer> {

	List<PredavanjeEntity> findAllByNastavnik(NastavnikEntity nastavnik);

	List<PredavanjeEntity> findAllByPredmet(PredmetEntity predmet);


}
