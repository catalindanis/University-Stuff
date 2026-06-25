import "./input.css";

function TextInput({ label, value, onChange, placeholder, type = "text", required = false, error = "" }) {
    return (
        <div className="input-group">
            <label className="input-label">{label}</label>
            <input
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                className={`input-field${error ? " input-field-error" : ""}`}
                required={required}
                aria-invalid={Boolean(error)}
                aria-describedby={error ? `${label.toLowerCase().replace(/\s+/g, "-")}-error` : undefined}
            />
            {error ? (
                <span className="input-error" id={`${label.toLowerCase().replace(/\s+/g, "-")}-error`}>
                    {error}
                </span>
            ) : null}
        </div>
    );
}

export default TextInput;

