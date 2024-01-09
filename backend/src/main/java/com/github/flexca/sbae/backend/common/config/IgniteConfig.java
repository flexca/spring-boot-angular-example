package com.github.flexca.sbae.backend.common.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.kubernetes.configuration.KubernetesConnectionConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class IgniteConfig {

    public static final String GENERIC_CACHE_NAME = "generic-cache";

    @Value("${ignite.kubernetes.namespace:''}")
    private String namespace;

    @Value("${ignite.kubernetes.servicename:''}")
    private String serviceName;

    @Bean
    public Ignite ignite(IgniteConfiguration igniteConfiguration) {
        return Ignition.start(igniteConfiguration);
    }

    @Bean
    public IgniteConfiguration igniteConfiguration(TcpDiscoverySpi tcpDiscoverySpi) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
        igniteConfiguration.setMetricsLogFrequency(0);
        return igniteConfiguration;
    }

    @Bean
    @Profile("!local")
    public TcpDiscoverySpi tcpDiscoverySpiKubernetes() {
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryKubernetesIpFinder());
        return tcpDiscoverySpi;
    }

    @Bean
    @Profile("!local")
    public TcpDiscoveryKubernetesIpFinder tcpDiscoveryKubernetesIpFinder() {
        KubernetesConnectionConfiguration kubernetesConnectionConfiguration = new KubernetesConnectionConfiguration();
        kubernetesConnectionConfiguration.setNamespace(namespace);
        kubernetesConnectionConfiguration.setServiceName(serviceName);
        return new TcpDiscoveryKubernetesIpFinder(kubernetesConnectionConfiguration);
    }

    @Bean
    @Profile("local")
    public TcpDiscoverySpi tcpDiscoverySpiLocal() {
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
        tcpDiscoveryMulticastIpFinder.setLocalAddress("127.0.0.1");
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryMulticastIpFinder);
        return tcpDiscoverySpi;
    }

    @Bean
    @Qualifier(GENERIC_CACHE_NAME)
    public IgniteCache<String, Object> cacheConfig(Ignite ignite) {

        CacheConfiguration<String, Object> cacheConfig = new CacheConfiguration<>(GENERIC_CACHE_NAME);
        cacheConfig.setCacheMode(CacheMode.REPLICATED);
        return ignite.getOrCreateCache(cacheConfig);
    }
}
