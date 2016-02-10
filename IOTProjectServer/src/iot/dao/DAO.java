package iot.dao;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.MongoException;
import com.mongodb.ReadPreference;

import iot.mvc.DeviceObject;
import iot.mvc.StateObject;

public class DAO {
	// TODO: Connection pooling
	// TODO: Look at how to return info from DB transactions
	// FIXME: Null pointer when no deviceID is found

	/**
	 * @return - Returns a List of all DeviceObject info stored in the database
	 */
	public List<DeviceObject> getAllDeviceInfo(){
		// TODO: Need to think what to return; all of the information, 
		// or combine with last known status
		List<DeviceObject> DeviceList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			// Get all
			DeviceList = mongoOperation.findAll(DeviceObject.class);
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
	public DeviceObject getDeviceInfo(String deviceID){

		DeviceObject device = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchDeviceID = new Query(Criteria.where("deviceID").is(deviceID));

			// find the device
			device = mongoOperation.findOne(searchDeviceID, DeviceObject.class);

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
	public int registerDevice(DeviceObject device){
		// Save registered device could do with better error 
		// checking need to look into how to get more info back from mongo
		System.out.println(device);
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			// save
			mongoOperation.save(device);

			// DEBUG: show number of entries
			List<DeviceObject> DeviceList = mongoOperation.findAll(DeviceObject.class);
			System.out.println("Number of entries: "+ DeviceList.size());

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}
		// TODO: Need to re-think what to return and if we can get a status from mongo
		return 0;
	}
	
	/**
	 * @param device
	 * @return
	 */
	public int removeDevice(String deviceID){
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query findDevice = new Query(Criteria.where("deviceID").is(deviceID));
			DeviceObject deviceToRemove = mongoOperation.findOne(findDevice, DeviceObject.class);
			
			System.out.println("\n\nDevice To Remove: "+deviceToRemove+"\n\n");
			
			// Remove from db
			mongoOperation.remove(deviceToRemove);

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}
		// TODO: Need to re-think what to return and if we can get a status from mongo
		return 0;
	}

	/**
	 * @param state
	 * @return
	 */
	public int updateState(StateObject state){
		System.out.println(state);
		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
			// save
			mongoOperation.save(state);


			// DEBUG: show number of entries
			List<StateObject> DeviceList = mongoOperation.findAll(StateObject.class);
			System.out.println("Number of entries"+ DeviceList.size());

		} catch (BeansException e) {
			e.printStackTrace();
		} catch (MongoException e){
			e.printStackTrace();
		} finally {
			// Close mongo operation
			((ConfigurableApplicationContext)ctx).close();
		}
		// TODO: Need to re-think what to return and if we can get a status from mongo
		return 0;		
	}

	/**
	 * @param deviceID
	 * @return
	 */
	public List<StateObject> getStateInfo(String deviceID){
		List<StateObject> stateList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchState = new Query(Criteria.where("deviceID").is(deviceID));
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));

			// find the device
			stateList = mongoOperation.find(searchState, StateObject.class);

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
	public List<StateObject> getAllStateInfo(){
		List<StateObject> stateList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchState = new Query();
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));

			// find the device
			stateList = mongoOperation.find(searchState, StateObject.class);

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
	public StateObject getLastStateInfo(String deviceID){

		StateObject state = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

			Query searchState = new Query(Criteria.where("deviceID").is(deviceID));
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));

			// find the device
			state = mongoOperation.findOne(searchState, StateObject.class);

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
	public List<StateObject> getAllLastStateInfo(){
		// FIXME: need to find a way to use "distinct" 
		List<StateObject> stateList = null;

		ApplicationContext ctx = 
				new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		try {
			MongoTemplate mongoOperation = (MongoTemplate) ctx.getBean("mongoTemplate");

			Query searchState = new Query();
			searchState.with(new Sort(new Order(Direction.DESC,"dateStored")));
			
			//stateList =  mongoOperation.createCollection(StateObject.class).distinct("deviceID", searchState);

//			AggregationOperation match = Aggregation.match(searchState);
//			AggregationOperation group = Aggregation.group("deviceID");
//			AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "dateStored");
//			Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("deviceID"),group,sort);
//
//			AggregationResults<StateObject> groupResults = mongoOperation.aggregate(aggregation, StateObject.class, StateObject.class);
//			stateList = groupResults.getMappedResults();

			// find the device
			stateList = mongoOperation.find(searchState, StateObject.class);

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
