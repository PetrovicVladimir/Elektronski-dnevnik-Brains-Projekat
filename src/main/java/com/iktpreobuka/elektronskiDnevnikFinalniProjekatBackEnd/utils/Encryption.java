package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {

	
	public static String getPassEncoded(String pass) {
		return new BCryptPasswordEncoder().encode(pass);
	}
	
	public static void main(String[] args) {
		System.out.println(getPassEncoded("pass"));
	}
}
