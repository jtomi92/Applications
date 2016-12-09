package com.jtech.apps.hcm.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.jtech.apps.hcm.dao.interfaces.ProductRegistrationDAO;
import com.jtech.apps.hcm.dao.mapper.ProductRegistrationMapper;
import com.jtech.apps.hcm.model.RegisteredProduct;
import com.jtech.apps.hcm.util.TimeUtil;

@Repository
public class ProductRegistrationDAOImpl implements ProductRegistrationDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	ProductRegistrationMapper mapper = new ProductRegistrationMapper();
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public int registerProduct(String serial) {

		RegisteredProduct registeredProduct = getRegisteredProductBySerialNumber(serial);
		registeredProduct.setActivated(true);

		return updateRegisteredProduct(registeredProduct);
	}

	@Override
	public int addRegisteredProduct(RegisteredProduct registeredProduct) {

		List<RegisteredProduct> registeredProductList = getRegisteredProducts();

		if (registeredProductList != null && !registeredProductList.isEmpty()) {
			for (int index = 0; index < registeredProductList.size(); index++) {
				if (registeredProductList.get(index).getSerialNumber().equals(registeredProduct.getSerialNumber())) {
					return 0;
				}
			}
		}

		String sql = "INSERT INTO REGISTERED_PRODUCTS (" + "PRODUCT_ID," + "SERIAL_NUMBER," + "IS_ACTIVATED,"
				+ "IS_REGISTERED," + "CREATION_DATE," + "LAST_UPDATE_DATE) " + "VALUES (" + ":PRODUCT_ID,"
				+ ":SERIAL_NUMBER," + ":IS_ACTIVATED," + ":IS_REGISTERED," + ":CREATION_DATE," + ":LAST_UPDATE_DATE)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", registeredProduct.getProductId());
		parameters.put("SERIAL_NUMBER", registeredProduct.getSerialNumber());
		parameters.put("IS_ACTIVATED", registeredProduct.isActivated() ? "Y" : "N");
		parameters.put("IS_REGISTERED", registeredProduct.isRegistered() ? "Y" : "N");
		parameters.put("CREATION_DATE", TimeUtil.getTimeStamp());
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public int updateRegisteredProduct(RegisteredProduct registeredProduct) {

		String sql = "UPDATE REGISTERED_PRODUCTS SET " + "PRODUCT_ID = :PRODUCT_ID, " + "IS_ACTIVATED = :IS_ACTIVATED, "
				+ "IS_REGISTERED = :IS_REGISTERED , " + "LAST_UPDATE_DATE = :LAST_UPDATE_DATE "
				+ "WHERE SERIAL_NUMBER = :SERIAL_NUMBER";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("PRODUCT_ID", registeredProduct.getProductId());
		parameters.put("IS_ACTIVATED", registeredProduct.isActivated() ? "Y" : "N");
		parameters.put("IS_REGISTERED", registeredProduct.isRegistered() ? "Y" : "N");
		parameters.put("LAST_UPDATE_DATE", TimeUtil.getTimeStamp());
		parameters.put("SERIAL_NUMBER", registeredProduct.getSerialNumber());

		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource namedParameters = new MapSqlParameterSource(parameters);
		return namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	@Override
	public RegisteredProduct getRegisteredProductBySerialNumber(String serialNumber) {

		List<RegisteredProduct> registeredProductList = getRegisteredProducts();

		for (int index = 0; index < registeredProductList.size(); index++) {
			if (registeredProductList.get(index).getSerialNumber().equals(serialNumber)) {
				return registeredProductList.get(index);
			}
		}

		return null;
	}

	@Override
	public List<RegisteredProduct> getRegisteredProducts() {

		List<RegisteredProduct> registeredProducts = new LinkedList<RegisteredProduct>();

		String sql = "SELECT * FROM REGISTERED_PRODUCTS";

		List<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();

		rows = jdbcTemplate.queryForList(sql);

		if (rows != null && !rows.isEmpty()) {
			for (Map<String, Object> row : rows) {

				RegisteredProduct registeredProduct = new RegisteredProduct();
				registeredProduct = mapper.mapRegisteredProduct(row);

				registeredProducts.add(registeredProduct);
			}
		}

		return registeredProducts;
	}

	@Override
	public RegisteredProduct getTestData() {

		RegisteredProduct registeredProduct = new RegisteredProduct();
		registeredProduct.setSerialNumber("test serial number");
		registeredProduct.setProductId(1);
		registeredProduct.setActivated(false);
		registeredProduct.setRegistered(true);
		registeredProduct.setCreationDate(TimeUtil.getTimeStamp());
		registeredProduct.setLastUpdateDate(TimeUtil.getTimeStamp());

		return registeredProduct;
	}

}
