package com.atguigu.springcloud.service.impl;

import cn.hutool.core.net.NetUtil;
import com.atguigu.springcloud.dao.OrderDao;
import com.atguigu.springcloud.domain.Order;
import com.atguigu.springcloud.service.AccountService;
import com.atguigu.springcloud.service.OrderService;
import com.atguigu.springcloud.service.StorageService;
import com.atguigu.springcloud.util.IdGeneratorSnowflake;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private IdGeneratorSnowflake snowflake;

    /**
     * 创建订单
     * 下订单
     *  调库存,减库存数量
     *  调账户,减账户额度
     *  改订单状态
     * @param order
     */
//    fsp-create-order  全局事务id,随便起,只要不唯一
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    @Override
    public void create(Order order) {
        log.info("-------> 新建订单");
        orderDao.create(order);

        log.info(">>>>>>>>>>> 订单微服务开始调用库存, 做扣减Count, begin");
        storageService.decrease(order.getProductId(),order.getCount());
        log.info(">>>>>>>>>>> 订单微服务完成调用库存, 做扣减Count, end");

        log.info(">>>>>>>>>>> 账户微服务开始调用账户, 做扣减Money, begin");
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info(">>>>>>>>>>> 账户微服务完成调用账户, 做扣减Money, end");

        log.info(">>>>>>>>>>>  修改订单状态开始");
        orderDao.update(order.getUserId(),0);
        log.info(">>>>>>>>>>>  修改订单状态结束");

        log.info(">>>>>>>>>>>  下订单结束!");
    }

    @Override
    public String snowflake() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
//        20个请求,一共五个线程池去处理
        for (int i = 0; i < 20; i++) {
            threadPool.submit(()->{
                System.out.println(snowflake.snowflakeId());
            });
        }
        threadPool.shutdown();
        return "hello";
    }
}
