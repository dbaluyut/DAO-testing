package com.techelevator.projects.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.projects.model.Employee;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcEmployeeDao implements EmployeeDao {

	private final JdbcTemplate jdbcTemplate;

	public JdbcEmployeeDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employeeList = new ArrayList<>();
		String sql = "select * from employee";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while(results.next()){
			employeeList.add(mapRowToEmployee(results));
		}
		return employeeList;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employeeList = new ArrayList<>();

		String sql = "select * from employee where first_name ilike ? and last_name ilike ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%"+firstNameSearch + "%", "%" + lastNameSearch + "%");

		while(results.next()){
			employeeList.add(mapRowToEmployee(results));
		}

		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> employeeList = new ArrayList<>();

		String sql = "SELECT * FROM employee AS e" +
				" JOIN project_employee AS pe ON pe.employee_id = e.employee_id" +
				" WHERE project_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, projectId);

		while(results.next()){
			employeeList.add(mapRowToEmployee(results));
		}

		return employeeList;
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sql = "insert into project_employee(project_id, employee_id) " +
				"values(?,?)";
		jdbcTemplate.update(sql, projectId, employeeId);

	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
	String sql = "delete from project_employee where employee_id = ? and project_id = ?";
	jdbcTemplate.update(sql, employeeId, projectId);
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> employeeList = new ArrayList<>();

		String sql = "SELECT * FROM employee AS e " +
		"FULL JOIN project_employee AS pe ON pe.employee_id = e.employee_id " +
		"WHERE pe.project_id IS NULL";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while(results.next()){
			employeeList.add(mapRowToEmployee(results));
		}

		return employeeList;
	}

	private Employee mapRowToEmployee(SqlRowSet rowSet){
		Employee employee = new Employee();
		employee.setId(rowSet.getLong("employee_id"));
		employee.setDepartmentId(rowSet.getLong("department_id"));
		employee.setFirstName(rowSet.getString("first_name"));
		employee.setLastName(rowSet.getString("last_name"));
		employee.setBirthDate(rowSet.getDate("birth_date").toLocalDate());
		employee.setHireDate(rowSet.getDate("hire_date").toLocalDate());

		Long department_id = rowSet.getLong("department_id");

		if(department_id != null){
			employee.setDepartmentId(rowSet.getLong("department_id"));
		}

		return employee;
	}

}
