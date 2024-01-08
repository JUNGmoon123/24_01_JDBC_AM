package com.KoreaIT.java.JDBCAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	public void run() {
		System.out.println("==프로그램 시작==");
		Scanner sc = new Scanner(System.in);

		while (true) {
			//명령을 받을때마다 재연결을 시도해서 console창을 멈출필요없이 실행가능하다
			//실행하는도중에 코딩문장이 달라질경우 에러를 잡아낸다.
			System.out.print("명령어 > ");
			String cmd = sc.nextLine().trim();

			Connection conn = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//포트번호 '/'뒤에 DB이름적어서 url로 전해준다.
			String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			try {
				//url주소와 SQLyog를 생성할때 만든 아이디, 비번(설정없으면밑에처럼) 해서 접속을 시도한다.
				conn = DriverManager.getConnection(url, "root", "");

				int actionResult = doAction(conn, sc, cmd);

				if (actionResult == -1) {
					System.out.println("==프로그램 종료==");
					sc.close();
					break;
				}

			} catch (SQLException e) {
				System.out.println("에러 1 : " + e);
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int doAction(Connection conn, Scanner sc, String cmd) {

		if (cmd.equals("exit")) {
			return -1;
		}

		if (cmd.equals("article write")) {
			System.out.println("==글쓰기==");
			System.out.print("제목 : ");
			String title = sc.nextLine();
			System.out.print("내용 : ");
			String body = sc.nextLine();

			PreparedStatement pstmt = null;

			try {
				String sql = "INSERT INTO article ";
				sql += "SET regDate = NOW(),";
				sql += "updateDate = NOW(),";
				sql += "title = '" + title + "',";
				sql += "`body`= '" + body + "';";

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);
				
				//executeUpdate()함수는 int형정수를 반환한다.
				int affectedRow = pstmt.executeUpdate();

				System.out.println(affectedRow + "열에 적용됨");

			} catch (SQLException e) {
				System.out.println("에러 2: " + e);
			} finally {
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (cmd.equals("article list")) {
			System.out.println("==목록==");

			PreparedStatement pstmt = null;
			ResultSet rs = null;

			List<Article> articles = new ArrayList<>();

			try {
				String sql = "SELECT *";
				sql += " FROM article";
				sql += " ORDER BY id DESC;";

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);

				rs = pstmt.executeQuery(sql);

				while (rs.next()) {
					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					Article article = new Article(id, regDate, updateDate, title, body);

					articles.add(article);
				}

			} catch (SQLException e) {
				System.out.println("에러 3 : " + e);
			} finally {
				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			if (articles.size() == 0) {
				System.out.println("게시글이 없습니다");
				return 0;
			}

			System.out.println("  번호  /   제목  ");
			for (Article article : articles) {
				System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());
			}
		} else if (cmd.startsWith("article modify")) {

			int id = 0;

			try {
				id = Integer.parseInt(cmd.split(" ")[2]);
			} catch (Exception e) {
				System.out.println("번호는 정수로 입력해");
				return 0;
			}

			System.out.println("==수정==");
			System.out.print("새 제목 : ");
			String title = sc.nextLine().trim();
			System.out.println("새 내용 : ");
			String body = sc.nextLine().trim();

			PreparedStatement pstmt = null;

			try {
				String sql = "UPDATE article";
				sql += " SET updateDate = NOW()";
				//수정시 아무것도 입력안할경우 원래값을 그대로 전달한다.
				if (title.length() > 0) {
					sql += " ,title = '" + title + "'";
				}
				if (body.length() > 0) {
					sql += " ,`body` = '" + body + "'";
				}
				sql += " WHERE id = " + id + ";";

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				System.out.println("에러 4 : " + e);
			} finally {
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			System.out.println(id + "번 글이 수정되었습니다.");
		}
		return 0;
	}
}