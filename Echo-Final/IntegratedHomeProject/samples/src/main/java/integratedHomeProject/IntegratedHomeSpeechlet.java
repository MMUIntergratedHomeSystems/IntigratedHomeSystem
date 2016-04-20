package integratedHomeProject;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;

/**
 * 
 * @author KarimHalani
 * @version 1.0
 */
@SuppressWarnings({ "deprecation", "resource" })
public class IntegratedHomeSpeechlet implements Speechlet {
	/*
	 * Declaration of global variables
	 */
	private static final Logger log = LoggerFactory
			.getLogger(IntegratedHomeSpeechlet.class);
	private static final String URL_PREFIX = "http://52.88.194.67:8080/IOTProjectServer/";
	private static String options = "These are the things Integrated Home can do. "
			+ "Check what devices are turned on. "
			+ "Turn on a device, "
			+ "turn off a device. "
			+ "To check what devices are on say, retrieve. "
			+ "To turn off or turn on a device, say turn off or turn on followed by the device name. "
			+ "For example, to turn off living room light, say turn off living room light";
	private static final String DEVICE_KEY = "DEVICE";
	private static final String DEVICE_SLOT = "DEVICE";

	/*
	 * Function called when new session begins.
	 */
	@Override
	public void onSessionStarted(final SessionStartedRequest request,
			final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}",
				request.getRequestId(), session.getSessionId());
	}

	/*
	 * Function called on launch of the Integrated Home skill from the Echo.
	 */
	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request,
			final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		return getWelcomeResponse();
	}

	/*
	 * Function called when new intent is set.
	 */
	@Override
	public SpeechletResponse onIntent(final IntentRequest request,
			final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		// Get intent from the request object.
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		// Note: If the session is started with an intent, no welcome message
		// will be rendered;
		// rather, the intent specific response will be returned.
		if ("GetState".equals(intentName)) {
			String speechOutput = GetState();
			String repromptText = "Is there anything else I can do for you.";

			return getSpeechletResponse(speechOutput, repromptText, true);
		} else if ("GetHistory".equals(intentName)) {
			String speechOutput = GetHistory();
			String repromptText = "Do you want more";
			return getSpeechletResponse(speechOutput, repromptText, true);
		} else if ("MyDEVICEIsIntent".equals(intentName)) {
			return setDEVICEInSession(intent, session);
		} else if ("WhatsMyDEVICEIntent".equals(intentName)) {
			return changeState(intent, session);
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			// Create the plain text output.
			String speechOutput = options;

			String repromptText = "What do you want to do now? " + options;

			return getSpeechletResponse(speechOutput, repromptText, false);
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

	/*
	 * Function called to end session.
	 */
	@Override
	public void onSessionEnded(final SessionEndedRequest request,
			final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}",
				request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a SpeechletResponse with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual welcome message
	 */
	private SpeechletResponse getWelcomeResponse() {
		// Create the welcome message.
		String speechOutput = "Welcome to Integrated Home," + options;
		// If the user either does not reply to the welcome message
		// or says something that is not
		// understood, they will be prompted again with this text.
		String repromptText = "Please choose one of the following options, "
				+ options;
		return getSpeechletResponse(speechOutput, repromptText, true);
	}

	/**
	 * Creates a SpeechletResponse for the intent and stores the extracted
	 * DEVICE in the Session.
	 *
	 * @param intent
	 *            intent for the request
	 * @return SpeechletResponse spoken and visual response the given intent
	 */
	private SpeechletResponse setDEVICEInSession(final Intent intent,
			final Session session) {
		// Get the slots from the intent.
		Map<String, Slot> slots = intent.getSlots();

		// Get the DEVICE slot from the list of slots.
		Slot favoriteDEVICESlot = slots.get(DEVICE_SLOT);
		String speechText, repromptText;

		// Check for DEVICE and create output to user.
		if (favoriteDEVICESlot != null) {
			// Store the user's favorite DEVICE in the Session and create
			// response.
			String favoriteDEVICE = favoriteDEVICESlot.getValue();
			session.setAttribute(DEVICE_KEY, favoriteDEVICE);
			speechText = String
					.format("I will change the state for %s. To confirm change say YES.",
							favoriteDEVICE);
			repromptText = "To confirm change say YES.";
		} else {
			// Render an error since we don't know what the users
			// DEVICE is.
			speechText = options;
			repromptText = options;
		}
		return getSpeechletResponse(speechText, repromptText, true);
	}

	/**
	 * Creates a SpeechletResponse for the intent and get the user's DEVICE from
	 * the Session.
	 *
	 * @param intent
	 *            intent for the request
	 * @return SpeechletResponse spoken and visual response for the intent
	 */
	private SpeechletResponse changeState(final Intent intent,
			final Session session) {
		String speechText;
		boolean isAskResponse = false;
		String deviceID = null, state = null, State = null;
		// Get the user's DEVICE from the session.
		String favoriteDEVICE = (String) session.getAttribute(DEVICE_KEY);

		// Check to make sure user's DEVICE is set in the session.
		if (StringUtils.isNotEmpty(favoriteDEVICE)) {
			State = changeState(deviceID, state, favoriteDEVICE);
			speechText = String.format("%s is now %s. Do you want me to do any thing else for you?",
					favoriteDEVICE, State);
			isAskResponse = true;
		} else {
			// Since the user's DEVICE is not set render an error
			// message.
			speechText = "I'm not sure what you asked me to do. Please choose one of the following options. "
					+ options;
			isAskResponse = true;
		}

		return getSpeechletResponse(speechText, speechText, isAskResponse);
	}

	private String changeState(String deviceID, String state,
			String favoriteDEVICE) {
		String State = null;
		// URL for getting all details
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
		String type =null;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				if (favoriteDEVICE.equalsIgnoreCase(jsonArray.getJSONObject(i)
						.getString("name"))) {
					deviceID = jsonArray.getJSONObject(i).getString("deviceID");
					state = jsonArray.getJSONObject(i)
							.getString("currentState");
					 type = jsonArray.getJSONObject(i)
							.getString("type");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			if (type.equals("Lock")) {
				State = "Locked";
			} else if (!type.equals("Lock")){
				State = "ON";
		}
		try {
			// Default HttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL_PREFIX + "setState?deviceID="
					+ deviceID + "&state=" + state);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
		} catch (ClientProtocolException e) {
			// Signals error in http protocol
			e.printStackTrace();
			// Log Errors Here
		} catch (IOException e) {
			e.printStackTrace();
		}
		return State;
	}

	/**
	 * Returns a Speechlet response for a speech and reprompt text.
	 */
	private SpeechletResponse getSpeechletResponse(String speechText,
			String repromptText, boolean isAskResponse) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Integrated Home");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		if (isAskResponse) {
			// Create reprompt
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(repromptText);
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);

			return SpeechletResponse.newAskResponse(speech, reprompt, card);

		} else {
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}

	public String GetState() {
		// URL for getting all details
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
		String name = null, type = null, result = "", State = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			try {

				if (jsonArray.getJSONObject(i).getString("currentState")
						.equals("1")
						|| !jsonArray.getJSONObject(i).getString("currentState").equals("0")) {
					name = jsonArray.getJSONObject(i).getString("name");
					type = jsonArray.getJSONObject(i).getString("type");
					if (jsonArray.getJSONObject(i).getString("type")
							.equals("Lock")) {
						State = "Locked";
					} else if (jsonArray.getJSONObject(i).getString("type")
							.equals("Thermostat")) {
						State = "at, "
								+ jsonArray.getJSONObject(i).getString(
										"currentState") + " degrees";
					} else {
						State = "ON";
					}
					result = result
							+ (name + ", is a " + type + " and it is currently "
									+ State + ". ");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public String GetHistory() {
		// URL for getting all details
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
		String name = null, state = null, when = null, result = "";
		for (int i = 0; i < jsonArray.length(); i++) {
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
			try {
				result = result
						+ (name + ", is " + state + ". It was used on " + when + ". ");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
