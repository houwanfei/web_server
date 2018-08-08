package com.hou.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpWebServer {
    private static final String SHUTDOWN_COMMAND = "shutdown";

    private boolean shutdown = false;
    public void await(){
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (shutdown){
            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;

            try {
                socket = serverSocket.accept();
                in = socket.getInputStream();
                out = socket.getOutputStream();

                Request request = new Request(in);
                request.parse();

                Response response = new Response(out);
                response.setRequest(request);

                String uri = request.getUri();
                if (uri.startsWith("/servlet/")){
                    ServletProcessOne servletProcessOne = new ServletProcessOne();
                    servletProcessOne.process(request, response);
                } else {
                    StaticResourcesProcessor staticResourcesProcessor = new StaticResourcesProcessor();
                    staticResourcesProcessor.process(request, response);
                }
                socket.close();
                shutdown = uri.equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new HttpWebServer().await();
    }
}
