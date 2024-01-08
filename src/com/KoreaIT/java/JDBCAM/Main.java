package com.KoreaIT.java.JDBCAM;

import java.awt.image.BandCombineOp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("==프로그램 시작==");
		Scanner sc = new Scanner(System.in);

		int lastArticleId = 0;

		while (true) {
			System.out.print("명령어 > ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}

			if (cmd.equals("article write")) {
				System.out.println("==글쓰기==");
				int id = lastArticleId + 1;
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();

				lastArticleId = id;

				System.out.println(id + "번 글이 등록되었습니다");

				Connection conn = null;
				PreparedStatement pstmt = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "INSERT INTO article ";
					sql += "SET regDate = NOW(),";
					sql += "updateDate = NOW(),";
					sql += "title = '" + title + "',";
					sql += "`body`= '" + body + "';";

					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

					int affectedRow = pstmt.executeUpdate();

					System.out.println("affectedRow : " + affectedRow);

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
				} finally {
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
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

			} else if (cmd.equals("article list")) {
				System.out.println("==목록==");

				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				List<Article> articles = new ArrayList<>();

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

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
//					for (int i = 0; i < articles.size(); i++) {
//						System.out.println("번호 : " + articles.get(i).getId());
//						System.out.println("등록 날짜 : " + articles.get(i).getRegDate());
//						System.out.println("수정 날짜 : " + articles.get(i).getUpdateDate());
//						System.out.println("제목 : " + articles.get(i).getTitle());
//						System.out.println("내용 : " + articles.get(i).getBody());
//					}

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (articles.size() == 0) {
					System.out.println("게시글이 없습니다");
					continue;
				}

				System.out.println("  번호  /   제목  ");
				for (Article article : articles) {
					System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());
				}
			}else if (cmd.startsWith("article modify")) {
				String[] cmdBits = cmd.split(" ");
				if(cmdBits.length <= 2) {
					System.out.println(cmdBits[0]);
					System.out.println(cmdBits[1]);
					continue;
				}
				System.out.println(cmdBits[2]);
				System.out.println("==수정==");

				List<Article> articles = new ArrayList<>();

				
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");
					
					String sql = "SELECT *";
					sql += " FROM article";
					sql += " ORDER BY id DESC;";
					
					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

					rs = pstmt.executeQuery(sql);
					Article article = null;
					String title = null;
					String body = null;
					while (rs.next()) {
						int id = rs.getInt("id");
						String regDate = rs.getString("regDate");
						String updateDate = rs.getString("updateDate");
						title = rs.getString("title");
						body = rs.getString("body");

						article = new Article(id, regDate, updateDate, title, body);

						articles.add(article);
						
						if(id == Integer.parseInt(cmdBits[2])) {
							System.out.println("번호 : " + article.getId());
							System.out.println("등록 날짜 : " + article.getRegDate());
							System.out.println("수정 날짜 : " + article.getUpdateDate());
							System.out.println("제목 : " + article.getTitle());
							System.out.println("내용 : " + article.getBody());
							}
						
						
					}
					System.out.print("수정 제목: ");
					title = sc.nextLine().trim();
					article.setTitle(title);
					System.out.print("수정 내용: ");
					body = sc.nextLine().trim();
					article.setTitle(body);
//					System.out.println("제목 : " + article.getTitle());
					sql =  "UPDATE article ";
					sql += "SET regDate = NOW(),";
					sql += "updateDate = NOW(),";
					sql += "title = '" + title + "',";
					sql += "`body` = '" + body + "'";
					sql += "WHERE id = '"+ cmdBits[2] +"';";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
					sql = "SELECT *";
					sql += " FROM article";
					sql += " ORDER BY id DESC;";
					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

					rs = pstmt.executeQuery(sql);
					
					while (rs.next()) {
						int id = rs.getInt("id");
						String regDate = rs.getString("regDate");
						String updateDate = rs.getString("updateDate");
						title = rs.getString("title");
						body = rs.getString("body");

						article = new Article(id, regDate, updateDate, title, body);

						articles.add(article);
						
						if(id == Integer.parseInt(cmdBits[2])) {
							System.out.println("번호 : " + article.getId());
							System.out.println("제목 : " + article.getTitle());
							System.out.println("내용 : " + article.getBody());
							}
						
					}
				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				
		
			}else if (cmd.startsWith("article detail")) {
				String[] cmdBits = cmd.split(" ");
				if(cmdBits.length <= 2) {
					System.out.println(cmdBits[0]);
					System.out.println(cmdBits[1]);
					continue;
				}
				System.out.println(cmdBits[2]);
				System.out.println("==상세보기==");

				List<Article> articles = new ArrayList<>();

				
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");
					
					String sql = "SELECT *";
					sql += " FROM article";
					sql += " ORDER BY id DESC;";
					
					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

					rs = pstmt.executeQuery(sql);
					Article article = null;
					String title = null;
					String body = null;
					while (rs.next()) {
						int id = rs.getInt("id");
						String regDate = rs.getString("regDate");
						String updateDate = rs.getString("updateDate");
						title = rs.getString("title");
						body = rs.getString("body");

						article = new Article(id, regDate, updateDate, title, body);

						articles.add(article);
						
						if(id == Integer.parseInt(cmdBits[2])) {
							System.out.println("번호 : " + article.getId());
							System.out.println("등록 날짜 : " + article.getRegDate());
							System.out.println("수정 날짜 : " + article.getUpdateDate());
							System.out.println("제목 : " + article.getTitle());
							System.out.println("내용 : " + article.getBody());
							}
						
						
					}
				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				
				
			}else if (cmd.startsWith("article remove")) {
				String[] cmdBits = cmd.split(" ");
				if(cmdBits.length <= 2) {
					System.out.println(cmdBits[0]);
					System.out.println(cmdBits[1]);
					continue;
				}
				System.out.println(cmdBits[2]);
				System.out.println("==삭제==");

				List<Article> articles = new ArrayList<>();

				
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql  = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");
					
					sql = "SELECT *";
					sql += " FROM article";
					sql += " ORDER BY id DESC;";
					
					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

					rs = pstmt.executeQuery(sql);
					
//					DELETE FROM Person WHERE id=1;
					sql = "DELETE FROM article ";
					sql += "WHERE id = '"+ cmdBits[2] +"';";
					
					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
					
					
				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러 : " + e);
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
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (articles.size() == 0) {
					System.out.println(Integer.parseInt(cmdBits[2])+" 번 게시글이 지워졌습니다.");
					continue;
				}

			}
		} 

		System.out.println("==프로그램 종료==");

		sc.close();
	}
}