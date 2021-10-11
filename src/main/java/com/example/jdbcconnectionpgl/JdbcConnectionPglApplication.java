package com.example.jdbcconnectionpgl;

import com.example.jdbcconnectionpgl.PostgresConnector.PostgresInterface;
import com.example.jdbcconnectionpgl.PostgresConnector.PostrgesSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;


@SpringBootApplication
public class JdbcConnectionPglApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(JdbcConnectionPglApplication.class, args);
        Connection[] cons = new Connection[10];
        for(int i =0 ; i< 10 ; i++) {
            Connection connection= PostrgesSingleton.getDatasource().getConnection();
            cons[i] = connection;
            System.out.println(i);
        }

        PostgresInterface pgInterface = new PostgresInterface(cons[0]);
        ResultSet rs = pgInterface.readQuery("SELECT VERSION() AS version");
        while (rs.next()) {
            String version = rs.getString("version");
            System.out.println(version);
        }

        //SELECT count, mall_id FROM public."ReadCount";



        //INSERT INTO public."ReadCount"(count, mall_id) VALUES (30, mall_30);

        Random  rd = new Random();
        for ( int i =0 ; i < 1000 ; i++){
            int rdReadCount = rd.nextInt(10000);
            String mall_id = "mall_" + String.valueOf(i);
            try {
                int resultCode = pgInterface.updateQuery("INSERT INTO public.\"ReadCount\"(count, mall_id) VALUES (" + rdReadCount + "," + "'" + mall_id + "'" + " )");
                System.out.println(resultCode);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        System.out.println("done");

        ResultSet rs2 = pgInterface.readQuery("SELECT count, mall_id FROM public.\"ReadCount\"");
        System.out.println("count, mall id");
        while(rs2.next()){
            long count = rs2.getLong("count");
            String mallID = rs2.getString("mall_id");
            System.out.println(count + "\t" + mallID);
        }

    }
}

//JDBC 정리
// JDBC 는 자바에서 제공하는 인터페이스, 각 데이터 베이스에 접근 하는 드라이버의 api의 시그네쳐가 정의되어 있다.
// 자바 개발자는 JDBC api를 적용할 수 있는 드라이버를 다운받아 사용하는 것으로 JDBC의 기능, 즉 통일된 인터페이스로 데이터베이스에 접근 가능 하다.

// 아래의 클래스는 java.sql의 요소이다.
// Connection           디비 커넥션
// ResultSet            쿼리 결과
// PreparedStatement    쿼리문
// 주로 미리 변수로 만들었다가, 요청 전후로 대입해서 쿼리문을 보내는 듯 하다.

/*
JDBC & DBCP 의 클래스 만들기에서 주의 해야 할점

1. 풀을 관리하는 로직, 상태가 들어있는 클래스는 싱글턴으로 제작한다.
public class singleDBManager {

}
 */

