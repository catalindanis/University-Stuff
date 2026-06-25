import "./button.css"

function Button({ onClick, children, disabled, className = "", type = "button" }) {
    return (
        <button onClick={onClick} className={`custom-button${className ? ` ${className}` : ""}`} disabled={disabled} type={type}>
            {children}
        </button>
    );
}

export default Button;