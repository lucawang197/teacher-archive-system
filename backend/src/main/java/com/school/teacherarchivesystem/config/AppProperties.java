package com.school.teacherarchivesystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Jwt jwt = new Jwt();
    private Security security = new Security();
    private Storage storage = new Storage();
    private Meta meta = new Meta();

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }
    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }
    public Storage getStorage() { return storage; }
    public void setStorage(Storage storage) { this.storage = storage; }
    public Meta getMeta() { return meta; }
    public void setMeta(Meta meta) { this.meta = meta; }

    public static class Jwt {
        private String secret = "teacher-archive-system-demo-secret-key-2026";
        private long expirationSeconds = 7200;
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getExpirationSeconds() { return expirationSeconds; }
        public void setExpirationSeconds(long expirationSeconds) { this.expirationSeconds = expirationSeconds; }
    }

    public static class Security {
        private int maxFailedLogin = 5;
        private int lockMinutes = 60;
        public int getMaxFailedLogin() { return maxFailedLogin; }
        public void setMaxFailedLogin(int maxFailedLogin) { this.maxFailedLogin = maxFailedLogin; }
        public int getLockMinutes() { return lockMinutes; }
        public void setLockMinutes(int lockMinutes) { this.lockMinutes = lockMinutes; }
    }

    public static class Storage {
        private String basePath = "./storage/uploads";
        private String backupPath = "./storage/backups";
        public String getBasePath() { return basePath; }
        public void setBasePath(String basePath) { this.basePath = basePath; }
        public String getBackupPath() { return backupPath; }
        public void setBackupPath(String backupPath) { this.backupPath = backupPath; }
    }

    public static class Meta {
        private String name = "teacher-archive-system";
        private String version = "1.0.0";
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}
