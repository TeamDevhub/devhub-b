package teamdevhub.devhub.fake.pure.application.port.out.file;

import teamdevhub.devhub.core.file.port.out.FileStorage;

import java.util.HashMap;
import java.util.Map;

public class FakeFileStorage implements FileStorage {

    private final Map<String, byte[]> storage = new HashMap<>();

    @Override
    public String save(String fileGuid, byte[] content) {
        return "";
    }

    @Override
    public byte[] read(String fileGuid) {
        return new byte[0];
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
