package com.uwjx.dssnmpdemo;


import java.util.Date;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class TrapSenderVersion2 {
    //snmptrap -v 2c -c public 47.103.62.118:1622 "aaa" .1.3.6.1.2.1.1.5.0 .1.3.6 s "test data aaaaaaaaaaa."
    //snmptrap -v 2c -c public localhost:1622 "aaa" .1.3.6.1.2.1.1.5.0 .1.3.6 s "test data ."

    public static final String community = "public";
    // Sending Trap for sysLocation of RFC1213
    public static final String Oid = ".1.3.6.1.2.1.1.8";
    //IP of Local Host
//    public static final String ipAddress = "localhost";
    public static final String ipAddress = "47.103.62.118";
    //Ideally Port 162 should be used to send receive Trap, any other available Port can be used
    public static final int port = 162;

    public static final String msg = "{\"name\":\"王欢\",\"age\":18}";

    public static void main(String[] args) {
        TrapSenderVersion2 trapV2 = new TrapSenderVersion2();
        trapV2.sendTrap_Version2();
    }

    /**
     * This methods sends the V1 trap to the Localhost in port 162
     */
    public void sendTrap_Version2() {
        try {
            // Create Transport Mapping
            TransportMapping transport = new DefaultUdpTransportMapping();
            transport.listen();

            // Create Target
            CommunityTarget cTarget = new CommunityTarget();
            cTarget.setCommunity(new OctetString(community));
            cTarget.setVersion(SnmpConstants.version2c);
            cTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
            cTarget.setRetries(2);
            cTarget.setTimeout(5000);

            // Create PDU for V2
            PDU pdu = new PDU();

            // need to specify the system up time
            pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                    new OctetString(new Date().toString())));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(
                    Oid)));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
                    new IpAddress(ipAddress)));

            pdu.add(new VariableBinding(new OID(Oid), new OctetString(msg)));
            pdu.setType(PDU.NOTIFICATION);

            // Send the PDU
            Snmp snmp = new Snmp(transport);
            System.out.println("Sending V2 Trap... Check Wheather NMS is Listening or not? ");
            snmp.send(pdu, cTarget);
            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
