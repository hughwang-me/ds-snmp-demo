package com.uwjx.dssnmpdemo.trap;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TrapReceiver implements CommandResponder {


    public static void main(String[] args) {
        TrapReceiver snmp4jTrapReceiver = new TrapReceiver();
        try {
            snmp4jTrapReceiver.listen(new UdpAddress(162));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Trap Listner
     */
    public synchronized void listen(TransportIpAddress address)
            throws IOException {
        AbstractTransportMapping transport;
        if (address instanceof TcpAddress) {
            log.warn("开启的是TCP");
            transport = new DefaultTcpTransportMapping((TcpAddress) address);
        } else {
            log.warn("开启的是UDP");
            transport = new DefaultUdpTransportMapping((UdpAddress) address);
        }

        ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
        MessageDispatcher mDispathcher = new MultiThreadedMessageDispatcher(
                threadPool, new MessageDispatcherImpl());

        // add message processing models
        mDispathcher.addMessageProcessingModel(new MPv1());
        mDispathcher.addMessageProcessingModel(new MPv2c());

        // add all security protocols
//        SecurityProtocols.getInstance().addDefaultProtocols();
//        SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

        // Create Target
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));

        Snmp snmp = new Snmp(mDispathcher, transport);
        snmp.addCommandResponder(this);

        transport.listen();
        System.out.println("Listening udp 162 on " + address);

        try {
            this.wait();
            log.warn("等待中》。。。");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This method will be called whenever a pdu is received on the given port
     * specified in the listen() method
     */
    @Override
    public synchronized void processPdu(CommandResponderEvent cmdRespEvent) {
        System.out.println("Received PDU...");
        PDU pdu = cmdRespEvent.getPDU();
        if (pdu != null) {
            System.out.println("Trap Type = " + pdu.getType());
            System.out.println("Variables = " + pdu.getVariableBindings());

            pdu.getVariableBindings().stream().forEach(vb -> {
                String msg = vb.getVariable().toString();
                log.warn("OID : {}", vb.getOid());
                log.warn("Variable : {}", vb.getVariable());
                log.warn("msg : {}", getChinese(vb.getVariable().toString()));
            });
        }
    }

    public static String getChinese(String octetString) {    //snmp4j遇到中文直接转成16进制字符串
        try {
            String[] temps = octetString.split(":");
            byte[] bs = new byte[temps.length];
            for (int i = 0; i < temps.length; i++) {
                bs[i] = (byte) Integer.parseInt(temps[i], 16);
            }


            return new String(bs, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

}
