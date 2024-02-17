package cn.xor7.iseeyou;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;

/**
 * @author MC_XiaoHei
 */
public class TomlEx<T> extends Toml {
    private final File file;
    private final TomlWriter tomlWriter = new TomlWriter();
    private final Class<T> clazz;
    public T data;

    public TomlEx(String filePath, Class<T> clazz) {
        super();
        this.clazz = clazz;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                if (!file.createNewFile()) {
                    throw new IOException("Can not create file: " + filePath);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        read();
        save();
    }

    public void read(){
        data = this.read(file).to(clazz);
    }

    public void save() {
        try {
            tomlWriter.write(data, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
