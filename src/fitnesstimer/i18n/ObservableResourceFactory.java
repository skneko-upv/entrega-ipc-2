/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.i18n;

import java.util.ResourceBundle;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Labeled;

/**
 *
 * @author Dani
 */
public class ObservableResourceFactory {
    
    private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
    
    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }
    
    public final ResourceBundle getResources() {
        return resourcesProperty().get();
    }
    
    public final void setResources(ResourceBundle resources) {
        resourcesProperty().set(resources);
    }
    
    public StringBinding getStringBinding(String key, Object... args) {
        return new StringBinding() {
        
            { bind(resourcesProperty()); }
    
            @Override
            public String computeValue() {
                return getString(key, args);
            }
        };
    }
    
    private String getString(String key, Object... args) {
        return String.format(getResources().getString(key), args);
    }
    
    public void bind(Labeled property, String key, Object... args) {
        property.textProperty().bind(getStringBinding(key, args));
    }
}
