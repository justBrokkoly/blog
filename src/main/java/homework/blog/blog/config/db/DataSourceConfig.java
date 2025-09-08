package homework.blog.blog.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DataSourceConfig {

    @Bean(name = "masterDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "masterDataSource")
    @ConfigurationProperties("spring.datasource.master.hikari")
    public HikariDataSource masterDataSource(@Qualifier("masterDataSourceProperties") DataSourceProperties properties) {
        var ds = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        ds.setPoolName("MasterCP");
        return ds;
    }

    @Bean(name = "replica1DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.replica1")
    public DataSourceProperties replica1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "replica1DataSource")
    @ConfigurationProperties("spring.datasource.replica1.hikari")
    public HikariDataSource replica1DataSource(@Qualifier("replica1DataSourceProperties") DataSourceProperties properties) {
        var ds = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        ds.setPoolName("Replica1CP");
        return ds;
    }

    @Bean(name = "replicaDataSources")
    public List<DataSource> replicaDataSources(
            @Qualifier("replica1DataSource") DataSource replica1) {
        return List.of(replica1);
    }

    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("replicaDataSources") List<DataSource> replicaDataSources) {

        ReadWriteRoutingDataSource routingDataSource = new ReadWriteRoutingDataSource();
        routingDataSource.setMasterDataSource(masterDataSource);
        routingDataSource.setReplicaDataSources(replicaDataSources);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

}
