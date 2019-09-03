package com.newproj.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ProxyResourceResolver {

	public static Resource [] getResource( String ... patterns  ) {
		if( patterns == null || patterns.length == 0 ){
			throw new IllegalArgumentException("resource pattern is null !") ;
		}
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver() ;
		List<Resource> resourceList = new ArrayList<Resource>() ;
		for( String pattern : patterns ){
			try {
				Resource [] resources = resourceResolver.getResources( pattern ) ;
				if( resources == null || resources.length == 0 ) continue ;
				for( Resource resource : resources ){
					resourceList.add( resource ) ;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Resource [] arrays = new Resource[ resourceList.size() ] ;
		for( int i = 0 ; i < resourceList.size() ; i ++ ){
			arrays[ i ] = resourceList.get( i ) ;
		}
		return arrays ;
	}
	
}
