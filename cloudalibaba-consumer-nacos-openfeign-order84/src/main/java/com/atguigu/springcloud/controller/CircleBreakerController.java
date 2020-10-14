package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * fallback管运行异常,        >> Java代码运行出错, 才起作用
 * blockHandler管配置违规    >>    当Sentinel配置违规了, 才起作用
 */
@RestController
@Slf4j
public class CircleBreakerController {

    @Resource
    private PaymentService paymentService;

    @GetMapping("/consumer/openfeign/paymentSQL/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return  paymentService.getPayment(id);
    }




}
