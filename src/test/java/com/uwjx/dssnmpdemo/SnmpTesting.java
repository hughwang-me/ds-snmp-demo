package com.uwjx.dssnmpdemo;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

@Slf4j
public class SnmpTesting {

    public static void main(String[] args) {
        //Snmp的三个版本号
        //int ver3 = SnmpConstants.version3;
        int ver2c = SnmpConstants.version2c;
        //int ver1 = SnmpConstants.version1;
        Snmp snmp = null;
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
             snmp = new Snmp(transport);
//            if (ver2c == SnmpConstants.version3) {
//                // 设置安全模式
//                USM usm = new USM(SecurityProtocols.getInstance(),new OctetString(MPv3.createLocalEngineID()), 0);
//                SecurityModels.getInstance().addSecurityModel(usm);
//            }

            // 开始监听消息
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 构造报文
        PDU pdu = new PDU();
        //PDU pdu = new ScopedPDU();
        // 设置要获取的对象ID，这个OID代表远程计算机的名称
        OID oids = new OID("1.3.6.1.2.1.1.5.0");
        pdu.add(new VariableBinding(oids));
        // 设置报文类型
        pdu.setType(PDU.GET);
        //((ScopedPDU) pdu).setContextName(new OctetString("priv"));
        try {
            // 发送消息 其中最后一个是想要发送的目标地址
            //manager.sendMessage(false, true, pdu, "udp:192.168.1.229/161");//192.168.1.229 Linux服务器
//            sendMessage(false, true, pdu, "udp:192.168.1.233/161");//192.168.1.233 WinServer2008服务器
            String addr = "udp:47.103.62.118/162";
            // 生成目标地址对象
            Address targetAddress = GenericAddress.parse(addr);
            Target target = null;
            target = new CommunityTarget();
            target.setVersion(SnmpConstants.version2c);
            ((CommunityTarget) target).setCommunity(new OctetString("wanghuan-aly"));
            // 目标对象相关设置
            target.setAddress(targetAddress);
            target.setRetries(5);
            target.setTimeout(1000);

//            if (!syn) {
                // 发送报文 并且接受响应
                ResponseEvent response = snmp.send(pdu, target);
                // 处理响应
                System.out.println("Synchronize(同步) message(消息) from(来自) "
                        + response.getPeerAddress() + "\r\n"+"request(发送的请求):"
                        + response.getRequest() + "\r\n"+"response(返回的响应):"
                        + response.getResponse());
                /**
                 * 输出结果：
                 * Synchronize(同步) message(消息) from(来自) 192.168.1.233/161
                 request(发送的请求):GET[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = Null]]
                 response(返回的响应):RESPONSE[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = WIN-667H6TS3U37]]

                 */
//            } else {
//                // 设置监听对象
//                ResponseListener listener = new ResponseListener() {
//
//                    public void onResponse(ResponseEvent event) {
//                        if (bro.equals(false)) {
//                            ((Snmp) event.getSource()).cancel(event.getRequest(),this);
//                        }
//                        // 处理响应
//                        PDU request = event.getRequest();
//                        PDU response = event.getResponse();
//                        System.out.println("Asynchronise(异步) message(消息) from(来自) "
//                                + event.getPeerAddress() + "\r\n"+"request(发送的请求):" + request
//                                + "\r\n"+"response(返回的响应):" + response);
//                    }
//
//                };
//                // 发送报文
//                snmp.send(pdu, target, null, listener);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Boolean syn, final Boolean bro, PDU pdu, String addr)
            throws IOException {

    }
}
