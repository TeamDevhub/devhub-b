package teamdevhub.devhub.fake.pure.application.port.out.file;

import teamdevhub.devhub.core.file.port.out.FileStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FakeFileStorage implements FileStorage {

    private final Map<String, byte[]> storage = new HashMap<>();

    @Override
    public String save(String fileGuid, InputStream content) {
        try {
            storage.put(fileGuid, content.readAllBytes());
            return "/fake/" + fileGuid;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String fileGuid) {
        storage.remove(fileGuid);
    }

    public boolean exists(String fileGuid) {
        return storage.containsKey(fileGuid);
    }

    byte[] get(String fileGuid) {
        return storage.get(fileGuid);
    }
}
