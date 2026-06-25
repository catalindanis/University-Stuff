import { useState } from "react";
import "./style.css"
import { saveConfiguration} from "../../api/ConfigurationsService.js";

function Configurations() {
    const [category, setCategory] = useState("");
    const [answer, setAnswer] = useState("");
    const [numberOfPoints, setNumberOfPoints] = useState("");
    const [statusMessage, setStatusMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const points = parseInt(numberOfPoints);
        if (isNaN(points) || points <= 0) {
            setStatusMessage("Number of points must be an integer greater than 0");
            return;
        }

        saveConfiguration(category, answer, points).then(() => {
            setStatusMessage("Success");
        }).catch((error) => {
            setStatusMessage(error.message);
        });
    }

    return (
        <div>
            <h1>Answers</h1>
            <form onSubmit={handleSubmit}>
                <input type={"text"} placeholder={"Category"} value={category} onChange={(e) => {setCategory(e.target.value)}}></input>
                <input type={"text"} placeholder={"Answer"} value={answer} onChange={(e) => {setAnswer(e.target.value)}}></input>
                <input type={"text"} placeholder={"Number of points"} value={numberOfPoints} onChange={(e) => {setNumberOfPoints(e.target.value)}}></input>
                <button type={"submit"}>Add answer</button>
            </form>
            {statusMessage.length > 0 && <h4>{statusMessage}</h4>}
        </div>
    );
}

export default Configurations;