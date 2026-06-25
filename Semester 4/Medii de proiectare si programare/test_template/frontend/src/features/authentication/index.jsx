import "./style.css"
import {useState} from "react";
import {getUsernameFromToken, login} from "../../api/AuthenticationService.js";
import { useNavigate } from 'react-router-dom';

function Authentication() {
    const [nickname, setNickname] = useState("");
    const [statusMessage, setStatusMessage] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (nickname.trim() === "") {
            setStatusMessage("Nickname cannot be empty.");
            return;
        }

        login(nickname).then(() => {
            navigate("/home");
            console.log("USERNAME: ", getUsernameFromToken());
        }).catch((error) => {
            setStatusMessage(error.message);
        });
    }

    return (
        <div>
            <h1>Authentication</h1>
            <form onSubmit={handleSubmit}>
                <input type={"text"} placeholder={"Your nickname"} value={nickname} onChange={(e) => {setNickname(e.target.value)}}></input>
                <button type={"submit"}>Login</button>
            </form>
            {statusMessage.length > 0 && <h4>{statusMessage}</h4>}
        </div>
    );
}

export default Authentication;