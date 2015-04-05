package com.project.signture.entities;

import java.util.List;

import com.orm.SugarRecord;

public class User extends SugarRecord<User>{
	String name;
	int age;
	String pin;
	long phone;
	String email;
	
	public User(){
	}
	
	public User(String name, int age, String pin, long phone, String email){
		this.name = name;
		this.age = age;
		this.pin = pin;
		this.phone = phone;
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long l) {
		this.phone = l;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Signature> getSignatures(User user){
        return Signature.find(Signature.class, "user = ?", user.getId().toString());
    }
}
