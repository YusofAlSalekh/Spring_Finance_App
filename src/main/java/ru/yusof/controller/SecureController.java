package ru.yusof.controller;

public interface SecureController<REQ, RES> {
    RES handle(REQ request, Integer userId);

    Class<REQ> getRequestClass();
}
