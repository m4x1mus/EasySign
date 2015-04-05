package com.project.signture.entities;

import com.orm.SugarRecord;

public class Signature extends SugarRecord<Signature>{
	float pressure;
	float size;
	int up_down;
	int time;
	User user;
	
	public Signature(){
		
	}
	
	public Signature(float pressure, float size, int up_down, int time, User user){
		this.pressure = pressure;
		this.size = size;
		this.up_down = up_down;
		this.time = time;
		this.user = user;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

	public float getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getUp_down() {
		return up_down;
	}

	public void setUp_down(int up_down) {
		this.up_down = up_down;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
