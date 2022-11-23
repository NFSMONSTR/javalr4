package classloader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CustomClassLoader extends ClassLoader {

    private byte[] loadClassBytes(String name) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(name.replace(".", "/") + ".class");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int length = 0;

        try {
            assert in != null;
            length = in.read();
            while (length != -1) {
                stream.write(length);
                length = in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    @Override
    public Class<?> findClass(String name) {
        byte[] b = loadClassBytes(name);
        return defineClass(name, b, 0, b.length);
    }
}