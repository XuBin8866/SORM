package com.xxbb.po;

import java.sql.*;
import java.util.*;

public class Account{

	private String password;
	private Double balance;
	private String name;
	private Integer id;
	private String account;

	public Account(){}

	public String getPassword(){
		return password;
	}
	public Double getBalance(){
		return balance;
	}
	public String getName(){
		return name;
	}
	public Integer getId(){
		return id;
	}
	public String getAccount(){
		return account;
	}

	public void setPassword(String password){
		this.password=password;
	}
	public void setBalance(Double balance){
		this.balance=balance;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setAccount(String account){
		this.account=account;
	}

}