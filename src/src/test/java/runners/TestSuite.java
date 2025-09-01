package runners;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ChromeTestRunner.class,
        EdgeTestRunner.class,
        EdgeIEModeTestRunner.class
})
public class TestSuite {
    // 测试套件类，用于运行所有浏览器测试
}