package com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.services;

import com.iktpreobuka.elektronskiDnevnikFinalniProjekatBackEnd.models.EmailObject;

public interface EmailService {

	void sendSimpleMessage(EmailObject object);
	void sendTemplateMessage(EmailObject object) throws Exception;
	void sendMessageWithAttachment(EmailObject object, String pathToAttachment) throws Exception;
	
}
