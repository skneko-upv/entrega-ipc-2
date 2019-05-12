/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.i18n;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Dani
 */
public class I18n extends ObservableResourceFactory {
    
    public static final String RESOURCES_PATH = "fitnesstimer.resources.locales.strings";
    
    public static final Locale[] ALLOWED_LOCALES = new Locale[] {
        new Locale("en", "us"),
        new Locale("es", "es")
    };
    public static final Locale DEFAULT_LOCALE = new Locale("en", "us");
    
    private static I18n instance;
    
    public static I18n createInstance(Locale locale) {
        instance = new I18n().setLocale(locale);
        return instance;
    }
    
    public static I18n getInstanceWithDefault(Locale locale) {
        if (instance == null) {
            return createInstance(locale);
        } else {
            return instance;
        }
    }
    
    public static I18n getInstance() {
        return getInstanceWithDefault(DEFAULT_LOCALE);
    }
    
    private I18n() {}
    
    public I18n setLocale(Locale locale) {
        Locale selectedOrDefault;
        if (Arrays.asList(ALLOWED_LOCALES).contains(locale)) {
            selectedOrDefault = locale;
        } else {
            selectedOrDefault = DEFAULT_LOCALE;
        }
        
        setResources(makeResources(selectedOrDefault));
        return this;
    }
    
    private ResourceBundle makeResources(Locale locale) {
        return ResourceBundle.getBundle(RESOURCES_PATH, locale);
    }
}
