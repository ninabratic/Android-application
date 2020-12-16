package Nicka.demo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import helper.Helper;
import model.DTO;

@RestController
public class TestController {

	@Autowired
	private JavaMailSender javaMailSender;

	@RequestMapping(value = "/image", method = RequestMethod.GET, produces = "image/jpg")
	public ResponseEntity<String> getImage() {

		String slika = Helper.fromFileToBase64("src/main/resources/static/test.jpg");
//		String slika1 = Helper.fromFileToBase64("src/main/resources/static/test.jpg");
		Map<String, String> map = new HashMap<>();
		int i = 0;
		for (i = 0; i < 4; i++) {

			map.put("tekst" + i, "Film " + i);
			System.out.println("tekst" + i);
			map.put("slika" + i, slika);
			map.put("cena" + i, (i + 1) * 100 + "");
		}
		map.put("ukupno", "" + i);

		String jsonResult = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		jsonResult.toString();

		return new ResponseEntity<>(jsonResult, HttpStatus.OK);
	}

	@PostMapping(value = "/kupi", consumes = "application/json")
	public ResponseEntity<String> kupovina(@RequestBody String json)
			throws JsonMappingException, JsonProcessingException {
		System.out.println("USAO");
		ObjectMapper mapper = new ObjectMapper();
		DTO k = mapper.readValue(json, DTO.class);
		double ukupno = k.getUkupno();
		String email = k.getEmail();
		String filmovi = k.getFilmovi();
		String film[] = filmovi.split(",");
		ArrayList<String> movies = new ArrayList<String>();
		for (String movie : film) {
			System.out.println(movie);
			movies.add(movie);
		}

		System.out.println("Sending Email...");

		try {

			sendEmail(email, ukupno, movies);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonResult = "";
		Map<String, String> map = new HashMap<>();
		map.put("response", "uspesno");

		System.out.println("Done");
		try {
			jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(jsonResult, HttpStatus.OK);

	}

	@Async
	void sendEmail(String email, double ukupno, ArrayList<String> movies ) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject("Uspesna kupovina filmova");
		String message = "";
		for (int i = 0; i < movies.size(); i++) {
			message += "Uspesno ste kupili film: " + movies.get(i) + "\n";
		}
		
		message +="Ukupna cena je: " + ukupno;
		msg.setText(message);

		javaMailSender.send(msg);

	}

}
