package com.dianxin.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {


    /**
     * 过期时间
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;

    /**
     * 令牌秘钥
     */
    private static final String SECRET = "zhongguodianxin";


    /**
     * 生成token
     * @param userId
     * @return
     */
    public static String createToken (Long userId){

        String jwtToken = Jwts.builder()

                //设置头部信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                //设置主题
                .setSubject("sso")

                //设置Payload
                .claim("userId", userId)

                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                //设置加密
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        return jwtToken;

    }


    /**
     * 判断token是否存在与有效
     * @return
     */
    public static boolean checkToken() {
        String jwtToken = getToken();
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取用户名
     * @return
     */
    public static Long getUserId() {

        String jwtToken = getToken();

        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken);
        }catch (Exception e){
//            e.printStackTrace();
            return null;
        }
        Claims claims = claimsJws.getBody();
        return Long.valueOf(claims.get("userId").toString());
    }


    /**
     * 根据Bearer认证标准从用户请求中获取token <a href=“https://datatracker.ietf.org/doc/html/rfc6750”></a>
     * @return
     */
    public static String getToken()  {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String authorization  = request.getHeader("Authorization");

        String jwtToken = authorization.replace("Bearer ", "");

        System.out.println(jwtToken);

        return jwtToken;

    }





}
