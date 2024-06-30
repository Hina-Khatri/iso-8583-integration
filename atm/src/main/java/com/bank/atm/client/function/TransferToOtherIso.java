package com.bank.atm.client.function;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferToOtherIso {
    private ISOMsg isoMsg;
    private InputStream is;
    private GenericPackager packager;
    private byte[] result = new byte[0];

    public String buildIsoMessageInquiry(String rekening, String pin, String virtualAccount, String bankCode,int amount, String server, int port){
        try {
            // Load package from resources directory.
            is = getClass().getResourceAsStream("/fields.xml");
            packager = new GenericPackager(is);

            isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");
            isoMsg.set (1,"0000000000000000");
            isoMsg.set (2,rekening);
            isoMsg.set(3, "390000");
            isoMsg.set(4, amount+"");
            isoMsg.set(7, new SimpleDateFormat ("MMddHHmmss").format(new Date ()));
            isoMsg.set(11, "000000");
            isoMsg.set (12, new SimpleDateFormat ("hhmmss").format(new Date ()));
            isoMsg.set (13, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set (15, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set(18,"0000");
            isoMsg.set(32,"00000000000");
            isoMsg.set(33,"00000000000");
            isoMsg.set (37,"000000000000");
            isoMsg.set (41, "00000000");
            isoMsg.set(43,"0000000000000000000000000000000000000000");
            isoMsg.set (48,server+port);
            isoMsg.set (49,"000");
            isoMsg.set (62, virtualAccount);
            isoMsg.set (100,"0314");
            isoMsg.set (102,"0");
            isoMsg.set (103,"0");
            isoMsg.set (125,"0");
            isoMsg.set (127,bankCode);
            isoMsg.set (52, pin);
            result= isoMsg.pack();
        } catch (ISOException e) {
            System.out.println ("Error: "+e.getMessage ());
        }
        return new String(result);
    }

    public String buildIsoMessageTransfer(String rekening, String virtualAccount, String bankCode, int amount,String server, int port){
        try {
            // Load package from resources directory.
            is = getClass().getResourceAsStream("/fields.xml");
            packager = new GenericPackager(is);

            isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");
            isoMsg.set (1,"0000000000000000");
            isoMsg.set (2,rekening);
            isoMsg.set(3, "400000");
            isoMsg.set(4, amount+"");
            isoMsg.set(7, new SimpleDateFormat ("MMddHHmmss").format(new Date ()));
            isoMsg.set(11, "000000");
            isoMsg.set (12, new SimpleDateFormat ("hhmmss").format(new Date ()));
            isoMsg.set (13, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set (15, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set(18,"0000");
            isoMsg.set(32,"00000000000");
            isoMsg.set(33,"0");
            isoMsg.set (37,"000000000000");
            isoMsg.set (41, "00000000");
            isoMsg.set (42,"000000000000000");
            isoMsg.set(43,"0000000000000000000000000000000000000000");
            isoMsg.set (48,server+port);
            isoMsg.set (49,"000");
            isoMsg.set (62, virtualAccount);
            isoMsg.set (100,"0314");
            isoMsg.set (102,"0");
            isoMsg.set (103,"0");
            isoMsg.set (125,"0");
            isoMsg.set (127,bankCode);
            isoMsg.set (52, "0");
            result= isoMsg.pack();
        } catch (ISOException e) {
            System.out.println ("Error: "+e.getMessage ());
        }
        return new String(result);
    }

    public String responseIsoMessageInquiry(String statusCode, String name, String bankCode, int balanced){
        try {
            // Load package from resources directory.
            is = getClass().getResourceAsStream("/fields.xml");
            packager = new GenericPackager(is);

            isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");
            isoMsg.set (1,"0000000000000000");
            isoMsg.set(3, "390000");
            isoMsg.set(4, balanced+"");
            isoMsg.set(7, new SimpleDateFormat ("MMddHHmmss").format(new Date ()));
            isoMsg.set(11, "000000");
            isoMsg.set (12, new SimpleDateFormat ("hhmmss").format(new Date ()));
            isoMsg.set (13, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set (15, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set(18,"0000");
            isoMsg.set(32,"00000000000");
            isoMsg.set(33,"00000000000");
            isoMsg.set (37,"000000000000");
            isoMsg.set (39, statusCode);
            isoMsg.set (41, "00000000");
            isoMsg.set (42,"000000000000000");
            isoMsg.set(43,"0000000000000000000000000000000000000000");
            isoMsg.set (48,"0");
            isoMsg.set (49,"000");
            isoMsg.set (62, balanced+"");
            isoMsg.set (100,"0314");
            isoMsg.set (102,"0");
            isoMsg.set (103,name);
            isoMsg.set (125,"0");
            isoMsg.set (127,bankCode);
            DecodeIso decodeIso = new DecodeIso ();
            decodeIso.printISOMessage(isoMsg);
            result= isoMsg.pack();
            System.out.println ("ISO Message from Server: "+ new String(result));
        } catch (ISOException e) {
            System.out.println ("Error: "+e.getMessage ());
        }
        return new String(result);
    }

    public String buildResponseMessage(String rekening, String statusCode, String bankCode, int balanced){
        try {
            // Load package from resources directory.
            is = getClass().getResourceAsStream("/fields.xml");
            packager = new GenericPackager(is);

            isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");
            isoMsg.set (1,"0000000000000000");
            isoMsg.set (2,rekening);
            isoMsg.set(3, "400000");
            isoMsg.set(4, "000000000000");
            isoMsg.set(7, new SimpleDateFormat ("MMddHHmmss").format(new Date ()));
            isoMsg.set(11, "000000");
            isoMsg.set (12, new SimpleDateFormat ("hhmmss").format(new Date ()));
            isoMsg.set (13, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set (15, new SimpleDateFormat ("MMdd").format(new Date ()));
            isoMsg.set(18,"0000");
            isoMsg.set(32,"00000000000");
            isoMsg.set(33,"00000000000");
            isoMsg.set (37,"000000000000");
            isoMsg.set (39, statusCode);
            isoMsg.set (41, "00000000");
            isoMsg.set (42,"000000000000000");
            isoMsg.set(43,"0000000000000000000000000000000000000000");
            isoMsg.set (48,"0");
            isoMsg.set (49,"000");
            isoMsg.set (62, balanced+"");
            isoMsg.set (100,"0314");
            isoMsg.set (102,"0");
            isoMsg.set (103,"0");
            isoMsg.set (125,"0");
            isoMsg.set (127, bankCode);

            DecodeIso decodeIso = new DecodeIso ();
            decodeIso.printISOMessage(isoMsg);
            result= isoMsg.pack();
            System.out.println ("ISO Message from Server: "+ new String(result));
        } catch (ISOException e) {
            System.out.println ("Error: "+e.getMessage ());
        }
        return new String(result);
    }
}
