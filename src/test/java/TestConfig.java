import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({
        "system:properties", // Чтение из системных свойств (например, -DbaseUrl=...)
        "classpath:test.properties", // Чтение из файла test.properties в classpath (src/test/resources)
        "classpath:default.properties" // Значения по умолчанию из файла default.properties
})
public interface TestConfig extends Config {

    @Key("test.base.url")
    @DefaultValue("https://otus.home.kartushin.su/training.html") // Значение по умолчанию
    String baseUrl();
}