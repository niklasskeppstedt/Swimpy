package se.skeppstedt.swimpy.application.parser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.skeppstedt.swimpy.application.PersonalBest;
import se.skeppstedt.swimpy.application.Swimmer;
import se.skeppstedt.swimpy.application.enumerations.Event;

public class OctoParser {

	private String url;
	private Document document;

	public Swimmer parseSwimmer(Document document, String octoId) {
		this.document = document;
        Swimmer swimmer = null;
		String name = extractSwimmerName();
		String dateOfBirth = extractSwimmerYearOfBirth();
		String swimmingClub = extractSwimmerClub();
	
		swimmer = new Swimmer(name, dateOfBirth, octoId , swimmingClub);
		extractPersonalBests(swimmer);
		return swimmer;
	}
	
	public Swimmer updateSwimmer(Swimmer swimmer) {
		try {
			document = Jsoup.parse(new URL(url + swimmer.octoId), 6000);
		} catch (IOException e) {
			Log.e("OctoParser", "Could not parse swimmer url" + url, e);
			return null;
		}
		extractPersonalBests(swimmer);
		return swimmer;
	}
	
	public Set<Swimmer> parseSearchResult() {
		try {
			document = Jsoup.parse(new URL(url), 3000);
		} catch (IOException e) {
			System.err.println("Could not parse swimmer url");
		}
		Set<Swimmer> swimmers = new HashSet<>();
		Elements odd = document.select("tr.odd");
		odd.addAll(document.select("tr.even"));
		for (Element element : odd) {
			String firstName = element.select("td.name-column").first().ownText();
			String lastName = element.select("td").get(1).ownText();
			String yearOfBirth = element.select("td").get(3).ownText();
			String club = element.select("td").get(4).ownText();
			String octoId = extractLinkParameter(element, "id");
			
			boolean added = swimmers.add(new Swimmer(firstName + " " + lastName, yearOfBirth, octoId , club));
			if(!added) {
				System.err.println("Could not add swimmer, already in set");
			}
		}
		return swimmers;
	}

	private void extractPersonalBests(Swimmer swimmer) {
		Elements odds = document.select("tr.odd");
		Elements evens = document.select("tr.even");
		odds.addAll(evens);
        Log.d("OctoParser","Extracting personal bests");
        for (Element element : odds) {
			String competition = extractCompetition(element);
			String date = extractDate(element);
			Duration time = extractTime(element);
			Event event = Event.fromCode(extractLinkParameter(element, "event"));
			PersonalBest personalBest = new PersonalBest(event, time, competition, swimmer);
			swimmer.personalBests.add(personalBest);
		}
        Log.d("OctoParser","Extracted " + swimmer.personalBests.size() + " personal bests");
	}

	private String extractLinkParameter(Element element, String parameterName) {
		String[] link = element.select("a").attr("href").split(parameterName +"=");
		return link[1];
	}

	private Duration extractTime(Element element) {
		String timeString = element.select("td").get(3).ownText().replace('.', ':').trim();
		String time = extractFromTextWithPattern(timeString, "\\d{2}:\\d{2}:\\d{2}");
		String[] parts = time.split(":");
        String minutes = parts[0];
        String seconds = parts[1];
        String millis = parts[2];
        Long minutesValue = Long.valueOf(minutes) * 60000;
        Long secondsValue = Long.valueOf(seconds) * 1000;
        Long millisValue = Long.valueOf(millis) * 10;

        return new Duration(Long.valueOf(minutesValue + secondsValue + millisValue));
	}

	private String extractDate(Element element) {
		return element.select("td").get(2).ownText().trim();
	}

	private String extractCompetition(Element element) {
		return element.select("td").get(1).ownText().trim();
	}

	private String extractSwimmerYearOfBirth() {
		String yearOfBirth = null;
		String bornText = null;
		String searchText = null;
		try {
			searchText = new String("dd".getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("OctoParser", "Encoding error, shouldnt happen", e);
			return "";
		}
		assert searchText != null;
		bornText = document.getElementsContainingOwnText(searchText).text();
		yearOfBirth = extractFromTextWithPattern(bornText, "\\d{4}");
		return yearOfBirth;
	}

	private String extractSwimmerClub() {
		final String searchText;
		try {
			searchText = new String("rening:".getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("OctoParser", "Encoding error, shouldnt happen", e);
			return null;
		}
		String swimmingClub = document.getElementsContainingOwnText(searchText).text();
		swimmingClub = swimmingClub.substring(swimmingClub.indexOf(searchText) + searchText.length(), swimmingClub.indexOf("Licens")).trim();
		return swimmingClub;
	}

	private String extractFromTextWithPattern(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if(matcher.find()) {
            return matcher.group();
        }
		return "Not found";
		
	}

	private String extractSwimmerName() {
		String name;
		name = document.select("h2").text();
		return name;
	}
}