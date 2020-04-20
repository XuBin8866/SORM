package com.xxbb.po;

import java.sql.*;
import java.util.*;

public class Freeze{

	private Integer id;
	private String state;

	public Freeze(){}

	public Integer getId(){
		return id;
	}
	public String getState(){
		return state;
	}

	public void setId(Integer id){
		this.id=id;
	}
	public void setState(String state){
		this.state=state;
	}

}