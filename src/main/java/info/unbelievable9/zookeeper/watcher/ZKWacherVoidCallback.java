package info.unbelievable9.zookeeper.watcher;

import org.apache.zookeeper.AsyncCallback;

/**
 * Author      : Unbelievable9
 * Class Name  :
 * Description :
 * Date        : 2019-01-10
 **/
public class ZKWacherVoidCallback implements AsyncCallback.VoidCallback {

    @Override
    public void processResult(int i, String s, Object o) {
        System.out.println(
                "Process Result: [" +
                        "Result Code: " + i + ", " +
                        "Path: " + s + ", " +
                        "Context: " + o + "]"
        );
    }
}
