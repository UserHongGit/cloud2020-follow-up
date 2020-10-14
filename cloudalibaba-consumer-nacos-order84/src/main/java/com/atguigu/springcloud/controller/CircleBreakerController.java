package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
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
    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;


//  示例:  fallback java代码出错
    @RequestMapping("/consumer/fallback/{id}")
//    @SentinelResource(value="fallback")  //不配置兜底方法就直接返回错误页面
    @SentinelResource(value="fallback",fallback = "handlerFallback")   //运行兜底方法
    public CommonResult<Payment> fallback(@PathVariable Long id){
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL+"/paymentSQL/"+id,CommonResult.class,id);
        if(id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException,参数非法!");
        }else if(result.getData() == null) {
            throw new NullPointerException("NullPointerException,没有对应id的数据!");
        }
        return result;
    }
    public CommonResult handlerFallback(@PathVariable Long id,Throwable e){
        Payment payment = new Payment(id,"null");
        return  new CommonResult(444,"兜底异常handleFallback,Exception内容: "+e.getMessage(),payment);
    }



//  示例:  blockHandler 配置违规出错
    @RequestMapping("/consumer/fallback2/{id}")
    @SentinelResource(value="fallback2",blockHandler = "blockHandler")   //运行兜底方法
    public CommonResult<Payment> fallback2(@PathVariable Long id){
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL+"/paymentSQL/"+id,CommonResult.class,id);
        if(id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException,参数非法!");
        }else if(result.getData() == null) {
            throw new NullPointerException("NullPointerException,没有对应id的数据!");
        }
        return result;
    }
    public CommonResult blockHandler(@PathVariable Long id, BlockException e){
        Payment payment = new Payment(id,"null");
        return  new CommonResult(555,"blockHandler-sentinel限流,没有对应id的数据,BlockException: "+e.getMessage(),payment);
    }


//  示例: blockHandler,fallback     java代码出错和配置违规出错  都配置
    @RequestMapping("/consumer/fallback3/{id}")
    @SentinelResource(value="fallback3",fallback = "handlerFallback",blockHandler = "blockHandler")   //运行兜底方法
    public CommonResult<Payment> fallback3(@PathVariable Long id){
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL+"/paymentSQL/"+id,CommonResult.class,id);
        if(id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException,参数非法!");
        }else if(result.getData() == null) {
            throw new NullPointerException("NullPointerException,没有对应id的数据!");
        }
        return result;
    }



//  示例: blockHandler,fallback都配置, 但是忽略IllegalArgumentException异常     java代码出错和配置违规出错  都配置
    @RequestMapping("/consumer/fallback4/{id}")
    @SentinelResource(value="fallback4"
            ,fallback = "handlerFallback"
            ,blockHandler = "blockHandler"
            ,exceptionsToIgnore = {IllegalArgumentException.class}
    )
    public CommonResult<Payment> fallback4(@PathVariable Long id){
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL+"/paymentSQL/"+id,CommonResult.class,id);
        if(id == 4) {
            throw new IllegalArgumentException("IllegalArgumentException,参数非法!");
        }else if(result.getData() == null) {
            throw new NullPointerException("NullPointerException,没有对应id的数据!");
        }
        return result;
    }
}
