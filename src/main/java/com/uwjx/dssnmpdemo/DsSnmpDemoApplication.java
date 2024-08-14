package com.uwjx.dssnmpdemo;

import com.uwjx.dssnmpdemo.trap.TrapReceiver;
import org.snmp4j.smi.UdpAddress;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DsSnmpDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsSnmpDemoApplication.class, args);
        TrapReceiver.main(new String[0]);
    }

}
