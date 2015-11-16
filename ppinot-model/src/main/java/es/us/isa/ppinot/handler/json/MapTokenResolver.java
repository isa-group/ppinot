package es.us.isa.ppinot.handler.json;

import java.util.HashMap;
import java.util.Map;

/**
 * MapTokenResolver
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class MapTokenResolver implements TokenResolver {
    protected Map<String, String> tokenMap = new HashMap<String, String>();

    public MapTokenResolver(Map<String, String> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public String resolveToken(String tokenName) {
        return this.tokenMap.get(tokenName);
    }
}
