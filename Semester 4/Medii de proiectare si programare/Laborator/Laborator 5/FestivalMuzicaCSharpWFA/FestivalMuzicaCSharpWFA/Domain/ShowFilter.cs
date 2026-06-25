using System;

namespace FestivalMuzicaCSharp.Domain;

public record ShowFilter(
    string? ArtistName,
    DateOnly? Date,
    string? Location,
    int? RemainingSeats
);
