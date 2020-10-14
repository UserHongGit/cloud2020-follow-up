package com.atguigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.alibaba.handler.CustomerBlockHandler;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")   //可以通过这里的resource来定义Sentinel流控规则
    public CommonResult byResource(){
        return new CommonResult(200,"按资源名称限流OK",new Payment(100L,"serial001"));
    }
    public CommonResult handleException(BlockException e){
        return new CommonResult(444,e.getClass().getCanonicalName()+"服务不可用");
    }


    @GetMapping("/rateLimit/byUrl")     //也可以用这里的url来定义Sentinel流控规则
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl(){
        return new CommonResult(200,"按url限流测试OK",new Payment(200L,"serial002"));
    }

//    -------上述的方式又和Hystrix一样了, 造成代码膨胀,每一个方法都需要一个对应的兜底方法----------------------------------------

    @GetMapping("/rateLimit/consumerBlockHandler")     //也可以用这里的url来定义Sentinel流控规则
    @SentinelResource(value = "consumerBlockHandler"
            ,blockHandlerClass = CustomerBlockHandler.class
            ,blockHandler = "handlerException2")
    public CommonResult consumerBlockHandler(){
        return new CommonResult(200,"按客户自定义统一限流测试OK",new Payment(300L,"serial003"));
    }


}
