using System;

namespace FestivalMuzicaCSharp.Domain;

[Serializable]
public record ShowFilter(
    string? ArtistName,
    DateOnly? Date,
    string? Location,
    int? RemainingSeats
);
