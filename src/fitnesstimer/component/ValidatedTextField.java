/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author Dani
 */
public class ValidatedTextField {

    private static final String CSS_ERROR_CLASS = "input-error";

    private final TextInputControl field;
    private final Labeled errorLabel;

    private boolean isError;
    private List<TextFieldValidator> validators;


    public ValidatedTextField(TextInputControl field, Labeled errorLabel) {
        this(field, errorLabel, 0);
    }

    public ValidatedTextField(TextInputControl field, Labeled errorLabel, int maxLength) {
        this(field, errorLabel, maxLength, true);
    }

    public ValidatedTextField(TextInputControl field, Labeled errorLabel, int maxLength, boolean autoValidate) {
        this.field = field;
        this.errorLabel = errorLabel;
        this.validators = new ArrayList<>();

        if (maxLength > 0) {
            field.textProperty().addListener(new TextLimiterListener(field, maxLength));
        }
        if (autoValidate) {
            field.focusedProperty().addListener((val, oldVal, newVal) -> {
                if (oldVal && !newVal) {  // exited focus
                    validate();
                }
            });
        }
    }

    public TextInputControl getField() {
        return field;
    }

    public Labeled getErrorLabel() {
        return errorLabel;
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String text) {
        field.setText(text);
    }

    public void setEditable(boolean editable) {
        field.setEditable(editable);
    }

    public boolean isError() {
        return isError;
    }

    public void addValidator(TextFieldValidator validator) {
        this.validators.add(validator);
    }

    public boolean validate() {
        return validate(true);
    }

    public boolean validate(boolean setErrorOnFail) {
        String error = null;
        for (TextFieldValidator validator : validators) {
            error = validator.apply(getText());
            if (error != null) {
                break;
            }
        }
        if (error != null) {
            if (setErrorOnFail) {
                setError(error);
            }
            return false;
        } else {
            setClear();
            return true;
        }
    }

    public void setError(String message) {
        isError = true;
        field.getStyleClass().add(CSS_ERROR_CLASS);
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }

    public void setClear() {
        isError = false;
        field.getStyleClass().remove(CSS_ERROR_CLASS);
        errorLabel.setVisible(false);
    }

    @FunctionalInterface
    public interface TextFieldValidator extends Function<String, String> {}
}
