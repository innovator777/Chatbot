package com.innovator.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VolunteerDAO {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Volunteer> listForBeanPropertyRowMapper() {
		String query = "SELECT * FROM Volunteer";
		return jdbc.query(query, new BeanPropertyRowMapper<Volunteer>(Volunteer.class));
	}
	
	public Volunteer selectTarget(int target) {
		String query = "select content from Volunteer where id = ?";
		return (Volunteer) jdbc.query(query, new BeanPropertyRowMapper(Volunteer.class), target);
	}
	
	public int deleteTarget(int target) {
		String query = "delete from Volunteer where id = ?";
		return jdbc.update(query, target);
	}
	
	public int deleteAll() {
		String query = "delete from Volunteer";
		return jdbc.update(query);
	}
	
	public int insert(Volunteer volunteer) {
		String query = "insert into Volunteer(content) values (?)";
		return jdbc.update(query, volunteer.getContent());
	}
	
}
