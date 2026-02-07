package teamdevhub.devhub.core.file.port.out;

import java.io.InputStream;

public interface FileStorage {

    String save(String fileGuid, InputStream content);
    void delete(String fileGuid);
}
