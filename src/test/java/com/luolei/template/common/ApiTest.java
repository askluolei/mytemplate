package com.luolei.template.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.common.api.R;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/12 22:52
 */
public class ApiTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testResult() throws JsonProcessingException {
        R r = R.ok();
        String json = objectMapper.writeValueAsString(r);
        assertThat(json).contains("success");
    }
}
