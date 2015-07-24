package au.gov.ga.geodesy.support.spring;

import geodb.GeoDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement

@EnableJpaRepositories(
    value = {"au.gov.ga.geodesy.domain.model", "au.gov.ga.geodesy.igssitelog.domain.model"},
    entityManagerFactoryRef = "geodesyEntityManagerFactory",
    transactionManagerRef = "geodesyTransactionManager"
)
public class PersistenceJpaConfig {

    private static final Logger log = LoggerFactory.getLogger(PersistenceJpaConfig.class);

    private static enum DatabaseType {
        IN_MEMORY, EXTERNAL;
    }

    private static final String DATABASE_TYPE_PROPERTY_NAME = "unitTestDatabaseType";
    private DatabaseType databaseType;

    @Autowired
    private ApplicationContext context;

    public PersistenceJpaConfig() {
        databaseType = DatabaseType.valueOf(System.getProperty(DATABASE_TYPE_PROPERTY_NAME));
        log.info("Database type: " + databaseType);
        /* Runtime.getRuntime().addShutdownHook(new Thread() { */
        /*     public void run() { */
        /*         System.out.println(isDaemon()); */
        /*         if (System.getProperty("keepAlive") != null) { */
        /*             log.info("Keeping the JVM alive, so you can inspect the in-memory database at http://localhost:8082/."); */
        /*             log.info("Type ctrl-c to exit."); */
        /*             try { */
        /*                 Thread.sleep(Long.MAX_VALUE); */
        /*             } catch (InterruptedException ok) { */
        /*             } */
        /*         } */
        /*     } */
        /* }); */
    }

    @Bean
    @Primary
    /* @DependsOn("liquibase") */
    public LocalContainerEntityManagerFactoryBean geodesyEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean springFactoryBean = new LocalContainerEntityManagerFactoryBean();
        springFactoryBean.setPackagesToScan(new String[]{"au.gov.ga.geodesy.domain.model", "au.gov.ga.geodesy.igssitelog.domain.model"});
        springFactoryBean.setDataSource(dataSource());
        springFactoryBean.setPersistenceUnitName("geodesy");
        JpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        springFactoryBean.setJpaVendorAdapter(vendor);
        springFactoryBean.setJpaProperties(jpaProperties());
        /* springFactoryBean.setMappingResources("au/gov/ga/geodesy/igssitelog/support/hibernate/hibernate.cfg.xml"); */
        return springFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        switch (databaseType) {
            case IN_MEMORY: return inMemoryDataSource();
            case EXTERNAL:  return externalDataSource();
            default: return null;
        }
    }

    private DataSource externalDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@dm02-scan.prod.lan:7381/ORADEV");
        dataSource.setUsername("u62208");
        dataSource.setPassword("");
        return dataSource;
    }

    private DataSource inMemoryDataSource() {
        System.setProperty("h2.baseDir", "target/");
        final String h2Url = "jdbc:h2:./h2-test-db;INIT=CREATE SCHEMA IF NOT EXISTS U62208;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        DataSource dataSource = new SingleConnectionDataSource() {
            {
                setDriverClassName("org.h2.Driver");
                setUrl(h2Url);
                setSuppressClose(true);
            }

            @Override
            public Connection getConnection() throws SQLException {
                Connection c = super.getConnection();
                GeoDB.InitGeoDB(c);
                return c;
            }
        };
        try {
            Server.createWebServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /* @Bean */
    public SpringLiquibase liquibase() {
        if (databaseType == DatabaseType.IN_MEMORY) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(dataSource());
            liquibase.setChangeLog("classpath:db/geodesy-database-changelog.xml");
            liquibase.setDefaultSchema("u62208");
            return liquibase;
        } else {
            return null;
        }
    }

    @Bean
    public PlatformTransactionManager geodesyTransactionManager(EntityManagerFactory f) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(f);
        transactionManager.setPersistenceUnitName("geodesy");
        return transactionManager;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.default_schema", "u62208");

        switch (databaseType) {
            case IN_MEMORY:
                properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.h2geodb.GeoDBDialect");
                properties.setProperty("hibernate.hbm2ddl.auto", "create");
                /* properties.setProperty("hibernate.show_sql", "true"); */
                /* properties.setProperty("hibernate.format_sql", "true"); */
                /* properties.setProperty("hibernate.use_sql_comments", "true"); */
                break;
            case EXTERNAL:
                properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.oracle.OracleSpatial10gDialect");
                properties.setProperty("hibernate.hbm2ddl.auto", "create");
                break;
            default:
        }
        return properties;
    }
}
