package com.socialNetwork.server.auth.email;

import com.socialNetwork.server.auth.entity.VerificationCode;
import com.socialNetwork.server.auth.utils.Constants;
import com.socialNetwork.server.auth.utils.TempCodePurpose;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationCodeService {

    private Cache codeCache;
    private Random random;

    public VerificationCodeService(CacheManager cacheManager) {
        this.codeCache = cacheManager.getCache("verificationCodes");
        this.random= new Random();

    }

    public VerificationCode crateLoginCode(String email){
        return createCode(email, TempCodePurpose.LOGIN_PURPOSE);
    }

    public VerificationCode crateRegisterCode(String email){
        return createCode(email, TempCodePurpose.REGISTER_PURPOSE);
    }

    public boolean ifCanUseLoginCode(String email, String code){
        return ifCanUseCode(TempCodePurpose.LOGIN_PURPOSE , email, code );
    }

    public boolean ifCanUseRegisterCode(String email, String code){
        return ifCanUseCode(TempCodePurpose.REGISTER_PURPOSE , email, code );
    }

    private VerificationCode createCode(String email , TempCodePurpose purpose){
        VerificationCode verificationCode = new VerificationCode(purpose,email, generateCode());
        String key = buildKey(purpose, email);
        this.codeCache.put(key, verificationCode);
        return verificationCode;
    }

    private String generateCode() {
        int number = 100000 + random.nextInt(899999);
        return String.valueOf(number);
    }
    private String buildKey(TempCodePurpose purpose, String email) {
        return purpose + ":" + normalizeEmail(email);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private boolean ifCanUseCode(TempCodePurpose purpose, String email, String code){
        code= code.trim();
        boolean ifCanUseCode = false;
        VerificationCode codeFromCache = getCodeFromCache(purpose, email);
        if (codeFromCache==null){return false;}
            if(codeFromCache.getCode().equals(code)){
                if (!ifBAN(codeFromCache)){
                    ifCanUseCode= true;
                    removeCode(purpose, email);
                }
                else {removeCode(purpose, email);}
        }
            else {codeFromCache.addAttempt();
                saveUpdatedCode(codeFromCache);
                removeIfBAN(codeFromCache);
            }
        return ifCanUseCode;
    }

    private void removeIfBAN(VerificationCode codeFromCache){
        if(ifBAN(codeFromCache)){
            removeCode(codeFromCache.getPurpose(),codeFromCache.getEmail());
        }
    }
    private boolean ifBAN(VerificationCode codeFromCache){
        return  codeFromCache.getAttempts() > Constants.CODE_MAX_ATTEMPT;
    }
    private VerificationCode getCodeFromCache(TempCodePurpose purpose, String email) {
        String key = buildKey(purpose, email);
        Cache.ValueWrapper wrapper = codeCache.get(key);
        if (wrapper == null) {
            return null;
        }
        return (VerificationCode) wrapper.get();
    }

    private void saveUpdatedCode(VerificationCode verificationCode) {
        String key = buildKey(verificationCode.getPurpose(), verificationCode.getEmail());
        codeCache.put(key, verificationCode);
    }

    private void removeCode(TempCodePurpose purpose, String email) {
        String key = buildKey(purpose, email);
        codeCache.evict(key);
    }








}