package com.xxbb.po;

import java.sql.*;
import java.util.*;

public class User{

	private String password;
	private Integer ifFreeze;
	private Integer id;
	private String username;

	public User(){}

	public String getPassword(){
		return password;
	}
	public Integer getIfFreeze(){
		return ifFreeze;
	}
	public Integer getId(){
		return id;
	}
	public String getUsername(){
		return username;
	}

	public void setPassword(String password){
		this.password=password;
	}
	public void setIfFreeze(Integer ifFreeze){
		this.ifFreeze=ifFreeze;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setUsername(String username){
		this.username=username;
	}

}