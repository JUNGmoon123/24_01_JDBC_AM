package com.KoreaIT.java.JDBCAM.service;

import java.sql.Connection;
import java.util.Map;

import com.KoreaIT.java.JDBCAM.dao.ArticleDao;

public class ArticleService {

	private ArticleDao articleDao;

	public ArticleService(Connection conn) {
		this.articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body) {
		return articleDao.doWrite(title, body);
	}

	public void showList() {
		articleDao.showList();
	}

	public int doModify(int id, String title, String body) {
		return articleDao.doModify(id, title, body);
		
	}

	public Map<String, Object> showDetail(int id) {
		return articleDao.showDetail(id);
	}

	public Map<String, Object> doDelete(int id) {
		return articleDao.doDelete(id);
		
	}

}