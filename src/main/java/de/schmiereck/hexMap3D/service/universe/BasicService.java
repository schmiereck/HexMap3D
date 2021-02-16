package de.schmiereck.hexMap3D.service.universe;

public class BasicService {
    private static boolean useCache = false;

    public static void setUseCache(final boolean useCache) {
        BasicService.useCache = useCache;
    }

    public static boolean getUseCache() {
        return BasicService.useCache;
    }
}
