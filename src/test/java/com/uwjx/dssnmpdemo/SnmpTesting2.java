package com.uwjx.dssnmpdemo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

@Slf4j
public class SnmpTesting2 {

    public static final String ipAddress = "47.103.62.118";

    public static final int port = 162;

    public static void main(String[] args) {
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();
            Snmp snmp = new Snmp(transport);
            String sendMsgList = "{\"name\":\"王欢\",\"age\",18}";
            PDU pdu = new PDU();
            VariableBinding v = new VariableBinding();
            v.setOid(SnmpConstants.sysName);
            v.setVariable(new OctetString(sendMsgList));
            pdu.add(v);
//            pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, SnmpConstants.sysName));
//            pdu.add(new VariableBinding(SnmpConstants.sysName, new OctetString("test msg by wanghuan")));
            pdu.setType(PDU.TRAP);
// set target
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public"));
            target.setAddress(new UdpAddress(ipAddress + "/" + port));
// retry times when commuication error
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version2c);
// send pdu, return response
             snmp.send(pdu, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
