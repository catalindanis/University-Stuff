import { useCallback, useEffect, useState } from "react";
import "./modal.css";
import TextInput from "./TextInput";
import Button from "./Button";
import { fetchShowById, createShow, updateShow } from "../api/ShowsService";

const initialFormData = {
    artistName: "",
    date: "",
    location: "",
    remainingSeats: ""
};

function ShowModal({ isOpen, onClose, showId = null }) {
    const [formData, setFormData] = useState(initialFormData);
    const [fieldErrors, setFieldErrors] = useState({});
    const [formError, setFormError] = useState("");

    const isEditMode = Boolean(showId);

    const initFormData = useCallback(() => {
        fetchShowById(showId)
            .then((data) => {
                setFormData({
                    artistName: data.artistName ?? "",
                    date: data.date ?? "",
                    location: data.location ?? "",
                    remainingSeats: data.remainingSeats ?? ""
                });
                setFieldErrors({});
                setFormError("");
            })
            .catch((error) => {
                console.error("Error fetching show data:", error);
                setFormError("Unable to load show data. Please try again.");
                setFormData(initialFormData);
            });
    }, [showId]);

    useEffect(() => {
        if (!isOpen) {
            resetModalState();
            return;
        }

        if (!showId) {
            resetModalState();
            return;
        }

        initFormData();
    }, [isOpen, showId, initFormData]);

    const resetModalState = () => {
        setFormData(initialFormData);
        setFieldErrors({});
        setFormError("");
    };

    const handleInputChange = (field) => (e) => {
        const value = e.target.value;

        setFormData((previous) => ({
            ...previous,
            [field]: value
        }));

        setFieldErrors((previous) => ({
            ...previous,
            [field]: ""
        }));

        if (formError) {
            setFormError("");
        }
    };

    const validateForm = () => {
        const nextErrors = {};

        if (!formData.artistName.trim()) {
            nextErrors.artistName = "Artist name is required.";
        }

        if (!formData.date) {
            nextErrors.date = "Date is required.";
        }

        if (!formData.location.trim()) {
            nextErrors.location = "Location is required.";
        }

        const seatsValue = Number(formData.remainingSeats);
        if (formData.remainingSeats === "") {
            nextErrors.remainingSeats = "Remaining seats are required.";
        } else if (!Number.isInteger(seatsValue) || seatsValue <= 0) {
            nextErrors.remainingSeats = "Remaining seats must be a positive integer number.";
        }

        return nextErrors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const nextErrors = validateForm();
        setFieldErrors(nextErrors);

        if (Object.keys(nextErrors).length > 0) {
            setFormError("Please fix the highlighted fields before saving.");
            return;
        }

        const payload = {
            artistName: formData.artistName.trim(),
            date: formData.date,
            location: formData.location.trim(),
            remainingSeats: Number(formData.remainingSeats)
        };

        try {
            if (isEditMode) {
                await updateShow(showId, payload);
            } else {
                await createShow(payload);
            }

            resetModalState();
            onClose();
        } catch (error) {
            console.error(isEditMode ? "Error updating show:" : "Error creating show:", error);
            setFormError(error?.message || "Something went wrong while saving the show.");
        }
    };

    const handleReset = () => {
        resetModalState();
        onClose();
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={handleReset}>
            <div className="modal-container" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2 className="modal-title">{isEditMode ? "Update show" : "Create new show"}</h2>
                    <button type="button" className="modal-close" onClick={handleReset}>×</button>
                </div>

                <form className="modal-form" onSubmit={handleSubmit} noValidate>
                    {formError ? (
                        <div className="modal-error" role="alert">
                            {formError}
                        </div>
                    ) : null}

                    <div className="form-grid">
                        <TextInput
                            label="Artist Name"
                            value={formData.artistName}
                            onChange={handleInputChange("artistName")}
                            placeholder="Enter artist name"
                            required
                            error={fieldErrors.artistName}
                        />

                        <TextInput
                            label="Date"
                            value={formData.date}
                            onChange={handleInputChange("date")}
                            type="date"
                            required
                            error={fieldErrors.date}
                        />

                        <TextInput
                            label="Location"
                            value={formData.location}
                            onChange={handleInputChange("location")}
                            placeholder="Enter location"
                            required
                            error={fieldErrors.location}
                        />

                        <TextInput
                            label="Remaining Seats"
                            value={formData.remainingSeats}
                            onChange={handleInputChange("remainingSeats")}
                            type="number"
                            placeholder="Enter number of seats"
                            required
                            error={fieldErrors.remainingSeats}
                        />
                    </div>

                    <div className="modal-actions">
                        <Button onClick={handleReset} className="btn-cancel" type="button">
                            Cancel
                        </Button>
                        <button type="submit" className="btn-submit">
                            {isEditMode ? "Update Show" : "Create Show"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default ShowModal;

