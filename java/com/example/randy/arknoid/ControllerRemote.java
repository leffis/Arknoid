package com.example.randy.arknoid;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.view.MotionEvent;

import android.widget.PopupWindow;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.NetworkInterface;

import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* Modified
    * Java
    *   Model.java
    *   ControllerRemote.java
    * AndroidManifest.xml
 */

public class ControllerRemote implements View.OnTouchListener, TimerObserver {
    /* Constants */
    private final Boolean sos = true;
    private final Integer touchRadius = 14;
    private final Pair<Double,Double> zeroPair = new Pair<>((double)0,(double)0);
    private final Pair<Double,Double> flipPair = new Pair<>((double)-1,(double)0);

    /* Remote Connection Establish Support */
    private ConnectionContent connectionContent = null;
    private View connectionView = null;
    private PopupWindow connectionMaker = null;
    private ServerSocket serverSocket = null;

    /* Remote Connection Support General */
    private Socket connectionSocket = null;
    private BufferedReader inFromRemote = null;
    private DataOutputStream outToRemote = null;
    private Pair<Integer,String> localPortIp = new Pair<>(-1,"");
    private Pair<Integer,String> remotePortIp = new Pair<>(-1,"");

    /* External Metadata */
    private Model model;

    /* Internal Metadata */
    private int localPadID; // bot = 1, top = 2

    /* Bot Paddle Support */
    private final Float p1Bound = (float)3/4;
    private Float p1Dest = null;
    private Boolean p1DestDirty = false;

    /* Top Paddle Support */
    private final Float p2Bound = (float)1/4;
    private Float p2Dest = null;
    private Boolean p2DestDirty = false;

    private class ConnectionContent extends LinearLayout {
        public ConnectionContent(AppCompatActivity mainActivity) {
            super(mainActivity);
        }
    }

    private void makePopWindow() {
        connectionView = new View(model.getMainActivity());



        connectionMaker = new PopupWindow(model.getMainActivity());
    }

    private String int2ip(int n) {
        String retval = "";

        for (int i = 0; i < 32; i += 8) {
            retval += ((n >> i) & 0xff);
            if (i < 24) retval += ".";
        }

        return retval;
    }

    private String getIp() {
        if (localPortIp.first != -1)
            return localPortIp.second;
        else
            return getIpForced();
    }

    private String getIpForced() {
        int intip;
        WifiManager wifiManager;
        WifiInfo wifiInfo;
        Enumeration<NetworkInterface> nif_it;
        NetworkInterface nif;
        Enumeration<InetAddress> ia_it;
        InetAddress ia;

        if (sos) Log.d("ControllerRemote","getIpForced()");

        /* Create Wifi Manager */
        wifiManager = (WifiManager)(model.getMainActivity().getSystemService(Context.WIFI_SERVICE));

        /* Check if using Wifi */
        if (wifiManager.isWifiEnabled()) { // we are usin wifi

            if (sos) Log.d("ControllerRemote","getIpForced()::wifi");

            wifiInfo = wifiManager.getConnectionInfo();
            intip = wifiInfo.getIpAddress();

            return int2ip(intip);

        } else { // we are usin GPRs
            if (sos) Log.d("ControllerRemote","getIpForced()::GPRs");

            try {
                for (nif_it = NetworkInterface.getNetworkInterfaces(); nif_it.hasMoreElements();) {

                    nif = nif_it.nextElement();

                    for (ia_it = nif.getInetAddresses(); ia_it.hasMoreElements();) {
                        ia = ia_it.nextElement();
                        if (!ia.isLoopbackAddress()) return ia.getHostAddress().toString();
                    }

                }
            } catch (Exception e) {
                Log.d("ControllerRemote","getIpForced()::GPRs: ERROR! Extract ip failed!");
            }
        }

        /* Should not reach here */
        Log.d("ControllerRemote","getIpForced(): ERROR! Should not reach here!");
        return null;
    }

    private void makeServerSocket() {

        /* Create ServerSocket */
        try {
            serverSocket = new ServerSocket(0);
        } catch (Exception e) {
            Log.d("ControllerRemote","makeServerSocket(): ERROR! Create ServerSocket Fail! " + e.getMessage());
        }

        /* Update Local Data */
        localPortIp.first = serverSocket.getLocalPort();
        localPortIp.second = getIpForced();

        /* Trace Case */
        if (sos) Log.d("ControllerRemote","ControllerRemote::makeServerSocket()::localPortIp = "+localPortIp.toString());
    }

    public ControllerRemote(Model _model) {
        super();

        Log.d("ControllerRemote","ControllerRemote::ControllerRemote(Model)");

        model = _model;

        makeServerSocket();

        System.exit(1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent m) {
        return true;
    }

    @Override
    public void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems) {
        return;
    }

    @Override
    public String getIdentifier() { return "ControllerRemote"; }
}
