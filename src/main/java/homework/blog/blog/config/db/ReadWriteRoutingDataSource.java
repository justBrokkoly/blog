package homework.blog.blog.config.db;


import homework.blog.blog.model.enums.DataSourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
@Setter
public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {

    private DataSource masterDataSource;
    private List<DataSource> replicaDataSources;
    private final AtomicInteger replicaCounter = new AtomicInteger(0);
    private final ThreadLocal<String> forceDataSource = new ThreadLocal<>();

    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER, masterDataSource);
        for (int i = 0; i < replicaDataSources.size(); i++) {
            targetDataSources.put(DataSourceType.REPLICA.name() + "_" + i, replicaDataSources.get(i));
        }
        setTargetDataSources(targetDataSources);
        setDefaultTargetDataSource(masterDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        if(DataSourceContextHolder.isMasterRequired()){
            log.debug("Using master datasource for write transaction");
            return DataSourceType.MASTER;
        }

        if(DataSourceContextHolder.isReplicaRequired() || TransactionSynchronizationManager.isCurrentTransactionReadOnly()){
            String replicaKey = selectReadReplica();
            log.debug("Using read replica: {}", replicaKey);
            return replicaKey;
        }

        if(TransactionSynchronizationManager.isActualTransactionActive() ){
            logger.debug("Using master datasource for write transaction");
            return DataSourceType.MASTER;
        }

        return DataSourceType.MASTER;
    }

    private String selectReadReplica() {
        if (replicaDataSources.isEmpty()) {
            logger.warn("No replica datasources available, falling back to master");
            return DataSourceType.MASTER.name();
        }
        for (int i = 0; i < replicaDataSources.size(); i++) {
            int index = replicaCounter.getAndIncrement() % replicaDataSources.size();
            String replicaKey = DataSourceType.REPLICA.name() + "_" + index;

            if (isDataSourceHealthy(replicaDataSources.get(index))) {
                return replicaKey;
            }
            log.warn("Replica {} is unhealthy, trying next replica", replicaKey);
        }

        logger.error("All replica datasources are unhealthy, falling back to master");
        return DataSourceType.MASTER.name();


    }

    private boolean isDataSourceHealthy(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1);
        } catch (SQLException e) {
            logger.error("Health check failed for replica datasource", e);
            return false;
        }
    }
}
