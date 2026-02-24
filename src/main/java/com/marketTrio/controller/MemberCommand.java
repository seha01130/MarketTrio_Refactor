package com.marketTrio.controller;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.marketTrio.domain.Member;

@SuppressWarnings("serial")
public class MemberCommand implements Serializable {
	private Member member;
	@NotBlank(message = "패스워드는 필수 항목입니다")
	private String repeatedPassword;

	public MemberCommand(Member member) {
		this.member = member;
	}

	public MemberCommand() {
		this.member = new Member();
	}

	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
        this.member = member;
    }
	
	@NotBlank(message = "패스워드는 필수 항목입니다")
	public String getPassword() {
		return member.getPassword();
	}
	
	public void setPassword(String password) {
        member.setPassword(password);
    }
	
	public String getRepeatedPassword() {
        return repeatedPassword;
    }

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

	@NotBlank(message = "이름은 필수 항목입니다")
	public String getName() {
        return member.getName();
    }

    public void setName(String name) {
        member.setName(name);
    }

    @NotBlank(message = "닉네임은 필수 항목입니다")
    public String getNickname() {
        return member.getNickname();
    }

    public void setNickname(String nickname) {
        member.setNickname(nickname);
    }

    @Email(message = "이메일의 형식에 맞게 입력해야합니다")
    @NotBlank(message = "이메일은 필수 항목입니다")
    public String getEmail() {
        return member.getEmail();
    }

    public void setEmail(String email) {
        member.setEmail(email);
    }

    @NotBlank(message = "번호는 필수 항목입니다")
    public String getPhone() {
        return member.getPhone();
    }

    public void setPhone(String phone) {
        member.setPhone(phone);
    }

    @NotBlank(message = "아이디는 필수 항목입니다")
    public String getUserId() {
        return member.getId();
    }

    public void setUserId(String userId) {
        member.setId(userId);
    }
}
