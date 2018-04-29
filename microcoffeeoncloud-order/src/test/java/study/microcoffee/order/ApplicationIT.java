package study.microcoffee.order;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.netflix.hystrix.Hystrix;

/**
 * Test of loading of application context.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class ApplicationIT {

    @AfterClass
    public static void destroyOnce() throws Exception {
        // Reset Hystrix to avoid IllegalStateException when later tests tries to call HystrixPlugins.registerCommandExecutionHook.
        Hystrix.reset();
    }

    @Test
    public void applicationContextShouldLoad() {

    }
}
