package com.marketTrio.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("serial")
@Entity
@Table(name = "Member")
public class Member implements Serializable {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String nickname;
	private String profilePicture;
	private float rating;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String phone;
	
//	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
//    private AParticipantEntity aparticipantEntity;
	
//	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
//    private GBParticipantEntity gbparticipantEntity;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AParticipantEntity> participants = new ArrayList<>();
	
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
        return profilePicture == null ? "" : profilePicture;
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
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
