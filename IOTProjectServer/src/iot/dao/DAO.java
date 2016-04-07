package iot.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import iot.models.DeviceModel;
import iot.models.ResponseModel;
import iot.models.StateModel;

public class DAO {
	// TODO: Connection pooling

	// Array of device types that publish information to the server, 
	// used in MqttServerReceive to record the data sent. 
	public String pubDevices[] = {"Thermostat", "test6"};

	/**
	 * @return - Returns a List of connected publishing DeviceObject's info stored in the database
	 */
	public List<DeviceModel> getPubDeviceInfo(){
		List<DeviceModel> DeviceList = null;
		List<Criteria> orCriteriaList = new ArrayList<Criteria>();
		for (String pubs: pubDevices) {
			Criteria c1 = Criteria.where("type").is(pubs).and("connected").is(true);
			orCriteriaList.add(c1);
		}	
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			Query findPubDevice;

			findPubDevice = new Query(new Criteria().orOperator(orCriteriaList.toArray(new Criteria[orCriteriaList.size()])));
			System.out.println(findPubDevice.toString());

			DeviceList = mongoOperation.find(findPubDevice, DeviceModel.class);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return DeviceList;
	}

	/**
	 * @return - Returns a List of all DeviceObject info stored in the database
	 */
	public List<DeviceModel> getAllDeviceInfo(){
		List<DeviceModel> DeviceList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			// Get all
			DeviceList = mongoOperation.findAll(DeviceModel.class);
			System.out.println("Number of entries"+ DeviceList.size());

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return DeviceList;
	}

	/**
	 * @param deviceID
	 * @return
	 */
	public DeviceModel getDeviceInfo(String deviceID){

		DeviceModel device = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchDeviceID = new Query(Criteria.where("deviceID").is(deviceID));

			// find the device
			device = mongoOperation.findOne(searchDeviceID, DeviceModel.class);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return device;
	}

	/**
	 * @param device
	 * @return
	 */
	@SuppressWarnings("finally")
	public ResponseModel registerDevice(DeviceModel device){
		// Save registered device could do with better error 
		// checking need to look into how to get more info back from mongo
		System.out.println(device);
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		ResponseModel responce = new ResponseModel(false, "Unknown error");
		try {
			MongoTemplate mongoOperation = (MongoTemplate) ctx.getBean("mongoTemplate");
			mongoOperation.setWriteConcern(WriteConcern.ACKNOWLEDGED);
			WriteResultChecking resultChecking = null;
			mongoOperation.setWriteResultChecking(resultChecking);
			// save
			mongoOperation.save(device);

			// DEBUG: show number of entries
			List<DeviceModel> DeviceList = mongoOperation.findAll(DeviceModel.class);
			System.out.println("Number of entries: "+ DeviceList.size());

			// No errors thrown so write was a success 
			responce.setSucsess(true);
			responce.setMessage("Device successfully registered");


		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
			return responce;
		}
	}

	/**
	 * @param device
	 * @return
	 */
	@SuppressWarnings("finally")
	public ResponseModel removeDevice(String deviceID){
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		ResponseModel responce = new ResponseModel(false, "Unknown error");
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query findDevice = new Query(Criteria.where("deviceID").is(deviceID));
			DeviceModel deviceToRemove = mongoOperation.findOne(findDevice, DeviceModel.class);

			System.out.println("\n\nDevice To Remove: "+deviceToRemove+"\n\n");

			// Remove from db
			mongoOperation.remove(deviceToRemove);

			// No errors thrown so write was a success 
			responce.setSucsess(true);
			responce.setMessage("Device successfully removed");

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
			return responce;
		}
	}

	/**
	 * @param state
	 * @return
	 */
	@SuppressWarnings("finally")
	public ResponseModel updateState(StateModel state){
		System.out.println(state);
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		ResponseModel responce = new ResponseModel(false, "Unknown error");
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			// save
			mongoOperation.save(state);


			// DEBUG: show number of entries
			List<StateModel> DeviceList = mongoOperation.findAll(StateModel.class);
			System.out.println("Number of entries"+ DeviceList.size());

			// No errors thrown so write was a success 
			responce.setSucsess(true);
			responce.setMessage("State successfully updated");

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
			return responce;
		}	
	}

	/**
	 * @param deviceID
	 * @return
	 */
	public List<StateModel> getStateInfo(String deviceID){
		List<StateModel> stateList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchState = new Query(Criteria.where("deviceID").is(deviceID));
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));

			// find the device
			stateList = mongoOperation.find(searchState, StateModel.class);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return stateList;
	}

	/**
	 * @return
	 */
	public List<StateModel> getAllStateInfo(){
		List<StateModel> stateList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchState = new Query();
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));

			// find the device
			stateList = mongoOperation.find(searchState, StateModel.class);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return stateList;
	}

	/**
	 * @param deviceID
	 * @return
	 */
	public StateModel getLastStateInfo(String deviceID){

		StateModel state = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchState = new Query(Criteria.where("deviceID").is(deviceID));
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));

			// find the device
			state = mongoOperation.findOne(searchState, StateModel.class);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return state;
	}

	/**
	 * @param deviceID
	 * @return
	 */
	public List<StateModel> getAllLastStateInfo(){
		// FIXME: need to find a way to use "distinct" 
		List<StateModel> stateList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoTemplate mongoOperation = (MongoTemplate) ctx.getBean("mongoTemplate");


			//BasicDBObject dbObj = new BasicDBObject();


			Query searchState = new Query();
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));
			//DBCollection bdCol = mongoOperation.createCollection(StateObject.class);
			//System.out.println("\n\nHERE:\n"+mongoOperation.getCollection("Device").distinct("deviceID")+"\n\n");
			//stateList =  mongoOperation.createCollection(StateObject.class).distinct("deviceID");


			//			String json = "[{ \"$sort\": { \"deviceID\": 1, \"dateStored\": 1 } }, { \"$group\": { \"_id\": \"$deviceID\", \"lastSalesDate\": { \"$last\": \"$dateStored\" } } } ]";
			//			BasicDBList pipeline = (BasicDBList)com.mongodb.util.JSON.parse(json);
			//		    BasicDBObject aggregation = new BasicDBObject("aggregate",StateObject.class)
			//		            .append("pipeline",pipeline);
			//		    System.out.println(aggregation);
			//
			//		    CommandResult commandResult = mongoOperation.executeCommand(aggregation);
			//			System.out.println("\n\nHERE:\n"+commandResult);
			//AggregationOperation match = Aggregation.match(searchState);
			//			AggregationOperation group = Aggregation.group("deviceID");
			//			AggregationOperation sort = Aggregation.sort(Direction.DESC, "dateStored");
			//			AggregationOperation project = Aggregation.project("dateStored");
			//			AggregationOperation project2 = Aggregation.project("deviceID");
			//Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("deviceID"),group,sort);
			//			Aggregation aggregation = Aggregation.newAggregation(StateObject.class, project, project2, group, sort);
			//			AggregationResults<StateObject> result = mongoOperation.aggregate(aggregation, "eft_transactions", StateObject.class);
			//			System.out.println("\n\nHERE:\n"+result);
			//mongoOperation.getCollection("Device").aggregate((List<DBObject>) aggregation);
			//AggregationResults<StateObject> groupResults = mongoOperation.aggregate(aggregation, StateObject.class, StateObject.class);
			//System.out.println("\n\nHERE:\n"+groupResults.getMappedResults());

			// find the device
			stateList = mongoOperation.find(searchState, StateModel.class);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}

		return stateList;
	}

}
