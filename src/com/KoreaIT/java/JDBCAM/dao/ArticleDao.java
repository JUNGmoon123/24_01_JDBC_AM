package com.KoreaIT.java.JDBCAM.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.KoreaIT.java.JDBCAM.container.Container;
import com.KoreaIT.java.JDBCAM.dto.Article;
import com.KoreaIT.java.JDBCAM.util.DBUtil;
import com.KoreaIT.java.JDBCAM.util.SecSql;

public class ArticleDao {

	public int doWrite(int memberId, String title, String body) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW(),");
		sql.append("updateDate = NOW(),");
		sql.append("memberId = ?,", memberId);
		sql.append("title = ?,", title);
		sql.append("`body`= ?;", body);

		return DBUtil.insert(Container.conn, sql);
	}
	//원래 Map<String, Object>메소드명이었으나 Article로 바꿈
	public Article getArticleById(int id) {
		
		SecSql sql = new SecSql();

		sql.append("SELECT A.*, M.name AS extra__writer");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		sql.append("WHERE A.id = ?;", id);
		
		
		
		//이부분에서 Map을 넘겨주면 
		Map<String, Object> articleMap = DBUtil.selectRow(Container.conn, sql);

		if (articleMap.isEmpty()) {
			return null;
		}
		//Article로 러턴값을 준다.
		return new Article(articleMap);
	}

	public void doDelete(int id) {
		SecSql sql = new SecSql();

		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?;", id);

		DBUtil.delete(Container.conn, sql);
	}

	public void doUpdate(int id, String title, String body) {
		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		if (title.length() > 0) {
			sql.append(",title = ?", title);
		}
		if (body.length() > 0) {
			sql.append(",`body`= ?", body);
		}
		sql.append("WHERE id = ?;", id);

		DBUtil.update(Container.conn, sql);

	}

	public List<Article> getArticles() {
		SecSql sql = new SecSql();

		sql.append("SELECT A.*, M.name AS extra__writer");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		sql.append("ORDER BY id DESC;");
		
		//이 부분을 위에서 다시 만듬, Map을 Article로 받을려고
		List<Map<String, Object>> articleListMap = DBUtil.selectRows(Container.conn, sql);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}
		return articles;
	}
	public List<Article> getForPrintArticles(Map<String, Object> args) {
		SecSql sql = new SecSql();

		String searchKeyword = null;

		if (args.containsKey("searchKeyword")) {
			searchKeyword = (String) args.get("searchKeyword");
		}

		int limitFrom = -1;
		int limitTake = -1;

		if (args.containsKey("limitFrom")) {
			limitFrom = (int) args.get("limitFrom");
		}

		if (args.containsKey("limitTake")) {
			limitTake = (int) args.get("limitTake");
		}

		sql.append("SELECT A.*, M.name AS extra__writer");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		if (searchKeyword.length() > 0) {
			sql.append("WHERE A.title LIKE CONCAT('%',?,'%')", searchKeyword); /// ? 띄어쓰기 x
		}										
		sql.append("ORDER BY id DESC");
		if (limitFrom != -1) {
			sql.append("LIMIT ?, ?;", limitFrom, limitTake);
		}					//From : 총 몇페이지까지 예) limit 5 5 
							//앞5는 0부터 4페이지검색 뒤에 5는 총5개만 나타내겠다.
		System.out.println(sql);

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(Container.conn, sql);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

}