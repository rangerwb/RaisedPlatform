package com.atguigu.crowd;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

public class StringTest {

    @Test
    public void testMd5() {
        String source = "123123";
        String encoded = CrowdUtil.md5(source);
        System.out.println(encoded);
    }

}
