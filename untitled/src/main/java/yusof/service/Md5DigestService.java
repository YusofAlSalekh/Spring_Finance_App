package yusof.service;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class Md5DigestService implements DigestService {
    @Override
    public String hex(String str) {
        return md5Hex(str);
    }
}
