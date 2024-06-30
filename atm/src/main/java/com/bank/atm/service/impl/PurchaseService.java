package com.bank.atm.service.impl;

import com.bank.atm.client.function.DecodeIso;
import com.bank.atm.client.function.PurchaseIso;
import com.bank.atm.model.Account;
import com.bank.atm.repository.AccountRepository;
import com.bank.atm.service.api.PurchaseQuery;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.net.Socket;

@Service
public class PurchaseService implements PurchaseQuery {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);
    private AccountRepository accountRepository;
    private boolean status;
    private boolean isAccountExist;
    private String message;
    private ISOMsg isoMsg;
    private Socket socket;
    private DecodeIso decodeIso = new DecodeIso ();

    @Autowired
    public PurchaseService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String purchaseInquiry(String msg) {
        isoMsg= decodeIso.parseISOMessage (msg);
        String accountNumber = isoMsg.getString (2);
        String pinNumber = isoMsg.getString (52);
        int amount = Integer.parseInt (isoMsg.getString (4));
        String server = isoMsg.getString (48).substring (0,9);
        int port = Integer.parseInt (isoMsg.getString (48).substring (9));

        Account account = accountRepository.findByAccnumber (accountNumber);
        logger.info ("Fetch data from database by account number {}",accountNumber);
        PurchaseIso purchaseIso = new PurchaseIso ();
        if(account!=null){
            if(accountNumber.equalsIgnoreCase (account.getAccnumber ()) && Integer.parseInt (pinNumber) == Integer.parseInt (account.getAccpin ())){
                if(checkSisaSaldo (accountNumber, amount)){
                    message = purchaseIso.responseIsoMessageInquiry("00", account.getAccbalance ());
                }else {
                    message = purchaseIso.responseIsoMessageInquiry ("51", account.getAccbalance ());
                }
            }else{
                logger.warn ("Fail to authentication");
                message = purchaseIso.responseIsoMessageInquiry ("05", 0);
            }
        }else {
            logger.warn ("Data is empty");
            message = purchaseIso.responseIsoMessageInquiry ("05",  0);
        }
        try{
            socket=new Socket (server,port);
            if(socket.isConnected()){
                logger.info ("Client connected to {} on port {}",socket.getInetAddress(), socket.getPort());
            }else{
                logger.warn ("Failed connect to client");
            }
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(message);
            dout.flush();
            dout.close ();
            socket.close ();
        }catch (Exception e){
            logger.error ("Error : {} in method {}",e.getMessage (), e.getStackTrace ());
        }

        return message;
    }

    @Override
    public String purchase(String msg) {
        isoMsg= decodeIso.parseISOMessage (msg);
        String accountNumber = isoMsg.getString (2);
        int amount = Integer.parseInt (isoMsg.getString (4));
        String server = isoMsg.getString (48).substring (0,9);
        int port = Integer.parseInt (isoMsg.getString (48).substring (9));

        PurchaseIso purchaseIso = new PurchaseIso ();
        Account account = accountRepository.findByAccnumber (accountNumber);
        logger.info ("Fetch data from database by account number {}",accountNumber);
        account.setAccbalance (account.getAccbalance ()-amount);
        accountRepository.save (account);
        logger.info ("Save data successfully");
        Account newAccount = accountRepository.findByAccnumber (accountNumber);
        message = purchaseIso.buildResponseMessage (accountNumber,"00",newAccount.getAccbalance ());

        try{
            socket=new Socket (server,port);
            if(socket.isConnected()){
                logger.info ("Client connected to {} on port {}",socket.getInetAddress(), socket.getPort());
            }else{
                logger.warn ("Failed connect to client");
            }
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(message);
            dout.flush();
            dout.close ();
            socket.close ();
        }catch (Exception e){
            logger.error ("Error : {} in method {}",e.getMessage (), e.getStackTrace ());
        }

        return message;
    }

    private boolean checkSisaSaldo(String accountNumber, int nominal){
        Account account = accountRepository.findByAccnumber (accountNumber);
        logger.info ("Fetch data from database by account number {}",accountNumber);
        if(account.getAccbalance ()<nominal){
            status=false;
            logger.info ("Return false");
        }else {
            logger.info ("Return true");
            status=true;
        }
        return status;
    }
}
