package org.plugface.core.factory;

import java.net.URL;

public interface ClassLoaderCreator {

    ClassLoader createClassLoader(URL[] jars);

}
