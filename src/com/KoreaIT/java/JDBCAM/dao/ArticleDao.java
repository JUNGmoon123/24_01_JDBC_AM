package com.KoreaIT.java.JDBCAM.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.KoreaIT.java.JDBCAM.Article;
import com.KoreaIT.java.JDBCAM.util.DBUtil;
import com.KoreaIT.java.JDBCAM.util.SecSql;

public class ArticleDao {
	private Connection conn;

	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	public int doWrite(String title, String body) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW(),");
		sql.append("updateDate = NOW(),");
		sql.append("title = ?,", title);
		sql.append("`body`= ?;", body);

		return DBUtil.insert(conn, sql);
	}

	public void showList() {

		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("ORDER BY id DESC;");

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
			
		}
		if (articles.size() == 0) {
			System.out.println("게시글이 없습니다");
			
		}

		System.out.println("  번호  /   제목  ");
		for (Article article : articles) {
			System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());
		}
	}

	public int doModify(int id, String title, String body) {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?;", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

		if (articleMap.isEmpty()) {
			System.out.println(id + "번 글은 없습니다.");
			return id;
		}

		sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		if (title.length() > 0) {
			sql.append(",title = ?", title);
		}
		if (body.length() > 0) {
			sql.append(",`body`= ?", body);
		}
		sql.append("WHERE id = ?;", id);

		DBUtil.update(conn, sql);
		return id;
	}

	public Map<String, Object> showDetail(int id) {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?;", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

//		Article article = new Article(articleMap);
		return articleMap;
	}

	public Map<String, Object> doDelete(int id) {
		
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?;", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

		sql = new SecSql();

		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?;", id);

		DBUtil.delete(conn, sql);
		return articleMap;
	}

}