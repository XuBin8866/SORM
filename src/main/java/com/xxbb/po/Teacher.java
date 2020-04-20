package com.xxbb.po;

import java.sql.*;
import java.util.*;

public class Teacher{

	private String name;
	private Integer id;

	public Teacher(){}

	public String getName(){
		return name;
	}
	public Integer getId(){
		return id;
	}

	public void setName(String name){
		this.name=name;
	}
	public void setId(Integer id){
		this.id=id;
	}

}