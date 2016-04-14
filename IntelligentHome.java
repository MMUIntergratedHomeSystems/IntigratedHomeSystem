package intelligentHome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

@SuppressWarnings({ "unused", "deprecation", "resource" })
public class IntelligentHome implements Speechlet {
	private static final Logger log = LoggerFactory
			.getLogger(IntelligentHome.class);

	/**
	 * URL prefix to download history content from Eds Server.
	 */
	private static final String URL_PREFIX = "http://52.88.194.67:8080/IOTProjectServer/";

	@Override
	public void onSessionStarted(final SessionStartedRequest request,
			final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}",
				request.getRequestId(), session.getSessionId());

		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request,
			final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request,
			final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = intent.getName();

		if ("GetState".equals(intentName)) {
			String speechOutput = GetState();
			String speechoutput = speechOutput.toString();
			String repromptText = "Do you want more";

			return newAskResponse(speechOutput, false, repromptText, false);	
		}
		if ("GetHistory".equals(intentName)) {
			String speechOutput = GetHistory();
			String speechoutput = speechOutput.toString();
			String repromptText = "Do you want more";

			return newAskResponse(speechoutput, false, repromptText, false);
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			// Create the plain text output.
			String speechOutput = "With Intelligent Home, you can get"
					+ " device Status for all your devices"
					+ " For example, you could say retrive,"
					+ " Or say the device name. Now, what do you want to do?";

			String repromptText = "What do you want to do now?";

			return newAskResponse(speechOutput, false, repromptText, false);
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else if ("AMAZON.CancelIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			throw new SpeechletException("Invalid Intent");
		}
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request,
			final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}",
				request.getRequestId(), session.getSessionId());

		// any session cleanup logic would go here
	}

	/**
	 * Function to handle the onLaunch skill behavior.
	 * 
	 * @return SpeechletResponse object with voice/card response to return to
	 *         the user
	 */
	private SpeechletResponse getWelcomeResponse() {
		String options ="To retrieve device status say, retrieve. "
				+"To get history of all device states, say history";
		String speechOutput = "Welcome to Intelligent Home,"+options;
		// If the user either does not reply to the welcome message
		// or says something that is not
		// understood, they will be prompted again with this text.
		String repromptText = "Please choose one of the following options, "+options;

		return newAskResponse(speechOutput, false, repromptText, false);
	}

	public String GetState() {
		// URL for getting all customers
		// Get HttpResponse Object from url.
		// Get HttpEntity from Http Response Object

		HttpEntity httpEntity = null;

		try {
			// Default HttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL_PREFIX + "getDevice");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
		} catch (ClientProtocolException e) {
			// Signals error in http protocol
			e.printStackTrace();
			// Log Errors Here
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert HttpEntity into JSON Array
		JSONArray jsonArray = null;

		if (httpEntity != null) {
			try {
				String entityResponse = EntityUtils.toString(httpEntity);
				jsonArray = new JSONArray(entityResponse);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String name = null,type = null, state = null, result = ""; 
		for(int i = 0; i < jsonArray.length(); i++){
		try {
			name = jsonArray.getJSONObject(i).getString("name");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			type = jsonArray.getJSONObject(i).getString("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			state = jsonArray.getJSONObject(i).getString("currentState");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			result =result+(name +", is a "+ type+" and it is currently "+state+". ");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return result;
	}

	
	public String GetHistory() {
		// URL for getting all customers
		// Get HttpResponse Object from url.
		// Get HttpEntity from Http Response Object

		HttpEntity httpEntity = null;

		try {
			// Default HttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL_PREFIX + "getState");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
		} catch (ClientProtocolException e) {
			// Signals error in http protocol
			e.printStackTrace();
			// Log Errors Here
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert HttpEntity into JSON Array
		JSONArray jsonArray = null;

		if (httpEntity != null) {
			try {
				String entityResponse = EntityUtils.toString(httpEntity);
				jsonArray = new JSONArray(entityResponse);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String name = null,state = null, when = null, result = "";
		for(int i = 0; i < jsonArray.length(); i++){
		try {
			name = jsonArray.getJSONObject(i).getString("deviceID");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			state = jsonArray.getJSONObject(i).getString("state");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			when = jsonArray.getJSONObject(i).getString("dateStored");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			result =result+(name +", is "+ state+". It was used on " + when+". ");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return result;
	}
	/**
	 * Wrapper for creating the Ask response from the input strings.
	 * 
	 * @param stringOutput
	 *            the output to be spoken
	 * @param isOutputSsml
	 *            whether the output text is of type SSML
	 * @param repromptText
	 *            the reprompt for if the user doesn't reply or is
	 *            misunderstood.
	 * @param isRepromptSsml
	 *            whether the reprompt text is of type SSML
	 * @return SpeechletResponse the speechlet response
	 */
	private SpeechletResponse newAskResponse(String stringOutput,
			boolean isOutputSsml, String repromptText, boolean isRepromptSsml) {
		OutputSpeech outputSpeech, repromptOutputSpeech;
		if (isOutputSsml) {
			outputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
		} else {
			outputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
		}

		if (isRepromptSsml) {
			repromptOutputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
		} else {
			repromptOutputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) repromptOutputSpeech)
					.setText(repromptText);
		}
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}

}
