package jakbenimble.data;

import javax.sql.DataSource;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakbenimble.spi.Configurable;
import query4j.Jdbc;

@ApplicationScoped
public class JakBeNimbleDataSource implements Configurable {

	DataSource ds;
	Config config;

	@PostConstruct
	public void init() {
		config = ConfigProvider.getConfig();
		HikariConfig hkConfig = new HikariConfig();
		hkConfig.setJdbcUrl(config.getValue(namespace() + "jdbc.url", String.class));
		hkConfig.setUsername(config.getValue(namespace() + "jdbc.user", String.class));
		hkConfig.setPassword(config.getValue(namespace() + "jdbc.pass", String.class));
		hkConfig.setMaximumPoolSize(config.getOptionalValue(namespace() + "jdbc.pool_size", Integer.class).orElse(5));
		ds = new HikariDataSource(hkConfig);
	}

	@Produces
	@ApplicationScoped
	public Jdbc jdbc() {
		return new Jdbc(ds);
	}

	@Override
	public String namespace() {
		return "jbn.data.";
	}
}
