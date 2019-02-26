package phone.test;

import com.leyou.LySmsModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySmsModule.class)
public class PhoneTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSendMessage() throws InterruptedException {
        Map<String,String> map = new HashMap<>();
        map.put("phone","13625638736");
        map.put("code","XL520");
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code",map);
        Thread.sleep(5000);
    }
}
