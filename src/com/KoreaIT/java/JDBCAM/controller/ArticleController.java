package com.KoreaIT.java.JDBCAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.java.JDBCAM.Article;
import com.KoreaIT.java.JDBCAM.service.ArticleService;
import com.KoreaIT.java.JDBCAM.util.DBUtil;
import com.KoreaIT.java.JDBCAM.util.SecSql;
import com.KoreaIT.java.JDBCAM.util.Util;

public class ArticleController {
	Connection conn;
	Scanner sc;

	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {
		this.conn = conn;
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite() {
		System.out.println("==글쓰기==");
		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body);

		System.out.println(id + "번 글이 생성되었습니다");

	}

	public void showList() {
		System.out.println("==목록==");
		
		articleService.showList();

	}

	public void doModify(String cmd) {

		int id = 0;

		try {
			id = Integer.parseInt(cmd.split(" ")[2]);
		} catch (Exception e) {
			System.out.println("번호는 정수로 입력해");
			return;
		}
		
		System.out.println("==수정==");
		System.out.print("새 제목 : ");
		String title = sc.nextLine().trim();
		System.out.print("새 내용 : ");
		String body = sc.nextLine().trim();
		
		
		id = articleService.doModify(id, title, body);

		System.out.println(id + "번 글이 수정되었습니다.");

	}

	public void showDetail(String cmd) {

		int id = 0;

		try {
			id = Integer.parseInt(cmd.split(" ")[2]);
		} catch (Exception e) {
			System.out.println("번호는 정수로 입력해");
			return;
		}

		System.out.println("==상세보기==");

		Map<String, Object> articles = articleService.showDetail(id);
		if (articles.isEmpty()) {
			System.out.println(id + "번 글은 없습니다.");
			return ;
		}
		Article article = new Article(articles);
		System.out.println("번호 : " + article.getId());
		System.out.println("작성날짜 : " + Util.getNowDate_TimeStr(article.getRegDate()));
		System.out.println("수정날짜 : " + Util.getNowDate_TimeStr(article.getUpdateDate()));
		System.out.println("제목 : " + article.getTitle());
		System.out.println("내용 : " + article.getBody());

	}

	public void doDelete(String cmd) {

		int id = 0;

		try {
			id = Integer.parseInt(cmd.split(" ")[2]);
		} catch (Exception e) {
			System.out.println("번호는 정수로 입력해");
			return;
		}
		Map<String, Object> articles = articleService.doDelete(id);
		if (articles.isEmpty()) {
			System.out.println(id + "번 글은 없습니다.");
			return;
		}
	
		System.out.println(id + "번 글이 삭제되었습니다.");

	}
}