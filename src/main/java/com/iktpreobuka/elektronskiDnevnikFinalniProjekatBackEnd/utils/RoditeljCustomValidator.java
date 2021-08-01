//package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.utils;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.entities.dto.RoditeljDTO;
//
//@Component
//public class RoditeljCustomValidator implements Validator {
//	@Override
//	public boolean supports(Class<?> myClass) {
//		return RoditeljDTO.class.equals(myClass);
//	}
//
//
//	@Override
//	public void validate(Object target, Errors errors) {
//		RoditeljDTO roditelj = (RoditeljDTO) target;
//		
//		if (!roditelj.getSifra().equals(roditelj.getPotvrdaSifre())) {
//			errors.reject("400", "Sifre i potvda sifre moraju biti isti.");
//		}
//		
//	}
//}