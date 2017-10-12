package au.gov.ga.geodesy.support.spring;

import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories(
    value = {"au.gov.ga.geodesy.domain.model"},
    entityManagerFactoryRef = "geodesyEntityManagerFactory",
    transactionManagerRef = "geodesyTransactionManager"
)
public class PersistenceJpaConfig {

    @Autowired
    private IntegrationTestConfig testConfig;

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return new DateTimeProvider() {

            @Override
            public Calendar getNow() {
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                c.setTimeInMillis(0L);
                return c;
            }
        };
    }

    @Bean
    @Primary
    @DependsOn("liquibase")
    public LocalContainerEntityManagerFactoryBean geodesyEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean springFactoryBean = new LocalContainerEntityManagerFactoryBean();

        springFactoryBean.setPackagesToScan(new String[]{
            "au.gov.ga.geodesy.domain.model",
            "au.gov.ga.geodesy.support.persistence.jpa"
        });

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
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(testConfig.getDbUrl());
        dataSource.setUsername(testConfig.getDbUsername());
        dataSource.setPassword(testConfig.getDbPassword());
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:db/geodesy-database-changelog.xml");
        liquibase.setDefaultSchema("geodesy");
        return liquibase;
    }

    @Bean
    public PlatformTransactionManager geodesyTransactionManager(EntityManagerFactory f) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(f);
        transactionManager.setPersistenceUnitName("geodesy");
        return transactionManager;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.default_schema", "geodesy");
        properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.create_empty_composites.enabled", "true");
        /* properties.setProperty("hibernate.FlushMode", "commit"); */
        /* properties.setProperty("hibernate.show_sql", "true"); */
        /* properties.setProperty("hibernate.format_sql", "true"); */
        /* properties.setProperty("hibernate.use_sql_comments", "true"); */
        return properties;
    }
}
