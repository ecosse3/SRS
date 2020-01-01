package pl.edu.wat.wel.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, pl.edu.wat.wel.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, pl.edu.wat.wel.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, pl.edu.wat.wel.domain.User.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.Authority.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.User.class.getName() + ".authorities");
            createCache(cm, pl.edu.wat.wel.domain.Department.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.Department.class.getName() + ".majors");
            createCache(cm, pl.edu.wat.wel.domain.Major.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.Major.class.getName() + ".schoolGroups");
            createCache(cm, pl.edu.wat.wel.domain.SchoolGroup.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.Building.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.ClassRoom.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.ClassRoom.class.getName() + ".reservations");
            createCache(cm, pl.edu.wat.wel.domain.ClassRoom.class.getName() + ".classModels");
            createCache(cm, pl.edu.wat.wel.domain.Reservation.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.Reservation.class.getName() + ".participants");
            createCache(cm, pl.edu.wat.wel.domain.ClassHours.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.ClassHours.class.getName() + ".originalStartTimes");
            createCache(cm, pl.edu.wat.wel.domain.ClassHours.class.getName() + ".newStartTimes");
            createCache(cm, pl.edu.wat.wel.domain.ClassDuration.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.ClassDuration.class.getName() + ".reservations");
            createCache(cm, pl.edu.wat.wel.domain.ClassModel.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.ClassModel.class.getName() + ".classRooms");
            createCache(cm, pl.edu.wat.wel.domain.Building.class.getName() + ".classRoomBS");
            createCache(cm, pl.edu.wat.wel.domain.ClassRoom.class.getName() + ".reservationCS");
            createCache(cm, pl.edu.wat.wel.domain.Building.class.getName() + ".reservationBS");
            createCache(cm, pl.edu.wat.wel.domain.SchoolGroup.class.getName() + ".reservationS");
            createCache(cm, pl.edu.wat.wel.domain.Status.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.Status.class.getName() + ".reservations");
            createCache(cm, pl.edu.wat.wel.domain.Timetable.class.getName());
            createCache(cm, pl.edu.wat.wel.domain.SchoolGroup.class.getName() + ".timetables");
            createCache(cm, pl.edu.wat.wel.domain.Building.class.getName() + ".timetables");
            createCache(cm, pl.edu.wat.wel.domain.ClassRoom.class.getName() + ".timetables");
            createCache(cm, pl.edu.wat.wel.domain.ClassHours.class.getName() + ".timetables");
            createCache(cm, pl.edu.wat.wel.domain.ClassDuration.class.getName() + ".timetables");
            createCache(cm, pl.edu.wat.wel.domain.ClassHours.class.getName() + ".tt_endTimes");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
