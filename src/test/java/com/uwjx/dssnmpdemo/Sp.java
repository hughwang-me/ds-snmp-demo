package com.uwjx.dssnmpdemo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sp {

    public static void main(String[] args) {

//        int width = 4096;
//        int height = 2160;

        int width = 1920;
        int height = 1080;

        int fps = 60;
        int colorCount = 3;
        int color = 8;
        long sum = (long) width * height * colorCount * color * fps;
        double result_b = sum/8.0;
        double result_kb = result_b/1024.0;
        double result_mb = result_kb/1024.0;
        double result_gb = result_mb/1024.0;
        double result = result_gb*1.1;

        double h256_from = result/1000;
        double h256_to = result/350;

        log.info("sum {}" , sum);
        log.info("result_b {}" , result_b);
        log.info("result_kb {}" , result_kb);
        log.info("result_mb {}" , result_mb);
        log.info("result_gb {}" , result_gb);
        log.info("result {}" , result);
        log.info("h256_from {}" , h256_from*1024);
        log.info("h256_to {}" , h256_to*1024);
    }
}
