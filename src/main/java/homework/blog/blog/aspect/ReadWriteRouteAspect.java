package homework.blog.blog.aspect;

import homework.blog.blog.config.db.DataSourceContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Aspect
@Component
@Order(0)
public class ReadWriteRouteAspect {

    @Before("@annotation(homework.blog.blog.aspect.ReadWriteRouteAspect.ReadOnlyReplica)")
    public void setReadDataSourceType() {
        DataSourceContextHolder.setReplicaRequired(true);
    }

    @After("@annotation(homework.blog.blog.aspect.ReadWriteRouteAspect.ReadOnlyReplica)")
    public void clearDataSourceType() {
        DataSourceContextHolder.clearReplica();
    }

    @AfterThrowing(pointcut = "@annotation(homework.blog.blog.aspect.ReadWriteRouteAspect.ReadOnlyReplica)", throwing = "ex")
    public void clearAfterException(Throwable ex) {
        DataSourceContextHolder.clearReplica();
    }

    @Before("@annotation(homework.blog.blog.aspect.ReadWriteRouteAspect.ForceMaster)")
    public void setForceMasterDataSourceType() {
        DataSourceContextHolder.setMasterRequired(true);
    }

    @After("@annotation(homework.blog.blog.aspect.ReadWriteRouteAspect.ForceMaster)")
    public void clearForceMasterDataSourceType() {
        DataSourceContextHolder.clearMaster();
    }

    @AfterThrowing(pointcut = "@annotation(homework.blog.blog.aspect.ReadWriteRouteAspect.ForceMaster)", throwing = "ex")
    public void clearForceMasterAfterException(Throwable ex) {
        DataSourceContextHolder.clearMaster();
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ReadOnlyReplica {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ForceMaster {
    }
}
