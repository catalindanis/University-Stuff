import "./style.css";
import {isAuthenticated} from "../../api/AuthenticationService.js";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {joinGame, selectInitialConfiguration, submitMove} from "../../api/PlayersService.js";
import useUpdates from "../../ws/webSocket.js";

function Home() {
    const navigate = useNavigate();
    const [gameStatus, setGameStatus] = useState(null);

    const [initialConfigurations, setInitialConfigurations] = useState({})
    const [turn, setTurn] = useState(false);

    const [gameId, setGameId] = useState(null);
    const [round, setRound] = useState(null);
    const [configuration, setConfiguration] = useState(null);

    const [lastGeneratedNumber, setLastGeneratedNumber] = useState(null);
    const [lastMovePoints, setLastMovePoints] = useState(null);
    const [lastPlayer, setLastPlayer] = useState(null);
    const [lastPlayerPosition, setLastPlayerPosition] = useState(null);

    if(!isAuthenticated()) {
        navigate("/login");
    }

    useUpdates({
        onMessage: (message) => {
            console.log("WS Message: ", message);

            if(message && message.status === "starting") {
                setGameStatus("starting");

                setInitialConfigurations(message.configurations);
            }
            else if (message && message.status === "started") {
                setGameStatus("started");

                setRound(message.round);
                setGameId(message.gameId);
                setConfiguration(message.configuration);
            }
            else if (message && message.status === "ongoing") {
                setGameStatus("ongoing");

                setRound(message.round);
                setLastGeneratedNumber(message.lastGeneratedNumber);
                setLastMovePoints(message.lastMovePoints);
                setLastPlayer(message.lastPlayer);
                setLastPlayerPosition(message.lastPlayerPosition);
            }
        },
        onPrivateMessage: (message) => {
            console.log("WS Private message: ", message);

            if(message && message.turn === "true") {
                setTurn(true);
            }
        },
        onConnect: () => {
            setGameStatus(null);
            joinGame().then((response) => {
                console.log("Joined game: ", response);
            }).catch((error) => {
                console.error("Failed to join game: ", error);
            });
        },
        onDisconnect: () => {
            console.log("Disconnected from WebSocket");
        }
    });

    const handleInitialConfigurationSubmit = (key, value) => {
        console.log("Selected configuration: ", key, value);

        selectInitialConfiguration(value.id).then((response) => {
            console.log("Selected configuration response: ", response);
            setTurn(false);
        }).catch((error) => {
            console.error("Failed to select initial configuration: ", error);
        });
    }

    const handleMoveSubmit = () => {
        const generatedNumber = Math.floor(Math.random() * configuration.length);

        submitMove(gameId, generatedNumber, round).then((response) => {
            console.log("Submitted move response: ", response);
            setTurn(false);
        }).catch((error) => {
            console.error("Failed to submit move: ", error);
        });
    }

    if(gameStatus === null) {
        return (
            <div>
                <h1>Asteptam jucatori...</h1>
            </div>
        );
    }
    else if(gameStatus === "starting") {
        return (
            <div>
                <h1>Optiuni initiale:</h1>
                <div className={"configuration-list"}>
                    {initialConfigurations && Object.entries(initialConfigurations).map(([key, value]) => (
                        <button className={"configuration-option"}
                                onClick={() => handleInitialConfigurationSubmit(key, value)}
                                key={key}
                                disabled={!turn}>
                            {value.points.join(", ")}
                        </button>
                    ))}
                </div>
            </div>
        );
    }
    else if(gameStatus === "started") {
        return (
            <div>
                <h1>Runda {round}</h1>
                <p>Configuratia: {configuration && configuration.join(", ")}</p>
                <button disabled={!turn} onClick={() => {handleMoveSubmit()}}>
                    Genereaza urmatoarea mutare
                </button>
            </div>
        );
    }
    else if(gameStatus === "ongoing") {
        return (
            <div>
                <h1>Runda {round}</h1>
                <p>Configuratia: {configuration && configuration.join(", ")}</p>
                <button disabled={!turn} onClick={() => {handleMoveSubmit()}}>
                    Genereaza urmatoarea mutare
                </button>
                <div className={"last-move-info"}>
                    <p>Ultima mutare: {lastGeneratedNumber}</p>
                    <p>Puncte obtinute: {lastMovePoints}</p>
                    <p>Jucatorul care a facut ultima mutare: {lastPlayer}</p>
                    <p>Pozitia jucatorului care a facut ultima mutare: {lastPlayerPosition}</p>
                </div>
            </div>
        );
    }
}

export default Home;