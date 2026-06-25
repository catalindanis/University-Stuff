import "./style.css";
import {isAuthenticated} from "../../api/AuthenticationService.js";
import {useNavigate} from "react-router-dom";
import {useState, useEffect} from "react";
import {fetchConfigurations} from "../../api/ConfigurationsService.js";
import useUpdates from "../../ws/webSocket.js";

function Home() {
    const navigate = useNavigate();
    const [configurations, setConfigurations] = useState([]);
    const { privateMessages, messages } = useUpdates();

    useEffect(() => {
        if(!isAuthenticated()) {
            navigate("/login");
        }

        console.log("WS Message: ", messages.at(messages.length - 1))
        fetchConfigurations().then((response) => {
            setConfigurations(response);
        }).catch((error) => {
            console.error("Failed to fetch configurations:", error);
        });
    }, [messages]);

    useEffect(() => {
        console.log("WS Private message: ", privateMessages.at(messages.length - 1))
    }, [privateMessages]);

    return (
        <div>
            <h1>Configurations list</h1>
            <ul>
                {configurations.map((config) => (
                    <li key={config.id}>
                        <h3>Number of players: {config.numberOfPlayers}</h3>
                        <h2>Points: [{config.points.join(', ')}]</h2>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Home;