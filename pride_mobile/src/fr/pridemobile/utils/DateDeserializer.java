package fr.pridemobile.utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Pour gérer les types de dates renvoyés par CXF
 * Attention, non ThreadSafe, utlisation de SimpleDateFormat
 * 
 */
public class DateDeserializer implements JsonDeserializer<Date> {

	/**  */
	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(Constants.DATE_DEFAULT_FORMAT, Locale.FRANCE);
	
	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		
		Date date = null;
		String str = json.getAsJsonPrimitive().getAsString();
		try {
			// DateTime
			long timestamp = Long.parseLong(str);
			date = new Date(timestamp);
		} catch (NumberFormatException e) {
			// Date
			try {
				date = DEFAULT_DATE_FORMAT.parse(str);
			} catch (ParseException e1) {
				throw new RuntimeException("Unable to parse date [" + str + "]", e1);
			}
		}
		
		return date;
	}

}
