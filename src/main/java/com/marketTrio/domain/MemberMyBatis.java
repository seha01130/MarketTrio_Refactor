package com.marketTrio.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberMyBatis implements Serializable {
	private String id;
	private String password;
	private String nickname;
	private String profilePicture;
	private float rating;
	private String email;
	private String name;
	private String phone;
	
	
	public MemberMyBatis() {
		super();
	}
	public MemberMyBatis(String id, String password, String nickname, String profilePicture, float rating, String email,
			String name, String phone) {
		super();
		this.id = id;
		this.password = password;
		this.nickname = nickname;
		this.profilePicture = profilePicture;
		this.rating = rating;
		this.email = email;
		this.name = name;
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
