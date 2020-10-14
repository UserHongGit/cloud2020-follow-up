package com.atguigu.springcloud.alibaba.handler;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;

public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException e) {
        return new CommonResult(555,"统一处理自定义异常  1111------Exception1");
    }
    public static CommonResult handlerException2(BlockException e) {
        return new CommonResult(66666,"统一处理自定义异常  22222------Exception2");
    }


}
