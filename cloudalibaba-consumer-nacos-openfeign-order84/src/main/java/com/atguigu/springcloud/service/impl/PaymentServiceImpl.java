package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements PaymentService {
    @Override
    public CommonResult<Payment> getPayment(Long id) {
        return new CommonResult<>(444,"服务降级返回!---PaymentServiceImpl",new Payment(id,"errorSerial"));
    }
}
