package com.KoreaIT.java.JDBCAM.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class JDBCSelectTest {
	public static void main(String[] args) {

        Connection conn = null;                // 데이터 베이스와 연결을 위한 객체
        Statement stmt = null;        // SQL 문을 데이터베이스에 보내기위한 객체
        ResultSet rs = null;                   // SQL 질의에 의해 생성된 테이블을 저장하는 객체

        // 1. JDBC Driver Class - com.mysql.jdbc.Driver
        String driver = "com.mysql.jdbc.Driver";

        // 2. 데이터베이스에 연결하기 위한 정보
        String url = "jdbc:mysql://127.0.0.1:3306/JDBC_AM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
        String user = "root";        // 데이터베이스 ID
        String pw = "";              // 데이터베이스 PW

        String SQL = "SELECT * FROM article ORDER BY id DESC";

        try {
            // 1. JDBC 드라이버 로딩
            Class.forName(driver);

            // 2. Connection 객체 생성
            conn = DriverManager.getConnection(url, user, pw); // DB 연결

            // 3. Statement 객체 생성
            stmt = conn.createStatement();

            // 4. SQL 문장을 실행하고 결과를 리턴
            // stmt.excuteQuery(SQL) : select
            // stmt.excuteUpdate(SQL) : insert, update, delete ..
            rs = stmt.executeQuery(SQL);

            // 5. ResultSet에 저장된 데이터 얻기 - 결과가 2개 이상
            while (rs.next()) {

                String id = rs.getString("id");
                String regDate = rs.getString("regDate");
                String updateDate = rs.getString("updateDate");
                String title = rs.getString("title");
                String body = rs.getString("body"); //rs.getString("email");

                System.out.println(id + " " + regDate + " " + updateDate + " " + title + " " + body);
            }

            //5. ResultSet에 저장된 데이터 얻기 - 결과가 1개
            // if(rs.next()) {
            //
            // }
            // else {
            //
            // }
        } catch (SQLException e) {

            System.out.println("SQL Error : " + e.getMessage());

        } catch (ClassNotFoundException e1) {

            System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");

        } finally {

            //사용순서와 반대로 close 함
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                	conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
