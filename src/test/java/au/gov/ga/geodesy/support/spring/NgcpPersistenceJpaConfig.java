package au.gov.ga.geodesy.support.spring;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        value = {"au.gov.ga.geodesy.ngcp.domain.model"},
        entityManagerFactoryRef = "ngcpEntityManagerFactory",
        transactionManagerRef = "ngcpTransactionManager")
public class NgcpPersistenceJpaConfig {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(NgcpPersistenceJpaConfig.class);

    @Bean
    public DataSource ngcpDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean ngcpEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean springFactoryBean = new LocalContainerEntityManagerFactoryBean();

        springFactoryBean.setDataSource(ngcpDataSource());
        springFactoryBean.setPackagesToScan(new String[] {"au.gov.ga.geodesy.ngcp.domain.model"});

        JpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        springFactoryBean.setJpaVendorAdapter(vendor);

        springFactoryBean.setJpaProperties(jpaProperties());
        return springFactoryBean;
    }

    @Bean
    public PlatformTransactionManager ngcpTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.default_schema", "NGCP");
        properties.setProperty("hibernate.dialect", "org.hibernate.spatial.dialect.oracle.OracleSpatial10gDialect");
        return properties;
    }
}
