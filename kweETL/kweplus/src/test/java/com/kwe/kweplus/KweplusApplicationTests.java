package com.kwe.kweplus;

import com.kwe.kweplus.bo.IJintieOutputBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class KweplusApplicationTests {

    @Autowired
    private IJintieOutputBo bo;
    @Test
    public void testXxxx() {
        bo.test();
    }



}
