package top.himcs.vote.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.himcs.vote.repository.SmsCodeRepository;
import top.himcs.vote.validate.code.ImageCode;
import top.himcs.vote.validate.smscode.SmsCode;
import top.himcs.vote.validate.smscode.ValidateTelException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class ValidateController {
    public final static String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) throws IOException {
        ImageCode imageCode = createImageCode();
        httpSession.setAttribute(SESSION_KEY_IMAGE_CODE, imageCode);
        ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
    }


    @GetMapping("/code/sms")
    public Map createSmsCode(HttpServletRequest request, HttpServletResponse response, String mobile) {


        if (mobile == null || mobile.equals("")) {

            throw new ValidateTelException("error mobile");
        }

        SmsCode smsCode = createSMSCode();
        //持久化
        SmsCodeRepository.smsCodeList.put(mobile, smsCode);
        // 输出验证码到控制台代替短信发送服务
        String s = mobile + ":" + smsCode.getCode() + "，有效时间为60秒";
        Map map = new HashMap();
        map.put("tel", mobile);
        map.put("code", smsCode);
        return map;
    }


    private SmsCode createSMSCode() {
        String code = String.format("%04d", new Random().nextInt(10000));
        return new SmsCode(code, 60);
    }

    private ImageCode createImageCode() {

        int width = 100; // 验证码图片宽度
        int height = 36; // 验证码图片长度
        int length = 4; // 验证码位数
        int expireIn = 60; // 验证码有效时间 60s

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), expireIn);
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
