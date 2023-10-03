package com.prodapt.learningspring.service;

import com.prodapt.learningspring.model.EmailDetails;


public interface EmailService {

	String sendSimpleMail(EmailDetails details);
	String sendMailWithAttachment(EmailDetails details);
}
