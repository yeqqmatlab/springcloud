package com.liveinpast.stress.auth.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.liveinpast.stress.auth.config.properties.LoginTokenProperties;
import com.liveinpast.stress.common.*;
import com.liveinpast.stress.common.exception.TokenValidException;
import com.liveinpast.stress.common.helper.AESHelper;
import com.liveinpast.stress.common.helper.MD5Helper;
import com.liveinpast.stress.common.helper.RSAHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author: Live.InPast
 * @date: 2020/4/12
 */
@RestController
@RequestMapping
public class AuthController {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Resource
    private LoginTokenProperties loginTokenProperties;

    /**
     * 校验Jwt Token
     *
     * @return
     */
    @PostMapping("/verify-jwt")
    public StressResult<TokenVerifySuccessResDTO> verifyJwt(@Valid @RequestBody TokenVerifyReqDTO tokenVerifyReqDTO) {
        try {
            //新的密钥,RSA解密
            String decrypt = RSAHelper.privateDecrypt(tokenVerifyReqDTO.getToken(), RSAHelper.getPrivateKey(loginTokenProperties.getRsaPrivateKey()));
            tokenVerifyReqDTO.setToken(decrypt);

            Algorithm algorithm = Algorithm.HMAC256(loginTokenProperties.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(loginTokenProperties.getJwtIssuer()).build();
            DecodedJWT decoded = verifier.verify(tokenVerifyReqDTO.getToken());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Token Valid Success({}).", tokenVerifyReqDTO.getToken());
            }
            //构建解密后的token返回值
            TokenVerifySuccessResDTO tokenVerify = new TokenVerifySuccessResDTO();
            tokenVerify.setServiceGroup(decoded.getClaim("serviceGroup").asString());
            tokenVerify.setClient(decoded.getClaim("client").asInt());
            tokenVerify.setClientName(tokenVerify.getClient().toString());
            tokenVerify.setUserId(decoded.getClaim("userId").asString());
            tokenVerify.setExtraUserId(decoded.getClaim("extraUserId").asString());
            tokenVerify.setOpenId(decoded.getClaim("openId").asString());
            tokenVerify.setUnionId(decoded.getClaim("unionId").asString());
            tokenVerify.setUserName(decoded.getClaim("userName").asString());
            tokenVerify.setSchoolId(decoded.getClaim("schoolId").asInt());

            return StressResult.success().data(tokenVerify);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unsupported Encoding Exception:{}", e);
            throw new TokenValidException();
        } catch (JWTVerificationException e) {
            LOG.error("JWTVerification Exception:{}", e);
            throw new TokenValidException();
        } catch (Exception e) {
            LOG.error("Exception:{}", e);
            throw new TokenValidException();
        }
    }


    /**
     * 校验Jwt Token
     *
     * @return
     */
    @PostMapping("/verify-jwt2")
    public StressResult<TokenVerifySuccessResDTO> verifyJwt2(@Valid @RequestBody TokenVerifyReqDTO tokenVerifyReqDTO) {
        try {
            //新的密钥,RSA解密
//            String decrypt = RSAHelper.privateDecrypt(tokenVerifyReqDTO.getToken(), RSAHelper.getPrivateKey(loginTokenProperties.getRsaPrivateKey()));
            String decrypt = AESHelper.decrypt(tokenVerifyReqDTO.getToken(), "ps8TEYTWcEK6ZMyGuI8uz0BekcSkLOhB");
            tokenVerifyReqDTO.setToken(decrypt);
          /*  TokenVerifySuccessResDTO tokenVerify = new TokenVerifySuccessResDTO();
            tokenVerify.setServiceGroup("aaaa");
            tokenVerify.setClient(1);
            tokenVerify.setClientName("aaaa");
            tokenVerify.setUserId("aaaa");
            tokenVerify.setExtraUserId("aaaa");
            tokenVerify.setOpenId("aaaa");
            tokenVerify.setUnionId("aaaa");
            tokenVerify.setUserName("aaaa");
            tokenVerify.setSchoolId(0);*/
            Algorithm algorithm = Algorithm.HMAC256(loginTokenProperties.getJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(loginTokenProperties.getJwtIssuer()).build();
            DecodedJWT decoded = verifier.verify(tokenVerifyReqDTO.getToken());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Token Valid Success({}).", tokenVerifyReqDTO.getToken());
            }
            //构建解密后的token返回值
            TokenVerifySuccessResDTO tokenVerify = new TokenVerifySuccessResDTO();
            tokenVerify.setServiceGroup(decoded.getClaim("serviceGroup").asString());
            tokenVerify.setClient(decoded.getClaim("client").asInt());
            tokenVerify.setClientName(tokenVerify.getClient().toString());
            tokenVerify.setUserId(decoded.getClaim("userId").asString());
            tokenVerify.setExtraUserId(decoded.getClaim("extraUserId").asString());
            tokenVerify.setOpenId(decoded.getClaim("openId").asString());
            tokenVerify.setUnionId(decoded.getClaim("unionId").asString());
            tokenVerify.setUserName(decoded.getClaim("userName").asString());
            tokenVerify.setSchoolId(decoded.getClaim("schoolId").asInt());
            return StressResult.success().data(tokenVerify);
        } catch (Exception e) {
            LOG.error("Exception:{}", e);
            throw new TokenValidException();
        }
    }


    /**
     * 校验Basic Token
     *
     * @return
     */
    @PostMapping("/verify-basic")
    public StressResult<TokenVerifySuccessResDTO> verifyBasic(@Valid @RequestBody TokenVerifyReqDTO tokenVerifyReqDTO) {
        String basicStr = new String(Base64.getDecoder().decode(tokenVerifyReqDTO.getToken()), StandardCharsets.UTF_8);
        String[] users = basicStr.split(":");
        String secretStr = MD5Helper.convert(users[0] + loginTokenProperties.getBasicUser() + loginTokenProperties.getBasicPassword(), 32, false);
        if (secretStr.equals(users[1])) {
            //验证通过
            if (LOG.isDebugEnabled()) {
                LOG.debug("Token Valid Success({}).", tokenVerifyReqDTO.getToken());
            }
            TokenVerifySuccessResDTO tokenVerifySuccessResDTO = new TokenVerifySuccessResDTO();
            tokenVerifySuccessResDTO.setSchoolId(Integer.parseInt(users[0]));
            tokenVerifySuccessResDTO.setClient(1);
            return StressResult.success().data(tokenVerifySuccessResDTO);
        } else {
            throw new TokenValidException();
        }
    }


}
