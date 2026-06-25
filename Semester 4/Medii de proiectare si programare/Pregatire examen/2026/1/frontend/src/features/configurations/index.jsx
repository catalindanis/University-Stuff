import { useState } from "react";
import "./style.css"
import { saveConfiguration} from "../../api/ConfigurationsService.js";

function Configurations() {
    const [numberOfPlayers, setNumberOfPlayers] = useState("");
    const [points, setPoints] = useState("");
    const [statusMessage, setStatusMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const numPlayers = parseInt(numberOfPlayers);
        if (isNaN(numPlayers) || numPlayers <= 0) {
            setStatusMessage("Number of players must be an integer greater than 0");
            return;
        }

        const pointsArray = points.split(",").map(p => parseInt(p.trim()));
        if (pointsArray.some(isNaN)) {
            setStatusMessage("Points must be a comma separated list of integers");
            return;
        }

        saveConfiguration(numPlayers, pointsArray).then(() => {
            setStatusMessage("Success");
        }).catch((error) => {
            setStatusMessage(error.message);
        });
    }

    return (
        <div>
            <h1>Configurations</h1>
            <form onSubmit={handleSubmit}>
                <input type={"text"} placeholder={"Number of players"} value={numberOfPlayers} onChange={(e) => {setNumberOfPlayers(e.target.value)}}></input>
                <input type={"text"} placeholder={"Points (comma separated)"} value={points} onChange={(e) => {setPoints(e.target.value)}}></input>
                <button type={"submit"}>Add configuration</button>
            </form>
            {statusMessage.length > 0 && <h4>{statusMessage}</h4>}
        </div>
    );
}

export default Configurations;