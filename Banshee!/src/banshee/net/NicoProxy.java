package banshee.net;

import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * <p>�^�C�g��: �j�R�j�R����L���b�V���v���L�V�u�΂񂵁[�I�v</p>
 *
 * <p>����: �A���A���킢����A���A</p>
 *
 * <p>���쌠: Copyright (c) 2007 PSI</p>
 *
 * <p>��Ж�: </p>
 *
 * @author ������
 * @version 1.0
 */
public class NicoProxy extends Thread {
    private final Semaphore Sem = new Semaphore(15); //�R�l�N�V��������p�Z�}�t�H
    private final HashMap<String,
                          String> AddressMap = new HashMap<String, String>();
    private final HashMap<String,
                          String> TitleMap = new HashMap<String, String>();
    private final HashMap<File, Integer> StateMap = new HashMap<File, Integer>();
    private final String Proxy;
    private final int ProxyPort;
    private final int Port;
    private final Pattern BannedPattern;
    public NicoProxy(final int port, final String proxy, final int proxy_port,
                     final Pattern pattern) {
        Port = port;
        BannedPattern = pattern;
        if (proxy != null && proxy.equals("")) {
            Proxy = proxy;
            ProxyPort = proxy_port;
        } else {
            Proxy = null;
            ProxyPort = -1;
        }
    }

    public void run() {
        System.out.print("�T�[�o�\�P�b�g���擾���܂��E�E�E");
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(Port);
            Socket sock;
            boolean runnning = true;
            System.out.println("�擾�����B�R�l�N�V�����ҋ@�J�n�B");
            (new File("./cache/")).mkdir();
            while (runnning) {
                sock = socket.accept();
                if (sock == null) {
                    continue;
                }
                Sem.acquire();
                Connection con = new Connection(sock, Sem, AddressMap, TitleMap,
                                                StateMap, Proxy, ProxyPort,
                                                BannedPattern);
                con.start();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }
    }
}
