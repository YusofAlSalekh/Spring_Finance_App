package ru.yusof.controller;

public interface Controller<REQ, RES> {
    RES handle(REQ request);

    Class<REQ> getRequestClass();
}
