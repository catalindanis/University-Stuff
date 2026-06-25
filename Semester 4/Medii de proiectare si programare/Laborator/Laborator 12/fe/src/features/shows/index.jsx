import { useState, useEffect } from 'react';
import './style.css';
import Button from "../../components/Button";
import ShowModal from "../../components/ShowModal";
import TextInput from "../../components/TextInput";
import { fetchShows, deleteShow } from '../../api/ShowsService';
import {isAuthenticated, logout} from '../../api/AuthService';
import { connectShowsTopic, disconnectShowsTopic } from '../../api/ShowsSocketService';

function Shows() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalShowId, setModalShowId] = useState(null);
    const [shows, setShows] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [filters, setFilters] = useState({
        artistName: "",
        date: "",
        location: "",
        numberOfSeats: ""
    });

    const handleOpenModal = () => {
        setModalShowId(null);
        setIsModalOpen(true);
    };

    const handleEdit = (id) => {
        setModalShowId(id);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setModalShowId(null);
        handleApplyFilters();
    };

    const loadShows = async () => {
        setLoading(true);
        setError("");
        try {
            const data = await fetchShows();
            setShows(data || []);
        } catch (err) {
            console.error('Failed to load shows', err);
            setError('Unable to load shows.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadShows();
    }, []);

    useEffect(() => {
        if(isAuthenticated()) {
            connectShowsTopic(() => {
                window.location.reload();
            });
        }

        return () => {
            disconnectShowsTopic();
        };
    }, []);

    const handleDelete = async (id) => {
        const confirmed = window.confirm('Are you sure you want to delete this show?');
        if (!confirmed) return;

        try {
            await deleteShow(id);
            setShows((prev) => prev.filter((s) => s.id !== id));
        } catch (err) {
            console.error('Failed to delete show', err);
            window.alert('Failed to delete show. Please try again.');
        }
    };

    const handleFilterChange = (field) => (e) => {
        const value = e.target.value;
        setFilters((prev) => ({
            ...prev,
            [field]: value
        }));
    };

    const handleApplyFilters = async () => {
        setLoading(true);
        setError("");
        try {
            const filterParams = {
                artistName: filters.artistName.trim() || undefined,
                date: filters.date || undefined,
                location: filters.location.trim() || undefined,
                numberOfSeats: filters.numberOfSeats ? Number(filters.numberOfSeats) : undefined
            };

            const data = await fetchShows(filterParams);
            setShows(data || []);
        } catch (err) {
            console.error('Failed to load shows', err);
            setError('Unable to load shows.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container">
            <div className="header">
                <span className="title">Shows</span>
                {isAuthenticated() &&
                    <div className="header-actions">
                        <Button onClick={() => { logout(); window.location.reload(); }} className="logout">Logout</Button>
                    </div>
                }
            </div>

            <div className="body">
                {isAuthenticated() &&
                    <div className="toolbar">
                        <Button onClick={handleOpenModal}>Add Show</Button>
                    </div>
                }

                <div className="filter-section">
                    <TextInput
                        label="Artist Name"
                        value={filters.artistName}
                        onChange={handleFilterChange("artistName")}
                        placeholder="Filter by artist"
                    />
                    <TextInput
                        label="Date"
                        value={filters.date}
                        onChange={handleFilterChange("date")}
                        type="date"
                    />
                    <TextInput
                        label="Location"
                        value={filters.location}
                        onChange={handleFilterChange("location")}
                        placeholder="Filter by location"
                    />
                    <TextInput
                        label="Remaining Seats"
                        value={filters.numberOfSeats}
                        onChange={handleFilterChange("numberOfSeats")}
                        type="number"
                        placeholder="Filter by seats"
                    />
                    <Button onClick={handleApplyFilters} className="filter-btn">Filter</Button>
                </div>

                {loading ? (
                    <div className="loading">Loading shows…</div>
                ) : error ? (
                    <div className="error">{error}</div>
                ) : (
                    <div className="table-wrapper">
                        <table className="shows-table">
                            <thead>
                                <tr>
                                    <th>Artist Name</th>
                                    <th>Date</th>
                                    <th>Location</th>
                                    <th>Remaining Seats</th>
                                    {isAuthenticated() && <th>Actions</th>}
                                </tr>
                            </thead>
                            <tbody>
                                {shows.map((show) => (
                                    <tr key={show.id}>
                                        <td>{show.artistName}</td>
                                        <td>{show.date}</td>
                                        <td>{show.location}</td>
                                        <td>{show.remainingSeats}</td>
                                        {isAuthenticated() &&
                                            <td className="actions">
                                                <Button onClick={() => handleEdit(show.id)} className="edit">Edit</Button>
                                                <Button onClick={() => handleDelete(show.id)} className="delete">Delete</Button>
                                            </td>
                                        }
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        {shows.length === 0 && <div className="empty">No shows available.</div>}
                    </div>
                )}
            </div>

            <ShowModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                showId={modalShowId}
            />
        </div>
    );
}

export default Shows;