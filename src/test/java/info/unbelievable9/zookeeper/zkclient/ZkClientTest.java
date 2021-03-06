package info.unbelievable9.zookeeper.zkclient;

import info.unbelievable9.zookeeper.ZkRootTest;
import info.unbelievable9.zookeeper.util.CommonUtil;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * 第三方客户端 ZkClient 测试
 *
 * @author : unbelievable9
 * @date : 2019-05-08
 */
public class ZkClientTest extends ZkRootTest {

    private static final Logger logger = Logger.getLogger(ZkClientTest.class);

    private static final String ZOO_PATH = "/zoo";

    private static final String PIG_PATH = ZOO_PATH + "/pig";

    private static final String DUCK_PATH = ZOO_PATH + "/duck";

    private ZkClient zkClient;

    @BeforeTest
    @Override
    public void beforeTest() throws IOException, InterruptedException {
        super.beforeTest();

        zkClient = CommonUtil.getZkClient();

        Assert.assertNotNull(zkClient);
    }

    @AfterTest
    @Override
    public void afterTest() throws InterruptedException {
        super.afterTest();

        if (zkClient != null) {
            zkClient.close();
        }
    }

    /**
     * 创建节点测试
     */
    @Test(priority = 1)
    public void createNodeTest() {
        zkClient.createPersistent(PIG_PATH, true);

        Assert.assertTrue(zkClient.exists(PIG_PATH));

        logger.info("递归创建节点成功");
    }

    /**
     * 简单删除节点测试
     */
    @Test(priority = 2)
    public void deteteNodeTest() {
        zkClient.deleteRecursive(ZOO_PATH);

        Assert.assertFalse(zkClient.exists(ZOO_PATH));

        logger.info("逐层删除节点成功");
    }

    /**
     * 节点操作测试
     *
     * @throws InterruptedException 中断异常
     */
    @Test(priority = 3)
    public void nodeOperationTest() throws InterruptedException {
        zkClient.subscribeChildChanges(ZOO_PATH, new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                logger.info("父节点: " + s + " 发生变化, 现有子节点为: " + list);
            }
        });

        zkClient.subscribeDataChanges(PIG_PATH, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                logger.info("节点: " + s + " 信息发生变化, 新信息为: " + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                logger.info("节点: " + s + " 信息被删除");
            }
        });

        zkClient.createPersistent(ZOO_PATH);
        Thread.sleep(500);

        zkClient.createPersistent(PIG_PATH);
        Thread.sleep(500);

        zkClient.createPersistent(DUCK_PATH);
        Thread.sleep(500);

        zkClient.writeData(PIG_PATH, "I'm Peggy.");
        Thread.sleep(500);

        zkClient.delete(PIG_PATH);
        Thread.sleep(500);

        zkClient.delete(DUCK_PATH);
        Thread.sleep(500);

        zkClient.delete(ZOO_PATH);
        Thread.sleep(500);

        Assert.assertFalse(zkClient.exists(ZOO_PATH));
    }
}
