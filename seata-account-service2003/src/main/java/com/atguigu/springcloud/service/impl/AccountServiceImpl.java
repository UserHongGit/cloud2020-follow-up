package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.dao.AccountDao;
import com.atguigu.springcloud.domain.CommonResult;
import com.atguigu.springcloud.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        LOGGER.info(">>>>>>>>>>> account-service 中扣减账户余额开始!");
        accountDao.decrease(userId,money);
        try {
            TimeUnit.SECONDS.sleep(20);  //故意超时
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info(">>>>>>>>>>> account-service 中扣减账户余额结束!");
    }
}
