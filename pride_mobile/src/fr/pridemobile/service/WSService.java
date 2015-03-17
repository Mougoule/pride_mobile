package fr.pridemobile.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.pridemobile.model.WebappResponse;
import fr.pridemobile.service.common.ServiceException;
import fr.pridemobile.service.common.WSFile;
import fr.pridemobile.utils.Constants;
import fr.pridemobile.utils.DateDeserializer;

/**
 * Service d'appel de WS
 */
public class WSService {
	
	private static final String TAG = "WS";

	/**
	 * Appel GET de WS
	 * 
	 * @param url
	 *            URL
	 * @param clazz
	 *            Classe retour
	 * @param token
	 *            Token
	 * @param androidId
	 *            Android ID
	 * @return Réponse
	 * @throws ServiceException
	 */
	public static <T extends WebappResponse<?>> T get(String url, Class<T> clazz, String token, String androidId)
			throws ServiceException {
		try {
			HttpGet get = new HttpGet(url);
			/*if (token != null) {
				get.setHeader(Constants.TOKEN_HEADER, token);
			}*/
			/*if (androidId != null) {
				get.setHeader(Constants.ANDROID_ID_HEADER, androidId);
			}*/

			return call(get, clazz);
		} catch (ClientProtocolException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Appel POST WS
	 * 
	 * @param url
	 *            URL
	 * @param clazz
	 *            Classe retour
	 * @param parameters
	 *            Paramètres POST
	 * @return Réponse
	 * @throws ServiceException
	 *             Why not
	 */
	public static <T extends WebappResponse<?>> T post(String url, Class<T> clazz, Map<String, Object> parameters, String token,
			String androidId) throws ServiceException {
		HttpEntity entity;
		try {
			if (parameters == null) {
				entity = null;
			} else {
				List<NameValuePair> params = new ArrayList<NameValuePair>(parameters.size());
				for (String key : parameters.keySet()) {
					Object paramValuesObj = parameters.get(key);
					
					if (paramValuesObj instanceof String[]) {
						// Liste de paramètres
						String arrayKey = key + "[]";
						for (String paramValue : (String[]) paramValuesObj) {
							BasicNameValuePair pair = new BasicNameValuePair(arrayKey, paramValue);
							params.add(pair);
						}
					} else if (paramValuesObj instanceof String) {
						// Paramètre unique
						BasicNameValuePair pair = new BasicNameValuePair(key, (String) paramValuesObj);
						params.add(pair);
					} else {
						Log.w(TAG, "A WS parameter has been passed as an Object not a String or String[], toString() will be used, errors can happen : " + url + " (" + paramValuesObj + ")");
						BasicNameValuePair pair = new BasicNameValuePair(key, paramValuesObj == null ? null : paramValuesObj.toString());
						params.add(pair);
					}
				}
				entity = new UrlEncodedFormEntity(params);
			}

			HttpPost post = new HttpPost(url);
			if (token != null) {
				post.setHeader(Constants.TOKEN_HEADER, token);
			}
			post.setEntity(entity);

			return call(post, clazz);

		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		} catch (ClientProtocolException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Appel PUT WS
	 * 
	 * @param url
	 *            URL
	 * @param clazz
	 *            Classe retour
	 * @param parameters
	 *            Paramètres PUT
	 * @return Réponse
	 * @throws ServiceException
	 *             Why not
	 */
	public static <T extends WebappResponse<?>> T put(String url, Class<T> clazz, Map<String, Object> parameters,
			Map<String, WSFile> files, String token, String androidId) throws ServiceException {
		MultipartEntityBuilder entityBuilder;
		
		try {
			if (parameters == null) {
				entityBuilder = null;
			} else {
				entityBuilder = MultipartEntityBuilder.create();
				entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

				// Ajout des paramètres
				if (parameters != null) {
					for (String key : parameters.keySet()) {
						// Nom du paramètre
						Object paramValuesObj = parameters.get(key);
						
						if (paramValuesObj instanceof String[]) {
							// Liste de paramètres
							for (String paramValue : (String[]) paramValuesObj) {
								ContentBody paramCb = new StringBody(paramValue, ContentType.MULTIPART_FORM_DATA);
								entityBuilder.addPart(key + "[]", paramCb);
							}
						} else if (paramValuesObj instanceof String) {
							// Paramètre basique
							ContentBody paramCb = new StringBody((String) paramValuesObj, ContentType.MULTIPART_FORM_DATA);
							entityBuilder.addPart(key, paramCb);
						} else {
							Log.w(TAG, "A WS parameter (" + key + "=" + paramValuesObj + ") has been passed as an Object not a String or String[], toString() will be used, errors can happen");
							ContentBody paramCb = new StringBody(paramValuesObj == null ? null : paramValuesObj.toString(), ContentType.MULTIPART_FORM_DATA);
							entityBuilder.addPart(key, paramCb);
						}
					}
				}

				// Ajout des fichiers attachés
				if (files != null) {
					for (String fileKey : files.keySet()) {
						WSFile wsFile = files.get(fileKey);
						ByteArrayInputStream bais = new ByteArrayInputStream(wsFile.getFileData());
						ContentBody cb = new InputStreamBody(bais, ContentType.MULTIPART_FORM_DATA, wsFile.getFileName());
						entityBuilder.addPart(fileKey, cb);
						// No need to close a ByteArrayInputStream
					}
				}
			}

			HttpPut put = new HttpPut(url);
			/*if (token != null) {
				put.setHeader(Constants.TOKEN_HEADER, token);
			}
			if (androidId != null) {
				put.setHeader(Constants.ANDROID_ID_HEADER, androidId);
			}*/
			put.setEntity(entityBuilder.build());

			return call(put, clazz);

		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		} catch (ClientProtocolException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Appel HTTP
	 * 
	 * @param request
	 *            Request
	 * @param clazz
	 *            Classe retour
	 * @return Réponse
	 * @throws ClientProtocolException
	 *             Why not
	 * @throws IOException
	 *             Why not
	 * @throws ServiceException
	 *             Why not
	 */
	private static <T extends WebappResponse<?>> T call(HttpUriRequest request, Class<T> clazz) throws ClientProtocolException,
			IOException, ServiceException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = httpclient.execute(request);

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (HttpStatus.SC_OK == statusCode || HttpStatus.SC_UNAUTHORIZED == statusCode) {
			InputStream input = httpResponse.getEntity().getContent();
			Reader reader = new InputStreamReader(input);

			try {
				Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();
				T response = gson.fromJson(reader, clazz);
				return response;

			} catch (Exception ex) {
				throw new ServiceException(ex);
			}
		} else {
			throw new ServiceException("Le serveur a renvoyé une erreur : code=[" + statusCode + "], reason=["
					+ httpResponse.getStatusLine().getReasonPhrase() + "]");
		}
	}

}
