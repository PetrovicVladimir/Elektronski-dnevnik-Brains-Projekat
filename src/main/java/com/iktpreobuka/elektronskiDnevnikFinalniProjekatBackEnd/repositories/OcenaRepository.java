package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.DnevnikEntity;
import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.OcenaEntity;

public interface OcenaRepository extends CrudRepository<OcenaEntity, Integer> {

	
}
