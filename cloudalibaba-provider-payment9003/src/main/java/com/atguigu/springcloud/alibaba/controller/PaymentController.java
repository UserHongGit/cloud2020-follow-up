package com.atguigu.springcloud.alibaba.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class PaymentController {
    @Value("${server.port}")
    private String serverPort;

//    模拟数据库
    public static HashMap<Long,Payment> hashMap = new HashMap<Long, Payment>();
    static {
        hashMap.put(1L,new Payment(1L,"sdfwreg54rege5r4g5e45aaaa"));
        hashMap.put(2L,new Payment(2L,"sdfgwre546hb6t43w6xdcebbb"));
        hashMap.put(3L,new Payment(3L,"sdfwreg54435e6wdf5e4ccccc"));
    }

    @GetMapping("/paymentSQL/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        Payment payment = hashMap.get(id);
        CommonResult<Payment> result = new CommonResult<>(200, "from mysql,serverPort" + serverPort, payment);
        return result;
    }


}
