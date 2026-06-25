package ro.mpp2026.festivalmuzicajavafx.network;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class OkResponse implements Response{
    private final Map<String, Object> data;

    public OkResponse() {
        data = new HashMap<>();
    }

    public OkResponse(Map<String, Object> data) {
        this.data = data;
    }

    public void addData(String key, Object value) { this.data.put(key, value); }
}


