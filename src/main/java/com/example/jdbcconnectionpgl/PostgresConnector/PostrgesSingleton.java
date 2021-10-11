package com.example.jdbcconnectionpgl.PostgresConnector;

import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.util.Properties;

public class PostrgesSingleton {
    private static final String DB_USERNAME="db.username";
    private static final String DB_PASSWORD="db.password";
    private static final String DB_URL ="db.url";
    private static final String DB_DRIVER_CLASS="driver.class.name";

    private static Properties properties = null;
    private static HikariDataSource dataSource;

    static{
        try {
            properties =  new Properties();
            properties.load(new FileInputStream("src/database.properties"));

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(properties.getProperty("driver.class.name"));
            dataSource.setJdbcUrl(properties.getProperty("db.url"));
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));

            dataSource.setMaximumPoolSize(20);
            dataSource.setMinimumIdle(20); // same as maximum pool size
            dataSource.setConnectionTimeout(2000);
            dataSource.setMaxLifetime(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PostrgesSingleton(){};

    public static HikariDataSource getDatasource(){
        return dataSource;
    }
}

/*
initialSize	BasicDataSource 클래스 생성 후 최초로 getConnection() 메서드를 호출할 때 커넥션 풀에 채워 넣을 커넥션 개수
maxActive	동시에 사용할 수 있는 최대 커넥션 개수(기본값: 8)
maxIdle	커넥션 풀에 반납할 때 최대로 유지될 수 있는 커넥션 개수(기본값: 8)
minIdle	최소한으로 유지할 커넥션 개수(기본값: 0)



maxActive >= initialSize
maxActive = 10이고 initialSize = 20이라고 가정하면 최초에 커넥션을 생성할 때 initialSize 값이 최대 커넥션 개수인 maxActive 값보다 커서 논리적으로 오류가 있는 설정이다.
maxIdle >= minIdle
maxIdle < minIdle로 설정할 수는 있지만 최솟값이 최댓값보다 커서 논리적으로 오류가 있는 설정이다.
maxActive = maxIdle
maxActive 값과 maxIdle 값이 같은 것이 바람직하다. maxActive = 10이고 maxIdle = 5라고 가정해 보자. 항상 커넥션을 동시에 5개는 사용하고 있는 상황에서 1개의 커넥션이 추가로 요청된다면 maxActive = 10이므로 1개의 추가 커넥션을 데이터베이스에 연결한 후 풀은 비즈니스 로직으로 커넥션을 전달한다. 이후 비즈니스 로직이 커넥션을 사용 후 풀에 반납할 경우, maxIdle=5에 영향을 받아 커넥션을 실제로 닫아버리므로, 일부 커넥션을 매번 생성했다 닫는 비용이 발생할 수 있다.
initialSize와 maxActive, maxIdle, minIdle 항목을 동일한 값으로 통일해도 무방하다. 커넥션 개수와 관련된 가장 중요한 성능 요소는 일반적으로 커넥션의 최대 개수다. 4개 항목의 설정 값 차이는 성능을 좌우하는 중요 변수는 아니다.

 */