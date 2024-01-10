package com.KoreaIT.java.JDBCAM;

import java.time.LocalDateTime;
import java.util.Map;

public class Member {
	private int id;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	private String loginId;
	private String loginPw;
	private String name;
	
	public Member(int id, LocalDateTime regDate, LocalDateTime updateDate, String loginId, String loginPw, String name) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.name = name;
	}

	public Member(Map<String, Object> membersMap) {
		this.id = (int) membersMap.get("id");
		this.regDate = (LocalDateTime) membersMap.get("regDate");
		this.updateDate = (LocalDateTime) membersMap.get("updateDate");
		this.loginId = (String) membersMap.get("loginId");
		this.loginPw = (String) membersMap.get("loginPw");
		this.name = (String) membersMap.get("`name`");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPw() {
		return loginPw;
	}

	public void setLoginPw(String loginPw) {
		this.loginPw = loginPw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
