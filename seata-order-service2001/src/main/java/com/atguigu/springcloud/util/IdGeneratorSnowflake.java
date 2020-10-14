package com.atguigu.springcloud.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class IdGeneratorSnowflake {
    private long workerId = 0;  //数据工作中心 >>>> 0 - 31
    private long datacenterId = 1;
    private Snowflake snowflake = IdUtil.createSnowflake(workerId,datacenterId);

    @PostConstruct
    public void init(){
        try{
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workId : {} ",workerId);
        }catch (Exception e) {
            e.printStackTrace();
            log.warn("当前机器的workId获取失败! ",e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }

    }

    public synchronized  long snowflakeId(){
        return  snowflake.nextId();
    }


    public synchronized  long snowflakeId(long workerId,long datacenterId){
        Snowflake snowflake = IdUtil.createSnowflake(workerId,datacenterId);
        return  snowflake.nextId();
    }

    public static void main(String[] args) {
        long l = new IdGeneratorSnowflake().snowflakeId();
        System.out.println("雪花算法生成的id: "+l +"    >>>>    "+(l+"").length());
//        System.out.println(Long.toBinaryString(l));
        Date date = new Date(1315919910471335951L);//1315918936205819904
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(date));
        System.out.println("雪花算法还可以使用年数: ");
        System.out.println(2039 - 1970);
        System.out.println(date.getTime());
    }

}
