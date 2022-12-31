package org.arachna.jsonexporter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * @author weigo
 */
public abstract class AbstractBaseTest {
    protected String readClasspathRessource(String path) throws IOException {
        InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(path));
        StringWriter writer = new StringWriter();
        reader.transferTo(writer);

        return writer.toString();
    }
}
