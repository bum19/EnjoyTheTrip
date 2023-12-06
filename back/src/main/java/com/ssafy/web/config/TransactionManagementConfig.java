package com.ssafy.web.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConditionalOnClass(PlatformTransactionManager.class)
public class TransactionManagementConfig {

    @Bean(name = "chainedTransactionManager")
    @Primary
    @ConditionalOnBean(PlatformTransactionManager.class)
    public PlatformTransactionManager transactionManager(
            @Qualifier("myTriptransactionManager") PlatformTransactionManager myTripTransactionManager,
            @Qualifier("tripSectransactionManager") PlatformTransactionManager tripSecTransactionManager) {
        return new ChainedTransactionManager(myTripTransactionManager, tripSecTransactionManager);
    }
}