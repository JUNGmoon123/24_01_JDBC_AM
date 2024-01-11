package com.KoreaIT.java.JDBCAM.session;

import com.KoreaIT.java.JDBCAM.dto.Member;

public class Session {
	//교차비교를 위해 id값을 하나더 가져옴.
	public Member loginedMember;
	public int loginedMemberId;
	//-1일때는 아무도 로그인 하지 않았음,
	//누군가 로그인시 Id값은 -1이 아닌 어떠한값을 가지게된다.
	//-1이 아니면 판단바로 가능.
	public Session() {
		loginedMemberId = -1;
	}
	public void login(Member member) {
		loginedMember = member;
		loginedMemberId = member.getId();
	}

	public void logout() {
		loginedMember = null;
		loginedMemberId = -1;
		System.out.println("로그아웃 됨");
	}

	public boolean isLogined() {
		return loginedMemberId != -1;
	}
}
