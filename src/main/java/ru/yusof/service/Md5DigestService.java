package ru.yusof.service;

import org.springframework.stereotype.Service;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Service
public class Md5DigestService implements DigestService {

    @Override
    public String hex(String str) {
        return md5Hex(str);
    }
}