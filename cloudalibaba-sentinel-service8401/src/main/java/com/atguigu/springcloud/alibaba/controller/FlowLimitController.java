package com.atguigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() throws InterruptedException {
//        Thread.sleep(1000);
        return "-----testA";
    }
    @GetMapping("/testB")
    public String testB(){
        return "-----testB";
    }

    /**
     * 测试RT,访问量降级熔断
     * @return
     */
    @GetMapping("/testD")
    public String testD(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("testD 测试RT(平均响应时间)");
        return "------testD";
    }

    /**
     *  测试异常比例, 服务降级熔断
     * @return
     */
    @GetMapping("/testE")
    public String testE(){
        log.info("testE 异常比例");
        int age = 10 / 0;
        return "------testE";
    }

    /**
     * 测试异常数,服务降级熔断
     * @return
     */
    @GetMapping("/testF")
    public String testF(){
        log.info("testF 测试异常数");
        int age = 10 / 0;
        return "------testF";
    }

    @GetMapping("/testG")
    @SentinelResource(value = "testG",blockHandler = "deal_testG")
//    一般和上边的GetMapping一样, 但是没有  /
//    如果不加blockHandler指定兜底方法, 页面上就会返回错误信息
    public String testHotkey(
            @RequestParam(value = "p1",required = false) String p1,
            @RequestParam(value = "p2",required = false) String p2
    ){
        int age =  10 /0;
//        注意这里报错了, deal_testG不会执行, 因为Sentinel配置的只是规则,如果是程序的错误,Sentinel是不管的
//        fallback管运行异常, blockHandler配置违规
        return "------testG";
    }

    public String deal_testG(String p1, String p2, BlockException e){
        return "处理违反热点规则的方法!!!!"; //系统默认提示为: Blocked by Sentinel(flow limiting)
    }










}
