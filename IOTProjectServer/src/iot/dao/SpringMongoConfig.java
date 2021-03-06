package iot.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.stereotype.Service;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * Class that stores database information
 *
 */
@Configuration
@Service("SpringMongoConfig")
public class SpringMongoConfig extends AbstractMongoConfiguration {

	@Override
	public String getDatabaseName() {
		return "DeviceDB";
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		// Can change to localhost when on server
		return new MongoClient("52.88.194.67");
	}

}