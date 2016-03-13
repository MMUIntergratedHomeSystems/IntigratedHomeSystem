package iot.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import iot.models.DeviceModel;

public interface DeviceRepo extends MongoRepository<DeviceModel, String>{//, QueryDslPredicateExecutor<DeviceObject>{
	@Query("{'deviceID' : ?0}")
	List<DeviceModel> findDeviceByID(String deviceID);

	List<DeviceModel> getAll();
	
	// Find by room
	// Find by type

}
