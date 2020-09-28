package fun.langel.datawharf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lonelyangel.jcw@gmail.com
 * @date 2020/5/28
 */
public class WharfData {

    private String key;

    private String data;

    private boolean needMerge;

    private final Map<String, String> tags = new HashMap<>();

    /**
     * Wharf Data
     * key : such as the key of kafka message key.
     *
     * @param key
     * @param data
     */
    public WharfData(String key, String data) {
        this.key = key;
        this.data = data;
    }

    public WharfData(String key, String data, boolean needMerge) {
        this.key = key;
        this.data = data;
        this.needMerge = needMerge;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public boolean isNeedMerge() {
        return needMerge;
    }

    public void setNeedMerge(boolean needMerge) {
        this.needMerge = needMerge;
    }

    public void addTags(Map<String, String> tags) {
        this.tags.putAll(tags);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
