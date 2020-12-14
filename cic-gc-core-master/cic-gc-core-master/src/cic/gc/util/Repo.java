/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.util;

import cic.gc.tcp.MBTcpServer;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 *
 * @author prera
 */
public class Repo {

    private static Repo repo;

    private Repo() {

    }

    public static Repo create() {
        if (repo == null) {
            repo = new Repo();
        }
        return repo;
    }

    SimpleProcessImage spi = new SimpleProcessImage();

    public SimpleProcessImage getSpi() {
        if (spi.getRegisterCount() == 0) {
            for (int i = 0; i < 17; i++) {
                //spi.addRegister(new TcpRegister(null));
                spi.addRegister(new SimpleRegister(i));
            }
        }
        return spi;
    }

    public void setSpi(SimpleProcessImage spi) {
        this.spi = spi;
    }

    MBTcpServer tcpServer = new MBTcpServer();

    public MBTcpServer getTcpServer(String ipPath) {

        //    tcpServer = new MBTcpServer();
        tcpServer.init(ipPath);

        return tcpServer;
    }

    public void setTcpServer(MBTcpServer tcpServer) {
        this.tcpServer = tcpServer;
    }

}
