package com.techelevator.projects.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;

public class JdbcDepartmentDao implements DepartmentDao {
	
	private final JdbcTemplate jdbcTemplate;

	public JdbcDepartmentDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Department getDepartment(Long id) {
		Department department = null;
		String sql = "select * from department where department_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			department = mapRowToDepartment(results);
		}
		return department;
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departmentList = new ArrayList<>();
		String sql = "SELECT * FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while(results.next()){
			departmentList.add(mapRowToDepartment(results));
		}
		return departmentList;
	}

	@Override
	public void updateDepartment(Department updatedDepartment) {
	String sqlString = "update department set name = ? where department_id = ?";
	String name = updatedDepartment.getName();
	Long id = updatedDepartment.getId();
	jdbcTemplate.update(sqlString, name, id);
	}

	private Department mapRowToDepartment(SqlRowSet rowSet){
		Department department = new Department();
		department.setId(rowSet.getLong("department_id"));
		department.setName(rowSet.getString("name"));
		return department;
	}
}
