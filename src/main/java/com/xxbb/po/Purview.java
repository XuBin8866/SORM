package com.xxbb.po;

import java.sql.*;
import java.util.*;

public class Purview{

	private String role;
	private Integer id;

	public Purview(){}

	public String getRole(){
		return role;
	}
	public Integer getId(){
		return id;
	}

	public void setRole(String role){
		this.role=role;
	}
	public void setId(Integer id){
		this.id=id;
	}

}