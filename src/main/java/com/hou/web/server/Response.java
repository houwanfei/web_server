package com.hou.web.server;

import java.io.OutputStream;

public class Response {
    private OutputStream out;
    private Request request;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void setRequest(Request request){
        this.request = request;
    }
}
